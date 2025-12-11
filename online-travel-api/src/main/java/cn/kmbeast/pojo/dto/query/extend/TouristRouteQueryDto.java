package cn.kmbeast.pojo.dto.query.extend;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TouristRouteQueryDto extends QueryDto{
    /*
    name of attraction route
     */
    private String name;
    private Integer attractionId;
}
