package com.zqw.crm;

import com.zqw.crm.entity.SysUser;
import com.zqw.crm.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CrmDbTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private com.zqw.crm.service.SysUserService sysUserService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Test
    public void testDatabaseConnection() {
        System.out.println("====== START TESTING DATABASE CONNECTION ======");
        try {
            List<SysUser> list = sysUserMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysUser>());
            System.out.println("SUCCESSFULLY QUERIED SYS_USER, COUNT: " + list.size());
            
            System.out.println("====== TESTING LOGIN METHOD ======");
            com.zqw.crm.vo.LoginReq req = new com.zqw.crm.vo.LoginReq();
            req.setUsername("admin");
            req.setPassword("123456");
            com.zqw.crm.vo.LoginResp resp = sysUserService.login(req);
            System.out.println("LOGIN SUCCESS! Token: " + resp.getToken());
        } catch (Throwable e) {
            System.err.println("LOGIN TEST FAILED: " + e.getMessage());
        }
        System.out.println("====== END TESTING DATABASE CONNECTION ======");
    }
}
