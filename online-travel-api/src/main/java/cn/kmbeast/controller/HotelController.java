package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelQueryDto;
import cn.kmbeast.pojo.entity.Hotel;
import cn.kmbeast.pojo.vo.HotelVO;
import cn.kmbeast.service.HotelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 酒店的控制器
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Resource
    private HotelService hotelService;

    /**
     * 酒店新增
     *
     * @param hotel 酒店实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody Hotel hotel) {
        return hotelService.save(hotel);
    }

    /**
     * 酒店修改
     *
     * @param hotel 酒店实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody Hotel hotel) {
        return hotelService.update(hotel);
    }

    /**
     * 酒店批量删除
     *
     * @param ids 酒店Id列表
     * @return Result<Void>
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return hotelService.batchDelete(ids);
    }

    /**
     * 查询供应商名下维护的酒店信息
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/queryHotelSupplier")
    @ResponseBody
    public Result<List<HotelVO>> queryHotelSupplier(@RequestBody HotelQueryDto dto) {
        return hotelService.queryHotelSupplier(dto);
    }

    /**
     * 查询酒店
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<HotelVO>> query(@RequestBody HotelQueryDto dto) {
        return hotelService.query(dto);
    }

}
