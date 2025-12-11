package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelOrderInfoQueryDto;
import cn.kmbeast.pojo.entity.HotelOrderInfo;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.HotelOrderInfoVO;
import cn.kmbeast.service.HotelOrderInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 酒店订单的控制器
 */
@RestController
@RequestMapping("/hotelOrderInfo")
public class HotelOrderInfoController {

    @Resource
    private HotelOrderInfoService hotelOrderInfoService;

    /**
     * 酒店订单新增
     *
     * @param hotelOrderInfo 酒店订单实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody HotelOrderInfo hotelOrderInfo) {
        return hotelOrderInfoService.save(hotelOrderInfo);
    }

    /**
     * 酒店订单修改
     *
     * @param hotelOrderInfo 酒店订单实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody HotelOrderInfo hotelOrderInfo) {
        return hotelOrderInfoService.update(hotelOrderInfo);
    }

    /**
     * 酒店订单批量删除
     *
     * @param ids 酒店订单Id列表
     * @return Result<Void>
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return hotelOrderInfoService.batchDelete(ids);
    }

    /**
     * 查询供应商名下的酒店订单
     *
     * @return Result<HotelOrderInfoVO>
     */
    @Pager
    @PostMapping(value = "/queryHotelOrderSupplier")
    @ResponseBody
    public Result<List<HotelOrderInfoVO>> queryHotelOrderSupplier(@RequestBody HotelOrderInfoQueryDto dto) {
        return hotelOrderInfoService.queryHotelOrderSupplier(dto);
    }

    /**
     * 查询酒店订单
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<HotelOrderInfoVO>> query(@RequestBody HotelOrderInfoQueryDto dto) {
        return hotelOrderInfoService.query(dto);
    }

    /**
     * 酒店订单支付
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/pay")
    @ResponseBody
    public Result<Void> pay(@RequestBody HotelOrderInfo hotelOrderInfo) {
        return hotelOrderInfoService.pay(hotelOrderInfo);
    }

    /**
     * 查询用户的酒店订单
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<HotelOrderInfoVO>> queryUser(@RequestBody HotelOrderInfoQueryDto dto) {
        // 设置上用户ID
        dto.setUserId(LocalThreadHolder.getUserId());
        return hotelOrderInfoService.query(dto);
    }

    /**
     * 统计全站指定日期里面的成交门票金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/daysQueryMoney/{day}")
    @ResponseBody
    public Result<List<ChartVO>> daysQueryMoney(@PathVariable Integer day) {
        return hotelOrderInfoService.daysQueryMoney(day);
    }

    /**
     * 统计成交金额
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/daysQuery/{day}")
    @ResponseBody
    public Result<List<ChartVO>> query(@PathVariable Integer day) {
        return hotelOrderInfoService.daysQuery(day);
    }

}
