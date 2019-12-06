package com.minivision.feign.extend.reset;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年12月06日 11:00:19 <br>
 */
@FeignClient("salmonDevice")
@RequestMapping("/salmon_device")
public interface DeviceWelcome {
    @GetMapping("welcome")
    public String welcome();
}
