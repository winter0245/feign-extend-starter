package com.minivision.feign.extend.model;

/**
 * 错误消息封装
 * @auther zhangdd
 * @date 2019-08-30 16:45:30
 * @description 错误消息封装
 */
public class RestRtnMessage {
    /**
     * 错误消息描述
     */
    private String msgText;

    /**
     * 消息编码
     */
    private String msgCode;


    /**
     * 获取错误描述
     * @return 错误描述
     */
    public String getMsgText() {
        return this.msgText;
    }

    /**
     * 设置错误描述
     * @param msgText 错误描述
     */
    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    /**
     * 获取错误码
     * @return 错误码
     */
    public String getMsgCode() {
        return this.msgCode;
    }

    /**
     * 设置错误码
     * @param msgCode 错误码
     */
    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    @Override
    public String toString() {
        return "RtnMessage(msgText=" + this.getMsgText() + ", msgCode=" + this.getMsgCode() + ")";
    }

    /**
     * 默认无参构造函数
     */
    public RestRtnMessage() {
    }
}
