package com.minivision.feign.extend.model;

import lombok.Data;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年12月02日 10:19:51 <br>
 */
@Data
public class RestResult<T> {
    public static final int SUCCESS = 0;

    private String requestId;

    private int timeUsed;

    private int status;

    private String message;

    private T data;

}
