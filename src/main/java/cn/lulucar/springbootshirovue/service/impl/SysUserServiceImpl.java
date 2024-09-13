package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.dto.RoleDTO;
import cn.lulucar.springbootshirovue.dto.UserAndRoleDTO;
import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.entity.SysUserRole;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.util.RequiredParametersUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final ISysRoleService sysRoleService;
    private final ISysUserRoleService sysUserRoleService;
    private final SysUserMapper sysUserMapper;
    public SysUserServiceImpl(SysUserMapper sysUserMapper, ISysRoleService sysRoleService, ISysUserRoleService sysUserRoleService, ISysUserRoleService iSysUserRoleService) {
        this.sysUserMapper = sysUserMapper;
        this.sysRoleService = sysRoleService;
        this.sysUserRoleService = sysUserRoleService;
    }

    /**
     * 查询用户及其角色
     * @return 用户列表（包含角色信息）
     */
    @Override
    public List<UserAndRoleDTO> listUserAndRole(Long pageNum, Long pageRow) {
        Page<SysUser> sysUserPage =listUser(pageNum,pageRow);
        long total = sysUserPage.getTotal();
        long current = sysUserPage.getCurrent();
        long size = sysUserPage.getSize();
        // 所有用户
        List<SysUser> sysUser = sysUserPage.getRecords();
        // 所有用户及其角色
        List<UserAndRoleDTO> userAndRoleDTOS = new ArrayList<>();
        for (SysUser user : sysUser) {
            UserAndRoleDTO userAndRoleDTO = new UserAndRoleDTO();
            // 先将user对象复制到userAndRoleDTO对象中，完成用户信息的复制（未包含角色）
            BeanUtils.copyProperties(user,userAndRoleDTO);
            // 获取用户的所有角色
            List<SysUserRole> userAllRoles = sysUserRoleService.getUserAllRoles(user.getId());
            List<RoleDTO> roles = new ArrayList<>();
            // 将用户的所有角色的roleId复制到roles中
            for (SysUserRole userAllRole : userAllRoles) {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoleId(userAllRole.getRoleId());
                roles.add(roleDTO);
            }
            // 根据角色roleId查询角色的roleName复制到roles中
            for (RoleDTO role : roles) {
                SysRole sysRole = sysRoleService.getById(role.getRoleId());
                if (sysRole != null)
                    role.setRoleName(sysRole.getRoleName());
            }
            // 将角色信息复制到userAndRoleDTO中
            userAndRoleDTO.setRoles(roles);
            userAndRoleDTO.setCurrent(current);
            userAndRoleDTO.setSize(size);
            userAndRoleDTO.setTotal(total);
            userAndRoleDTOS.add(userAndRoleDTO);
        }
        return userAndRoleDTOS;
    }

    /**
     * 查询用户列表
     * @param pageNum 当前页
     * @param pageRow 每页大小
     * @return
     */
    @Override
    public Page<SysUser> listUser(Long pageNum, Long pageRow) {
        Page<SysUser> page = new Page<>(pageNum,pageRow);
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
    public void addUser(SysUser user, List<Integer> roleIds) {
        // 判断用户是否存在
        SysUser checked = checkUser(user.getUsername());
        if (checked != null) {
            throw new CommonJsonException(ErrorEnum.E_10009);
        }
        // 插入用户
        sysUserMapper.insert(user);
        // 批量给用户添加角色（用户关联角色）
        sysUserRoleService.batchAddUserRole(user.getId(), roleIds);
    }

    // 修改用户
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(SysUser user, Set<Integer> roleIds) {
        // 参数校验
        String[] requiredColumns = {"user", "roleIds", "userId"};
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", user);
        jsonObject.put("roleIds", roleIds);
        jsonObject.put("userId", user.getId());
        // 校验方法
        RequiredParametersUtil.hasAllRequired(jsonObject,requiredColumns);
        // 不允许修改管理员
        if (user.getId() == 10001)
            return;
        Integer userId = user.getId();
        SysUser userTemp = new SysUser();
        userTemp.setId(user.getId());
        userTemp.setNickname(user.getNickname());
        userTemp.setDeleteStatus(user.getDeleteStatus());
        log.info("deleteStatus of user in here:{}", user.getDeleteStatus());
        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysUser::getId,userId);
        lambdaUpdateWrapper.ne(SysUser::getId,10001);
        // 1.更新user
        sysUserMapper.update(userTemp,lambdaUpdateWrapper);
        // 2.更新user的role（user_role表）
        // 新角色
        Set<Integer> newRoleIds = roleIds;
        log.info("新角色集合：{}",newRoleIds);
        Set<SysUserRole> userAllRoles = new HashSet<>(sysUserRoleService.getUserAllRoles(userId));
        // 旧角色
        Set<Integer> oldRoleIds = new HashSet<>();
        for (SysUserRole userAllRole : userAllRoles) {
            oldRoleIds.add(userAllRole.getRoleId());
        }
        log.info("旧角色集合：{}",oldRoleIds);
        // 添加角色集合
        Set<Integer> addRoleIds = newRoleIds.stream().filter(e -> !oldRoleIds.contains(e)).collect(Collectors.toSet());
        log.info("添加角色集合：{}",addRoleIds);
        // 删除角色集合
        Set<Integer> removeRoleIds = oldRoleIds.stream().filter(e -> !newRoleIds.contains(e)).collect(Collectors.toSet());
        log.info("删除角色集合：{}",removeRoleIds);
        sysUserRoleService.batchAddUserRole(userId,addRoleIds);
        sysUserRoleService.batchRemoveUserRole(userId,removeRoleIds);
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

    /**
     * 删除用户，根据 userId
     *
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(Integer userId, Set<Integer> roleIds) {
        // 超级管理员不能删除
        if (userId == 10001) {
            return ;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",userId);
        jsonObject.put("roleIds",roleIds);
        RequiredParametersUtil.hasAllRequired(jsonObject, new String[]{"userId","roleIds"});
        sysUserMapper.deleteById(userId);
        // 还要删除用户的角色
        sysUserRoleService.batchRemoveUserRole(userId,roleIds);
    }


}
