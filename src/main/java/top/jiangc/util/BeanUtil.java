package top.jiangc.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Description:获取spring的bean工具类
 *@author jiangcheng
 *
 *2017年4月5日
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    /**
     * 当前IOC
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置当前上下文环境，此方法由spring自动装配
     * 
     * @param context
     *            上下文对象
     */
    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        applicationContext = context;
    }

    /**
     * 从当前IOC获取bean
     * 
     * @param id
     *            bean的id
     *            
     * @return	bean实例
     */
    public static Object getBean(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }

}