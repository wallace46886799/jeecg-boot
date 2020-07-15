package org.jeecg.modules.dreamlabs.org.service;

import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 金融机构表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
public interface IDreamlabsOrgService extends IService<DreamlabsOrg> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(DreamlabsOrg dreamlabsOrg,List<DreamlabsOrgParam> dreamlabsOrgParamList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(DreamlabsOrg dreamlabsOrg,List<DreamlabsOrgParam> dreamlabsOrgParamList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
