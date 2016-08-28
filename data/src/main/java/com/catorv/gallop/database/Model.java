package com.catorv.gallop.database;

import com.catorv.gallop.database.entity.Entity;
import com.catorv.gallop.util.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by cator on 8/27/16.
 */
public class Model<E extends Entity> {

	public Model(E entity) {
		try {
			ReflectUtils.copy(entity, this);
			configure(entity);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	protected void configure(E entity) {
		// nothing
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entity, T extends Model<E>> T of(E entity, Class<T> modelClass) {
		try {
			Constructor constructor = modelClass.getDeclaredConstructor(entity.getClass());
			constructor.setAccessible(true);
			T model = (T) constructor.newInstance(entity);
			ReflectUtils.copy(entity, model);
			model.configure(entity);
			return model;
		} catch (InstantiationException | IllegalAccessException |
				NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
