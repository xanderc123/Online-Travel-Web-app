package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttractionStrategyQueryDto extends QueryDto {
    private Integer userId;
    private Integer attractionId;
    private String title;
    private Boolean isAudit;
}
