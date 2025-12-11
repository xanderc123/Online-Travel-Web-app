package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Attraction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)

public class AttractionVO extends Attraction {
    private String categoryName;
    private String supplierName;
    private Double ratingScore;
}
