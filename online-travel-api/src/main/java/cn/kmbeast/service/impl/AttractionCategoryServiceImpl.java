package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.AttractionCategoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionCategoryQueryDto;
import cn.kmbeast.pojo.entity.AttractionCategory;
import cn.kmbeast.service.AttractionCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 景点分类的业务逻辑实现类
 */
@Service
public class AttractionCategoryServiceImpl implements AttractionCategoryService {
    @Resource
    private  AttractionCategoryMapper attractionCategoryMapper;
    @Override
    public Result<Void> save(AttractionCategory attractionCategory) {
        attractionCategoryMapper.save(attractionCategory);
        return ApiResult.success();
    }

    @Override
    public Result<Void> update(AttractionCategory attractionCategory) {
        attractionCategoryMapper.update(attractionCategory);
        return ApiResult.success();
    }

    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        attractionCategoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    @Override
    public Result<List<AttractionCategory>> query(AttractionCategoryQueryDto dto) {
        Integer totalCount = attractionCategoryMapper.queryCount(dto);
        List<AttractionCategory> result = attractionCategoryMapper.query(dto);
        return ApiResult.success(result,totalCount);
    }
}
