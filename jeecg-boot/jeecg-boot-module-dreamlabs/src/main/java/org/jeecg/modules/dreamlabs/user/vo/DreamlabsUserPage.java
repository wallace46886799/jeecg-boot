package org.jeecg.modules.dreamlabs.user.vo;

import java.util.List;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUser;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 用户表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Data
@ApiModel(value="dreamlabs_userPage对象", description="用户表")
public class DreamlabsUserPage {

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
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	@ApiModelProperty(value = "姓名")
	private java.lang.String name;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	
	@ExcelCollection(name="用户参数表")
	@ApiModelProperty(value = "用户参数表")
	private List<DreamlabsUserParam> dreamlabsUserParamList;
	
}
