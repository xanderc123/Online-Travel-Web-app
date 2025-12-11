package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelRoomQueryDto;
import cn.kmbeast.pojo.entity.HotelRoom;
import cn.kmbeast.pojo.vo.HotelRoomVO;

import java.util.List;

public interface HotelRoomService {

    Result<Void> save(HotelRoom hotelRoom);

    Result<Void> update(HotelRoom hotelRoom);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<HotelRoomVO>> querySupplierRoom(HotelRoomQueryDto dto);

    Result<List<HotelRoomVO>> query(HotelRoomQueryDto dto);

}
