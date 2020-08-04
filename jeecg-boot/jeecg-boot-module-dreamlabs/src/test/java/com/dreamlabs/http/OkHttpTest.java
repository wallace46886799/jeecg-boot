package com.dreamslab.http;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;

import cn.hutool.core.text.StrSpliter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Slf4j
public class OkHttpTest {

	

	@BeforeClass
	public static void setupClass() {
		
	}

	@Before
	public void setupTest() {}

	@After
	public void teardown() {
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

	@Test
	public void okhttp() throws IOException {

		String login_url = "https://i.gtja.com/wxcos/weixin/in.html";
		//JSESSIONID=23EEB1265B104199BF41E5A3B0D49D6C; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; forcedisplay=.; _displaytype=.; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0; _cos-suid=464fb070b95f4fbff5a1df335f71505f; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117,1593852732,1593855742,1593872864; _msg-suid=59fab9bcbb8608c1309ddb282705c066; _mall-suid=9e7812575d4cc0f75b62352d31caef64; _wxcos-suid=6306f90b93e82ad81ef9dddafb41101a; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1593923498; _tcs=3de91058f705db1842ea9d1c3b607aa1
		String cookie = "JSESSIONID=23EEB1265B104199BF41E5A3B0D49D6C; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; forcedisplay=.; _displaytype=.; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0; _cos-suid=464fb070b95f4fbff5a1df335f71505f; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117,1593852732,1593855742,1593872864; _msg-suid=59fab9bcbb8608c1309ddb282705c066; _mall-suid=9e7812575d4cc0f75b62352d31caef64; _wxcos-suid=6306f90b93e82ad81ef9dddafb41101a; Hm_lpvt_33a906ba02982c5141104b551b576bf3=1593923498; _tcs=3de91058f705db1842ea9d1c3b607aa1";
		//sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221730d94c3794cc-03641a5a34778c-31627405-1024000-1730d94c37a913%22%7D; _wxcos-suid=78cb4553477a3d1271c0cbde14a0a97e; forcedisplay=.; _displaytype=.; Hm_lvt_33a906ba02982c5141104b551b576bf3=1593839117; loginWay=quickly; loginAccount=420303198209121736; loginBranch=; accountType=7; suitability=0
		OkHttpClient client = new OkHttpClient.Builder()
				.build();
		
		Request.Builder request_builder = new Request.Builder();
		request_builder = request_builder.addHeader("Host", "i.gtja.com");
		request_builder = request_builder.addHeader("Connection", "keep-alive");
		request_builder = request_builder.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
		request_builder = request_builder.addHeader("Accept", "application/json, text/plain, */*");
		request_builder = request_builder.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		request_builder = request_builder.addHeader("Origin", "https://i.gtja.com");
		request_builder = request_builder.addHeader("Sec-Fetch-Site", "same-origin");
		request_builder = request_builder.addHeader("Sec-Fetch-Mode", "cors");
		request_builder = request_builder.addHeader("Sec-Fetch-Dest", "empty");
		request_builder = request_builder.addHeader("Referer", "https://i.gtja.com/wxcos/weixin/in.html?url=https%3A%2F%2Fi.gtja.com%2Fquotes%2Fv-index.html%23%2Ftrade");
		//request_builder = request_builder.addHeader("Accept-Encoding", "gzip, deflate, br");
		request_builder = request_builder.addHeader("Accept-Language", "zh,zh-CN;q=0.9");
		request_builder = request_builder.addHeader("Sec-Fetch-Site", "same-origin");
		request_builder = request_builder.addHeader("Sec-Fetch-Mode", "cors");
		request_builder = request_builder.addHeader("Sec-Fetch-Dest", "empty");
		request_builder = request_builder.addHeader("Cookie", cookie);
		request_builder = request_builder.addHeader("Content-Length", "142");
		
		
		FormBody.Builder form_builder = new FormBody.Builder();
		form_builder = form_builder.add("username", "420303198209121736");
		form_builder = form_builder.add("branchNo", "");
		form_builder = form_builder.add("branchId", "");
		form_builder = form_builder.add("loginType", "7");
		form_builder = form_builder.add("trackcode", "3de91058f705db1842ea9d1c3b607aa1");
		form_builder = form_builder.add("opEntrustWay", "1");
		form_builder = form_builder.add("captcha", "");
		form_builder = form_builder.add("password", "828100");
		FormBody loing_body = form_builder.build();
		
		Request login_request = request_builder.url(login_url)
	      .post(loing_body)
	      .build();
		

		try (Response response = client.newCall(login_request).execute()) {

			ResponseBody body = response.body();
			System.out.println(body.string());

		}
		
	
		request_builder = request_builder.addHeader("Referer", "https://i.gtja.com/quotes/v-index.html");

		String consumer_url = "https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json";
				
	    Request consumer_request = request_builder.url(consumer_url)
		      .build();
	    
	    try (Response response = client.newCall(consumer_request).execute()) {

			ResponseBody body = response.body();
			System.out.println(body.string());
			

		}
	 // https://i.gtja.com/wxcos/mycenter/api/consumerInfo.json
		
//		request2 = request2.header("Host", "i.gtja.com").header("Connection", "keep-alive")
//				.header("Accept", "application/json, text/plain, */*").header("User-Agent",
//						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
//		request2 = request2.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
//				.header("Origin", "https://i.gtja.com");
//		request2 = request2.header("Sec-Fetch-Site", "same-origin");
//		request2 = request2.header("Sec-Fetch-Mode", "cors");
//		request2 = request2.header("Sec-Fetch-Dest", "empty");
//		request2 = request2.header("Referer", "https://i.gtja.com/quotes/v-index.html");
//		request2 = request2.header("Accept-Encoding", "gzip, deflate, br");
//		request2 = request2.header("Accept-Language", "zh,zh-CN;q=0.9");
		

	}

}
