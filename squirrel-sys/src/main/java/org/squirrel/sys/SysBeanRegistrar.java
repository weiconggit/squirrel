package org.squirrel.sys;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.SquirrelProperties;
import org.squirrel.framework.util.ClassUtil;
import org.squirrel.framework.util.StrUtil;

/**
 * @author weicong
 * @time   2021年2月6日
 * @version 1.0
 */
public class SysBeanRegistrar implements ImportBeanDefinitionRegistrar  {

	private static final Logger log = LoggerFactory.getLogger(SysBeanRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Set<Class<?>> classes = ClassUtil.getClasses("org.squirrel.sys");
		Iterator<Class<?>> iterator = classes.iterator();
		while (iterator.hasNext()) {
			Class<?> clazz = iterator.next();
			Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
			for (Annotation annotation : declaredAnnotations) {
				if (annotation instanceof RestController
						|| annotation instanceof Service
						|| annotation instanceof Controller
						|| annotation instanceof Component) {
					RootBeanDefinition beanDefinition = new RootBeanDefinition(clazz);
					registry.registerBeanDefinition(StrUtil.lowerFirstLetter(clazz.getSimpleName()), beanDefinition);
					
					if (log.isDebugEnabled()) {
						log.debug("{} Sys registry bean: {}", SquirrelProperties.LOG_SIGN, StrUtil.lowerFirstLetter(clazz.getSimpleName()));
					}
					
					break;
				}
			}
		}
	}
}
