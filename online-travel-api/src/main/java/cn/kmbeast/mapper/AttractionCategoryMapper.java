package cn.kmbeast.mapper;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionCategoryQueryDto;
import cn.kmbeast.pojo.entity.AttractionCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 景点分类持久化接口
 */
@Mapper
public interface AttractionCategoryMapper {
    void save(AttractionCategory attractionCategory);

    void update(AttractionCategory attractionCategory);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<AttractionCategory> query(AttractionCategoryQueryDto dto);

    Integer queryCount(AttractionCategoryQueryDto dto);

}
