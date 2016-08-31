package com.biostime.finagle.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**上帝之手
 * through the class's name we can know,this class hope to help the peple who
 * use it to get all kinds of value.
 * 
 * @author Dylan
 * @time 2016-7-8
 */
public final class GodHands {

	private GodHands() {
	}

	/**
	 * 得到字段值
	 *  @param instance
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(final Object instance, final String fieldName) {

		Field field = getAccessibleField(instance, fieldName);
		DemonPredict.notNull(field,"fieldName not be found");
		try {
			return field.get(instance);
		} catch (IllegalAccessException e) {
			handleReflectionException(e);
		}
		return null;
	}

	/**
	 * 设置字段值
	 *  @param instance
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(final Object instance, final String fieldName, final Object value) {

		Field field = getAccessibleField(instance, fieldName);

		DemonPredict.notNull(field, "fieldName not be founded");

		try {
			field.set(instance, value);
		} catch (IllegalAccessException e) {
			handleReflectionException(e);
		}

	}

	/**
	 * 得到可用字段
	 * 
	 * @param instance
	 *            the target instance which we want to get
	 * @param fieldName
	 *            the name of field we want to get
	 * @return
	 */
	public static Field getAccessibleField(final Object instance, final String fieldName) {
		DemonPredict.notNull(instance, "the parameter of instance can't be null");
		// DemonPredict.notBlank(fieldName, "fieldName can't be blank", new
		// Object[0]);
		DemonPredict.notEmpty(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = instance.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {

			try {

				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;

			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}
	/**
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getAccessibleField(final Class<?> clazz, final String fieldName) {
		DemonPredict.notNull(clazz, "the parameter of instance can't be null");
		// DemonPredict.notBlank(fieldName, "fieldName can't be blank", new
		// Object[0]);
		DemonPredict.notEmpty(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {

			try {

				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;

			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}
	
	/**
	 * 拿属性的属性，如 fieldName 为son.name
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getAccessibleField2(final Class<?> clazz, final String fieldName) {
		DemonPredict.notEmpty(fieldName, "fieldName can not empty!");
		Class<?> tempClazz = clazz;
		String[] gradationField = fieldName.split(".");
		Field field = null;
		for (String f : gradationField) {
			field = getAccessibleField(tempClazz, f);
			if (null == field)
				return null;
			tempClazz = field.getType();
		}
		return field;
	}

	/**
	 * 
	 * @param instance
	 * @return
	 */
	public static Field[] getAccessibleFields(final Object instance) {
		DemonPredict.notNull(instance, "the parameter of instance can't be null");
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> superClass = instance.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field [] field = superClass.getDeclaredFields();
			for(Field f : field){
				makeAccessible(f);
				fields.add(f);
			}
		}
		return fields.toArray(new Field[fields.size()]);
	}
	
	/**
	 * @param clazz
	 * @return
	 */
	public static Field[] getAccessibleFields(final Class<?> clazz) {
		DemonPredict.notNull(clazz, "the parameter of instance can't be null");
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field [] field = superClass.getDeclaredFields();
			for(Field f : field){
				makeAccessible(f);
				fields.add(f);
			}
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * 得到可用方法
	 * 
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getAccessibleMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {

		for (Class<?> superClass = clazz; clazz != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				handleReflectionException(e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param instance
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getAccessibleMethod(final Object instance, final String methodName, final Class<?>... parameterTypes) {

		return getAccessibleMethod(instance.getClass(), methodName, parameterTypes);
	}

	/**
	 * 
	 * @param clazz
	 * @param annotationClass
	 * @return
	 */
	public static <A extends Annotation> A getAccessibleAnnotation(final Class<?> clazz, final Class<A> annotationClass) {

		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				A annotation = superClass.getAnnotation(annotationClass);
				return annotation;
			} catch (Exception e) {
				handleReflectionException(e);
			}

		}

		return null;

	}

	/**
	 * 执行方法
	 * 
	 * @param instance
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static Object invokeMethod(Object instance, String methodName, Class<?> parameterTypes, Object... args) {

		Method method = getAccessibleMethod(instance, methodName, parameterTypes);
		DemonPredict.notNull(method, "method "+methodName+" not be found");
		try {
			return method.invoke(instance, args);
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return null;
	}

	/**
	 * 得到泛型的类�? * @param clazz
	 * 
	 * @return
	 */
	public static Class<?>[] genericsTypes(Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();
			for (Type t : types) {
				clazzs.add((Class<?>) t);
			}
		}
		return clazzs.toArray(new Class<?>[clazzs.size()]);
	}

    /**
     * @param clazz
     * @return
     */
    public static Class<?>[] getInterfaces(Class<?> clazz){
        DemonPredict.notNull(clazz,"clazz can not be null");

        try {
            Class<?>[] clazzs = Class.forName(clazz.getName()).getInterfaces();
            return null != clazzs ? clazzs : new Class<?>[0];
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new Class<?>[0];
        }
    }

	/**
	 * 
	 * @param field
	 */
	public static void makeAccessible(Field field) {
		field.setAccessible(true);
	}

	/**
	 * 
	 * @param method
	 */
	public static void makeAccessible(Method method) {
		method.setAccessible(true);
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz) {

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return null;
	}

    public static <T> T newInstance(Class<T> clazz,Class<?>[] parameterTypes,Object[] parameters){
        try {
            Constructor<T> constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            handleReflectionException(e);
        }
        return null;
    }

	/**
	 * 
	 * @param fullClassName
	 * @return
	 */
	public static Object newInstance(String fullClassName) {
		try {
			Class<?> clazz = Class.forName(fullClassName);
			return newInstance(clazz);
		} catch (ClassNotFoundException e) {
			handleReflectionException(e);
		}
		return null;
	}

    public static <T> T newInstance(String fullClassName,Class<?>[] parameterTypes,Object[] parameters){
        try {
            Class<?> clazz = Class.forName(fullClassName);
            return (T)newInstance(clazz,parameterTypes,parameters);
        } catch (Exception e) {
            handleReflectionException(e);
        }
        return null;
    }
	
	/**
	 * 
	 * @param clazz
	 * @param annotationClazz
	 * @return
	 */
	public  static <A extends Annotation> Field[] getAnnotaionFields(Class<?> clazz, Class<A> annotationClazz) {
		List<Field> fields = new ArrayList<Field>();
		Field [] fieldz = getAccessibleFields(clazz);
		if(null != fieldz){
			for(Field field : fieldz){
				if(field.isAnnotationPresent(annotationClazz)){
					fields.add(field);
				}
			}
			
		}
		return fields.toArray(new Field[fields.size()]);
	}
	

	/**
	 * 异常处理方法
	 * 
	 * @param e
	 */
	private static void handleReflectionException(Exception e) {

		if (e instanceof NoSuchMethodException) {
			throw new IllegalStateException("method not found " + e.getMessage());
		}
		if (e instanceof NoSuchFieldException) {
			throw new IllegalStateException("field not fount " + e.getMessage());
		}
		if (e instanceof IllegalArgumentException) {
			throw new IllegalStateException("args is illegal" + e.getMessage());
		}
        if (e instanceof InvocationTargetException) {
            throw new IllegalStateException("can not invoke this method" + e.getMessage());
        }
        if (e instanceof InstantiationException) {
            throw new IllegalStateException("can not instance" + e.getMessage());
        }

		throw new UndeclaredThrowableException(e);

	}
}
