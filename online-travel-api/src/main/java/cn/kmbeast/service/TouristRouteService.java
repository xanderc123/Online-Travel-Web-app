package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.TouristRouteQueryDto;
import cn.kmbeast.pojo.entity.TouristRoute;
import cn.kmbeast.pojo.vo.TouristRouteVO;

import java.util.List;

public interface TouristRouteService {


    Result<Void> save(TouristRoute touristRoute);

    Result<Void> update(TouristRoute touristRoute);

    Result<Void> batchDelete(List<Integer> ids);
    /**
     * @param dto 查询实体
     * @return Result<Void>
     */
    Result<List<TouristRouteVO>> query(TouristRouteQueryDto dto);

}
