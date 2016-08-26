package com.catorv.gallop.redis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis client
 * Created by cator on 8/2/16.
 */
public class Redis {

	private JedisTemplate jedisTemplate;

	public Redis(JedisTemplate jedisTemplate) {
		this.jedisTemplate = jedisTemplate;
	}

	public <V> List<V> execute(Iterable<? extends String> keys, JedisTemplate.Invoker<V> invoker) {
		return jedisTemplate.execute(keys, invoker);
	}

	public <V> V execute(String key, String value, JedisTemplate.Invoker<V> invoker) {
		return jedisTemplate.execute(key, value, invoker);
	}

	public <V> V execute(JedisTemplate.Invoker<V> invoker) {
		return jedisTemplate.execute(invoker);
	}

	public <V> List<V> execute(Map<String, String> map, JedisTemplate.Invoker<V> invoker) {
		return jedisTemplate.execute(map, invoker);
	}

	public <V> V execute(String key, JedisTemplate.Invoker<V> invoker) {
		return jedisTemplate.execute(key, invoker);
	}

	public Boolean exists(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Boolean>() {
			@Override
			public Boolean invoke(Jedis jedis, String key, String value) {
				return jedis.exists(key);
			}
		});
	}

	public Set<String> keys(final String pattern) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.keys(pattern);
			}
		});
	}

	public String get(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.get(key);
			}
		});
	}

	public Long strlen(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.strlen(key);
			}
		});
	}

	public String getSet(String key, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.getSet(key, value);
			}
		});
	}

	public List<String> mget(final String... keys) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<List<String>>() {
			@Override
			public List<String> invoke(Jedis jedis, String key, String value) {
				return jedis.mget(jedisTemplate.buildRegion(keys));
			}
		});
	}

	public String set(String key, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.set(key, value);
			}
		});
	}

	public Long setnx(String key, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.setnx(key, value);
			}
		});
	}

	public String setex(String key, final int seconds, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.setex(key, seconds, value);
			}
		});
	}

	public String psetex(String key, final long milliseconds, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.psetex(key, milliseconds, value);
			}
		});
	}

	public Long expire(final String key, final int seconds){
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>(){

			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.expire(key, seconds);
			}
		});
	}

	public Long pexpire(final String key, final long milliseconds){
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>(){

			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.pexpire(key, milliseconds);
			}
		});
	}

	public Long ttl(final String key){
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>(){

			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.ttl(key);
			}
		});
	}

	public Long pttl(final String key){
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>(){

			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.pttl(key);
			}
		});
	}

	public String mset(final String... keysvalues) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.mset(jedisTemplate.buildRegion(2, keysvalues));
			}
		});
	}

	public Long append(String key, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.append(key, value);
			}
		});
	}

	public String getRange(String key, final long start, final long end) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.getrange(key, start, end);
			}
		});
	}

	public Long setRange(String key, final long offset, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.setrange(key, offset, value);
			}
		});
	}

	public Long decr(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.decr(key);
			}
		});
	}

	public Long decrBy(String key, final Long decrement) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.decrBy(key, decrement);
			}
		});
	}

	public Long incr(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.incr(key);
			}
		});
	}

	public Long incrBy(String key, final Long increment) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.incrBy(key, increment);
			}
		});
	}

	public Double incrByFloat(String key, final double increment) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Double>() {
			@Override
			public Double invoke(Jedis jedis, String key, String value) {
				return jedis.incrByFloat(key, increment);
			}
		});
	}

	public Long del(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.del(key);
			}
		});
	}

	public Boolean hexists(String key, String field) {
		return jedisTemplate.execute(key, field, new JedisTemplate.Invoker<Boolean>() {
			@Override
			public Boolean invoke(Jedis jedis, String key, String field) {
				return jedis.hexists(key, field);
			}
		});
	}

	public Set<String> hkeys(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.hkeys(key);
			}
		});
	}

	public List<String> hvals(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<List<String>>() {
			@Override
			public List<String> invoke(Jedis jedis, String key, String value) {
				return jedis.hvals(key);
			}
		});
	}

	public Long hdel(String key, final String... fields) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.hdel(key, fields);
			}
		});
	}

	public Long hlen(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.hlen(key);
			}
		});
	}

	public String hget(String key, String field) {
		return jedisTemplate.execute(key, field, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String field) {
				return jedis.hget(key, field);
			}
		});
	}

	public List<String> hmget(String key, final String... fields) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<List<String>>() {
			@Override
			public List<String> invoke(Jedis jedis, String key, String value) {
				return jedis.hmget(key, fields);
			}
		});
	}

	public Map<String, String> hgetAll(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Map<String, String>>() {
			@Override
			public Map<String, String> invoke(Jedis jedis, String key, String value) {
				return jedis.hgetAll(key);
			}
		});
	}

	public Long hset(String key, final String field, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.hset(key, field, value);
			}
		});
	}

	public String hmset(String key, final Map<String,String> fieldValueMap) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.hmset(key, fieldValueMap);
			}
		});
	}

	public Long hincrBy(String key, String field, final long increment) {
		return jedisTemplate.execute(key, field, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String field) {
				return jedis.hincrBy(key, field, increment);
			}
		});
	}

	public Double hincrByFloat(String key, String field, final double increment) {
		return jedisTemplate.execute(key, field, new JedisTemplate.Invoker<Double>() {
			@Override
			public Double invoke(Jedis jedis, String key, String field) {
				return jedis.hincrByFloat(key, field, increment);
			}
		});
	}

	public Long llen(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.llen(key);
			}
		});
	}

	public String lindex(String key, final long index) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.lindex(key, index);
			}
		});
	}

	public Long linsert(String key, final Client.LIST_POSITION where,
	                    final String pivot, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.linsert(key, where, pivot, value);
			}
		});
	}

	public String lpop(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.lpop(key);
			}
		});
	}

	public Long lpush(String key, final String... values) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.lpush(key, values);
			}
		});
	}

	public Long lpushx(String key, final String... values) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.lpushx(key, values);
			}
		});
	}

	public List<String> lrange(String key, final long start, final long stop) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<List<String>>() {
			@Override
			public List<String> invoke(Jedis jedis, String key, String value) {
				return jedis.lrange(key, start, stop);
			}
		});
	}

	public Long lrem(String key, final long count, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.lrem(key, count, value);
			}
		});
	}

	public String lset(String key, final long index, String value) {
		return jedisTemplate.execute(key, value, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.lset(key, index, value);
			}
		});
	}

	public String ltrim(String key, final long start, final long end) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.ltrim(key, start, end);
			}
		});
	}

	public String rpop(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.rpop(key);
			}
		});
	}

	public String rpoplpush(String srckey, String destkey) {
		return jedisTemplate.execute(srckey, destkey, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String srckey, String destkey) {
				return jedis.rpoplpush(srckey, destkey);
			}
		});
	}

	public Long rpush(String key, final String... values) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.rpush(key, values);
			}
		});
	}

	public Long rpushx(String key, final String... values) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.rpushx(key, values);
			}
		});
	}

	public Long sadd(String key, final String... member) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.sadd(key, member);
			}
		});
	}

	public Long scard(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.scard(key);
			}
		});
	}

	public Set<String> sdiff(final String... keys) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.sdiff(jedisTemplate.buildRegion(keys));
			}
		});
	}

	public Long sdiffstore(String dstkey, final String... keys) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.sdiffstore(key, jedisTemplate.buildRegion(keys));
			}
		});
	}

	public Set<String> sinter(final String... keys) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.sinter(jedisTemplate.buildRegion(keys));
			}
		});
	}

	public Long sinterstore(String dstkey, final String... keys) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.sinterstore(key, jedisTemplate.buildRegion(keys));
			}
		});
	}

	public Boolean sismember(String key, String member) {
		return jedisTemplate.execute(key, member,
				new JedisTemplate.Invoker<Boolean>() {
					@Override
					public Boolean invoke(Jedis jedis, String key, String member) {
						return jedis.sismember(key, member);
					}
				});
	}

	public Set<String> smembers(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.smembers(key);
			}
		});
	}

	public Long smove(String srckey, final String dstkey, String member) {
		return jedisTemplate.execute(srckey, member, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String member) {
				return jedis.smove(key, jedisTemplate.buildRegion(dstkey), member);
			}
		});
	}

	public String spop(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.spop(key);
			}
		});
	}

	public String srandmember(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.srandmember(key);
			}
		});
	}

	public Long srem(String key, final String... member) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.srem(key, member);
			}
		});
	}

	public ScanResult<String> sscan(final String key, final String cursor) {
		return jedisTemplate.execute(key, cursor,
				new JedisTemplate.Invoker<ScanResult<String>>() {
					@Override
					public ScanResult<String> invoke(Jedis jedis, String key, String cursor) {
						return jedis.sscan(key, cursor);
					}
				});
	}

	public Set<String> sunion(final String... keys) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.sunion(jedisTemplate.buildRegion(keys));
			}
		});
	}

	public Long sunionstore(String dstkey, final String... keys) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.srem(key, jedisTemplate.buildRegion(keys));
			}
		});
	}

	public Long zadd(String key, final double score, final String member) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zadd(key, score, member);
			}
		});
	}

	public Long zadd(String key, final Map<String, Double> scoreMembers) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zadd(key, scoreMembers);
			}
		});
	}

	public Long zcard(String key) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zcard(key);
			}
		});
	}

	public Long zcount(String key, final double min, final double max) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zcount(key, min, max);
			}
		});
	}

	public Long zcount(String key, final String min, final String max) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zcount(key, min, max);
			}
		});
	}

	public Double zincrby(String key, final double score, final String member) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Double>() {
			@Override
			public Double invoke(Jedis jedis, String key, String value) {
				return jedis.zincrby(key, score, member);
			}
		});
	}

	public Long zinterstore(String dstkey, final String... sets) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zinterstore(key, jedisTemplate.buildRegion(sets));
			}
		});
	}

	public Long zinterstore(String dstkey, final ZParams params, final String... sets) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zinterstore(key, params, jedisTemplate.buildRegion(sets));
			}
		});
	}

	public Set<String> zrange(String key, final long start, final long end) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrange(key, start, end);
			}
		});
	}

	public Set<String> zrangeByScore(String key, final double min, final double max) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScore(key, min, max);
			}
		});
	}

	public Set<String> zrangeByScore(String key, final String min, final String max) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScore(key, min, max);
			}
		});
	}

	public Set<String> zrangeByScore(String key, final double min, final double max,
	                                 final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScore(key, min, max, offset, count);
			}
		});
	}

	public Set<String> zrangeByScore(String key, final String min, final String max,
	                                 final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScore(key, min, max, offset, count);
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, final double min, final double max) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScoreWithScores(key, min, max);
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, final double min, final double max,
	                                          final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, final String min, final String max) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScoreWithScores(key, min, max);
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(String key,
	                                          final String min, final String max,
	                                          final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
			}
		});
	}

	public Set<String> zrevrange(String key, final long start, final long end) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrange(key, start, end);
			}
		});
	}

	public Set<String> zrevrangeByScore(String key, final double max, final double min) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScore(key, max, min);
			}
		});
	}

	public Set<String> zrevrangeByScore(String key, final String max, final String min) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScore(key, max, min);
			}
		});
	}

	public Set<String> zrevrangeByScore(String key, final double max, final double min,
	                                    final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			}
		});
	}

	public Set<String> zrevrangeByScore(String key, final String max, final String min,
	                                    final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<String>>() {
			@Override
			public Set<String> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, final double max, final double min) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScoreWithScores(key, max, min);
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, final String max, final String min) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScoreWithScores(key, max, min);
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, final double max, final double min,
	                                             final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, final String max, final String min,
	                                             final int offset, final int count) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<Set<Tuple>>() {
			@Override
			public Set<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			}
		});
	}

	public Long zrevrank(String key, String member) {
		return jedisTemplate.execute(key, member, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String member) {
				return jedis.zrevrank(key, member);
			}
		});
	}

	public ScanResult<Tuple> zscan(final String key, final String cursor) {
		return jedisTemplate.execute(key, new JedisTemplate.Invoker<ScanResult<Tuple>>() {
			@Override
			public ScanResult<Tuple> invoke(Jedis jedis, String key, String value) {
				return jedis.zscan(key, cursor);
			}
		});
	}

	public Double zscore(String key, String member) {
		return jedisTemplate.execute(key, member, new JedisTemplate.Invoker<Double>() {
			@Override
			public Double invoke(Jedis jedis, String key, String member) {
				return jedis.zscore(key, member);
			}
		});
	}

	public Long zunionstore(String dstkey, final String... sets) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zunionstore(key, sets);
			}
		});
	}

	public Long zunionstore(String dstkey, final ZParams params, final String... sets) {
		return jedisTemplate.execute(dstkey, new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.zunionstore(key, params, sets);
			}
		});
	}

	public String bgrewriteaof() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.bgrewriteaof();
			}
		});
	}

	public String bgsave() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.bgsave();
			}
		});
	}

	public String clientKill(final String client) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.clientKill(client);
			}
		});
	}

	public String clientList() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.clientList();
			}
		});
	}

	public String clientGetName() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.clientGetname();
			}
		});
	}

	public String clientSetname(final String name) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.clientSetname(name);
			}
		});
	}

	public List<String> configGet(final String pattern) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<List<String>>() {
			@Override
			public List<String> invoke(Jedis jedis, String key, String value) {
				return jedis.configGet(pattern);
			}
		});
	}

	public String configSet(final String parameter, final String value) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.configSet(parameter, value);
			}
		});
	}

	public String configResetStat() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.configResetStat();
			}
		});
	}

	public Long dbSize() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.dbSize();
			}
		});
	}

	public String debug(final DebugParams params) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.debug(params);
			}
		});
	}

	public String flushAll() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.flushAll();
			}
		});
	}

	public String flushDB() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.flushDB();
			}
		});
	}

	public String info() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.info();
			}
		});
	}

	public String info(final String section) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.info(section);
			}
		});
	}

	public Long lastsave() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<Long>() {
			@Override
			public Long invoke(Jedis jedis, String key, String value) {
				return jedis.lastsave();
			}
		});
	}

	public void monitor(final JedisMonitor jedisMonitor) {
		jedisTemplate.execute(new JedisTemplate.Invoker<Object>() {
			@Override
			public Object invoke(Jedis jedis, String key, String value) {
				jedis.monitor(jedisMonitor);
				return null;
			}
		});
	}

	public String save() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.save();
			}
		});
	}

	public String shutdown() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.shutdown();
			}
		});
	}

	public String slaveof(final String host, final int port) {
		return jedisTemplate.execute(new JedisTemplate.Invoker<String>() {
			@Override
			public String invoke(Jedis jedis, String key, String value) {
				return jedis.slaveof(host, port);
			}
		});
	}

	public void sync() {
		jedisTemplate.execute(new JedisTemplate.Invoker<Object>() {
			@Override
			public Object invoke(Jedis jedis, String key, String value) {
				jedis.sync();
				return null;
			}
		});
	}

	public List<String> time() {
		return jedisTemplate.execute(new JedisTemplate.Invoker<List<String>>() {
			@Override
			public List<String> invoke(Jedis jedis, String key, String value) {
				return jedis.time();
			}
		});
	}

}

