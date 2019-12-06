package com.minivision.feign.extend.form.processor;

import feign.MethodMetadata;
import feign.Param;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static feign.Util.checkState;
import static feign.Util.emptyToNull;

/**
 * 扩展 @Param 支持 <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @date  2019年12月04日 17:45:18 <br>
 */
public class ParamAnnotationProcessor implements AnnotatedParameterProcessor {
    @Override public Class<? extends Annotation> getAnnotationType() {
        return Param.class;
    }

    @Override public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
        Param paramAnnotation = (Param) annotation;
        String name = paramAnnotation.value();
        int paramIndex = context.getParameterIndex();
        checkState(emptyToNull(name) != null, "Param annotation was empty on param %s.",
                paramIndex);
        MethodMetadata methodMetadata = context.getMethodMetadata();
        nameParam(methodMetadata, name, paramIndex);
        Class<? extends Param.Expander> expander = paramAnnotation.expander();
        if (expander != Param.ToStringExpander.class) {
            methodMetadata.indexToExpanderClass().put(paramIndex, expander);
        }
        methodMetadata.indexToEncoded().put(paramIndex, paramAnnotation.encoded());
        if (!methodMetadata.template().queries().containsKey(name)) {
            methodMetadata.formParams().add(name);
        }
        return true;
    }

    /**
     * links a parameter name to its index in the method signature.
     */
    private void nameParam(MethodMetadata data, String name, int i) {
        data.indexToName().computeIfAbsent(i, k -> new ArrayList<>()).add(name);
    }

}
