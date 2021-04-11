package org.squirrel.framework.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.oval.Check;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import net.sf.oval.exception.ValidationFailedException;
import net.sf.oval.internal.ClassChecks;
import net.sf.oval.internal.ContextCache;
import net.sf.oval.internal.util.Assert;
import net.sf.oval.internal.util.ReflectionUtils;
import net.sf.oval.localization.message.ResourceBundleMessageResolver;

/**
 * 重写oval校验逻辑，使其校验到一个错误后即可停止返回，不在继续校验之后的字段
 * @author weicong
 * @time   2020年12月22日
 * @version 1.0
 */
public class ValidatorHelper extends Validator {

	@Override
	protected void validateInvariants(Object validatedObject, List<ConstraintViolation> violations, String[] profiles)
			throws IllegalArgumentException, ValidationFailedException {
		Assert.argumentNotNull("validatedObject", validatedObject);

		currentlyValidatedObjects.get().getLast().add(validatedObject);
		if (validatedObject instanceof Class< ? >) {
			_validateStaticInvariantsHelper((Class< ? >) validatedObject, violations, profiles);
		} else {
			_validateObjectInvariantsHelper(validatedObject, validatedObject.getClass(), violations, profiles);
		}	
		
		// 使用自定义错误提示语言
		ResourceBundleMessageResolver resolver = (ResourceBundleMessageResolver) Validator.getMessageResolver();
		String path = "oval/Messages_cn";
		resolver.addMessageBundle(ResourceBundle.getBundle(path));
	}

	
	/**
	 * 本方法为 _validateObjectInvariants(validatedObject, validatedObject.getClass(), violations, profiles) 源码，改动为校验有错即可停止返回
	 * 
	 * validate validatedObject based on the constraints of the given class 
	 */
	private void _validateObjectInvariantsHelper(final Object validatedObject, final Class< ? > clazz,
			final List<ConstraintViolation> violations, final String[] profiles) throws ValidationFailedException
	{
		assert validatedObject != null;
		assert clazz != null;
		assert violations != null;

		// abort if the root class has been reached
		if (clazz == Object.class) return;

		try
		{
			final ClassChecks cc = getClassChecks(clazz);

			// validate field constraints
			for (final Field field : cc.constrainedFields)
			{
				final Collection<Check> checks = cc.checksForFields.get(field);

				if (checks != null && checks.size() > 0)
				{
					final Object valueToValidate = ReflectionUtils.getFieldValue(field, validatedObject);
					final OValContext ctx = ContextCache.getFieldContext(field);

					for (final Check check : checks) {
						checkConstraint(violations, check, validatedObject, valueToValidate, ctx, profiles, false, 
								false);
						// 2020年12月22日 新增自定义逻辑，校验出字段错误停止循环
						if (!violations.isEmpty()) {
							break;
						}
					}
					
				}
				// 2020年12月22日
				if (!violations.isEmpty()) {
					break;
				}
			}

			// validate constraints on getter methods
			for (final Method getter : cc.constrainedMethods)
			{
				final Collection<Check> checks = cc.checksForMethodReturnValues.get(getter);

				if (checks != null && checks.size() > 0)
				{
					final Object valueToValidate = ReflectionUtils.invokeMethod(getter, validatedObject);
					final OValContext ctx = ContextCache.getMethodReturnValueContext(getter);

					for (final Check check : checks) {
						checkConstraint(violations, check, validatedObject, valueToValidate, ctx, profiles, false,
								false);
					}
				}
			}

			// validate object constraints
			if (cc.checksForObject.size() > 0)
			{
				final OValContext ctx = ContextCache.getClassContext(clazz);
				for (final Check check : cc.checksForObject) {
					checkConstraint(violations, check, validatedObject, validatedObject, ctx, profiles, false, false);
				}
			}

			// if the super class is annotated to be validatable also validate it against the object
			_validateObjectInvariantsHelper(validatedObject, clazz.getSuperclass(), violations, profiles);
		}
		catch (final OValException ex)
		{
			throw new ValidationFailedException("Object validation failed. Class: " + clazz + " Validated object: "
					+ validatedObject, ex);
		}
	}
	
	/**
	 * 本方法为_validateStaticInvariants((Class< ? >) validatedObject, violations, profiles)源码，改动为校验有错即可停止返回
	 * 
	 * Validates the static field and static getter constrains of the given class.
	 * Constraints specified for super classes are not taken in account.
	 */
	private void _validateStaticInvariantsHelper(final Class< ? > validatedClass, final List<ConstraintViolation> violations,
			final String[] profiles) throws ValidationFailedException
	{
		assert validatedClass != null;
		assert violations != null;

		final ClassChecks cc = getClassChecks(validatedClass);

		// validate static field constraints
		for (final Field field : cc.constrainedStaticFields)
		{
			final Collection<Check> checks = cc.checksForFields.get(field);

			if (checks != null && checks.size() > 0)
			{
				final Object valueToValidate = ReflectionUtils.getFieldValue(field, null);
				final OValContext context = ContextCache.getFieldContext(field);

				for (final Check check : checks) {
					checkConstraint(violations, check, validatedClass, valueToValidate, context, profiles, false, false);
					if (!violations.isEmpty()) {
						break;
					}
				}
			}
			if (!violations.isEmpty()) {
				break;
			}
		}

		// validate constraints on getter methods
		for (final Method getter : cc.constrainedStaticMethods)
		{
			final Collection<Check> checks = cc.checksForMethodReturnValues.get(getter);

			if (checks != null && checks.size() > 0)
			{
				final Object valueToValidate = ReflectionUtils.invokeMethod(getter, null);
				final OValContext context = ContextCache.getMethodReturnValueContext(getter);

				for (final Check check : checks) {
					checkConstraint(violations, check, validatedClass, valueToValidate, context, profiles, false, false);
				}
			}
		}
	}
	
}
