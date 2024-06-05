package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.util.PageFromRequestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台角色表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;

    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    /**
     * 角色列表
     * @param role Map结构
     * @return 
     */
    @Override
    public Page<SysRole> listRole(JSONObject role) {
        
        Page<SysRole> page = new Page<>((Long) role.get("current"), (Long) role.get("size"));
        return sysRoleMapper.selectPage(page,null);
    }

    /**
     * 新增角色
     * 1.新增角色
     * 2.新增角色的权限    
     * @param jsonObject 包含 角色实体和权限列表
     * @return
     */
    @Override
    public boolean addRole(JSONObject jsonObject) {
        
        
        return false;
    }

    /**
     * 修改角色
     * @param role 角色实体
     * @return
     */
    @Override
    public boolean updateRole(SysRole role) {
        return false;
    }

    /**
     * @param role 角色实体
     * @return
     */
    @Override
    public boolean deleteRole(SysRole role) {
        return false;
    }
}
