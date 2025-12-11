package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.entity.Attraction;
import cn.kmbeast.pojo.entity.Supplier;
import cn.kmbeast.pojo.vo.AttractionVO;
import cn.kmbeast.pojo.vo.SelectedVO;

import java.util.List;

public interface AttractionService {


    Result<Void> save(Attraction attraction);

    Result<Void> update(Attraction attraction);

    Result<Void> batchDelete(List<Integer> ids);
    /**
     * @param dto 查询实体
     * @return Result<Void>
     */
    Result<List<AttractionVO>> query(AttractionQueryDto dto);

    Result<List<AttractionVO>> querySupplierAttraction(AttractionQueryDto dto);

    Result<List<SelectedVO>> querySelectedAttraction();

    Result<Void> viewOperation(Integer attractionId);

    Result<Boolean> saveStatus(Integer attractionId);

    Result<Void> saveOperation(Integer attractionId);

    Result<List<AttractionVO>> querySave();
}
