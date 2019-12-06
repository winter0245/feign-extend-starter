package com.minivision.feign.extend.client;

import com.google.common.collect.Sets;
import feign.Client;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 使用okHttp client <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @date 2019年11月27日 16:59:34 <br>
 */
public class OkHttpFeignClient implements Client {

    private static final Set<String> METHOD_WITH_BODY_SET = Sets.newHashSet(HttpMethod.POST.name(), HttpMethod.PATCH.name(), HttpMethod.PUT.name());

    private final okhttp3.OkHttpClient delegate;

    public OkHttpFeignClient() {
        this(new okhttp3.OkHttpClient());
    }

    public OkHttpFeignClient(okhttp3.OkHttpClient delegate) {
        this.delegate = delegate;
    }

    static Request toOkHttpRequest(feign.Request input) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(input.url());
        MediaType mediaType = null;
        boolean hasAcceptHeader = input.headers().containsKey("Accept");
        for (Map.Entry<String, Collection<String>> field : input.headers().entrySet()) {
            for (String value : field.getValue()) {
                requestBuilder.addHeader(field.getKey(), value);
                if ("Content-Type".equalsIgnoreCase(field.getKey())) {
                    mediaType = parseMediaType(value, input);
                }
            }
        }
        // Some servers choke on the default accept string.
        if (!hasAcceptHeader) {
            requestBuilder.addHeader("Accept", "*/*");
        }

        byte[] inputBody = input.body();
        String method = input.method().toUpperCase();
        boolean isMethodWithBody = METHOD_WITH_BODY_SET.contains(method);
        if (isMethodWithBody) {
            requestBuilder.removeHeader("Content-Type");
            if (inputBody == null) {
                // write an empty BODY to conform with okhttp 2.4.0+
                // http://johnfeng.github.io/blog/2015/06/30/okhttp-updates-post-wouldnt-be-allowed-to-have-null-body/
                inputBody = new byte[0];
            }
        }

        RequestBody body = inputBody != null ? RequestBody.create(inputBody, mediaType) : null;
        requestBuilder.method(method, body);
        return requestBuilder.build();
    }

    private static feign.Response toFeignResponse(Response response, feign.Request request) {
        return feign.Response.builder()
                .status(response.code())
                .reason(response.message())
                .request(request)
                .headers(toMap(response.headers()))
                .body(toBody(response.body()))
                .build();
    }

    private static MediaType parseMediaType(String type, feign.Request input) {
        MediaType mediaType = MediaType.parse(type);
        if (input.charset() != null && mediaType != null) {
            mediaType.charset(input.charset());
        }
        return mediaType;
    }

    private static Map<String, Collection<String>> toMap(Headers headers) {
        return (Map) headers.toMultimap();
    }

    private static feign.Response.Body toBody(final ResponseBody input) {
        if (input == null || input.contentLength() == 0) {
            if (input != null) {
                input.close();
            }
            return null;
        }
        final Integer length = input.contentLength() >= 0 && input.contentLength() <= Integer.MAX_VALUE
                ? (int) input.contentLength()
                : null;

        return new feign.Response.Body() {

            @Override
            public void close() throws IOException {
                input.close();
            }

            @Override
            public Integer length() {
                return length;
            }

            @Override
            public boolean isRepeatable() {
                return false;
            }

            @Override
            public InputStream asInputStream() throws IOException {
                return input.byteStream();
            }

            @Override
            public Reader asReader() throws IOException {
                return input.charStream();
            }

        };
    }

    @Override
    public feign.Response execute(feign.Request input, feign.Request.Options options)
            throws IOException {
        okhttp3.OkHttpClient requestScoped;
        if (delegate.connectTimeoutMillis() != options.connectTimeoutMillis()
                || delegate.readTimeoutMillis() != options.readTimeoutMillis()) {
            requestScoped = delegate.newBuilder()
                    .connectTimeout(options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .readTimeout(options.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .build();
        } else {
            requestScoped = delegate;
        }
        Request request = toOkHttpRequest(input);
        Response response = requestScoped.newCall(request).execute();
        return toFeignResponse(response, input).toBuilder().request(input).build();
    }
}
