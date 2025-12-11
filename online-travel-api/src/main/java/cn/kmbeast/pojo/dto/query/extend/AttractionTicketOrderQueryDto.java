package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点门票信息参数接受类Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttractionTicketOrderQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 景点ID
     */
    private Integer attractionId;
    /**
     * 门票ID
     */
    private Integer ticketId;
    /**
     * 支付状态
     */
    private Boolean payStatus;
}
