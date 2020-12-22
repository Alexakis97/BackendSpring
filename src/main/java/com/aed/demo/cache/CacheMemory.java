package com.aed.demo.cache;

import java.util.concurrent.TimeUnit;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class CacheMemory implements CacheInterface {

	private Cache<String, Integer> cache;
	private final int MAX_ATTEMPT = 10;

	public CacheMemory() {
		System.out.println("Create Infinispan Cache");
		ConfigurationBuilder cacheConfig = new ConfigurationBuilder();
		cacheConfig.expiration().lifespan(1, TimeUnit.DAYS);
		DefaultCacheManager cacheManager = new DefaultCacheManager();
		cacheManager.defineConfiguration("local", cacheConfig.build());
		cache = cacheManager.getCache("local");
		cache.addListener(new MyListener());
		System.out.println("cache " + cache.getName());
	}

	@Override
	public Object getEntry(String key) {
		return cache.get(key);
	}

	@Override
	public void putEntry(String ip, int attempts) {
		cache.put(ip, attempts);
		
	}

	public void loginSucceeded(String key) {
		if (cache.containsKey(key)) {
			cache.remove(key);
		}
	}

	public void loginFailed(String key) {
		int attempts = 0;
		if (cache.containsKey(key)) {
			attempts = cache.get(key);
		}

		attempts++;
		cache.put(key, attempts);
	}

	public boolean isBlocked(String key) {
		if (cache.containsKey(key)) {
			return cache.get(key) >= MAX_ATTEMPT;
		} else {
			return false;
		}

	}

	@Listener
	public class MyListener {
		@CacheEntryCreated
		public void entryCreated(CacheEntryCreatedEvent<String, Integer> event) {
			if (!event.isPre()) {
				System.out.println("Added Entry " + event.getKey());
			}
		}

	}

}