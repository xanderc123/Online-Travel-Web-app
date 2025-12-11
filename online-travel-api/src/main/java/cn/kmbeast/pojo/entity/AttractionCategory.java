package cn.kmbeast.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionCategory {

    /**
     * 景点编号
     */
    private Integer id;

    private String name;

    public void save(AttractionCategory attractionCategory) {
    }
}
