package com.minivision.feign.extend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <Description>查询设备信息详情请求 <br>
 * 
 * @author xubin<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate Nov 13, 2018 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDeviceInfoDetailReq {

    /**
     * 设备编码
     */
    private String deviceCode;
}
