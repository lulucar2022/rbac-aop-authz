package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysPermission;
import cn.lulucar.springbootshirovue.entity.SysRolePermission;
import cn.lulucar.springbootshirovue.mapper.SysPermissionMapper;
import cn.lulucar.springbootshirovue.mapper.SysRolePermissionMapper;
import cn.lulucar.springbootshirovue.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色-权限关联表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    private final SysPermissionMapper sysPermissionMapper;

    public SysRolePermissionServiceImpl(SysPermissionMapper sysPermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
    }

    /**
     * @param roleId 角色Id
     * @param permissions 新增的权限列表
     * @return
     */
    @Transactional(rollbackFor = PersistenceException.class)
    @Override
    public boolean insertRolePermission(int roleId, Collection<Integer> permissions) {
        List<SysRolePermission> rolePermissionList = new ArrayList<>();
        for (Integer permission : permissions) {
            SysRolePermission temp = new SysRolePermission();
            temp.setRoleId(roleId);
            temp.setPermissionId(permission);
            rolePermissionList.add(temp);
        }
        
        MybatisBatch<SysRolePermission> mybatisBatch = new MybatisBatch<>(getSqlSessionFactory(), rolePermissionList);
        MybatisBatch.Method<SysRolePermission> method = new MybatisBatch.Method<>(SysRolePermissionMapper.class);
        mybatisBatch.execute(method.insert());
        return true;
    }
}
