package com.minivision.feign.extend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年11月27日 14:44:59 <br>
 */

@ConfigurationProperties(prefix = "feign.extend")
@Data
public class FeignExtendProperties {

    public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = false;

    public static final int DEFAULT_MAX_CONNECTIONS = 200;

    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

    public static final long DEFAULT_TIME_TO_LIVE = 900L;

    public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;

    public static final boolean DEFAULT_FOLLOW_REDIRECTS = true;

    public static final int DEFAULT_CONNECTION_TIMEOUT = 2000;

    public static final int DEFAULT_CONNECTION_TIMER_REPEAT = 3000;

    private HttpClientType clientType = HttpClientType.JDK_CONNECT;

    private ApacheHttpConfig apacheHttpConfig = new ApacheHttpConfig();

    private OkHttpConfig okHttpConfig = new OkHttpConfig();

    private Map<String, String> routeMap = new HashMap<>();

    @Data
    static class ApacheHttpConfig {

        /**
         * 建立连接超时时间(ms),默认时间为60s
         */
        private int connectTimeOutMills = 60000;

        /**
         * 从连接池获取连接超时时间(ms),默认时间为30s
         */
        private int connectRequestTimeOutMills = 30000;

        /**
         * 从服务端获取响应结果超时时间(ms),默认时间为60s
         */
        private int socketTimeOutMills = 60000;

        /**
         * 最大连接数
         */
        private int maxConnections = DEFAULT_MAX_CONNECTIONS;

        /**
         * 单路由最大连接数
         */
        private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

        /**
         * 连接释放时间(s)
         */
        private long timeToLive = DEFAULT_TIME_TO_LIVE;

        /**
         * 清理连接失效的任务间隔时间(ms)
         */
        private int connectionTimerRepeat = DEFAULT_CONNECTION_TIMER_REPEAT;
    }

    @Data
    static class OkHttpConfig {

        /**
         * 建立连接超时时间(ms),默认时间为60s
         */
        private int connectTimeOutMills = 60000;

        /**
         * 从连接池获取连接超时时间(ms),默认时间为30s
         */
        private int connectRequestTimeOutMills = 30000;

        /**
         * 从服务端获取响应结果超时时间(ms),默认时间为60s
         */
        private int socketTimeOutMills = 60000;

        /**
         * 最大空闲连接数
         */
        private int maxIdleConnections = 100;

        /**
         * 连接释放时间(s)
         */
        private long timeToLive = DEFAULT_TIME_TO_LIVE;

    }

    enum HttpClientType {
        /**
         * APACHE_HTTP
         */
        APACHE_HTTP,
        /**
         * OK_HTTP
         */
        OK_HTTP,
        /**
         * jdk原生连接
         */
        JDK_CONNECT
    }

}
