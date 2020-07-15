package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.util.List;
import java.util.Map;

import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PlaceholderUtil;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动登录
 * 
 * @Author Frank
 */
@Slf4j
public class AutoLoginJob extends AbstractGtjaJob {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		log.info("自动登录!时间:{}", DateUtils.getTimestamp());

		List<DreamlabsOrg> orgs = this.queryOrgs();

		for (DreamlabsOrg org : orgs) {
			log.info("金融机构:{}", org.getOrgName());

			log.info("查询需登录的机构参数:{}", org.getOrgName());
			Map<String, String> orgParamsMap = this.queryOrgParams(org);

			List<DreamlabsAccount> accounts = this.queryAccounts();

			for (DreamlabsAccount account : accounts) {
				if (!account.getStatus().equals("1")) {
					log.info("账户冻结:{}", account.getAccountName());
					continue;
				}

				log.info("查询需登录的账户参数:{}", account.getAccountName());
				Map<String, String> accountParamsMap = this.queryAccountParams(account);

				log.info("国泰君安站点--登录");
				String login_url = orgParamsMap.get("login.url");
				
				HttpRequest shares = HttpUtil.createPost(login_url).cookie(accountParamsMap.get("session.cookie"));
				HttpResponse response = shares.execute();
				String res = response.body();
				log.info("国泰君安站点--查询今日新股信息:{}", res);

			}

			log.info("数据库--获取国泰君安证券公司信息");

			log.info("数据库--获取国泰君安证券用户信息");

			log.info("数据库--获取国泰君安证券账户信息");

			log.info("国泰君安站点--发起登录请求");

			log.info("数据库--将登录会话保存至数据库");

		}

	}

}
