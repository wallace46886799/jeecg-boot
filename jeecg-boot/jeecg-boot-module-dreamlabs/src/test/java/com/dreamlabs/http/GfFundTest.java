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
public class GfFundTest extends BaseTest {

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
		driver.get("https://trade.gffunds.com.cn/login?_=" + System.currentTimeMillis());

		if (proxy != null) {
			proxy.newHar("i.gtja.com");
		}

		Thread.sleep(3000);
		driver.findElement(By.id("userName")).sendKeys("420303198209121736");
		Thread.sleep(3000);
		driver.findElement(By.id("password")).sendKeys(super.decryptzPassword());
		Thread.sleep(3000);
		//// *[@id="container"]/div/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[2]/form/div[3]/div/div/button
		driver.findElement(By.xpath(
				"//*[@id=\"container\"]/div/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[2]/form/div[3]/div/div/button"))
				.click();
		Thread.sleep(3000);

		driver.get("https://trade.gffunds.com.cn/account/my-asset");

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
		driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div[2]/div[1]/div/div[1]/a/span")).click();
		Thread.sleep(3000);
	}

	@Test
	public void login_request() throws Exception {
		login();
		logout();
	}

	/**
	 * <tbody class="ant-table-tbody" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2">
	 * <tr class="ant-table-row ant-table-row-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0">
	 * <td class="text-left" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name"><span style="padding-left:0px;"
	 * class="ant-table-row-indent indent-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.0"></span><noscript data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.1"></noscript><div data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.2"><span style=
	 * "vertical:align-middle;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.2.1"><span class="con-middle
	 * text-mormal" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.2.1.0"><a class="a-hover-orange
	 * light-black" href="/fund/my-fund/detail?fundCode=000118" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.2.1.0.1">广发聚鑫债券A</a></span><span
	 * class="gray con-middle text-small" style="margin-left:10px;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$name.2.1.2">000118</span></span></div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$agencyKey"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$agencyKey.2">国泰君安</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$dividendMethod"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$dividendMethod.2">红利再投资</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$shareType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$shareType.2">前收费</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$navProfitInfo"><span value="1.506000"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$navProfitInfo.2">1.5060</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$totalShare"><span value=
	 * "84921.470000" type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$totalShare.2">84,921.47</span></td>
	 * <td class=" ant-table-column-sort" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$totalVal"><span value="127891.733820"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$totalVal.2">127,891.73</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$noIncome"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$noIncome.2">--</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$moneyType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$0.$moneyType.2">--</div></td>
	 * </tr>
	 * <tr class="ant-table-row ant-table-row-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1">
	 * <td class="text-left" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name"><span style="padding-left:0px;"
	 * class="ant-table-row-indent indent-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.0"></span><noscript data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.1"></noscript><div data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.2"><span style=
	 * "vertical:align-middle;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.2.1"><span class="con-middle
	 * text-mormal" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.2.1.0"><a class="a-hover-orange
	 * light-black" href="/fund/my-fund/detail?fundCode=004851" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.2.1.0.1">广发医疗保健股票A</a></span><span
	 * class="gray con-middle text-small" style="margin-left:10px;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$name.2.1.2">004851</span></span></div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$agencyKey"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$agencyKey.2">广发基金（招商银行8177）</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$dividendMethod"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$dividendMethod.2">红利再投资</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$shareType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$shareType.2">前收费</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$navProfitInfo"><span value="3.084400"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$navProfitInfo.2">3.0844</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$totalShare"><span value=
	 * "31223.220000" type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$totalShare.2">31,223.22</span></td>
	 * <td class=" ant-table-column-sort" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$totalVal"><span value="96304.900000"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$totalVal.2">96,304.90</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$noIncome"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$noIncome.2">--</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$moneyType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$1.$moneyType.2">--</div></td>
	 * </tr>
	 * <tr class="ant-table-row ant-table-row-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2">
	 * <td class="text-left" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name"><span style="padding-left:0px;"
	 * class="ant-table-row-indent indent-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.0"></span><noscript data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.1"></noscript><div data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.2"><span style=
	 * "vertical:align-middle;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.2.1"><span class="con-middle
	 * text-mormal" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.2.1.0"><a class="a-hover-orange
	 * light-black" href="/fund/my-fund/detail?fundCode=000119" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.2.1.0.1">广发聚鑫债券C</a></span><span
	 * class="gray con-middle text-small" style="margin-left:10px;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$name.2.1.2">000119</span></span></div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$agencyKey"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$agencyKey.2">国泰君安</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$dividendMethod"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$dividendMethod.2">现金分红</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$shareType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$shareType.2">前收费</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$navProfitInfo"><span value="1.502000"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$navProfitInfo.2">1.5020</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$totalShare"><span value=
	 * "31492.620000" type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$totalShare.2">31,492.62</span></td>
	 * <td class=" ant-table-column-sort" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$totalVal"><span value="47301.915240"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$totalVal.2">47,301.92</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$noIncome"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$noIncome.2">--</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$moneyType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$2.$moneyType.2">--</div></td>
	 * </tr>
	 * <tr class="ant-table-row ant-table-row-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3">
	 * <td class="text-left" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name"><span style="padding-left:0px;"
	 * class="ant-table-row-indent indent-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.0"></span><noscript data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.1"></noscript><div data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.2"><span style=
	 * "vertical:align-middle;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.2.1"><span class="con-middle
	 * text-mormal" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.2.1.0"><a class="a-hover-orange
	 * light-black" href="/fund/my-fund/detail?fundCode=006479" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.2.1.0.1">广发纳斯达克100C人民币(QDII)</a></span><br
	 * data-reactid=".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.2.1.1">
	 * <span class="gray con-middle text-small" style="margin-left: 0px;"
	 * data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$name.2.1.2">006479</span></span></div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$agencyKey"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$agencyKey.2">广发基金（招商银行8177）</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$dividendMethod"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$dividendMethod.2">红利再投资</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$shareType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$shareType.2">前收费</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$navProfitInfo"><span value="3.242700"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$navProfitInfo.2">3.2427</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$totalShare"><span value=
	 * "13081.940000" type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$totalShare.2">13,081.94</span></td>
	 * <td class=" ant-table-column-sort" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$totalVal"><span value="46420.810000"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$totalVal.2">46,420.81</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$noIncome"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$noIncome.2">--</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$moneyType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$3.$moneyType.2">--</div></td>
	 * </tr>
	 * <tr class="ant-table-row ant-table-row-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4">
	 * <td class="text-left" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name"><span style="padding-left:0px;"
	 * class="ant-table-row-indent indent-level-0" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.0"></span><noscript data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.1"></noscript><div data-reactid
	 * =".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.2"><span style=
	 * "vertical:align-middle;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.2.1"><span class="con-middle
	 * text-mormal" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.2.1.0"><a class="a-hover-orange
	 * light-black" href="/wallet/my-wallet" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.2.1.0.4">广发钱袋子货币A</a></span><span
	 * class="gray con-middle text-small" style="margin-left:10px;" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$name.2.1.2">000509</span></span></div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$agencyKey"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$agencyKey.2">广发基金（招商银行8177）</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$dividendMethod"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$dividendMethod.2">红利再投资</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$shareType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$shareType.2">前收费</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$navProfitInfo"><span value="1.000000"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$navProfitInfo.2">1.0000</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$totalShare"><span value=
	 * "38797.380000" type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$totalShare.2">38,797.38</span></td>
	 * <td class=" ant-table-column-sort" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$totalVal"><span value="38797.380000"
	 * type="amount" class="text-x2l" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$totalVal.2">38,797.38</span></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$noIncome"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$noIncome.2">--</div></td>
	 * <td class="" data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$moneyType"><div data-reactid=
	 * ".0.0.2.1.0.1.0.0.0.1.0.1.1.0.1.0.2.$4.$moneyType.2">--</div></td>
	 * </tr>
	 * </tbody>
	 * 
	 * @throws Exception
	 */
	@Test
	public void holding_shares() throws Exception {

		// 0.登录
		login();
		driver.get("https://trade.gffunds.com.cn/account/income/holding-profit");
		Thread.sleep(1000);
		
		List<WebElement> elements = driver.findElements(By.xpath(
				"//*[@id=\"container\"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr"));
		int size = elements.size();
		
		String prefix = "//*[@id=\"container\"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[";
		// 名称
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[1]/div/span/span[1]/a
		String surfix1 = "]/td[1]/span[2]/span[1]/a";
		// 编码
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[1]/span[2]/span[2]
		String surfix2 = "]/td[1]/span[2]/span[2]";
		// 持仓收益
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[2]/span
		String surfix3 = "]/td[2]/span";
		// 持仓收益率
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[3]/span
		String surfix4 = "]/td[3]/span";
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[4]/span
		// 成本价格
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[5]
		String surfix5 = "]/td[4]/span";
		// 最近净值
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[6]/span
		String surfix6 = "]/td[5]/span";
		// 持仓成本
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[7]/span
		String surfix7 = "]/td[6]/span";
		// 资产
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[7]/span
		String surfix8 = "]/td[7]/span";
		
		List<Map<String, String>> holdShares = new ArrayList<Map<String, String>>();
		for (int i = 1; i <= size; i++) {
			Map<String, String> holdShare = new HashMap<String, String>();
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// 当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			current_amount = StrUtil.removeAll(current_amount, ',');
			// 当前份额
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			share = StrUtil.removeAll(share, ',');
			// 成本价
			String orig_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();;
			// 当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			price = StrUtil.removeAll(price, ',');
			// 净值日期
			String price_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			// 当日涨跌
			String current_percent = "--";
			// 当日收益
			String current_porfit = "--";
			// 持仓收益
			String floating_porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			floating_porfit = StrUtil.removeAll(floating_porfit, ',');
			// 累计收益
			String porfit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			porfit = StrUtil.removeAll(porfit, ',');
			// 资产占比
			String asset_percent = "--";

			// 份额
			BigDecimal share_bd = NumberUtil.div(NumberUtil.parseNumber(current_amount), NumberUtil.parseNumber(price));
			share = NumberUtil.roundStr(share_bd.toPlainString(),2); 

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

	@Test
	public void current_trans_detail() throws Exception {
		// 0.登录
		login();

		// /html/body/div[2]/div/ul/li[5]/a
		// 1.持仓列表
		driver.get("https://trade.gffunds.com.cn/record/trade-record/direct-sale");
		Thread.sleep(1000);
		
		//*[@id="container"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[1]/span[2]
		List<WebElement> elements = driver
				.findElements(By.xpath("//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr"));
		int size = elements.size();

		String prefix = "//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[";
		//下单时间
		//*[@id="container"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[1]/span[2]
		String surfix1 = "]/td[1]/span[2]";
		//基金名称
		//*[@id="container"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[2]/span
		String surfix2 = "]/td[2]/span";
		//业务类型
		//*[@id="container"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[3]/span
		String surfix3 = "]/td[3]/span";
		// 交易数额
		//*[@id="container"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[4]/span
		String surfix4 = "]/td[4]/span";
		// 交易账户
		String surfix5 = "]/td[5]/span";
		// 交易状态
		//*[@id="container"]/div/div/div[3]/div[2]/div/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[6]/span
		String surfix6 = "]/td[6]/span";
		// 手续费 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[6]
		String surfix7 = "]/td[7]/span";

		
		// 交易状态 //*[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]/td[8]
		String surfix9 = "]/td[9]";
		List<Map<String, String>> transactions = new ArrayList<Map<String, String>>();
		for (int i = 2; i <= size; i++) {
			// *[@id="showTblconf"]/tbody/tr[1]/td[4]
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[5]
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[3]
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			confirm_date = DateUtil.parse(confirm_date).toDateStr();
			// *[@id="showTblconf"]/tbody/tr[1]/td[6]
			String trans_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[11]
			String net_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[8]
			// //*[@id="showTblconf"]/tbody/tr[1]/td[7]
			String confirm_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			confirm_amount = StrUtil.removePrefix(confirm_amount, "¥");
			confirm_amount = StrUtil.removeAll(confirm_amount, ",");
			// *[@id="showTblconf"]/tbody/tr[1]/td[10]
			String fee = "--";
			fee = StrUtil.removePrefix(fee, "¥");
			fee = StrUtil.removeAll(fee, ",");
			String unit = "--";
			// *[@id="showTblconf"]/tbody/tr[1]/td[1]
			String sold_org = "--";
			String confirm_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			confirm_share = StrUtil.removeSuffix(confirm_share, "份");
			String confirm_result = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			

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
	
	
	//https://trade.gffunds.com.cn/record/bill-detail
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[1]/div[2]/button[1]/span
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[1]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[2]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[3]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[4]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[5]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[6]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[7]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[8]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[9]
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div[10]
	
	
	//*[@id="container"]/div/div/div[3]/div[2]/div/div[1]/div[2]/button[1]
	@Test
	public void current_trans_detail_bill() throws Exception {

		// 0.登录
		login();

		// /html/body/div[2]/div/ul/li[5]/a
		// 1.持仓列表
		driver.get("https://trade.gffunds.com.cn/record/bill-detail");
		Thread.sleep(1000);
		
		
		List<WebElement> elements = driver
				.findElements(By.xpath("//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div"));
		int size = elements.size();
		log.info("Size:{}",size);
		String prefix = "//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[";
		//确认日期
		String surfix1 = "]/div[1]";
		//业务类型
		String surfix2 = "]/div[2]";
		//产品名称
		String surfix3 = "]/div[3]";
		//确认份额
		String surfix4 = "]/div[4]";
		//确认金额
		String surfix5 = "]/div[5]";
		//币种
		String surfix6 = "]/div[6]";
		//成交净值
		String surfix7 = "]/div[7]";
		//手续费
		String surfix8 = "]/div[8]";
		//收费方式
		String surfix9 = "]/div[9]";
		//销售机构
		String surfix10 = "]/div[10]";
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[202]/div[1]
		List<Map<String, String>> transactions = new ArrayList<Map<String, String>>();
		for (int i = size ; i >= size - 50; i--) {
			// *[@id="showTblconf"]/tbody/tr[1]/td[4]
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[5]
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[3]
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			confirm_date = DateUtil.parse(confirm_date).toDateStr();
			// *[@id="showTblconf"]/tbody/tr[1]/td[6]
			String trans_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[11]
			String net_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[8]
			// //*[@id="showTblconf"]/tbody/tr[1]/td[7]
			String confirm_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			confirm_amount = StrUtil.removePrefix(confirm_amount, "¥");
			confirm_amount = StrUtil.removeAll(confirm_amount, ",");
			// *[@id="showTblconf"]/tbody/tr[1]/td[10]
			String fee = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			fee = StrUtil.removePrefix(fee, "¥");
			fee = StrUtil.removeAll(fee, ",");
			String unit = "--";
			// *[@id="showTblconf"]/tbody/tr[1]/td[1]
			String sold_org = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix10)).getText();
			String confirm_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			confirm_share = StrUtil.removeSuffix(confirm_share, "份");
			String confirm_result = "--";
			

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
