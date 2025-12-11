package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelRoomQueryDto;
import cn.kmbeast.pojo.entity.HotelRoom;
import cn.kmbeast.pojo.vo.HotelRoomVO;
import cn.kmbeast.service.HotelRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 酒店房间的控制器
 */
@RestController
@RequestMapping("/hotelRoom")
public class HotelRoomController {

    @Resource
    private HotelRoomService hotelRoomService;

    /**
     * 酒店房间新增
     *
     * @param hotelRoom 酒店房间实体
     * @return Result<Void>
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<Void> save(@RequestBody HotelRoom hotelRoom) {
        return hotelRoomService.save(hotelRoom);
    }

    /**
     * 酒店房间修改
     *
     * @param hotelRoom 酒店房间实体
     * @return Result<Void>
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<Void> update(@RequestBody HotelRoom hotelRoom) {
        return hotelRoomService.update(hotelRoom);
    }

    /**
     * 酒店房间批量删除
     *
     * @param ids 酒店房间Id列表
     * @return Result<Void>
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return hotelRoomService.batchDelete(ids);
    }

    /**
     * 查询酒店房间
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<HotelRoomVO>> query(@RequestBody HotelRoomQueryDto dto) {
        return hotelRoomService.query(dto);
    }

    /**
     * 查询酒店房间 -- 做了权限隔离的
     *
     * @return Result<Void>
     */
    @Pager
    @PostMapping(value = "/querySupplierRoom")
    @ResponseBody
    public Result<List<HotelRoomVO>> querySupplierRoom(@RequestBody HotelRoomQueryDto dto) {
        return hotelRoomService.querySupplierRoom(dto);
    }

}
