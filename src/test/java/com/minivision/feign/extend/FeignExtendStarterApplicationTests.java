package com.minivision.feign.extend;

import com.minivision.feign.extend.model.AppInfo;
import com.minivision.feign.extend.model.AuthParam;
import com.minivision.feign.extend.model.DeviceInfoBO;
import com.minivision.feign.extend.model.PageInfo;
import com.minivision.feign.extend.model.QueryDeviceInfoDetailReq;
import com.minivision.feign.extend.model.QueryDeviceInfoReq;
import com.minivision.feign.extend.model.RestResult;
import com.minivision.feign.extend.model.RestResultInfo;
import com.minivision.feign.extend.reset.DeviceRestInner;
import com.minivision.feign.extend.reset.DeviceWelcome;
import com.minivision.feign.extend.reset.FileInner;
import com.minivision.feign.extend.reset.IotAppInner;
import com.netflix.loadbalancer.ILoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Server.class)
public class FeignExtendStarterApplicationTests {

    @Autowired
    private DeviceRestInner deviceRestInner;

    @Autowired
    private IotAppInner iotAppInner;

    @Autowired
    private FileInner fileInner;

    @Autowired
    private DeviceWelcome deviceWelcome;

    @Autowired(required = false)
    private SpringClientFactory springClientFactory;

    @Test
    public void testWelcome(){
        String welcome = deviceWelcome.welcome();
        log.info("welcome result:{}", welcome);
        assert !welcome.isEmpty();
    }

    @Test
    public void testLoadBalance(){
        ILoadBalancer loadBalancer = springClientFactory.getLoadBalancer("FDIOT-PLAT");
        log.info("fdiot load balance:{}", loadBalancer);
        ILoadBalancer fdiot2 = springClientFactory.getLoadBalancer("fdiot2");
        log.info("fdiot2 load balance:{}", fdiot2);
    }

    @Test
    public void contextLoads() {
        QueryDeviceInfoDetailReq queryDeviceInfoDetailReq = new QueryDeviceInfoDetailReq();
        queryDeviceInfoDetailReq.setDeviceCode("1546828402483");
        String appKey = "7bbf10d0bdca560edb30dabb1254964d";
        RestResultInfo<DeviceInfoBO> deviceInfoDetail = deviceRestInner
                .getDeviceInfoDetail(queryDeviceInfoDetailReq, appKey);
        log.info("query result:{}", deviceInfoDetail);

        QueryDeviceInfoReq queryDeviceInfoReq = new QueryDeviceInfoReq();
        queryDeviceInfoReq.setDeviceName("测");
        RestResultInfo<PageInfo<DeviceInfoBO>> pageInfoRestResultInfo = deviceRestInner.queryDeviceListByPage(queryDeviceInfoReq, appKey);
        log.info("page result:{}", pageInfoRestResultInfo);
        RestResultInfo<DeviceInfoBO> cacheDeviceInfoDetail = deviceRestInner.getCacheDeviceInfoDetail(queryDeviceInfoDetailReq, appKey);
        log.info("get cache :{}", cacheDeviceInfoDetail);
    }

    @Test
    public void benchMark() throws InterruptedException {
        QueryDeviceInfoDetailReq queryDeviceInfoDetailReq = new QueryDeviceInfoDetailReq();
        queryDeviceInfoDetailReq.setDeviceCode("1546828402483");
        String appKey = "7bbf10d0bdca560edb30dabb1254964d";
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int a = 0; a < 10; a++) {
            int totalCount = 10000;
            int batch = totalCount / threadCount;
            CountDownLatch countDownLatch = new CountDownLatch(totalCount);
            long start = System.currentTimeMillis();
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    for (int j = 0; j < 10; j++) {
//                        RestResultInfo<DeviceInfoBO> cacheDeviceInfoDetail = deviceRestInner
//                                .getCacheDeviceInfoDetail(queryDeviceInfoDetailReq, appKey);
//                        assert cacheDeviceInfoDetail.getResData().getDeviceCode().equals(queryDeviceInfoDetailReq.getDeviceCode());
                        String welcome = deviceWelcome.welcome();
                        assert !welcome.isEmpty();
                    }
                    for (int k = 0; k < batch; k++) {
                        String welcome = deviceWelcome.welcome();
                        assert !welcome.isEmpty();
                        countDownLatch.countDown();
                    }

                });
            }
            countDownLatch.await();

            log.info("take time :{}ms, total count:{}", (System.currentTimeMillis() - start), totalCount);
        }
    }

    @Test
    public void testApp() {
        String time = String.valueOf(System.currentTimeMillis());
        String appKey = "7bbf10d0bdca560edb30dabb1254964d";
        String token = DigestUtils.md5DigestAsHex(Utf8.encode("7bbf10d0bdca560edb30dabb1254964d{123456:" + time + "}"));
        AuthParam authParam = new AuthParam();
        authParam.setAppKey("7bbf10d0bdca560edb30dabb1254964d");
        authParam.setTimestamp(time);
        RestResult<AppInfo> detail = iotAppInner.detail(authParam, token);
        log.info("app detail:{}", detail);
    }


}
