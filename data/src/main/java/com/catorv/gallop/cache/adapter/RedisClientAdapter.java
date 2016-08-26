package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * RedisClient Adapter
 * Created by cator on 8/3/16.
 */
public class RedisClientAdapter<V> implements Cache<String, V> {

	private Logger logger = null;

	private static final SerializingTranscoder transcoder = new SerializingTranscoder();

	private ObjectMapper objectMapper;

	private JedisPool adaptee;

	private long valueExpireSeconds;

	private String namespace;

	/**
	 * @param jedisPool Jedis poll
	 * @param objectMapper the object mapper from Jackson
	 * @param namespace namespace
	 * @param valueExpireSeconds the duration of value expiration in seconds.
	 */
	public RedisClientAdapter(JedisPool jedisPool, ObjectMapper objectMapper,
	                          String namespace, long valueExpireSeconds,
	                          Logger logger) {
		this.adaptee = jedisPool;
		this.namespace = namespace;
		this.valueExpireSeconds = valueExpireSeconds;
		this.objectMapper = objectMapper;
		this.logger = logger;
	}

	@Override
	public V get(String key) {
		return (new JedisPoolInvokeTemplate() {

			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				return jedis.hget(namespace, key);
			}
		}).execute(key, null);
	}

	@Override
	public Map<String, V> getAll(Iterable<? extends String> keys) {
		return (new JedisPoolInvokeTemplate() {
			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				return jedis.hget(namespace, key);
			}
		}).execute(keys);
	}

	@Override
	public void put(String key, V value) {
		(new JedisPoolInvokeTemplate() {
			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				jedis.hset(namespace, key, value);
				return null;
			}
		}).execute(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> values) {
		(new JedisPoolInvokeTemplate() {
			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				jedis.hset(namespace, key, value);
				return null;
			}
		}).execute(values);
	}

	@Override
	public void invalidate(String key) {
		(new JedisPoolInvokeTemplate() {
			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				jedis.hdel(namespace, key);
				return null;
			}
		}).execute(key, null);
	}

	@Override
	public void invalidateAll(Iterable<? extends String> keys) {
		(new JedisPoolInvokeTemplate() {

			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				jedis.hdel(namespace, key);
				return null;
			}
		}).execute(keys);
	}

	@Override
	public void invalidateAll() {
		(new JedisPoolInvokeTemplate() {
			@Override
			protected String invoke(Jedis jedis, String key, String value) {
				jedis.del(key);
				return null;
			}
		}).execute(namespace, null);
	}

	private abstract class JedisPoolInvokeTemplate {

		void execute(Map<? extends String, ? extends V> values) {
			if (adaptee == null) {
				return;
			}

			Jedis jedis = null;
			try {
				jedis = adaptee.getResource();
				for (String key : values.keySet()) {
					execute0(jedis, key, values.get(key));
				}
			} catch (Exception e) {
				logger.error("Jedis operation error", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}

		Map<String, V> execute(Iterable<? extends String> keys) {

			if (adaptee == null) {
				return Collections.emptyMap();
			}

			Jedis jedis = null;
			Map<String, V> values = new HashMap<>();
			try {
				jedis = adaptee.getResource();
				for (String key : keys) {
					V v = execute0(jedis, key, null);
					values.put(key, v);
				}
				return values;
			} catch (Exception e) {
				logger.error("Jedis operation error", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
			return Collections.emptyMap();
		}

		V execute(String key, V value) {
			if (adaptee == null) {
				return null;
			}

			Jedis jedis = null;
			try {
				jedis = adaptee.getResource();
				return execute0(jedis, key, value);
			} catch (Exception e) {
				logger.error("Jedis operation error", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private V execute0(Jedis jedis, String key, V value) throws IOException {
			String ret;

			if (value == null) {
				ret = invoke(jedis, key, null);
			} else {
				String content = objectMapper.writeValueAsString(new RedisCachedData(
						transcoder.encode(value)));
				ret = invoke(jedis, key, content);
			}

			if (ret == null) {
				return null;
			}

			RedisCachedData rcd = objectMapper.readValue(ret, RedisCachedData.class);
			if (System.currentTimeMillis() - rcd.getCreatedTime() >= valueExpireSeconds * 1000) {
				jedis.hdel(namespace, key);
				return null;
			}
			return (V) transcoder.decode(rcd.getCachedData());
		}

		protected abstract String invoke(Jedis jedis, String key, String value);
	}
}

