package cn.lulucar.springbootshirovue.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 后台权限表
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自定id,主要供前端展示权限列表分类排序使用.
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 归属菜单,前端判断并展示菜单使用,
     */
    @TableField("menu_code")
    private String menuCode;

    /**
     * 菜单的中文释义
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 权限的代码/通配符,对应代码中@RequiresPermissions 的value
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 本权限的中文释义
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 是否本菜单必选权限, 1.必选 2非必选 通常是"列表"权限是必选
     */
    @TableField("required_permission")
    private Boolean requiredPermission;


}
