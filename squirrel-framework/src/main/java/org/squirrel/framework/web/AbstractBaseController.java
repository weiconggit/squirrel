package org.squirrel.framework.web;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.squirrel.framework.SquirrelInitializer;
import org.squirrel.framework.auth.Auth;
import org.squirrel.framework.auth.AuthManager;
import org.squirrel.framework.response.Rp;
import org.squirrel.framework.response.RpEnum;
import org.squirrel.framework.spring.ApplicationContextHelper;
import org.squirrel.framework.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.swagger.annotations.ApiOperation;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

/**
 * controller接口通用实现
 * @author weicong
 * @time 2020年11月25日
 * @version 1.0
 * @param <V>
 * @param <V>
 */
public abstract class AbstractBaseController<S extends ServiceImpl<? extends Mapper<V>, V>, V> 
		implements BaseController<V>, SquirrelInitializer {
	
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
	private Class<V> classVO;
	
	/**
	 * 当前服务类型
	 */
	private Class<S> classService;
	
	/**
	 * mybatis-plus 服务实现
	 */
	protected S serviceImpl;
	
	private Validator validator;
	
	@SuppressWarnings("unchecked")
	@Override
	public final void init() {
		// 可放入无参构造中先行执行
		Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		this.classService = (Class<S>) actualTypeArguments[0];
		this.classVO = (Class<V>) actualTypeArguments[1]; 
		// 需等待 ApplicationContextHelper创建之后
		this.serviceImpl = (S)ApplicationContextHelper.getBean(StrUtil.lowerFirstLetter(classService.getSimpleName()));
		this.validator = ApplicationContextHelper.getBean(Validator.class);
	}
	
	
	
	// ~ common api
	// ================================================
	
	@Auth(AuthManager.GET)
	@ApiOperation(value = "获取列表信息")
	@GetMapping(value = LIST)
	@Override
	public Rp<Collection<V>> list(@RequestParam(name = "query", required = false) Map<String, Object> query) {
		return listImpl(query);
	}
	
	@Auth(AuthManager.GET)
	@ApiOperation(value = "获取分页信息")
	@GetMapping(value = PAGE)
	@Override
	public Rp<Page<V>> page(@RequestParam(name = "query", required = false) Map<String, Object> query
			, @RequestParam(value = "current") String current, @RequestParam(value = "size") String size, @RequestParam(value = "sort", required = false) String sort) {
		return pageImpl(query, current, size, sort);
	}
	
	@Auth(AuthManager.GET)
	@ApiOperation(value = "获取详情信息")
	@GetMapping(value = DETAIL)
	@Override
	public Rp<V> detail(@PathVariable(value = "id") String id) {
		return detailImpl(id);
	}

	@Auth(AuthManager.ADD)
	@ApiOperation(value = "新增信息")
	@PostMapping(value = ADD)
	@Override
	public Rp<V> add(@RequestBody V data) {
		return addImpl(data);
	}

	@Auth(AuthManager.EDIT)
	@ApiOperation(value = "修改信息")
	@PutMapping(value = EDIT)
	@Override
	public Rp<V> edit(@PathVariable(value = "id") String id, @RequestBody V data) {
		return editImpl(id, data);
	}
	
	@Auth(AuthManager.DEL)
	@ApiOperation(value = "删除信息")
	@DeleteMapping(value = DEL)
	@Override
	public Rp<V> delete(@PathVariable(value = "ids") Set<String> ids) {
		return deleteImpl(ids);
	}

	
	
	// ~ custom impl
	// ================================================
	
	protected Rp<Collection<V>> listImpl(Map<String, Object> query){
		if (query == null) {
			query = new HashMap<>();
			query.put(ISDEL, false);
		} 
		query.put(ISDEL, false);
		Collection<V> listByMap = serviceImpl.listByMap(query);
		return Rp.success(listByMap);
	}
	
	protected Rp<Page<V>> pageImpl(Map<String, Object> query, String current, String size, String sort){
		Page<V> page = createPage(current, size, sort);
		QueryWrapper<V> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(ISDEL, false);
		Page<V> result = null;
		if (query == null || query.isEmpty()) {
			result = serviceImpl.page(page, queryWrapper); // mysql tinyint 1 true, 0 false
		} else {
			query.entrySet().forEach(v -> queryWrapper.like(v.getKey(), v.getValue()));
			result = serviceImpl.page(page, queryWrapper);
		}
		return Rp.success(result);
	}
	
	protected Rp<V> detailImpl(String id){
		return Rp.success(serviceImpl.getById(id));
	}
	
	@Transactional
	protected Rp<V> addImpl(V data){
		List<ConstraintViolation> validate = validator.validate(data);
		if (!validate.isEmpty()) {
			Rp.failed(RpEnum.CUSTOM_MSG, validate.get(0).getMessage());
		}
		String afterValidate = afterValidate(data);
		if (afterValidate != null) {
			Rp.failed(RpEnum.CUSTOM_MSG, validate.get(0).getMessage());
		}
		
		beforeSave(data);
//		(clazzTable)data;
		boolean save = serviceImpl.save(data);
		if (save) {
			afterSave(data);
			return Rp.success();
		}
		return Rp.failed(RpEnum.ERROR_SYSTEM);
	}
	
	@Transactional
	protected Rp<V> editImpl(String id, V data){
		List<ConstraintViolation> validate = validator.validate(data);
		if (!validate.isEmpty()) {
			return Rp.failed(RpEnum.CUSTOM_MSG, validate.get(0).getMessage());
		}
		String afterValidate = afterValidate(data);
		if (afterValidate != null) {
			return Rp.failed(RpEnum.CUSTOM_MSG, afterValidate);
		}
		// 比对和本地是否相同，可以重写T的toString、hashCode和equals方法实现
		V byId = serviceImpl.getById(id);
		if (StrUtil.equals(byId.toString(), data.toString())) {
			return Rp.success(); // 没有任何修改直接返回即可
		}
		
		beforeUpdate(data);
		boolean updateById = serviceImpl.updateById(data);
		if (updateById) {
			afterUpdate(data);
			return Rp.success();
		}
		return Rp.failed(RpEnum.ERROR_SYSTEM);
	}

	@Transactional
	protected Rp<V> deleteImpl(Set<String> ids) {
		UpdateWrapper<V> updateWrapper = new UpdateWrapper<>();
		ids.forEach(id -> updateWrapper.eq("id", id).or());
		updateWrapper.set(ISDEL, true);
		
		beforeDel(ids);
		boolean update = serviceImpl.update(updateWrapper);
		if (update) {
			afterDel(ids);
			return Rp.success();
		}
		return Rp.failed(RpEnum.ERROR_SYSTEM);
	}
	
	
	
	protected String afterValidate(V data) {
		return null;
	}
	
	protected void beforeSave(V data) {}
	
	protected void afterSave(V data) {}
	
	protected void beforeUpdate(V data) {}
	
	protected void afterUpdate(V data) {}
	
	protected void beforeDel(Set<String> ids) {}
	
	protected void afterDel(Set<String> ids) {}
	
	
	
	// ~ util
	// ================================================
	
	/**
	 * 创建分页及排序
	 * @param current
	 * @param size
	 * @param sort 规则：asc,字段1,字段2
	 * @return
	 */
	protected Page<V> createPage(String current, String size,  String sort) {
		Page<V> page = new Page<>(Long.parseLong(current), Long.parseLong(size));
		if (StrUtil.isNotEmpty(sort)) {
			String[] sortAndFields = sort.split(",", 2);
			List<OrderItem> orderItems = null;
			if (StrUtil.equals(sortAndFields[0], DESC)) {
				orderItems = OrderItem.descs(sortAndFields[1].split(","));
			} else {
				orderItems = OrderItem.ascs(sortAndFields[1].split(","));
			}
			page.setOrders(orderItems);
		}
		return page;
	}
	
}
