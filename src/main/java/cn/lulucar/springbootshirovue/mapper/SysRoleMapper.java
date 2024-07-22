package cn.lulucar.springbootshirovue.mapper;

import cn.lulucar.springbootshirovue.entity.SysRole;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台角色表 Mapper 接口
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<JSONObject> listRole();
}
