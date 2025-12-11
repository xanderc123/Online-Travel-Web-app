package cn.kmbeast.pojo.dto.query.extend;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点查询Dto类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttractionQueryDto extends QueryDto{
    private String name;
    private Integer supplierId;
    private  Integer categoryId;
    private String saveIds;
    private Boolean status;
}
