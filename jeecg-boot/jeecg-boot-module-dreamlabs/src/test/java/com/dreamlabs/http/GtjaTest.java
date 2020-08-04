package com.dreamslab.http;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import cn.hutool.core.text.StrSpliter;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.mitm.TrustSource;
import net.lightbody.bmp.proxy.CaptureType;

@Slf4j
public class SeleniumTest {

	private WebDriver driver;
	
	private BrowserMobProxy proxy;
	
	private boolean isProxy = false;
	
	private boolean isCustomSSL = false;
	
	private static String cookies = "_displaytype=.mobile; forcedisplay=.mobile; accountType=7; loginAccount=420303198209121736; loginBranch=; loginWay=quickly; suitability=0; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1594098225; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593951272,1594046079,1594085123,1594095108; _tcs=49446c09b94574c27fcf735f48f02927; _tcs-sb=5678186005745533545; _mall-suid=23ee9793bf82f12816b65ff3ad46e107; _msg-suid=90e4aff8221f7480700b608c3beb6e44; _cos-suid=27021c6c30024a0ae704041f12ad9d31; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730f222ab4ad2-0a41af2199f277-7427e73-304704-1730f222ab5ca9%22%2C%22%24device_id%22%3A%221730f222ab4ad2-0a41af2199f277-7427e73-304704-1730f222ab5ca9%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%7D; _tcs-tm=2020-07-05 18:41:59; Hm_lvt_8677059eb5096cf2cc4ba1a3476a1f6b=1593686633";

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setupTest() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "iPhone X");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("Host: i.gtja.com");
		chromeOptions.addArguments("Connection: keep-alive");
		chromeOptions.addArguments("Upgrade-Insecure-Requests: 1");
		chromeOptions.addArguments("User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.13(0x17000d2a) NetType/WIFI Language/zh_CN");
		chromeOptions.addArguments("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		chromeOptions.addArguments("Sec-Fetch-Site: same-origin");
		chromeOptions.addArguments("Sec-Fetch-Mode: navigate");
		chromeOptions.addArguments("Sec-Fetch-User: ?1");
		chromeOptions.addArguments("Sec-Fetch-Dest: document");
		chromeOptions.addArguments("Accept-Encoding: gzip, deflate, br");
		chromeOptions.addArguments("Accept-Language: zh,zh-CN;q=0.9");
		
	    
		if(isProxy) {
			driver = getWebDriverWithProxy(chromeOptions);
		}else {
			driver = getWebDriver(chromeOptions);
		}
		driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
		
		
	}
	
	
	private WebDriver getWebDriver(ChromeOptions chromeOptions) {
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	
	private WebDriver getWebDriverWithProxy(ChromeOptions chromeOptions) {
		
	    
		proxy = new BrowserMobProxyServer();
		 
	    if(isCustomSSL) {
	        // your root CA certificate(s) may be in a Java KeyStore, a PEM-encoded File or String, or an X509Certificate
	    	File pemEncodedCAFile = new File("/Users/Frank/Temp/browsermob/i.gtja.com.cer");
	    	TrustSource trustSource = TrustSource.defaultTrustSource().add(pemEncodedCAFile);
	        
	    	File caFile = new File("/Users/Frank/Temp/browsermob/cacert.pem");
	    	trustSource.add(caFile);
	        
	        File fillderFile = new File("/Users/Frank/Temp/browsermob/fillder.crt");
	        trustSource.add(fillderFile);
	        
	        // when using BrowserMob Proxy, use the .setTrustSource() method:  
	        proxy.setTrustSource(trustSource);
	        // or disable server certificate validation:
	        proxy.setTrustAllServers(true);
	    }
	    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
	    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
	    
	    List<String> whitelist = new ArrayList<String>();
	    whitelist.add("https?://i\\\\.gtja\\\\.com/.*.json");
	    proxy.whitelistRequests(whitelist,200);
	    proxy.start(0);
	    
	    
	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
	    chromeOptions.setCapability(CapabilityType.PROXY, seleniumProxy);
	    chromeOptions.addArguments("--ignore-certificate-errors");
	    
	    // start the browser up
	    WebDriver driver = new ChromeDriver(chromeOptions);
	    return driver;
	}

	@After
	public void teardown() {
		
		if (proxy != null) {
			proxy.abort();
		}
		
		if (driver != null) {
			driver.quit();
		}
	}

	
	private void login() throws Exception {
		driver.get("https://i.gtja.com/wxcos/weixin/in.html?url=https%3A%2F%2Fi.gtja.com%2Fquotes%2Fv-index.html%23%2Ftrade%3Fv%3D469%26v%3D350");
		 
		Date expire = new Date(System.currentTimeMillis() + 10000000000L);
		List<String> cookieStrs = StrSpliter.split(cookies, "; ", true, true);

		for (String cStr : cookieStrs) {
			List<String> value = StrSpliter.split(cStr, "=", true, true);
			if ("JSESSIONID".equals(value.get(0))) {
				Cookie cookie = new Cookie(value.get(0), value.get(1), "i.gtja.com", "/wxcos", expire, false, true);
				driver.manage().addCookie(cookie);
				log.info("Cookie is :{}",cookie);
				continue;
			}

			if (value.size() == 2) {
				Cookie cookie = new Cookie(value.	get(0), value.get(1), "i.gtja.com", "/", expire);
				driver.manage().addCookie(cookie);
				log.info("Cookie is :{}",cookie);
			} else {
				Cookie cookie = new Cookie(value.get(0), "", "i.gtja.com", "/", expire);
				driver.manage().addCookie(cookie);
				log.info("Cookie is :{}",cookie);
			}

		}
		if(proxy!=null) {
			proxy.newHar("i.gtja.com");
		}
		
		
		driver.get("https://i.gtja.com/evercos/in.html");
		
		driver.findElement(By.id("quicklyPassword")).sendKeys("828100");
		driver.findElement(By.id("quicklyLogin")).click();

		driver.get("https://i.gtja.com/evercos/securities/index.html");

		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		
		
		 // get the HAR data
		if(proxy!=null) {
			  Har har = proxy.getHar();
			  log.info("Har is {}.",har);
			    
			  File harFile = new File("/Users/Frank/Temp/browsermob/hari.gtja.com.json");
		      har.writeTo(harFile);
		}
	}
	
	
	@Test
	public void dreamlabs_site() {

	    // create a new HAR with the label "dreamlabs.site"
	    proxy.newHar("dreamlabs.site");
	    
	    // open dreamlabs.site
	    driver.get("http://dreamlabs.site/#/dashboard");

	    // get the HAR data
	    Har har = proxy.getHar();
	    log.info("Har is {}.",har);
	    
	    try {
	    	Thread.sleep(3000000);
	    }catch(Exception e) {
	    	
	    }
	}
	
	
	private void logout() throws Exception {
		//持仓记录
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		Thread.sleep(3000);
		//交易记录		
		driver.get("https://i.gtja.com/evercos/securities/stock/query/deal.html");
		Thread.sleep(3000);
		//退出
		driver.get("https://i.gtja.com/mall/eplus/rest/logOut.json");
		Thread.sleep(3000);
		//首页
		driver.manage().deleteAllCookies();
		driver.get("https://i.gtja.com");
		Thread.sleep(30000);
	}
	
	@Test
	public void login_request() throws Exception {
		login();
		logout();
	}
	
	
	
	@Test
	public void ipo_shares() throws Exception{
		//0.登录
		login();
	
		//1.新股页面
		driver.get("https://i.gtja.com/evercos/securities/stock/query/ipo/shares.html");
		driver.get("https://i.gtja.com/evercos/securities/stock/query/ipo/oneKeyBuyBuy.html");
		Thread.sleep(5000);
		
		//2.一键申购
		// $("#oneKeyBuy")
		Object result=((JavascriptExecutor) driver).executeScript("$(\"#oneKeyBuy\").click()");
		log.info("Result is {}",result);
		
		//3.等待30秒
		Thread.sleep(30000);
	}
	
	@Test
	public void buy() throws Exception{
		//0.登录
		login();
		
		//1.打开买入页面
		driver.get("https://i.gtja.com/quotes/v-index.html");
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/buy_index.html");
		Thread.sleep(5000);
		
		//2.输入股票代码		
		driver.findElement(By.id("stockCode")).sendKeys("000002");
		driver.findElement(By.id("stockCode")).sendKeys(Keys.TAB);
		Thread.sleep(5000);
		
		//3.输入买入金额
		driver.findElement(By.id("entrustPrice")).sendKeys("28.650");
		driver.findElement(By.cssSelector("[class='stepper-btn refresh-price']")).click();
		for(int i=0 ; i < 100; i++) {
			driver.findElement(By.cssSelector("[class='stepper-btn before']")).click();
		}
		driver.findElement(By.id("entrustPrice")).sendKeys(Keys.TAB);
		Thread.sleep(5000);
		
		//4.输入买入份额
		driver.findElement(By.id("entrustAmount")).sendKeys("100");
		Thread.sleep(5000);
		
		//5.提交	
		driver.findElement(By.id("submitBuy")).click();
		Thread.sleep(5000);
		
		//6.确认
		((JavascriptExecutor) driver).executeScript("$(\"#stockTrading\").ajaxSubmit({\n" + 
				"			url : '/evercos/securities/stock/trade/trading.json',\n" + 
				"			type : \"post\",\n" + 
				"			dataType : \"json\",\n" + 
				"			data : {\n" + 
				"				\"stockAccount\" : '0106044666',\n" + 
				"				\"fullAccountNo\" : '0311040018017037',\n" + 
				"				\"entrustProp\" :'U'\n" + 
				"			},\n" + 
				"			beforeSend : function(){\n" + 
				"				console.log(\"before\")\n" + 
				"			},\n" + 
				"			complete : function(){\n" + 
				"				console.log(\"complete\")\n" + 
				"			},\n" + 
				"			success : function(data) {\n" + 
				"				console.log(data)\n" + 
				"			}\n" + 
				"})");
		
		//7.等待30秒
		Thread.sleep(30000);
	}
	
	
	@Test
	public void sell() throws Exception{
		//0.登录
		login();

		//1.
		driver.get("https://i.gtja.com/quotes/v-index.html");
		
		Thread.sleep(3000);
		
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/buy_index.html");
		
		Thread.sleep(3000);
		
		// $("#oneKeyBuy")
		Object result = ((JavascriptExecutor) driver).executeScript("$(\"#oneKeyBuy\").click()");
		
		log.info("Result is {}",result);
		
		Thread.sleep(30000);
	}
	
}
