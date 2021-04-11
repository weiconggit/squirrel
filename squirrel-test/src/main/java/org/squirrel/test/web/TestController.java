package org.squirrel.test.web;

import javax.annotation.Resource;

import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.squirrel.framework.auth.Auth;
import org.squirrel.framework.auth.AuthCache;
import org.squirrel.framework.auth.AuthUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author weicong
 * @time   2020年9月14日
 * @version 1.0
 */
@Api(tags = "综合测试")
@RequestMapping("test")
@RestController
public class TestController {
	
//	@Autowired
//    private ApplicationContext applicationContext;
	@Resource
	private RedissonClient redissonClient;
	@Resource
	private AuthCache authCache;
	@Resource
	private TestMapper testMapper;
	
	@GetMapping("auth")
	public AuthUser testAuth() {
		return authCache.getAuth("test");
	}
	
	@ApiOperation(value="获取列表信息")
	@GetMapping("alive")
	public String test() {
		redissonClient.getMap("a").put("1", "1");
		return "test api";
	}
	
	@Auth("test2")
	@GetMapping("alive2/{id}")
	public String test2(@PathVariable String id) {
		return "test api2";
	}
	@GetMapping("alive2")
	public String test3(@RequestParam String id) {
		return "test api3";
	}

//	@ApiOperation(value="查询列表信息")
//	@GetMapping(value = "list")
//	@Override
//	public Rp<Collection<SysUser>> list(Map<String, Object> query) {
//		return super.list(query);
//	}
}
