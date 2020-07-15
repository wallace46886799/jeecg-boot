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
 * @Description: 笨鸟任务
 * @Author: jeecg-boot
 * @Date:   2020-01-02
 * @Version: V1.0
 */
@Data
@TableName("bird_tasks")
public class BirdTasks implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
	/**笨鸟用户ID*/
	@Excel(name = "笨鸟用户ID", width = 15)
    private java.lang.String userId;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    private java.lang.String taskName;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
    private java.lang.String taskDesc;
	/**星期几*/
	@Excel(name = "星期几", width = 15)
    private java.lang.String weekDay;
	/**权重*/
	@Excel(name = "权重", width = 15)
    private java.lang.Integer weight;
	
	
	
	/**
	 * 以下为适配任务模板
	 */
	@Excel(name = "任务类型编号", width = 100)
    private java.lang.String taskTargetTypeId;
	@Excel(name = "任务描述描述", width = 5000)
    private java.lang.String taskTypeDesc;
	@Excel(name = "是否必填", width = 2)
    private java.lang.String isMust;
	

}
