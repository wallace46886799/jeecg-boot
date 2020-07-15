package org.jeecg.modules.dreamlabs.account.service.impl;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecg.modules.dreamlabs.account.mapper.DreamlabsAccountParamMapper;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountParamService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 账户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@Service
public class DreamlabsAccountParamServiceImpl extends ServiceImpl<DreamlabsAccountParamMapper, DreamlabsAccountParam> implements IDreamlabsAccountParamService {
	
	@Autowired
	private DreamlabsAccountParamMapper dreamlabsAccountParamMapper;
	
	@Override
	public List<DreamlabsAccountParam> selectByMainId(String mainId) {
		return dreamlabsAccountParamMapper.selectByMainId(mainId);
	}
}
