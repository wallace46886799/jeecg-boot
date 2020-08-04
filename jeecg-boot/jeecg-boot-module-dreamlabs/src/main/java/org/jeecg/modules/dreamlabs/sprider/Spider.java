package org.jeecg.modules.dreamlabs.sprider;

import java.util.Map;
/**
 * 爬虫执行接口
 * @author Frank
 *
 */
public interface Spider {
	/**
	 * 获取信息，大体步骤如下：<br>
	 * 1、根据编号获取基础信息<br>
	 * 2、创建浏览器<br>
	 * 3、登录<br>
	 * 4、爬取信息<br>
	 * 5、退出<br>
	 * 6、销毁浏览器<br>
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> process(String id) throws Exception;
	
}
