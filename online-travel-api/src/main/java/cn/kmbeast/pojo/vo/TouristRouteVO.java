package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.TouristRoute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点路线的VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TouristRouteVO extends TouristRoute {
    /**
     * 景点名称
     */
    private String attractionName;
    /**
     * 景点封面
     */
    private String attractionCover;
    /**
     * 景点所在地址
     */
    private String attractionAddress;

}
