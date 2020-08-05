package com.dreamlabs.http;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.dreamlabs.junit.BaseTest;

import cn.hutool.core.util.NumberUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.mitm.TrustSource;
import net.lightbody.bmp.proxy.CaptureType;

@Slf4j
public class JsFundTest extends BaseTest {

	private WebDriver driver;
	
	private BrowserMobProxy proxy;
	
	private boolean isProxy = false;
	
	private boolean isCustomSSL = false;

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setupTest() {
		//Map<String, String> mobileEmulation = new HashMap<String, String>();
		//mobileEmulation.put("deviceName", "iPhone X");
		ChromeOptions chromeOptions = new ChromeOptions();
		//chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		//chromeOptions.addArguments("--headless");
//		chromeOptions.addArguments("Host: i.gtja.com");
//		chromeOptions.addArguments("Connection: keep-alive");
//		chromeOptions.addArguments("Upgrade-Insecure-Requests: 1");
//		chromeOptions.addArguments("User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.13(0x17000d2a) NetType/WIFI Language/zh_CN");
//		chromeOptions.addArguments("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//		chromeOptions.addArguments("Sec-Fetch-Site: same-origin");
//		chromeOptions.addArguments("Sec-Fetch-Mode: navigate");
//		chromeOptions.addArguments("Sec-Fetch-User: ?1");
//		chromeOptions.addArguments("Sec-Fetch-Dest: document");
//		chromeOptions.addArguments("Accept-Encoding: gzip, deflate, br");
//		chromeOptions.addArguments("Accept-Language: zh,zh-CN;q=0.9");
		
	    
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

	////*[@id="showTbl"]/tbody/tr[1]/td[2]
	////*[@id="showTb2"]/tbody/tr/td[1]
	private void login() throws Exception {
		driver.get("https://e.jsfund.cn/passport");
		 
		if(proxy!=null) {
			proxy.newHar("i.gtja.com");
		}
		
		
		Thread.sleep(3000);
		driver.findElement(By.id("loginno")).sendKeys("420303198209121736");
		Thread.sleep(3000);
		driver.findElement(By.id("pwd")).sendKeys(super.decrypt8Password());
		Thread.sleep(3000);
		driver.findElement(By.id("submit_btn")).click();
		Thread.sleep(3000);
		
		driver.get("https://e.jsfund.cn/custom");
		
		
		Thread.sleep(3000);
		
		 // get the HAR data
		if(proxy!=null) {
			  Har har = proxy.getHar();
			  log.info("Har is {}.",har);
			    
			  File harFile = new File("/Users/Frank/Temp/browsermob/hari.gtja.com.json");
		      har.writeTo(harFile);
		}
	}
	
	
	
	private void logout() throws Exception {
		////*[@id="header_id"]/div[1]/div/div[2]/a[1]
		driver.findElement(By.xpath("//*[@id=\"header_id\"]/div[1]/div/div[2]/a[1]")).click();
		Thread.sleep(3000);
	}
	
	@Test
	public void login_request() throws Exception {
		login();
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
	 * @throws Exception
	 * ////*[@id="showTbl"]/tbody/tr[1]/td[2]
	////*[@id="showTb2"]/tbody/tr/td[1]
	 */
	@Test
	public void holding_shares() throws Exception{
		//0.登录
		login();
	
		//1.持仓列表
		driver.get("https://e.jsfund.cn/custom");
		Thread.sleep(5000);
		List<WebElement> elements  = driver.findElements(By.xpath("//*[@id=\"showTbl\"]/tbody/tr"));
		int size = elements.size();
		
		String prefix = "//*[@id=\"showTbl\"]/tbody/tr[";
		String surfix1 = "]/td[1]";
		String surfix2 = "]/td[2]";
		String surfix3 = "]/td[3]";
		String surfix4 = "]/td[4]";
		String surfix5 = "]/td[5]";
		String surfix6 = "]/td[6]/span";
		String surfix7 = "]/td[7]";
		String surfix8 = "]/td[8]";
		String surfix9 = "]/td[9]";
		String surfix10 = "]/td[10]";
		String surfix11 = "]/td[11]";
		String surfix12 = "]/td[12]";
		
		////*[@id="showTbl"]/tbody/tr[1]/td[12]
		////*[@id="showTbl"]/tbody/tr[1]/td[3]
		List<Map<String, String>> holdShares = new ArrayList<Map<String, String>>();
		for(int i = 1 ;i <= size; i++ ) {

			Map<String, String> holdShare = new HashMap<String, String>();
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			////*[@id="currency156"]/div[2]/div[1] //*[@id="currency156"]/div[2]/div[2]
			//当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			//当前份额
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			//成本价 //*[@id="showTbl"]/tbody/tr[1]/td[9]
			String orig_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix9)).getText();
			//当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix9)).getText();
			//净值日期  //*[@id="showTbl"]/tbody/tr[1]/td[8]
			String price_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			//当日涨跌
			String current_percent = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			//当日收益 //*[@id="showTbl"]/tbody/tr[1]/td[6]/span
			String current_porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			//持仓收益 //*[@id="showTbl"]/tbody/tr[1]/td[5]
			String floating_porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			//累计收益
			String porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			//资产占比
			String asset_percent = "--";

			log.info("=====================");
			log.info("code:{}",code);
			holdShare.put("code", code);
			log.info("name:{}",name);
			holdShare.put("name", name);
			log.info("current_amount:{}",current_amount);
			holdShare.put("current_amount", current_amount);
			log.info("share:{}",share);
			holdShare.put("share", share);
			log.info("orig_price:{}",orig_price);
			holdShare.put("orig_price", orig_price);
			log.info("orig_price:{}",price);
			holdShare.put("price", price);
			log.info("price_date:{}",price_date);
			holdShare.put("price_date", price_date);
			log.info("current_percent:{}",current_percent);
			holdShare.put("current_percent", current_percent);
			log.info("current_porfit:{}",current_porfit);
			holdShare.put("current_porfit", current_porfit);
			log.info("floating_porfit:{}",floating_porfit);
			holdShare.put("floating_porfit", floating_porfit);
			log.info("porfit:{}",porfit);
			holdShare.put("porfit", porfit);
			log.info("asset_percent:{}",asset_percent);
			holdShare.put("asset_percent", asset_percent);
			holdShares.add(holdShare);
		}
		
		List<WebElement> elements_tb2 = driver.findElements(By.xpath("//*[@id=\"showTb2\"]/tbody/tr"));
		int size_tb2 = elements_tb2.size();
		
		String prefix_tb2 = "//*[@id=\"showTb2\"]/tbody/tr[";
		String surfix1_tb2 = "]/td[1]";
		String surfix2_tb2 = "]/td[2]";
		String surfix3_tb2 = "]/td[3]";
		String surfix4_tb2 = "]/td[4]";
		String surfix5_tb2 = "]/td[5]";
		String surfix6_tb2 = "]/td[6]";
		String surfix7_tb2 = "]/td[7]";
		String surfix8_tb2 = "]/td[8]";
		String surfix9_tb2 = "]/td[9]";
		String surfix10_tb2 = "]/td[10]";
		String surfix11_tb2 = "]/td[11]";
		String surfix12_tb2 = "]/td[12]";
		////*[@id="showTbl"]/tbody/tr[1]/td[12]
		////*[@id="showTbl"]/tbody/tr[1]/td[3]
		
		////*[@id="showTb2"]/tbody/tr/td[2]
		for(int i = 1 ;i <= size_tb2; i++ ) {

			Map<String, String> holdShare = new HashMap<String, String>();
			String code = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix3_tb2)).getText();
			String name = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix2_tb2)).getText();
			////*[@id="currency156"]/div[2]/div[1] //*[@id="currency156"]/div[2]/div[2]
			//当前市值
			String current_amount = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix4_tb2)).getText();
			//当前份额 //*[@id="showTb2"]/tbody/tr/td[7]
			String share = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix7_tb2)).getText();

			//当前净值 //*[@id="showTb2"]/tbody/tr/td[9]
			String price = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix9_tb2)).getText();
			//净值日期  //*[@id="showTbl"]/tbody/tr[1]/td[8]
			String price_date = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix8_tb2)).getText();
			//当日涨跌 //*[@id="showTb2"]/tbody/tr/td[6]
			// String current_percent = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix6_tb2)).getText();
			String current_percent = "--";
			//当日收益 //*[@id="showTbl"]/tbody/tr[1]/td[6]/span
			String current_porfit = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix6_tb2)).getText();
			//持仓收益 //*[@id="showTbl"]/tbody/tr[1]/td[5] //*[@id="showTb2"]/tbody/tr/td[5]
			String floating_porfit = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix5_tb2)).getText();
			//累计收益
			String porfit = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix5_tb2)).getText();
			//资产占比
			String asset_percent = "--";
			
			//成本价 //*[@id="showTbl"]/tbody/tr[1]/td[9]
			//Number n = NumberUtil.parseNumber(numberStr);
			BigDecimal orig_amount = NumberUtil.sub(NumberUtil.parseNumber(current_amount), NumberUtil.parseNumber(floating_porfit));
			BigDecimal orig_price = NumberUtil.round(NumberUtil.div(orig_amount, NumberUtil.parseNumber(share)), 3);
			String orig_price_str = NumberUtil.toStr(orig_price);
			log.info("=====================");
			log.info("code:{}",code);
			holdShare.put("code", code);
			log.info("name:{}",name);
			holdShare.put("name", name);
			log.info("current_amount:{}",current_amount);
			holdShare.put("current_amount", current_amount);
			log.info("share:{}",share);
			holdShare.put("share", share);
			log.info("orig_price:{}",orig_price_str);
			holdShare.put("orig_price", orig_price_str);
			log.info("price:{}",price);
			holdShare.put("price", price);
			log.info("price_date:{}",price_date);
			holdShare.put("price_date", price_date);
			log.info("current_percent:{}",current_percent);
			holdShare.put("current_percent", current_percent);
			log.info("current_porfit:{}",current_porfit);
			holdShare.put("current_porfit", current_porfit);
			log.info("floating_porfit:{}",floating_porfit);
			holdShare.put("floating_porfit", floating_porfit);
			log.info("porfit:{}",porfit);
			holdShare.put("porfit", porfit);
			log.info("asset_percent:{}",asset_percent);
			holdShare.put("asset_percent", asset_percent);
			holdShares.add(holdShare);
		}
		//2.等待30秒
		Thread.sleep(1000);
	}
	
	//https://e.jsfund.cn/query/histreq
	
	
	/**
	 * <table id="showTblreq" width="100%" border="1" cellspacing="0" cellpadding="0" class="formtable">
			<input type="hidden" id="tbsize" value="2">
			<thead>
				<tr class="formtable_title">
					    <td>申请单编号</td>
						<td>下单日期</td>
						<td>下单时间</td>
						<td>基金代码</td>
						<td>基金名称</td>
<!-- 						<td>收费方式</td> -->
						<td>业务名称</td>
						<td>申请份额</td>
						<td>申请金额</td>
<!-- 						<td>扣款状态</td> -->
						<td>确认状态</td>
						<td>详情</td>
				</tr>
			</thead>
			<tbody>
				
				        
			              
			               <tr class="trhover">
			              
			              
			            
						<td style="mso-number-format:000000;">
						<!-- 申请单编号 -->
							JS2020072300068117
						</td>
						<td><!-- 下单日期 -->
							20200723
						</td>
						<td><!-- 下单时间 -->
							
						</td>
						
						<td><!-- 基金代码 -->
							000618
						</td>
						
						<td><!-- 基金名称 -->
							嘉实薪金宝货币
						</td>
<!-- 						<td>收费方式 -->

<!-- 						</td> -->
						
						<td><!-- 业务类型-->
							定期定额基金转换
						</td>
						<td><!-- 申请份额-->
							2,500
						</td>
						
						<td><!-- 申请金额-->
							0
						</td>
<!-- 						<td> -->

<!-- 						</td> -->
						
						<td>
							确认成功
						</td>
						<td>
							<a class="redfont" onclick="queryReqDetail('JS2020072300068117')">详情</a>
							
				    		
						</td>
					</tr>
				
				        
			              
			              
			              	 <tr class="trhover tr_bgcolor">
			              
			            
						<td style="mso-number-format:000000;">
						<!-- 申请单编号 -->
							JS2020072040417858
						</td>
						<td><!-- 下单日期 -->
							20200720
						</td>
						<td><!-- 下单时间 -->
							10:22:41
						</td>
						
						<td><!-- 基金代码 -->
							000618
						</td>
						
						<td><!-- 基金名称 -->
							嘉实薪金宝货币
						</td>
<!-- 						<td>收费方式 -->

<!-- 						</td> -->
						
						<td><!-- 业务类型-->
							申购
						</td>
						<td><!-- 申请份额-->
							0
						</td>
						
						<td><!-- 申请金额-->
							40,000
						</td>
<!-- 						<td> -->

<!-- 						</td> -->
						
						<td>
							确认成功
						</td>
						<td>
							<a class="redfont" onclick="queryReqDetail('JS2020072040417858')">详情</a>
							
				    		
						</td>
					</tr>
				
				
			</tbody>
		</table>
	 * @throws Exception
	 * //*[@id="showTblconf"]/tbody/tr[1]
	 * 
	 * 
	 * <table id="showTblconf" width="100%" border="1" cellspacing="0" cellpadding="0" class="formtable">
				<input type="hidden" id="tbsize" value="4">
				<thead>
					<tr class="formtable_title">
						<td>销售机构</td>
						<td>网点</td>
						<td>确认日期</td>
						<td>基金代码</td>
						<td>基金名称</td>
						<td>业务名称</td>
						<td>确认金额</td>
						<td>确认份额</td>
						<td>支付渠道</td>
						<td>手续费</td>
						<td>净值</td>
						<td>详情</td> 
					</tr>
				</thead>
				<tbody>
					
					        
				              
				               <tr class="trhover">
				              
				              
				            
				            <td>直销中心</td>
				            <td>网上交易</td>
							<td>20200724&nbsp;&nbsp;<img class="hj_img" style="cursor: pointer;" src="https://static.jsfund.cn/etrade//static/images/jingzhi.gif" align="absmiddle" onclick="hj_img_click(this,'000751', '20200723')"></td>
							<td>000751</td>
							<td>嘉实新兴产业股票</td>
							<td>基金转换入</td>
							<td>2,500</td>
							<td>514.61</td>
							<td>招商银行62258801****4331</td>
							<td>0</td>
							<td>4.8580</td>
 							<td><a class="redfont" onclick="queryDetail_hisreq('直销中心','网上交易','20200724','000751','嘉实新兴产业股票','基金转换入','2,500','514.61','招商银行62258801****4331','0','4.8580','0','后收费')">详情</a></td> 
						    <input type="hidden" name="hisreq_agencyfee" value="0">
						    </tr>
					
					        
				              
				              
				              	 <tr class="trhover tr_bgcolor">
				              
				            
				            <td>直销中心</td>
				            <td>网上交易</td>
							<td>20200724&nbsp;&nbsp;<img class="hj_img" style="cursor: pointer;" src="https://static.jsfund.cn/etrade//static/images/jingzhi.gif" align="absmiddle" onclick="hj_img_click(this,'000618', '20200723')"></td>
							<td>000618</td>
							<td>嘉实薪金宝货币</td>
							<td>基金转换出</td>
							<td>2,500</td>
							<td>2,500</td>
							<td>招商银行62258801****4331</td>
							<td>0</td>
							<td>1.0000</td>
 							<td><a class="redfont" onclick="queryDetail_hisreq('直销中心','网上交易','20200724','000618','嘉实薪金宝货币','基金转换出','2,500','2,500','招商银行62258801****4331','0','1.0000','0','前收费')">详情</a></td> 
						    <input type="hidden" name="hisreq_agencyfee" value="0">
						    </tr>
					
					        
				              
				               <tr class="trhover">
				              
				              
				            
				            <td>直销中心</td>
				            <td>网上交易</td>
							<td>20200721&nbsp;&nbsp;<img class="hj_img" style="cursor: pointer;" src="https://static.jsfund.cn/etrade//static/images/jingzhi.gif" align="absmiddle" onclick="hj_img_click(this,'000618', '20200720')"></td>
							<td>000618</td>
							<td>嘉实薪金宝货币</td>
							<td>申购</td>
							<td>40,000</td>
							<td>40,000</td>
							<td>招商银行62258801****4331</td>
							<td></td>
							<td>1.0000</td>
 							<td><a class="redfont" onclick="queryDetail_hisreq('直销中心','网上交易','20200721','000618','嘉实薪金宝货币','申购','40,000','40,000','招商银行62258801****4331','','1.0000','','前收费')">详情</a></td> 
						    <input type="hidden" name="hisreq_agencyfee" value="">
						    </tr>
					
					        
				              
				              
				              	 <tr class="trhover tr_bgcolor">
				              
				            
				            <td>直销中心</td>
				            <td>肯特瑞代销-小金库</td>
							<td>20200720&nbsp;&nbsp;<img class="hj_img" style="cursor: pointer;" src="https://static.jsfund.cn/etrade//static/images/jingzhi.gif" align="absmiddle" onclick="hj_img_click(this,'000581', '20200720')"></td>
							<td>000581</td>
							<td>嘉实活钱包A</td>
							<td>快速取现</td>
							<td>121.92</td>
							<td>121.92</td>
							<td>京东JRD360160200073****0156</td>
							<td>0</td>
							<td>1.0000</td>
 							<td><a class="redfont" onclick="queryDetail_hisreq('直销中心','肯特瑞代销-小金库','20200720','000581','嘉实活钱包A','快速取现','121.92','121.92','京东JRD360160200073****0156','0','1.0000','0','前收费')">详情</a></td> 
						    <input type="hidden" name="hisreq_agencyfee" value="0">
						    </tr>
					
					
				</tbody>
			</table>
	 */
	@Test
	public void current_trans_detail() throws Exception{
		//0.登录
		login();
		//1.持仓列表
		driver.get("https://e.jsfund.cn/query/histconf");
		Thread.sleep(1000);
		// https://e.efunds.com.cn/transaction/records?partial&sort=&startDate=2020-07-13&endDate=2020-07-17&businType=LC&status=1&fundCode=
		
		//https://e.efunds.com.cn/transaction/statements?startDate=2020-06-18&fundAccount=&currency=
		////*[@id="order"]/div[2]/div[3]/div[2]/table/tbody
		// //*[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"showTblconf\"]/tbody/tr"));
		
		int size = elements.size();
		
		////*[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr[1]/td[1]
		
		////*[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[1]
		
		String prefix = "//*[@id=\"showTblconf\"]/tbody/tr[";
		String surfix1 = "]/td[1]";
		String surfix2 = "]/td[2]";
		String surfix3 = "]/td[3]";
		String surfix4 = "]/td[4]";
		String surfix5 = "]/td[5]";
		String surfix6 = "]/td[6]";
		String surfix7 = "]/td[7]";
		String surfix8 = "]/td[8]";
		String surfix9 = "]/td[9]";
		String surfix10 = "]/td[10]";
		String surfix11 = "]/td[11]";
		String surfix12 = "]/td[12]";
		List<Map<String, String>> transactions = new ArrayList<Map<String, String>>();
		for(int i = 1 ;i <= size; i++ ) {
			Map<String, String> holdShare = new HashMap<String, String>();
			//*[@id="showTblconf"]/tbody/tr[1]/td[4]
			String code = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix4)).getText();
			//*[@id="showTblconf"]/tbody/tr[1]/td[5]
			String name = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix5)).getText();
			//*[@id="showTblconf"]/tbody/tr[1]/td[3]
			String confirm_date = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix3)).getText(); 
			//*[@id="showTblconf"]/tbody/tr[1]/td[6]
			String trans_type =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix6)).getText();
			//*[@id="showTblconf"]/tbody/tr[1]/td[11]
			String net_price =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix11)).getText();
			//*[@id="showTblconf"]/tbody/tr[1]/td[8] //*[@id="showTblconf"]/tbody/tr[1]/td[7]
			String confirm_amount = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix7)).getText();
			//*[@id="showTblconf"]/tbody/tr[1]/td[10]
			String fee =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix10)).getText();
			String unit =  "--";
			//*[@id="showTblconf"]/tbody/tr[1]/td[1]
			String sold_org =  driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix1)).getText();
			String confirm_result =  "--";
			//*[@id="showTblconf"]/tbody/tr[1]/td[8]
			String confirm_share = driver.findElement(By.xpath(prefix+String.valueOf(i)+surfix8)).getText();;
			
		
			Map<String, String> transaction = new HashMap<String, String>();
			log.info("==============================");
			log.info("code:{}", code);
			transaction.put("code", code);
			log.info("name:{}", name);
			transaction.put("name", name);
			log.info("confirm_date:{}", confirm_date);
			transaction.put("confirm_date", confirm_date);
			log.info("trans_type:{}", trans_type);
			transaction.put("trans_type", trans_type);
			log.info("net_price:{}", net_price);
			transaction.put("net_price", net_price);
			log.info("confirm_share:{}", confirm_share);
			transaction.put("confirm_share", confirm_share);
			log.info("confirm_amount:{}", confirm_amount);
			transaction.put("confirm_amount", confirm_amount);
			log.info("fee:{}", fee);
			transaction.put("fee", fee);
			log.info("unit:{}", unit);
			transaction.put("unit", unit);
			log.info("sold_org:{}", sold_org);
			transaction.put("sold_org", sold_org);
			log.info("confirm_result:{}", confirm_result);
			transaction.put("confirm_result", confirm_result);
			transactions.add(transaction);
		}
		
		Thread.sleep(30000);
	}
	
	@Test
	public void to_number() {
		String numberStr = "46,369.27";
		Number n = NumberUtil.parseNumber(numberStr);
		System.out.println(n);
	}
	
}
