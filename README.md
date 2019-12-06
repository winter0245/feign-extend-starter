# feign-extend-starter
本项目为对feign的扩展支持，引用了open-feign的部分特性

### 使用方法

引入maven依赖

```xml
<dependency>    
    <groupId>com.github.winter</groupId>
    <artifactId>feign-extend-starter</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

在springboot启动类开启注解

```java
@EnableFeignExtend
```

### 1.服务路由配置

feign支持调用注册到注册中心的服务调用和指定url地址调用，在此基础上扩展了路由配置，以支持动态调用非注册中心接口

在feign注解`@FeignClient`注解上指定服务名称，例如

```java
@FeignClient( "multipart-support-service")
public interface Client {
}
```

在配置文件中指定路由地址

```properties
feign.extend.route-map.multipart-support-service=127.0.0.1:8081
```

### 2.http连接池扩展

feign默认通信方式为jdk自带的 `HttpURLConnection` ,扩展为支持okHttp和apache httpClient连接池客户端

apache httpClient配置

```properties
feign.extend.client-type=apache_http
#feign.extend.apache-http-config 下可以配置连接池参数
feign.extend.apache-http-config.max-connections-per-route=50
...
```

okHttp 配置

```properties
feign.extend.client-type=ok_http
#feign.extend.ok-http-config 下可以配置连接池参数
feign.extend.ok-http-config.max-idle-connections=100
...
```

原生配置

```properties
feign.extend.client-type=jdk_connect
```

连接池客户端的实现参考了 https://github.com/OpenFeign/feign

### 3.form请求支持

该部分的实现参考了https://github.com/OpenFeign/feign-form项目

支持以下形态的form请求

```java
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
```