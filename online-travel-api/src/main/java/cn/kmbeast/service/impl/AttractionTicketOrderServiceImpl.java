package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.AttractionMapper;
import cn.kmbeast.mapper.AttractionTicketMapper;
import cn.kmbeast.mapper.AttractionTicketOrderMapper;
import cn.kmbeast.mapper.SupplierMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.*;
import cn.kmbeast.pojo.entity.AttractionTicket;
import cn.kmbeast.pojo.entity.AttractionTicketOrder;
import cn.kmbeast.pojo.vo.*;
import cn.kmbeast.service.AttractionTicketOrderService;
import cn.kmbeast.utils.DateUtil;
import cn.kmbeast.utils.DecimalUtils;
import cn.kmbeast.utils.MoneyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景区订单的业务逻辑实现类
 */
@Service
public class AttractionTicketOrderServiceImpl implements AttractionTicketOrderService {

    @Resource
    private AttractionTicketOrderMapper attractionTicketOrderMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private AttractionMapper attractionMapper;
    @Resource
    private AttractionTicketMapper attractionTicketMapper;

    /**
     * 景区订单新增
     *
     * @param attractionTicketOrder 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(AttractionTicketOrder attractionTicketOrder) {
        if (!StringUtils.hasText(attractionTicketOrder.getContactPerson()) ||
                !StringUtils.hasText(attractionTicketOrder.getContactNum())) {
            return ApiResult.error("Contact person or contact number cannot be empty");
        }
        AttractionTicketQueryDto attractionTicketQueryDto = new AttractionTicketQueryDto();
        attractionTicketQueryDto.setId(attractionTicketOrder.getTicketId());
        List<AttractionTicketVO> attractionTicketVOS = attractionTicketMapper.query(attractionTicketQueryDto);
        if (attractionTicketVOS.isEmpty()) {
            return ApiResult.error("No ticket information available");
        }
        AttractionTicketVO attractionTicketVO = attractionTicketVOS.get(0);
        Integer attractionId = attractionTicketVO.getAttractionId();
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        attractionQueryDto.setId(attractionId);
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        if (attractionVOS.isEmpty()){
            return ApiResult.error("Attraction information is abnormal");
        }
        // Check if the attraction is available, unavailable means under maintenance
        if (!attractionVOS.get(0).getStatus()) {
            return ApiResult.error("Attraction service is temporarily suspended");
        }
        // Check if the ticket is available
        if (!attractionTicketVO.getTicketStatus()) {
            return ApiResult.error("Ticket is temporarily unavailable");
        }
        // Check if there are enough tickets in stock
        if (attractionTicketOrder.getBuyNumber() > attractionTicketVO.getNumber()) {
            return ApiResult.error("Insufficient ticket inventory");
        }

        BigDecimal amount = DecimalUtils.calculateTotal(
                attractionTicketOrder.getBuyNumber(),
                attractionTicketVO.getPrice(),
                attractionTicketVO.getDiscount() == null ? 1 : (attractionTicketVO.getDiscount() / 10)
        );
        // 设置金额
        attractionTicketOrder.setAmount(amount);
        // 将购买者的用户ID设置上
        attractionTicketOrder.setUserId(LocalThreadHolder.getUserId());
        // 设置初始时间
        attractionTicketOrder.setCreateTime(LocalDateTime.now());
        attractionTicketOrder.setTicketId(attractionTicketVO.getId());
        // 开始的时候，订单的状态是未支付的
        attractionTicketOrder.setPayStatus(false);
        attractionTicketOrderMapper.save(attractionTicketOrder);
        // 改门票数量
        AttractionTicket attractionTicket = new AttractionTicket();
        attractionTicket.setId(attractionTicketVO.getId());
        attractionTicket.setNumber(attractionTicketVO.getNumber() - attractionTicketOrder.getBuyNumber());
        attractionTicketMapper.update(attractionTicket);
        return ApiResult.success("Order placed successfully");
    }

    /**
     * 景区订单修改
     *
     * @param attractionTicketOrder 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(AttractionTicketOrder attractionTicketOrder) {
        attractionTicketOrderMapper.update(attractionTicketOrder);
        return ApiResult.success();
    }

    /**
     * 景区订单删除
     *
     * @param ids 景区订单ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        attractionTicketOrderMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 景区订单查询
     *
     * @param dto 查询实体
     * @return Result<AttractionTicketOrderVO>
     */
    @Override
    public Result<List<AttractionTicketOrderVO>> query(AttractionTicketOrderQueryDto dto) {
        Integer totalCount = attractionTicketOrderMapper.queryCount(dto);
        List<AttractionTicketOrderVO> result = attractionTicketOrderMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }

