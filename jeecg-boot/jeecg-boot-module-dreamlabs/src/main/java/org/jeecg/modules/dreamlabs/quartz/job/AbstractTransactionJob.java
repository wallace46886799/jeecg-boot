package org.jeecg.modules.dreamlabs.quartz.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountParamService;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountService;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgParamService;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgService;
import org.jeecg.modules.dreamlabs.translog.entity.DreamlabsTransLog;
import org.jeecg.modules.dreamlabs.translog.service.IDreamlabsTransLogService;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUser;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserParamService;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserService;
import org.quartz.Job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author Frank
 *
 */
@Slf4j
public abstract class AbstractTransactionJob implements Job {
	
	
	protected List<DreamlabsAccount> queryAccounts(DreamlabsOrg org){
		IDreamlabsAccountService dreamlabsAccountService = SpringContextUtils.getBean(IDreamlabsAccountService.class);
		LambdaQueryWrapper<DreamlabsAccount> account_q = new LambdaQueryWrapper<DreamlabsAccount>();
		account_q.eq(DreamlabsAccount::getOrgOwner,org.getId());
		List<DreamlabsAccount> accounts = dreamlabsAccountService.list(account_q);
		log.info("打新账户:{}" ,accounts);
		return accounts;
	}
	
	protected List<DreamlabsOrg> queryOrgs(String orgId){
		IDreamlabsOrgService dreamlabsOrgService = SpringContextUtils.getBean(IDreamlabsOrgService.class);
		LambdaQueryWrapper<DreamlabsOrg> org_q = new LambdaQueryWrapper<DreamlabsOrg>();
		org_q.eq(DreamlabsOrg::getId,orgId);
		List<DreamlabsOrg> orgs = dreamlabsOrgService.list(org_q);
		log.info("打新机构:{}", orgs);
		return orgs;
	}
	
	protected List<DreamlabsAccount> queryAccounts(){
		IDreamlabsAccountService dreamlabsAccountService = SpringContextUtils.getBean(IDreamlabsAccountService.class);
		LambdaQueryWrapper<DreamlabsAccount> account_q = new LambdaQueryWrapper<DreamlabsAccount>();
		account_q.eq(DreamlabsAccount::getAccountType,"1");
		account_q.eq(DreamlabsAccount::getOrgOwner,"1278679933966618626");
		List<DreamlabsAccount> accounts = dreamlabsAccountService.list(account_q);
		log.info("打新账户:{}" ,accounts);
		return accounts;
	}
	
	protected List<DreamlabsOrg> queryOrgs(){
		IDreamlabsOrgService dreamlabsOrgService = SpringContextUtils.getBean(IDreamlabsOrgService.class);
		LambdaQueryWrapper<DreamlabsOrg> org_q = new LambdaQueryWrapper<DreamlabsOrg>();
		org_q.eq(DreamlabsOrg::getId,"1278679933966618626");
		List<DreamlabsOrg> orgs = dreamlabsOrgService.list(org_q);
		log.info("打新机构:{}", orgs);
		return orgs;
	}
	
	
	protected List<DreamlabsUser> queryUsers(){
		IDreamlabsUserService dreamlabsUserService = SpringContextUtils.getBean(IDreamlabsUserService.class);
		LambdaQueryWrapper<DreamlabsUser> user_q = new LambdaQueryWrapper<DreamlabsUser>();
		user_q.eq(DreamlabsUser::getId,"1278679933966618626");
		List<DreamlabsUser> users = dreamlabsUserService.list(user_q);
		log.info("打新用户:{}", users);
		return users;
	}
	
	
	protected Map<String,String> queryUserParams(DreamlabsUser user){
		IDreamlabsUserParamService dreamlabsUserParamService = SpringContextUtils.getBean(IDreamlabsUserParamService.class);
		LambdaQueryWrapper<DreamlabsUserParam> user_query = new LambdaQueryWrapper<DreamlabsUserParam>();
		user_query.eq(DreamlabsUserParam::getUserId,user.getId());
		List<DreamlabsUserParam> userParams  = dreamlabsUserParamService.list(user_query);
		Map<String,String> params = new HashMap<String,String>();
		for (DreamlabsUserParam userParam : userParams) {
			params.put(userParam.getParamGroup()+"."+userParam.getParamKey(), userParam.getParamValue());
		}
		return params;
	}
	
	protected Map<String,String> queryOrgParams(DreamlabsOrg org){
		IDreamlabsOrgParamService dreamlabsOrgParamService = SpringContextUtils.getBean(IDreamlabsOrgParamService.class);
		LambdaQueryWrapper<DreamlabsOrgParam> org_query = new LambdaQueryWrapper<DreamlabsOrgParam>();
		org_query.eq(DreamlabsOrgParam::getOrgId,org.getId());
		List<DreamlabsOrgParam> orgParams  = dreamlabsOrgParamService.list(org_query);
		Map<String,String> params = new HashMap<String,String>();
		for (DreamlabsOrgParam orgParam : orgParams) {
			params.put(orgParam.getParamGroup()+"."+orgParam.getParamKey(), orgParam.getParamValue());
		}
		return params;
	}
	
	protected Map<String,String> queryAccountParams(DreamlabsAccount account){
		IDreamlabsAccountParamService dreamlabsAccountParamService = SpringContextUtils.getBean(IDreamlabsAccountParamService.class);
		LambdaQueryWrapper<DreamlabsAccountParam> account_query = new LambdaQueryWrapper<DreamlabsAccountParam>();
		account_query.eq(DreamlabsAccountParam::getAccountId,account.getId());
		List<DreamlabsAccountParam> accountParams = dreamlabsAccountParamService.list(account_query);
		Map<String,String> params = new HashMap<String,String>();
		for (DreamlabsAccountParam orgParam : accountParams) {
			params.put(orgParam.getParamGroup()+"."+orgParam.getParamKey(), orgParam.getParamValue());
		}
		return params;
	}
	
	protected void insertTransLog(DreamlabsTransLog log) {
		IDreamlabsTransLogService dreamlabsTransLogService = SpringContextUtils.getBean(IDreamlabsTransLogService.class);
		dreamlabsTransLogService.save(log);
	}
	
	
	protected void insertTransLogs(List<DreamlabsTransLog> logs) {
		IDreamlabsTransLogService dreamlabsTransLogService = SpringContextUtils.getBean(IDreamlabsTransLogService.class);
		dreamlabsTransLogService.saveBatch(logs);
	}
}
