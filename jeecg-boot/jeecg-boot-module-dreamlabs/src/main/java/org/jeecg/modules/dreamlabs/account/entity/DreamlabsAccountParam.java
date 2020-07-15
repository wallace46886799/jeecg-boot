package org.jeecg.modules.dreamlabs.account.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 账户参数表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@ApiModel(value="dreamlabs_account对象", description="账户表")
@Data
@TableName("dreamlabs_account_param")
public class DreamlabsAccountParam implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private java.lang.String sysOrgCode;
	/**参数组*/
	@Excel(name = "参数组", width = 15)
	@ApiModelProperty(value = "参数组")
	private java.lang.String paramGroup;
	/**参数键*/
	@Excel(name = "参数键", width = 15)
	@ApiModelProperty(value = "参数键")
	private java.lang.String paramKey;
	/**参数值*/
	@Excel(name = "参数值", width = 15)
	@ApiModelProperty(value = "参数值")
	private java.lang.String paramValue;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "dict_item_status")
	@ApiModelProperty(value = "状态")
	private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**账户*/
	@ApiModelProperty(value = "账户")
	private java.lang.String accountId;
}
