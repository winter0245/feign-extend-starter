package com.minivision.feign.extend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <Description> 设备信息业务对象<br>
 *
 * @author xubin<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年9月11日 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfoBO extends AbstractFatherBO implements Serializable {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Description: 构造器<br>
     *
     * @param deviceCode 设备code<br>
     *                   Argument for @NotNull parameter 'type' of com/intellij/codeInsight/template/PsiTypeResult.<init> must not be null
     * @author xubin <br>
     * @taskId <br>
     */
    public DeviceInfoBO(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    /**
     * id
     */
    private String id;

    /**
     * 是否删除0未删除1已删除
     */
    private String isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改人ID
     */
    private String modifierId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备描述
     */
    private String deviceDesc;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 设备类型code
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
     * 设备状态code <br>
     */
    private String deviceStateCode;

    /**
     * IPv4地址
     */
    private String deviceIpv4;

    /**
     * MAC地址
     */
    private String deviceMac;

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

    /**
     * 设备属性数量
     */
    private Integer attrCount;

    /**
     * 子设备列表 <br>
     */
    private List<DeviceInfoBO> subDeviceList;

    /**
     * 父级设备
     */
    private DeviceInfoBO parentDevice;

    /**
     * 设备模型
     */
    private String deviceModel;
    /**
     * 业务关联模块编码
     */
    private String moduleCode;

    /**
     * 业务关联id
     */
    private String bizId;

}
