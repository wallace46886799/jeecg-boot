package org.jeecg.modules.dreamlabs.org.mapper;

import java.util.List;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 金融机构参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
public interface DreamlabsOrgParamMapper extends BaseMapper<DreamlabsOrgParam> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DreamlabsOrgParam> selectByMainId(@Param("mainId") String mainId);
}
