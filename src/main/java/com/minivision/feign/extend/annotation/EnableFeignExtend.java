package com.minivision.feign.extend.annotation;

import com.minivision.feign.extend.config.FeignExtendConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动注解 <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @date  2019年11月27日 17:12:29 <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignExtendConfig.class)
@EnableFeignExtend
public @interface EnableFeignExtend {
}
