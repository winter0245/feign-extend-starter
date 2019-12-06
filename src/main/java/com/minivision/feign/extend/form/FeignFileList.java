package com.minivision.feign.extend.form;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年12月05日 16:27:17 <br>
 */
public class FeignFileList<E> extends ArrayList {
    public FeignFileList(Collection<? extends E> c) {
        super(c);
    }

    public FeignFileList() {
        super();
    }

    public FeignFileList(int initialCapacity) {
        super(initialCapacity);
    }
}
