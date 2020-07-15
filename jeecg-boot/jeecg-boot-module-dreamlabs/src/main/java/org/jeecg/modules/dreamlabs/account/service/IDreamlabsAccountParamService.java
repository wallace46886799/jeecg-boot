package org.jeecg.modules.dreamlabs.account.service;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 账户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
public interface IDreamlabsAccountParamService extends IService<DreamlabsAccountParam> {

	public List<DreamlabsAccountParam> selectByMainId(String mainId);
}
