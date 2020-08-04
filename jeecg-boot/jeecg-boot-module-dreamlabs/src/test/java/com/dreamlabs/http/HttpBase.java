package com.dreamlabs.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpBase {
	
	
	/**
	 * http://39.106.132.214:8080/jeecg-boot/ Result(success=true, message=登录成功,
	 * code=200,
	 * result={"multi_depart":1,"userInfo":{"activitiSync":1,"birthday":400608000000,"createBy":"zhagnxiao","createTime":1593754358000,"delFlag":0,"departIds":"","email":"46886799@163.com","id":"bd0a2a2bbdbc4bd89a97182951ccbb4e","orgCode":"A02A02","password":"a6fb715449ba64d2","phone":"18600574553","post":"devleader","realname":"张甫","salt":"zfIJdiyP","sex":1,"status":1,"telephone":"","updateBy":"zhagnxiao","updateTime":1593754438000,"userIdentity":1,"username":"zhangfu","workNo":"A101"},"sysAllDictItems":{"ol_form_biz_type":[{"text":"官方示例","title":"官方示例","value":"demo"},{"text":"流程表单","title":"流程表单","value":"bpm"},{"text":"测试表单","title":"测试表单","value":"temp"},{"text":"导入表单","title":"导入表单","value":"bdfl_include"}],"account_type":[{"text":"股票账户","title":"股票账户","value":"1"},{"text":"基金账户","title":"基金账户","value":"2"},{"text":"期货账户","title":"期货账户","value":"3"}],"msgType":[{"text":"系统","title":"系统","value":"4"},{"text":"短信","title":"短信","value":"1"},{"text":"邮件","title":"邮件","value":"2"},{"text":"微信","title":"微信","value":"3"}],"eoa_plan_status":[{"text":"未开始","title":"未开始","value":"0"},{"text":"进行中","title":"进行中","value":"1"},{"text":"已完成","title":"已完成","value":"2"}],"position_rank":[{"text":"员级","title":"员级","value":"1"},{"text":"助级","title":"助级","value":"2"},{"text":"中级","title":"中级","value":"3"},{"text":"副高级","title":"副高级","value":"4"},{"text":"正高级","title":"正高级","value":"5"}],"rule_conditions":[{"text":"大于","title":"大于","value":">"},{"text":"小于","title":"小于","value":"<"},{"text":"不等于","title":"不等于","value":"!="},{"text":"等于","title":"等于","value":"="},{"text":"大于等于","title":"大于等于","value":">="},{"text":"小于等于","title":"小于等于","value":"<="},{"text":"左模糊","title":"左模糊","value":"LEFT_LIKE"},{"text":"右模糊","title":"右模糊","value":"RIGHT_LIKE"},{"text":"模糊","title":"模糊","value":"LIKE"},{"text":"包含","title":"包含","value":"IN"},{"text":"自定义SQL表达式","title":"自定义SQL表达式","value":"USE_SQL_RULES"}],"online_graph_data_type":[{"text":"SQL","title":"SQL","value":"sql"},{"text":"JSON","title":"JSON","value":"json"}],"database_type":[{"text":"MySQL","title":"MySQL","value":"1"},{"text":"Oracle","title":"Oracle","value":"2"},{"text":"SQLServer","title":"SQLServer","value":"3"}],"online_graph_display_template":[{"text":"Tab风格","title":"Tab风格","value":"tab"},{"text":"单排布局","title":"单排布局","value":"single"},{"text":"双排布局","title":"双排布局","value":"double"},{"text":"组合布局","title":"组合布局","value":"combination"}],"log_type":[{"text":"操作日志","title":"操作日志","value":"2"},{"text":"登录日志","title":"登录日志","value":"1"}],"send_status":[{"text":"未发布","title":"未发布","value":"0"},{"text":"已发布","title":"已发布","value":"1"},{"text":"已撤销","title":"已撤销","value":"2"}],"eoa_cms_menu_type":[{"text":"列表","title":"列表","value":"1"},{"text":"链接","title":"链接","value":"2"}],"bpm_process_type":[{"text":"测试流程","title":"测试流程","value":"test"},{"text":"OA办公","title":"OA办公","value":"oa"},{"text":"业务办理","title":"业务办理","value":"business"}],"form_perms_type":[{"text":"可见(未授权不可见)","title":"可见(未授权不可见)","value":"1"},{"text":"可编辑(未授权禁用)","title":"可编辑(未授权禁用)","value":"2"}],"valid_status":[{"text":"无效","title":"无效","value":"0"},{"text":"有效","title":"有效","value":"1"}],"urgent_level":[{"text":"一般","title":"一般","value":"1"},{"text":"重要","title":"重要","value":"2"},{"text":"紧急","title":"紧急","value":"3"}],"del_flag":[{"text":"已删除","title":"已删除","value":"1"},{"text":"正常","title":"正常","value":"0"}],"user_status":[{"text":"正常","title":"正常","value":"1"},{"text":"冻结","title":"冻结","value":"2"}],"operate_type":[{"text":"查询","title":"查询","value":"1"},{"text":"添加","title":"添加","value":"2"},{"text":"修改","title":"修改","value":"3"},{"text":"删除","title":"删除","value":"4"},{"text":"导入","title":"导入","value":"5"},{"text":"导出","title":"导出","value":"6"}],"quartz_status":[{"text":"正常","title":"正常","value":"0"},{"text":"停止","title":"停止","value":"-1"}],"cgform_table_type":[{"text":"单表","title":"单表","value":"1"},{"text":"主表","title":"主表","value":"2"},{"text":"附表","title":"附表","value":"3"}],"menu_type":[{"text":"按钮权限","title":"按钮权限","value":"2"},{"text":"子菜单","title":"子菜单","value":"1"},{"text":"一级菜单","title":"一级菜单","value":"0"}],"sex":[{"text":"男","title":"男","value":"1"},{"text":"女","title":"女","value":"2"}],"msg_category":[{"text":"通知公告","title":"通知公告","value":"1"},{"text":"系统消息","title":"系统消息","value":"2"}],"org_type":[{"text":"银行","title":"银行","value":"1"},{"text":"券商","title":"券商","value":"2"},{"text":"基金","title":"基金","value":"3"},{"text":"保险","title":"保险","value":"4"},{"text":"综合","title":"综合","value":"9"}],"org_category":[{"text":"岗位","title":"岗位","value":"3"},{"text":"公司","title":"公司","value":"1"},{"text":"部门","title":"部门","value":"2"}],"perms_type":[{"text":"显示","title":"显示","value":"1"},{"text":"禁用","title":"禁用","value":"2"}],"global_perms_type":[{"text":"可见/可访问(授权后可见/可访问)","title":"可见/可访问(授权后可见/可访问)","value":"1"},{"text":"可编辑(未授权时禁用)","title":"可编辑(未授权时禁用)","value":"2"}],"priority":[{"text":"高","title":"高","value":"H"},{"text":"中","title":"中","value":"M"},{"text":"低","title":"低","value":"L"}],"dict_item_status":[{"text":"启用","title":"启用","value":"1"},{"text":"不启用","title":"不启用","value":"0"}],"online_graph_type":[{"text":"柱状图","title":"柱状图","value":"bar"},{"text":"曲线图","title":"曲线图","value":"line"},{"text":"饼图","title":"饼图","value":"pie"},{"text":"数据列表","title":"数据列表","value":"table"}],"activiti_sync":[{"text":"同步","title":"同步","value":"1"},{"text":"不同步","title":"不同步","value":"0"}],"msgSendStatus":[{"text":"未发送","title":"未发送","value":"0"},{"text":"发送成功","title":"发送成功","value":"1"},{"text":"发送失败","title":"发送失败","value":"2"}],"msg_type":[{"text":"指定用户","title":"指定用户","value":"USER"},{"text":"全体用户","title":"全体用户","value":"ALL"}],"bpm_status":[{"text":"待提交","title":"待提交","value":"1"},{"text":"处理中","title":"处理中","value":"2"},{"text":"已完成","title":"已完成","value":"3"},{"text":"已作废","title":"已作废","value":"4"}],"eoa_plan_type":[{"text":"日常记录","title":"日常记录","value":"1"},{"text":"本周工作","title":"本周工作","value":"2"},{"text":"下周计划","title":"下周计划","value":"3"}],"depart_status":[{"text":"不启用","title":"不启用","value":"0"},{"text":"启用","title":"启用","value":"1"}]},"departs":[{"createBy":"admin","createTime":1551170678000,"delFlag":"0","departName":"研发部","departOrder":0,"id":"5159cde220114246b045e574adceafe9","orgCategory":"1","orgCode":"A02A02","orgType":"2","parentId":"6d35e179cd814e3299bd588ea7daed3f","updateBy":"admin","updateTime":1551922613000}],"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTYxOTI2MDgsInVzZXJuYW1lIjoiemhhbmdmdSJ9.vxEOXn5RRVGppzJwZjMKzf8msS5n66WlGaWPO8dnnd0"},
	 * timestamp=1595760572595)
	 * 
	 * @throws Exception
	 */
	@Test
	public void jeecg_login() throws Exception {
		HttpRequest request = HttpUtil.createPost("http://localhost:8080/jeecg-boot/sys/login");
		String body = "{\"captcha\":\"-1\",\"checkKey\":\"-1\",\"password\":\"frank@bj828100\",\"username\":\"zhangfu\"}";
		request = request.body(body);
		HttpResponse response = request.execute();
		log.info(response.headers().toString());
		String bodyString = response.body();
		log.info(bodyString);
		JSONObject jb = JSONUtil.parseObj(bodyString);
		token = (String) jb.getByPath("result.token");
		System.out.println("====================================");
		System.out.println(jb.getByPath("result.token"));
		System.out.println("====================================");
	}
	
	private String token = null;
	
	/**
	 * http://39.106.132.214:8080/jeecg-boot/ Result(success=true, message=登录成功,
	 * code=200,
	 * result={"multi_depart":1,"userInfo":{"activitiSync":1,"birthday":400608000000,"createBy":"zhagnxiao","createTime":1593754358000,"delFlag":0,"departIds":"","email":"46886799@163.com","id":"bd0a2a2bbdbc4bd89a97182951ccbb4e","orgCode":"A02A02","password":"a6fb715449ba64d2","phone":"18600574553","post":"devleader","realname":"张甫","salt":"zfIJdiyP","sex":1,"status":1,"telephone":"","updateBy":"zhagnxiao","updateTime":1593754438000,"userIdentity":1,"username":"zhangfu","workNo":"A101"},"sysAllDictItems":{"ol_form_biz_type":[{"text":"官方示例","title":"官方示例","value":"demo"},{"text":"流程表单","title":"流程表单","value":"bpm"},{"text":"测试表单","title":"测试表单","value":"temp"},{"text":"导入表单","title":"导入表单","value":"bdfl_include"}],"account_type":[{"text":"股票账户","title":"股票账户","value":"1"},{"text":"基金账户","title":"基金账户","value":"2"},{"text":"期货账户","title":"期货账户","value":"3"}],"msgType":[{"text":"系统","title":"系统","value":"4"},{"text":"短信","title":"短信","value":"1"},{"text":"邮件","title":"邮件","value":"2"},{"text":"微信","title":"微信","value":"3"}],"eoa_plan_status":[{"text":"未开始","title":"未开始","value":"0"},{"text":"进行中","title":"进行中","value":"1"},{"text":"已完成","title":"已完成","value":"2"}],"position_rank":[{"text":"员级","title":"员级","value":"1"},{"text":"助级","title":"助级","value":"2"},{"text":"中级","title":"中级","value":"3"},{"text":"副高级","title":"副高级","value":"4"},{"text":"正高级","title":"正高级","value":"5"}],"rule_conditions":[{"text":"大于","title":"大于","value":">"},{"text":"小于","title":"小于","value":"<"},{"text":"不等于","title":"不等于","value":"!="},{"text":"等于","title":"等于","value":"="},{"text":"大于等于","title":"大于等于","value":">="},{"text":"小于等于","title":"小于等于","value":"<="},{"text":"左模糊","title":"左模糊","value":"LEFT_LIKE"},{"text":"右模糊","title":"右模糊","value":"RIGHT_LIKE"},{"text":"模糊","title":"模糊","value":"LIKE"},{"text":"包含","title":"包含","value":"IN"},{"text":"自定义SQL表达式","title":"自定义SQL表达式","value":"USE_SQL_RULES"}],"online_graph_data_type":[{"text":"SQL","title":"SQL","value":"sql"},{"text":"JSON","title":"JSON","value":"json"}],"database_type":[{"text":"MySQL","title":"MySQL","value":"1"},{"text":"Oracle","title":"Oracle","value":"2"},{"text":"SQLServer","title":"SQLServer","value":"3"}],"online_graph_display_template":[{"text":"Tab风格","title":"Tab风格","value":"tab"},{"text":"单排布局","title":"单排布局","value":"single"},{"text":"双排布局","title":"双排布局","value":"double"},{"text":"组合布局","title":"组合布局","value":"combination"}],"log_type":[{"text":"操作日志","title":"操作日志","value":"2"},{"text":"登录日志","title":"登录日志","value":"1"}],"send_status":[{"text":"未发布","title":"未发布","value":"0"},{"text":"已发布","title":"已发布","value":"1"},{"text":"已撤销","title":"已撤销","value":"2"}],"eoa_cms_menu_type":[{"text":"列表","title":"列表","value":"1"},{"text":"链接","title":"链接","value":"2"}],"bpm_process_type":[{"text":"测试流程","title":"测试流程","value":"test"},{"text":"OA办公","title":"OA办公","value":"oa"},{"text":"业务办理","title":"业务办理","value":"business"}],"form_perms_type":[{"text":"可见(未授权不可见)","title":"可见(未授权不可见)","value":"1"},{"text":"可编辑(未授权禁用)","title":"可编辑(未授权禁用)","value":"2"}],"valid_status":[{"text":"无效","title":"无效","value":"0"},{"text":"有效","title":"有效","value":"1"}],"urgent_level":[{"text":"一般","title":"一般","value":"1"},{"text":"重要","title":"重要","value":"2"},{"text":"紧急","title":"紧急","value":"3"}],"del_flag":[{"text":"已删除","title":"已删除","value":"1"},{"text":"正常","title":"正常","value":"0"}],"user_status":[{"text":"正常","title":"正常","value":"1"},{"text":"冻结","title":"冻结","value":"2"}],"operate_type":[{"text":"查询","title":"查询","value":"1"},{"text":"添加","title":"添加","value":"2"},{"text":"修改","title":"修改","value":"3"},{"text":"删除","title":"删除","value":"4"},{"text":"导入","title":"导入","value":"5"},{"text":"导出","title":"导出","value":"6"}],"quartz_status":[{"text":"正常","title":"正常","value":"0"},{"text":"停止","title":"停止","value":"-1"}],"cgform_table_type":[{"text":"单表","title":"单表","value":"1"},{"text":"主表","title":"主表","value":"2"},{"text":"附表","title":"附表","value":"3"}],"menu_type":[{"text":"按钮权限","title":"按钮权限","value":"2"},{"text":"子菜单","title":"子菜单","value":"1"},{"text":"一级菜单","title":"一级菜单","value":"0"}],"sex":[{"text":"男","title":"男","value":"1"},{"text":"女","title":"女","value":"2"}],"msg_category":[{"text":"通知公告","title":"通知公告","value":"1"},{"text":"系统消息","title":"系统消息","value":"2"}],"org_type":[{"text":"银行","title":"银行","value":"1"},{"text":"券商","title":"券商","value":"2"},{"text":"基金","title":"基金","value":"3"},{"text":"保险","title":"保险","value":"4"},{"text":"综合","title":"综合","value":"9"}],"org_category":[{"text":"岗位","title":"岗位","value":"3"},{"text":"公司","title":"公司","value":"1"},{"text":"部门","title":"部门","value":"2"}],"perms_type":[{"text":"显示","title":"显示","value":"1"},{"text":"禁用","title":"禁用","value":"2"}],"global_perms_type":[{"text":"可见/可访问(授权后可见/可访问)","title":"可见/可访问(授权后可见/可访问)","value":"1"},{"text":"可编辑(未授权时禁用)","title":"可编辑(未授权时禁用)","value":"2"}],"priority":[{"text":"高","title":"高","value":"H"},{"text":"中","title":"中","value":"M"},{"text":"低","title":"低","value":"L"}],"dict_item_status":[{"text":"启用","title":"启用","value":"1"},{"text":"不启用","title":"不启用","value":"0"}],"online_graph_type":[{"text":"柱状图","title":"柱状图","value":"bar"},{"text":"曲线图","title":"曲线图","value":"line"},{"text":"饼图","title":"饼图","value":"pie"},{"text":"数据列表","title":"数据列表","value":"table"}],"activiti_sync":[{"text":"同步","title":"同步","value":"1"},{"text":"不同步","title":"不同步","value":"0"}],"msgSendStatus":[{"text":"未发送","title":"未发送","value":"0"},{"text":"发送成功","title":"发送成功","value":"1"},{"text":"发送失败","title":"发送失败","value":"2"}],"msg_type":[{"text":"指定用户","title":"指定用户","value":"USER"},{"text":"全体用户","title":"全体用户","value":"ALL"}],"bpm_status":[{"text":"待提交","title":"待提交","value":"1"},{"text":"处理中","title":"处理中","value":"2"},{"text":"已完成","title":"已完成","value":"3"},{"text":"已作废","title":"已作废","value":"4"}],"eoa_plan_type":[{"text":"日常记录","title":"日常记录","value":"1"},{"text":"本周工作","title":"本周工作","value":"2"},{"text":"下周计划","title":"下周计划","value":"3"}],"depart_status":[{"text":"不启用","title":"不启用","value":"0"},{"text":"启用","title":"启用","value":"1"}]},"departs":[{"createBy":"admin","createTime":1551170678000,"delFlag":"0","departName":"研发部","departOrder":0,"id":"5159cde220114246b045e574adceafe9","orgCategory":"1","orgCode":"A02A02","orgType":"2","parentId":"6d35e179cd814e3299bd588ea7daed3f","updateBy":"admin","updateTime":1551922613000}],"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTYxOTI2MDgsInVzZXJuYW1lIjoiemhhbmdmdSJ9.vxEOXn5RRVGppzJwZjMKzf8msS5n66WlGaWPO8dnnd0"},
	 * timestamp=1595760572595)
	 * 
	 * @throws Exception
	 */
	@Test
	public void jeecg_logout() throws Exception {
		
		HttpRequest request = HttpUtil.createPost("http://localhost:8080/jeecg-boot/sys/login");
		String body = "{\"captcha\":\"-1\",\"checkKey\":\"-1\",\"password\":\"frank@bj828100\",\"username\":\"zhangfu\"}";
		request = request.body(body);
		HttpResponse response = request.execute();
		log.info(response.headers().toString());
		String bodyString = response.body();
		log.info(bodyString);
		JSONObject jb = JSONUtil.parseObj(bodyString);
		token = (String) jb.getByPath("result.token");
		System.out.println("====================================");
		System.out.println(jb.getByPath("result.token"));
		System.out.println("====================================");
		
		
		HttpRequest request_logout = HttpUtil.createGet("http://localhost:8080/jeecg-boot/sys/logout");
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("X-Access-Token", token);
		request_logout = request_logout.addHeaders(headers);
		HttpResponse response_logout = request_logout.execute();
		log.info(response_logout.headers().toString());
		String bodyString_logout = response_logout.body();
		log.info(bodyString_logout);
		JSONObject jb_logout = JSONUtil.parseObj(bodyString_logout);
		System.out.println("====================================");
		System.out.println(jb_logout.getStr("code"));
		System.out.println("====================================");
	}

}
