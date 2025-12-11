package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.TouristRouteMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.TouristRouteQueryDto;
import cn.kmbeast.pojo.entity.TouristRoute;
import cn.kmbeast.pojo.vo.TouristRouteVO;
import cn.kmbeast.service.TouristRouteService;
import cn.kmbeast.service.TouristRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TouristRoute Route的业务逻辑实现类
 */
@Service
public class TouristRouteServiceImpl implements TouristRouteService {
    @Resource
    private TouristRouteMapper touristRouteMapper;
    @Override
    public Result<Void> save(TouristRoute touristRoute) {
        //set initial time
        touristRoute.setCreateTime(LocalDateTime.now());
        touristRouteMapper.save(touristRoute);
        return ApiResult.success();
    }

    @Override
    public Result<Void> update(TouristRoute touristRoute) {
        touristRouteMapper.update(touristRoute);
        return ApiResult.success();
    }

    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        touristRouteMapper.batchDelete(ids);
        return ApiResult.success();
    }

    @Override
    public Result<List<TouristRouteVO>> query(TouristRouteQueryDto dto) {
        Integer totalCount = touristRouteMapper.queryCount(dto);
        List<TouristRouteVO> result = touristRouteMapper.query(dto);
        return ApiResult.success(result,totalCount);
    }
}
