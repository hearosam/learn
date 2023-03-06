package com.smallrig.mall.product.service.impl;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallrig.mall.common.constant.CommonConstant;
import com.smallrig.mall.common.dto.product.GetProductInfoSkuSpecDTO;
import com.smallrig.mall.common.dto.product.ProductImageListDTO;
import com.smallrig.mall.common.enums.SiteCodeEnums;
import com.smallrig.mall.common.enums.ZoneIdEnum;
import com.smallrig.mall.common.enums.product.ProductClassValueBasicEnum;
import com.smallrig.mall.common.enums.product.ProductStatusEnum;
import com.smallrig.mall.common.enums.product.search.SearchScoreTypeEnums;
import com.smallrig.mall.common.exception.CheckException;
import com.smallrig.mall.common.request.product.info.ProductGetRequest;
import com.smallrig.mall.common.request.product.search.CategorySearchReq;
import com.smallrig.mall.common.request.product.search.CouponSearchReq;
import com.smallrig.mall.common.response.product.info.GetProductLabelResponse;
import com.smallrig.mall.common.response.product.info.ProductResp;
import com.smallrig.mall.common.response.product.search.vo.ProductLabelVO;
import com.smallrig.mall.common.response.product.search.vo.ProductSearchOtherInfoVO;
import com.smallrig.mall.common.response.product.search.vo.SelectVO;
import com.smallrig.mall.common.utils.TimeUtil;
import com.smallrig.mall.product.config.TransactionCommitHandler;
import com.smallrig.mall.product.entity.*;
import com.smallrig.mall.product.mapper.ProductSearchMapper;
import com.smallrig.mall.product.search.dto.ProductSearchDTO;
import com.smallrig.mall.product.search.dto.SyncSearchCacheStockDTO;
import com.smallrig.mall.product.service.*;
import com.smallrig.mall.product.utils.SearchingAlgorithmUtil;
import com.smallrig.mall.product.word.Word;
import com.smallrig.mall.product.word.WordSegmented;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局搜索表 服务实现类
 * </p>
 *
 * @author chenyujun
 * @since 2021-11-16
 */
@Slf4j
@Service
public class ProductSearchServiceImpl extends ServiceImpl<ProductSearchMapper, ProductSearch> implements IProductSearchService {

    @Resource
    private IProductService iProductService;
    @Resource
    private IProductAttributeService iProductAttributeService;
    @Resource
    private IProductSpecService iProductSpecService;
    @Resource
    private IProductCacheService productCacheService;
    @Resource
    private IProductStockService productStockService;
    @Resource
    private MapperFacade mapperFacade;
    @Resource
    private IProductLabelRelationService labelRelationService;
    @Resource
    private IProductLabelService labelService;

    @Autowired
    private TransactionCommitHandler transactionCommitHandler;

    private final static Map<String, String> BRAND_AND_MODEL = new HashMap<String,String>(){{
        put(SiteCodeEnums.ZH_CN.getCode()+"brand","内部品牌");
        put(SiteCodeEnums.ZH_CN.getCode()+"su_brand","适用品牌");
        put(SiteCodeEnums.ZH_CN.getCode()+"model","适用型号");
        put(SiteCodeEnums.KO_KR.getCode()+"brand","내부 브랜드");
        put(SiteCodeEnums.KO_KR.getCode()+"su_brand","해당 브랜드");
        put(SiteCodeEnums.KO_KR.getCode()+"model","적용 모델");
        put(SiteCodeEnums.EN_US.getCode()+"brand","Brands");
        put(SiteCodeEnums.EN_US.getCode()+"su_brand","For Brands");
        put(SiteCodeEnums.EN_US.getCode()+"model","For Model");
        put(SiteCodeEnums.DE_DE.getCode()+"brand","Interne Marke");
        put(SiteCodeEnums.DE_DE.getCode()+"su_brand","Anwendbare Marken");
        put(SiteCodeEnums.DE_DE.getCode()+"model","Anwendbares Modell");
        put(SiteCodeEnums.JA_JP.getCode()+"brand","社内ブランド");
        put(SiteCodeEnums.JA_JP.getCode()+"su_brand","該当するブランド");
        put(SiteCodeEnums.JA_JP.getCode()+"model","該当モデル");
    }};

    private final static Map<String, String> CATEGORY_NAME = new HashMap<String,String>(){{
        put(SiteCodeEnums.ZH_CN.getCode(),"类目");
        put(SiteCodeEnums.KO_KR.getCode(),"범주");
        put(SiteCodeEnums.EN_US.getCode(),"Category");
        put(SiteCodeEnums.DE_DE.getCode(),"Kategorie");
        put(SiteCodeEnums.JA_JP.getCode(),"カテゴリー");
    }};

