/**
 * 
 */
package org.jeecg.modules.dreamlabs.transaction;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;

/**
 * @author Frank
 * 参考：https://www.cnblogs.com/wynjauu/articles/9273759.html
 */
@Data
@ToString
public class TransactionItem {
	/**
	 * 标的代码
	 */
	private String code;
	
	/**
	 * 标的名称
	 */
	private String name;
	/**
	 * 交易日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date date;
	/**
	 * 交易时间
	 */
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
	private Date time;
	/**
	 * 交易价格，净值
	 */
	private BigDecimal price;
	/**
	 * 交易类型
	 */
	private String dealType;
	/**
	 * 交易份额
	 */
	private BigDecimal dealShare;
	/**
	 * 委托份额
	 */
	private BigDecimal share;
	/**
	 * 交易金额
	 */
	private BigDecimal dealAmount;
	/**
	 * 手续费
	 */
	private BigDecimal fee;
	/**
	 * 单位
	 */
	private String unit;
	

}
