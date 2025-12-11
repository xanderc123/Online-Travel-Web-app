package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.HotelRoomBedQueryDto;
import cn.kmbeast.pojo.entity.HotelRoomBed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 酒店房间床位持久化接口
 */
@Mapper
public interface HotelRoomBedMapper {

    void save(HotelRoomBed hotelRoomBed);

    void update(HotelRoomBed hotelRoomBed);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<HotelRoomBed> query(HotelRoomBedQueryDto dto);

    Integer queryCount(HotelRoomBedQueryDto dto);

}
