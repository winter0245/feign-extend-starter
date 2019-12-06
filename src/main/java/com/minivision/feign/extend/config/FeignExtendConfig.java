package com.minivision.feign.extend.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.minivision.feign.extend.client.ApacheHttpFeignClient;
import com.minivision.feign.extend.client.OkHttpFeignClient;
import com.minivision.feign.extend.converter.SpringManyMultipartFilesReader;
import com.minivision.feign.extend.form.FeignFileList;
import com.minivision.feign.extend.form.SpringFormEncoder;
import com.minivision.feign.extend.form.processor.ParamAnnotationProcessor;
import com.netflix.loadbalancer.ILoadBalancer;
import feign.Client;
import feign.Contract;
import feign.Request;
import feign.Response;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.PathVariableParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.RequestHeaderParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.RequestParamParameterProcessor;
import org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.minivision.feign.extend.config.FeignExtendProperties.HttpClientType;
import static com.minivision.feign.extend.config.FeignExtendProperties.OkHttpConfig;

/**
 * feign扩展配置 <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @date 2019年11月27日 09:52:15 <br>
 */
@EnableConfigurationProperties(FeignExtendProperties.class)
@Slf4j
public class FeignExtendConfig {

    @Autowired
    private FeignExtendProperties feignExtendProperties;

    @Autowired(required = false)
    private CloseableHttpClient httpClient;

    @Autowired(required = false)
    private OkHttpClient okHttpClient;

    @Autowired(required = false)
    private CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory;

    @Autowired(required = false)
    private SpringClientFactory springClientFactory;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    private final ScheduledExecutorService connectionManagerTimer = new ScheduledThreadPoolExecutor(1,
            new ThreadFactoryBuilder().setNameFormat("close-idle-clients-%d").build());

    /**
     * 表单类型编码器
     *
     * @return SpringFormEncoder
     */
    @Bean
    public Encoder formEncoder() {
        SpringEncoder springEncoder = new SpringEncoder(messageConverters);
        return new SpringFormEncoder(springEncoder);
    }

    /**
     * 文件下载解析器
     *
     * @return SpringManyMultipartFilesReader
     */
    @Bean
    public SpringManyMultipartFilesReader multipartFilesReader() {
        return new SpringManyMultipartFilesReader(4096);
    }

    /**
     * spring规约支持的注解处理器，新增了 ParamAnnotationProcessor 处理器
     *
     * @return
     */
    @Bean
    public List<AnnotatedParameterProcessor> requestParamParameterProcessor() {
        List<AnnotatedParameterProcessor> annotatedArgumentResolvers = new ArrayList<>();
        annotatedArgumentResolvers.add(new PathVariableParameterProcessor());
        annotatedArgumentResolvers.add(new RequestParamParameterProcessor());
        annotatedArgumentResolvers.add(new RequestHeaderParameterProcessor());
        annotatedArgumentResolvers.add(new ParamAnnotationProcessor());
        return annotatedArgumentResolvers;
    }

    /**
     * 扩展ConversionService,排除文件类型
     *
     * @param feignConversionService ConversionService
     * @return
     */
    @Bean
    public Contract feignContract(ConversionService feignConversionService) {
        return new SpringMvcContract(requestParamParameterProcessor(), new ExcludeFileConversionServiceImpl(feignConversionService));
    }

