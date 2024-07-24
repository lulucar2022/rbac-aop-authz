package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysUserRole;
import cn.lulucar.springbootshirovue.mapper.SysUserRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
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

    private final SysUserRoleMapper sysUserRoleMapper;

    public SysUserRoleServiceImpl(SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    /**
     * 批量添加用户的角色
     *
     * @param userId 用户Id
     * @param roles  角色Id集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchAddUserRole(Integer userId , Collection<Integer> roles) {
        if (roles.isEmpty())
            return;
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Integer role : roles) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(role);
            userRoleList.add(sysUserRole);
        }
        MybatisBatch<SysUserRole> mybatisBatch = new MybatisBatch<>(getSqlSessionFactory(), userRoleList);
        MybatisBatch.Method<SysUserRole> method = new MybatisBatch.Method<>(SysUserRoleMapper.class);
        mybatisBatch.execute(method.insert());
    }

    /**
     * 批量移除用户的所有角色
     *
     * @param userId 用户Id
     * @param roles  角色Id集合
     */
    @Override
    public void batchRemoveUserRole(Integer userId, Collection<Integer> roles) {
        if (roles.isEmpty())
            return;
        LambdaUpdateWrapper<SysUserRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysUserRole::getUserId, userId)
                .in(SysUserRole::getRoleId, roles);

        sysUserRoleMapper.delete(lambdaUpdateWrapper);
    }

    /**
     * 查询用户的所有角色
     * @param userId 用户id
     * @return 角色列表（roleId）
     */
    @Override
    public List<SysUserRole> getUserAllRoles(Integer userId) {
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserRole::getUserId, userId);
        return sysUserRoleMapper.selectList(lambdaQueryWrapper);
    }
}
