package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评分实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class attraction_rating {

    /**
     * 景点编号
     */
    private Integer id;


    private Integer attraction_id;
    private Integer user_id;
    private Integer grade;
    
    /**
     * 评分时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
