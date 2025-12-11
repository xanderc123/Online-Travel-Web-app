package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionCategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.entity.AttractionCategory;
import cn.kmbeast.pojo.entity.Supplier;
import cn.kmbeast.pojo.vo.SupplierVO;
import cn.kmbeast.service.SupplierService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/*供应商的控制器*/
@RestController
@RequestMapping(value="/supplier")
public class SupplierController {
    
    @Resource
    private SupplierService supplierService;

    /**
     * 供应商新增
     * @param supplier 供应商实体
     * @return Result<Void></Void>
     */
    @PostMapping(value="/save")
    @ResponseBody
    public Result<Void> save(@RequestBody Supplier supplier ){
        return supplierService.save(supplier);
    }
    /**
     * 供应商修改
     * @param supplier 供应商实体
     * @return Result<Void>
     */
    @PutMapping(value="/update")
    @ResponseBody
    public Result<Void> update(@RequestBody Supplier supplier){

        return supplierService.update(supplier);
    }
    /**
     * 景点批量删除
     * @param ids 供应商id列表
     * @return Result<Void>
     */
    @PostMapping(value="/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids){
        return supplierService.batchDelete(ids);
    }

    /**
     * 查询用户关联的供应商信息
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<SupplierVO>> queryUser() {
        SupplierQueryDto queryDto = new SupplierQueryDto();
        // 取得当前操作者的用户ID，并且设置上查询参数
        queryDto.setUserId(LocalThreadHolder.getUserId());
        return supplierService.query(queryDto);
    }
    /**
     * 查询供应商
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping (value="/query")
    @ResponseBody
    public Result<List<SupplierVO>> query(@RequestBody SupplierQueryDto dto ){
        return supplierService.query(dto);
    }


}
