package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.HotelMapper;
import cn.kmbeast.mapper.HotelOrderInfoMapper;
import cn.kmbeast.mapper.HotelRoomMapper;
import cn.kmbeast.mapper.SupplierMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.*;
import cn.kmbeast.pojo.entity.HotelOrderInfo;
import cn.kmbeast.pojo.vo.*;
import cn.kmbeast.service.HotelOrderInfoService;
import cn.kmbeast.utils.DateUtil;
import cn.kmbeast.utils.DecimalUtils;
import cn.kmbeast.utils.MoneyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒店订单的业务逻辑实现类
 */
@Service
public class HotelOrderInfoServiceImpl implements HotelOrderInfoService {

    @Resource
    private HotelOrderInfoMapper hotelOrderInfoMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private HotelMapper hotelMapper;
    @Resource
    private HotelRoomMapper hotelRoomMapper;

    /**
     * 酒店订单新增
     *
     * @param hotelOrderInfo 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(HotelOrderInfo hotelOrderInfo) {
        if (!StringUtils.hasText(hotelOrderInfo.getContactPerson())){
            return ApiResult.error("Contact person cannot be empty");
        }
        if (!StringUtils.hasText(hotelOrderInfo.getContactNum())){
            return ApiResult.error("Contact phone number cannot be empty");
        }
        HotelRoomQueryDto hotelRoomQueryDto = new HotelRoomQueryDto();
        hotelRoomQueryDto.setId(hotelOrderInfo.getRoomId());
        List<HotelRoomVO> hotelRoomVOS = hotelRoomMapper.query(hotelRoomQueryDto);
        if (hotelRoomVOS.isEmpty()) {
            return ApiResult.error("Room information not found");
        }
        // Resolve duplicate order issue
        HotelOrderInfoQueryDto queryDto = new HotelOrderInfoQueryDto();
        queryDto.setRoomId(hotelOrderInfo.getRoomId());
        queryDto.setCurrent(1);
        queryDto.setSize(1);
        List<HotelOrderInfoVO> orderInfoVOS = hotelOrderInfoMapper.query(queryDto);
        if (!orderInfoVOS.isEmpty()) {
            HotelOrderInfoVO hotelOrderInfoVO = orderInfoVOS.get(0);
            LocalDateTime createTime = hotelOrderInfoVO.getCreateTime();
            LocalDateTime dateTime = LocalDateTime.now();
            // Calculate day difference
            long days = ChronoUnit.DAYS.between(createTime, dateTime);
            // Because the design is for one night purchase
            if (days < 1) {
                return ApiResult.error("Room has already been booked");
            }
        }
        HotelRoomVO hotelRoomVO = hotelRoomVOS.get(0);
        Double discount = hotelRoomVO.getDiscount();
        // 没有设计买多少晚，这里使用1就行
        BigDecimal amount = DecimalUtils.calculateTotal(
                1,
                hotelRoomVO.getPrice(),
                discount == null ? 1 : (discount / 10)
        );
        hotelOrderInfo.setAmount(amount);
        // 设置用户ID
        hotelOrderInfo.setUserId(LocalThreadHolder.getUserId());
        // 设置初始时间
        hotelOrderInfo.setCreateTime(LocalDateTime.now());
        // 开始的时候，订单的状态是未支付的
        hotelOrderInfo.setPayStatus(false);
        hotelOrderInfoMapper.save(hotelOrderInfo);
        return ApiResult.success();
    }

    /**
     * 酒店订单修改
     *
     * @param hotelOrderInfo 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(HotelOrderInfo hotelOrderInfo) {
        hotelOrderInfoMapper.update(hotelOrderInfo);
        return ApiResult.success();
    }

    /**
     * 酒店订单删除
     *
     * @param ids 酒店订单ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        hotelOrderInfoMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 酒店订单查询
     *
     * @param dto 查询实体
     * @return Result<HotelOrderInfoVO>
     */
    @Override
    public Result<List<HotelOrderInfoVO>> query(HotelOrderInfoQueryDto dto) {
        Integer totalCount = hotelOrderInfoMapper.queryCount(dto);
        List<HotelOrderInfoVO> result = hotelOrderInfoMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }


