package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.util.HashMap;

import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 爬取国泰君安的网站进行自动打新。
 * 
 * 1、迭代金融机构、迭代账户 2、查询新股 3、查询新股额度 4、进行打新 5、打新记录
 * 
 * @Author Frank
 */
@Slf4j
public class AutoBuyJob extends AbstractGtjaJob {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("==============》自动买入开始，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());
		WebDriver driver = SpringContextUtils.getBean(WebDriver.class);

		try {
			loginWithSelenium(driver,null, new HashMap(), null, new HashMap());
		} catch (Exception e) {
			log.error("Auto login failed.", e);
			return;
		}

		try {
			// 5.买入页面
			driver.get("https://i.gtja.com/evercos/securities/stock/trade/buy_index.html");
			Thread.sleep(5000);
			driver.findElement(By.id("stockCode")).sendKeys("000002");
			driver.findElement(By.id("stockCode")).sendKeys(Keys.TAB);
			Thread.sleep(5000);
			driver.findElement(By.id("entrustPrice")).sendKeys("28.650");
			driver.findElement(By.cssSelector("[class='stepper-btn refresh-price']")).click();
			Thread.sleep(5000);
			for (int i = 0; i < 100; i++) {
				driver.findElement(By.cssSelector("[class='stepper-btn before']")).click();
			}
			driver.findElement(By.id("entrustPrice")).sendKeys(Keys.TAB);
			driver.findElement(By.id("entrustAmount")).sendKeys("100");
			Thread.sleep(5000);
			driver.findElement(By.id("submitBuy")).click();
			Thread.sleep(5000);

			// 6.发起买入请求
			((JavascriptExecutor) driver).executeScript("$(\"#stockTrading\").ajaxSubmit({\n"
					+ "			url : '/evercos/securities/stock/trade/trading.json',\n"
					+ "			type : \"post\",\n" + "			dataType : \"json\",\n" + "			data : {\n"
					+ "				\"stockAccount\" : '0106044666',\n"
					+ "				\"fullAccountNo\" : '0311040018017037',\n" + "				\"entrustProp\" :'U'\n"
					+ "			},\n" + "			beforeSend : function(){\n"
					+ "				console.log(\"before\")\n" + "			},\n"
					+ "			complete : function(){\n" + "				console.log(\"complete\")\n"
					+ "			},\n" + "			success : function(data) {\n" + "				console.log(data)\n"
					+ "			}\n" + "})");

			Thread.sleep(10000);

			// 7.结束打印日志
			driver.get("https://i.gtja.com/mall/eplus/rest/logOut.json");

		} catch (Exception e) {
			log.error("Auto transaction failed.", e);
		}

		log.info("《==============自动买入结束，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());

	}

}