    /**
     * Description: 自定义feign路由<br>
     *
     * @param
     * @return com.minivision.fdiot.config.IotRestConfig.CustomFeignClient <br>
     * @author zhangdongdong <br>
     * @taskId <br>
     */
    @Primary
    @Bean
    public CustomFeignClient customFeignClient(FeignExtendProperties extendProperties)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Client client = null;
        FeignExtendProperties.HttpClientType clientType = extendProperties.getClientType();
        if (clientType == HttpClientType.APACHE_HTTP) {
            if (httpClient == null) {
                log.info("set feign client type to apache http client pool,configs [{}]", extendProperties.getApacheHttpConfig());
                httpClient = buildHttpClient();
            } else {
                log.info("set feign client type to exist apache http client pool [{}]", httpClient);
            }
            client = new ApacheHttpFeignClient(httpClient);
        } else if (clientType == HttpClientType.OK_HTTP) {
            if (okHttpClient == null) {
                log.info("set feign client type to okHttp client pool,configs [{}]", extendProperties.getOkHttpConfig());
                okHttpClient = buildOkHttpClient();
            } else {
                log.info("set feign client type to exist okHttp client pool [{}]", okHttpClient);
            }
            client = new OkHttpFeignClient(okHttpClient);
        } else {
            log.warn("use feign default jdk http connection factory");
            client = new Client.Default(null, null);
        }
        LoadBalancerFeignClient ribbonClient = null;
        if (springClientFactory != null && cachingSpringLoadBalancerFactory != null) {
            ribbonClient = new LoadBalancerFeignClient(client,
                    cachingSpringLoadBalancerFactory, springClientFactory);
            log.info("build ribbon client as back up");
        }
        return new CustomFeignClient(client, ribbonClient, extendProperties.getRouteMap(), springClientFactory);
    }

    /**
     * 构建httpClient
     *
     * @return HttpClient
     * @throws KeyStoreException        KeyStoreException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws KeyManagementException   KeyManagementException
     */
    private CloseableHttpClient buildHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        FeignExtendProperties.ApacheHttpConfig httpConfig = feignExtendProperties.getApacheHttpConfig();
        Assert.notNull(httpConfig, "not find apache http config");
        SSLContext sslcontext =
                SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslConnectionSocketFactory).build();
        // 5秒超时
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(httpConfig.getConnectRequestTimeOutMills())
                .setSocketTimeout(httpConfig.getSocketTimeOutMills()).setConnectTimeout(httpConfig.getConnectTimeOutMills()).build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(httpConfig.getSocketTimeOutMills()).build();
        PoolingHttpClientConnectionManager cm =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setDefaultMaxPerRoute(httpConfig.getMaxConnectionsPerRoute());
        cm.setMaxTotal(httpConfig.getMaxConnections());
        connectionManagerTimer
                .scheduleAtFixedRate(cm::closeExpiredConnections, httpConfig.getConnectionTimerRepeat(), httpConfig.getConnectionTimerRepeat(),
                        TimeUnit.MILLISECONDS);
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new StandardHttpRequestRetryHandler())
                .setDefaultSocketConfig(socketConfig).setConnectionManager(cm).build();
    }

    private OkHttpClient buildOkHttpClient() {
        OkHttpConfig okHttpConfig = feignExtendProperties.getOkHttpConfig();
        Assert.notNull(okHttpConfig, "not find okHttp config");
        OkHttpClientConnectionPoolFactory clientConnectionPoolFactory = new DefaultOkHttpClientConnectionPoolFactory();
        ConnectionPool connectionPool = clientConnectionPoolFactory
                .create(okHttpConfig.getMaxIdleConnections(), okHttpConfig.getTimeToLive(), TimeUnit.SECONDS);
        OkHttpClientFactory okHttpClientFactory = new DefaultOkHttpClientFactory();
        return okHttpClientFactory.createBuilder(false).followRedirects(false).connectionPool(connectionPool)
                .connectTimeout(okHttpConfig.getConnectTimeOutMills(), TimeUnit.MILLISECONDS)
                .callTimeout(okHttpConfig.getSocketTimeOutMills(), TimeUnit.MILLISECONDS).build();
    }

    @PreDestroy
    public void preDestroy() {
        try {
            connectionManagerTimer.shutdown();
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.error("shut down http pool error", e);
        }
    }

    /**
     * 自定义feign客户端,替代ribbon
     *
     * @author zhangdongdong<br>
     * @version 1.0<br>
     * @taskId <br>
     * @date 2019年11月20日 14:11:03 <br>
     */
    static class CustomFeignClient implements Client {

        private final Client delegate;

        private final LoadBalancerFeignClient ribbonClient;

        private Map<String, String> hostMap;

        private SpringClientFactory springClientFactory;

        public final boolean isLoadBalanceService(String service) {
            ILoadBalancer loadBalancer = springClientFactory.getLoadBalancer(service);
            return loadBalancer != null && !CollectionUtils.isEmpty(loadBalancer.getAllServers());
        }

        CustomFeignClient(Client delegate, LoadBalancerFeignClient ribbonClient, Map<String, String> hostMap,
                SpringClientFactory springClientFactory) {
            this.delegate = delegate;
            this.ribbonClient = ribbonClient;
            this.hostMap = hostMap;
            this.springClientFactory = springClientFactory;
            log.info("host mapping :{}", hostMap);
        }

        @Override public Response execute(Request request, Request.Options options) throws IOException {
            URI uri = URI.create(request.url());
            String host = uri.getHost();
            String targetHost = hostMap.get(host);
            if (targetHost != null) {
                String url = request.url().replaceFirst(host, targetHost);
                log.debug("wrap feign url [{}] to [{}]", request.url(), url);
                Request newRequest = Request.create(request.method(), url, request.headers(), request.body(), request.charset());
                return delegate.execute(newRequest, options);
            } else if (ribbonClient != null && isLoadBalanceService(host)) {
                return ribbonClient.execute(request, options);
            } else {
                return delegate.execute(request, options);
            }

        }
    }

    /**
     * 扩展转换body参数转换器 <br>
     * 排除文件类型处理
     *
     * @author zhangdongdong<br>
     * @version 1.0<br>
     * @taskId <br>
     * @date 2019年12月05日 17:30:17 <br>
     */
    private static class ExcludeFileConversionServiceImpl implements ConversionService {

        private Set<Class<?>> excludeTypes = new HashSet<>();

        private ConversionService delegate;

        public ExcludeFileConversionServiceImpl(ConversionService delegate) {
            excludeTypes.add(File.class);
            excludeTypes.add(MultipartFile.class);
            excludeTypes.add(File[].class);
            excludeTypes.add(MultipartFile[].class);
            excludeTypes.add(FeignFileList.class);
            this.delegate = delegate;
        }

        private boolean isExcludeType(Class<?> sourceType) {
            for (Class<?> exclude : excludeTypes) {
                if (exclude.isAssignableFrom(sourceType)) {
                    return true;
                }
            }
            return false;
        }

        @Override public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
            if (sourceType == null) {
                return false;
            }
            if (isExcludeType(sourceType)) {
                return false;
            }
            return delegate.canConvert(sourceType, targetType);
        }

        @Override public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (sourceType == null) {
                return false;
            }
            Class<?> type = sourceType.getType();
            if (isExcludeType(type)) {
                return false;
            }
            return delegate.canConvert(sourceType, targetType);
        }

        @Override public <T> T convert(Object source, Class<T> targetType) {
            return delegate.convert(source, targetType);
        }

        @Override public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return delegate.convert(source, sourceType, targetType);
        }
    }

}
