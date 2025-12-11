package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.HotelOrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 酒店订单的VO出参类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HotelOrderInfoVO extends HotelOrderInfo {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 房间号
     */
    private String roomName;
    /**
     * 酒店名
     */
    private String hotelName;
}
