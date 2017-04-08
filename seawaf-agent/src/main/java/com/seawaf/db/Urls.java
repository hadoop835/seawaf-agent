package com.seawaf.db;

import com.seawaf.utils.CacheUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Results;

public class Urls {
	public static String findUrlById(Integer uid){
		if(uid!=null){
			Cache cache = CacheUtils.getInstance().getCache("urls");
			Query query = cache.createQuery();
			Attribute<Integer> v = cache.getSearchAttribute("value");
			query.includeKeys().includeValues();
			query.addCriteria(v.eq(uid));
			Results results = query.execute();
			if(results!=null){
				return results.all().get(0).getKey().toString();
			}
		}
		return null;
	}
}
