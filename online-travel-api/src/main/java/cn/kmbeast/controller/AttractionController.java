package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionQueryDto;

import cn.kmbeast.pojo.entity.Attraction;
import cn.kmbeast.pojo.vo.AttractionVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.AttractionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/*景点的控制器*/
@RestController
@RequestMapping(value="/attraction")
public class AttractionController {
    
    @Resource
    private AttractionService attractionService;

    /**
     * 景点新增
     * @param attraction 景点实体
     * @return Result<Void></Void>
     */
    @PostMapping(value="/save")
    @ResponseBody
    public Result<Void> save(@RequestBody Attraction attraction ){
        return attractionService.save(attraction);
    }
    /**
     * 景点修改
     * @param attraction 景点实体
     * @return Result<Void>
     */
    @PutMapping(value="/update")
    @ResponseBody
    public Result<Void> update(@RequestBody Attraction attraction){

        return attractionService.update(attraction);
    }
    /**
     * 批量删除
     * @param ids 景点id列表
     * @return Result<Void>
     */
    @PostMapping(value="/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids){
        return attractionService.batchDelete(ids);
    }
    /**
     * 查询供应商名下管理的景点信息
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/querySupplierAttraction")
    @ResponseBody
    public Result<List<AttractionVO>> querySupplierAttraction(@RequestBody AttractionQueryDto dto) {
        return attractionService.querySupplierAttraction(dto);
    }

    /**
     * 关联景点下拉选择器
     *
     * @return Result<List<SelectedVO>>
     */
    @GetMapping(value = "/querySelectedAttraction")
    @ResponseBody
    public Result<List<SelectedVO>> querySelectedAttraction() {
        return attractionService.querySelectedAttraction();
    }

    /**
     * 浏览操作
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/viewOperation/{attractionId}")
    @ResponseBody
    public Result<Void> viewOperation(@PathVariable Integer attractionId) {
        return attractionService.viewOperation(attractionId);
    }

    /**
     * 收藏操作
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/saveOperation/{attractionId}")
    @ResponseBody
    public Result<Void> saveOperation(@PathVariable Integer attractionId) {
        return attractionService.saveOperation(attractionId);
    }

    /**
     * 查询用户的收藏景点状况
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/saveStatus/{attractionId}")
    @ResponseBody
    public Result<Boolean> saveStatus(@PathVariable Integer attractionId) {
        return attractionService.saveStatus(attractionId);
    }

    /**
     * 查询用户收藏的景点信息
     *
     * @return List<AttractionVO>
     */
    @Pager
    @GetMapping(value = "/querySave")
    @ResponseBody
    public Result<List<AttractionVO>> querySave() {
        return attractionService.querySave();
    }

    /**
     * 查询景点
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping (value="/query")
    @ResponseBody
    public Result<List<AttractionVO>> query(@RequestBody AttractionQueryDto dto ){
        return attractionService.query(dto);
    }


}
