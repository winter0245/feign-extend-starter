package com.minivision.feign.extend.reset;

import com.alibaba.fastjson.JSONObject;
import com.minivision.feign.extend.model.AuthParam;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年12月02日 18:14:53 <br>
 */
@FeignClient("provider2")
@RequestMapping("hello2")
public interface FileInner {

    @PostMapping(value = "testUploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject testUploadFile(@Param("authParam") AuthParam authParam, @Param("file") MultipartFile file);


}
