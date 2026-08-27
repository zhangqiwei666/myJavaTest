package com.zqw.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqw.crm.common.BizException;
import com.zqw.crm.common.JwtUtils;
import com.zqw.crm.common.PageResult;
import com.zqw.crm.entity.SysUser;
import com.zqw.crm.mapper.SysUserMapper;
import com.zqw.crm.service.SysUserService;
import com.zqw.crm.vo.AssignRolesReq;
import com.zqw.crm.vo.LoginReq;
import com.zqw.crm.vo.LoginResp;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public SysUserServiceImpl(SysUserMapper sysUserMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.baseMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResp login(LoginReq loginReq) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginReq.getUsername()));
        if (user == null) {
            throw new BizException("用户不存在");
        }
        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            throw new BizException("密码不正确");
        }
        if (user.getStatus() != 1) {
            throw new BizException("账号已被禁用");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        List<String> roles = baseMapper.selectRoleKeysByUserId(user.getId());
        List<String> permissions = baseMapper.selectPermissionsByUserId(user.getId());

        return new LoginResp(token, user.getUsername(), user.getRealName(), roles, permissions);
    }

    @Override
    public PageResult<SysUser> getUserPage(Integer pageNum, Integer pageSize, String username) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username).or().like(SysUser::getRealName, username);
        }
        Page<SysUser> result = this.page(page, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public void createUser(SysUser sysUser) {
        Long count = this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (count > 0) {
            throw new BizException("用户名已存在");
        }
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        this.save(sysUser);
    }

    @Override
    public void updateUser(SysUser sysUser) {
        if (StringUtils.hasText(sysUser.getPassword())) {
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        } else {
            sysUser.setPassword(null);
        }
        this.updateById(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(AssignRolesReq req) {
        baseMapper.deleteUserRoles(req.getUserId());
        if (req.getRoleIds() != null && !req.getRoleIds().isEmpty()) {
            for (Long roleId : req.getRoleIds()) {
                baseMapper.insertUserRole(req.getUserId(), roleId);
            }
        }
    }
}
