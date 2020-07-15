package org.jeecg.modules.bird.service.impl;

import org.jeecg.modules.bird.entity.BirdUser;
import org.jeecg.modules.bird.mapper.BirdUserMapper;
import org.jeecg.modules.bird.service.IBirdUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 笨鸟用户
 * @Author: jeecg-boot
 * @Date:   2019-12-25
 * @Version: V1.0
 */
@Service
public class BirdUserServiceImpl extends ServiceImpl<BirdUserMapper, BirdUser> implements IBirdUserService {

}
