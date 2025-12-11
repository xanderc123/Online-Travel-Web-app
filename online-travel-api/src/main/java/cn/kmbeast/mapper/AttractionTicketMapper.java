package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.AttractionTicketQueryDto;
import cn.kmbeast.pojo.entity.AttractionTicket;
import cn.kmbeast.pojo.vo.AttractionTicketVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 景点门票持久化接口
 */
@Mapper
public interface AttractionTicketMapper {

    void save(AttractionTicket attractionTicket);

    void update(AttractionTicket attractionTicket);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<AttractionTicketVO> query(AttractionTicketQueryDto dto);

    Integer queryCount(AttractionTicketQueryDto dto);

    List<AttractionTicketVO> queryByAttractionIds(@Param(value = "ids")  List<Integer> ids);

}
