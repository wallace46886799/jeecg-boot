package org.jeecg.modules.dreamlabs.account.service;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
public interface IDreamlabsAccountService extends IService<DreamlabsAccount> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(DreamlabsAccount dreamlabsAccount,List<DreamlabsAccountParam> dreamlabsAccountParamList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(DreamlabsAccount dreamlabsAccount,List<DreamlabsAccountParam> dreamlabsAccountParamList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
