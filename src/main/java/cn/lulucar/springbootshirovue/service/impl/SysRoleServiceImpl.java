package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    /**
     * 角色列表
     * @param role Map结构
     * @return
     */
    @Override
    public Page<SysRole> listRole(JSONObject role) {
        return null;
    }

    /**
     * 新增角色
     * @param role 角色实体
     * @return
     */
    @Override
    public boolean addRole(SysRole role) {
        return false;
    }

    /**
     * 修改角色
     * @param role 
     * @return
     */
    @Override
    public boolean updateRole(SysRole role) {
        return false;
    }

    /**
     * @param role 
     * @return
     */
    @Override
    public boolean deleteRole(SysRole role) {
        return false;
    }
}
