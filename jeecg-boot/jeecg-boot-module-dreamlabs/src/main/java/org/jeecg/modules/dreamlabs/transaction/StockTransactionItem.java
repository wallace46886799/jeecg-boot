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
public class StockTransactionItem extends TransactionItem {
	
}
