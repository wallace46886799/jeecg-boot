/**
 * 
 */
package org.jeecg.modules.dreamlabs.transaction;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Frank
 *
 */
@Data
public class HoldingShareItem {
	/**
	 * 标的编码
	 */
	private String code;
	/**
	 * 标的名称
	 */
	private String name;
	/**
	 * 份额
	 */
	private BigDecimal shares;
	/**
	 * 可用份额
	 */
	private BigDecimal availableShares;
	/**
	 * 市值
	 */
	private BigDecimal amount;
	/**
	 * 当日涨跌比例
	 */
	private BigDecimal lastFloatingPercent = BigDecimal.ZERO;;
	/**
	 * 当日盈亏金额
	 */
	private BigDecimal lastFloatingProfit;
	/**
	 * 成本价
	 */
	private BigDecimal origPrice;
	/**
	 * 成本
	 */
	private BigDecimal origAmount;
	/**
	 * 价格、当日净值
	 */
	private BigDecimal price;
	/**
	 * 价格日期、净值日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date priceDate;
	/**
	 * 浮动盈亏比例
	 */
	private BigDecimal floatingPercent = BigDecimal.ZERO;
	/**
	 * 浮动盈亏
	 */
	private BigDecimal floatingProfit;
	/**
	 * 累计盈亏，一般不向客户端返回
	 */
	private BigDecimal profit;
	/**
	 * 累计盈亏比例，一般不向客户端返回
	 */
	private BigDecimal pecent = BigDecimal.ZERO;
	/**
	 * 资产占比
	 */
	private BigDecimal assetPercent;

}
