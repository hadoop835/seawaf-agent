package cn.haison.waf.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.seawaf.utils.CacheUtils;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Result;

public class CacheUtilsTest {

	private static CacheUtils cacheUtils;
	private static int times;
	
	@BeforeClass
	public static void setUp(){
		cacheUtils = CacheUtils.getInstance();
		times=10001;
		for(int i=1;i<times;i++){
			cacheUtils.put("sessions", "KEY-"+i, i);
		}
	}
	@Before
	public void setUp2(){
		for(int i=0;i<10;i++){
			for(int j=1;j<20;j++){
				CacheUtils.getInstance().put("sessions", "192.168."+i+"."+j, 1);
			}
			CacheUtils.getInstance().enc("sessions", "192.168."+i+".5");
			CacheUtils.getInstance().enc("sessions", "192.168."+i+".8");
			CacheUtils.getInstance().enc("sessions", "192.168."+i+".9");
			CacheUtils.getInstance().enc("sessions", "192.168."+i+".9");
			CacheUtils.getInstance().enc("sessions", "192.168."+i+".9");
		}
	}
	
	@After
	public void clear(){
//		CacheUtils.getInstance().clear("sessions");
	}
	
	@Test
	@Ignore
	public void testGet(){
		for(int i=1;i<times;i++){
			Object v = cacheUtils.get("sessions", "KEY-"+i);
			int value = Integer.parseInt(v.toString());
			assertEquals(i, value);
		}
	}
	
	@Test
	@Ignore
	public void testRemove(){
		for(int i=0;i<times;i++){
			cacheUtils.remove("sessions", "KEY-"+i);
		}
		assertEquals(0, cacheUtils.count("sessions"));
	}
	
	@Test
	public void testQuery(){
		List<Result> list = CacheUtils.getInstance().keysOrderByIntValue("sessions", "192.168.1.*");
		Attribute<Integer> value = CacheUtils.getInstance().getCache("sessions").getSearchAttribute("value");
		for(Result result:list){
			System.out.println(result.getKey()+":"+result.getValue()+":"+result.getAttribute(value));
		}
	}
	
	@AfterClass
	public static void tearDown(){
		cacheUtils.clear("sessions");
		cacheUtils.shutdown();
	}
}
