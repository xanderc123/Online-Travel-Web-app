package cn.kmbeast.pojo.dto.query.extend;

import lombok.Data;

import java.util.List;

/**
 * 供应商查询门票订单参数入参
 */
@Data
public class AttractionTicketQueryParamDto {

    /**
     * 门票的ID列表
     */
    private List<Integer> ticketIds;
    /**
     * 查询门票订单的参数实体
     */
    private AttractionTicketOrderQueryDto attractionTicketOrderQueryDto;


    public AttractionTicketQueryParamDto(List<Integer> ticketIds, AttractionTicketOrderQueryDto attractionTicketOrderQueryDto) {
        this.ticketIds = ticketIds;
        this.attractionTicketOrderQueryDto = attractionTicketOrderQueryDto;
    }
}
