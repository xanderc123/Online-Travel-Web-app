package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.HotelQueryDto;
import cn.kmbeast.pojo.entity.Hotel;
import cn.kmbeast.pojo.vo.HotelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 酒店持久化接口
 */
@Mapper
public interface HotelMapper {

    void save(Hotel hotel);

    void update(Hotel hotel);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<HotelVO> query(HotelQueryDto dto);

    Integer queryCount(HotelQueryDto dto);

}
