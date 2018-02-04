package top.jiangc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidConfiguration {

	 @Bean 
    public ServletRegistrationBean druidServlet() {
		 ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		 
		 servletRegistrationBean.setServlet(new StatViewServlet());
		 servletRegistrationBean.addUrlMappings("/druid/*");
//		 servletRegistrationBean.addInitParameter("allow", "127.0.0.1");// IP白名单 (没有配置或者为空，则允许所有访问)
//		 servletRegistrationBean.addInitParameter("deny", "");// IP黑名单 (存在共同时，deny优先于allow)
		 servletRegistrationBean.addInitParameter("loginUsername", "admin");// 用户名
		 servletRegistrationBean.addInitParameter("loginPassword", "123456");// 密码
		 servletRegistrationBean.addInitParameter("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
		 return servletRegistrationBean;
    }

    @Bean 
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
