package org.jeecg.modules.dreamlabs.user.service;

import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUser;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 用户表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
public interface IDreamlabsUserService extends IService<DreamlabsUser> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(DreamlabsUser dreamlabsUser,List<DreamlabsUserParam> dreamlabsUserParamList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(DreamlabsUser dreamlabsUser,List<DreamlabsUserParam> dreamlabsUserParamList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
