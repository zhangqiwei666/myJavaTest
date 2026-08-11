package com.zqw.crm.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqw.crm.entity.SysUser;
import com.zqw.crm.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    public UserDetailsServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        List<String> permissions = sysUserMapper.selectPermissionsByUserId(sysUser.getId());
        List<String> roles = sysUserMapper.selectRoleKeysByUserId(sysUser.getId());

        roles.forEach(role -> permissions.add(role.startsWith("ROLE_") ? role : "ROLE_" + role));

        return new SecurityUser(sysUser, permissions);
    }
}
