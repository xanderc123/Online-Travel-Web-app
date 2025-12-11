package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.SupplierMapper;

import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.em.RoleEnum;
import cn.kmbeast.pojo.entity.Supplier;
import cn.kmbeast.pojo.vo.SupplierVO;
import cn.kmbeast.service.SupplierService;
import cn.kmbeast.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Supplier的业务逻辑实现类
 */
@Service
public class SupplierServiceImpl implements SupplierService {
    @Resource
    private SupplierMapper supplierMapper;
    @Override
    public Result<Void> save(Supplier supplier) {
        if(!StringUtils.hasText(supplier.getName())){
            return ApiResult.error("Supplier name cannot be empty");
        }
        if(!StringUtils.hasText(supplier.getEmail())){
            return ApiResult.error("Email cannot be empty");
        }
        if(!StringUtils.hasText(supplier.getContactPerson())){
            return ApiResult.error("Contact person cannot be empty");
        }
        if(!StringUtils.hasText(supplier.getContactNumber())){
            return ApiResult.error("Contact number cannot be empty");
        }
        if(!StringUtils.hasText(supplier.getProductType())){
            return ApiResult.error("Product type cannot be empty");
        }
        // Only validate legal representative if the user is an administrator
        if (Objects.equals(LocalThreadHolder.getRoleId(),
                RoleEnum.ADMIN.getRole())) {
            if(supplier.getUserId() == null){
                return ApiResult.error("Legal representative cannot be empty");
            }
        }
        // Each person can only have one application record
        SupplierQueryDto queryDto = new SupplierQueryDto();
        queryDto.setUserId(supplier.getUserId());
        if (!Objects.equals(LocalThreadHolder.getRoleId(),
                RoleEnum.ADMIN.getRole())) {
            queryDto.setUserId(LocalThreadHolder.getUserId());
        }
        Integer count = supplierMapper.queryCount(queryDto);
        if (count != 0) {
            return ApiResult.error("This legal representative is already bound to a supplier. Please review");
        }
        // Set creation time
        supplier.setCreateTime(LocalDateTime.now());
        // Set initial supplier status - default is active (just not audited when newly added)
        supplier.setStatus(true);
        // Additional logic - if added by administrator, legal representative is the passed ID (user ID)
        // Otherwise, it's an application made by the frontend operator themselves
        if (!Objects.equals(LocalThreadHolder.getRoleId(),
                RoleEnum.ADMIN.getRole())) {
            // Set user ID
            supplier.setUserId(LocalThreadHolder.getUserId());
            // User application, initially unaudited
            supplier.setIsAudit(false);
        } else {
            // Administrator operation, default is audited
            supplier.setIsAudit(true);
        }
        supplierMapper.save(supplier);
        return ApiResult.success();
    }

    @Override
    public Result<Void> update(Supplier supplier) {
        supplierMapper.update(supplier);
        return ApiResult.success();
    }

    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        supplierMapper.batchDelete(ids);
        return ApiResult.success();
    }

    @Override
    public Result<List<SupplierVO>> query(SupplierQueryDto dto) {
        Integer totalCount = supplierMapper.queryCount(dto);
        List<SupplierVO> result = supplierMapper.query(dto);
        return ApiResult.success(result,totalCount);
    }
}
