package org.jeecg.modules.dreamlabs.user.service.impl;

import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import org.jeecg.modules.dreamlabs.user.mapper.DreamlabsUserParamMapper;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserParamService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 用户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Service
public class DreamlabsUserParamServiceImpl extends ServiceImpl<DreamlabsUserParamMapper, DreamlabsUserParam> implements IDreamlabsUserParamService {
	
	@Autowired
	private DreamlabsUserParamMapper dreamlabsUserParamMapper;
	
	@Override
	public List<DreamlabsUserParam> selectByMainId(String mainId) {
		return dreamlabsUserParamMapper.selectByMainId(mainId);
	}
}
