package com.sam.cn.myframework.endity;

/**
 * 处理结果信息
 * @author sam.liang
 */
public class ResultInfo<R> {
    //
    private ResultType resultType;
    private String reason;
    private R returnData;

    public ResultInfo(ResultType resultType, String reason, R returnData) {
        this.resultType = resultType;
        this.reason = reason;
        this.returnData = returnData;
    }
    public ResultInfo(ResultType resultType, String reason) {
        this.resultType = resultType;
        this.reason = reason;
    }
    public ResultInfo(ResultType resultType, R returnData) {
        this.resultType = resultType;
        this.returnData = returnData;
    }
    public ResultInfo(ResultType resultType) {
        this.resultType = resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public R getReturnData() {
        return returnData;
    }

    public void setReturnData(R returnData) {
        this.returnData = returnData;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "resultType=" + resultType +
                ", reason='" + reason + '\'' +
                ", returnData=" + returnData +
                '}';
    }
}
