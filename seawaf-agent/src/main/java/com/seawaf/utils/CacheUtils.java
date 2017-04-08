package com.seawaf.utils;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.TransactionController;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

/**
 *  add the following code to web.xml
 *   <listener>
 *       <listener-class>net.sf.ehcache.constructs.web.ShutdownListener</listener-class>
 *   </listener>
 * @author zhugl
 *
 */
public class CacheUtils {
	
	private CacheManager cacheManager;
	private static CacheUtils cacheUtils;
	
	public CacheUtils(){
		InputStream stream = this.getClass().getResourceAsStream("/cache.xml");
		this.cacheManager = CacheManager.create(stream);
	}
	
	public static CacheUtils getInstance(){
		if(cacheUtils!=null){
			return cacheUtils;
		}else{
			cacheUtils = new CacheUtils();
			return cacheUtils;
		}
	}
	
	public void put(String cacheName,String key,Object value){
		Cache cache = this.cacheManager.getCache(cacheName);  
		if(cache!=null){
			Element element = new Element(key, value);  
			cache.put(element);  
		}
	}
	
	public Object get(String cacheName,String key){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			Element element = cache.get(key);
			if(element!=null){
				return element.getObjectValue();
			}
		}
		return null;
	}
	
	public int getIntValue(String cacheName,String key){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			return cache.get(key)==null?-1:Integer.parseInt(cache.get(key).getObjectValue().toString());
		}
		return -1;
	}
	
	public String getStringValue(String cacheName,String key){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			return cache.get(key)==null?null:cache.get(key).getObjectValue().toString();
		}
		return null;
	}
	
	public Cache getCache(String name){
		return this.cacheManager.getCache(name);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> keys(String cacheName){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			return (List<String>)cache.getKeys();
		}
		return null;
	}
	
	public List<Result> keysOrderByIntValue(String cacheName,String keyPattern){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			Attribute<String> key = cache.getSearchAttribute("key");
			Attribute<Integer> value = cache.getSearchAttribute("value");
			Query query = cache.createQuery();
			query.includeAttribute(key).includeAttribute(value).includeKeys().includeValues();
			query.addCriteria(key.ilike(keyPattern)).addOrderBy(value, Direction.DESCENDING);
			Results results = query.execute();
			if(results!=null){
				return results.all();
			}
		}
		return null;
	}
	
	public List<Result> keys(String cacheName,String keyPattern){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			Attribute<String> key = cache.getSearchAttribute("key");
			Query query = cache.createQuery();
			query.includeKeys().includeValues();
			query.addCriteria(key.ilike(keyPattern));
			Results results = query.execute();
			if(results!=null){
				return results.all();
			}
			
		}
		return null;
	}
	
	public int count(String cacheName){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			return cache.getSize();
		}
		return 0;
	}
	
	public void remove(String cacheName,String key){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			cache.remove(key);
		}
	}
	
	public void clear(String cacheName){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			cache.removeAll();
		}
	}
	
	public void save(String cacheName){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			cache.flush();
		}
	}
	
	public void shutdown(){
		this.cacheManager.shutdown();
	}
	
	public int enc(String cacheName,String key){
		TransactionController tc = this.cacheManager.getTransactionController();
		Cache cache = this.cacheManager.getCache(cacheName);
		int v=0;
		if(cache!=null){
			Element element = cache.get(key);
			tc.begin();
			if(element!=null){
				v = Integer.parseInt(element.getObjectValue().toString());
			}
			cache.put(new Element(key,v+1));
			tc.commit();
		}
		return v+1;
	}
	
	public void autoKeyId(String cacheName,String key){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			if(cache.get(key)==null){
				cache.put(new Element(key,cache.getSize()+10001));
			}
		}
	}
	
	public boolean exists(String cacheName,String key){
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			return cache.get(key)!=null;
		}
		return false;
	}
	
	
	public void dec(String cacheName,String key){
		TransactionController tc = this.cacheManager.getTransactionController();
		Cache cache = this.cacheManager.getCache(cacheName);
		if(cache!=null){
			Element element = cache.get(key);
			if(element!=null){
				tc.begin();
				int v = Integer.parseInt(element.getObjectValue().toString());
				cache.put(new Element(key,v-1));
				tc.commit();
			}
		}
	}
	
	public void create(String cacheName,int ttl,int maxElement){
		Cache cache = new Cache(new CacheConfiguration(cacheName, maxElement).timeToLiveSeconds(ttl).timeToIdleSeconds(ttl));
		this.cacheManager.addCache(cache);
	}
	
	public String[] cacheNames(){
		return this.cacheManager.getCacheNames();
	}
}
