package com.minivision.feign.extend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * AppInfo
 *
 * @author denmawei
 * @date 2019/7/24 10:04
 */
@Data
public class AppInfo implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1675966457152308836L;

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * appkey
     */
    private String appKey;

    /**
     * 秘钥
     */
    private String appSecret;

    /**
     * 模型id
     */
    private String modelUuid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
