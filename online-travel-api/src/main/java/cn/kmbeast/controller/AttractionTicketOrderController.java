package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionTicketOrderQueryDto;
import cn.kmbeast.pojo.entity.AttractionTicketOrder;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.AttractionTicketOrderVO;
import cn.kmbeast.service.AttractionTicketOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 景点门票的控制器
 */
@RestController
@RequestMapping("/attractionTicketOrder")
public class AttractionTicketOrderController {

    @Resource
    private AttractionTicketOrderService attractionTicketOrderService;

    /**
     * 景点门票新增
     *
     * @param attractionTicketOrder 景点门票实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody AttractionTicketOrder attractionTicketOrder) {
        return attractionTicketOrderService.save(attractionTicketOrder);
    }

    /**
     * 景点门票修改
     *
     * @param attractionTicketOrder 景点门票实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody AttractionTicketOrder attractionTicketOrder) {
        return attractionTicketOrderService.update(attractionTicketOrder);
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
        return attractionTicketOrderService.batchDelete(ids);
    }

    /**
     * 查询供应商管理的全部景点的门票订单
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/queryAttractionTicketOrder")
    @ResponseBody
    public Result<List<AttractionTicketOrderVO>> queryAttractionTicketOrder(@RequestBody AttractionTicketOrderQueryDto dto) {
        return attractionTicketOrderService.queryAttractionTicketOrder(dto);
    }

    /**
     * 门票订单支付
     *
     * @return Result<List < AttractionTicketOrderVO>>
     */
    @PostMapping(value = "/pay")
    @ResponseBody
    public Result<Void> pay(@RequestBody AttractionTicketOrder attractionTicketOrder) {
        return attractionTicketOrderService.pay(attractionTicketOrder);
    }

    /**
     * 查询用户的景点门票数据
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<AttractionTicketOrderVO>> queryUser(@RequestBody AttractionTicketOrderQueryDto dto) {
        // 设置上用户ID
        dto.setUserId(LocalThreadHolder.getUserId());
        return attractionTicketOrderService.query(dto);
    }


    /**
     * 查询景点门票
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<AttractionTicketOrderVO>> query(@RequestBody AttractionTicketOrderQueryDto dto) {
        return attractionTicketOrderService.query(dto);
    }

    /**
     * 统计成交金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/daysQueryUser/{day}")
    @ResponseBody
    public Result<List<ChartVO>> daysQueryUser(@PathVariable Integer day) {
        return attractionTicketOrderService.daysQueryUser(day);
    }

    /**
     * 统计全站指定日期里面的成交门票金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/daysQueryMoney/{day}")
    @ResponseBody
    public Result<List<ChartVO>> daysQueryMoney(@PathVariable Integer day) {
        return attractionTicketOrderService.daysQueryMoney(day);
    }

    /**
     * 统计成交金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/daysQuery/{day}")
    @ResponseBody
    public Result<List<ChartVO>> query(@PathVariable Integer day) {
        return attractionTicketOrderService.daysQuery(day);
    }


}
