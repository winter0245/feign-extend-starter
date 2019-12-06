package com.minivision.feign.extend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <Description> 设备缓存对象<br>
 * 
 * @author xubin<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate Nov 1, 2018 <br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCache implements Serializable {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 1L;


    /**
     * 应用key
     */
    private String appKey;

    /**
     * id
     */
    private String id;

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
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 设备类型编码 <br>
     */
    private String deviceTypeCode;

    /**
     * 在线状态0在线1离线
     */
    private String isOnline;

    /**
     * 在线状态变更时间戳 <br>
     */
    private Long isOnlineUpdTime;

    /**
     * 设备状态
     */
    private String deviceState;

    /**
     * 设备状态名称 <br>
     */
    private String deviceStateName;

    /**
     * 上级设备
     */
    private String pId;

    /**
     * 是否激活 <br>
     */
    private String isActivated;

    /**
     * 激活时间 <br>
     */
    private Date activateTime;

    /**
     * 设备版本号 <br>
     */
    private String version;




}
