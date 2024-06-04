package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysUserRole;
import cn.lulucar.springbootshirovue.mapper.SysUserRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户-角色关联表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    /**
     * 批量添加用户的角色
     * @param roles 角色集合
     * @return
     */
    @Override
    public boolean batchAddUserRole(List<SysRole> roles) {
        return false;
    }

    /**
     * 移除用户的所有角色
     * @param userId 用户Id
     * @return
     */
    @Override
    public boolean removeAllUserRole(Integer userId) {
        return false;
    }
}
