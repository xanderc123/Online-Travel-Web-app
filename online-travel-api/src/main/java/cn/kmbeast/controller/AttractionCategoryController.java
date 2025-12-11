package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.dto.query.extend.AttractionCategoryQueryDto;
import cn.kmbeast.pojo.entity.AttractionCategory;
import cn.kmbeast.service.AttractionCategoryService;
import org.springframework.web.bind.annotation.*;
import cn.kmbeast.pojo.api.Result;

import javax.annotation.Resource;
import java.util.List;
/*景点分类的控制器*/
@RestController
@RequestMapping(value="/attractionCategory")
public class AttractionCategoryController {
    //增删改查
    //JSON-->实体类（可以直接用）
    //视图层（controller)--业务逻辑层(service)--持久化层(mapper)
    @Resource
    private AttractionCategoryService AttractionCategoryService;

    /**
     * 景点分类新增
     * @param attractionCategory 景点分类实体
     * @return Result<Void></Void>
     */
    @PostMapping(value="/save")
    @ResponseBody
    public Result<Void> save(@RequestBody AttractionCategory attractionCategory ){
        return AttractionCategoryService.save(attractionCategory);
    }
    /**
     * 景点分类修改
     * @param attractionCategory 景点分类实体
     * @return Result<Void>
     */
    @PutMapping(value="/update")
    @ResponseBody
    public Result<Void> update(@RequestBody AttractionCategory attractionCategory){

        return AttractionCategoryService.update(attractionCategory);
    }
    /**
     * 景点批量删除
     * @param ids 景点分类id列表
     * @return Result<Void>
     */
    @PostMapping(value="/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids){
        return AttractionCategoryService.batchDelete(ids);
    }


    /**
     * 查询景点分类
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping (value="/query")
    @ResponseBody
    public Result<List<AttractionCategory>> query(@RequestBody AttractionCategoryQueryDto dto ){
        return AttractionCategoryService.query(dto);
    }


}
