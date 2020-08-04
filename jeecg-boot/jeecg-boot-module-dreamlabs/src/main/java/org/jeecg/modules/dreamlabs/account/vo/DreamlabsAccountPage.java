package org.jeecg.modules.dreamlabs.account.vo;

import java.util.List;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@Data
@ApiModel(value="dreamlabs_accountPage对象", description="账户表")
public class DreamlabsAccountPage {

	/**主键*/
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
	/**账户名称*/
	@Excel(name = "账户名称", width = 15)
	@ApiModelProperty(value = "账户名称")
	private java.lang.String accountName;
	/**账户类型*/
	@Excel(name = "账户类型", width = 15)
	@ApiModelProperty(value = "账户类型")
	private java.lang.String accountType;
	/**账户号码*/
	@Excel(name = "账户号码", width = 15)
	@ApiModelProperty(value = "账户号码")
	private java.lang.String accountNumber;
	/**所属用户*/
	@Excel(name = "所属用户", width = 15)
	@ApiModelProperty(value = "所属用户")
	private java.lang.String accountOwner;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
	@ApiModelProperty(value = "所属机构")
	private java.lang.String orgOwner;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	
	@ExcelCollection(name="账户参数表")
	@ApiModelProperty(value = "账户参数表")
	private List<DreamlabsAccountParam> dreamlabsAccountParamList;
	
}
