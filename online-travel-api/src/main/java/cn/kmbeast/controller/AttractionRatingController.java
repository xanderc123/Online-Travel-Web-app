package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionRatingQueryDto;
import cn.kmbeast.pojo.entity.AttractionRating;
import cn.kmbeast.pojo.vo.AttractionRatingVO;
import cn.kmbeast.service.AttractionRatingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 景点评分的控制器
 */
@RestController
@RequestMapping("/attractionRating")
public class AttractionRatingController {

    @Resource
    private AttractionRatingService attractionRatingService;

    /**
     * 景点评分新增
     *
     * @param attractionRating 景点评分实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody AttractionRating attractionRating) {
        return attractionRatingService.save(attractionRating);
    }

    /**
     * 查询景点评分
     *
     * @return Result<List<AttractionRatingVO>>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<AttractionRatingVO>> query(@RequestBody AttractionRatingQueryDto dto) {
        return attractionRatingService.query(dto);
    }

    /**
     * 查询用户对于景点的评分
     *
     * @return Result<List<AttractionRatingVO>>
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<AttractionRatingVO>> queryUser(@RequestBody AttractionRatingQueryDto dto) {
        dto.setUserId(LocalThreadHolder.getUserId());
        return attractionRatingService.query(dto);
    }

}
