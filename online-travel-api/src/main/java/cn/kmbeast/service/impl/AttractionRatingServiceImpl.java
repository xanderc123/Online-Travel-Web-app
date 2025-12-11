package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.AttractionRatingMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionRatingQueryDto;
import cn.kmbeast.pojo.entity.AttractionRating;
import cn.kmbeast.pojo.vo.AttractionRatingVO;
import cn.kmbeast.service.AttractionRatingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 景点评分的业务逻辑实现类
 */
@Service
public class AttractionRatingServiceImpl implements AttractionRatingService {

    @Resource
    private AttractionRatingMapper attractionRatingMapper;

    /**
     * 景点评分新增
     *
     * @param attractionRating 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(AttractionRating attractionRating) {
        AttractionRatingQueryDto attractionRatingQueryDto = new AttractionRatingQueryDto();
        attractionRatingQueryDto.setAttractionId(attractionRating.getAttractionId());
        attractionRatingQueryDto.setUserId(LocalThreadHolder.getUserId());
        Integer queryCount = attractionRatingMapper.queryCount(attractionRatingQueryDto);
        if (queryCount != 0) {
            return ApiResult.error("Already rated");
        }
        // Set rating time
        attractionRating.setUserId(LocalThreadHolder.getUserId());
        attractionRating.setCreateTime(LocalDateTime.now());
        attractionRatingMapper.save(attractionRating);
        return ApiResult.success("Rating successful");
    }

    /**
     * 景点评分查询
     *
     * @param dto 查询实体
     * @return Result<List < AttractionRatingVO>>
     */
    @Override
    public Result<List<AttractionRatingVO>> query(AttractionRatingQueryDto dto) {
        Integer totalCount = attractionRatingMapper.queryCount(dto);
        List<AttractionRatingVO> result = attractionRatingMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }
}
