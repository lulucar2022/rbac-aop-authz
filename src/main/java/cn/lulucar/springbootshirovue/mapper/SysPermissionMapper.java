package cn.lulucar.springbootshirovue.mapper;

import cn.lulucar.springbootshirovue.entity.SysPermission;
import cn.lulucar.springbootshirovue.entity.SysRolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台权限表 Mapper 接口
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectList(LambdaQueryWrapper<SysRolePermission> lambdaQueryWrapper);
}
