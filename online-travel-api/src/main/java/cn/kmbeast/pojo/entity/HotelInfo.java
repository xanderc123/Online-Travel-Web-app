package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfo {

    /**
     * 用户编号
     */
    private Integer id;

    /**
     * hotel_name
     */
    private String name;
    /**
     *
     */
    private String cover;
    /**
     *
     */
    private String address;
    /**
     *
     */
    private String contact_num;
    /**
     *
     */
    private Integer supplier_id;
    /**
     *
     * 用户注册时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