    /**
     * 更新搜索商品是否有库存接口
     */
    @Override
    public void updateStockStatus(String prdCode){
        try {
            ProductSearchDTO prdCache = productCacheService.getPrdCacheByPrdCode(prdCode);
            if (prdCache == null ) {
                throw new CheckException("商品缓存数据不一致prdCode: "+prdCode);
            }
            if(CommonConstant.STATUS_YES.equals(prdCache.getOverSold())){
                //允许超售商品不判断库存
                return;
            }
            //库存4个站点共享
            int status = getSpuStockStatus(prdCode);
            //无库存--->有库存，有库存--->无库存
            if(!prdCache.getStockStatus().equals(status)) {
                log.info("update product：{}, stock status:{} ,start",prdCode,status);
                //售罄商品不展示
                //更新缓存，同时更新数据库
                boolean update = lambdaUpdate().setSql("stock_status=" + status)
                        .eq(ProductSearch::getPrdCode, prdCode)
                        .update();
                log.info("update product：{}, stock status:{},end:{}",prdCode,status,update);
                //更新缓存
                productCacheService.updatePrdCacheStockStatus(prdCode,status,true);
            }
        }catch (Exception e) {
            log.error("更新搜索缓存库存状态失败",e);
        }
    }

    /**
     * 根据productCode统计spu库存数量返回库存状态
     * @param prdCode productCode
     * @return 1:在售，2:售罄
     */
    private int getSpuStockStatus(String prdCode) {
        //获取spu所有库存,包括活动库存
        Set<String> set = new HashSet<>();
        set.add(prdCode);
        Map<String, Long> spuStockSet = productStockService.getSpuStockListGroupByPrdCode(set);
        Long spuTotalStock = spuStockSet.get(prdCode);
        return Objects.isNull(spuTotalStock) || spuTotalStock <= 0 ? CommonConstant.PRD_STOCK_STATUS_SOLD_OUT: CommonConstant.PRD_STOCK_STATUS_ON_SALE ;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeProductSearch(Long prdId, String siteCode, String productCode) {
        baseMapper.deleteById(prdId);
        productCacheService.removeCache(prdId,siteCode,true);
    }

    /**
     * 创建商品搜索表数据
     */
    @Override
    public void saveProductSearch(Long spuId){
        //设置缓存发送异步消息
        List<ProductSearchDTO> productSearchDTOList = buildProductSearchDTO(Collections.singletonList(spuId));
        if (CollectionUtils.isEmpty(productSearchDTOList)) {
            return;
        }
        ProductSearchDTO productSearchDTO = productSearchDTOList.get(0);
        ProductSearch productSearch = buildProductSearch(productSearchDTO);
        this.saveOrUpdate(productSearch);
        //设置缓存发送异步消息
        productCacheService.setCache(productSearchDTO,true);
    }

    /**
     * 构建ProductSearch持久化对象
     * @param productSearchDTO 内存倒排索引对象
     * @return ProductSearch对象
     */
    private ProductSearch buildProductSearch(ProductSearchDTO productSearchDTO) {
        ProductSearch productSearch = mapperFacade.map(productSearchDTO,ProductSearch.class);

        Set<Long> linkedPropertyIdSet = productSearchDTO.getLinkedPropertyIdSet();
        if (!linkedPropertyIdSet.isEmpty()) {
            List<Long> list = new ArrayList<>(linkedPropertyIdSet);
            String linkedPropertyIdSetStr = Strings.join(list, ',');
            productSearch.setLinkedPropertyIdSet(linkedPropertyIdSetStr);
        }
        List<SelectVO.CategoryVO> categoryList = productSearchDTO.getCategoryList();
        productSearch.setCategoryList(JSONUtil.toJsonStr(categoryList));
        List<SelectVO.OptionVO> selectOptionList = productSearchDTO.getSelectOptionList();
        productSearch.setSelectOptionList(JSONUtil.toJsonStr(selectOptionList));
        //其他信息处理
        productSearch.setOtherInfo(JSONUtil.toJsonStr(productSearchDTO.getOtherInfoVO()));
        return productSearch;
    }

    /**
     * 批量
     * @param spuIds
     */
    @Override
    public void saveOrUpdateProductSearchBatch(List<Long> spuIds) {
        if (CollectionUtils.isEmpty(spuIds)) {
            return;
        }
        List<ProductSearchDTO> productSearchDTOList = buildProductSearchDTO(spuIds);
        if (CollectionUtils.isEmpty(productSearchDTOList)) {
            return;
        }
        List<ProductSearch> collect = productSearchDTOList.parallelStream()
                .map(this::buildProductSearch)
                .collect(Collectors.toList());
        this.saveOrUpdateBatch(collect);
        //发送同步消息
        productSearchDTOList.forEach(e->productCacheService.setCache(e,true));
    }

    @Override
    public void updateProductSearchByPropertyIds(List<Long> ids, String siteCode) {
        List<ProductSearchDTO> allProductList = productCacheService.getAllProductList(siteCode);
        //商品关联的属性在属性变更id列表中存在就需要更新商品缓存
        List<Long> collect = allProductList.stream()
                .filter(e -> ProductSearchPredicate.intersect(ids, e.getLinkedPropertyIdSet()))
                .map(ProductSearchDTO::getId).collect(Collectors.toList());
        //批量更新搜索缓存
        saveOrUpdateProductSearchBatch(collect);
    }

    @Override
    public void updatePrdCacheByPrdId(String prdId){
        List<ProductSearchDTO> productSearchDTOList = buildProductSearchDTO(Collections.singletonList(Long.parseLong(prdId)));
        if (CollectionUtils.isEmpty(productSearchDTOList)) {
            return;
        }
        //设置缓存不发送异步消息
        productCacheService.setCache(productSearchDTOList.get(0),false);
    }

    /**
     * Description: 根据spuId构建倒排索引结构的商品数据
     * @param spuIdList spuId集合
     */
    private List<ProductSearchDTO> buildProductSearchDTO(List<Long> spuIdList){
        //类目
        List<ProductResp> productList = null;
        List<ProductSearchDTO> resultList = new ArrayList<>();
        try {
            ProductGetRequest request = new ProductGetRequest();
            request.setSiteCode("all");
            request.setIds(spuIdList);
            productList = iProductService.getProductByIds(request);
        }catch (Exception e) {
            log.error("商品:{},构建索引异常：",spuIdList,e);
        }
        if (CollectionUtils.isEmpty(productList)) {
            return resultList;
        }
        //构建商品缓存需要同步更新其他4个站点
        //正式构建ProductSearchDTO数据结构
        return productList.parallelStream().map(productById -> {

            if(!ProductStatusEnum.IN_SALES.getCode().equals(productById.getStatus())){
                return null;
            }

            String siteCode = productById.getSiteCode();
            //搜索词信息列表,分词后再入库，考虑异步优化
            List<String> keyWordList = new ArrayList<>();
            //关联属性id列表(实现分类搜索使用)
            Set<Long> linkedPropertyIdSet = new HashSet<>();
            //规格属性名称id列表,分类筛选使用使用
            List<SelectVO.OptionVO> selectOptionList = new ArrayList<>();
            List<SelectVO.CategoryVO> categoryList = new ArrayList<>();

            //spu
            keyWordList.add(productById.getProductCode()+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.SPU.getCode());

            //搜索词
            if (!StringUtils.isEmpty(productById.getKeyword())){
                String[] splitKey = productById.getKeyword().split(",");
                Arrays.stream(splitKey)
                        .map(s->WordSegmented.seg(s,siteCode))
                        .flatMap(Collection::stream)
                        .map(Word::getText)
                        .forEach(key-> keyWordList.add(key+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.PRD_KEYWORD.getCode()));
            }
            //标题
            WordSegmented.seg(productById.getProductName(),siteCode)
                    .stream().map(Word::getText)
                    .forEach(prdName-> keyWordList.add(prdName+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.PRD_NAME.getCode()));
            //内部品牌+后台商品标题+单规格sku
            categoryProcess(productById,keyWordList,linkedPropertyIdSet,categoryList,selectOptionList);
            branAndModelProcess(productById,keyWordList,linkedPropertyIdSet,selectOptionList);
            normalAttributeProcess(productById,keyWordList,linkedPropertyIdSet,selectOptionList);
            skuSpecsProcess(productById,keyWordList,linkedPropertyIdSet,selectOptionList);
            productLabelProcess(productById,linkedPropertyIdSet);

            ProductSearchDTO productSearchDTO = buildProductSearch(productById, keyWordList,linkedPropertyIdSet,selectOptionList,categoryList);
            //处理商品标签 -----------------------
            List<ProductLabelVO> productLabelList = labelRelationService.queryProductSearchLabelList(productById.getId());
            productSearchDTO.setProductLabelList(productLabelList);
            if(CollectionUtils.isNotEmpty(productLabelList)) {
                Set<Long> labelIdSet = productLabelList.stream().map(ProductLabelVO::getId).collect(Collectors.toSet());
                productSearchDTO.getLinkedPropertyIdSet().addAll(labelIdSet);
            }
            //处理商品标签 -----------------------
            //不允许允许超卖不判断库存
            if(!CommonConstant.STATUS_YES.equals(productById.getOverSold())) {
                //检查库存,搜索过滤售罄商品
                int status = getSpuStockStatus(productById.getProductCode());
                productSearchDTO.setStockStatus(status);
            }
            return productSearchDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 商品搜索支持标签关联id查询(标签只支持id关联，搜索结果页不需要展示分类筛选下拉选)
     * @param product
     * @param preIdSet
     */
    private void productLabelProcess(ProductResp product,Set<Long> preIdSet) {
        List<GetProductLabelResponse> productLabels = product.getProductLabels();
        if (CollectionUtils.isEmpty(productLabels)) {
            return;
        }
        Set<Long> collect = productLabels.stream().map(GetProductLabelResponse::getId).collect(Collectors.toSet());
        preIdSet.addAll(collect);

    }

    private void categoryProcess(ProductResp product,List<String> keyWordList,Set<Long> preIdSet
            ,List<SelectVO.CategoryVO> categoryList,List<SelectVO.OptionVO> selectOptionList) {
        String siteCode = product.getSiteCode();
        product.getClassInfo().forEach(getProductInfoClassInfoResponse -> {
            WordSegmented.seg(getProductInfoClassInfoResponse.getClassName(),siteCode)
                    .stream().map(Word::getText)
                    .forEach(cName-> keyWordList.add(cName+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.CATEGORY.getCode()));

            preIdSet.add(getProductInfoClassInfoResponse.getClassId());

            SelectVO.OptionVO bo = new SelectVO.OptionVO();
            bo.setId(getProductInfoClassInfoResponse.getClassId());
            bo.setName(getProductInfoClassInfoResponse.getClassName());
            bo.setSelectName(CATEGORY_NAME.get(siteCode));
            bo.setType(SearchScoreTypeEnums.CATEGORY.getCode());
            selectOptionList.add(bo);

            //处理分类下拉选
            SelectVO.CategoryVO cvo = new SelectVO.CategoryVO();
            BeanUtils.copyProperties(getProductInfoClassInfoResponse,cvo);
            categoryList.add(cvo);
        });

    }
    @Override
    public List<ProductSearchDTO> searchProduct(List<String> keywords, String siteCode, List<Long> filterFieldIdList) {
        List<ProductSearchDTO> step1Collect = baseProductSearch(siteCode,e ->keywords == null || keywords.stream().anyMatch(word -> SearchingAlgorithmUtil.sundaySearch(e.getKeyWord(), word) >= 0));
        if (CollectionUtils.isNotEmpty(filterFieldIdList)) {
            //二次过滤
            return step1Collect.stream()
                    .filter(e->e.getLinkedPropertyIdSet().containsAll(filterFieldIdList))
                    .collect(Collectors.toList());
        }
        return step1Collect;
    }

    /**
     * 商品缓存搜索入口
     * @param siteCode 站点
     * @param prdPredicate 断言
     * @return
     */
    private List<ProductSearchDTO> baseProductSearch(String siteCode,Predicate<ProductSearchDTO> prdPredicate){
        List<ProductSearchDTO> allProductList = productCacheService.getAllProductList(siteCode);
        List<ProductSearchDTO> prdCollect = allProductList.stream()
                .filter(e->e.getStockStatus().equals(CommonConstant.PRD_STOCK_STATUS_ON_SALE))
                .filter(prdPredicate)
                .collect(Collectors.toList());

        return prdCollect;
    }
    @Override
    public List<ProductSearchDTO> selectProductListByItemIds(List<CategorySearchReq> itemIdList,
                                                             List<Long> filterFieldIdList, String siteCode) {
        List<ProductSearchDTO> collect = baseProductSearch(siteCode,e -> categorySearchMatchPattern(itemIdList, e));
        if (CollectionUtils.isNotEmpty(filterFieldIdList)) {
            //二次过滤
            return collect.stream()
                    .filter(e->e.getLinkedPropertyIdSet().containsAll(filterFieldIdList))
                    .collect(Collectors.toList());
        }
        return collect;
    }

    @Override
    public Map<String, String> getProductSummaryInfo(String siteCode) {
        //查所有  e->Objects.nonNull(e.getId()) 添加一个无效的条件
        List<ProductSearchDTO> allProductList = baseProductSearch(siteCode,e->Objects.nonNull(e.getId()));
        return allProductList.parallelStream().collect(Collectors.toMap(ProductSearchDTO::getUrl,ProductSearchDTO::getFormatPrdName,(o1,o2)->o2));
    }

    @Override
    public void refreshCache(String siteCode) {
        List<ProductSearch> list = baseMapper
                .selectList(new LambdaQueryWrapper<ProductSearch>()
                        //.eq(ProductSearch::getStockStatus,CommonConstant.PRD_STOCK_STATUS_ON_SALE)
                        .eq(!StringUtils.isEmpty(siteCode),ProductSearch::getSiteCode,siteCode));
        if (CollectionUtils.isEmpty(list)) {
            productCacheService.clearPrdCacheBySiteCode(siteCode);
            return;
        }
        Map<String, List<ProductSearchDTO>> groupByList = list.stream().map(e -> {
            ProductSearchDTO productSearchDTO = new ProductSearchDTO();
            BeanUtils.copyProperties(e, productSearchDTO);
            if (!StringUtils.isEmpty(e.getLinkedPropertyIdSet())) {
                Set<Long> linkedPropertyIdSet = Arrays.stream(e.getLinkedPropertyIdSet().split(","))
                        .map(Long::parseLong).collect(Collectors.toSet());
                productSearchDTO.setLinkedPropertyIdSet(linkedPropertyIdSet);
            }

            productSearchDTO.setCategoryList(JSONUtil.toList(JSONUtil.parseArray(e.getCategoryList()), SelectVO.CategoryVO.class));
            productSearchDTO.setSelectOptionList(JSONUtil.toList(JSONUtil.parseArray(e.getSelectOptionList()), SelectVO.OptionVO.class));
            String otherInfo = e.getOtherInfo();
            if (Strings.isNotBlank(otherInfo)) {
                productSearchDTO.setOtherInfoVO(JSONUtil.toBean(otherInfo,ProductSearchOtherInfoVO.class));
            }
            return productSearchDTO;
        }).collect(Collectors.groupingBy(ProductSearchDTO::getSiteCode));

        groupByList.forEach((k,v)->{
            Map<Long, ProductSearchDTO> allProductCache = v.stream().collect(Collectors.toMap(ProductSearchDTO::getId, Function.identity()));
            //更新站点缓存
            productCacheService.refreshCache(k,allProductCache);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initPrdCacheFromProductTable(String siteCode) {
        remove(new LambdaQueryWrapper<ProductSearch>()
                .eq(!StringUtils.isEmpty(siteCode),ProductSearch::getSiteCode,siteCode));
        //清空缓存
        productCacheService.clearPrdCacheBySiteCode(siteCode);

        transactionCommitHandler
                .handle(() -> {
            //重建缓存
            List<Product> list = iProductService.lambdaQuery()
                    .select(Product::getId)
                    .eq(!StringUtils.isEmpty(siteCode),Product::getSiteCode,siteCode)
                    .eq(Product::getStatus, CommonConstant.PRD_STOCK_STATUS_ON_SALE)
                    .list();
            List<Long> collect = list.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            //批量更新搜索缓存
            saveOrUpdateProductSearchBatch(collect);
        });
    }

    @Override
    public void updatePrdCacheByPropertyIdFromDb(List<Long> idList) {
        String reg = Strings.join(idList, '|');
        List<Product> list = iProductService.lambdaQuery()
                .select(Product::getId)
                .last("and status = 1 and product_class REGEXP '" + reg + "'").list();
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> collect = list.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            //批量更新搜索缓存
            saveOrUpdateProductSearchBatch(collect);
        }
    }

    @Override
    public void updatePrdCacheByLabelIdFromDb(List<Long> idList) {
        //根据标签id查所有商品列表
        List<ProductLabelRelation> relationList = labelRelationService.queryRelationListByLabelIdList(idList);
        if (CollectionUtils.isEmpty(relationList)) {
            return;
        }
        Set<Long> prdIdSet = relationList.stream().map(ProductLabelRelation::getProductId).collect(Collectors.toSet());
        saveOrUpdateProductSearchBatch(new ArrayList<>(prdIdSet));
    }

    @Override
    public void syncSearchCacheStockStatus(List<SyncSearchCacheStockDTO> stockInfoList) {
        if (CollectionUtils.isEmpty(stockInfoList)) {
            return;
        }
        for (SyncSearchCacheStockDTO dto : stockInfoList ) {
            ProductSearchDTO prdCache = productCacheService.getPrdCacheByPrdCode(dto.getPrdCode());
            //允许超售商品不判断库存(不更新库存状态)
            if(Objects.isNull(prdCache) || CommonConstant.STATUS_YES.equals(prdCache.getOverSold())){
                continue;
            }

            int stockStatus = dto.getUsableStock() > 0 ?  CommonConstant.PRD_STOCK_STATUS_ON_SALE : CommonConstant.PRD_STOCK_STATUS_SOLD_OUT;
            productCacheService.updatePrdCacheStockStatus(dto.getPrdCode(),stockStatus,true);

            lambdaUpdate().setSql("stock_status=" + stockStatus)
                    .eq(ProductSearch::getPrdCode, dto.getPrdCode())
                    .update();
        }
    }

    @Override
    public List<ProductSearchDTO> selectProductListByCoupon(CouponSearchReq couponSearchReq, List<Long> filterFieldIdList, String siteCode) {
        List<ProductSearchDTO> collect = baseProductSearch(siteCode,e -> couponSearchMatchPattern(couponSearchReq, e));
        if (CollectionUtils.isNotEmpty(filterFieldIdList)) {
            //二次过滤
            return collect.stream()
                    .filter(e->e.getLinkedPropertyIdSet().containsAll(filterFieldIdList))
                    .collect(Collectors.toList());
        }
        return collect;
    }

    /**
     * 构建produceSearch对象
     */
    private ProductSearchDTO buildProductSearch(ProductResp product, List<String> keyWordList,
                                                Set<Long> preIdSet, List<SelectVO.OptionVO> optionList,
                                                List<SelectVO.CategoryVO> categoryList){
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        productSearchDTO.setKeyWord(String.join(CommonConstant.PRD_SEARCH_KEYWORD_DELIMITER,keyWordList));
        productSearchDTO.setId(product.getId());
        productSearchDTO.setSelectOptionList(optionList);
        productSearchDTO.setCategoryList(categoryList);
        productSearchDTO.setLinkedPropertyIdSet(preIdSet);
        productSearchDTO.setPrdName(product.getProductName());
        productSearchDTO.setSiteCode(product.getSiteCode());
        productSearchDTO.setPrdCode(product.getProductCode());
        productSearchDTO.setOverSold(product.getOverSold());
        productSearchDTO.setPrdType(product.getType());
        productSearchDTO.setStockStatus(CommonConstant.PRD_STOCK_STATUS_ON_SALE);
        productSearchDTO.setFormatPrdName(product.getShowProductName());

        //最低价格sku信息
        GetProductInfoSkuSpecDTO getProductInfoSkuSpecDTO = product.getSkuSpecs().stream()
                .min(Comparator.comparing(GetProductInfoSkuSpecDTO::getPrice)).get();

        productSearchDTO.setPrice(getProductInfoSkuSpecDTO.getPrice());

        //其他信息处理
        ProductSearchOtherInfoVO otherInfo = new ProductSearchOtherInfoVO();
        otherInfo.setListPrice(getProductInfoSkuSpecDTO.getListPrice());
        otherInfo.setAllowPurchase(product.getAllowPurchase());
        productSearchDTO.setOtherInfoVO(otherInfo);

        String mainImage = product.getProductImage()
                .stream().filter(e -> e.getType() == 1)
                .map(ProductImageListDTO::getUrl)
                .findAny().orElse("");

        productSearchDTO.setMainImage(mainImage);
        productSearchDTO.setUpTime(TimeUtil.localDateTimeToTimestamp(product.getUpTime(), ZoneIdEnum.CTT));
        productSearchDTO.setUrl(product.getUrl());
        productSearchDTO.setPreSale(product.getPreSale());
        productSearchDTO.setSort(product.getSort());
        return productSearchDTO;
    }

    private void branAndModelProcess(ProductResp product,List<String> keyWordList,Set<Long> preIdSet,List<SelectVO.OptionVO> categoryList) {
        String siteCode = product.getSiteCode();
        //品牌非必填
        if (CollectionUtils.isEmpty(product.getBasics())) {
            return;
        }
        //品牌和适用型号
        product.getBasics().forEach(getProductInfoClassBasicValueDTO -> {
            String basicsType = getProductInfoClassBasicValueDTO.getBasicsType();
            Map<Long, String> idNameMap = getProductInfoClassBasicValueDTO.getIdNameMap();

            if (!org.springframework.util.CollectionUtils.isEmpty(idNameMap)){
                idNameMap.forEach((id, name) -> {
                    int type = 0;
                    SelectVO.OptionVO bo = new SelectVO.OptionVO();
                    bo.setId(id);
                    bo.setName(name);
                    String categoryName = "";
                    if (ProductClassValueBasicEnum.BRAND.getName().equals(basicsType)){
                        categoryName = BRAND_AND_MODEL.get(siteCode + "brand");
                        type = SearchScoreTypeEnums.INNER_BRAND.getCode();
                    }
                    if (ProductClassValueBasicEnum.APPLICABLE_BRAND.getName().equals(basicsType)){
                        categoryName = BRAND_AND_MODEL.get(siteCode + "su_brand");
                        type = SearchScoreTypeEnums.OUTER_BRAND.getCode();
                    }
                    if (ProductClassValueBasicEnum.MODEL.getName().equals(basicsType)){
                        type = SearchScoreTypeEnums.SUIT_MODEL.getCode();
                        categoryName = BRAND_AND_MODEL.get(siteCode + "model");
                    }

                    bo.setSelectName(categoryName);
                    bo.setType(type);

                    final int finalType = type;
                    WordSegmented.seg(name,siteCode)
                            .stream().map(Word::getText)
                            .forEach(s-> keyWordList.add(s+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+finalType));
                    preIdSet.add(id);
                    categoryList.add(bo);
                });
            }
        });
    }

    /**
     * 普通属性处理
     */
    private void normalAttributeProcess(ProductResp product,List<String> keyWordList,Set<Long> preIdSet,List<SelectVO.OptionVO> categoryList) {
        String siteCode = product.getSiteCode();
        //普通属性
        product.getAttributes().forEach(getProductInfoClassValueDTO -> {
            //查询配置是否能够加入
            ProductAttribute byIdProductAttribute = iProductAttributeService.getById(getProductInfoClassValueDTO.getAttributeId());
            if (byIdProductAttribute!=null && CommonConstant.STATUS_YES.equals(byIdProductAttribute.getCanSearch())){
                //单文本类型属性不参与搜索
                if (Objects.nonNull(getProductInfoClassValueDTO.getIdNameMap())) {
                    getProductInfoClassValueDTO.getIdNameMap().forEach((id, name) ->{
                        SelectVO.OptionVO npBo = new SelectVO.OptionVO();
                        npBo.setId(id);
                        npBo.setName(name);
                        npBo.setIsShow(byIdProductAttribute.getIsShow());
                        npBo.setType(SearchScoreTypeEnums.NORMAL_PROPERTY.getCode());
                        npBo.setSelectName(iProductAttributeService
                                .findAttrNameById(getProductInfoClassValueDTO.getAttributeId()));
                        WordSegmented.seg(name,siteCode)
                                .stream().map(Word::getText)
                                .forEach(s-> keyWordList.add(s+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.NORMAL_PROPERTY.getCode()));
                        preIdSet.add(id);
                        categoryList.add(npBo);
                    });
                }
            }
        });
    }

    /**
     * 处理sku属性
     */
    private void skuSpecsProcess(ProductResp product,List<String> keyWordList,Set<Long> preIdSet,List<SelectVO.OptionVO> categoryList) {
        String siteCode = product.getSiteCode();
        //规格&sku
        product.getSkuSpecs().forEach(getProductInfoSkuSpecDTO -> {
            //规格
            getProductInfoSkuSpecDTO.getSkuSpecInfos().forEach(getProductInfoSkuSpecInfoDTO -> {
                ProductSpec byIdProductSpec = iProductSpecService.getById(getProductInfoSkuSpecInfoDTO.getSpecId());
                //查询配置是否能够加入
                if (byIdProductSpec!=null &&CommonConstant.STATUS_YES.equals(byIdProductSpec.getCanSearch())){

                    SelectVO.OptionVO skuSpecBo = new SelectVO.OptionVO();
                    skuSpecBo.setId(getProductInfoSkuSpecInfoDTO.getSpecValueId());
                    skuSpecBo.setName(getProductInfoSkuSpecInfoDTO.getSpecValueName());
                    skuSpecBo.setType(SearchScoreTypeEnums.SPEC_PROPERTY.getCode());
                    skuSpecBo.setSelectName(iProductSpecService
                            .findSpecName(getProductInfoSkuSpecInfoDTO.getSpecId()));
                    categoryList.add(skuSpecBo);

                    WordSegmented.seg(getProductInfoSkuSpecInfoDTO.getSpecValueName(),siteCode)
                            .stream().map(Word::getText)
                            .forEach(name-> keyWordList.add(name+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.SPEC_PROPERTY.getCode()));
                    preIdSet.add(getProductInfoSkuSpecInfoDTO.getSpecValueId());
                }
            });
            //sku
            keyWordList.add(getProductInfoSkuSpecDTO.getThirdpartySkuCode()+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.SKU.getCode());
            //upc
            if (!StringUtils.isEmpty(getProductInfoSkuSpecDTO.getUpcCode())){
                keyWordList.add(getProductInfoSkuSpecDTO.getUpcCode()+CommonConstant.PRD_SEARCH_SCORE_TYPE_DELIMITER+SearchScoreTypeEnums.UPC.getCode());
            }
        });
    }

    /**
     * 优惠券商品搜索匹配模式
     */
    private boolean couponSearchMatchPattern(CouponSearchReq req, ProductSearchDTO dto) {
        return ProductSearchPredicate.couponSearchVerify(req,
                spuIdList->spuIdList.stream().anyMatch(d -> d.equals(dto.getId())),
                prdType -> prdType.equals(dto.getPrdType()),
                preSale -> preSale.equals(dto.getPreSale()),
                dto.getLinkedPropertyIdSet());
    }
    /**
     * 分类搜索匹配模式
     */
    private boolean categorySearchMatchPattern(List<CategorySearchReq> itemIdList, ProductSearchDTO dto) {
        for (CategorySearchReq req : itemIdList) {

            //进行参数模式匹配
            boolean verifyResult = ProductSearchPredicate.verify(req,
                    cid -> dto.getLinkedPropertyIdSet().stream().anyMatch(d -> d.equals(cid)),
                    prdType -> prdType.equals(dto.getPrdType()),
                    preSale -> preSale.equals(dto.getPreSale())
                    , dto.getLinkedPropertyIdSet());
            if (verifyResult) {
                return true;
            }
        }
        return false;
    }
}

/**
 * 分类搜索Predicate工具类，用于匹配前台类目带过来的属性
 * @author ljs
 */
class ProductSearchPredicate{

    /**
     * 优惠券商品搜索
     * @param couponSearchReq 优惠券商品搜索请求参数
     * @param spuIdPre spuId断言
     * @param prdTypePre 商品类型断言
     * @param preSalePre 商品预售断言
     * @param linkedPropertyIdSet 商品关联属性id列表
     * @return 校验结果
     */
    public static boolean couponSearchVerify(CouponSearchReq couponSearchReq,Predicate<List<Long>> spuIdPre,
                                 Predicate<Integer> prdTypePre,Predicate<Integer> preSalePre,
                                 Set<Long> linkedPropertyIdSet){

        //spuId、类目Id、(商品属性和商品类型)之间是且的关系，而商品属性和商品类型之间是或的关系
        int spuIdIsMatch = 1,categoryIsMatch = 1;
        //相关属性匹配模式
        //getSpuIsContains 有可能为null的需要判空
        int spuIsContains = Objects.isNull(couponSearchReq.getSpuIsContains()) ? 1 : couponSearchReq.getSpuIsContains();
        //spuId搜索处理
        if (CollectionUtils.isNotEmpty(couponSearchReq.getSpuIdList()) && !spuIdPre.test(couponSearchReq.getSpuIdList())) {
            spuIdIsMatch = 0;
        }
        //spuId搜索结果验证
        if (spuIdIsMatch != spuIsContains) {
            return false;
        }
        /*
         * 类目、商品属性、预售、商品类型处理逻辑
         * 举例
         * 匹配模式 不包含 -->categoryIsContains 0
         *
         * 命中了类目id !intersect -->false --> categoryIsMatch 1
         * 0 == 1 结果是 false
         * 不命中类目id !intersect-->true --> categoryIsMatch 0
         * 0 == 0 结果是 true
         *
         * 匹配模式 包含 -->categoryIsContains 1
         * 命中了类目id !intersect-->false --> categoryIsMatch 1
         * 1 == 1 结果是 true
         * 不命中类目id --> !intersect-->true -->categoryIsMatch 0
         * 1 == 0 结果是 false
         *
         * 匹配模式 包含 -->categoryIdList is null -->false -->categoryIsContains null -->1
         * 1 == 1 结果是 true
         */
        //类目Id搜索处理
        int categoryIsContains = Objects.isNull(couponSearchReq.getCategoryIsContains()) ? 1 : couponSearchReq.getCategoryIsContains();
        if (CollectionUtils.isNotEmpty(couponSearchReq.getCategoryIdList()) && !intersect(couponSearchReq.getCategoryIdList(),linkedPropertyIdSet)) {
            categoryIsMatch = 0;
        }
        //类目搜索结果验证
        if (categoryIsContains != categoryIsMatch) {
            return false;
        }
        //不开启属性过滤
        if (Objects.isNull(couponSearchReq.getPropertyIsContains())) {
            return true;
        }

        //商品属性校验结果
        boolean propertyCheckResult = couponSearchPropertyCheck(couponSearchReq,linkedPropertyIdSet);
        //商品类型校验结果
        boolean typeCheckResult = couponSearchPreSaleAndTypeCheck(couponSearchReq.getPropertyIsContains(),couponSearchReq.getType(),prdTypePre);
        //商品预售校验结果
        boolean preSaleCheckResult = couponSearchPreSaleAndTypeCheck(couponSearchReq.getPropertyIsContains(),couponSearchReq.getPreSale(),preSalePre);

        //商品属性搜索、商品类型搜索、商品预售搜索结果验证
        return preSaleCheckResult || typeCheckResult || propertyCheckResult;
    }

    /**
     * 校验预售和商品类型
     * @param propertyIsContains
     * @param target
     * @param intPredicate
     * @return
     */
    private static boolean couponSearchPreSaleAndTypeCheck(int propertyIsContains,Integer target,Predicate<Integer> intPredicate) {
        int isMatchInt = -1;
        //商品类型搜索处理
        if (Objects.nonNull(target)) {
            if (intPredicate.test(target)) {
                isMatchInt = 1;
            }else {
                isMatchInt = 2;
            }
        }
        //搜索验证结果
        return specialCheck(propertyIsContains,isMatchInt);
    }

    /**
     * 校验商品关联属性
     * @param couponSearchReq
     * @param linkedPropertyIdSet
     * @return
     */
    private static boolean couponSearchPropertyCheck(CouponSearchReq couponSearchReq,Set<Long> linkedPropertyIdSet) {
        //默认没有匹配到
        int propertyIsMatch = -1;
        //商品属性搜索处理
        int propertyIsContains = couponSearchReq.getPropertyIsContains();
        if (CollectionUtils.isNotEmpty(couponSearchReq.getProductPropertyIdList())){
            if (intersect(couponSearchReq.getProductPropertyIdList(),linkedPropertyIdSet)) {
                propertyIsMatch = 1;
            }else{
                propertyIsMatch = 2;
            }
        }
        //商品属性搜索验证结果
        return specialCheck(propertyIsContains,propertyIsMatch);
    }
    /**
     * 商品类型、属性、预售搜索结果校验
     * @return
     */
    private static boolean specialCheck(int isContains,int isMatchInt) {
        if (isMatchInt == 2 && isContains == 0) {
            //没有匹配到，且不包含
            return true;
        }
        //匹配到且包含
        return isMatchInt == isContains;
    }
    /**
     *  分类搜索校验
     * @param req 请求参数信息
     * @param categoryIdPre 类目断言
     * @param prdTypePre 商品类型断言
     * @param preSalePre 商品预售断言
     * @param linkedPropertyIdSet 商品关联属性id列表
     * @return 校验结果
     */
    public static boolean verify(CategorySearchReq req,Predicate<Long> categoryIdPre,
                                 Predicate<Integer> prdTypePre,Predicate<Integer> preSalePre,
                                 Set<Long> linkedPropertyIdSet){
        //categoryId,prdType,preSale,BrandIdList, GeneralIdList,InnerBrandIdList,ModelIdList,SpecIdList之间是且关系
        // 都是非必填,但是至少有一个为非空,其中list中的id是or关系
        //一荣俱荣，一损俱损
        if (!Objects.isNull(req.getCategoryId()) && !categoryIdPre.test(req.getCategoryId())) {
            return false;
        }
        if (!Objects.isNull(req.getPrdType()) && !prdTypePre.test(req.getPrdType())) {
            return false;
        }
        if (!Objects.isNull(req.getPreSale()) && !preSalePre.test(req.getPreSale())) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(req.getBrandIdList()) && !intersect(req.getBrandIdList(),linkedPropertyIdSet)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(req.getGeneralIdList()) && !intersect(req.getGeneralIdList(),linkedPropertyIdSet)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(req.getInnerBrandIdList()) && !intersect(req.getInnerBrandIdList(),linkedPropertyIdSet)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(req.getModelIdList()) && !intersect(req.getModelIdList(),linkedPropertyIdSet)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(req.getSpecIdList()) && !intersect(req.getSpecIdList(),linkedPropertyIdSet)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(req.getLabelIdList()) && !intersect(req.getLabelIdList(),linkedPropertyIdSet)) {
            return false;
        }
        //商品id判断
        //类目id列表判断
        //商品其他属性判断

        return true;
    }

    /**
     * 判定两个集合是否有交集
     */
    public static boolean intersect(List<Long> itemIdList, Set<Long> linkedPropertyIdSet) {
        for (Long itemId : itemIdList) {
            if (linkedPropertyIdSet.contains(itemId)) {
                return true;
            }
        }
        return false;
    }
}
