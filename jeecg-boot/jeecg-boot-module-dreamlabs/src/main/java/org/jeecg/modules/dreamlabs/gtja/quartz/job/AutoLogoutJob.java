package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import org.jeecg.common.util.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动登出
 * 
 * @Author Frank
 */
@Slf4j
public class AutoLogoutJob implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(String.format("welcome %s! 自动退出开始!时间:" + DateUtils.now(), this.parameter));
		
		log.info("从数据库获取国泰君安证券账户登录信息");
		
		log.info("发起登出请求");
		
		log.info("清空数据库的会话信息");
		
		log.info(String.format("welcome %s! 自动登出结束!时间:" + DateUtils.now(), this.parameter));
	}
}
