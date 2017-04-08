package cn.haison.waf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class GeneralTest {
	
	@Test
	@Ignore
	public void case1(){
		String u1="/console/center?program=MENU&key=10131";
		String u2="/console/center/MENU/10132/action?m=post";
		String expression="/[a-z]+/";
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(u2);
		if(m.matches()){
			
		}else{
			System.out.println("NOT FOUND");
		}
	}

	@Test
	@Ignore
	public void case2(){
		String text="192.*.10-12.*";
		String[] x=text.split("\\.");
		String expression="([\\d{1,3}|\\*|\\d{1,3}\\-\\d{1-3}]\\.?)+";
		Matcher m = Pattern.compile(expression).matcher(text);
		System.out.println(m.matches());
	}
	
	@Test
	@Ignore
	public void case3(){
		System.out.println(Boolean.parseBoolean("false"));
	}
	
	@Test
	@Ignore
	public void case4(){
		String url = "http://e.wodehr.cn/home/2/abc?x=1&y=2&location=http%2a%2cwodehr.cn";
		String exp=".*\\?(\\w+=\\d+&?)+";
		Pattern pattern = Pattern.compile(exp);
		Matcher m = pattern.matcher(url);
		if(m.matches()){
			System.out.println("matched");
			System.out.println(m.group(1));
		}
		System.out.println(url.replaceAll("=\\d+", "=#{number}"));
	}
	
	@Test
	@Ignore
	public void case5(){
		String text="char(85)";
		Pattern sql = Pattern.compile(".*(select|update|union|char\\().*", Pattern.CASE_INSENSITIVE);
		if(sql.matcher(text).matches()){
			System.out.println("sql matched");
		}else{
			System.out.println("sql not detected");
		}
	}
	
	@Test
	@Ignore
	public void ts6(){
		String text="30/60s";
		String e="(\\d{1,4})/(\\d{1,4})[s|m|h]";
		Pattern p = Pattern.compile(e);
		Matcher m = p.matcher(text);
		if(m.matches()){
			System.out.println("correct speed expression");
			System.out.println(m.group(1)+":"+m.group(2));
		}else{
			System.out.println("wrong speed expression");
		}
	}
	
	@Test
	public void ts7(){
		String expression="global except names";
		String text="x";
		Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		System.out.println(m.find());
	}
}
