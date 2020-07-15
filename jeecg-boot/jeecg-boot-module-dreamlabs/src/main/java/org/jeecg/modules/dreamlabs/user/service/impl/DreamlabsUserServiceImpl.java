package org.jeecg.modules.dreamlabs.user.service.impl;

import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUser;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import org.jeecg.modules.dreamlabs.user.mapper.DreamlabsUserParamMapper;
import org.jeecg.modules.dreamlabs.user.mapper.DreamlabsUserMapper;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 用户表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Service
public class DreamlabsUserServiceImpl extends ServiceImpl<DreamlabsUserMapper, DreamlabsUser> implements IDreamlabsUserService {

	@Autowired
	private DreamlabsUserMapper dreamlabsUserMapper;
	@Autowired
	private DreamlabsUserParamMapper dreamlabsUserParamMapper;
	
	@Override
	@Transactional
	public void saveMain(DreamlabsUser dreamlabsUser, List<DreamlabsUserParam> dreamlabsUserParamList) {
		dreamlabsUserMapper.insert(dreamlabsUser);
		if(dreamlabsUserParamList!=null && dreamlabsUserParamList.size()>0) {
			for(DreamlabsUserParam entity:dreamlabsUserParamList) {
				//外键设置
				entity.setUserId(dreamlabsUser.getId());
				dreamlabsUserParamMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(DreamlabsUser dreamlabsUser,List<DreamlabsUserParam> dreamlabsUserParamList) {
		dreamlabsUserMapper.updateById(dreamlabsUser);
		
		//1.先删除子表数据
		dreamlabsUserParamMapper.deleteByMainId(dreamlabsUser.getId());
		
		//2.子表数据重新插入
		if(dreamlabsUserParamList!=null && dreamlabsUserParamList.size()>0) {
			for(DreamlabsUserParam entity:dreamlabsUserParamList) {
				//外键设置
				entity.setUserId(dreamlabsUser.getId());
				dreamlabsUserParamMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		dreamlabsUserParamMapper.deleteByMainId(id);
		dreamlabsUserMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			dreamlabsUserParamMapper.deleteByMainId(id.toString());
			dreamlabsUserMapper.deleteById(id);
		}
	}
	
}
