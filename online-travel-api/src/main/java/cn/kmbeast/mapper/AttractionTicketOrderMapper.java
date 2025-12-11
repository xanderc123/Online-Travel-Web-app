package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.AttractionTicketOrderQueryDto;
import cn.kmbeast.pojo.dto.query.extend.AttractionTicketQueryParamDto;
import cn.kmbeast.pojo.entity.AttractionTicketOrder;
import cn.kmbeast.pojo.vo.AttractionTicketOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 景区订单持久化接口
 */
@Mapper
public interface AttractionTicketOrderMapper {

    void save(AttractionTicketOrder attractionTicketOrder);

    void update(AttractionTicketOrder attractionTicketOrder);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<AttractionTicketOrderVO> query(AttractionTicketOrderQueryDto dto);

    Integer queryCount(AttractionTicketOrderQueryDto dto);

    List<AttractionTicketOrderVO> queryByAttractionIds(AttractionTicketQueryParamDto paramDto);

    Integer queryCountByAttractionIds(AttractionTicketQueryParamDto paramDto);

}
