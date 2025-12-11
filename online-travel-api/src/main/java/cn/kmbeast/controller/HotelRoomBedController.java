package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelRoomBedQueryDto;
import cn.kmbeast.pojo.entity.HotelRoomBed;
import cn.kmbeast.service.HotelRoomBedService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 酒店房间床位的控制器
 */
@RestController
@RequestMapping("/hotelRoomBed")
public class HotelRoomBedController {

    @Resource
    private HotelRoomBedService hotelRoomBedService;

    /**
     * 酒店房间床位新增
     *
     * @param hotelRoomBed 酒店房间床位实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody HotelRoomBed hotelRoomBed) {
        return hotelRoomBedService.save(hotelRoomBed);
    }

    /**
     * 酒店房间床位修改
     *
     * @param hotelRoomBed 酒店房间床位实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody HotelRoomBed hotelRoomBed) {
        return hotelRoomBedService.update(hotelRoomBed);
    }

    /**
     * 酒店房间床位批量删除
     *
     * @param ids 酒店房间床位Id列表
     * @return Result<Void>
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return hotelRoomBedService.batchDelete(ids);
    }

    /**
     * 查询酒店房间床位
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<HotelRoomBed>> query(@RequestBody HotelRoomBedQueryDto dto) {
        return hotelRoomBedService.query(dto);
    }

}
