package org.squirrel.framework.data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;

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
import org.squirrel.framework.response.BizException;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.DateUtil;
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

	// 通用接口地址
	protected static final String LIST = "list";
	protected static final String PAGE = "page";
	protected static final String DETAIL = "detail/{id}";
	protected static final String ADD = "add";
	protected static final String EDIT = "edit/{id}";
	protected static final String DEL = "del/{ids}";

	// 通用排序标识
	protected static final String SORT_KEY = "sortKey";
	protected static final String SORT_TYPE = "sortType";
	protected static final String ASC = "ASC";
	protected static final String DESC = "DESC";
	
	/** oval检验 */
	private Validator validator;
	/** service实现 */
	private BaseService<T> baseService;
	/** entity类 */
	private Class<? super T> entityClass;
	
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
		this.entityClass = classVO.getSuperclass();
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
	public Rp<List<T>> add(@RequestBody List<T> t) {
		Rp<List<T>> add = getBaseService().insertBatch(t);
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
			Rp.failed(RpEnum.ERROR_VALIDATE, afterValidate);
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
		return getBaseService().deleteByIds(ids);
	}

	@Authority(AuthorityMenuLoader.GET)
	@ApiOperation(value = "明细")
	@GetMapping(value = DETAIL)
	@Override
	public Rp<T> detail(@PathVariable(value = "id") String id) {
		return getBaseService().selectById(id);
	}

	@Authority(AuthorityMenuLoader.GET)
	@ApiOperation(value = "获取列表")
	@GetMapping(value = LIST)
	@Override
	public Rp<List<T>> list(@RequestParam(name = "query", required = false) Map<String, Object> query) {
		List<OrderBean> orderBeans = getOrderBeans(query);
		List<WhereBean> whereBeans = getWhereBeans(query);
		return getBaseService().select(whereBeans, orderBeans);
	}
	
	@Authority(AuthorityMenuLoader.GET)
	@ApiOperation(value = "获取分页")
	@GetMapping(value = PAGE)
	@Override
	public Rp<BasePage<T>> page(@RequestParam(name = "query", required = false) Map<String, Object> query, 
								@RequestParam(name = "current") Integer current,
								@RequestParam(name = "limit") Integer limit) {
		List<OrderBean> orderBeans = getOrderBeans(query);
		List<WhereBean> whereBeans = getWhereBeans(query);
		return getBaseService().page(whereBeans, current, limit, orderBeans);
	}

	/**
	 * 获取排序参数
	 * @param query
	 * @return
	 */
	public List<OrderBean> getOrderBeans(Map<String, Object> query){
		// 检查下是否含有排序字段，用于通用接口的参数
		List<OrderBean> list = null;
		if (query != null) {
			Object sortKeyObj = query.remove(SORT_KEY);
			if (sortKeyObj != null) {
				list = new ArrayList<>();
				Object sortTypeObj = query.remove(SORT_TYPE);
				String key = String.valueOf(sortKeyObj);
				if (sortTypeObj == null) {
					// 默认为升序
					list.add(new OrderBean(key, ASC));
				} else {
					String sortType = String.valueOf(sortTypeObj);
					if (ASC.equals(sortType) || DESC.equals(sortType)) {
						list.add(new OrderBean(key, sortType));
					}
				}
			}
		}
		return list;
	}

	public List<WhereBean> getWhereBeans(Map<String, Object> query){
		List<WhereBean> list = null;
		if (query != null) {
			list = new ArrayList<>(query.size());
			TableBean tableBean = BaseDao.getTableBean(entityClass);
			Map<String, String> fieldTypeMap = tableBean.getFieldTypeMap();
			Iterator<Map.Entry<String, Object>> iterator = query.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> next = iterator.next();
				String key = next.getKey();
				Object value = next.getValue();
				String fieldType = fieldTypeMap.get(key);
				if (fieldType != null) {
					switch (fieldType) {
						case "java.util.List":
							list.add(new WhereBean(key, value, WhereBean.IN));
							break;
						case "java.lang.String":
							// 区分是日期
							String string = String.valueOf(value);
							if (string.contains(" - ")) {
								try {
									String[] split = string.split("-");
									Date start = DateUtil.convertStringToDate(split[0].trim());
									Date end = DateUtil.convertStringToDate(split[1].trim());
									list.add(new WhereBean(key, start, WhereBean.GTE));
									list.add(new WhereBean(key, end, WhereBean.LTE));
								} catch (ParseException e) {
									throw new BizException(e.getMessage());
								}
							} else {
								list.add(new WhereBean(key, value, WhereBean.LIKE));
							}
							break;
						case "java.util.Date":
							list.add(new WhereBean(key, value, WhereBean.GT));
							list.add(new WhereBean(key, value, WhereBean.LT));
							break;
						case "java.lang.Boolean":
						case "java.lang.Character":
						case "java.lang.Interger":
						case "java.lang.Long":
						case "java.lang.Double":
						case "java.lang.Float":
						case "java.lang.Short":
						case "java.lang.Byte":
							list.add(new WhereBean(key, value, WhereBean.EQ));
							break;
						default:
							break;
					}
				}
			}
		}
		return list;
	}

	/**
	 * 通过校验之后的处理
	 * @param data
	 * @return 错误信息
	 */
	protected String afterValidate(T data) {
		return null;
	}


}
