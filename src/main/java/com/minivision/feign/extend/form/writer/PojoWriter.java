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

package com.minivision.feign.extend.form.writer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.util.Map;

import static com.minivision.feign.extend.util.PojoUtil.isUserPojo;
import static com.minivision.feign.extend.util.PojoUtil.toMap;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Artem Labazin
 */
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PojoWriter extends AbstractWriter {

    Iterable<Writer> writers;

    @Override
    public boolean isApplicable(Object object) {
        return isUserPojo(object);
    }

    @Override
    public void write(Output output, String boundary, String key, Object object) {
        writer0(output, boundary, key, object, 0);
    }

    void writer0(Output output, String boundary, String key, Object object, int level) {
        Map<String, Object> map = null;
        if (level > 0) {
            map = toMap(object, key);
        } else {
            map = toMap(object, null);
        }
        for (val entry : map.entrySet()) {
            val writer = findApplicableWriter(entry.getValue());
            if (writer == null) {
                continue;
            }
            if (writer == this) {
                this.writer0(output, boundary, entry.getKey(), entry.getValue(), ++level);
            } else {
                writer.write(output, boundary, entry.getKey(), entry.getValue());
            }
        }
    }

    private Writer findApplicableWriter(Object value) {
        for (val writer : writers) {
            if (writer.isApplicable(value)) {
                return writer;
            }
        }
        return null;
    }
}
