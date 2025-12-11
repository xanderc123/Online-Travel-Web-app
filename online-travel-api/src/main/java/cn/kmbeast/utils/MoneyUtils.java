package cn.kmbeast.utils;

import cn.kmbeast.pojo.dto.query.extend.MoneyDto;
import cn.kmbeast.pojo.vo.ChartVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 货币工具类
 */
public class MoneyUtils {

    /**
     * 统计指定时间之内的成交额
     *
     * @param dayRange     往前面查的时间范围
     * @param moneyDtoList 成交金额统计实体
     * @return List<ChartVO>
     */
    public static List<ChartVO> countMoney(Integer dayRange, List<MoneyDto> moneyDtoList) {
        LocalDate startDate = LocalDate.now().minusDays(dayRange);
        List<ChartVO> chartVOS = new ArrayList<>();
        for (int offset = 0; offset <= dayRange; offset++) {
            LocalDate currentDate = startDate.plusDays(offset);
            String dateKey = String.format("%02d-%02d", currentDate.getMonthValue(), currentDate.getDayOfMonth());
            double totalAmount = moneyDtoList.stream()
                    .filter(order->order.getPayTime() != null)
                    .filter(order -> order.getPayTime().toLocalDate().equals(currentDate))
                    .mapToDouble(order -> order.getAmount().doubleValue())
                    .sum();
            if (totalAmount != 0) {
                chartVOS.add(new ChartVO(dateKey, totalAmount));
            }
        }
        return chartVOS;
    }
}
