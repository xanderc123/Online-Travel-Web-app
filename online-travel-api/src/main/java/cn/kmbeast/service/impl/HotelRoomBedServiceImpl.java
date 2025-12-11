package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.HotelRoomBedMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelRoomBedQueryDto;
import cn.kmbeast.pojo.entity.HotelRoomBed;
import cn.kmbeast.service.HotelRoomBedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 酒店房间床位的业务逻辑实现类
 */
@Service
public class HotelRoomBedServiceImpl implements HotelRoomBedService {

    @Resource
    private HotelRoomBedMapper hotelRoomBedMapper;

    /**
     * 酒店房间床位新增
     *
     * @param hotelRoomBed 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(HotelRoomBed hotelRoomBed) {
        // 设置初始时间
        hotelRoomBed.setCreateTime(LocalDateTime.now());
        hotelRoomBedMapper.save(hotelRoomBed);
        return ApiResult.success();
    }

    /**
     * 酒店房间床位修改
     *
     * @param hotelRoomBed 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(HotelRoomBed hotelRoomBed) {
        hotelRoomBedMapper.update(hotelRoomBed);
        return ApiResult.success();
    }

    /**
     * 酒店房间床位删除
     *
     * @param ids 酒店房间床位ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        hotelRoomBedMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 酒店房间床位查询
     *
     * @param dto 查询实体
     * @return Result<HotelRoomBed>
     */
    @Override
    public Result<List<HotelRoomBed>> query(HotelRoomBedQueryDto dto) {
        Integer totalCount = hotelRoomBedMapper.queryCount(dto);
        List<HotelRoomBed> result = hotelRoomBedMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }
}
