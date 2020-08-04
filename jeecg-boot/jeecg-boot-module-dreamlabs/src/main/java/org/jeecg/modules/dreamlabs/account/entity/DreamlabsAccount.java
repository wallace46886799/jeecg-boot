package org.jeecg.modules.dreamlabs.account.entity;

import java.io.Serializable;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel(value="dreamlabs_account对象", description="账户表")
@Data
@TableName("dreamlabs_account")
public class DreamlabsAccount implements Serializable {
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
	/**账户名称*/
	@Excel(name = "账户名称", width = 15)
    @ApiModelProperty(value = "账户名称")
    private java.lang.String accountName;
	/**账户类型*/
	@Excel(name = "账户类型", width = 15, dicCode = "account_type")
    @Dict(dicCode = "account_type")
    @ApiModelProperty(value = "账户类型")
    private java.lang.String accountType;
	/**账户号码*/
	@Excel(name = "账户号码", width = 15)
    @ApiModelProperty(value = "账户号码")
    private java.lang.String accountNumber;
	/**所属用户*/
	@Excel(name = "所属用户", width = 15, dictTable = "dreamlabs_user", dicText = "name", dicCode = "id")
    @Dict(dictTable = "dreamlabs_user", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属用户")
    private java.lang.String accountOwner;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15, dictTable = "dreamlabs_org", dicText = "org_name", dicCode = "id")
    @Dict(dictTable = "dreamlabs_org", dicText = "org_name", dicCode = "id")
    @ApiModelProperty(value = "所属机构")
    private java.lang.String orgOwner;
	/**状态*/
	@Excel(name = "账户别名", width = 15)
    @Dict(dicCode = "account_alias")
    @ApiModelProperty(value = "账户别名")
    private java.lang.String accountAlias;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "dict_item_status")
    @Dict(dicCode = "dict_item_status")
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
}
