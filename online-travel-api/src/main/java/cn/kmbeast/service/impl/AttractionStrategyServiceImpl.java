package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.AttractionStrategyMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionStrategyQueryDto;
import cn.kmbeast.pojo.entity.AttractionStrategy;
import cn.kmbeast.pojo.vo.AttractionStrategyListVO;
import cn.kmbeast.pojo.vo.AttractionStrategyVO;
import cn.kmbeast.service.AttractionStrategyService;
import cn.kmbeast.utils.TextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 景点攻略的业务逻辑实现类
 */
@Service
public class AttractionStrategyServiceImpl implements AttractionStrategyService {

    @Resource
    private AttractionStrategyMapper attractionStrategyMapper;

    /**
     * 景点攻略新增
     *
     * @param attractionStrategy 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(AttractionStrategy attractionStrategy) {
        // 设置初始时间
        attractionStrategy.setCreateTime(LocalDateTime.now());
        // 设置上发布者的用户ID
        attractionStrategy.setUserId(LocalThreadHolder.getUserId());
        // 默认未审核
        attractionStrategy.setIsAudit(false);
        attractionStrategyMapper.save(attractionStrategy);
        return ApiResult.success();
    }

    /**
     * 景点攻略修改
     *
     * @param attractionStrategy 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(AttractionStrategy attractionStrategy) {
        attractionStrategyMapper.update(attractionStrategy);
        return ApiResult.success();
    }

    /**
     * 景点攻略删除
     *
     * @param ids 景点攻略ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        attractionStrategyMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 景点攻略查询
     *
     * @param dto 查询实体
     * @return Result<AttractionStrategyVO>
     */
    @Override
    public Result<List<AttractionStrategyVO>> query(AttractionStrategyQueryDto dto) {
        Integer totalCount = attractionStrategyMapper.queryCount(dto);
        List<AttractionStrategyVO> result = attractionStrategyMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }

    /**
     * 景点攻略的审核
     *
     * @param id 景点攻略的ID
     * @return Result<Void>
     */
    @Override
    public Result<Void> audit(Integer id) {
        AttractionStrategy attractionStrategy = new AttractionStrategy();
        attractionStrategy.setId(id);
        // 设置审核状态
        attractionStrategy.setIsAudit(true);
        attractionStrategyMapper.update(attractionStrategy);
        return ApiResult.success("Attraction strategy audit successful");
    }

    /**
     * 查询攻略列表
     *
     * @param dto 查询参数
     * @return Result<List < AttractionStrategyListVO>>
     */
    @Override
    public Result<List<AttractionStrategyListVO>> queryList(AttractionStrategyQueryDto dto) {
        // 只能查已经审核通过的内容
        dto.setIsAudit(true);
        Integer totalCount = attractionStrategyMapper.queryCount(dto);
        List<AttractionStrategyVO> attractionStrategyVOS = attractionStrategyMapper.query(dto);
        List<AttractionStrategyListVO> attractionStrategyListVOS = new ArrayList<>();
        for (AttractionStrategyVO attractionStrategyVO : attractionStrategyVOS) {
            AttractionStrategyListVO attractionStrategyListVO = new AttractionStrategyListVO();
            BeanUtils.copyProperties(attractionStrategyVO, attractionStrategyListVO);
            // 处理简要 --- 现在是富文本类型 --- 需要处理
            String detail = TextUtils.extractText(attractionStrategyVO.getContent(), 200);
            attractionStrategyListVO.setDetail(detail);
            attractionStrategyListVOS.add(attractionStrategyListVO);
        }
        return ApiResult.success(attractionStrategyListVOS, totalCount);
    }
}
