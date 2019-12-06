package com.minivision.feign.extend.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * rest接口返回结果
 *
 * @param <E> 数据泛型
 * @author zhangdd
 * @date 2019-08-30 16:42:24
 * @description rest接口返回结果
 */
public class RestResultInfo<E> {

    /**
     * 1-处理成功,0-处理失败
     */
    private int resCode;

    /**
     * 错误消息集合
     */
    private List<RestRtnMessage> resMsg;

    /**
     * 结果数据
     */
    private E resData;

    /**
     * 构造函数，带返回结果
     *
     * @param resCode 错误码
     * @param resMsg  错误描述
     * @param resData 结果数据
     */
    public RestResultInfo(int resCode, List<RestRtnMessage> resMsg, E resData) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.resData = resData;
    }

    /**
     * 构造函数，包含错误信息
     *
     * @param resCode 错误码
     * @param resMsg  错误信息
     */
    public RestResultInfo(int resCode, List<RestRtnMessage> resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.resData = (E) new Object();
    }

    /**
     * 构造函数，只包含错误码和结果数据
     *
     * @param resCode 错误码
     * @param resData 结果数据
     */
    public RestResultInfo(int resCode, E resData) {
        this.resCode = resCode;
        this.resMsg = new ArrayList();
        this.resData = resData;
    }

    /**
     * 构造函数
     *
     * @param resCode 错误码
     * @param rtnMsg  错误消息
     */
    public RestResultInfo(int resCode, RestRtnMessage rtnMsg) {
        this.resCode = resCode;
        this.resMsg = Arrays.asList(rtnMsg);
        this.resData = (E) rtnMsg;
    }


    /**
     * 获取错误码
     * @return 错误码
     */
    public int getResCode() {
        return this.resCode;
    }

    /**
     * 设置错误码
     * @param resCode 错误码
     */
    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    /**
     * 获取错误消息
     * @return 错误消息
     */
    public List<RestRtnMessage> getResMsg() {
        return this.resMsg;
    }

    /**
     * 设置错误消息
     * @param resMsg 错误消息
     */
    public void setResMsg(List<RestRtnMessage> resMsg) {
        this.resMsg = resMsg;
    }

    /**
     * 获取结果数据
     * @return 结果数据
     */
    public E getResData() {
        return this.resData;
    }

    /**
     * 设置结果数据
     * @param resData 结果数据
     */
    public void setResData(E resData) {
        this.resData = resData;
    }

    @Override
    public String toString() {
        return "ResultInfo(resCode=" + this.getResCode() + ", resMsg=" + this.getResMsg() + ", resData=" + this.getResData() + ")";
    }

    /**
     * 默认无参构造函数
     */
    public RestResultInfo() {
    }
}
