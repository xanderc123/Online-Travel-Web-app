package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelOrderInfoQueryDto;
import cn.kmbeast.pojo.entity.HotelOrderInfo;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.HotelOrderInfoVO;

import java.util.List;

public interface HotelOrderInfoService {

    Result<Void> save(HotelOrderInfo hotelOrderInfo);

    Result<Void> update(HotelOrderInfo hotelOrderInfo);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<HotelOrderInfoVO>> query(HotelOrderInfoQueryDto dto);

    Result<List<HotelOrderInfoVO>> queryHotelOrderSupplier(HotelOrderInfoQueryDto dto);

    Result<List<ChartVO>> daysQuery(Integer day);

    Result<Void> pay(HotelOrderInfo hotelOrderInfo);

    Result<List<ChartVO>> daysQueryMoney(Integer day);

}
