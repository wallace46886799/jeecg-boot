package org.jeecg.modules.dreamlabs.gtja.sprider;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.sprider.Spider;
import org.jeecg.modules.dreamlabs.transaction.SeleniumTransaction;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import cn.hutool.core.text.StrSpliter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGtjaSpider extends SeleniumTransaction implements Spider {

	public static final String HOST = "i.gtja.com";

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
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

		return driver;
	}

	protected void login(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account,
			Map<String, String> accountParamsMap) throws Exception {

		// 1.初始化
		driver.get(
				"https://i.gtja.com/wxcos/weixin/in.html?url=https%3A%2F%2Fi.gtja.com%2Fquotes%2Fv-index.html%23%2Ftrade%3Fv%3D469%26v%3D350");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		// 2.设置cookie
		String cookies = accountParamsMap.get("login.cookie");
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

		// 3.登录页面
		driver.get("https://i.gtja.com/evercos/in.html");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("quicklyPassword")).sendKeys(accountParamsMap.get("login.password"));
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("quicklyLogin")).click();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		// 4.过渡页面
		// driver.get("https://i.gtja.com/evercos/securities/index.html");
		// Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto login done");
	}

	protected void logout(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account,
			Map<String, String> accountParamsMap) throws Exception {
		// 退出
		driver.get("https://i.gtja.com/mall/eplus/rest/logOut.json");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		// 首页
		driver.manage().deleteAllCookies();
		driver.get("https://i.gtja.com");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto logout done");
	}
	
	
	@Override
	public Map<String, Object> process(String id) throws Exception {
		return doTransaction(id);
	}

}
