package cn.wolfcode.config;

import cn.wolfcode.shiro.AjaxFormAuthenticationFilter;
import cn.wolfcode.shiro.CrmRealm;
import cn.wolfcode.shiro.MyFreeMarkerConfig;
import freemarker.template.TemplateException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * FreeMarker提供shiro标签
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        MyFreeMarkerConfig myFreeMarkerConfig = new MyFreeMarkerConfig();
        myFreeMarkerConfig.setTemplateLoaderPath("classpath:templates/");
        return myFreeMarkerConfig;
    }

    /**
     * 安全管理器
     * @param userRealm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(CrmRealm userRealm, CacheManager cacheManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm); //关联自定义Realm
        securityManager.setCacheManager(cacheManager);//缓存管理器
        return securityManager;
    }

    /**
     * shiro过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new LinkedHashMap<>();
        //自定义过滤器
        filters.put("authc", new AjaxFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout.do", "logout");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    /**
     * 密码匹配器
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        return credentialsMatcher;
    }

    /**
     * 缓存管理器
     * @return
     */
    @Bean
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     *  开启Shiro的注解
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

}