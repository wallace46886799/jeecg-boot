package org.jeecg.modules.bird.service.impl;

import org.jeecg.modules.bird.entity.BirdTasks;
import org.jeecg.modules.bird.mapper.BirdTasksMapper;
import org.jeecg.modules.bird.service.IBirdTasksService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 笨鸟任务
 * @Author: jeecg-boot
 * @Date:   2020-01-02
 * @Version: V1.0
 */
@Service
public class BirdTasksServiceImpl extends ServiceImpl<BirdTasksMapper, BirdTasks> implements IBirdTasksService {

}
