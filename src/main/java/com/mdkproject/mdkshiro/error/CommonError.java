package com.mdkproject.mdkshiro.error;

public interface CommonError {
    public Integer getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
