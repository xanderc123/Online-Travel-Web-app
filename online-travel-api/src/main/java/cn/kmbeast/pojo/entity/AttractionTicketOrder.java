package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评分实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionTicketOrder {

    /**
     * 门票订单编号
     */
    private Integer id;
    private Integer userId;
    private Integer ticketId;
    private String contactPerson;
    private String contactNum;
    private BigDecimal amount;
    private Integer buyNumber;
    private Boolean payStatus;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;


    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
