package org.squirrel.framework.spring;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.squirrel.framework.Language;
import org.squirrel.framework.SquirrelComponent;
import org.squirrel.framework.auth.AuthInterceptor;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author weicong
 * @time   2021年2月2日
 * @version 1.0
 */
@SquirrelComponent
public class SquirrelWebMvcConfigurer implements WebMvcConfigurer {
	
	private static final Logger log = LoggerFactory.getLogger(SquirrelWebMvcConfigurer.class);
	
	@Resource
	private AuthInterceptor authInterceptor;
	@Resource
	private ObjectMapper objectMapper;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		log.info("{} Framework CorsMapping add", Language.LOG_SIGN);
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
				.allowedHeaders("*").maxAge(3600).allowCredentials(true);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("{} Framework AuthInterceptor add", Language.LOG_SIGN);
		registry.addInterceptor(authInterceptor);
	}
	
	/**
	 * 自定义转换Map参数解析 @RequestParam(value = "query", required = false) Map<String, Object> query
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		log.info("{} Framework StringToMapConverter add", Language.LOG_SIGN);
		registry.addConverter(new StringToMapConverter(objectMapper));
	}

	/**
	 * 将返回结果中的long转换为string，避免js转long丢失精度，用于id
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper msgObjectMapper = jackson2HttpMessageConverter.getObjectMapper();
		//不显示为null的字段
		msgObjectMapper.setSerializationInclusion(Include.NON_NULL);
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		msgObjectMapper.registerModule(simpleModule);
		
		jackson2HttpMessageConverter.setObjectMapper(msgObjectMapper);
		//放到第一个
		converters.add(0, jackson2HttpMessageConverter);
	}
	
}
