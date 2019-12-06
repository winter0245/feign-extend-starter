package com.minivision.feign.extend.reset;

import com.alibaba.fastjson.JSONObject;
import com.minivision.feign.extend.model.AppInfo;
import com.minivision.feign.extend.model.AuthParam;
import com.minivision.feign.extend.model.IotDeviceDetailParam;
import com.minivision.feign.extend.model.RestResult;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @CreateDate 2019年12月02日 10:21:26 <br>
 */
@FeignClient(value = "FDIOT-PLAT")
@RequestMapping(value = "api/v1/app")
public interface IotAppInner {
    @PostMapping(value = "detail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestResult<AppInfo> detail(AuthParam param, @RequestHeader("Token") String token);

    @PostMapping(value = "detail2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestResult<AppInfo> detail2(@Param("param") AuthParam param, @Param("token") String token);
}
