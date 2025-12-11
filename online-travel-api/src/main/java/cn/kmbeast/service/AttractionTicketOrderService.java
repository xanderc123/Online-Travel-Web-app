package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionTicketOrderQueryDto;
import cn.kmbeast.pojo.entity.AttractionTicketOrder;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.AttractionTicketOrderVO;

import java.util.List;

public interface AttractionTicketOrderService {

    Result<Void> save(AttractionTicketOrder attractionTicketOrder);

    Result<Void> update(AttractionTicketOrder attractionTicketOrder);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<AttractionTicketOrderVO>> query(AttractionTicketOrderQueryDto dto);

    Result<List<AttractionTicketOrderVO>> queryAttractionTicketOrder(AttractionTicketOrderQueryDto dto);

    Result<List<ChartVO>> daysQuery(Integer day);

    Result<Void> pay(AttractionTicketOrder attractionTicketOrder);


    Result<List<ChartVO>> daysQueryUser(Integer day);

    Result<List<ChartVO>> daysQueryMoney(Integer day);

}
