package com.dreamlabs.core.covert;

import java.math.BigDecimal;

import cn.hutool.core.convert.Converter;
import cn.hutool.core.util.StrUtil;
import me.zhyd.oauth.log.Log;

public class BigDecimalConverter  implements Converter<BigDecimal>{
	@Override
	public BigDecimal convert(Object value, BigDecimal defaultValue) throws IllegalArgumentException {
		if(value == null) {
			return BigDecimal.ZERO;
		}
		String origValue = (String) value;
		if (StrUtil.isEmptyOrUndefined(origValue) || StrUtil.equalsIgnoreCase(origValue, "--")) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			if (StrUtil.contains(origValue, '%')) {
				origValue = StrUtil.removeAll(origValue,"%");
				origValue = StrUtil.removeAll(origValue,",");
				origValue = StrUtil.removeAll(origValue," ");
				result = new BigDecimal(origValue).multiply(new BigDecimal("0.01")).setScale(4, BigDecimal.ROUND_HALF_UP);
			} else {
				origValue = StrUtil.removeAll(origValue, "¥");
				origValue = StrUtil.removeAll(origValue, ",");
				origValue = StrUtil.removeAll(origValue, " ");
				result = new BigDecimal(origValue).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}catch(Exception e) {
			Log.error("Error.",e);
		}
		
		return result;
	}
}
