package com.catorv.gallop.cache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cache Stats
 * Created by cator on 8/3/16.
 */
public class CacheStats {

	private AtomicInteger entryCount = new AtomicInteger(0);

	private AtomicInteger hitCount = new AtomicInteger(0);

	private AtomicInteger missCount = new AtomicInteger(0);

	private AtomicInteger accessCount = new AtomicInteger(0);

	private AtomicInteger invalidateCount = new AtomicInteger(0);

	private long lastAccess;

	private long lastUpdate;

	private long lastInvalidate;

	public int getEntryCount() {
		return entryCount.get();
	}

	public int getHitCount() {
		return hitCount.get();
	}

	public int getMissCount() {
		return missCount.get();
	}

	public int getAccessCount() {
		return accessCount.get();
	}

	public long getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getLastInvalidate() {
		return lastInvalidate;
	}

	public void setLastInvalidate(long lastInvalidate) {
		this.lastInvalidate = lastInvalidate;
	}

	public void incrAccess(int count, boolean hit) {
		this.accessCount.addAndGet(count);
		if (hit) {
			this.hitCount.addAndGet(count);
		} else {
			this.missCount.addAndGet(count);
		}
		this.lastAccess = System.currentTimeMillis();
	}

	public void incrUpdate(int count) {
		this.entryCount.addAndGet(count);
		this.lastUpdate = System.currentTimeMillis();
	}

	public void incrInvalidate(int count) {
		this.invalidateCount.addAndGet(1);
		this.entryCount.getAndAdd(count * -1);
		this.lastInvalidate = System.currentTimeMillis();
	}

	public void incrInvalidateAll() {
		this.invalidateCount.addAndGet(1);
		this.entryCount.set(0);
		this.accessCount.set(0);
		this.hitCount.set(0);
		this.missCount.set(0);
		this.lastInvalidate = System.currentTimeMillis();
	}
}

