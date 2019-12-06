package com.minivision.feign.extend.client;

import feign.Client;
import feign.Request;
import feign.Response;
import feign.Util;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static feign.Util.UTF_8;

/**
 * 使用apache http-client <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @date 2019年11月27日 11:39:11 <br>
 */
public class ApacheHttpFeignClient implements Client {
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private HttpClient httpClient;

    public ApacheHttpFeignClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override public Response execute(Request request, Request.Options options) throws IOException {
        HttpUriRequest httpUriRequest;
        try {
            httpUriRequest = buildHttpRequest(request, options);
        } catch (URISyntaxException e) {
            throw new IOException("URL '" + request.url() + "' couldn't be parsed into a URI", e);
        }
        HttpResponse httpResponse = httpClient.execute(httpUriRequest);
        return toFeignResponse(httpResponse, request);
    }

    private HttpUriRequest buildHttpRequest(Request request, Request.Options options) throws URISyntaxException {
        RequestBuilder requestBuilder = RequestBuilder.create(request.method());
        // per request timeouts
        RequestConfig requestConfig =
                (httpClient instanceof Configurable ? RequestConfig.copy(((Configurable) httpClient).getConfig())
                        : RequestConfig.custom())
                        .setConnectTimeout(options.connectTimeoutMillis())
                        .setSocketTimeout(options.readTimeoutMillis())
                        .build();
        requestBuilder.setConfig(requestConfig);
        requestBuilder.setCharset(request.charset());
        URI uri = new URIBuilder(request.url()).build();
        requestBuilder.setUri(uri.getScheme() + "://" + uri.getAuthority() + uri.getRawPath());
        // request query params
        List<NameValuePair> queryParams =
                URLEncodedUtils.parse(uri, requestBuilder.getCharset());
        for (NameValuePair queryParam : queryParams) {
            requestBuilder.addParameter(queryParam);
        }
        // request headers
        boolean hasAcceptHeader = false;
        for (Map.Entry<String, Collection<String>> headerEntry : request.headers().entrySet()) {
            String headerName = headerEntry.getKey();
            if (headerName.equalsIgnoreCase(ACCEPT_HEADER_NAME)) {
                hasAcceptHeader = true;
            }
            if (headerName.equalsIgnoreCase(Util.CONTENT_LENGTH)) {
                // The 'Content-Length' header is always set by the Apache client and it
                // doesn't like us to set it as well.
                continue;
            }
            for (String headerValue : headerEntry.getValue()) {
                requestBuilder.addHeader(headerName, headerValue);
            }
        }
        // some servers choke on the default accept string, so we'll set it to anything
        if (!hasAcceptHeader) {
            requestBuilder.addHeader(ACCEPT_HEADER_NAME, "*/*");
        }
        // request body
        if (request.body() != null) {
            HttpEntity entity = null;
            if (request.charset() != null) {
                ContentType contentType = getContentType(request);
                String content = new String(request.body(), request.charset());
                entity = new StringEntity(content, contentType);
            } else {
                entity = new ByteArrayEntity(request.body());
            }

            requestBuilder.setEntity(entity);
        } else {
            requestBuilder.setEntity(new ByteArrayEntity(new byte[0]));
        }
        return requestBuilder.build();
    }

    private ContentType getContentType(Request request) {
        ContentType contentType = null;
        for (Map.Entry<String, Collection<String>> entry : request.headers().entrySet()) {
            if ("Content-Type".equalsIgnoreCase(entry.getKey())) {
                Collection<String> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    contentType = ContentType.parse(values.iterator().next());
                    if (contentType.getCharset() == null) {
                        contentType = contentType.withCharset(request.charset());
                    }
                    break;
                }
            }
        }
        return contentType;
    }

    Response toFeignResponse(HttpResponse httpResponse, Request request) {
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        String reason = statusLine.getReasonPhrase();
        Header[] allHeaders = httpResponse.getAllHeaders();
        Map<String, Collection<String>> headers = new HashMap<>(allHeaders.length);
        for (Header header : allHeaders) {
            String name = header.getName();
            String value = header.getValue();
            headers.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        }
        return Response.builder()
                .status(statusCode)
                .reason(reason)
                .headers(headers)
                .request(request)
                .body(toFeignBody(httpResponse))
                .build();
    }

    Response.Body toFeignBody(HttpResponse httpResponse) {
        final HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
            return null;
        }
        return new Response.Body() {
            @Override
            public Integer length() {
                return entity.getContentLength() >= 0 && entity.getContentLength() <= Integer.MAX_VALUE
                        ? (int) entity.getContentLength()
                        : null;
            }

            @Override
            public boolean isRepeatable() {
                return entity.isRepeatable();
            }

            @Override
            public InputStream asInputStream() throws IOException {
                return entity.getContent();
            }

            @Override
            public Reader asReader() throws IOException {
                return new InputStreamReader(asInputStream(), UTF_8);
            }

            @Override
            public void close() throws IOException {
                EntityUtils.consume(entity);
            }
        };
    }

}
