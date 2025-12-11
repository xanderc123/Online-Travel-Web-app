package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionTicketQueryDto;
import cn.kmbeast.pojo.entity.AttractionTicket;
import cn.kmbeast.pojo.vo.AttractionTicketVO;
import cn.kmbeast.service.AttractionTicketService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 景点门票的控制器
 */
@RestController
@RequestMapping("/attractionTicket")
public class AttractionTicketController {

    @Resource
    private AttractionTicketService attractionTicketService;

    /**
     * 景点门票新增
     *
     * @param attractionTicket 景点门票实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody AttractionTicket attractionTicket) {
        return attractionTicketService.save(attractionTicket);
    }

    /**
     * 景点门票修改
     *
     * @param attractionTicket 景点门票实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody AttractionTicket attractionTicket) {
        return attractionTicketService.update(attractionTicket);
    }

    /**
     * 景点门票批量删除
     *
     * @param ids 景点门票Id列表
     * @return Result<Void>
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return attractionTicketService.batchDelete(ids);
    }


    /**
     * 查询供应商所管理的景点门票信息
     *
     * @return Result<Void>
     */
    @PostMapping(value = "/queryAttractionTicket")
    @ResponseBody
    public Result<List<AttractionTicketVO>> queryAttractionTicket() {
        return attractionTicketService.queryAttractionTicket();
    }

    /**
     * 查询景点门票
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<AttractionTicketVO>> query(@RequestBody AttractionTicketQueryDto dto) {
        return attractionTicketService.query(dto);
    }

}
