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

package com.minivision.feign.extend;

import com.minivision.feign.extend.form.FeignFileList;
import com.minivision.feign.extend.form.FormData;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import feign.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Artem Labazin
 */
@FeignClient(value = "localTest11",url = "http://localhost:8080")
public interface TestClient {

    @PostMapping(value = "/form", consumes = "application/x-www-form-urlencoded")
    Response form(@Param("key1") String key1, @Param("key2") String key2);

    @PostMapping(value = "/upload/{id}", consumes = "multipart/form-data")
    String upload(@Param("id") Integer id, @Param("public") Boolean isPublic, @Param("file") File file);

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    String upload(@Param("file") File file);

    @PostMapping(value = "/json")
    String json(Dto dto);

    @PostMapping(value = "/query_map", consumes = "multipart/form-data")
    String queryMap(@QueryMap Map<String, ?> value);

    @PostMapping(value = "/upload/files", consumes = "multipart/form-data")
    String uploadWithArray(@Param("files") File[] files);

    @PostMapping(value = "/upload/files", consumes = "multipart/form-data")
    String uploadWithList(@Param(value = "files") FeignFileList<File> files);

    @PostMapping(value = "/upload/files", consumes = "multipart/form-data")
    String uploadWithManyFiles(@Param("files") File file1, @Param("files") File file2);

    @PostMapping(value = "/upload/with_dto", consumes = "multipart/form-data")
    Response uploadWithDto(@Param("dto") Dto dto, @Param("file") File file);

    @PostMapping(value = "/upload/with_dtos", consumes = "multipart/form-data")
    Response uploadWithDtos(@Param("dto") Dto dto, @Param("dto2") SpringDto springDto, @Param("file") File file);

    @PostMapping(value = "/upload/unknown_type", consumes = "multipart/form-data")
    String uploadUnknownType(@Param("file") File file);

    @PostMapping(value = "/upload/form_data", consumes = "multipart/form-data")
    String uploadFormData(@Param("file") FormData formData);

    @PostMapping(value = "/submit/url", consumes = "application/x-www-form-urlencoded")
    String submitRepeatableQueryParam(@Param("names") String[] names);

    @PostMapping(value = "/submit/form", consumes = "multipart/form-data")
    String submitRepeatableFormParam(@Param("names") Collection<String> names);

}
