package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.*;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.*;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.service.ViewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页可视化
 */
@Service
public class ViewsServiceImpl implements ViewsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private AttractionMapper attractionMapper;
    @Resource
    private HotelMapper hotelMapper;
    @Resource
    private AttractionStrategyMapper attractionStrategyMapper;
    @Resource
    private AttractionTicketOrderMapper attractionTicketOrderMapper;
    @Resource
    private HotelOrderInfoMapper hotelOrderInfoMapper;

    /**
     * 统计一些系统的基础数据
     *
     * @return Result<List < ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> staticControls() {
        List<ChartVO> chartVOS = new ArrayList<>();
        // 1. User count
        UserQueryDto userQueryDto = new UserQueryDto();
        int userCount = userMapper.queryCount(userQueryDto);
        change(userCount, "Existing Users", chartVOS);
        // 2. Included attractions
        Integer attractionCount = attractionMapper.queryCount(new AttractionQueryDto());
        change(attractionCount, "Included Attractions", chartVOS);
        // 3. Partner hotels
        Integer hotelCount = hotelMapper.queryCount(new HotelQueryDto());
        change(hotelCount, "Partner Hotels", chartVOS);
        // 4. Included guides
        Integer strategyCount = attractionStrategyMapper.queryCount(new AttractionStrategyQueryDto());
        change(strategyCount, "Included Guides", chartVOS);
        // 5. Ticket orders
        Integer attractionTicketOrderCount = attractionTicketOrderMapper.queryCount(new AttractionTicketOrderQueryDto());
        change(attractionTicketOrderCount, "Ticket Orders", chartVOS);
        // 6. Hotel orders
        Integer hotelOrderInfoCount = hotelOrderInfoMapper.queryCount(new HotelOrderInfoQueryDto());
        change(hotelOrderInfoCount, "Hotel Orders", chartVOS);
        return ApiResult.success(chartVOS);
    }

    /**
     * 参数处理
     *
     * @param count    总数目
     * @param name     统计项
     * @param chartVOS 装它们的集合
     */
    private void change(Integer count, String name, List<ChartVO> chartVOS) {
        ChartVO chartVO = new ChartVO(name, count);
        chartVOS.add(chartVO);
    }


}
