package com.minivision.feign.extend.reset;

import com.alibaba.fastjson.JSONObject;
import com.minivision.feign.extend.model.RestResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年12月04日 15:41:34 <br>
 */
@FeignClient(value = "FDIOT-PLAT")
@RequestMapping(value = "api/v1/device")
public interface IotDeviceDetail {
    @PostMapping(value = "deviceDetail")
    RestResult<List<JSONObject>> deviceDetail(@RequestParam(value = "deviceSns") String deviceSns, @RequestParam("appKey") String appKey,
            @RequestParam("timestamp") String timestamp, @RequestHeader("Token") String token);
}
