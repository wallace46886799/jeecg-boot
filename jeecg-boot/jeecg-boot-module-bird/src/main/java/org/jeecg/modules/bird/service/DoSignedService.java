package org.jeecg.modules.bird.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bird.entity.BirdUser;
import org.jeecg.modules.quartz.entity.QuartzJob;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface DoSignedService extends IService<QuartzJob> {

	String signin(BirdUser birdUser);
	String signout(BirdUser birdUser);
}
