package org.jeecg.modules.dreamlabs.user.service;

import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 用户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
public interface IDreamlabsUserParamService extends IService<DreamlabsUserParam> {

	public List<DreamlabsUserParam> selectByMainId(String mainId);
}
