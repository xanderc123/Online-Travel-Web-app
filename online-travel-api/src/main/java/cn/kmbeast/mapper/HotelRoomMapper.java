package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.HotelRoomQueryDto;
import cn.kmbeast.pojo.entity.HotelRoom;
import cn.kmbeast.pojo.vo.HotelRoomVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 酒店房间持久化接口
 */
@Mapper
public interface HotelRoomMapper {

    void save(HotelRoom hotelRoom);

    void update(HotelRoom hotelRoom);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<HotelRoomVO> querySupplierRoom(HotelRoomQueryDto dto);

    Integer querySupplierRoomCount(HotelRoomQueryDto dto);

    List<HotelRoomVO> query(HotelRoomQueryDto dto);

    Integer queryCount(HotelRoomQueryDto dto);

    List<HotelRoomVO> queryByHotelIds(@Param(value = "ids") List<Integer> ids);

}
