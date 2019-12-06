package com.minivision.feign.extend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <Description> 查询设备信息请求<br>
 *
 * @author xubin<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年9月13日 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDeviceInfoReq {

    /**
     * 页码 <br>
     */
    private Integer pageNum;

    /**
     * 页大小 <br>
     */
    private Integer pageSize;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备状态
     */
    private String deviceState;

    /**
     * MAC地址
     */
    private String deviceMac;

    /**
     * 是否在线
     */
    private String isOnline;

    /**
     * 是否激活
     */
    private String isActivated;

    private String pId;

    /**
     * 业务关联模块编码
     */
    private String moduleCode;

    /**
     * 业务关联id
     */
    private String bizId;

}
