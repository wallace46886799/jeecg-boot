package com.dreamlabs.http;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.dreamlabs.junit.BaseTest;

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
public class AbstractSeleniumTest extends BaseTest {

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
		//chromeOptions.addArguments("--headless");
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
		Thread.sleep(3000);
		driver.findElement(By.id("quicklyPassword")).sendKeys(super.decrypt8Password());
		Thread.sleep(3000);
		driver.findElement(By.id("quicklyLogin")).click();
		Thread.sleep(3000);
		driver.get("https://i.gtja.com/evercos/securities/index.html");
		
		 // get the HAR data
		if(proxy!=null) {
			  Har har = proxy.getHar();
			  log.info("Har is {}.",har);
			    
			  File harFile = new File("/Users/Frank/Temp/browsermob/hari.gtja.com.json");
		      har.writeTo(harFile);
		}
	}
	
	
	private void logout() throws Exception {
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
	
	
	/**
	 * 自动打新
	 * @throws Exception
	 */
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
		
		//3.等待5秒然后退出
		Thread.sleep(5000);
		logout();
	}
	
	/**
	 * <div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="0">
						<p class="pure-u-1-2 tl">万 科Ａ  <i>000002</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>43568.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="000002" data-exchange_type="2" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>1600.00</p><span>持股数量</span>
						<p>1600.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>27.23</p><span>现价</span>
						<p>27.152</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">124.30</p><span>浮动盈亏</span>	
						<p class="up">0.29%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
		</div>
		
		
		<div id="holdings-panel" class="eui-grid-content bg">
		
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="0">
						<p class="pure-u-1-2 tl">万 科Ａ  <i>000002</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>43568.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="000002" data-exchange_type="2" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>1600.00</p><span>持股数量</span>
						<p>1600.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>27.23</p><span>现价</span>
						<p>27.152</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">124.30</p><span>浮动盈亏</span>	
						<p class="up">0.29%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="1">
						<p class="pure-u-1-2 tl">美的集团  <i>000333</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>31950.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="000333" data-exchange_type="2" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>500.00</p><span>持股数量</span>
						<p>500.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>63.90</p><span>现价</span>
						<p>53.911</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">4994.60</p><span>浮动盈亏</span>	
						<p class="up">18.53%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="2">
						<p class="pure-u-1-2 tl">本钢转债  <i>127018</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>1000.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="127018" data-exchange_type="2" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>10.00</p><span>持股数量</span>
						<p>10.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>100.00</p><span>现价</span>
						<p>100.000</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p>0.00</p><span>浮动盈亏</span>	
						<p>0.00</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="3">
						<p class="pure-u-1-2 tl">微创光电  <i>430198</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>2001.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="430198" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>100.00</p><span>持股数量</span>
						<p>100.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>20.01</p><span>现价</span>
						<p>21.441</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="down">-143.12</p><span>浮动盈亏</span>	
						<p class="down">-6.68%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="4">
						<p class="pure-u-1-2 tl">苏轴股份  <i>430418</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>1795.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="430418" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>100.00</p><span>持股数量</span>
						<p>100.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>17.95</p><span>现价</span>
						<p>18.740</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="down">-79.00</p><span>浮动盈亏</span>	
						<p class="down">-4.22%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="5">
						<p class="pure-u-1-2 tl">麟龙股份  <i>430515</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>2198.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="430515" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>200.00</p><span>持股数量</span>
						<p>200.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>10.99</p><span>现价</span>
						<p>12.382</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="down">-278.31</p><span>浮动盈亏</span>	
						<p class="down">-11.25%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="6">
						<p class="pure-u-1-2 tl">蓝山科技  <i>830815</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>3114.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="830815" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>600.00</p><span>持股数量</span>
						<p>600.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>5.19</p><span>现价</span>
						<p>3.569</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">972.87</p><span>浮动盈亏</span>	
						<p class="up">45.42%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="7">
						<p class="pure-u-1-2 tl">鸿辉光通  <i>832063</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>3556.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="832063" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>400.00</p><span>持股数量</span>
						<p>400.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>8.89</p><span>现价</span>
						<p>5.736</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">1261.80</p><span>浮动盈亏</span>	
						<p class="up">54.99%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="8">
						<p class="pure-u-1-2 tl">利尔达  <i>832149</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>2795.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="832149" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>500.00</p><span>持股数量</span>
						<p>500.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>5.59</p><span>现价</span>
						<p>4.613</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">488.74</p><span>浮动盈亏</span>	
						<p class="up">21.18%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="9">
						<p class="pure-u-1-2 tl">贝特瑞  <i>835185</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>4591.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="835185" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>100.00</p><span>持股数量</span>
						<p>100.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>45.91</p><span>现价</span>
						<p>47.797</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="down">-188.67</p><span>浮动盈亏</span>	
						<p class="down">-3.95%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="10">
						<p class="pure-u-1-2 tl">用友金融  <i>839483</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>2371.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="839483" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>100.00</p><span>持股数量</span>
						<p>100.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>23.71</p><span>现价</span>
						<p>17.559</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="up">615.06</p><span>浮动盈亏</span>	
						<p class="up">35.04%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="11">
						<p class="pure-u-1-2 tl">颖泰生物  <i>889001</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>545.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="889001" data-exchange_type="9" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>100.00</p><span>持股数量</span>
						<p>100.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>5.45</p><span>现价</span>
						<p>5.450</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p>0.00</p><span>浮动盈亏</span>	
						<p>0.00</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
				<div class="eui-grid-row line">
					<div class="eui-grid-row-title" name="market" data-index="12">
						<p class="pure-u-1-2 tl">建设银行  <i>601939</i><i class="icon-stock-market"></i></p>
						<p class="pure-u-1-2 tr"><span class="icon-qiandai icon-total-holding"></span>25160.00</p>
					</div>
					<div class="eui-grid-row-content" data-stock_code="601939" data-exchange_type="1" name="sale">
					<div class="pure-u-2-5 eui-grid-col tl">
						<p>4000.00</p><span>持股数量</span>
						<p>4000.00</p><span>可卖数量</span>
					</div>
					<div class="pure-u-1-5 eui-grid-col">
						<p>6.29</p><span>现价</span>
						<p>6.724</p><span>成本价格</span>
					</div>
					<div class="pure-u-2-5 eui-grid-col">
						<p class="down">-1736.80</p><span>浮动盈亏</span>	
						<p class="down">-6.46%</p><span>盈亏比例</span>
					</div> 
					</div>
					<div class="eui-grid-row-market pure-u-1"></div>
				</div>            
			
		
	</div>
	 * 持仓	
	 * @throws Exception
	 */
	@Test
	public void holding_shares() throws Exception{
		//0.登录
		login();
	
		//1.持仓列表
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		Thread.sleep(5000);
		List<WebElement> elements  = driver.findElements(By.className("eui-grid-row"));
		int size = elements.size();
		
		String prefix = "//*[@id='holdings-panel']/div[";
		String surfix1 = "]/div[1]/p[1]";
		String surfix2 = "]/div[1]/p[2]";
		String surfix3 = "]/div[2]/div[1]/p[1]";
		String surfix4 = "]/div[2]/div[1]/p[2]";
		String surfix5 = "]/div[2]/div[2]/p[1]";
		String surfix6 = "]/div[2]/div[2]/p[2]";
		String surfix7 = "]/div[2]/div[3]/p[1]";
		String surfix8 = "]/div[2]/div[3]/p[2]";
		
		for(int i = 1 ;i < size-1; i++ ) {
			String name = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix1)).getText();
			String code = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix1+"/i[1]")).getText();
			////*[@id="holdings-panel"]/div[1]/div[1]/p[2]
			String holding = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix2)).getText(); 
			////*[@id="holdings-panel"]/div[1]/div[2]/div[1]/p[1]
			String shares =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix3)).getText();
			String available_shares =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix4)).getText();
			String price =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix5)).getText();
			String orig_price =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix6)).getText();
			String fy =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix7)).getText();
			String fyr =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix8)).getText();
			
			System.out.println("=====================");
			System.out.println(name);
			System.out.println(code);
			System.out.println(holding);
			System.out.println(shares);
			System.out.println(available_shares);
			System.out.println(price);
			System.out.println(orig_price);
			System.out.println(fy);
			System.out.println(fyr);
		}
		
		//2.等待5秒然后退出
		Thread.sleep(5000);
		logout();
	}
	
	/**
	 * 自动购买
	 * @throws Exception
	 */
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
		
		//7.等待5秒然后退出
		Thread.sleep(5000);
		logout();
	}
	
	/**
	 * 自动卖出
	 * @throws Exception
	 */
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
		
		//7.等待5秒然后退出
		Thread.sleep(5000);
		logout();
	}
	
	/**
	 * 资金余额
	 * @throws Exception
	 */
	@Test
	public void balance_detail() throws Exception{
		//0.登录
		login();
		//1.持仓列表
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		Thread.sleep(5000);
		List<WebElement> elements  = driver.findElements(By.className("eui-grid-row"));
		int size = elements.size();
				
		
		Thread.sleep(300000);
	}
	
	/**
	 * 当日交易记录
	 * @throws Exception
	 */
	@Test
	public void current_trans_detail() throws Exception{
		//0.登录
		login();
		//1.持仓列表
		driver.get("https://i.gtja.com/evercos/securities/stock/query/deal.html");
		Thread.sleep(5000);
						
		
		Thread.sleep(300000);
	}
	
	/**
	 * 历史交易记录
	 * @throws Exception
	 */
	@Test
	public void history_trans_detail() throws Exception{
		//0.登录
		login();
		//1.持仓列表
		driver.get("https://i.gtja.com/evercos/securities/stock/query/deal.html");
		Thread.sleep(5000);
						
		
		Thread.sleep(300000);
	}
	
}
