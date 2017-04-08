# seawaf-agent
An open source web application firewall component
# features
* exception manager 
*  counter,capture,protect
* quota manager (max sessions,max sessions per user,max online users,max single url per session in self-defin time unit)
* attack defence (SQL Injection,XSS)
* muti-mode support
# screenshots
seawaf index page
![](https://github.com/zhuinfo/seawaf-agent/blob/master/seawaf-agent/images/index.png)
when 500 error detected
![](https://github.com/zhuinfo/seawaf-agent/blob/master/seawaf-agent/images/exceptions.png)
when 500 error occur times more than max permitted ,the exception url will be protected for a while
![](https://github.com/zhuinfo/seawaf-agent/blob/master/seawaf-agent/images/protected.png)
# how to
Adding the follow configuration to your web.xml
```xml
<filter>
  	<filter-name>security-filter</filter-name>
  	<filter-class>com.seawaf.filters.WafFilter</filter-class>
  </filter>
  <filter-mapping>
  	<filter-name>security-filter</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
  <listener>
  	<listener-class>com.seawaf.listeners.WafSessionAttrListener</listener-class>
  </listener>
  <listener>
  	<listener-class>com.seawaf.listeners.WafSessionListener</listener-class>
  </listener>
  <servlet>
  	<servlet-name>seawaf</servlet-name>
  	<servlet-class>com.seawaf.SecurityCenter</servlet-class>
  </servlet>
  <servlet-mapping>
  	<servlet-name>seawaf</servlet-name>
  	<url-pattern>/seawaf</url-pattern>
  </servlet-mapping>
 ```
 # configuration
 copy the following text to waf.xml and put it to /your/webapp/WEB-INF
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<waf>
	<application>
		<id>EHR</id>
		<name>Human Resource Management System</name>
		<ip>192.168.1.131</ip>
		<port>8080</port>
		<active.mode>prd</active.mode>
		<session-user-attribute-name>user</session-user-attribute-name>
		<session-user-name-path>name</session-user-name-path>
		<session-user-id-path>id</session-user-id-path>
		<administrator id="1023" name="smith" email="smith@abc.com"></administrator>
		<auditor id="2189" name="frank" email="frank@abc.com"></auditor>
	</application>
	<!-- you can define several mode but actually only one mode was activated -->
	<mode id="prd">
		<exceptions>
			<capture>true</capture>
			<mailto>john@abc.com</mailto>
		</exceptions>
		<quotas>
			<max-sessions>1000</max-sessions>
			<max-sessions-per-user>1</max-sessions-per-user>
			<max-online-users>500</max-online-users>
			<!-- user can open the same URL 10 times per session in 5 seconds,the default time unit is second-->
			<max-same-url-open-per-session>10/5s</max-same-url-open-per-session>
			<max-exceptions-per-url>5</max-exceptions-per-url>
			<out-of-service-redirect-url>http://127.0.0.1</out-of-service-redirect-url>
		</quotas>
		<defences>
			<!-- enable cross site request forgery attack defence -->
			<csrf>true</csrf>
			<!-- enable CC attack defence -->
			<cc>true</cc>
			<input-validator>
				<except-names>global except names</except-names>
				<except-urls>global except urls</except-urls>
				<pattern>
					<name>SQL</name>
					<description>SQL Inject Detect</description>
					<expression><![CDATA[
					select|union|and|or|&&|from|dual|char\(
					]]></expression>
					<except-names>password</except-names>
					<except-urls></except-urls>
					<action>replace</action><!-- warn|intercept|replace -->
				</pattern>
				<pattern>
					<name>XSS</name>
					<description>XSS Attack Detect</description>
					<expression><![CDATA[
					<script>|iframe|frame
					]]></expression>
					<except-names>password</except-names>
					<except-urls></except-urls>
					<action>replace</action>
				</pattern>
				<pattern>
					<name>dangerous-char</name>
					<description>Dangerous Char Detect</description>
					<expression><![CDATA[
					@@|%|!
					]]></expression>
					<except-names>password</except-names>
					<except-urls></except-urls>
					<action>replace</action>
				</pattern>
			</input-validator>
		</defences>
	</mode>
	<mode id="dev">
		<exceptions>
			<capture>true</capture>
			<mailto>john@abc.com</mailto>
		</exceptions>
		<quotas>
			<max-sessions>10</max-sessions>
			<max-sessions-per-user>1</max-sessions-per-user>
			<max-online-users>5</max-online-users>
			<!-- user can open the same URL 30 times per session in 10 seconds,the default time unit is second-->
			<max-same-url-open-per-session>30/10s</max-same-url-open-per-session>
			<max-exceptions-per-url>5</max-exceptions-per-url>
			<out-of-service-redirect-url>http://127.0.0.1</out-of-service-redirect-url>
		</quotas>
		<defences>
			<csrf>enabled</csrf>
			<cc>enabled</cc>
			<input-validator>
				<except-names>global except names</except-names>
				<except-urls>global except urls</except-urls>
				<pattern>
					<name>SQL</name>
					<description>SQL Inject Detect</description>
					<expression><![CDATA[
					select|union|and|or|&&|from|dual|char(|
					]]></expression>
					<except-names>password</except-names>
					<except-urls></except-urls>
					<action>warn</action>
				</pattern>
				<pattern>
					<name>XSS</name>
					<description>XSS Attack Detect</description>
					<expression><![CDATA[
					<script>|iframe|frame
					]]></expression>
					<except-names></except-names>
					<except-urls></except-urls>
					<action>intercept</action>
				</pattern>
				<pattern>
					<name>dangerous-char</name>
					<description>Dangerous Char Detect</description>
					<expression><![CDATA[
					@@|%|!|=|<|>
					]]></expression>
					<except-names></except-names>
					<except-urls></except-urls>
					<action>replace</action>
				</pattern>
			</input-validator>
		</defences>
	</mode>
</waf>
```
