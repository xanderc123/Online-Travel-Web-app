package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.AttractionStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点攻略VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttractionStrategyVO extends AttractionStrategy {
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 景点名
     */
    private String attractionName;
}
