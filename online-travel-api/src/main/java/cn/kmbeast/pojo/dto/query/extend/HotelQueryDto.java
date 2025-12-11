package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HotelQueryDto extends QueryDto {
    /**
     * 酒店名称
     */
    private String name;
    /**
     * 供应商/服务商ID
     */
    private Integer supplierId;
}
