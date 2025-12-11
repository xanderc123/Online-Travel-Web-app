package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.entity.Supplier;
import cn.kmbeast.pojo.vo.SupplierVO;

import javax.annotation.Resource;
import java.util.List;

public interface SupplierService {


    Result<Void> save(Supplier supplier);

    Result<Void> update(Supplier supplier);

    Result<Void> batchDelete(List<Integer> ids);
    /**
     * @param dto 查询实体
     * @return Result<Void>
     */
    Result<List<SupplierVO>> query(SupplierQueryDto dto);

}
