package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.TouristRouteQueryDto;
import cn.kmbeast.pojo.entity.TouristRoute;
import cn.kmbeast.pojo.vo.TouristRouteVO;
import cn.kmbeast.service.TouristRouteService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/*route的控制器*/
@RestController
@RequestMapping(value="/touristRoute")
public class TouristRouteController {
    
    @Resource
    private TouristRouteService touristRouteService;

    /**
     * route新增
     * @param touristRoute route实体
     * @return Result<Void></Void>
     */
    @PostMapping(value="/save")
    @ResponseBody
    public Result<Void> save(@RequestBody TouristRoute touristRoute ){
        return touristRouteService.save(touristRoute);
    }
    /**
     * route修改
     * @param touristRoute 实体
     * @return Result<Void>
     */
    @PutMapping(value="/update")
    @ResponseBody
    public Result<Void> update(@RequestBody TouristRoute touristRoute){

        return touristRouteService.update(touristRoute);
    }
    /**
     * 批量删除
     * @param ids routeid列表
     * @return Result<Void>
     */
    @PostMapping(value="/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids){
        return touristRouteService.batchDelete(ids);
    }


    /**
     * 查询route
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping (value="/query")
    @ResponseBody
    public Result<List<TouristRouteVO>> query(@RequestBody TouristRouteQueryDto dto ){
        return touristRouteService.query(dto);
    }


}
