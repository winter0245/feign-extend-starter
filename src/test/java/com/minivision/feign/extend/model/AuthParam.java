package com.minivision.feign.extend.model;

import lombok.Data;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年12月02日 10:21:01 <br>
 */
@Data
public class AuthParam {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 4368603393100317596L;

    /**
     * appKey
     */
    protected String appKey;

    /**
     * 时间戳
     */
    protected String timestamp;

    /**
     * needPush2Device add by guming iot添加人脸方法中需要增加参数判断是否将该人脸推送到终端
     */
    protected boolean needPush2Device;

}
