package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelQueryDto;
import cn.kmbeast.pojo.entity.Hotel;
import cn.kmbeast.pojo.vo.HotelVO;

import java.util.List;

public interface HotelService {

    Result<Void> save(Hotel hotel);

    Result<Void> update(Hotel hotel);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<HotelVO>> query(HotelQueryDto dto);

    Result<List<HotelVO>> queryHotelSupplier(HotelQueryDto dto);

}
