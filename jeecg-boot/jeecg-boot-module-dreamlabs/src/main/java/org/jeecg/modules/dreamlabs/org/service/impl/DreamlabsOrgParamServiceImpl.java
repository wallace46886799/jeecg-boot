package org.jeecg.modules.dreamlabs.org.service.impl;

import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import org.jeecg.modules.dreamlabs.org.mapper.DreamlabsOrgParamMapper;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgParamService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 金融机构参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Service
public class DreamlabsOrgParamServiceImpl extends ServiceImpl<DreamlabsOrgParamMapper, DreamlabsOrgParam> implements IDreamlabsOrgParamService {
	
	@Autowired
	private DreamlabsOrgParamMapper dreamlabsOrgParamMapper;
	
	@Override
	public List<DreamlabsOrgParam> selectByMainId(String mainId) {
		return dreamlabsOrgParamMapper.selectByMainId(mainId);
	}
}
