package org.jeecg.modules.dreamlabs.account.service.impl;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecg.modules.dreamlabs.account.mapper.DreamlabsAccountParamMapper;
import org.jeecg.modules.dreamlabs.account.mapper.DreamlabsAccountMapper;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@Service
public class DreamlabsAccountServiceImpl extends ServiceImpl<DreamlabsAccountMapper, DreamlabsAccount> implements IDreamlabsAccountService {

	@Autowired
	private DreamlabsAccountMapper dreamlabsAccountMapper;
	@Autowired
	private DreamlabsAccountParamMapper dreamlabsAccountParamMapper;
	
	@Override
	@Transactional
	public void saveMain(DreamlabsAccount dreamlabsAccount, List<DreamlabsAccountParam> dreamlabsAccountParamList) {
		dreamlabsAccountMapper.insert(dreamlabsAccount);
		if(dreamlabsAccountParamList!=null && dreamlabsAccountParamList.size()>0) {
			for(DreamlabsAccountParam entity:dreamlabsAccountParamList) {
				//外键设置
				entity.setAccountId(dreamlabsAccount.getId());
				dreamlabsAccountParamMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(DreamlabsAccount dreamlabsAccount,List<DreamlabsAccountParam> dreamlabsAccountParamList) {
		dreamlabsAccountMapper.updateById(dreamlabsAccount);
		
		//1.先删除子表数据
		dreamlabsAccountParamMapper.deleteByMainId(dreamlabsAccount.getId());
		
		//2.子表数据重新插入
		if(dreamlabsAccountParamList!=null && dreamlabsAccountParamList.size()>0) {
			for(DreamlabsAccountParam entity:dreamlabsAccountParamList) {
				//外键设置
				entity.setAccountId(dreamlabsAccount.getId());
				dreamlabsAccountParamMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		dreamlabsAccountParamMapper.deleteByMainId(id);
		dreamlabsAccountMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			dreamlabsAccountParamMapper.deleteByMainId(id.toString());
			dreamlabsAccountMapper.deleteById(id);
		}
	}
	
}
