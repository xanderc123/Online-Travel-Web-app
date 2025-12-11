package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.HotelMapper;
import cn.kmbeast.mapper.HotelRoomMapper;
import cn.kmbeast.mapper.SupplierMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelQueryDto;
import cn.kmbeast.pojo.dto.query.extend.HotelRoomQueryDto;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.entity.Hotel;
import cn.kmbeast.pojo.entity.HotelRoom;
import cn.kmbeast.pojo.vo.HotelRoomVO;
import cn.kmbeast.pojo.vo.HotelVO;
import cn.kmbeast.pojo.vo.SupplierVO;
import cn.kmbeast.service.HotelRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒店房间的业务逻辑实现类
 */
@Service
public class HotelRoomServiceImpl implements HotelRoomService {

    @Resource
    private HotelRoomMapper hotelRoomMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private HotelMapper hotelMapper;

    /**
     * 酒店房间新增
     *
     * @param hotelRoom 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(HotelRoom hotelRoom) {
        // 设置初始时间
        hotelRoom.setCreateTime(LocalDateTime.now());
        // 一个用户 --- 一个供应商信息 ----管理多个酒店
        hotelRoomMapper.save(hotelRoom);
        return ApiResult.success();
    }

    /**
     * 酒店房间修改
     *
     * @param hotelRoom 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(HotelRoom hotelRoom) {
        hotelRoomMapper.update(hotelRoom);
        return ApiResult.success();
    }

    /**
     * 酒店房间删除
     *
     * @param ids 酒店房间ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        hotelRoomMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 酒店房间查询 -- 做了权限隔离的
     *
     * @param dto 查询实体
     * @return Result<HotelRoomVO>
     */
    @Override
    public Result<List<HotelRoomVO>> querySupplierRoom(HotelRoomQueryDto dto) {
        // 1. 先查供应商信息
        SupplierQueryDto supplierQueryDto = new SupplierQueryDto();
        supplierQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<SupplierVO> supplierVOS = supplierMapper.query(supplierQueryDto);
        // 两种情况：第一种是没有申请；第二种是已经申请了但是没有审核。
        // 未申请
        if (supplierVOS.isEmpty()) {
            return ApiResult.success();
        }
        SupplierVO supplierVO = supplierVOS.get(0);
        // 未审核
        if (!supplierVO.getIsAudit()) {
            return ApiResult.success();
        }
        Integer supplierId = supplierVO.getId(); // 供应商ID
        // 2. 通过供应商ID查询所管辖的酒店
        HotelQueryDto hotelQueryDto = new HotelQueryDto();
        hotelQueryDto.setSupplierId(supplierId);
        List<HotelVO> hotelVOS = hotelMapper.query(hotelQueryDto);
        // 如果发现供应商账户下并没有管理有酒店，直接退出
        if (hotelVOS.isEmpty()) {
            return ApiResult.success();
        }
        // 通过stream流，取出酒店ID集合
        List<Integer> hotelIds = hotelVOS.stream()
                .map(Hotel::getId)
                .collect(Collectors.toList());
        dto.setHotelIds(hotelIds);
        Integer totalCount = hotelRoomMapper.querySupplierRoomCount(dto);
        List<HotelRoomVO> result = hotelRoomMapper.querySupplierRoom(dto);
        return ApiResult.success(result, totalCount);
    }

    /**
     * 酒店房间查询
     *
     * @param dto 查询参数
     * @return 后台响应
     */
    @Override
    public Result<List<HotelRoomVO>> query(HotelRoomQueryDto dto) {
        List<HotelRoomVO> hotelRoomVOS = hotelRoomMapper.query(dto);
        Integer count = hotelRoomMapper.queryCount(dto);
        return ApiResult.success(hotelRoomVOS, count);
    }
}
