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
public class FundShareItem extends HoldingShareItem {
	
	/**
	 * 基金类型：
	 * 货币型-0
	 * 股票型-1
	 * 债券型-2
	 * 指数型-3
	 */
	private String type;
	
}
