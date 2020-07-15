package org.jeecg.modules.bird.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.jeecg.common.util.AESUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.HttpUtil;
import org.jeecg.modules.bird.entity.BirdParam;
import org.jeecg.modules.bird.entity.BirdTasks;
import org.jeecg.modules.bird.entity.BirdUser;
import org.jeecg.modules.bird.entity.BirdUserLog;
import org.jeecg.modules.bird.service.DoStartEndTasksService;
import org.jeecg.modules.bird.service.IBirdParamService;
import org.jeecg.modules.bird.service.IBirdTasksService;
import org.jeecg.modules.bird.service.IBirdUserLogService;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.mapper.QuartzJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 笨鸟业务逻辑
 *
 * @Author Frank
 */
@Slf4j
@Service
public class DoStartEndTasksServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob>
		implements DoStartEndTasksService {

	@Autowired
	private IBirdParamService iBirdParamService;

	@Autowired
	private IBirdTasksService iBirdTasksService;

	@Autowired
	private IBirdUserLogService iBirdUserLogService;


	@Override
	public void approvalTasks(BirdUser birdUser) {
		try {
			log.info("审批人：{}审批任务开始", birdUser.getName());
			// 查询回待审批人
			List<String> employeeList = this.employeeList(birdUser);
			// 循环待审批人
			for (String employeeId : employeeList) {
				// 查询每个待审批人的任务
				List<String> tasks =  this.employeeTasks(birdUser, employeeId);
				// 提交审批任务
				this.doApprovalTasks(birdUser, employeeId, tasks);
				log.info("审批人：{}，对待审批人：{}的审批完成", birdUser.getName(),employeeId);
			}
			log.info("审批人：{}审批任务结束", birdUser.getName());
		} catch (Exception e) {
			log.error("Approval task error! {}", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void newTask(BirdUser birdUser) {
		log.info("START 新增任务定时任务 newTask !  时间:{}", DateUtils.getTimestamp());
		Random random = new Random();

		// 计算需要创建的任务
		Calendar calendar = Calendar.getInstance();
		List<BirdTasks> birdTasks = this.getBirdTasks(birdUser, calendar, random);
		if (birdTasks.size() == 0) {
			BirdUserLog entity = new BirdUserLog();
			entity.setBirdUserId(birdUser.getId());
			entity.setLogMsg("【定时】创建任务，用户：【" + birdUser.getName() + "】响应：【请先配置需要创建的任务】");
			iBirdUserLogService.save(entity);
			return;
		}
		
		// 获取审批人信息，发起HTTP请求
		JSONObject jsonObject1 = taskList(birdUser);
		JSONObject data = jsonObject1.getJSONObject("data");
		String verifyId = data.getString("verifyId");
		String verifyName = data.getString("verifyName");
		
		// 组装新增任务参数
		String response = null;
		
		// 公共配置
		String protocol = this.getBirdParam("protocol");
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");

		// 业务配置
		// /task/addTask
		String path = this.getBirdParam("newtask.path");
		String method = this.getBirdParam("newtask.method");
		// ?targetName=${targetName:encry}&taskType=${taskType:encry}&taskTargetTypeId=${taskTargetTypeId:encry}&targetDesc=${targetDesc:encry}
		// &verifyId=${verifyId:encry}&verifyName=${verifyName:encry}&beginTimeTmp=${beginTimeTmp:encry}&endTimeTmp=${endTimeTmp:encry}
		// &contractNo=${contractNo:encry}&inwork=${inwork:encry}&projectNo=${projectNo:encry}&token=${token:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		String paramTemp = this.getBirdParam("newtask.paramTemp");

		// 需要根据必输任务模板优先提交任务，根据报文获取必输任务模板编号和选输任务模板编号
		for (BirdTasks birdTask : birdTasks) {
			try {
				// 软集项目讨论
				// 36.5
				String targetName = birdTask.getTaskName();
				// 指派任务和计划任务
				String taskType = "2";
				// 根据任务模板填充任务数据，在数据库中保存好JSON数组
				// [{"taskTargetOption":"0","taskTargetName":"任务名称","isMust":"1","option":null,"description":"5字以上任务名","content":"软集项目讨论"},{"taskTargetOption":"1","taskTargetName":"任务描述","isMust":"0","option":null,"description":"请输入任务描述","content":"软集项目讨论"}]
				// [{"taskTargetOption":"7","taskTargetName":"体温任务","isMust":"1","option":null,"description":"请输入数字","content":36.5}]
				String targetTypeDesc = birdTask.getTaskTypeDesc();
				calendar.set(Calendar.HOUR_OF_DAY, 9);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				String beginTimeTmp = String.valueOf(calendar.getTimeInMillis());
				calendar.set(Calendar.HOUR_OF_DAY, 18);
				String endTimeTmp = String.valueOf(calendar.getTimeInMillis());
				// 2020022757:初始化任务类型
				// 2020030969:今日体温
				String taskTargetTypeId = birdTask.getTaskTargetTypeId();
				
				// PRO00000637
				String contractNo = birdUser.getProject();
				// eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwMDAwMDE1NCIsImVtcGxveWVlSWQiOiIwMDAwMDE1NCIsImlhdCI6MTU4NDcwMzU0OH0.0i3dhGJHbOkqS1dgJR5WnpKGYkSqgVf7Hldt3u86DpQ
				String token = birdUser.getTokenStr();
				// 1
				String inwork = birdUser.getInwork();
				
				// 构建新增任务的请求参数明文Map
				Map<String, String> param = this.getTaskRequstParamMap(targetName, taskType,taskTargetTypeId, targetTypeDesc, beginTimeTmp,
						endTimeTmp, contractNo, inwork ,token, verifyId, verifyName, appVersion);
				
				String paramStr = createRequest(param,paramTemp);

				response = sendRequest(protocol, domain, path, method, paramStr);

				JSONObject jsonObject = JSON.parseObject(response);

				if ("000000".equals(jsonObject.getString("code"))) {
					log.info("newTask success!");
					BirdUserLog entity = new BirdUserLog();
					entity.setBirdUserId(birdUser.getId());
					entity.setLogMsg("【定时】创建任务：【"+jsonObject.getJSONObject("data").getString("taskId")+"】，用户：【" + birdUser.getName() + "】成功");
					iBirdUserLogService.save(entity);
				} else {
					log.error("newTask error! {}", response);
					BirdUserLog entity = new BirdUserLog();
					entity.setBirdUserId(birdUser.getId());
					entity.setLogMsg("【定时】创建任务，用户：【" + birdUser.getName() + "】失败");
					iBirdUserLogService.save(entity);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				response = e.toString();
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】创建任务，用户：【" + birdUser.getName() + "】失败");
				iBirdUserLogService.save(entity);
			}
		}
		log.info("END 新增任务定时任务 newTask !  时间:{}", DateUtils.getTimestamp());
	}

	

	@Override
	public void startTask(BirdUser birdUser) {
		log.info("START 开始任务定时任务 startTask !  时间:{}", DateUtils.getTimestamp());
		Random random = new Random();

		String response = null;

		// 公共配置
		String protocol = this.getBirdParam("protocol");
		// 3.5.6
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");

		// 业务配置
		// /task/getApprovalCount
		String path = this.getBirdParam("starttask.path");
		String method = this.getBirdParam("starttask.method");
		// GET /task/startTask?taskId=Hu/VeG%252B4WdDIk%252BtdvuDA1A==&token=cYpWndRpfb4IXWgMYCRSwry7xs9o17IwraKge1vjY6mWq%252BgUmwvxfJVWMPZp0r4acs2BGLOr/79pHizWBZPJS%252BD7hVCt1NYpNt4xQjH8Qb27lDE7Zr/%252BCFZfS%252B9a4EF6keMcT9ZlaymIIaHXWvBuQ3zxCXSWUmqihP5oKt9QzdSczgfa5Uh1w2UigsWk0QWB&appVersion=E8BKqXovdzOyN/6r4W3bhQ==&timestampWithRandomValue=1586434504012with0.82546779773819 HTTP/1.1
		// ?taskId=${taskId:encry}&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		String paramTemp = this.getBirdParam("starttask.paramTemp");
		
		// 获取任务列表
		JSONObject jsonObject1 =taskList(birdUser);
		JSONObject data = jsonObject1.getJSONObject("data");
		JSONArray targetList = data.getJSONArray("todayTaskTargetList");

		for (int i = 0; i < targetList.size(); i++) {
			JSONObject jsonObject = targetList.getJSONObject(i);
			String taskId = jsonObject.getString("taskId");
			String status = jsonObject.getString("status");

			if ("0".equals(status)) {
				try {
					String token = birdUser.getTokenStr();
					
					Map<String, String> param = this.getTaskStartEndRequstParamMap(random, appVersion, token, taskId);
					
					String paramStr = createRequest(param,paramTemp);
					
					response = sendRequest(protocol, domain, path, method, paramStr);

					JSONObject jsonObject2 = JSON.parseObject(response);

					if ("000000".equals(jsonObject2.getString("code"))) {
						log.info("startTask success!");
						BirdUserLog entity = new BirdUserLog();
						entity.setBirdUserId(birdUser.getId());
						entity.setLogMsg("【定时】开始任务：【"+ taskId +"】，用户：【" + birdUser.getName() + "】成功");
						iBirdUserLogService.save(entity);
					} else {
						log.error("startTask error! {}", response);
						BirdUserLog entity = new BirdUserLog();
						entity.setBirdUserId(birdUser.getId());
						entity.setLogMsg("【定时】开始任务，用户：【" + birdUser.getName() + "】失败");
						iBirdUserLogService.save(entity);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					response = e.toString();
					BirdUserLog entity = new BirdUserLog();
					entity.setBirdUserId(birdUser.getId());
					entity.setLogMsg("【定时】开始任务，用户：【" + birdUser.getName() + "】失败");
					iBirdUserLogService.save(entity);
				}
			}
		}
		log.info("END 开始任务定时任务 startTask !  时间:{}", DateUtils.getTimestamp());
	}

	

	@Override
	public void endTask(BirdUser birdUser) {
		log.info("START 提交任务定时任务 endTask !  时间:{}", DateUtils.getTimestamp());
		Random random = new Random();

		String response = null;

		// 公共配置
		String protocol = this.getBirdParam("protocol");
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");

		// 业务配置
		// /task/submitTask
		String path = this.getBirdParam("submittask.path");
		String method = this.getBirdParam("submittask.method");
		// ?taskId=${taskId:encry}&resultDetail=&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		String paramTemp = this.getBirdParam("submittask.paramTemp");

		JSONObject jsonObject1 = taskList(birdUser);
		JSONObject data = jsonObject1.getJSONObject("data");
		JSONArray targetList = data.getJSONArray("todayTaskTargetList");

		for (int i = 0; i < targetList.size(); i++) {
			JSONObject jsonObject = targetList.getJSONObject(i);
			String taskId = jsonObject.getString("taskId");
			String status = jsonObject.getString("status");

			if ("1".equals(status)) {
				try {
					String token = birdUser.getTokenStr();

					Map<String, String> param = this.getTaskStartEndRequstParamMap(random, appVersion, token, taskId);

					String paramStr = createRequest(param,paramTemp);

					response = sendRequest(protocol, domain, path, method, paramStr);

					JSONObject jsonObject2 = JSON.parseObject(response);

					if ("000000".equals(jsonObject2.getString("code"))) {
						log.info("endTask success! 任务{}，状态{}，提交成功。",taskId,status);
						BirdUserLog entity = new BirdUserLog();
						entity.setBirdUserId(birdUser.getId());
						entity.setLogMsg("【定时】提交任务：【"+taskId+"】，用户：【" + birdUser.getName() + "】成功");
						iBirdUserLogService.save(entity);
					} else {
						log.error("endTask error! {}", response);
						BirdUserLog entity = new BirdUserLog();
						entity.setBirdUserId(birdUser.getId());
						entity.setLogMsg("【定时】提交任务，用户：【" + birdUser.getName() + "】失败");
						iBirdUserLogService.save(entity);
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
					response = e.toString();
					BirdUserLog entity = new BirdUserLog();
					entity.setBirdUserId(birdUser.getId());
					entity.setLogMsg("【定时】提交任务，用户：【" + birdUser.getName() + "】失败");
					iBirdUserLogService.save(entity);
				}
			} else {
				log.info("任务{}，状态{}，未开始或者已经被提交。",taskId,status);
			}
		}
		log.info("END 提交任务定时任务 endTask !  时间:{}", DateUtils.getTimestamp());
	}
	
	private Map<String, String> getTaskStartEndRequstParamMap(Random random, String appVersion, String token,
			String taskId) {
		Map<String, String> param = new HashMap<>();
		param.put("taskId", taskId);
		param.put("token", token);
		param.put("appVersion", appVersion);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());
		return param;
	}
	
	
	private String getError(String message) {
		return "{\n" + "\t\t\"message\": \"" + message + "\",\n" + "\t\t\"code\": \"ERROR\"\n" + "\t}";
	}

	/**
	 * 获取此次创建的任务
	 * 增加每日必填任务
	 *
	 * @param birdUser
	 * @param calendar
	 * @param random
	 * @return
	 */
	private List<BirdTasks> getBirdTasks(BirdUser birdUser, Calendar calendar, Random random) {
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

		QueryWrapper<BirdTasks> query1 = new QueryWrapper<>();
		query1.eq("week_day", weekDay).eq("user_id", birdUser.getId());
		List<BirdTasks> birdTasks1 = iBirdTasksService.list(query1);
		log.info("START 提交任务定时任务 startTask !  当天任务列表:{}", birdTasks1);
		
		
		// 包括没有设置具体星期和必填的任务
		QueryWrapper<BirdTasks> query2 = new QueryWrapper<>();
		query2.eq("is_must", "1").eq("user_id", birdUser.getId());
		List<BirdTasks> birdTasks2 = iBirdTasksService.list(query2);
		log.info("START 提交任务定时任务 startTask !  必填任务列表:{}", birdTasks2);
		
		List<BirdTasks> birdTasks = new ArrayList<>(birdTasks1.size() + birdTasks2.size());
		birdTasks.addAll(birdTasks1);
		birdTasks.addAll(birdTasks2);
		
		return birdTasks;
//		
//		if (birdTasks.size() <= 5) {
//			return birdTasks;
//		} else {
//			List<BirdTasks> birdTasksReturn = new ArrayList<>(5);
//			while (birdTasksReturn.size() < 5) {
//				int i = random.nextInt(birdTasks.size());
//				BirdTasks birdTask = birdTasks.get(i);
//				if (!birdTasksReturn.contains(birdTask)) {
//					birdTasksReturn.add(birdTask);
//				}
//			}
//			return birdTasksReturn;
//		}
	}

	/*
	 * @Override public String signin(BirdUser birdUser) {
	 * 
	 * String response;
	 * 
	 * log.info("START 签到定时任务 SignedJob !  时间:{}", DateUtils.getTimestamp()); Random
	 * random = new Random(); // 公共配置 String protocol =
	 * this.getBirdParam("protocol"); String appVersion =
	 * this.getBirdParam("appVersion"); String domain = this.getBirdParam("domain");
	 * 
	 * // 业务配置 String path = this.getBirdParam("signin.path"); String method =
	 * this.getBirdParam("signin.method"); String paramTemp =
	 * this.getBirdParam("signin.paramTemp"); // String path = "/sign/in";// 请求地址 //
	 * String method = "get";// 请求方式 // String paramTemp =
	 * "?signInOutFlag=${signInOutFlag:encry}&signAddress=${signAddress:encry}&signProject=${signProject:encry}&signLngLat=${signLngLat:encry}&timestamp=${timestamp:encry}&signature=${signature:encry}&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}";
	 * try { // 用户信息 String addresses = birdUser.getAddresses(); String latitudeStr
	 * = birdUser.getLatitude();// 0.000150 String longitudeStr =
	 * birdUser.getLongitude();// 0.000700 String project = birdUser.getProject();
	 * String token = birdUser.getTokenStr();
	 * 
	 * Map<String, String> param = this.getRequstParamMap(random, appVersion,
	 * addresses, latitudeStr, longitudeStr, project, token);
	 * 
	 * Map<String, String> paramEncry = new HashMap<>(); for (String key :
	 * param.keySet()) { String value = param.get(key);
	 * log.info("request param [{}] : [{}]", key, value); String value2 =
	 * AESUtil.encrypt(value);// 使用 进行填充，所有字符串不得含有字符  （\u0000） paramEncry.put(key,
	 * value2); log.info("encrypted request param [{}] : [{}]", key, value2);
	 * log.info("decrypt test encrypted request param [{}] : [{}]", key,
	 * AESUtil.decrypt(value2)); }
	 * 
	 * // 添加timestampWithRandomValue String paramStr = paramTemp; for (String key :
	 * param.keySet()) { String replaceKey = "\\$\\{" + key + "}"; String
	 * replaceKey2 = "\\$\\{" + key + ":encry}"; paramStr =
	 * paramStr.replaceAll(replaceKey, encodeURIComponent(param.get(key))); paramStr
	 * = paramStr.replaceAll(replaceKey2, encodeURIComponent(paramEncry.get(key)));
	 * }
	 * 
	 * log.info("request params：{}", paramStr); response = HttpUtil.request(protocol
	 * + "://" + domain + path, domain, paramStr, method);
	 * log.info("response body：{}", response);
	 * 
	 * JSONObject jsonObject = JSON.parseObject(response);
	 * 
	 * if ("000000".equals(jsonObject.getString("code"))) {
	 * log.info("sign in success!"); } else { log.error("sign in error!"); } } catch
	 * (Exception e) { log.error(e.getMessage(), e); response = e.toString(); }
	 * log.info("END 签到定时任务 SignedJob !  时间:{}", DateUtils.getTimestamp()); return
	 * response; }
	 * 
	 * @Override public String signout(BirdUser birdUser) { String response;
	 * 
	 * log.info("START 签到定时任务 SignedJob !  时间:{}", DateUtils.getTimestamp()); Random
	 * random = new Random(); // 公共配置 String protocol =
	 * this.getBirdParam("protocol"); String appVersion =
	 * this.getBirdParam("appVersion"); String domain = this.getBirdParam("domain");
	 * 
	 * // 业务配置 String path = this.getBirdParam("signout.path"); String method =
	 * this.getBirdParam("signout.method"); String paramTemp =
	 * this.getBirdParam("signout.paramTemp"); try { // 用户信息 String addresses =
	 * birdUser.getAddresses(); String latitudeStr = birdUser.getLatitude();//
	 * 0.000150 String longitudeStr = birdUser.getLongitude();// 0.000700 String
	 * project = birdUser.getProject(); String token = birdUser.getTokenStr();
	 * 
	 * Map<String, String> param = this.getRequstParamMap(random, appVersion,
	 * addresses, latitudeStr, longitudeStr, project, token);
	 * 
	 * Map<String, String> paramEncry = new HashMap<>(); for (String key :
	 * param.keySet()) { String value = param.get(key);
	 * log.info("request param [{}] : [{}]", key, value); String value2 =
	 * AESUtil.encrypt(value);// 使用 进行填充，所有字符串不得含有字符  （\u0000） paramEncry.put(key,
	 * value2); log.info("encrypted request param [{}] : [{}]", key, value2);
	 * log.info("decrypt test encrypted request param [{}] : [{}]", key,
	 * AESUtil.decrypt(value2)); }
	 * 
	 * // 添加timestampWithRandomValue String paramStr = paramTemp; for (String key :
	 * param.keySet()) { String replaceKey = "\\$\\{" + key + "}"; String
	 * replaceKey2 = "\\$\\{" + key + ":encry}"; paramStr =
	 * paramStr.replaceAll(replaceKey, encodeURIComponent(param.get(key))); paramStr
	 * = paramStr.replaceAll(replaceKey2, encodeURIComponent(paramEncry.get(key)));
	 * }
	 * 
	 * log.info("request params：{}", paramStr); response = HttpUtil.request(protocol
	 * + "://" + domain + path, domain, paramStr, method);
	 * log.info("response body：{}", response);
	 * 
	 * JSONObject jsonObject = JSON.parseObject(response);
	 * 
	 * if ("000000".equals(jsonObject.getString("code"))) {
	 * log.info("sign in success!"); } else { log.error("sign in error!"); } } catch
	 * (Exception e) { log.error(e.getMessage(), e); response = e.toString(); }
	 * log.info("END 签到定时任务 SignedJob !  时间:{}", DateUtils.getTimestamp()); return
	 * response; }
	 */

	private String getBirdParam(String key) {
		QueryWrapper<BirdParam> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("param_key", key);
		BirdParam one = iBirdParamService.getOne(queryWrapper);
		return one.getParamValue();
	}
	
	private String getBirdParam(String key,String defaultValue) {
		try {
			String value = getBirdParam(key);
			return value;
		}catch(Exception e) {
			// log.error("can not get value", e);
			log.info("can not get value, return default value：{}", defaultValue);
			return defaultValue;
		}
	}

	/**
	 * URLEncoder TODO URLEncoder，js和java的encodeURIComponent 和
	 * URLEncoder，的规则不同，目前不影响使用，但是报文会不一样
	 *
	 * @param string 需要加密的字符串
	 * @return URLEncoder后的字符串
	 * @throws UnsupportedEncodingException 若不支持UTF-8编码方式，抛出异常
	 */
	private String encodeURIComponent(String string) throws UnsupportedEncodingException {
		return string.replace("+","%252B");
//		return URLEncoder.encode(string, "UTF-8");
	}
	
	private String decodeURIComponent(String string) throws UnsupportedEncodingException {
		return string.replace("%252B","+");
//		return URLDecoder.decode(string, "UTF-8");
	}

	private Map<String, String> getTaskRequstParamMap(String targetName, String taskType,String taskTargetTypeId, String targetTypeDesc, String beginTimeTmp,
			String endTimeTmp, String contractNo, String inwork ,String token, String verifyId, String verifyName, String appVersion) {
		
		// ?targetName=${targetName:encry}&taskType=${taskType:encry}&taskTargetTypeId=${taskTargetTypeId:encry}&targetDesc=${targetDesc:encry}
		// &verifyId=${verifyId:encry}&verifyName=${verifyName:encry}&beginTimeTmp=${beginTimeTmp:encry}&endTimeTmp=${endTimeTmp:encry}
		// &contractNo=${contractNo:encry}&inwork=${inwork:encry}&projectNo=${projectNo:encry}&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		
		Map<String, String> param = new HashMap<>();
		param.put("targetName", targetName);
		param.put("taskType", taskType);
		param.put("taskTargetTypeId", taskTargetTypeId);
		param.put("taskTypeDesc", targetTypeDesc == null ? "" : targetTypeDesc);
		param.put("verifyId", verifyId);
		param.put("verifyName", verifyName);
		param.put("beginTimeTmp", beginTimeTmp);
		param.put("endTimeTmp", endTimeTmp);
		param.put("contractNo", contractNo);
		param.put("inwork", inwork);
		param.put("projectNo", contractNo);
		
		param.put("token", token);
		param.put("appVersion", appVersion);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());
		
		return param;
	}

	/**
	 * 组装参数
	 *
	 * @param random
	 * @param appVersion0
	 * @param addresses
	 * @param latitudeStr
	 * @param longitudeStr
	 * @param project
	 * @param token
	 * @return 参数map
	 */
	private Map<String, String> getTasklistRequstParamMap(Random random, String appVersion0, String addresses,
			String latitudeStr, String longitudeStr, String project, String token) {
		Map<String, String> param = new HashMap<>();
		param.put("signInOutFlag", "1");
		param.put("signAddress", addresses);
		param.put("signProject", project);
		param.put("signLngLat", this.getSignLngLat(latitudeStr, longitudeStr, random));
		param.put("timestamp", String.valueOf(System.currentTimeMillis()));
		param.put("token", token);
		param.put("appVersion", appVersion0);
		param.put("contractNo", project);

		String signature = DigestUtils
				.sha1Hex(param.get("signInOutFlag") + "&" + param.get("signAddress") + "&" + param.get("signProject")
						+ "&" + param.get("signLngLat") + "&" + param.get("timestamp") + "&" + param.get("token"));
		param.put("signature", signature);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());

		log.info(signature);
		return param;
	}

	private Map<String, String> getEmployeeTasksRequstParamMap(Random random, String employeeId, String defaultNum,String token, String appVersion
			) {
		Map<String, String> param = new HashMap<>();
		param.put("timestamp", String.valueOf(System.currentTimeMillis()));
		param.put("token", token);
		param.put("appVersion", appVersion);
		param.put("subEmployeeId", employeeId);
		param.put("defaultNum", defaultNum);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());
		return param;
	}

	/**
	 * 组装参数
	 *
	 * @param random
	 * @param appVersion0
	 * @param addresses
	 * @param latitudeStr
	 * @param longitudeStr
	 * @param project
	 * @param token
	 * @return 参数map
	 */
	private Map<String, String> getEmployeelistRequstParamMap(Random random, String token, String appVersion) {
		Map<String, String> param = new HashMap<>();
		param.put("timestamp", String.valueOf(System.currentTimeMillis()));
		param.put("token", token);
		param.put("appVersion", appVersion);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());
		return param;
	}

	/**
	 * 组装参数
	 *
	 * @param random
	 * @param appVersion0
	 * @param addresses
	 * @param latitudeStr
	 * @param longitudeStr
	 * @param project
	 * @param token
	 * @return 参数map
	 */
	private Map<String, String> getApprovalTaskRequstParamMap(Random random,String taskId,
			String completeScore, String qualityScore, String token, String appVersion) {
		Map<String, String> param = new HashMap<>();
		param.put("timestamp", String.valueOf(System.currentTimeMillis()));
		// ${completeScore:encry}&qualityScore=${qualityScore:encry}
		param.put("taskId", taskId);
		param.put("completeScore", completeScore); //100
		param.put("qualityScore", qualityScore); //100
		
		param.put("token", token);
		param.put("appVersion", appVersion);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());
		return param;
	}
	
	private Map<String, String> getDayApproveRequstParamMap(Random random, String token, String appVersion,
			String employeeId, String dayCompleteScore, String dayQualityScore, String totalPoint, String taskId,
			String busyGrade) {
		// GET
		// /approval/dayApprove?subEmployeeId=N3f8gT13yzhqBRoQ8298nQ==&dayCompleteScore=tn19kYq9gPWZXgim%252BRQsZg==&dayQualityScore=tn19kYq9gPWZXgim%252BRQsZg==&unresolverBugCount=0&remark=xfQoBRZv/GHc/2mfnqxHyw==&totalPoint=yd%252B8YFV9jRzwLm3PHWu41g==&
		// taskId=RFrvLK5DKkQ4HVdc6LGCzg==&busyGrade=yd%252B8YFV9jRzwLm3PHWu41g==&approvalFlag=0&token=cYpWndRpfb4IXWgMYCRSwry7xs9o17IwraKge1vjY6mWq%252BgUmwvxfJVWMPZp0r4acs2BGLOr/79pHizWBZPJS%252BD7hVCt1NYpNt4xQjH8Qb27lDE7Zr/%252BCFZfS%252B9a4EF6keMcT9ZlaymIIaHXWvBuQ3zxCXSWUmqihP5oKt9QzdSczgfa5Uh1w2UigsWk0QWB&appVersion=E8BKqXovdzOyN/6r4W3bhQ==&timestampWithRandomValue=1586434430501with0.8069630794855165
		// HTTP/1.1
		
		int randomInt = random.nextInt(5);
		log.info("current random is:{},the base score is:{}", randomInt,totalPoint);
		Map<String, String> param = new HashMap<>();
		// ${completeScore:encry}&qualityScore=${qualityScore:encry}
		param.put("subEmployeeId", employeeId);
		param.put("dayCompleteScore", String.valueOf(Integer.parseInt(dayCompleteScore)+randomInt));
		param.put("dayQualityScore", String.valueOf(Integer.parseInt(dayQualityScore)+randomInt));
		param.put("unresolverBugCount", "0");
		param.put("remark", "Good Job!");
		param.put("totalPoint",  String.valueOf(Integer.parseInt(totalPoint)+randomInt));
		param.put("taskId", taskId);
		param.put("busyGrade", String.valueOf(Integer.parseInt(busyGrade)+randomInt));
		param.put("approvalFlag", "0");
		
		param.put("token", token);
		param.put("timestamp", String.valueOf(System.currentTimeMillis()));
		param.put("appVersion", appVersion);
		param.put("timestampWithRandomValue", System.currentTimeMillis() + "with" + Math.random());
		return param;
	}

	/*
	 * public static void main(String[] args) throws JobExecutionException {
	 * SignedServiceImpl signedService = new SignedServiceImpl();
	 * signedService.execute(birdUserO); }
	 */

	/**
	 * 获取随机地址坐标 经度：+-0.00015 纬度：+-0.00070
	 *
	 * @param latitudeStr        经度
	 * @param longitudeStr       纬度
	 * @param addressRandowIndex random对象
	 * @return 范围内的随机坐标
	 */
	private String getSignLngLat(String latitudeStr, String longitudeStr, Random addressRandowIndex) {
		double latituderange = addressRandowIndex.nextInt(15) * 0.00001;
		double longituderange = addressRandowIndex.nextInt(70) * 0.00001;
		int i = addressRandowIndex.nextInt(2);
		if (i < 1) {
			latituderange = -latituderange;
		}
		// int j = addressRandowIndex.nextInt(2);
		if (i < 1) {
			longituderange = -longituderange;
		}
		BigDecimal latitude = new BigDecimal(latitudeStr).add(new BigDecimal(latituderange)).setScale(6,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal longitude = new BigDecimal(longitudeStr).add(new BigDecimal(longituderange)).setScale(6,
				BigDecimal.ROUND_HALF_UP);
		return latitude.toString() + "," + longitude.toString();
	}
	
	
	/**
	 * 
	 * 请求：/task/getPersonalTaskList?contractNo=4Inhak3fl0ma93iikQWz2A==&token=cYpWndRpfb4IXWgMYCRSwry7xs9o17IwraKge1vjY6mWq%252BgUmwvxfJVWMPZp0r4acs2BGLOr/79pHizWBZPJS%252BD7hVCt1NYpNt4xQjH8Qb27lDE7Zr/%252BCFZfS%252B9a4EF6keMcT9ZlaymIIaHXWvBuQ3zxCXSWUmqihP5oKt9QzdSczgfa5Uh1w2UigsWk0QWB&appVersion=E8BKqXovdzOyN/6r4W3bhQ==&timestampWithRandomValue=1586412280818with0.5256150928088917
	 * 响应：{"message":"请求成功","data":{"acceptTaskList":[],"dispatchEmpList":[{"employeeId":"00000103","employeeName":"范亚坤","email":"fanyk@i2finance.net","mobile":"13522723119","departmentName":null,"gradeName":null,"projectName":null,"boardingTime":"2013-08-19T16:00:00.000+0000","employeeGrade":"2","integration":230,"imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483007049116.jpg","level":null,"cardid":"412725199209225038","employeeDuty":"0050","isOnDuty":"01","stock":0,"employeeAdress":null,"sex":"1","employeeNation":"汉族","birthTime":"1992-09-21T16:00:00.000+0000","workLife":3.00,"departmentId":"0273","degree":"专科","leaveTime":null,"hukou":"河南","hukouType":"6","marriedStatus":"1","politicsStatus":"5","educationBackground":"4","gradutedSchool":null,"major":"软件工程","gradutedTime":null,"beginWorkTime":"2012-06-30T16:00:00.000+0000","laborContractBeginTime":"2013-08-19T16:00:00.000+0000","expectedPositiveTime":"2014-02-19T16:00:00.000+0000","actualPositiveTime":"2014-02-19T16:00:00.000+0000","householdAddress":"河南省鹿邑县辛集镇辛旺办事处辛旺","employmentWay":"2","leaveReason":null,"beyondCompany":"00022","grassLevelId":"00017","isFixedPlaceWorking":"1","actualEmployeeId":"00033","isCheckImsi":"1","leavePostTime":null,"leavePostReason":"1","viewBudget":null,"rabc":null},{"employeeId":"00000163","employeeName":"李志伟","email":"lizw@i2finance.net","mobile":"18612836040","departmentName":null,"gradeName":null,"projectName":null,"boardingTime":"2015-08-16T16:00:00.000+0000","employeeGrade":"2","integration":47,"imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483092484897.jpg","level":null,"cardid":"411526198904052917","employeeDuty":"0050","isOnDuty":"01","stock":0,"employeeAdress":null,"sex":"1","employeeNation":"汉族","birthTime":"1989-04-04T16:00:00.000+0000","workLife":5.00,"departmentId":"0274","degree":"学士","leaveTime":null,"hukou":"河南","hukouType":"6","marriedStatus":"0","politicsStatus":"5","educationBackground":"3","gradutedSchool":null,"major":"电子信息技术及仪器","gradutedTime":null,"beginWorkTime":"2013-10-07T16:00:00.000+0000","laborContractBeginTime":"2015-08-16T16:00:00.000+0000","expectedPositiveTime":"2016-02-16T16:00:00.000+0000","actualPositiveTime":"2016-02-16T16:00:00.000+0000","householdAddress":"河南省信阳市横川县上油岗乡桃庄村学校组022号","employmentWay":"智联招聘","leaveReason":null,"beyondCompany":"00022","grassLevelId":"00017","isFixedPlaceWorking":"1","actualEmployeeId":"00137","isCheckImsi":"1","leavePostTime":null,"leavePostReason":"1","viewBudget":null,"rabc":null},{"employeeId":"00000422","employeeName":"刘爽","email":"liushuang1@i2finance.net","mobile":"18910701757","departmentName":null,"gradeName":null,"projectName":null,"boardingTime":"2014-08-10T16:00:00.000+0000","employeeGrade":"3","integration":8,"imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483074792859.JPG","level":null,"cardid":"411502198511207710","employeeDuty":"0143","isOnDuty":"01","stock":0,"employeeAdress":null,"sex":"1","employeeNation":"汉族","birthTime":"1985-11-19T16:00:00.000+0000","workLife":10.00,"departmentId":"0275","degree":"学士","leaveTime":null,"hukou":"河南","hukouType":"5","marriedStatus":"1","politicsStatus":"6","educationBackground":"3","gradutedSchool":null,"major":"计算机软件","gradutedTime":null,"beginWorkTime":"2007-07-31T16:00:00.000+0000","laborContractBeginTime":"2014-08-10T16:00:00.000+0000","expectedPositiveTime":"2015-02-10T16:00:00.000+0000","actualPositiveTime":"2015-02-10T16:00:00.000+0000","householdAddress":"河南省信阳市浉河区柳林乡新街1号院14号","employmentWay":"智联招聘","leaveReason":null,"beyondCompany":"00022","grassLevelId":"00018","isFixedPlaceWorking":"1","actualEmployeeId":"00067","isCheckImsi":"1","leavePostTime":null,"leavePostReason":"1","viewBudget":null,"rabc":null},{"employeeId":"00000490","employeeName":"田立志","email":"tianlz@i2finance.net","mobile":"18600788399","departmentName":null,"gradeName":null,"projectName":null,"boardingTime":"2013-04-26T16:00:00.000+0000","employeeGrade":"4","integration":9,"imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483074141976.jpg","level":null,"cardid":"130283198503071511","employeeDuty":"0425","isOnDuty":"01","stock":0,"employeeAdress":null,"sex":"1","employeeNation":"汉族","birthTime":"1985-03-06T16:00:00.000+0000","workLife":5.00,"departmentId":"0273","degree":"学士","leaveTime":null,"hukou":"河北","hukouType":"6","marriedStatus":"1","politicsStatus":"6","educationBackground":"3","gradutedSchool":null,"major":"信息与计算科学","gradutedTime":null,"beginWorkTime":"2010-02-28T16:00:00.000+0000","laborContractBeginTime":"2013-04-26T16:00:00.000+0000","expectedPositiveTime":"2013-10-26T16:00:00.000+0000","actualPositiveTime":"2013-10-26T16:00:00.000+0000","householdAddress":"河北省唐山市迁安县夏官营镇花庄村016号","employmentWay":"智联招聘","leaveReason":"","beyondCompany":"00022","grassLevelId":"00021","isFixedPlaceWorking":"1","actualEmployeeId":"00026","isCheckImsi":"1","leavePostTime":null,"leavePostReason":"1","viewBudget":null,"rabc":null},{"employeeId":"00003318","employeeName":"张剑","email":"zhangj@i2finance.net","mobile":"18621646569","departmentName":null,"gradeName":null,"projectName":null,"boardingTime":"2019-11-30T16:00:00.000+0000","employeeGrade":"3","integration":null,"imageId":"http://bird2.i2soft.net:82/images/2/00000205/1575013380747.png","level":null,"cardid":"32038119820604921X","employeeDuty":"0372","isOnDuty":"01","stock":null,"employeeAdress":null,"sex":"1","employeeNation":"汉","birthTime":"1982-06-03T16:00:00.000+0000","workLife":15.00,"departmentId":"0317","degree":"学士","leaveTime":null,"hukou":"江苏","hukouType":"6","marriedStatus":"1","politicsStatus":"6","educationBackground":"3","gradutedSchool":null,"major":"计算机科学与技术","gradutedTime":null,"beginWorkTime":"2004-01-31T16:00:00.000+0000","laborContractBeginTime":"2019-11-30T16:00:00.000+0000","expectedPositiveTime":"2019-11-30T16:00:00.000+0000","actualPositiveTime":"2019-11-30T16:00:00.000+0000","householdAddress":"江苏省新沂市新安镇藏圩村","employmentWay":"其他: 内部转岗 ","leaveReason":"","beyondCompany":"00022","grassLevelId":"00017","isFixedPlaceWorking":"1","actualEmployeeId":"00988","isCheckImsi":"0","leavePostTime":null,"leavePostReason":"1","viewBudget":null,"rabc":null}],"verifyId":"00000854","planTaskTargetList":[],"dispatchFlag":"0","todayTaskTargetList":[{"taskTypeId":null,"taskTypeName":null,"isMust":null,"choice":null,"taskId":"2020040902468075","inwork":"1","inworkVerify":null,"budget":null,"projectNo":"PRO00000637","verifyId":"00000854","employeeId":"00000154","contractNo":"PRO00000637","targetName":"36.5","targetDesc":null,"completeScore":null,"qualityScore":null,"createTime":"2020-04-09T05:51:37.000+0000","beginTime":"2020-04-09T05:51:00.000+0000","endTime":"2020-04-09T10:00:00.000+0000","status":"0","taskType":"2","completeTime":null,"verifyName":"张锐","remark":null,"vremark":null,"responsibleEmpName":null,"subTaskCount":null,"assignedId":null,"resultDetail":null,"taskTargetTypeId":"2020030969","taskTypeDesc":"[{\"taskTargetOption\":\"7\",\"taskTargetName\":\"体温任务\",\"isMust\":\"1\",\"option\":null,\"description\":\"请输入数字\",\"content\":36.5}]"}],"verifyName":"张锐"},"code":"000000"}
	 * @param birdUser
	 * @return
	 */
	private JSONObject taskList(BirdUser birdUser) {
		Random random = new Random();

		String response = null;

		// 公共配置
		String protocol = this.getBirdParam("protocol");
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");

		// 业务配置
		// /task/getPersonalTaskList
		String path = this.getBirdParam("tasklist.path");
		String method = this.getBirdParam("tasklist.method");
		// ?contractNo=${contractNo:encry}&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		String paramTemp = this.getBirdParam("tasklist.paramTemp");

		try {
			// 用户信息
			String addresses = birdUser.getAddresses();
			String latitudeStr = birdUser.getLatitude();// 0.000150
			String longitudeStr = birdUser.getLongitude();// 0.000700
			String project = birdUser.getProject();
			String token = birdUser.getTokenStr();
			

			Map<String, String> param = this.getTasklistRequstParamMap(random, appVersion, addresses, latitudeStr, longitudeStr,project, token);
			
			String paramStr = createRequest(param,paramTemp);

			response = sendRequest(protocol, domain, path, method, paramStr);

			JSONObject jsonObject = JSON.parseObject(response);

			if ("000000".equals(jsonObject.getString("code"))) {
				log.info("taskList success!");
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】查询用户个人信息，用户：【" + birdUser.getName() + "】成功");
				iBirdUserLogService.save(entity);
			} else {
				log.error("taskList error! {}", response);
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】查询用户个人信息，用户：【" + birdUser.getName() + "】失败");
				iBirdUserLogService.save(entity);
			}

		} catch (Exception e) {
			BirdUserLog entity = new BirdUserLog();
			entity.setBirdUserId(birdUser.getId());
			entity.setLogMsg("【定时】查询用户个人信息，用户：【" + birdUser.getName() + "】失败");
			iBirdUserLogService.save(entity);
			throw new RuntimeException(e);
		}
		
		
		JSONObject jsonObject1 = JSON.parseObject(response);
		
		return jsonObject1;
	}
	
	
	// GET /approval/getApproveMembers?token=cYpWndRpfb4IXWgMYCRSwry7xs9o17IwraKge1vjY6mWq%252BgUmwvxfJVWMPZp0r4acs2BGLOr/79pHizWBZPJS%252BD7hVCt1NYpNt4xQjH8Qb27lDE7Zr/%252BCFZfS%252B9a4EF6keMcT9ZlaymIIaHXWvBuQ3zxCXSWUmqihP5oKt9QzdSczgfa5Uh1w2UigsWk0QWB&appVersion=E8BKqXovdzOyN/6r4W3bhQ==&timestampWithRandomValue=1586434460315with0.44527704141899493 HTTP/1.1
	// /approval/getApproveMembers?token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
	// {"code":"000000","msg":"请求成功","data":{"approvemployeeId":"00000154","approveList":[{"employeeId":"00000103","employeeName":"范亚坤","imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483007049116.jpg","totalPoint":null,"totalPointStr":null,"busyGrade":null,"busyGradeStr":null,"dayCompleteScore":null,"dayCompleteScoreStr":null,"dayQualityScore":null,"dayQualityScoreStr":null,"unresolverBugCount":0,"approveFlag":"1","exitTask":"0","remark":null},{"employeeId":"00000422","employeeName":"刘爽","imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483074792859.JPG","totalPoint":null,"totalPointStr":null,"busyGrade":null,"busyGradeStr":null,"dayCompleteScore":null,"dayCompleteScoreStr":null,"dayQualityScore":null,"dayQualityScoreStr":null,"unresolverBugCount":0,"approveFlag":"1","exitTask":"0","remark":null},{"employeeId":"00000490","employeeName":"田立志","imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483074141976.jpg","totalPoint":null,"totalPointStr":null,"busyGrade":null,"busyGradeStr":null,"dayCompleteScore":null,"dayCompleteScoreStr":null,"dayQualityScore":null,"dayQualityScoreStr":null,"unresolverBugCount":0,"approveFlag":"1","exitTask":"0","remark":null},{"employeeId":"00000163","employeeName":"李志伟","imageId":"http://bird2.i2soft.net:82/images/3/00000202/1483092484897.jpg","totalPoint":null,"totalPointStr":null,"busyGrade":null,"busyGradeStr":null,"dayCompleteScore":null,"dayCompleteScoreStr":null,"dayQualityScore":null,"dayQualityScoreStr":null,"unresolverBugCount":0,"approveFlag":"1","exitTask":"0","remark":null},{"employeeId":"00003318","employeeName":"张剑","imageId":"http://bird2.i2soft.net:82/images/2/00000205/1575013380747.png","totalPoint":null,"totalPointStr":null,"busyGrade":null,"busyGradeStr":null,"dayCompleteScore":null,"dayCompleteScoreStr":null,"dayQualityScore":null,"dayQualityScoreStr":null,"unresolverBugCount":0,"approveFlag":"1","exitTask":"0","remark":null}],"leaves":[],"employeeQuit":[]}}
	private List<String> employeeList(BirdUser birdUser) throws Exception {
		Random random = new Random();
		String response;
		// 公共配置
		String protocol = this.getBirdParam("protocol");
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");

		// 业务配置
		// /approval/getApproveMembers
		String path = this.getBirdParam("employeelist.path");
		String method = this.getBirdParam("employeelist.method","GET");
		// GET /approval/getApproveMembers
		// ?token=${token:encry}&appVersion=${appVersion:encry}
		// ?token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		String paramTemp = this.getBirdParam("employeelist.paramTemp");
		
		String token = birdUser.getTokenStr();

		try {
			// 用户信息
			Map<String, String> param = this.getEmployeelistRequstParamMap(random, token, appVersion);
			
			String paramStr = createRequest(param,paramTemp);
			
			response = sendRequest(protocol, domain, path, method, paramStr);
			
			JSONObject jsonObject = JSON.parseObject(response);
			if ("000000".equals(jsonObject.getString("code"))) {
				log.info("Employeelist success!");
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】查看被审批人列表，用户：【" + birdUser.getName() + "】成功");
				iBirdUserLogService.save(entity);
				JSONArray empolyeeList = jsonObject.getJSONObject("data").getJSONArray("approveList");
				List<String> employees = new ArrayList<String>();
				for (int i = 0; i < empolyeeList.size(); i++) {
					JSONObject json = empolyeeList.getJSONObject(i);
					String employeeId = json.getString("employeeId");
					employees.add(employeeId);
				}
				return employees;
			} else {
				log.error("Employeelist error! {}", response);
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】查看被审批人列表，用户：【" + birdUser.getName() + "】失败");
				iBirdUserLogService.save(entity);
				throw new RuntimeException("Employeelist error!");
			}
		} catch (Exception e) {
			BirdUserLog entity = new BirdUserLog();
			entity.setBirdUserId(birdUser.getId());
			entity.setLogMsg("【定时】查看被审批人列表，用户：【" + birdUser.getName() + "】失败");
			iBirdUserLogService.save(entity);
			throw new RuntimeException("Employeelist error!",e);
		}
	}

	// GET
	// /approval/getEmployeeTaskList?subEmployeeId=yiBwBx/aYEiKaFhc3q8BMw==&token=cYpWndRpfb4IXWgMYCRSwry7xs9o17IwraKge1vjY6mWq%252BgUmwvxfJVWMPZp0r4acs2BGLOr/79pHizWBZPJS%252BD7hVCt1NYpNt4xQjH8Qb14NrKLxJ1Aiw5gAThhmFlS6Fl/bMcUOsMeDKjCcq5JeJNeiZxrkY6qoJ2FX7RI07OnLdWnsednEiAkM13Oww9o&appVersion=BC1eg8LGq2VW02JHYRcaEw==&timestampWithRandomValue=1578475048676with0.26895048628466334
	// HTTP/1.1
	// GET /approval/getEmployeeTaskList?subEmployeeId=${subEmployeeId:encry}&defaultNum=${defaultNum:encry}&token=${token:encry}&appVersion=${appversion:encry}&timestampWithRandomValue=${timestampWithRandomValue} HTTP/1.1
	// {"code":"000000","msg":"请求成功","data":{"signOutFlag":"1","yesterdayTotalInWork":13,"todaySurplusTotalInWork":0,"C":10,"G":432,"H":0,"untaskTargets":[{"taskId":"2020010802064264","employeeId":"00000490","contractNo":"PRO00000463","targetName":"跟进小象需求进度","targetDesc":"
	// ","completeScore":null,"qualityScore":null,"createTime":"2020-01-08T00:12:15.000+0000","beginTime":"2020-01-08T00:14:00.000+0000","endTime":"2020-01-08T10:00:00.000+0000","status":"1","verifyId":"00000154","taskType":"2","completeTime":null,"verifyName":"张甫","remark":null,"vremark":null,"responsibleEmpName":null,"subTaskCount":null,"assignedId":null,"resultDetail":null,"inwork":null,"budget":null,"inworkVerify":null},{"taskId":"2020010802064272","employeeId":"00000490","contractNo":"PRO00000463","targetName":"整理全面预算","targetDesc":"
	// ","completeScore":null,"qualityScore":null,"createTime":"2020-01-08T00:12:32.000+0000","beginTime":"2020-01-08T00:14:00.000+0000","endTime":"2020-01-08T10:00:00.000+0000","status":"1","verifyId":"00000154","taskType":"2","completeTime":null,"verifyName":"张甫","remark":null,"vremark":null,"responsibleEmpName":null,"subTaskCount":null,"assignedId":null,"resultDetail":null,"inwork":null,"budget":null,"inworkVerify":null},{"taskId":"2020010802064350","employeeId":"00000490","contractNo":"PRO00000463","targetName":"营销平台代码审核","targetDesc":"
	// ","completeScore":null,"qualityScore":null,"createTime":"2020-01-08T00:14:02.000+0000","beginTime":"2020-01-08T00:14:00.000+0000","endTime":"2020-01-08T10:00:00.000+0000","status":"1","verifyId":"00000154","taskType":"2","completeTime":null,"verifyName":"张甫","remark":null,"vremark":null,"responsibleEmpName":null,"subTaskCount":null,"assignedId":null,"resultDetail":null,"inwork":null,"budget":null,"inworkVerify":null}],"budgetary":11342899.323,"employeeImage":"http://bird2.i2soft.net:82/images/3/00000202/1483074141976.jpg","S":2399.14,"organFlag":0,"dayApproveVo":{"employeeId":null,"employeeName":null,"imageId":null,"totalPoint":null,"totalPointStr":null,"busyGrade":null,"busyGradeStr":null,"dayCompleteScore":null,"dayCompleteScoreStr":null,"dayQualityScore":null,"dayQualityScoreStr":null,"unresolverBugCount":0,"approveFlag":"0","exitTask":null,"remark":null},"Z":0,"yesterdaySurplusTotalInWork":13,"departmentName":"第六软件中心","employeeName":"田立志","DYY":18,"todayTotalInWork":0,"taskTargets":[],"approveLeaveList":[],"employeeId":"00000490","signInLocal":"北京市海淀区太平庄中街1号中国邮政储蓄银行(富力桃园营业所)","approvalBudgetsList":[{"contractNo":"PRO00000463","projectName":"工商银行电子商务平台B2B商城2019年项目","proBudget":null,"usedBudget":null}],"signInTime":"2020-01-08
	// 08:24:54","showButton":"0","lastDayUnTaskTargets":[]}}
	private List<String> employeeTasks(BirdUser birdUser, String employeeId) throws Exception {
		Random random = new Random();
		String response;
		// 公共配置
		String protocol = this.getBirdParam("protocol");
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");

		// 业务配置
		// /approval/getEmployeeTaskList
		String path = this.getBirdParam("employeetasks.path");
		// GET
		String method = this.getBirdParam("employeetasks.method","GET");
		// ?subEmployeeId=${subEmployeeId:encry}&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		// /approval/getEmployeeTaskList?subEmployeeId=${subEmployeeId:encry}&defaultNum=${defaultNum:encry}&token=${token:encry}&appVersion=${appversion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
		String paramTemp = this.getBirdParam("employeetasks.paramTemp");
		List<String> tasks = new ArrayList<String>();
		try {
			// 用户信息
			String token = birdUser.getTokenStr();
			String defaultNum = "1";
			Map<String, String> param = this.getEmployeeTasksRequstParamMap(random, employeeId,defaultNum, token, appVersion);
			
			String paramStr = createRequest(param,paramTemp);

			response = sendRequest(protocol, domain, path, method, paramStr);

			JSONObject jsonObject = JSON.parseObject(response);
			if ("000000".equals(jsonObject.getString("code"))) {
				log.info("employeeTasks success!");
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】查看被审批人任务列表，用户：【" + birdUser.getName() + "】成功");
				iBirdUserLogService.save(entity);
				JSONArray taskTargets = jsonObject.getJSONObject("data").getJSONArray("taskTargets");
				
				for (int i = 0; i < taskTargets.size(); i++) {
					JSONObject json = taskTargets.getJSONObject(i);
					String taskId = json.getString("taskId");
					// 只加入未审批的任务
					if("2".equals(json.getString("status"))){	
						tasks.add(taskId);
						log.info("employeeTasks:{} has been added.",taskId);
					} else {
						log.info("employeeTasks:{} has been approved.",taskId);
					}
				}
			} else {
				log.error("employeeTasks error! {}", response);
				throw new RuntimeException("employeeTasks error!");
			}

		} catch (Exception e) {
			log.error("employeeTasks error!", e);
			BirdUserLog entity = new BirdUserLog();
			entity.setBirdUserId(birdUser.getId());
			entity.setLogMsg("【定时】查看被审批人任务列表，用户：【" + birdUser.getName() + "】失败");
			iBirdUserLogService.save(entity);
		}
		return tasks;
	}

	private void doApprovalTasks(BirdUser birdUser, String employeeId, List<String> tasks) throws Exception {
		Random random = new Random();
		String response = null;
		// 公共配置
		String protocol = this.getBirdParam("protocol");
		String appVersion = this.getBirdParam("appVersion");
		String domain = this.getBirdParam("domain");
		
		String finalTaskId = null;
		for (String taskId : tasks) {
			finalTaskId = taskId;
			log.info("EmployeeId:{},TaskId:{}", employeeId, taskId);
			// 业务配置
			// /approval/approveTask
			String path = this.getBirdParam("employeetask.path");
			// GET
			String method = this.getBirdParam("employeetask.method","GET");
			
			// ?taskId=${taskId:encry}&completeScore=${completeScore:encry}&qualityScore=${qualityScore:encry}&token=${token:encry}&timestampWithRandomValue=${timestampWithRandomValue}
			String paramTemp = this.getBirdParam("employeetask.paramTemp");

			String completeScore = this.getBirdParam("employeetask.completeScore");
			String qualityScore = this.getBirdParam("employeetask.qualityScore");
			try {
				// 用户信息
				String token = birdUser.getTokenStr();
				Map<String, String> param = this.getApprovalTaskRequstParamMap(random,taskId,completeScore,
						qualityScore, token, appVersion);
				
				String paramStr = createRequest(param,paramTemp);

				response = sendRequest(protocol, domain, path, method, paramStr);

				JSONObject jsonObject = JSON.parseObject(response);
				if ("000000".equals(jsonObject.getString("code"))) {
					log.info("approveTask success!");
					BirdUserLog entity = new BirdUserLog();
					entity.setBirdUserId(birdUser.getId());
					entity.setLogMsg("【定时】审批被审批人任务：【"+ taskId + "】，用户：【" + birdUser.getName() + "】成功");
					iBirdUserLogService.save(entity);
				} else {
					log.error("approveTask error! {}", response);
					BirdUserLog entity = new BirdUserLog();
					entity.setBirdUserId(birdUser.getId());
					entity.setLogMsg("【定时】审批被审批人任务：【" + taskId + "】，用户：【" + birdUser.getName() + "】失败");
					iBirdUserLogService.save(entity);
					log.error("Continue next task");
					continue;
				}
			} catch (Exception e) {
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】审批被审批人任务，用户：【" + birdUser.getName() + "】失败");
				iBirdUserLogService.save(entity);
			}
		}

		// 提交综合得分------------------------------------------------------------------------------------------------------------
		try {
			// /approval/dayApprove
			String path = this.getBirdParam("employeebusy.path");
			String method = this.getBirdParam("employeebusy.method","GET");
			// ?subEmployeeId=${subEmployeeId:encry}&dayCompleteScore=${dayCompleteScore:encry}&dayQualityScore=${dayQualityScore:encry}&unresolverBugCount=${unresolverBugCount}&remark=${remark:encry}&totalPoint=${totalPoint:encry}&taskId=${taskId:encry}&busyGrade=${busyGrade:encry}&approvalFlag=${approvalFlag}&token=${token:encry}&appVersion=${appVersion:encry}&timestampWithRandomValue=${timestampWithRandomValue}
			String paramTemp = this.getBirdParam("employeebusy.paramTemp");
			
			String dayCompleteScore = this.getBirdParam("employeebusy.dayCompleteScore");
			String dayQualityScore = this.getBirdParam("employeebusy.dayQualityScore");
			String totalPoint = this.getBirdParam("employeebusy.totalPoint");
			String busyGrade = this.getBirdParam("employeebusy.busyGrade");

			// 用户信息
			String token = birdUser.getTokenStr();
			Map<String, String> param = this.getDayApproveRequstParamMap(random, token, appVersion, employeeId,
					dayCompleteScore, dayQualityScore, totalPoint, finalTaskId, busyGrade);

			String paramStr = createRequest(param,paramTemp);

			response = sendRequest(protocol, domain, path, method, paramStr);

			JSONObject jsonObject = JSON.parseObject(response);
			if ("000000".equals(jsonObject.getString("code"))) {
				log.info("dayApprove success!");
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】审批被审批人综合评分，用户：【" + birdUser.getName() + "】成功");
				iBirdUserLogService.save(entity);
			} else {
				log.error("dayApprove error! {}", response);
				BirdUserLog entity = new BirdUserLog();
				entity.setBirdUserId(birdUser.getId());
				entity.setLogMsg("【定时】审批被审批人综合评分，用户：【" + birdUser.getName() + "】失败");
				iBirdUserLogService.save(entity);
			}

		} catch (Exception e) {
			BirdUserLog entity = new BirdUserLog();
			entity.setBirdUserId(birdUser.getId());
			entity.setLogMsg("【定时】审批被审批人综合评分，用户：【" + birdUser.getName() + "】失败");
			iBirdUserLogService.save(entity);
		}

	}
	
	// 构建请求参数
	private String createRequest(Map<String, String> param,String paramTemp) throws Exception {
		Map<String, String> paramEncry = new HashMap<>();
		for (String key : param.keySet()) {
			String value = param.get(key);
			log.info("                                     ");
			log.info("request param [{}] : [{}]", key, value);
			String value2 = AESUtil.encrypt(value);// 使用 进行填充，所有字符串不得含有字符  （\u0000）
			paramEncry.put(key, value2);
			log.info("encrypted request param [{}] : [{}]", key, value2);
			log.info("decrypt test encrypted request param [{}] : [{}]", key, AESUtil.decrypt(value2));
		}

		// 添加timestampWithRandomValue
		String paramEncryStr = paramTemp;
		String paramPlainStr = paramTemp;
		for (String key : param.keySet()) {
			String replaceKey = "\\$\\{" + key + "}";
			String replaceKey2 = "\\$\\{" + key + ":encry}";

			paramPlainStr = paramPlainStr.replaceAll(replaceKey, encodeURIComponent(param.get(key)));
			paramPlainStr = paramPlainStr.replaceAll(replaceKey2, encodeURIComponent(param.get(key)));
			
			paramEncryStr = paramEncryStr.replaceAll(replaceKey, encodeURIComponent(param.get(key)));
			paramEncryStr = paramEncryStr.replaceAll(replaceKey2, encodeURIComponent(paramEncry.get(key)));
		}
		log.info("                                 ");
		log.info("***plain string：{}",paramPlainStr);
		return paramEncryStr;
	}
	
	private String sendRequest(String protocol, String domain, String path, String method, String paramStr)
			throws Exception {
		String response;
		String url = protocol + "://" + domain + path;
		log.info("***request url：{},with method:{}", url, method);
		log.info("***request params：{}", paramStr);
		response = HttpUtil.request(url, domain, paramStr, method);
		log.info("***response body：{}", response);
		return response;
	}
	
	
}
