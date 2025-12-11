package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.AttractionMapper;
import cn.kmbeast.mapper.AttractionTicketMapper;
import cn.kmbeast.mapper.SupplierMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.AttractionTicketQueryDto;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.entity.Attraction;
import cn.kmbeast.pojo.entity.AttractionTicket;
import cn.kmbeast.pojo.vo.AttractionTicketVO;
import cn.kmbeast.pojo.vo.AttractionVO;
import cn.kmbeast.pojo.vo.SupplierVO;
import cn.kmbeast.service.AttractionTicketService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景点门票的业务逻辑实现类
 */
@Service
public class AttractionTicketServiceImpl implements AttractionTicketService {

    @Resource
    private AttractionTicketMapper attractionTicketMapper;
    @Resource
    private AttractionMapper attractionMapper;
    @Resource
    private SupplierMapper supplierMapper;

    /**
     * 景点门票新增
     *
     * @param attractionTicket 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(AttractionTicket attractionTicket) {
        // 设置初始时间
        attractionTicket.setCreateTime(LocalDateTime.now());
        attractionTicketMapper.save(attractionTicket);
        return ApiResult.success();
    }

    /**
     * 景点门票修改
     *
     * @param attractionTicket 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(AttractionTicket attractionTicket) {
        attractionTicketMapper.update(attractionTicket);
        return ApiResult.success();
    }

    /**
     * 景点门票删除
     *
     * @param ids 景点门票ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        attractionTicketMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 景点门票查询
     *
     * @param dto 查询实体
     * @return Result<AttractionTicketVO>
     */
    @Override
    public Result<List<AttractionTicketVO>> query(AttractionTicketQueryDto dto) {
        Integer totalCount = attractionTicketMapper.queryCount(dto);
        List<AttractionTicketVO> result = attractionTicketMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }

    /**
     * 查询供应商所管理的景点门票信息
     *
     * @return Result<Void>
     */
    @Override
    public Result<List<AttractionTicketVO>> queryAttractionTicket() {
        // 用户名下的供应商信息ID
        Integer supplierId = getSupplierId();
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        attractionQueryDto.setSupplierId(supplierId);
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        // 取出所有景点的ID
        List<Integer> attractionIds = attractionVOS.stream()
                .map(Attraction::getId)
                .collect(Collectors.toList());
        List<AttractionTicketVO> attractionTicketVOS =
                attractionTicketMapper.queryByAttractionIds(attractionIds);

        return ApiResult.success(attractionTicketVOS);
    }


    private Integer getSupplierId() {
        SupplierQueryDto supplierQueryDto = new SupplierQueryDto();
        // 由用户ID去查回来的供应商信息
        supplierQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<SupplierVO> supplierVOS = supplierMapper.query(supplierQueryDto);
        // 1. 要么真的没有 2. 有的话，只有一项
        if (supplierVOS.isEmpty()) {
            return null;
        }
        return supplierVOS.get(0).getId();
    }
}
