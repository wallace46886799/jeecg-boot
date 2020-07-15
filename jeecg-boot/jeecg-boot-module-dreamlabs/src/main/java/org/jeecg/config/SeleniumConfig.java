package org.jeecg.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bonigarcia.wdm.WebDriverManager;

@Configuration
public class SeleniumConfig {
	
	static {
		WebDriverManager.chromiumdriver().useMirror().setup();
	}
	
    /**
     * 	初始化 WebDriver
     */
    @Bean
    public WebDriver webDriver() {
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
		driver.manage().timeouts().implicitlyWait(5000,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(5000,TimeUnit.SECONDS);
		
		return driver;
    }
    
}
