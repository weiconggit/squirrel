package org.squirrel.framework;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.squirrel.framework.auth.AuthCache;
import org.squirrel.framework.cache.CaffeineSquirrelCache;
import org.squirrel.framework.cache.RedissonSquirrelCache;
import org.squirrel.framework.mybatis.SquirrelMapperFactoryBean;
import org.squirrel.framework.util.ClassUtil;
import org.squirrel.framework.util.StrUtil;
import org.squirrel.framework.validate.ValidatorHelper;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import net.sf.oval.Validator;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @description  bean注册
 * @author weicong
 * @time   2021年2月17日 下午6:16:34
 * @version 1.0
 */
public class BeanRegistrar implements ImportBeanDefinitionRegistrar {

	private static final Logger log = LoggerFactory.getLogger(BeanRegistrar.class);
	private static final String REDISSON_CONFIG_FILE_NAME = "redisson-squ.yml";
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		log.info("{} Framework registry beans", Language.LOG_SIGN);
		
		String packageName = BeanRegistrar.class.getPackage().getName();
		
		if (log.isDebugEnabled()) {
			log.debug("{} Framework scan package is: {}", Language.LOG_SIGN, packageName);
		}
		
		Set<Class<?>> classes = ClassUtil.getClasses(packageName);
		Iterator<Class<?>> iterator = classes.iterator();
		RootBeanDefinition beanDefinition = null;
		while (iterator.hasNext()) {
			Class<?> clazz = iterator.next();
			Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
			for (Annotation annotation : declaredAnnotations) {
				if (annotation instanceof SquirrelComponent) {
					beanDefinition = new RootBeanDefinition(clazz);
					registry.registerBeanDefinition(StrUtil.lowerFirstLetter(clazz.getSimpleName()), beanDefinition);
					
					if (log.isDebugEnabled()) {
						log.debug("{} Framework registry bean: {}", Language.LOG_SIGN, StrUtil.lowerFirstLetter(clazz.getSimpleName()));
					}
					
					break;
				}
			}
		}
		
		registrySpecialBean(registry);
		registryAuthCacheBean(registry);
	}
	
	
	
	// ~ Special Registry
	// =============================================================================

	/**
	 * 特殊bean注册
	 * @param registry
	 */
	private void registrySpecialBean(BeanDefinitionRegistry registry) {
		RootBeanDefinition beanDefinition = null; 
		// objectMapper
		if (!registry.containsBeanDefinition(StrUtil.lowerFirstLetter(ObjectMapper.class.getSimpleName()))) {
			beanDefinition = new RootBeanDefinition(ObjectMapper.class);
			registry.registerBeanDefinition(StrUtil.lowerFirstLetter(ObjectMapper.class.getSimpleName()), beanDefinition);
		}
		// validator
		Validator validator = new ValidatorHelper();
		beanDefinition = new RootBeanDefinition(Validator.class, () -> validator);
		registry.registerBeanDefinition(StrUtil.lowerFirstLetter(Validator.class.getSimpleName()), beanDefinition);
	}

	/**
	 * 权限缓存bean注册，如果有 redisson 会同时注册 redisson
	 * @param registry
	 */
	private void registryAuthCacheBean(BeanDefinitionRegistry registry) {
		String path = System.getProperty("user.dir");
		path = new StringBuilder(File.separator).append(path).append(File.separator)
				.append("src").append(File.separator)
				.append("main").append(File.separator)
				.append("resources").append(File.separator)
				.append(REDISSON_CONFIG_FILE_NAME).toString();
		
		String beanName = StrUtil.lowerFirstLetter(AuthCache.class.getSimpleName());
		File file = new File(path);
		// redisson 配置文件是否存在，不存在使用 caffeine cache
		if (!file.exists()) {
			log.info("{} Enable caffeine cache", Language.LOG_SIGN);
			RootBeanDefinition beanDefinition = new RootBeanDefinition(CaffeineSquirrelCache.class);
			registry.registerBeanDefinition(beanName, beanDefinition);
			return;
		}
		try {
			Config config = Config.fromYAML(new File(path));
			if (config == null) {
				log.error("{} Redisson config from yaml is null", Language.LOG_SIGN);
				return;
			}
			RedissonClient redissonClient = Redisson.create(config);
			RedissonSquirrelCache.setRedissonCache(redissonClient);
			log.info("{} Enable redis cahce", Language.LOG_SIGN);
			
			RootBeanDefinition beanDefinition = new RootBeanDefinition(RedissonSquirrelCache.class);
			registry.registerBeanDefinition(beanName, beanDefinition);
			
			// 同时注册 redissonClient
			beanDefinition = new RootBeanDefinition(RedissonClient.class, () -> redissonClient);
			registry.registerBeanDefinition(StrUtil.lowerFirstLetter(RedissonClient.class.getSimpleName()), beanDefinition);
		} catch (IOException e) {
			log.error("{} Redisson config from yaml error: {}", Language.LOG_SIGN, e);
		}
	}
	
	
	
	// ~ Class Config
	// =============================================================================
	
	@SquirrelComponent
	@EnableTransactionManagement(proxyTargetClass = true) // 不采用service - serviceImpl 形式时，需要该注解
	@MapperScan(value = {"org.squirrel.*.*"}, factoryBean = SquirrelMapperFactoryBean.class)
	@Configuration
	protected static class MybatisConfig {
		/**
		 * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
		 */
		@Bean
		public MybatisPlusInterceptor mybatisPlusInterceptor() {
			MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
			interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
			return interceptor;
		}
		@SuppressWarnings("deprecation")
		@Bean
		public ConfigurationCustomizer configurationCustomizer() {
			return configuration -> configuration.setUseDeprecatedExecutor(false);
		}
	}
	
	@SquirrelComponent
	@EnableSwagger2WebMvc
	@Configuration
	protected static class Swagger2Config {
		@Bean
		public Docket api() {
//			ParameterBuilder param = new ParameterBuilder();
			List<Parameter> list = new ArrayList<Parameter>();
//			param.name("permit").description("用户访问凭证").modelRef(new ModelRef("string")).parameterType("header")
//					.required(false).build();
//			list.add(param.build());

			return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
					.apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).paths(PathSelectors.any()).build()
					.globalOperationParameters(list);
		}

		private ApiInfo apiInfo() {
			return new ApiInfoBuilder().title("corn restful api").description("corn 在线接口文档")
					// 服务条款网址
					.termsOfServiceUrl("http://localhost/").version("1.0.0")
					.contact(new Contact(" 魏 聪 ", "", "weicongmail@foxmail.com")).build();
		}
	}
}
