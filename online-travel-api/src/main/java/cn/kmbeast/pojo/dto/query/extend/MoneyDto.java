package cn.kmbeast.pojo.dto.query.extend;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MoneyDto {
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 成交时间
     */
    private LocalDateTime payTime;
}
