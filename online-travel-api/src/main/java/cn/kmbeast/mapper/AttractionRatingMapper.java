package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.TouristRouteQueryDto;
import cn.kmbeast.pojo.dto.query.extend.AttractionRatingQueryDto;
import cn.kmbeast.pojo.entity.TouristRoute;
import cn.kmbeast.pojo.entity.AttractionRating;
import cn.kmbeast.pojo.vo.TouristRouteVO;
import cn.kmbeast.pojo.vo.AttractionRatingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 景点评分持久化接口
 */
@Mapper
public interface AttractionRatingMapper {

    void save(AttractionRating attractionRating);

    List<AttractionRatingVO> query(AttractionRatingQueryDto dto);

    Integer queryCount(AttractionRatingQueryDto dto);

}
