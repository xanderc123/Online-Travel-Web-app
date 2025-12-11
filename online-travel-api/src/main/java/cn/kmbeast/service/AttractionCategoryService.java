package cn.kmbeast.service;

import cn.kmbeast.mapper.AttractionCategoryMapper;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionCategoryQueryDto;
import cn.kmbeast.pojo.entity.AttractionCategory;

import javax.annotation.Resource;
import java.util.List;

public interface AttractionCategoryService {


    Result<Void> save(AttractionCategory attractionCategory);

    Result<Void> update(AttractionCategory attractionCategory);

    Result<Void> batchDelete(List<Integer> ids);
    /**
     * 景点分类新增
     * @param dto 查询实体
     * @return Result<Void>
     */
    Result<List<AttractionCategory>> query(AttractionCategoryQueryDto dto);

}
