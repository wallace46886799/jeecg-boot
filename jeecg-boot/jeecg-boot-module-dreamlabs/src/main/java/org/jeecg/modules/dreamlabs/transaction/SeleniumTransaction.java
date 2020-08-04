package org.jeecg.modules.dreamlabs.transaction;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SeleniumTransaction extends AbstractTransaction {
	
	public final static int DEFAULT_WAIT_MILLIS = 1000;
	public final static int LONGER_WAIT_MILLIS = 3000;
	public final static int LONGEST_WAIT_MILLIS = 5000;
	
	public final static int IMPLICITLY_WAIT = 20;
	public final static int PAGELOAD_TIMEOUT = 20;
	/**
	 * 创建WebDriver
	 * @return
	 */
	protected WebDriver createWebDriver() {
		ChromeOptions chromeOptions = new ChromeOptions();
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(PAGELOAD_TIMEOUT,TimeUnit.SECONDS);
		return driver;
	}
	/**
	 * 销毁WebDriver
	 * @param driver
	 */
	protected void destroyWebDriver(WebDriver driver) {
		if (driver != null) {
			driver.quit();
		}
	}
	/**
	 * 登录接口
	 * @param driver
	 * @param org
	 * @param orParamsMap
	 * @param account
	 * @param accountParamsMap
	 * @throws Exception
	 */
	protected abstract void login(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception;
	/**
	 * 登出接口
	 * @param driver
	 * @param org
	 * @param orParamsMap
	 * @param account
	 * @param accountParamsMap
	 * @throws Exception
	 */
	protected abstract void logout(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception;
	/**
	 * 获取信息，大体步骤如下：<br>
	 * 1、根据编号获取基础信息<br>
	 * 2、创建浏览器<br>
	 * 3、登录<br>
	 * 4、爬取信息<br>
	 * 5、退出<br>
	 * 6、销毁浏览器<br>
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doTransaction(String id) {
		log.info("==============》同步信息开始，日期:{}，时间:{}，账户编号:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now(),id);
		DreamlabsAccount account = this.queryAccount(id);
		Map<String,String> accountParamsMap = this.queryAccountParams(account);
		DreamlabsOrg org = this.queryOrg(account.getOrgOwner()); 
		Map<String,String> orParamsMap = this.queryOrgParams(org);
		WebDriver driver = null;
		Map<String,Object> result = null;
		try {
			driver = this.createWebDriver();
			this.login(driver, org, orParamsMap, account, accountParamsMap);
			result = this.internalTransaction(driver, org, orParamsMap, account, accountParamsMap);
			this.logout(driver, org, orParamsMap, account, accountParamsMap);
		}catch(Exception e){
			log.error("《==============同步信息错误，日期:{}，时间:{}，账户编号:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now(),id);
			log.error(e.getMessage(),e);
		}finally {
			this.destroyWebDriver(driver);
		}
		log.info("《==============同步信息结束，日期:{}，时间:{}，账户编号:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now(),id);
		return result;
	}
	
	/**
	 * 判断元素是否存在
	 * @param webDriver
	 * @param by
	 * @return
	 */
	 public boolean isElementExists(WebDriver webDriver, By by) {
	        try {
	            webDriver.findElement(by);
	            return true;
	        } catch (Exception e) {
	            log.error("Element does not exists.",e);
	            return false;
	        }
	    }
	
	/**
	 * 业务交易实现
	 * @param driver
	 * @param org
	 * @param orParamsMap
	 * @param account
	 * @param accountParamsMap
	 * @return
	 * @throws Exception
	 */
	protected abstract Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception;
	

}
