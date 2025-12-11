package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionRatingQueryDto;
import cn.kmbeast.pojo.entity.TouristRoute;
import cn.kmbeast.pojo.entity.AttractionRating;
import cn.kmbeast.pojo.vo.AttractionRatingVO;

import java.util.List;

/**
 * 景点评分业务逻辑接口
 */
public interface AttractionRatingService {

    Result<Void> save(AttractionRating attractionRating);

    Result<List<AttractionRatingVO>> query(AttractionRatingQueryDto dto);

}
