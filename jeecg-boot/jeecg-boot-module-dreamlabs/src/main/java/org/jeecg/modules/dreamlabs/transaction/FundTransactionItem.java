/**
 * 
 */
package org.jeecg.modules.dreamlabs.transaction;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Frank
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FundTransactionItem extends TransactionItem {
	/**
	 * 销售机构
	 */
	private String soldOrg;
	/**
	 * 确认日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date confirmDate;
	/**
	 * 确认结果
	 */
	private String confirmResult;
	
	
	
}
