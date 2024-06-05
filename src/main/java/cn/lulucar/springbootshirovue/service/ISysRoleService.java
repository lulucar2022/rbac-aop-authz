package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysRole;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台角色表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysRoleService extends IService<SysRole> {
    // 角色列表
    Page<SysRole> listRole(JSONObject role);
    // 新增角色
    boolean addRole(JSONObject jsonObject);
    // 修改角色 
    boolean updateRole(SysRole role);
    // 删除角色
    boolean deleteRole(SysRole role);
}
