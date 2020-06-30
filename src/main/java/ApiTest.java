public class ApiTest {

    public static void main(String[] args) throws Exception {
//        DecodeCertificates decodeCertificates = new DecodeCertificates();
//        // 模拟usingKey：认购有效期至2089年7月8日
//        String license = decodeCertificates.decodeLicense("Subscription is active until July 8, 2089");
//        System.out.println("测试结果：" + license);
        String str = "stprefinfrefordno20200613";
        String str2 = "STPREFINFREFORDNO";
        System.out.println(str.toUpperCase().contains(str2));
    }
}
