package org.gan.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;//当前页的数据
    private int pageNumber;//当前页码(从0开始)
    private int pageSize;
    private long totalElements;//每页大小
    private int totalPages;//总记录数
    private boolean first;//是否是第一页
    private boolean last;//是否是最后一页
}
