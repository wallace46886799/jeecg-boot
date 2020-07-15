package org.jeecg.modules.dreamlabs.account.mapper;

import java.util.List;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 账户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
public interface DreamlabsAccountParamMapper extends BaseMapper<DreamlabsAccountParam> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DreamlabsAccountParam> selectByMainId(@Param("mainId") String mainId);
}
