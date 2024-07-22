package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysPermission;
import cn.lulucar.springbootshirovue.mapper.SysPermissionMapper;
import cn.lulucar.springbootshirovue.service.ISysPermissionService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台权限表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    private final SysPermissionMapper sysPermissionMapper;

    public SysPermissionServiceImpl(SysPermissionMapper sysPermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
    }

    /**
     * 查询所有权限
     *
     * @return
     */
    @Override
    public JSONObject listAllPermission() {
        List<JSONObject> permissions = sysPermissionMapper.listAllPermission();
        return CommonUtil.successJSON(permissions);
    }
}
