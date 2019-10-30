package com.mdkproject.mdkshiro.base.config;

import com.mdkproject.mdkshiro.entity.SysPermission;
import com.mdkproject.mdkshiro.entity.SysRole;
import com.mdkproject.mdkshiro.entity.SysUserRole;
import com.mdkproject.mdkshiro.entity.UserInfo;
import com.mdkproject.mdkshiro.service.SysRoleService;
import com.mdkproject.mdkshiro.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-17 15:10
 * @Description:  这里主要进行用户身份的校验,自定义Realm 处理登录 权限
 * @Version 1.0
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {


    @Resource
    private UserInfoService userInfoService;
    @Autowired
    private SysRoleService sysRoleService;






    /**
     * @auther: lizhen
     * @date: 2019-06-25 13:52
     * 功能描述: 权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();            //获取用户信息
        String account = userInfo.getAccount();
        /**
         * 1.获取当前登录用户
         * 2.获取当前登录用户拥有的角色集合
         * 3.获取当前登录用户角色集合对应的权限集合
         */
        List<SysUserRole> rolelist = sysRoleService.findRoleByUid(userInfo.getUid());   //通过Uid获取角色集合
        System.err.println(rolelist.size());
        for (SysUserRole sr:rolelist){
            System.err.println(account+"用户拥有的角色:"+sr.getRoleId());
        }
        if (rolelist.size()>0){

        }
        for (SysUserRole sr:rolelist){
            Integer roleId = sr.getRoleId();                                //角色ID
            SysRole role = sysRoleService.findByOne(String.valueOf(roleId));//角色对象
            authorizationInfo.addRole(role.getRole());
            List<SysPermission> permissionList = sysRoleService.findPermissionListByRoleId(roleId);//通过roleId获取权限集合
            for (SysPermission sp:permissionList){
                System.err.println(account+"用户拥有的权限:"+sp.getName()+"-----"+sp.getPermission()+"-----"+sp.getUrl());
                authorizationInfo.addStringPermission(sp.getPermission());
            }
        }
        return authorizationInfo;
    }


    /**
     * @auther: lizhen
     * @date: 2019-06-19 10:37
     * 功能描述: 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
 *              登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("ShiroRealm.doGetAuthenticationInfo()");
        //获取用户输入的账号
        String account = (String) token.getPrincipal();
        //通过account从数据库中查找 user对象，如果找到，没找到
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制
        UserInfo userInfo = userInfoService.findByAccount(account);
        if (userInfo==null){
            log.info("用户对象为空");
            return null;
        }

        SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(
                userInfo,
                userInfo.getPassword(),                                 //密码
                ByteSource.Util.bytes(userInfo.getSalt()),              //加密盐
                getName()
        );
        return authorizationInfo;
    }




}
