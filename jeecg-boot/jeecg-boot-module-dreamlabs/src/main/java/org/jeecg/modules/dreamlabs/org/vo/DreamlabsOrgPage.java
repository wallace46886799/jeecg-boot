package org.jeecg.modules.dreamlabs.org.vo;

import java.util.List;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
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
 * @Description: 金融机构表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Data
@ApiModel(value="dreamlabs_orgPage对象", description="金融机构表")
public class DreamlabsOrgPage {

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
	/**机构名称*/
	@Excel(name = "机构名称", width = 15)
	@ApiModelProperty(value = "机构名称")
	private java.lang.String orgName;
	/**机构类型*/
	@Excel(name = "机构类型", width = 15)
	@ApiModelProperty(value = "机构类型")
	private java.lang.String orgType;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	
	@ExcelCollection(name="金融机构参数表")
	@ApiModelProperty(value = "金融机构参数表")
	private List<DreamlabsOrgParam> dreamlabsOrgParamList;
	
}
