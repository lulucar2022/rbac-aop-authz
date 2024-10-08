package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysPermission;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台权限表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysPermissionService extends IService<SysPermission> {
    // 查询所有权限
    JSONObject listAllPermission();
    
}