    /**
     * 查询供应商名下的酒店订单
     *
     * @return Result<HotelOrderInfoVO>
     */
    @Override
    public Result<List<HotelOrderInfoVO>> queryHotelOrderSupplier(HotelOrderInfoQueryDto dto) {
        // 链路： 用户ID  --- 供应商  --- 名下管理的酒店 --- 房间 --- 订单（用户下单）
        // 当前操作者的用户ID
        Integer userId = LocalThreadHolder.getUserId();
        SupplierQueryDto supplierQueryDto = new SupplierQueryDto();
        supplierQueryDto.setUserId(userId);
        // 名下管理的供应商信息
        List<SupplierVO> supplierVOS = supplierMapper.query(supplierQueryDto);
        // 这是没有的判断，反之，过了if，一定存在并且只有一项
        if (supplierVOS.isEmpty()) {
            return ApiResult.error("Supplier information is abnormal.");
        }
        SupplierVO supplierVO = supplierVOS.get(0);
        // Only suppliers that have passed audit and are in normal status are eligible to query
        if (!supplierVO.getStatus() && !supplierVO.getIsAudit()) {
            return ApiResult.error("Supplier status is abnormal or no viewing permission.");
        }
        // Query hotel information under this supplier
        HotelQueryDto hotelQueryDto = new HotelQueryDto();
        hotelQueryDto.setSupplierId(supplierVO.getId());
        List<HotelVO> hotelVOS = hotelMapper.query(hotelQueryDto);
        if (hotelVOS.isEmpty()) {
            return ApiResult.error("No managed hotels under this account.");
        }
        // 过滤取得酒店的Id集合列表
        List<Integer> hotelIds = hotelVOS.stream()
                .map(HotelVO::getId)
                .collect(Collectors.toList());
        // 通过酒店的的这一批ID，去获取他们名下的房间信息
        List<HotelRoomVO> hotelRoomVOS = hotelRoomMapper.queryByHotelIds(hotelIds);
        // 过滤获取房间的ID集合列表
        List<Integer> hotelRoomIds = hotelRoomVOS.stream()
                .map(HotelRoomVO::getId)
                .collect(Collectors.toList());
        // 构造查询条件
        HotelOrderInfoQueryParamDto paramDto = new HotelOrderInfoQueryParamDto(
                hotelRoomIds,
                dto
        );
        // 查询指定房间下的所有符合条件订单
        List<HotelOrderInfoVO> orderInfoVOList
                = hotelOrderInfoMapper.queryByHotelRoomIds(paramDto);
        Integer totalCount
                = hotelOrderInfoMapper.queryCountByHotelRoomIds(paramDto);
        return ApiResult.success(orderInfoVOList, totalCount);
    }

    /**
     * 统计成交金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(Integer day) {
        // 获取时间范围
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        HotelOrderInfoQueryDto dto = new HotelOrderInfoQueryDto();
        dto.setStartTime(queryDto.getStartTime());
        dto.setEndTime(queryDto.getEndTime());
        Result<List<HotelOrderInfoVO>> hotelOrderSupplier
                = queryHotelOrderSupplier(dto);
        ApiResult<List<HotelOrderInfoVO>> response = (ApiResult) hotelOrderSupplier;
        List<HotelOrderInfoVO> data = response.getData();
        List<MoneyDto> moneyDtoList = data.stream().map(hotelOrderInfoVO -> new MoneyDto(
                hotelOrderInfoVO.getAmount(),
                hotelOrderInfoVO.getPayTime()
        )).collect(Collectors.toList());
        List<ChartVO> chartVOS = MoneyUtils.countMoney(day, moneyDtoList);
        return ApiResult.success(chartVOS);
    }

    /**
     * 统计全站指定日期里面的成交门票金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQueryMoney(Integer day) {
        // 获取时间范围
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        HotelOrderInfoQueryDto dto = new HotelOrderInfoQueryDto();
        dto.setStartTime(queryDto.getStartTime());
        dto.setEndTime(queryDto.getEndTime());
        List<HotelOrderInfoVO> orderInfoVOS = hotelOrderInfoMapper.query(dto);
        List<MoneyDto> moneyDtoList = orderInfoVOS.stream().map(hotelOrderInfoVO -> new MoneyDto(
                hotelOrderInfoVO.getAmount(),
                hotelOrderInfoVO.getPayTime()
        )).collect(Collectors.toList());
        List<ChartVO> chartVOS = MoneyUtils.countMoney(day, moneyDtoList);
        return ApiResult.success(chartVOS);
    }

    /**
     * 酒店订单支付
     *
     * @param hotelOrderInfo 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> pay(HotelOrderInfo hotelOrderInfo) {
        hotelOrderInfo.setPayStatus(true);
        hotelOrderInfo.setPayTime(LocalDateTime.now());
        hotelOrderInfoMapper.update(hotelOrderInfo);
        return ApiResult.success();
    }
}
