package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionStrategyQueryDto;
import cn.kmbeast.pojo.entity.AttractionStrategy;
import cn.kmbeast.pojo.vo.AttractionStrategyListVO;
import cn.kmbeast.pojo.vo.AttractionStrategyVO;

import java.util.List;

public interface AttractionStrategyService {

    Result<Void> save(AttractionStrategy attractionStrategy);

    Result<Void> update(AttractionStrategy attractionStrategy);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<AttractionStrategyVO>> query(AttractionStrategyQueryDto dto);

    Result<Void> audit(Integer id);

    Result<List<AttractionStrategyListVO>> queryList(AttractionStrategyQueryDto dto);


}
