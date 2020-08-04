package org.jeecg.modules.dreamlabs.transaction;

import java.util.Map;

public interface Transaction {
	
	public Map<String, Object> doTransaction(String id);

}
