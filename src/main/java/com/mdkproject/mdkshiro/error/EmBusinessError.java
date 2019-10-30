package com.mdkproject.mdkshiro.error;


public enum EmBusinessError implements CommonError {

    //定义通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"服务器异常"),
    REPEATED_REQ(10003,"请勿重复请求"),


    //20000开头为用户信息相关错误
    USER_NOT_EXIT(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"账号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户还未登录"),
    USER_LOGIN_GQ(20004,"用户登录过期，请重新登录"),
    USER_IS_EXIT(20005,"账号已存在"),



    //30000开头验证码错误
    CODE_ERROR(30001,"验证码错误"),
    CODE_LOSE(30002,"验证码已失效"),
    CODE_EXCEED(30003,"发送次数超过限制"),
    CODE_GET_ERROR(30004,"验证码获取失败"),

    //权限和数据问题
    CANT_DELETE_ADMIN(600, "不能删除超级管理员"),
    CANT_FREEZE_ADMIN(600, "不能冻结超级管理员"),
    CANT_CHANGE_ADMIN(600, "不能修改超级管理员角色"),
    CANT_PASSWORD_ADMIN(600, "不能重置管理员密码"),
    CANT_INFO_ADMIN(600, "不能修改超级管理员信息"),

    //50000开头操作权限错误信息
    ROLE_NOT_EXIT(50001,"角色不存在"),
    ROLE_NOT_MANAGE(50002,"没有操作权限"),


    //60000开头
    SESSION_NOT_EXIT(60001,"session过期，请重新登录"),



    ;

    private EmBusinessError(Integer errCode,String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    private Integer errCode;
    private String errMsg;

    @Override
    public Integer getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
