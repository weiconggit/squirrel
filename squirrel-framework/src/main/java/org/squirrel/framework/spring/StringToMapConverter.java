package org.squirrel.framework.spring;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description 
 * @author weicong
 * @time   2020年12月19日 下午9:05:47
 * @version 1.0
 */
public class StringToMapConverter implements Converter<String, Map<String, Object>>{

	private ObjectMapper objectMapper;
	
	public StringToMapConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Map<String, Object> convert(String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
	}

}
