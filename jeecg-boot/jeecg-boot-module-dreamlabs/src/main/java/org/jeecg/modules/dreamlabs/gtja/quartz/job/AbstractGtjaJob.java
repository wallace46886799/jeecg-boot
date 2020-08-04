package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.SeleniumTransaction;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.quartz.Job;

import cn.hutool.core.text.StrSpliter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Frank
 *
 */
@Slf4j
public abstract class AbstractGtjaJob extends SeleniumTransaction implements Job {
	
	public static final String HOST="i.gtja.com";
	
	
	protected WebDriver createWebDriver() {
    	Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "iPhone X");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		chromeOptions.addArguments("Host: i.gtja.com");
		chromeOptions.addArguments("Connection: keep-alive");
		chromeOptions.addArguments("Upgrade-Insecure-Requests: 1");
		chromeOptions.addArguments(
				"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
		chromeOptions.addArguments(
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		chromeOptions.addArguments("Sec-Fetch-Site: same-origin");
		chromeOptions.addArguments("Sec-Fetch-Mode: navigate");
		chromeOptions.addArguments("Sec-Fetch-User: ?1");
		chromeOptions.addArguments("Sec-Fetch-Dest: document");
		chromeOptions.addArguments("Accept-Encoding: gzip, deflate, br");
		chromeOptions.addArguments("Accept-Language: zh,zh-CN;q=0.9");

		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
		
		return driver;
    }
	
	protected void destroyWebDriver(WebDriver driver) {
		if(driver!=null) {
			driver.quit();
		}
	}
	
	protected void login(WebDriver driver,DreamlabsOrg org, Map<String,String> orParamsMap,DreamlabsAccount account,Map<String,String> accountParamsMap) throws Exception {
		log.info("Auto login start");
		//1.初始化
		driver.get("https://i.gtja.com/wxcos/weixin/in.html?url=https%3A%2F%2Fi.gtja.com%2Fquotes%2Fv-index.html%23%2Ftrade%3Fv%3D469%26v%3D350");
		
		//2.设置cookie		
		String cookies = accountParamsMap.getOrDefault("login.cookie", "_displaytype=.mobile; forcedisplay=.mobile; accountType=7; loginAccount=420303198209121736; loginBranch=; loginWay=quickly; suitability=0; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1594098225; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593951272,1594046079,1594085123,1594095108; _tcs=49446c09b94574c27fcf735f48f02927; _tcs-sb=5678186005745533545; _mall-suid=23ee9793bf82f12816b65ff3ad46e107; _msg-suid=90e4aff8221f7480700b608c3beb6e44; _cos-suid=27021c6c30024a0ae704041f12ad9d31; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730f222ab4ad2-0a41af2199f277-7427e73-304704-1730f222ab5ca9%22%2C%22%24device_id%22%3A%221730f222ab4ad2-0a41af2199f277-7427e73-304704-1730f222ab5ca9%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%7D; _tcs-tm=2020-07-05 18:41:59; Hm_lvt_8677059eb5096cf2cc4ba1a3476a1f6b=1593686633");
		Date expire = new Date(System.currentTimeMillis() + 10000000000L);
		List<String> cookieStrs = StrSpliter.split(cookies, "; ", true, true);
		for (String cStr : cookieStrs) {
			List<String> value = StrSpliter.split(cStr, "=", true, true);
			if ("JSESSIONID".equals(value.get(0))) {
				Cookie cookie = new Cookie(value.get(0), value.get(1), HOST, "/wxcos", expire, false, true);
				driver.manage().addCookie(cookie);
				continue;
			}
			if (value.size() == 2) {
				Cookie cookie = new Cookie(value.get(0), value.get(1), HOST, "/", expire);
				driver.manage().addCookie(cookie);
			} else {
				Cookie cookie = new Cookie(value.get(0), "", HOST, "/", expire);
				driver.manage().addCookie(cookie);
			}
		}
		
		//3.登录页面
		driver.get("https://i.gtja.com/evercos/in.html");
		Thread.sleep(LONGEST_WAIT_MILLIS);
		driver.findElement(By.id("quicklyPassword")).sendKeys(accountParamsMap.get("login.password"));
		Thread.sleep(LONGEST_WAIT_MILLIS);
		driver.findElement(By.id("quicklyLogin")).click();
		Thread.sleep(LONGEST_WAIT_MILLIS);
		//4.过渡页面
		driver.get("https://i.gtja.com/evercos/securities/index.html");
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto login done");
	}
	
	
	
	protected void logout(WebDriver driver,DreamlabsOrg org, Map<String,String> orParamsMap,DreamlabsAccount account,Map<String,String> accountParamsMap) throws Exception{
		log.info("Auto logout start");
		//持仓记录
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		//交易记录		
		driver.get("https://i.gtja.com/evercos/securities/stock/query/deal.html");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		//退出
		driver.get("https://i.gtja.com/mall/eplus/rest/logOut.json");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		//首页
		driver.manage().deleteAllCookies();
		driver.get("https://i.gtja.com");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto logout done");
	}
	
	
}
