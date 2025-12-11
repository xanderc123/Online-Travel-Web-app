package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelRoomBedQueryDto;
import cn.kmbeast.pojo.entity.HotelRoomBed;

import java.util.List;

public interface HotelRoomBedService {

    Result<Void> save(HotelRoomBed hotelRoomBed);

    Result<Void> update(HotelRoomBed hotelRoomBed);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<HotelRoomBed>> query(HotelRoomBedQueryDto dto);

}
