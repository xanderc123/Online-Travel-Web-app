package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionTicketQueryDto;
import cn.kmbeast.pojo.entity.AttractionTicket;
import cn.kmbeast.pojo.vo.AttractionTicketVO;

import java.util.List;

public interface AttractionTicketService {

    Result<Void> save(AttractionTicket attractionTicket);

    Result<Void> update(AttractionTicket attractionTicket);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<AttractionTicketVO>> query(AttractionTicketQueryDto dto);

    Result<List<AttractionTicketVO>> queryAttractionTicket();

}
