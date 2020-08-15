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
 * 参考：https://www.cnblogs.com/wynjauu/articles/9273759.html
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StockEntrustItem extends EntrustItem {
	
}
