package com.mdkproject.mdkshiro.response;

public class CommonReturnType {
    //状态    success or fail
    private String status;
    //success 返回数据   fail返回错误码
    private Object data;



    //定义一个通用的创建方法
    public static CommonReturnType createCommonReturnType(Object result){
        return createCommonReturnType(result,"success");
    }

    public static CommonReturnType createCommonReturnType(Object result,String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return commonReturnType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
