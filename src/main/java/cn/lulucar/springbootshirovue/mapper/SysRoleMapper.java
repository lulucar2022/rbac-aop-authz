package cn.lulucar.springbootshirovue.mapper;

import cn.lulucar.springbootshirovue.dto.RoleDTO;
import cn.lulucar.springbootshirovue.entity.SysRole;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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
    // List<JSONObject> listRole();
    
    // 查询所有角色（id，roleName）
    @Results(
            value = { @Result(column = "id", property = "roleId"), 
                    @Result(column = "role_name", property = "roleName")
    })
    @Select("select id,role_name from sys_role where delete_status = '1'")
    List<RoleDTO> getAllRoles();
}
