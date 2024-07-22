package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper sysUserMapper;
    private final ISysUserRoleService iSysUserRoleService;
    public SysUserServiceImpl(SysUserMapper sysUserMapper, ISysRoleService iSysRoleService, ISysUserRoleService iSysUserRoleService) {
        this.sysUserMapper = sysUserMapper;
        this.iSysUserRoleService = iSysUserRoleService;
    }

    /**
     * 查询用户列表
     * @param current 当前页
     * @param size 每页大小
     * @return
     */
    @Override
    public Page<SysUser> listUser(Long current, Long size) {
        Page<SysUser> page = new Page<>(current,size);
        return sysUserMapper.selectPage(page, null);   
    }

    // 查询用户总数
    @Override
    public int countUser() {
        return Math.toIntExact(sysUserMapper.selectCount(null));
    }

    // 新增用户
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(SysUser user, List<Integer> roles) {
        // 判断用户是否存在
        // QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        // queryWrapper.eq("username",user.getUsername());
        // int exist = Math.toIntExact(sysUserMapper.selectCount(queryWrapper));
        SysUser checked = checkUser(user.getUsername());
        if (checked != null) {
            throw new CommonJsonException(ErrorEnum.E_10009);
        }
        // 插入用户
        sysUserMapper.insert(user);
        // 批量给用户添加角色（用户关联角色）
        iSysUserRoleService.batchAddUserRole(user.getId(), roles);
    }

    // 修改用户
    @Override
    public boolean updateUser(SysUser user) {
        return false;
    }

    /**
     * 验证用户是否存在
     * @param username 用户名
     * @return 用户对象（为空说明不存在）
     */
    @Override
    public SysUser checkUser(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return sysUserMapper.selectOne(queryWrapper);
    }
    
    
}
