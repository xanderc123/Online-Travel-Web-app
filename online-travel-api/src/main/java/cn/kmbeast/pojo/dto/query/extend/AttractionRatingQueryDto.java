package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点评分查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttractionRatingQueryDto extends QueryDto {
    /**
     * Attraction_id
     */
    private Integer attractionId;
    /**
     * User_id
     */
    private Integer userId;
}
