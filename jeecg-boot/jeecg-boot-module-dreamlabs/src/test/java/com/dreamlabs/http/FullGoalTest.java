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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.mitm.TrustSource;
import net.lightbody.bmp.proxy.CaptureType;

@Slf4j
public class FullGoalTest extends BaseTest {

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
		ChromeOptions chromeOptions = new ChromeOptions();

		if (isProxy) {
			driver = getWebDriverWithProxy(chromeOptions);
		} else {
			driver = getWebDriver(chromeOptions);
		}
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

	}

	private WebDriver getWebDriver(ChromeOptions chromeOptions) {
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	private WebDriver getWebDriverWithProxy(ChromeOptions chromeOptions) {

		proxy = new BrowserMobProxyServer();

		if (isCustomSSL) {
			// your root CA certificate(s) may be in a Java KeyStore, a PEM-encoded File or
			// String, or an X509Certificate
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
		// enable more detailed HAR capture, if desired (see CaptureType for the
		// complete list)
		proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

		List<String> whitelist = new ArrayList<String>();
		whitelist.add("https?://i\\\\.gtja\\\\.com/.*.json");
		proxy.whitelistRequests(whitelist, 200);
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
		driver.get("https://etrading.fullgoal.com.cn/etrading?_=" + System.currentTimeMillis());

		if (proxy != null) {
			proxy.newHar("i.gtja.com");
		}

		Thread.sleep(3000);
		driver.findElement(By.id("lognumberMode2")).sendKeys("420303198209121736");
		Thread.sleep(3000);
		driver.findElement(By.id("passwordMode2")).sendKeys(super.decrypt1Password());
		Thread.sleep(3000);
		//// *[@id="container"]/div/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[2]/form/div[3]/div/div/button
		driver.findElement(By.id("loginForm_submit")).click();
		Thread.sleep(3000);

		//

		driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[1]/a")).click();

		Thread.sleep(3000);

		// get the HAR data
		if (proxy != null) {
			Har har = proxy.getHar();
			log.info("Har is {}.", har);

			File harFile = new File("/Users/Frank/Temp/browsermob/hari.gtja.com.json");
			har.writeTo(harFile);
		}
	}

	private void logout() throws Exception {
		//// *[@id="container"]/div/div/div[2]/div[1]/div/div[1]/a/span
		driver.findElement(By.id("logoutEtradingBt")).click();
		Thread.sleep(3000);
	}

	@Test
	public void login_request() throws Exception {
		login();
		logout();
	}

	/**
	 * <table width="100%">
	 * <tbody>
	 * <tr class="bg-blueef mt20 gray2">
	 * <th class="tal gray7" style="width:25%;"><span class="gray1
	 * fz-14">富国中国中小盘混合（QDII）</span>(100061)
	 * <p>
	 * 分红方式：现金红利<a href=
	 * "/etrading/trade/bonus/list?initPage=1&amp;menuId=30005&amp;SECURE_TOKEN=TBO3L1BCnauocX3ESut0iE6n4mk7zhVOkYCgpzf7EU"
	 * class="blue" shape="rect">［修改］</a>
	 * </p>
	 * </th>
	 * <th class="tar" style="width:15%;">份额<br clear="none">
	 * <span class="fz-18">16,776.99</span>&nbsp;份</th>
	 * <th class="tar" style="width:15%;">市值<br>
	 * <span class="fz-18 orange">41,741.15</span>&nbsp;元</th>
	 * <th class="tar" style="width:15%;">盈亏<br clear="none">
	 * <span class="fz-18 orange">1,741.15</span>&nbsp;元</th>
	 * <th class="tar" style="width:20%;">最新净值: (20200723)
	 * <p class="fz-18 tac ml30">
	 * 2.4880
	 * </p>
	 * </th>
	 * <th class="op"><a class="my_arrow arrow_xia"><i></i></a></th>
	 * </tr>
	 * <tr class="bg-blueef" id="custRoad" style="display: none;">
	 * <th colspan="6" style="padding: 0px;"><div class="arfdiv"><a class="current"
	 * id="paytag_100061" href="javascript:;">支付渠道</a></div></th>
	 * </tr>
	 * <tr class="" style="display: none;">
	 * <th class="bankacco">支付渠道</th>
	 * <th class="usableremainshare">持有份额</th>
	 * <th class="marketvalue">当前市值</th>
	 * <th class="totalincome">累计盈亏</th>
	 * <th class="sharetype">份额类别</th>
	 * <th class="op">操作</th>
	 * </tr>
	 * <tr class="boxshadow bg-yellow" style="display: none;">
	 * <td class="bankacco">招商银行(直联代扣) ****4331</td>
	 * <td class="usableremainshare">16,776.99 份</td>
	 * <td class="marketvalue">41,741.15 元</td>
	 * <td class="totalincome">1,741.15 元</td>
	 * <td class="sharetype">前收费</td>
	 * <td class="op"><a href=
	 * "/etrading/trade/declare/init?menuId=30000&amp;initPage=1&amp;sharetype=A&amp;source=30001&amp;fundcode=100061&amp;pernetvalue=2.4880&amp;SECURE_TOKEN=TBO3L1BCnauocX3ESut0iE6n4mk7zhVOkYCgpzf7EU"
	 * class="blue" shape="rect">追加购买</a> <a href=
	 * "/etrading/trade/redeem/init?initPage=1&amp;menuId=30002&amp;shareType=A&amp;source=30001&amp;capitalmode=G&amp;tradeacco=02587813&amp;bankSerial=021&amp;fundcode=100061&amp;sharetype=A&amp;usableremainshare=16776.99&amp;SECURE_TOKEN=TBO3L1BCnauocX3ESut0iE6n4mk7zhVOkYCgpzf7EU"
	 * class="blue" shape="rect">赎回</a> <a href=
	 * "/etrading/trade/trans/list?menuId=30003&amp;shareType=A&amp;initPage=1&amp;SECURE_TOKEN=TBO3L1BCnauocX3ESut0iE6n4mk7zhVOkYCgpzf7EU"
	 * class="blue" shape="rect">转换</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @throws Exception
	 */
	@Test
	public void holding_shares() throws Exception {
		// 0.登录
		login();
		driver.findElement(By.id("fundZcClickFlg")).click();
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]
		Thread.sleep(5000);

		// *[@id="myfunddetail"]/div[1]/div[1]
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"myfunddetail\"]/div[1]/div"));
		int size = elements.size();
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[3]/span
		//// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[1]/span
		String prefix = "//*[@id=\"myfunddetail\"]/div[1]/div[";
		// 名称、编码
		String surfix1 = "]/table/tbody/tr[1]/th[1]/span";
		// 份额
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[2]/span
		String surfix2 = "]/table/tbody/tr[1]/th[2]/span";
		// 市值
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[3]/span
		String surfix3 = "]/table/tbody/tr[1]/th[3]/span";
		// 盈亏
		String surfix4 = "]/table/tbody/tr[1]/th[4]/span";
		// 净值
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[5]/p
		String surfix5 = "]/table/tbody/tr[1]/th[5]/p";

		List<Map<String, String>> holdShares = new ArrayList<Map<String, String>>();
		for (int i = 1; i <= size; i++) {

			Map<String, String> holdShare = new HashMap<String, String>();
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			//// *[@id="currency156"]/div[2]/div[1] //*[@id="currency156"]/div[2]/div[2]
			// 当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			current_amount = StrUtil.removeAll(current_amount, ',');
			// 当前份额
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			share = StrUtil.removeAll(share, ',');
			// 成本价 //*[@id="showTbl"]/tbody/tr[1]/td[9]
			String orig_price = "--";
			// 当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			price = StrUtil.removeAll(price, ',');
			// 净值日期 //*[@id="showTbl"]/tbody/tr[1]/td[8]
			String price_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			// 当日涨跌
			String current_percent = "--";
			// 当日收益 //*[@id="showTbl"]/tbody/tr[1]/td[6]/span
			String current_porfit = "--";
			// 持仓收益 //*[@id="showTbl"]/tbody/tr[1]/td[5]
			String floating_porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			floating_porfit = StrUtil.removeAll(floating_porfit, ',');
			// 累计收益
			String porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			porfit = StrUtil.removeAll(porfit, ',');
			// 资产占比
			String asset_percent = "--";

			// 成本价 //*[@id="showTbl"]/tbody/tr[1]/td[9]
			// Number n = NumberUtil.parseNumber(numberStr);
			BigDecimal orig_amount = NumberUtil.sub(NumberUtil.parseNumber(current_amount),
					NumberUtil.parseNumber(floating_porfit));
			BigDecimal orig_price_bd = NumberUtil.round(NumberUtil.div(orig_amount, NumberUtil.parseNumber(share)), 3);
			orig_price = NumberUtil.toStr(orig_price_bd);

			log.info("=====================");
			log.info("code:{}", code);
			holdShare.put("code", code);
			log.info("name:{}", name);
			holdShare.put("name", name);
			log.info("current_amount:{}", current_amount);
			holdShare.put("current_amount", current_amount);
			log.info("share:{}", share);
			holdShare.put("share", share);
			log.info("orig_price:{}", orig_price);
			holdShare.put("orig_price", orig_price);
			log.info("price:{}", price);
			holdShare.put("price", price);
			log.info("price_date:{}", price_date);
			holdShare.put("price_date", price_date);
			log.info("current_percent:{}", current_percent);
			holdShare.put("current_percent", current_percent);
			log.info("current_porfit:{}", current_porfit);
			holdShare.put("current_porfit", current_porfit);
			log.info("floating_porfit:{}", floating_porfit);
			holdShare.put("floating_porfit", floating_porfit);
			log.info("porfit:{}", porfit);
			holdShare.put("porfit", porfit);
			log.info("asset_percent:{}", asset_percent);
			holdShare.put("asset_percent", asset_percent);
			holdShares.add(holdShare);
		}

		// 2.等待30秒
		Thread.sleep(1000);
	}

	/**
	 * <table id="view_config_view_account_tradeapply_list" width="100%" class=
	 * "t_list config_view_account_tradeapply_list" border="0" cellspacing="1"
	 * cellpadding="0">
	 * <tbody>
	 * <tr class="">
	 * <th class="fundname">基金名称</th>
	 * <th class="oriapplydate">交易日期</th>
	 * <th class="businflagstr">业务名称</th>
	 * <th class="apply">申请份额/金额</th>
	 * <th class="confirmnetvalue">确认净值</th>
	 * <th class="confirmapply">确认份额/金额</th>
	 * <th class="fees">手续费</th>
	 * <th class="bankname">关联银行卡</th>
	 * <th class="confirmflag">交易状态</th>
	 * <th class="detail">操作</th>
	 * </tr>
	 * <tr class="">
	 * <td class="fundname" style="text-align: center;">
	 * <p class="inlineblock vam">
	 * <span class="gray7 fz-12">富国富钱包</span><br>
	 * <b class="fz-14">富国天惠成长混合(LOF)</b>
	 * </p>
	 * <i class="icon i_32 i32_zuanr vam"></i></td>
	 * <td class="oriapplydate" style="text-align: center;">2020-07-24 09:42:35</td>
	 * <td class="businflagstr" style="text-align: center;">目标盈定投</td>
	 * <td class="apply" style="text-align: center;">¥2,500.00</td>
	 * <td class="confirmnetvalue" style="text-align: center;">--</td>
	 * <td class="confirmapply" style="text-align: center;">--</td>
	 * <td class="fees" style="text-align: center;">--</td>
	 * <td class="bankname" style="text-align: center;">招商银行(直联代扣)<br>
	 * </td>
	 * <td class="confirmflag" style="text-align: center;">已受理</td>
	 * <td class="detail" style="text-align: center;"><a id="detail_000638" style=
	 * "color:#0c50a3" herf="#" name=
	 * "fundcode=000638&amp;agencytype=D&amp;acceptdate=2020-07-24
	 * 09:42:35&amp;acceptdatebig=2020-07-24 09:42:35&amp;identity=0" callingcode=
	 * "024">详情</a></td>
	 * </tr>
	 * <tr class="bg-gray">
	 * <td class="fundname" style="text-align: center;">
	 * <p class="inlineblock vam">
	 * <span class="gray7 fz-12">富国富钱包</span><br>
	 * <b class="fz-14">富国天惠成长混合(LOF)</b>
	 * </p>
	 * <i class="icon i_32 i32_zuanr vam"></i></td>
	 * <td class="oriapplydate" style="text-align: center;">2020-07-24 09:39:58</td>
	 * <td class="businflagstr" style="text-align: center;">目标盈定投</td>
	 * <td class="apply" style="text-align: center;">¥5,000.00</td>
	 * <td class="confirmnetvalue" style="text-align: center;">--</td>
	 * <td class="confirmapply" style="text-align: center;">--</td>
	 * <td class="fees" style="text-align: center;">--</td>
	 * <td class="bankname" style="text-align: center;">招商银行(直联代扣)<br>
	 * </td>
	 * <td class="confirmflag" style="text-align: center;">已受理</td>
	 * <td class="detail" style="text-align: center;"><a id="detail_000638" style=
	 * "color:#0c50a3" herf="#" name=
	 * "fundcode=000638&amp;agencytype=D&amp;acceptdate=2020-07-24
	 * 09:39:58&amp;acceptdatebig=2020-07-24 09:39:58&amp;identity=1" callingcode=
	 * "024">详情</a></td>
	 * </tr>
	 * <tr class="">
	 * <td class="fundname" style="text-align: center;">
	 * <p class="inlineblock vam">
	 * <span class="gray7 fz-12">富国富钱包</span><br>
	 * <b class="fz-14">富国天惠成长混合(LOF)</b>
	 * </p>
	 * <i class="icon i_32 i32_zuanr vam"></i></td>
	 * <td class="oriapplydate" style="text-align: center;">2020-07-24 09:37:39</td>
	 * <td class="businflagstr" style="text-align: center;">目标盈定投</td>
	 * <td class="apply" style="text-align: center;">¥2,500.00</td>
	 * <td class="confirmnetvalue" style="text-align: center;">--</td>
	 * <td class="confirmapply" style="text-align: center;">--</td>
	 * <td class="fees" style="text-align: center;">--</td>
	 * <td class="bankname" style="text-align: center;">招商银行(直联代扣)<br>
	 * </td>
	 * <td class="confirmflag" style="text-align: center;">已受理</td>
	 * <td class="detail" style="text-align: center;"><a id="detail_000638" style=
	 * "color:#0c50a3" herf="#" name=
	 * "fundcode=000638&amp;agencytype=D&amp;acceptdate=2020-07-24
	 * 09:37:39&amp;acceptdatebig=2020-07-24 09:37:39&amp;identity=2" callingcode=
	 * "024">详情</a></td>
	 * </tr>
	 * <tr class="bg-gray">
	 * <td class="fundname" style="text-align: center;">
	 * <p class="inlineblock vam">
	 * <span class="gray7 fz-12">富国富钱包</span><br>
	 * <b class="fz-14">富国中国中小盘混合（QDII）</b>
	 * </p>
	 * <i class="icon i_32 i32_zuanr vam"></i></td>
	 * <td class="oriapplydate" style="text-align: center;">2020-07-20 10:01:31</td>
	 * <td class="businflagstr" style="text-align: center;">经典定投</td>
	 * <td class="apply" style="text-align: center;">¥10,000.00</td>
	 * <td class="confirmnetvalue" style="text-align: center;">2.3870</td>
	 * <td class="confirmapply" style="text-align: center;">4188.73份</td>
	 * <td class="fees" style="text-align: center;">¥1.50</td>
	 * <td class="bankname" style="text-align: center;">招商银行(直联代扣)<br>
	 * </td>
	 * <td class="confirmflag" style="text-align: center;">确认成功</td>
	 * <td class="detail" style="text-align: center;"><a id="detail_000638" style=
	 * "color:#0c50a3" herf="#" name=
	 * "fundcode=000638&amp;agencytype=D&amp;acceptdate=2020-07-20
	 * 10:01:31&amp;acceptdatebig=2020-07-20 10:01:31&amp;identity=3" callingcode=
	 * "024">详情</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @throws Exception
	 */
	@Test
	public void current_trans_detail() throws Exception {
		// 0.登录
		login();

		// /html/body/div[2]/div/ul/li[5]/a
		// 1.持仓列表
		driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[5]/a")).click();
		Thread.sleep(1000);
		// //*[@id="60"]/li[3]/a
		driver.findElement(By.xpath("//*[@id=\"60\"]/li[3]/a")).click();
		Thread.sleep(1000);
		// https://e.efunds.com.cn/transaction/records?partial&sort=&startDate=2020-07-13&endDate=2020-07-17&businType=LC&status=1&fundCode=
		// *[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]
		List<WebElement> elements = driver
				.findElements(By.xpath("//*[@id=\"view_config_view_account_tradeapply_list\"]/tbody/tr"));
		int size = elements.size();

		String prefix = "//*[@id=\"view_config_view_account_tradeapply_list\"]/tbody/tr[";
		// 基金名称
		// //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[1]/p/b
		String surfix1 = "]/td[1]/p/b";
		// 交易日期 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[2]
		String surfix2 = "]/td[2]";
		// 业务类型 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[3]
		String surfix3 = "]/td[3]";
		// 确认净值 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[4]
		String surfix4 = "]/td[4]";
		// 确认份额 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[5]
		String surfix5 = "]/td[5]";
		String surfix6 = "]/td[6]";
		// 手续费 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[6]
		String surfix7 = "]/td[7]";

		
		// 交易状态 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[8]
		String surfix9 = "]/td[9]";
		List<Map<String, String>> transactions = new ArrayList<Map<String, String>>();
		for (int i = 2; i <= size; i++) {
			// *[@id="showTblconf"]/tbody/tr[1]/td[4]
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[5]
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[3]
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			confirm_date = DateUtil.parse(confirm_date).toDateStr();
			// *[@id="showTblconf"]/tbody/tr[1]/td[6]
			String trans_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[11]
			String net_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[8]
			// //*[@id="showTblconf"]/tbody/tr[1]/td[7]
			String confirm_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			confirm_amount = StrUtil.removePrefix(confirm_amount, "¥");
			confirm_amount = StrUtil.removeAll(confirm_amount, ",");
			// *[@id="showTblconf"]/tbody/tr[1]/td[10]
			String fee = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			fee = StrUtil.removePrefix(fee, "¥");
			fee = StrUtil.removeAll(fee, ",");
			String unit = "--";
			// *[@id="showTblconf"]/tbody/tr[1]/td[1]
			String sold_org = "--";
			String confirm_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			confirm_share = StrUtil.removeSuffix(confirm_share, "份");
			String confirm_result = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix9)).getText();
			

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

		Thread.sleep(10000);
	}

}
