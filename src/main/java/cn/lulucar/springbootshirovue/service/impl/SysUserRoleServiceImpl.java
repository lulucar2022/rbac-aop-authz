package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysUserRole;
import cn.lulucar.springbootshirovue.mapper.SysUserRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void batchAddUserRole(Integer userId , List<Integer> roles) {
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
     * 移除用户的所有角色
     * @param userId 用户Id
     * @return
     */
    @Override
    public boolean removeAllUserRole(Integer userId) {
        return false;
    }
}
