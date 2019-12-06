/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.minivision.feign.extend.reset;

import com.minivision.feign.extend.Dto;
import com.minivision.feign.extend.SpringDto;
import feign.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author Artem Labazin
 */
@FeignClient("multipart-support-service")
public interface Client {

  @RequestMapping(
      value = "/multipart/upload1/{folder}",
      method = POST,
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  String upload1 (@PathVariable("folder") String folder,
          @RequestPart MultipartFile file,
          @RequestParam(value = "message", required = false) String message);

  @RequestMapping(
      value = "/multipart/upload2/{folder}",
      method = POST,
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  String upload2 (@RequestBody MultipartFile file,
          @PathVariable("folder") String folder,
          @RequestParam(value = "message", required = false) String message);

  @RequestMapping(
      value = "/multipart/upload3/{folder}",
      method = POST,
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  String upload3 (@RequestBody MultipartFile file,
          @PathVariable("folder") String folder,
          @RequestParam(value = "message", required = false) String message);

  @RequestMapping(
      path = "/multipart/upload4/{id}",
      method = POST,
      produces = APPLICATION_JSON_VALUE
  )
  String upload4 (@PathVariable("id") String id,
          @RequestBody Map<Object, Object> map,
          @RequestParam("userName") String userName);

  @RequestMapping(
      path = "/multipart/upload5",
      method = POST,
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  Response upload5(SpringDto dto);

  @RequestMapping(
      path = "/multipart/upload6",
      method = POST,
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  String upload6Array (@RequestPart MultipartFile[] files);

  @RequestMapping(
      path = "/multipart/upload6",
      method = POST,
      consumes = MULTIPART_FORM_DATA_VALUE
  )
  String upload6Collection (@RequestPart List<MultipartFile> files);


}
