package com.minivision.feign.extend.reset;

import com.minivision.feign.extend.model.DeviceInfoBO;
import com.minivision.feign.extend.model.PageInfo;
import com.minivision.feign.extend.model.QueryDeviceInfoDetailReq;
import com.minivision.feign.extend.model.QueryDeviceInfoReq;
import com.minivision.feign.extend.model.RestResultInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年11月27日 09:41:03 <br>
 */
@FeignClient("salmonDevice")
@RequestMapping("/salmon_device/deviceInfoApi/")
public interface DeviceRestInner {

    @PostMapping("getDeviceInfoDetail")
    RestResultInfo<DeviceInfoBO> getDeviceInfoDetail(@RequestBody QueryDeviceInfoDetailReq queryDeviceInfoDetailReq,
            @RequestHeader("appKey") String appKey);

    @PostMapping("queryDeviceListByPage")
    RestResultInfo<PageInfo<DeviceInfoBO>> queryDeviceListByPage(
            @RequestBody QueryDeviceInfoReq queryDeviceInfoReq, @RequestHeader("appKey") String appKey);

    @PostMapping("c/getDeviceInfoDetail")
    RestResultInfo<DeviceInfoBO> getCacheDeviceInfoDetail(@RequestBody QueryDeviceInfoDetailReq queryDeviceInfoDetailReq,
            @RequestHeader("appKey") String appKey);

}
