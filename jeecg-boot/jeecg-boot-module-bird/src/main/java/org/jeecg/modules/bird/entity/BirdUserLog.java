package org.jeecg.modules.bird.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 笨鸟操作日志
 * @Author: jeecg-boot
 * @Date:   2019-12-25
 * @Version: V1.0
 */
@Data
@TableName("bird_user_log")
public class BirdUserLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**用户ID*/
	@Excel(name = "用户ID", width = 15)
    private java.lang.String birdUserId;
	/**详细操作日志*/
	@Excel(name = "详细操作日志", width = 15)
    private java.lang.String logMsg;
	/**操作时间*/
	@Excel(name = "操作时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
}
