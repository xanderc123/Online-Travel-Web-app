package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.HotelRoom;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 酒店房间的VO出参类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HotelRoomVO extends HotelRoom {
    /**
     * 酒店名称
     */
    private String hotelName;
}
