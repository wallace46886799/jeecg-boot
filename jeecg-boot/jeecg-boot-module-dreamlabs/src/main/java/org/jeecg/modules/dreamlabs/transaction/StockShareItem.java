/**
 * 
 */
package org.jeecg.modules.dreamlabs.transaction;

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
public class StockShareItem  extends HoldingShareItem{
	/**
	 * 市场类型
	 * 沪市-1
	 * 深市-2
	 * 创业板-3
	 * 科创板-4
	 * 新三板-5
	 */
	private String marketType;

}
