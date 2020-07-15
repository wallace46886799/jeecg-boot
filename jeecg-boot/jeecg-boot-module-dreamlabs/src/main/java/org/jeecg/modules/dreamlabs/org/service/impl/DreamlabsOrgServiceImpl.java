package org.jeecg.modules.dreamlabs.org.service.impl;

import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import org.jeecg.modules.dreamlabs.org.mapper.DreamlabsOrgParamMapper;
import org.jeecg.modules.dreamlabs.org.mapper.DreamlabsOrgMapper;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 金融机构表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Service
public class DreamlabsOrgServiceImpl extends ServiceImpl<DreamlabsOrgMapper, DreamlabsOrg> implements IDreamlabsOrgService {

	@Autowired
	private DreamlabsOrgMapper dreamlabsOrgMapper;
	@Autowired
	private DreamlabsOrgParamMapper dreamlabsOrgParamMapper;
	
	@Override
	@Transactional
	public void saveMain(DreamlabsOrg dreamlabsOrg, List<DreamlabsOrgParam> dreamlabsOrgParamList) {
		dreamlabsOrgMapper.insert(dreamlabsOrg);
		if(dreamlabsOrgParamList!=null && dreamlabsOrgParamList.size()>0) {
			for(DreamlabsOrgParam entity:dreamlabsOrgParamList) {
				//外键设置
				entity.setOrgId(dreamlabsOrg.getId());
				dreamlabsOrgParamMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(DreamlabsOrg dreamlabsOrg,List<DreamlabsOrgParam> dreamlabsOrgParamList) {
		dreamlabsOrgMapper.updateById(dreamlabsOrg);
		
		//1.先删除子表数据
		dreamlabsOrgParamMapper.deleteByMainId(dreamlabsOrg.getId());
		
		//2.子表数据重新插入
		if(dreamlabsOrgParamList!=null && dreamlabsOrgParamList.size()>0) {
			for(DreamlabsOrgParam entity:dreamlabsOrgParamList) {
				//外键设置
				entity.setOrgId(dreamlabsOrg.getId());
				dreamlabsOrgParamMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		dreamlabsOrgParamMapper.deleteByMainId(id);
		dreamlabsOrgMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			dreamlabsOrgParamMapper.deleteByMainId(id.toString());
			dreamlabsOrgMapper.deleteById(id);
		}
	}
	
}
