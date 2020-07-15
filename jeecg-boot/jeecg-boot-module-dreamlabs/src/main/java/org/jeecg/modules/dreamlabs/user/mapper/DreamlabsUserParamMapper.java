package org.jeecg.modules.dreamlabs.user.mapper;

import java.util.List;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
public interface DreamlabsUserParamMapper extends BaseMapper<DreamlabsUserParam> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DreamlabsUserParam> selectByMainId(@Param("mainId") String mainId);
}
