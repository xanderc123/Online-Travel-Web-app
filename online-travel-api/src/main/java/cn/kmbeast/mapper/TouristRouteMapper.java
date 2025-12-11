package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.TouristRouteQueryDto;
import cn.kmbeast.pojo.entity.TouristRoute;
import cn.kmbeast.pojo.vo.TouristRouteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * supplier类持久化接口
 */
@Mapper
public interface TouristRouteMapper {
    void save(TouristRoute touristRoute);

    void update(TouristRoute touristRoute);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<TouristRouteVO> query(TouristRouteQueryDto dto);

    Integer queryCount(TouristRouteQueryDto dto);

}
