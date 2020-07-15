package org.jeecg.modules.dreamlabs.org.service;

import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 金融机构参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
public interface IDreamlabsOrgParamService extends IService<DreamlabsOrgParam> {

	public List<DreamlabsOrgParam> selectByMainId(String mainId);
}
