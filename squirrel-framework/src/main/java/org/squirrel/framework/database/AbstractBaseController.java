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
import org.squirrel.framework.auth.Auth;
import org.squirrel.framework.auth.AuthMenuLoader;
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
public abstract class AbstractBaseController<T> implements DataOperator<T>, SquirrelInitializer {
	
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
	
	/**
	 * 当前实体对应VO类型
	 */
	private Class<T> classVO;
	/**
	 * oval检验
	 */
	private Validator validator;

	public abstract BaseService<T> getService();

	@SuppressWarnings("unchecked")
	@Override
	public final void init() {
		// 可放入无参构造中先行执行
		Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		this.classVO = (Class<T>) actualTypeArguments[0];
		// 需等待 ApplicationContextHelper创建之后
		this.validator = ApplicationContextHelper.getBean(Validator.class);
	}

	@Deprecated
	@Auth(AuthMenuLoader.ADD)
	@ApiOperation(value = "新增批量")
	@PostMapping(value = "addba")
	@Override
	public Rp<T> add(@RequestBody List<T> t) {
//		List<ConstraintViolation> validate = validator.validate(t);
//		if (!validate.isEmpty()) {
//			Rp.failed(RpEnum.ERROR_VALIDATE, validate.get(0).getMessage());
//		}
//		String afterValidate = afterValidate(t);
//		if (afterValidate != null) {
//			Rp.failed(RpEnum.ERROR_VALIDATE, validate.get(0).getMessage());
//		}
//		beforeSave(t);
		Rp<T> add = getService().add(t);
		if (add.isSuccess()) {
//			afterSave(t);
			return Rp.success();
		}
		return add;
	}

	@Auth(AuthMenuLoader.ADD)
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
		beforeSave(t);
		Rp<T> add = getService().add(t);
		if (add.isSuccess()) {
			afterSave(t);
			return Rp.success();
		}
		return add;
	}

	@Auth(AuthMenuLoader.EDIT)
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
		beforeUpdate(t);
		Rp<T> edit = getService().edit(id, t);
		if (edit.isSuccess()) {
			afterUpdate(t);
			return Rp.success();
		}
		return edit;
	}

	@Auth(AuthMenuLoader.DEL)
	@ApiOperation(value = "删除")
	@DeleteMapping(value = DEL)
	@Override
	public Rp<T> remove(@PathVariable(value = "ids") Set<String> ids) {
		beforeDel(ids);
		Rp<T> remove = getService().remove(ids);
		if (remove.isSuccess()) {
			afterDel(ids);
			return Rp.success();
		}
		return remove;
	}

	@Auth(AuthMenuLoader.GET)
	@ApiOperation(value = "获取列表")
	@GetMapping(value = LIST)
	@Override
	public Rp<List<T>> list(@RequestParam(name = "query", required = false) Map<String, Object> query) {
		if (query == null) {
			query = new HashMap<>();
		}
		query.put(ISDEL, false);
		return getService().list(query);
	}
	
	protected String afterValidate(T data) {
		return null;
	}
	
	protected void beforeSave(T data) {}
	
	protected void afterSave(T data) {}
	
	protected void beforeUpdate(T data) {}
	
	protected void afterUpdate(T data) {}
	
	protected void beforeDel(Set<String> ids) {}
	
	protected void afterDel(Set<String> ids) {}
	
}
