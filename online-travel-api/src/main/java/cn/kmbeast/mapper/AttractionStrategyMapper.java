package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.AttractionStrategyQueryDto;
import cn.kmbeast.pojo.entity.AttractionStrategy;
import cn.kmbeast.pojo.vo.AttractionStrategyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 景点攻略持久化接口
 */
@Mapper
public interface AttractionStrategyMapper {

    void save(AttractionStrategy attractionStrategy);

    void update(AttractionStrategy attractionStrategy);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<AttractionStrategyVO> query(AttractionStrategyQueryDto dto);

    Integer queryCount(AttractionStrategyQueryDto dto);

}
