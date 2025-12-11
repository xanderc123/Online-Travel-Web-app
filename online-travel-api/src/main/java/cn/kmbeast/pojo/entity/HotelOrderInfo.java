package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelOrderInfo {

    /**
     * order_id
     */
    private Integer id;

    private Integer roomId;
    private Integer userId;
    private String contactPerson;
    private String contactNum;
    private BigDecimal amount;
    private Boolean  payStatus;
    private LocalDateTime createTime;

    /**
     *
     * 用户注册时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
}
