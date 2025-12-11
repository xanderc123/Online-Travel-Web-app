package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.AttractionQueryDto;
import cn.kmbeast.pojo.entity.Attraction;
import cn.kmbeast.pojo.vo.AttractionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * supplier类持久化接口
 */
@Mapper
public interface AttractionMapper {
    void save(Attraction attraction);

    void update(Attraction attraction);

    void updateSaveIds(Attraction attraction);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<AttractionVO> query(AttractionQueryDto dto);

    Integer queryCount(AttractionQueryDto dto);

}