    /**
     * 查询供应商管理的全部景点的门票订单
     *
     * @return Result<Void>
     */
    @Override
    public Result<List<AttractionTicketOrderVO>> queryAttractionTicketOrder(AttractionTicketOrderQueryDto dto) {
        // 取出供应商信息
        Integer supplierId = getSupplierId();
        if (supplierId == null){
            return ApiResult.success(new ArrayList<>());
        }
        // 查供应商管理的全部的景点信息
        AttractionQueryDto queryDto = new AttractionQueryDto();
        queryDto.setSupplierId(supplierId);
        List<AttractionVO> attractionVOS = attractionMapper.query(queryDto);
        // 取出里面的全部景点ID
        List<Integer> attractionIds = attractionVOS.stream()
                .map(AttractionVO::getId)
                .collect(Collectors.toList());
        // 用户ID --- 供应商ID --- 景点ID --- 门票ID --- 订单信息
        List<AttractionTicketVO> attractionTicketVOS
                = attractionTicketMapper.queryByAttractionIds(attractionIds);
        // 获取门票的Id列表
        List<Integer> attractionTicketIds = attractionTicketVOS.stream()
                .map(AttractionTicketVO::getId)
                .collect(Collectors.toList());
        AttractionTicketQueryParamDto orderQueryParam = new AttractionTicketQueryParamDto(
                attractionTicketIds,
                dto
        );
        List<AttractionTicketOrderVO> attractionTicketOrderVOS =
                attractionTicketOrderMapper.queryByAttractionIds(orderQueryParam);
        Integer totalCount =
                attractionTicketOrderMapper.queryCountByAttractionIds(orderQueryParam);
        return ApiResult.success(attractionTicketOrderVOS, totalCount);
    }

    /**
     * 统计近期成交的金额
     *
     * @param day 往前面查的天数
     * @return Result<List < ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> daysQuery(Integer day) {
        // 获取时间范围
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        AttractionTicketOrderQueryDto dto = new AttractionTicketOrderQueryDto();
        dto.setStartTime(queryDto.getStartTime());
        dto.setEndTime(queryDto.getEndTime());
        Result<List<AttractionTicketOrderVO>> attractionTicketOrder
                = queryAttractionTicketOrder(dto);
        ApiResult<List<AttractionTicketOrderVO>> response = (ApiResult) attractionTicketOrder;
        List<AttractionTicketOrderVO> data = response.getData();
        List<MoneyDto> moneyDtoList = data.stream().map(attractionTicketOrderVO -> new MoneyDto(
                attractionTicketOrderVO.getAmount(),
                attractionTicketOrderVO.getPayTime()
        )).collect(Collectors.toList());
        List<ChartVO> chartVOS = MoneyUtils.countMoney(day, moneyDtoList);
        return ApiResult.success(chartVOS);
    }

    /**
     * 查询用户自己的门票消费金额数据
     *
     * @param day 往前查多少天
     * @return Result<List < ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> daysQueryUser(Integer day) {
        // 获取时间范围
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        AttractionTicketOrderQueryDto dto = new AttractionTicketOrderQueryDto();
        dto.setStartTime(queryDto.getStartTime());
        dto.setEndTime(queryDto.getEndTime());
        // 设置上用户的ID
        dto.setUserId(LocalThreadHolder.getUserId());
        List<AttractionTicketOrderVO> attractionTicketOrderVOS = attractionTicketOrderMapper.query(dto);
        List<MoneyDto> moneyDtoList = attractionTicketOrderVOS.stream()
                .map(attractionTicketOrderVO -> new MoneyDto(
                        attractionTicketOrderVO.getAmount(),
                        attractionTicketOrderVO.getPayTime())).collect(Collectors.toList());
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
        AttractionTicketOrderQueryDto dto = new AttractionTicketOrderQueryDto();
        dto.setStartTime(queryDto.getStartTime());
        dto.setEndTime(queryDto.getEndTime());
        List<AttractionTicketOrderVO> attractionTicketOrderVOS = attractionTicketOrderMapper.query(dto);
        List<MoneyDto> moneyDtoList = attractionTicketOrderVOS.stream()
                .map(attractionTicketOrderVO -> new MoneyDto(
                        attractionTicketOrderVO.getAmount(),
                        attractionTicketOrderVO.getPayTime())).collect(Collectors.toList());
        List<ChartVO> chartVOS = MoneyUtils.countMoney(day, moneyDtoList);
        return ApiResult.success(chartVOS);
    }

    /**
     * 取得供应商ID
     *
     * @return Integer
     */
    private Integer getSupplierId() {
        SupplierQueryDto supplierQueryDto = new SupplierQueryDto();
        // 当前操作者的用户ID
        supplierQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<SupplierVO> supplierVOS = supplierMapper.query(supplierQueryDto);
        // 1. 要么真的没有 2. 有的话，只有一项
        if (CollectionUtils.isEmpty(supplierVOS)) {
            return null;
        }
        return supplierVOS.get(0).getId();
    }

    /**
     * 门票订单支付
     *
     * @param attractionTicketOrder 门票订单数据
     * @return Result<Void>
     */
    @Override
    public Result<Void> pay(AttractionTicketOrder attractionTicketOrder) {
        // 支付状态
        attractionTicketOrder.setPayStatus(true);
        // 支付事件
        attractionTicketOrder.setPayTime(LocalDateTime.now());
        attractionTicketOrderMapper.update(attractionTicketOrder);
        return ApiResult.success();
    }
}
