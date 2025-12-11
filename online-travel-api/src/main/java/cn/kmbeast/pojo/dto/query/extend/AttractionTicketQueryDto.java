package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttractionTicketQueryDto extends QueryDto {
    /**
     * 景点ID
     */
    private Integer attractionId;
    /**
     * 门票的介绍
     */
    private String description;
    /**
     * 门票的可用状态
     */
    private Boolean ticketStatus;
}
