package org.squirrel.framework.database;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.squirrel.framework.SquirrelInitializer;
import org.squirrel.framework.auth.annotation.Authority;
import org.squirrel.framework.auth.AuthorityMenuLoader;
import org.squirrel.framework.database.page.BasePage;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

import io.swagger.annotations.ApiOperation;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

/**
 * controller接口通用实现
 * @author weicong
 * @time 2020年11月25日
 * @version 1.0
 * @param <T>
 * @param <T>
 */
public abstract class AbstractBaseController<T> implements BaseController<T>, SquirrelInitializer {
	
	private final Logger log = LoggerFactory.getLogger(AbstractBaseController.class);
	
	// 通用接口地址
	protected static final String LIST = "list";
	protected static final String PAGE = "page";
	protected static final String DETAIL = "detail/{id}";
	protected static final String ADD = "add";
	protected static final String EDIT = "edit/{id}";
	protected static final String DEL = "del/{ids}";
	// 通用逻辑删除标识
	protected static final String ISDEL = "isdel";
	// 排序规则关键字
	private static final String ASC = "asc";
	private static final String DESC = "desc";
	
	/** oval检验 */
	private Validator validator;
	/** service实现 */
	private BaseService<T> baseService;
	
	@Override
	public BaseService<T> getBaseService(){
		return baseService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void init() {
		// 可放入无参构造中先行执行
		Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		Class<T> classVO = (Class<T>) actualTypeArguments[0];
		// 需等待 ApplicationContextHelper 创建之后
		String simpleName = classVO.getSimpleName();
		String serviceName = simpleName.substring(0, simpleName.length() - 2) + "Service";
		serviceName = StrUtil.lowerFirstLetter(serviceName);
		this.baseService = (BaseService<T>)ApplicationContextHelper.getBean(serviceName);
		this.validator = ApplicationContextHelper.getBean(Validator.class);
	}

	@Deprecated // 仅测试使用
	@Authority(AuthorityMenuLoader.ADD)
	@ApiOperation(value = "新增批量")
	@PostMapping(value = "addba")
	@Override
	public Rp<T> add(@RequestBody List<T> t) {
		Rp<T> add = getBaseService().insert(t);
		return add;
	}

	@Authority(AuthorityMenuLoader.ADD)
	@ApiOperation(value = "新增")
	@PostMapping(value = ADD)
	@Override
	public Rp<T> add(@RequestBody T t) {
		List<ConstraintViolation> validate = validator.validate(t);
		if (!validate.isEmpty()) {
			Rp.failed(RpEnum.ERROR_VALIDATE, validate.get(0).getMessage());
		}
		String afterValidate = afterValidate(t);
		if (afterValidate != null) {
			Rp.failed(RpEnum.ERROR_VALIDATE, validate.get(0).getMessage());
		}
		return getBaseService().insert(t);
	}

	@Authority(AuthorityMenuLoader.EDIT)
	@ApiOperation(value = "修改")
	@PutMapping(value = EDIT)
	@Override
	public Rp<T> edit(@PathVariable(value = "id") String id, @RequestBody T t) {
		List<ConstraintViolation> validate = validator.validate(t);
		if (!validate.isEmpty()) {
			return Rp.failed(RpEnum.ERROR_VALIDATE, validate.get(0).getMessage());
		}
		String afterValidate = afterValidate(t);
		if (afterValidate != null) {
			return Rp.failed(RpEnum.ERROR_VALIDATE, afterValidate);
		}
		return getBaseService().update(t);
	}

	@Authority(AuthorityMenuLoader.DEL)
	@ApiOperation(value = "删除")
	@DeleteMapping(value = DEL)
	@Override
	public Rp<T> remove(@PathVariable(value = "ids") Set<String> ids) {
		return getBaseService().delete(ids);
	}

	@Authority(AuthorityMenuLoader.GET)
	@ApiOperation(value = "获取列表")
	@GetMapping(value = LIST)
	@Override
	public Rp<List<T>> list(@RequestParam(name = "query", required = false) Map<String, Object> query) {
		if (query == null) {
			query = new HashMap<>();
		}
		query.put(ISDEL, false);
		// TODO sort
		return getBaseService().select(query, "");
	}
	
	@Authority(AuthorityMenuLoader.GET)
	@ApiOperation(value = "获取分页")
	@GetMapping(value = PAGE)
//	@Override
	public Rp<BasePage<T>> page(@RequestParam(name = "query", required = false) Map<String, Object> query, 
			@RequestParam(name = "current") Integer current,@RequestParam(name = "limit") Integer limit) {
		if (query == null) {
			query = new HashMap<>();
		}
//		query.put(ISDEL, false);
		// TODO sort
		return getBaseService().page(query, current, limit, "");
	}
	
	protected String afterValidate(T data) {
		return null;
	}
	
}
