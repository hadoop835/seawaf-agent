package cn.haison.waf.utils;

import org.junit.Before;
import org.junit.Test;

import com.seawaf.config.WafConfig;
import com.seawaf.utils.ConfigReader;

public class ConfigReaderTest {
	private ConfigReader configReader;
	
	@Before
	public void setUp(){
		this.configReader = new ConfigReader();
	}
	
	@Test
	public void testLoad(){
		try {
			WafConfig config = this.configReader.load();
			System.out.println(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
