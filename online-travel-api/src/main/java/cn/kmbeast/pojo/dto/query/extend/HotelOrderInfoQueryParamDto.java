package cn.kmbeast.pojo.dto.query.extend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 供应商查询酒店订单参数入参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelOrderInfoQueryParamDto {

    /**
     * 酒店房间的ID列表
     */
    private List<Integer> hotelRoomIds;
    /**
     * 查询酒店订单的参数实体
     */
    private HotelOrderInfoQueryDto hotelOrderInfoQueryDto;

}
