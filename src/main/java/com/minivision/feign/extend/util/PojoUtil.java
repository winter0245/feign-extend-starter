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

package com.minivision.feign.extend.util;

import com.minivision.feign.extend.form.FormProperty;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.rmi.UnexpectedException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Artem Labazin
 */
public final class PojoUtil {

    public static boolean isUserPojo(@NonNull Object object) {
        val type = object.getClass();
        val packageName = type.getPackage().getName();
        return !packageName.startsWith("java.");
    }

    public static boolean isUserPojo(@NonNull Type type) {

        val typeName = type.toString();

        return !typeName.startsWith("class java.");
    }

    @SneakyThrows
    public static Map<String, Object> toMap(@NonNull Object object, String key) {
        val result = new HashMap<String, Object>(16);
        val type = object.getClass();
        val setAccessibleAction = new SetAccessibleAction();
        for (val field : type.getDeclaredFields()) {
            val modifiers = field.getModifiers();
            if (isFinal(modifiers) || isStatic(modifiers)) {
                continue;
            }
            setAccessibleAction.setField(field);
            AccessController.doPrivileged(setAccessibleAction);

            val fieldValue = field.get(object);
            if (fieldValue == null) {
                continue;
            }

            val propertyKey = field.isAnnotationPresent(FormProperty.class)
                    ? field.getAnnotation(FormProperty.class).value()
                    : field.getName();
            if (key != null && key.length() > 0) {
                result.put(key + "." + propertyKey, fieldValue);
            } else {
                result.put(propertyKey, fieldValue);
            }
        }
        return result;
    }



    private PojoUtil() throws UnexpectedException {
        throw new UnexpectedException("It is not allowed to instantiate this class");
    }

    @Setter
    @NoArgsConstructor
    @FieldDefaults(level = PRIVATE)
    private static class SetAccessibleAction implements PrivilegedAction<Object> {

        Field field;

        @Override
        public Object run() {
            field.setAccessible(true);
            return null;
        }
    }
}
