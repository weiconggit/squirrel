package org.squirrel.framework.spring;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.SquirrelComponent;
import org.squirrel.framework.SquirrelInitializer;

/**
 * <p> @ApplicationContextHelper 对象创建后于 @AbstractBaseController
 * <p> 需要由spring初始化
 * @author weicong
 * @time   2020年11月30日
 * @version 1.0
 */
@SquirrelComponent
public final class ApplicationContextHelper implements ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(ApplicationContextHelper.class);
	
	private static ApplicationContext context; 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		log.info("### Framework ApplicationContextHelper init");
		
		if (context == null) {
			setStaticApplicationContext(applicationContext);
		}
		initSpringInitializer();
	}

	private static void setStaticApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	private void initSpringInitializer() {
		Map<String, SquirrelInitializer> map = context.getBeansOfType(SquirrelInitializer.class);
		
    	if (!map.isEmpty()) {
    		
    		if (log.isDebugEnabled()) {
    			map.values().forEach( v-> log.debug("{} SpringInitializer init: {}", SquirrelProperties.LOG_SIGN, v.getClass().getName()));
    		}
    		
    		map.values().forEach(SquirrelInitializer::init);
		}
    }

    /**
     * 获取 ApplicationContext
     * @return ApplicationContext。class
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取spring bean对象
     * 
     * @param name bean的name
     * @return Spring bean
     * @throws BeansException
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }
    
    public static <T> T getBean(Class<T> clazz) {
    	return context.getBean(clazz);
    }
}
