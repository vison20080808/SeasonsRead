package com.seasonsread.app.util;

import java.util.TreeMap;

/**
 * @author Bo Hu
 *         created at 1/30/11, 5:19 PM
 */
public class ObjectCache<K, V> {
	private int maxSize = 10;
	private TreeMap<K, V> cache;


	public ObjectCache(int maxSize) {
		this.maxSize = maxSize > 0 ? maxSize : 10;
		cache = new TreeMap<K, V>();
	}

	public synchronized void put(K k, V v) {
		cache.put(k, v);
		if (cache.size() > maxSize)
			cache.remove(cache.firstKey());
	}

	public synchronized V get(K k) {
		V v = cache.get(k);

		/*if (PapayaConfig.DEV_BUILD) {
			if (v == null)
				missed++;
			else
				hits++;
			
			if (D)
			d("url cache status: size %d, hits %d, missed %d, hit rate: %f", cache.size(), hits, missed, hits* 100.0 / (hits + missed));
		}*/

		return v;
	}

	public synchronized V remove(K k) {
		return cache.remove(k);
	}

	public synchronized int size() {
		return cache.size();
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void clear() {
		cache.clear();
	}
}