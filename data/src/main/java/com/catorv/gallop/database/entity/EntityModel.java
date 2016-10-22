package com.catorv.gallop.database.entity;

import com.catorv.gallop.util.ReflectUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by cator on 8/27/16.
 */
public class EntityModel<E extends Entity> implements Serializable {

	public EntityModel(E entity) {
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
	public static <E extends Entity, T extends EntityModel<E>> T of(E entity, Class<T> modelClass) {
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

	public static <E extends Entity, T extends EntityModel<E>> List<T> listOf(Collection<E> entities, Class<T> modelClass) {
		List<T> list = new ArrayList<>();
		if (entities != null) {
			for (E entity : entities) {
				list.add(of(entity, modelClass));
			}
		}
		return list;
	}

}
