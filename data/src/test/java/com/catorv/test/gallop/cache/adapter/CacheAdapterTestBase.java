package com.catorv.test.gallop.cache.adapter;

import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cache.Cache;
import com.catorv.test.gallop.cache.model.CacheValueBean;
import com.google.inject.AbstractModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 *
 * Created by cator on 8/4/16.
 */
public abstract class CacheAdapterTestBase extends AbstractModule {

	protected abstract Cache<String, Object> getCache(CacheConfiguration configuration);

	@Test
	public void testCRUD() throws Exception {
		CacheConfiguration configuration = new CacheConfiguration();
		configuration.setRegionName("region");
		configuration.setExpireSeconds(10L);
		Cache<String, Object> cache = getCache(configuration);

		cache.put("name", "ting");
		Assert.assertEquals("ting", cache.get("name"));

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());
		cache.put("person", bean);

		CacheValueBean bean2 = (CacheValueBean) cache.get("person");
		Assert.assertEquals(bean, bean2);

		cache.invalidate("name");

		Assert.assertNull(cache.get("name"));

		cache.invalidateAll();
		Assert.assertNull(cache.get("person"));
	}

	@Test
	public void testExpireTime() throws Exception {
		CacheConfiguration configuration = new CacheConfiguration();
		configuration.setRegionName("region");
		configuration.setExpireSeconds(1L);
		Cache<String, Object> cache = getCache(configuration);

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());
		cache.put("person", bean);

		Thread.sleep(2000L);
		bean = (CacheValueBean) cache.get("person");
		Assert.assertNull(bean);
	}

	@Test
	public void testRegionIsolation() throws Exception {
		CacheConfiguration configuration = new CacheConfiguration();
		configuration.setRegionName("region");
		configuration.setExpireSeconds(10L);
		Cache<String, Object> cache1 = getCache(configuration);

		configuration.setRegionName("region2");
		Cache<String, Object> cache2 = getCache(configuration);

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());
		cache1.put("person", bean);
		cache2.put("person", bean);

		cache1.invalidateAll();
		bean = (CacheValueBean) cache1.get("person");
		Assert.assertNull(bean);

		bean = (CacheValueBean) cache2.get("person");
		Assert.assertNotNull(bean);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCollectionData() throws Exception {
		CacheConfiguration configuration = new CacheConfiguration();
		configuration.setRegionName("regionmap");
		configuration.setExpireSeconds(120L);
		Cache<String, Object> cache1 = getCache(configuration);

		List<CacheValueBean> listData = new ArrayList<>();

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());

		listData.add(bean);

		cache1.put("listcache", listData);

		List<CacheValueBean> listData2 = (List<CacheValueBean>) cache1.get("listcache");

		Assert.assertEquals(1, listData.size());

		bean = listData.get(0);
		Assert.assertEquals("xiaoting", bean.getName());

		cache1.invalidateAll();
		Assert.assertNull(cache1.get("listcache"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMapData() throws Exception {
		CacheConfiguration configuration = new CacheConfiguration();
		configuration.setRegionName("regionmap");
		configuration.setExpireSeconds(120L);
		Cache<String, Object> cache1 = getCache(configuration);

		Map<String, CacheValueBean> mapData = new HashMap<String, CacheValueBean>();

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());
		mapData.put("abc", bean);

		cache1.put("mapcache", mapData);
		Map<String, CacheValueBean> mapData2 = (Map<String, CacheValueBean>) cache1
				.get("mapcache");
		bean = mapData2.get("abc");
		Assert.assertEquals("xiaoting", bean.getName());

		cache1.invalidateAll();
		Assert.assertNull(cache1.get("mapcache"));
	}

	@Test
	public void testArrayData() throws Exception {
		CacheConfiguration configuration = new CacheConfiguration();
		configuration.setRegionName("regionmap");
		configuration.setExpireSeconds(120L);
		Cache<String, Object> cache1 = getCache(configuration);

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());

		CacheValueBean[] arrayData = new CacheValueBean[] {bean};

		cache1.put("arraycache", arrayData);

		CacheValueBean[] arrayData2 = (CacheValueBean[]) cache1.get("arraycache");

		Assert.assertEquals(1, arrayData2.length);

		bean = arrayData2[0];
		Assert.assertEquals("xiaoting", bean.getName());

		cache1.invalidateAll();
		Assert.assertNull(cache1.get("arraycache"));
	}
}
