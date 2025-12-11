package cn.kmbeast.pojo.vo;

import lombok.Data;

/**
 * 选择器依赖数据VO
 */
@Data
public class SelectedVO {
    /**
     * ID
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;

    public SelectedVO() {
    }

    public SelectedVO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
