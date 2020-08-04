package com.dreamslab.http;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;

import cn.hutool.core.text.StrSpliter;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HutoolTest {

	

	@BeforeClass
	public static void setupClass() {
	}

	@Before
	public void setupTest() {
		
		
		
	}

	@After
	public void teardown() {
	
	}
	
	
	@Test
	public void ipo_purchase() {
		
		
		
		
	}

	@Test
	public void login_page() {
		// https://i.gtja.com/quotes/v-index.html
		HttpRequest request = HttpUtil.createGet("https://i.gtja.com/quotes/v-index.html");
		HttpResponse response = request.execute();
		log.info(response.headers().toString());
		log.info(response.body());

		// https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json

		// https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json

//POST /wxcos/mycenter/api/consumerInfo.json HTTP/1.1
//		Host: i.gtja.com
//		Connection: keep-alive
//		Content-Length: 0
//		Accept: application/json, text/plain, */*
//		User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36
//		Content-Type: application/x-www-form-urlencoded;charset=UTF-8
//		Origin: https://i.gtja.com
//		Sec-Fetch-Site: same-origin
//		Sec-Fetch-Mode: cors
//		Sec-Fetch-Dest: empty
//		Referer: https://i.gtja.com/quotes/v-index.html
//		Accept-Encoding: gzip, deflate, br
//		Accept-Language: zh,zh-CN;q=0.9
//		Cookie: sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; _wxcos-suid=78cb4553477a3d1271c0cbde14a0a97e; forcedisplay=.; _displaytype=.; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0

		log.info(
				"************************************************************************************************************************");
		String cookie = "JSESSIONID=23EEB1265B104199BF41E5A3B0D49D6C; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; forcedisplay=.; _displaytype=.; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0; _cos-suid=464fb070b95f4fbff5a1df335f71505f; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117,1593852732,1593855742,1593872864; _msg-suid=59fab9bcbb8608c1309ddb282705c066; _mall-suid=9e7812575d4cc0f75b62352d31caef64; _wxcos-suid=6306f90b93e82ad81ef9dddafb41101a; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1593923498; _tcs=3de91058f705db1842ea9d1c3b607aa1";
		HttpRequest request1 = HttpUtil.createPost("https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json")
				.cookie(cookie);
		request1 = request1.header("Host", "i.gtja.com").header("Connection", "keep-alive")
				.header("Accept", "application/json, text/plain, */*").header("User-Agent",
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
		request1 = request1.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").header("Origin",
				"https://i.gtja.com");
		request1 = request1.header("Sec-Fetch-Site", "same-origin");
		request1 = request1.header("Sec-Fetch-Mode", "cors");
		request1 = request1.header("Sec-Fetch-Dest", "empty");
		request1 = request1.header("Referer", "https://i.gtja.com/quotes/v-index.html");
		request1 = request1.header("Accept-Encoding", "gzip, deflate, br");
		request1 = request1.header("Accept-Language", "zh,zh-CN;q=0.9");
		request1 = request1.keepAlive(true);

		HttpResponse response1 = request1.execute();
		log.info(response1.headers().toString());
		log.info(response1.body());

		// https://i.gtja.com/quotes/v-index.html

	}

	@Test
	public void login_request() {

		for (int i = 0; i < 2; i++) {

//		POST /wxcos/weixin/in.html HTTP/1.1
//		Host: i.gtja.com
//		Connection: keep-alive
//		Content-Length: 142
//		Cache-Control: max-age=0
//		Upgrade-Insecure-Requests: 1
//		Origin: https://i.gtja.com
//		Content-Type: application/x-www-form-urlencoded
//		User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
//		Sec-Fetch-Site: same-origin
//		Sec-Fetch-Mode: navigate
//		Sec-Fetch-Dest: document
//		Referer: https://i.gtja.com/wxcos/weixin/in.html?url=https%3A%2F%2Fi.gtja.com%2Fquotes%2Fv-index.html%23%2Ftrade%3Fv%3D578
//		Accept-Encoding: gzip, deflate, br
//		Accept-Language: zh,zh-CN;q=0.9
//		Cookie: JSESSIONID=FD72A71FF220ED1BD38789B8F272195A; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; forcedisplay=.; _displaytype=.; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0; _cos-suid=27021c6c30024a0ae704041f12ad9d31; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117,1593852732,1593855742; _msg-suid=90e4aff8221f7480700b608c3beb6e44; _mall-suid=9fd72abda2408430cbd68a9d45ab3598; _wxcos-suid=6306f90b93e82ad81ef9dddafb41101a; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1593866305; _tcs=3de91058f705db1842ea9d1c3b607aa1

			String cookie = "sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; _wxcos-suid=78cb4553477a3d1271c0cbde14a0a97e; forcedisplay=.; _displaytype=.; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0";
			// String cookie = "JSESSIONID=FD72A71FF220ED1BD38789B8F272195A;
			// sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D;
			// _wxcos-suid=78cb4553477a3d1271c0cbde14a0a97e; forcedisplay=.; _displaytype=.;
			// Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117; loginWay=quickly;
			// loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0";

			HttpRequest request1 = HttpUtil.createPost("https://i.gtja.com/wxcos/weixin/in.html").cookie(cookie);
			request1 = request1.header("Host", "i.gtja.com").header("Connection", "keep-alive")
					.header("Accept", "application/json, text/plain, */*").header("User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
			request1 = request1.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
					.header("Origin", "https://i.gtja.com");
			request1 = request1.header("Sec-Fetch-Site", "same-origin");
			request1 = request1.header("Sec-Fetch-Mode", "cors");
			request1 = request1.header("Sec-Fetch-Dest", "empty");
			request1 = request1.header("Referer", "https://i.gtja.com/quotes/v-index.html");
			request1 = request1.header("Accept-Encoding", "gzip, deflate, br");
			request1 = request1.header("Accept-Language", "zh,zh-CN;q=0.9");
			request1 = request1.keepAlive(true);

			request1 = request1.form("username", "420303198209121736");
			request1 = request1.form("branchNo", "");
			request1 = request1.form("branchId", "");
			request1 = request1.form("loginType", "7");
			request1 = request1.form("trackcode", "3de91058f705db1842ea9d1c3b607aa1");
			request1 = request1.form("opEntrustWay", "1");
			request1 = request1.form("captcha", "");
			request1 = request1.form("password", "828100");

			HttpResponse response1 = request1.execute();
			log.info("<====:" + response1.headers().toString());
			log.info("<====:" + response1.getCookieStr());
			log.info("<====:" + response1.body());

			log.info(
					"                                                                                                                        ");
			log.info(
					"************************************************************************************************************************");
			log.info(
					"                                                                                                                        ");

			// https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json
			HttpRequest request2 = HttpUtil.createPost("https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json")
					.cookie(cookie);
			request2 = request2.header("Host", "i.gtja.com").header("Connection", "keep-alive")
					.header("Accept", "application/json, text/plain, */*").header("User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
			request2 = request2.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
					.header("Origin", "https://i.gtja.com");
			request2 = request2.header("Sec-Fetch-Site", "same-origin");
			request2 = request2.header("Sec-Fetch-Mode", "cors");
			request2 = request2.header("Sec-Fetch-Dest", "empty");
			request2 = request2.header("Referer", "https://i.gtja.com/quotes/v-index.html");
			request2 = request2.header("Accept-Encoding", "gzip, deflate, br");
			request2 = request2.header("Accept-Language", "zh,zh-CN;q=0.9");
			request2 = request2.keepAlive(true);

			HttpResponse response2 = request2.execute();
			log.info("<====:" + response2.headers().toString());
			log.info("<====:" + response2.getCookieStr());
			log.info("<====:" + response2.body());

			log.info(
					"                                                                                                                        ");
			log.info(
					"************************************************************************************************************************");
			log.info(
					"                                                                                                                        ");
			
			// https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json
			HttpRequest request3 = HttpUtil.createGet(
					"https://i.gtja.com/wxcos/securities/stock/query/ipo/credits.json?stockAccount=A493418262&exchangeType=1&businAccount=0311040018017037")
					.cookie(cookie);
			request3 = request3.header("Host", "i.gtja.com").header("Connection", "keep-alive")
					.header("Accept", "application/json, text/plain, */*").header("User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
			request3 = request3.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
					.header("Origin", "https://i.gtja.com");
			request3 = request3.header("Sec-Fetch-Site", "same-origin");
			request3 = request3.header("Sec-Fetch-Mode", "cors");
			request3 = request3.header("Sec-Fetch-Dest", "empty");
			request3 = request3.header("Referer", "https://i.gtja.com/quotes/v-index.html");
			request3 = request3.header("Accept-Encoding", "gzip, deflate, br");
			request3 = request3.header("Accept-Language", "zh,zh-CN;q=0.9");
			request3 = request3.keepAlive(true);

			HttpResponse response3 = request3.execute();
			log.info("<====:" + response3.headers().toString());
			log.info("<====:" + response3.getCookieStr());
			log.info("<====:" + response3.body());

		}
	}
	
	
	

	@Test
	public void str_splite() {
		String cookies = "JSESSIONID=B74BB334B991E7FE45E9E35E266AE54F; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; forcedisplay=.; _displaytype=.; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0; _cos-suid=27021c6c30024a0ae704041f12ad9d31; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117,1593852732,1593855742; _msg-suid=90e4aff8221f7480700b608c3beb6e44; _mall-suid=9fd72abda2408430cbd68a9d45ab3598; _wxcos-suid=6306f90b93e82ad81ef9dddafb41101a; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1593858812; _tcs=3de91058f705db1842ea9d1c3b607aa1";
		Date expire = new Date(System.currentTimeMillis() + 10000000000L);
		List<String> cookieStrs = StrSpliter.split(cookies, "; ", true, true);

		for (String cStr : cookieStrs) {
			List<String> value = StrSpliter.split(cStr, "=", true, true);

			if ("JSESSIONID".equals(value.get(0))) {
				Cookie cookie = new Cookie(value.get(0), value.get(1), "i.gtja.com", "/wxcos", expire, false, true);
				System.out.println(cookie);
				continue;
			}
			Cookie cookie = new Cookie(value.get(0), value.get(1), "i.gtja.com", "/", expire);
			System.out.println(cookie);

		}

		System.out.println(cookieStrs);
	}


}
