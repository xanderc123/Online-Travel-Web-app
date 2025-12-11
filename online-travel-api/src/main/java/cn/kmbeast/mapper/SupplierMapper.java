package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.entity.Supplier;
import cn.kmbeast.pojo.vo.SupplierVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * supplier类持久化接口
 */
@Mapper
public interface SupplierMapper {
    void save(Supplier supplier);

    void update(Supplier supplier);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<SupplierVO> query(SupplierQueryDto dto);

    Integer queryCount(SupplierQueryDto dto);

}
