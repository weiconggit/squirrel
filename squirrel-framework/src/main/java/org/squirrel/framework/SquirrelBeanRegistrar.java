package org.squirrel.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import net.sf.oval.Validator;
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
import org.squirrel.framework.cache.BaseCache;
import org.squirrel.framework.cache.LocalBaseCache;
import org.squirrel.framework.cache.RedissonBaseCache;
import org.squirrel.framework.database.SquirrelMybatisInterceptor;
import org.squirrel.framework.util.ClassUtil;
import org.squirrel.framework.util.StrUtil;
import org.squirrel.framework.validate.ValidatorHelper;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @description  bean注册
 * @author weicong
 * @time   2021年2月17日 下午6:16:34
 * @version 1.0
 */
public class SquirrelBeanRegistrar implements ImportBeanDefinitionRegistrar {

	private static final Logger log = LoggerFactory.getLogger(SquirrelBeanRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		String packageName = SquirrelBeanRegistrar.class.getPackage().getName();
		
		if (log.isDebugEnabled()) {
			log.debug("{} Framework scan package is: {}", SquirrelProperties.LOG_SIGN, packageName);
		}

		// 扫描所有待注册的 spring bean
		Set<Class<?>> classes = ClassUtil.getClasses(packageName);
		Iterator<Class<?>> iterator = classes.iterator();
		while (iterator.hasNext()) {
			Class<?> clazz = iterator.next();
			Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
			for (Annotation annotation : declaredAnnotations) {
				if (annotation instanceof SquirrelComponent) {
					registry.registerBeanDefinition(StrUtil.lowerFirstLetter(clazz.getSimpleName()), new RootBeanDefinition(clazz));
					
					if (log.isDebugEnabled()) {
						log.debug("{} Framework registry bean: {}", SquirrelProperties.LOG_SIGN, clazz.getSimpleName());
					}
					
					break;
				}
			}
		}
		// objectMapper
		String objectMapper = StrUtil.lowerFirstLetter(ObjectMapper.class.getSimpleName());
		if (!registry.containsBeanDefinition(objectMapper)) {
			registry.registerBeanDefinition(objectMapper, new RootBeanDefinition(ObjectMapper.class));
		}
		// validator
		Validator validator = new ValidatorHelper();
		registry.registerBeanDefinition(StrUtil.lowerFirstLetter(Validator.class.getSimpleName()), new RootBeanDefinition(Validator.class, () -> validator));
		// 缓存
		registryBaseCacheBean(registry);
	}

	/**
	 * 权限缓存bean注册
	 * @param registry
	 */
	private void registryBaseCacheBean(BeanDefinitionRegistry registry) {
		// 2021年5月10日 增加 redisson 配置文件名定义
		String redissonFileName = SquirrelProperties.REDISSON_FILE_NAME;
		String baseCache = StrUtil.lowerFirstLetter(BaseCache.class.getSimpleName());
		// 无配置文件时，启用 本地缓存 否则启用 redisson
		if (StrUtil.isEmpty(redissonFileName)) {
			log.info("{} Enable local cache", SquirrelProperties.LOG_SIGN);
			registry.registerBeanDefinition(baseCache, new RootBeanDefinition(LocalBaseCache.class));
		} else {
			log.info("{} Enable redisson cahce", SquirrelProperties.LOG_SIGN);
			String path = System.getProperty("user.dir");
			path = new StringBuilder(File.separator).append(path).append(File.separator)
					.append("src").append(File.separator)
					.append("main").append(File.separator)
					.append("resources").append(File.separator)
					.append(redissonFileName).toString();
			File file = new File(path);
			if (file.exists()) {
				try {
					// redissonClient 自定义配置文件官方使用步骤
					Config config = Config.fromYAML(file);
					RedissonClient redissonClient = Redisson.create(config);
					registry.registerBeanDefinition("redissonClient", new RootBeanDefinition(RedissonClient.class, () -> redissonClient));
					// redisson缓存
					registry.registerBeanDefinition(baseCache, new RootBeanDefinition(RedissonBaseCache.class, () -> new RedissonBaseCache(redissonClient)));
				} catch (IOException e) {
					log.error("{} Redisson config from yaml error: {}", SquirrelProperties.LOG_SIGN, e);
				}
			} else {
				log.error("{} Redisson file not exists: {}", SquirrelProperties.LOG_SIGN, path);
			}
		}
	}
	
	
	
	// ~ Class Config
	// =============================================================================
	
	@SquirrelComponent
	@EnableTransactionManagement(proxyTargetClass = true) // 不采用service - serviceImpl 形式时，需要该注解
	@MapperScan(value = {"org.squirrel.*.*"})
	@Configuration
	protected static class MybatisConfig {
		@Bean
		public SquirrelMybatisInterceptor mybatisPlusInterceptor() {
			return new SquirrelMybatisInterceptor();
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
