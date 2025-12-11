package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.AttractionRating;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点评分的出参类VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttractionRatingVO extends AttractionRating {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
}
