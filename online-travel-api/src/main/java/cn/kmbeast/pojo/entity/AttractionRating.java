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
public class AttractionRating {

    /**
     * 景点编号
     */
    private Integer id;


    private Integer attractionId;
    private Integer userId;
    private Integer grade;
    
    /**
     * 评分时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
