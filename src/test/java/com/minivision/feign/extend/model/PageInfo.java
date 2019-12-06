package com.minivision.feign.extend.model;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019年11月27日 10:50:18 <br>
 */
public class PageInfo<T> {
    private int pageNum = 1;

    private int pageSize = 0;

    private long total = 0L;

    private List<T> rows;

    public int getPageNum() {
        return this.pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public long getTotal() {
        return this.total;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageInfo)) {
            return false;
        } else {
            PageInfo<?> other = (PageInfo) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getPageNum() != other.getPageNum()) {
                return false;
            } else if (this.getPageSize() != other.getPageSize()) {
                return false;
            } else if (this.getTotal() != other.getTotal()) {
                return false;
            } else {
                Object this$rows = this.getRows();
                Object other$rows = other.getRows();
                if (this$rows == null) {
                    if (other$rows != null) {
                        return false;
                    }
                } else if (!this$rows.equals(other$rows)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageInfo;
    }

    @Override
    public int hashCode() {
        int PRIME = 0;
        int result = 1;
        result = result * 59 + this.getPageNum();
        result = result * 59 + this.getPageSize();
        long $total = this.getTotal();
        result = result * 59 + (int) ($total >>> 32 ^ $total);
        Object $rows = this.getRows();
        result = result * 59 + ($rows == null ? 43 : $rows.hashCode());
        return result;
    }
    @Override
    public String toString() {
        return "PageInfo(pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ", total=" + this.getTotal() + ", rows=" + this
                .getRows() + ")";
    }

    @ConstructorProperties({ "pageNum", "pageSize", "total", "rows" })
    public PageInfo(int pageNum, int pageSize, long total, List<T> rows) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = rows;
    }

    public PageInfo() {
    }
}

