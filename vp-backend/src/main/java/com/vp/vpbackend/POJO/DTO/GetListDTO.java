package com.vp.vpbackend.POJO.DTO;

import com.vp.vpbackend.enums.FileSortType;
import lombok.Data;

@Data
public class GetListDTO {
    private Integer pageNum;
    private Integer pageSize;
    private FileSortType sortType;
}
