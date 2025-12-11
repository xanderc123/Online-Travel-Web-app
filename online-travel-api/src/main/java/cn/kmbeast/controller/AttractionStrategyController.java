package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionStrategyQueryDto;
import cn.kmbeast.pojo.entity.AttractionStrategy;
import cn.kmbeast.pojo.vo.AttractionStrategyListVO;
import cn.kmbeast.pojo.vo.AttractionStrategyVO;
import cn.kmbeast.service.AttractionStrategyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 景点攻略的控制器
 */
@RestController
@RequestMapping("/attractionStrategy")
public class AttractionStrategyController {

    @Resource
    private AttractionStrategyService attractionStrategyService;

    /**
     * 景点攻略新增
     *
     * @param attractionStrategy 景点攻略实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody AttractionStrategy attractionStrategy) {
        return attractionStrategyService.save(attractionStrategy);
    }

    /**
     * 景点攻略审核
     *
     * @return Result<Void>
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/audit/{id}")
    @ResponseBody
    public Result<Void> audit(@PathVariable Integer id) {
        return attractionStrategyService.audit(id);
    }

    /**
     * 景点攻略修改
     *
     * @param attractionStrategy 景点攻略实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody AttractionStrategy attractionStrategy) {
        return attractionStrategyService.update(attractionStrategy);
    }

    /**
     * 景点攻略批量删除
     *
     * @param ids 景点攻略Id列表
     * @return Result<Void>
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return attractionStrategyService.batchDelete(ids);
    }

    /**
     * 查询攻略列表
     *
     * @return Result<List < AttractionStrategyListVO>>
     */
    @Pager
    @PostMapping(value = "/queryList")
    @ResponseBody
    public Result<List<AttractionStrategyListVO>> queryList(@RequestBody AttractionStrategyQueryDto dto) {
        return attractionStrategyService.queryList(dto);
    }

    /**
     * 查询用户发表的景点攻略
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<AttractionStrategyVO>> queryUser(@RequestBody AttractionStrategyQueryDto dto) {
        // 设置上用户ID，数据隔离
        dto.setUserId(LocalThreadHolder.getUserId());
        return attractionStrategyService.query(dto);
    }


    /**
     * 查询景点攻略
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<AttractionStrategyVO>> query(@RequestBody AttractionStrategyQueryDto dto) {
        return attractionStrategyService.query(dto);
    }

}
