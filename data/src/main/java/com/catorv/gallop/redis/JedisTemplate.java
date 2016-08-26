package com.catorv.gallop.redis;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Jedis Template
 * Created by cator on 8/2/16.
 */
@Singleton
public class JedisTemplate {

	private JedisPool pool;

	private String namespace;

	private Logger logger;

	public JedisTemplate(String namespace, JedisPool pool, Logger logger) {
		this.namespace = namespace;
		this.pool = pool;
		this.logger = logger;
	}

	public <V> List<V> execute(Iterable<? extends String> keys, Invoker<V> invoker) {
		if (pool != null) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				List<V> results = new ArrayList<V>();
				for (String key : keys) {
					results.add(invoker.invoke(jedis, buildRegion(key), null));
				}
				return results;
			} catch (Exception e) {
				logger.error("Jedis operation error", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return Collections.emptyList();
	}

	public <V> List<V> execute(Map<String, String> map, Invoker<V> invoker) {
		if (pool != null) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				List<V> results = new ArrayList<V>();
				for (String key : map.keySet()) {
					results.add(invoker.invoke(jedis, buildRegion(key), map.get(key)));
				}
				return results;
			} catch (Exception e) {
				logger.error("Jedis operation error", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return Collections.emptyList();
	}

	public <V> V execute(String key, String value, Invoker<V> invoker) {
		if (pool != null) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return invoker.invoke(jedis, buildRegion(key), value);
			} catch (Exception e) {
				logger.error("Jedis operation error", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}

		return null;
	}

	public <V> V execute(String key, Invoker<V> invoker) {
		return execute(key, null, invoker);
	}

	public <V> V execute(Invoker<V> invoker) {
		return execute(null, null, invoker);
	}

	public static abstract class Invoker<T> {
		public abstract T invoke(Jedis jedis, String key, String value);
	}

	public String buildRegion(String region) {
		if (region != null && !Strings.isNullOrEmpty(namespace)) {
			region = namespace + "." + region;
		}
		return region;
	}

	public String[] buildRegion(int step, String... regions) {
		String[] keys = new String[regions.length];
		for (int i = 0; i < regions.length; i += step) {
			keys[i] = buildRegion(regions[i]);
		}
		return keys;
	}

	public String[] buildRegion(String... regions) {
		return buildRegion(1, regions);
	}
}

