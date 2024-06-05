package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    // 用户列表
    @Override
    public Page<SysUser> listUser(JSONObject user) {
        Page<SysUser> page = new Page<>((Long) user.get("current"),(Long) user.get("size"));
        return sysUserMapper.selectPage(page, null);   
    }

    // 查询用户总数
    @Override
    public int countUser() {
        return Math.toIntExact(sysUserMapper.selectCount(null));
    }

    // 新增用户
    @Override
    public boolean addUser(SysUser user) {
        // 判断用户名是否存在
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        int exist = Math.toIntExact(sysUserMapper.selectCount(queryWrapper));
        log.info("exist = {}",exist);
        if (exist == 0) {
          sysUserMapper.insert(user);  
        } else {
            throw new CommonJsonException(ErrorEnum.E_10009);
        }
        return true;
    }

    // 修改用户
    @Override
    public boolean updateUser(SysUser user) {
        return false;
    }
}
