package cn.haison.waf.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.seawaf.utils.CacheUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class CacheTest {
	
	@Before
	public void setUp(){
		Cache cache = CacheUtils.getInstance().getCache("session.url.counters");
		for(int i=0;i<1000;i++){
			cache.put(new Element("SESSION:"+i, i*2));
		}
	}
	
	@After
	public void tearDown(){
		CacheUtils.getInstance().clear("session.url.counters");
	}
	
	@Test
	public void testTimeToLive(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cache cache = CacheUtils.getInstance().getCache("session.url.counters");
		for(int i=0;i<10;i++){
			Element element = cache.get("SESSION:"+i);
			if(element!=null){
				System.out.println(element.getObjectValue());
			}else{
				System.out.println("element not exists");
			}
		}
	}

}
