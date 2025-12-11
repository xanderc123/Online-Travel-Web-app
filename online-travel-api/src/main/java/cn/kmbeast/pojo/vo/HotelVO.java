package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Hotel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 酒店的VO出参类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HotelVO extends Hotel {
    /**
     * 供应商名称
     */
    private String supplierName;
}
