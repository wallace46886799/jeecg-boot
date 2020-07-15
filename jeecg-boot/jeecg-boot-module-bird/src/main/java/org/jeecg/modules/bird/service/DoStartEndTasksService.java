package org.jeecg.modules.bird.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.bird.entity.BirdUser;
import org.jeecg.modules.quartz.entity.QuartzJob;

/**
 * @Description: 定时任务在线管理
 * @Author: Frank
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface DoStartEndTasksService extends IService<QuartzJob> {
	/**
	 * 根据用户创建新任务
	 * @param birdUser
	 */
    void newTask(BirdUser birdUser);
    /**
     * 根据用户启动新任务
     * @param birdUser
     */
    void startTask(BirdUser birdUser);
    /**
     * 根据用户结束任务
     * @param birdUser
     */
    void endTask(BirdUser birdUser);
    /**
     * 根据审批人自动审批任务
     * @param birdUser
     */
    void approvalTasks(BirdUser birdUser);
}
