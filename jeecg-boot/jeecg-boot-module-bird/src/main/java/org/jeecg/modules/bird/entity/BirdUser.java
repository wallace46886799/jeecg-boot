package org.jeecg.modules.bird.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 笨鸟用户
 * @Author: jeecg-boot
 * @Date:   2020-01-02
 * @Version: V1.0
 */
@Data
@TableName("bird_user")
public class BirdUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**用户名称*/
	@Excel(name = "用户名称", width = 15)
    private String name;
	/**签到地址描述*/
	@Excel(name = "签到地址描述", width = 15)
    private String addresses;
	/**经度*/
	@Excel(name = "经度", width = 15)
    private String latitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    private String longitude;
	/**项目编号*/
	@Excel(name = "项目编号", width = 15)
    private String project;
	/**登陆TOKEN*/
	@Excel(name = "登陆TOKEN", width = 15)
    private String tokenStr;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    private String phone;
	/**工作日自动填写任务*/
	@Excel(name = "工作日自动填写任务", width = 15)
    private String status;
	/**工作状态*/
	@Excel(name = "工作状态", width = 10)
    private String inwork;
	
}
