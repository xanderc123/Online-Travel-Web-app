package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Supplier;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierVO extends Supplier {
    /**
     * query supplier
     */
    private String userName;



}
