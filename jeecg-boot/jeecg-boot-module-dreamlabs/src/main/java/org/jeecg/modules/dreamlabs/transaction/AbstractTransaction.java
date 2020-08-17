package org.jeecg.modules.dreamlabs.transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountParamService;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountService;
import org.jeecg.modules.dreamlabs.fund.entity.DreamlabsFund;
import org.jeecg.modules.dreamlabs.fund.service.IDreamlabsFundService;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgParamService;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgService;
import org.jeecg.modules.dreamlabs.stock.entity.DreamlabsStock;
import org.jeecg.modules.dreamlabs.stock.service.IDreamlabsStockService;
import org.jeecg.modules.dreamlabs.translog.entity.DreamlabsTransLog;
import org.jeecg.modules.dreamlabs.translog.service.IDreamlabsTransLogService;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUser;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserParamService;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractTransaction implements Transaction {
	
	@Autowired
	protected ConverterRegistry converterRegistry;
	
	/**
	 * 
	 * @param amount
	 * @param price
	 * @param profit
	 * @return
	 */
	protected Map<String,BigDecimal> parseProfitLoss(BigDecimal amount,BigDecimal price, BigDecimal floatingProfit){
		if (amount == null || price == null || floatingProfit == null) {
			throw new IllegalArgumentException("Please check arguments.");
		}
		Map<String,BigDecimal> result = new HashMap<String,BigDecimal> ();
		BigDecimal origAmount = NumberUtil.round(NumberUtil.sub(amount,floatingProfit), 2);
		result.put("origAmount", origAmount);
		BigDecimal share =  NumberUtil.round( NumberUtil.div(amount,price), 2);
		result.put("share", share);
		BigDecimal origPrice = NumberUtil.round( NumberUtil.div(origAmount,share), 2);
		result.put("origPrice", origPrice);
		BigDecimal floatingPercent = NumberUtil.round( NumberUtil.div(floatingProfit,origAmount), 4);
		result.put("floatingPercent", floatingPercent);
		return result;
	}
	
	/**
	 * 百分数保留小数点后4位
	 * @param percent
	 * @return
	 */
	protected String parsePercent(String percent) {
		try {
			Double floating_percent_double = (Double)NumberFormat.getPercentInstance().parse(percent);
			DecimalFormat df = new DecimalFormat("######0.0000");
			return df.format(floating_percent_double);
		}catch (java.text.ParseException exception) {
			log.info("ParseException,return default with 0.00,real percent is {}", percent);
			return "0.00";
		}
	}
	/**
	 * 金额保留小数点后2位
	 * @param number
	 * @return
	 */
	protected String parseNumber(String number) {
		try {
			double d = new DecimalFormat().parse(number).doubleValue(); 
			return String.valueOf(d);
		}catch (java.text.ParseException exception) {
			log.info("ParseException,return default with 0.00,real number is {}", number);
			return "0.00";
		}
	}
	/**
	 * 金额保留小数点后2位
	 * @param currency
	 * @return
	 */
	protected String parseCurrency(String currency) {
		String curr = StrUtil.removeAll(currency, "￥");
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(curr);
	}
	
	/**
	 * 根据基金名称查基金信息
	 * @param name
	 * @return
	 */
	protected DreamlabsFund queryFundByName(String name) {
		String finalName = StrUtil.subBefore(name, "(", true);
		log.info("查询基金的名称为:{},其原始名称为:{}", finalName, name);
		IDreamlabsFundService dreamlabsFundService = SpringContextUtils.getBean(IDreamlabsFundService.class);
		LambdaQueryWrapper<DreamlabsFund> queryWrapper = new LambdaQueryWrapper<DreamlabsFund>();
		queryWrapper.eq(DreamlabsFund::getName, finalName);
		DreamlabsFund fund = dreamlabsFundService.getOne(queryWrapper);
		if (fund == null) {
			log.error("无法查询基金信息");
			throw new IllegalArgumentException("Illegal Fund name:"+name);
		}
		log.info("基金信息:{}", fund);
		return fund;
	}
	
	/**
	 * 根据基金名称查基金信息
	 * @param name
	 * @return
	 */
	protected DreamlabsStock queryStockByName(String name) {
		String finalName = StrUtil.subBefore(name, "(", true);
		log.info("查询股票的名称为:{},其原始名称为:{}", finalName, name);
		IDreamlabsStockService dreamlabsStockService = SpringContextUtils.getBean(IDreamlabsStockService.class);
		LambdaQueryWrapper<DreamlabsStock> queryWrapper = new LambdaQueryWrapper<DreamlabsStock>();
		queryWrapper.eq(DreamlabsStock::getName, finalName);
		DreamlabsStock stock = dreamlabsStockService.getOne(queryWrapper);
		if (stock == null) {
			log.error("无法查询股票信息");
			throw new IllegalArgumentException("Illegal Stock name:"+name);
		}
		log.info("股票信息:{}", stock);
		return stock;
	}
	
	/**
	 * 根据机构编号查询机构
	 * @param orgId
	 * @return
	 */
	protected DreamlabsOrg queryOrg(String orgId) {
		IDreamlabsOrgService dreamlabsOrgService = SpringContextUtils.getBean(IDreamlabsOrgService.class);
		DreamlabsOrg org = dreamlabsOrgService.getById(orgId);
		log.info("交易机构:{}", org);
		return org;
	}
	/**
	 * 根据机构条件查询机构
	 * @param org_q
	 * @return
	 */
	protected List<DreamlabsOrg> queryOrgs(LambdaQueryWrapper<DreamlabsOrg> org_q) {
		IDreamlabsOrgService dreamlabsOrgService = SpringContextUtils.getBean(IDreamlabsOrgService.class);
		List<DreamlabsOrg> orgs = dreamlabsOrgService.list(org_q);
		log.info("交易机构:{}", orgs);
		return orgs;
	}
	/**
	 * 根据账户编号查询账户
	 * @param accountId
	 * @return
	 */
	protected DreamlabsAccount queryAccount(String accountId) {
		IDreamlabsAccountService dreamlabsAccountService = SpringContextUtils.getBean(IDreamlabsAccountService.class);
		DreamlabsAccount account = dreamlabsAccountService.getById(accountId);
		log.info("交易账户:{}", account);
		return account;
	}
	/**
	 * 根据账户条件查询账户
	 * @param account_q
	 * @return
	 */
	protected List<DreamlabsAccount> queryAccounts(LambdaQueryWrapper<DreamlabsAccount> account_q) {
		IDreamlabsAccountService dreamlabsAccountService = SpringContextUtils.getBean(IDreamlabsAccountService.class);
		List<DreamlabsAccount> accounts = dreamlabsAccountService.list(account_q);
		log.info("交易账户:{}", accounts);
		return accounts;
	}
	/**
	 * 根据用户编号查询用户
	 * @param userId
	 * @return
	 */
	protected DreamlabsUser queryUser(String userId) {
		IDreamlabsUserService dreamlabsUserService = SpringContextUtils.getBean(IDreamlabsUserService.class);
		DreamlabsUser user = dreamlabsUserService.getById(userId);
		log.info("交易用户:{}", userId);
		return user;
	}
	/**
	 * 根据用户条件查询用户
	 * @param user_q
	 * @return
	 */
	protected List<DreamlabsUser> queryUsers(LambdaQueryWrapper<DreamlabsUser> user_q) {
		IDreamlabsUserService dreamlabsUserService = SpringContextUtils.getBean(IDreamlabsUserService.class);
		List<DreamlabsUser> users = dreamlabsUserService.list(user_q);
		log.info("交易用户:{}", users);
		return users;
	}
	/**
	 * 根据用户查询用户参数
	 * @param user
	 * @return
	 */
	protected Map<String, String> queryUserParams(DreamlabsUser user) {
		IDreamlabsUserParamService dreamlabsUserParamService = SpringContextUtils
				.getBean(IDreamlabsUserParamService.class);
		LambdaQueryWrapper<DreamlabsUserParam> user_query = new LambdaQueryWrapper<DreamlabsUserParam>();
		user_query.eq(DreamlabsUserParam::getUserId, user.getId());
		user_query.eq(DreamlabsUserParam::getStatus, "1");
		List<DreamlabsUserParam> userParams = dreamlabsUserParamService.list(user_query);
		Map<String, String> params = new HashMap<String, String>();
		for (DreamlabsUserParam userParam : userParams) {
			params.put(userParam.getParamGroup() + "." + userParam.getParamKey(), userParam.getParamValue());
		}
		return params;
	}
	/**
	 * 根据机构查询机构参数
	 * @param org
	 * @return
	 */
	protected Map<String, String> queryOrgParams(DreamlabsOrg org) {
		IDreamlabsOrgParamService dreamlabsOrgParamService = SpringContextUtils
				.getBean(IDreamlabsOrgParamService.class);
		LambdaQueryWrapper<DreamlabsOrgParam> org_query = new LambdaQueryWrapper<DreamlabsOrgParam>();
		org_query.eq(DreamlabsOrgParam::getOrgId, org.getId());
		org_query.eq(DreamlabsOrgParam::getStatus, "1");
		List<DreamlabsOrgParam> orgParams = dreamlabsOrgParamService.list(org_query);
		Map<String, String> params = new HashMap<String, String>();
		for (DreamlabsOrgParam orgParam : orgParams) {
			params.put(orgParam.getParamGroup() + "." + orgParam.getParamKey(), orgParam.getParamValue());
		}
		return params;
	}
	/**
	 * 根据账户查询账户参数
	 * @param account
	 * @return
	 */
	protected Map<String, String> queryAccountParams(DreamlabsAccount account) {
		IDreamlabsAccountParamService dreamlabsAccountParamService = SpringContextUtils
				.getBean(IDreamlabsAccountParamService.class);
		LambdaQueryWrapper<DreamlabsAccountParam> account_query = new LambdaQueryWrapper<DreamlabsAccountParam>();
		account_query.eq(DreamlabsAccountParam::getAccountId, account.getId());
		account_query.eq(DreamlabsAccountParam::getStatus, "1");
		List<DreamlabsAccountParam> accountParams = dreamlabsAccountParamService.list(account_query);
		Map<String, String> params = new HashMap<String, String>();
		for (DreamlabsAccountParam orgParam : accountParams) {
			params.put(orgParam.getParamGroup() + "." + orgParam.getParamKey(), orgParam.getParamValue());
		}
		return params;
	}
	/**
	 * 插入单条日志
	 * @param log
	 */
	protected void insertTransLog(DreamlabsTransLog log) {
		IDreamlabsTransLogService dreamlabsTransLogService = SpringContextUtils
				.getBean(IDreamlabsTransLogService.class);
		dreamlabsTransLogService.save(log);
	}
	/**
	 * 插入多条日志
	 * @param logs
	 */
	protected void insertTransLogs(List<DreamlabsTransLog> logs) {
		IDreamlabsTransLogService dreamlabsTransLogService = SpringContextUtils
				.getBean(IDreamlabsTransLogService.class);
		dreamlabsTransLogService.saveBatch(logs);
	}
}
