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
public class EntrustItem {
	/**
	 * 标的代码
	 */
	private String code;
	
	/**
	 * 标的名称
	 */
	private String name;
	/**
	 * 委托日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date date;
	/**
	 * 委托时间
	 */
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
	private Date time;
	/**
	 * 委托价格
	 */
	private BigDecimal price;
	/**
	 * 委托类型
	 */
	private String entrustType;
	/**
	 * 委托份额
	 */
	private BigDecimal entrustShare;
	/**
	 * 委托状态
	 */
	private String entrustStatus;
	/**
	 * 委托份额
	 */
	private BigDecimal share;
	/**
	 * 委托金额
	 */
	private BigDecimal entrustAmount;
	/**
	 * 手续费
	 */
	private BigDecimal fee;
	/**
	 * 单位
	 */
	private String unit;
	
	

}
