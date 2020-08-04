package org.jeecg.config;

import org.jeecg.modules.dreamlabs.efund.sprider.EFundHoldingSpider;
import org.jeecg.modules.dreamlabs.efund.sprider.EFundTransactionSpider;
import org.jeecg.modules.dreamlabs.fullgoal.sprider.FullGoalHoldingSpider;
import org.jeecg.modules.dreamlabs.fullgoal.sprider.FullGoalTransactionSpider;
import org.jeecg.modules.dreamlabs.gffund.sprider.GfFundHoldingSpider;
import org.jeecg.modules.dreamlabs.gffund.sprider.GfFundTransactionSpider;
import org.jeecg.modules.dreamlabs.gtja.sprider.GtjaHoldingSpider;
import org.jeecg.modules.dreamlabs.gtja.sprider.GtjaTransactionSpider;
import org.jeecg.modules.dreamlabs.jsfund.sprider.JsFundHoldingSpider;
import org.jeecg.modules.dreamlabs.jsfund.sprider.JsFundTransactionSpider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpiderConfig {
	
	@Bean(name="gtjaHoldingSpider")
	public GtjaHoldingSpider gtjaHoldingSpider() {
		return new GtjaHoldingSpider();
	}
	
	@Bean(name="gtjaTransactionSpider")
	public GtjaTransactionSpider gtjaTransactionSpider() {
		return new GtjaTransactionSpider();
	}
	
	@Bean(name="eFundHoldingSpider")
	public EFundHoldingSpider eFundHoldingSpider() {
		return new EFundHoldingSpider();
	}
	
	@Bean(name="eFundTransactionSpider")
	public EFundTransactionSpider eFundTransactionSpider() {
		return new EFundTransactionSpider();
	}
	
	@Bean(name="jsFundHoldingSpider")
	public JsFundHoldingSpider jsFundHoldingSpider() {
		return new JsFundHoldingSpider();
	}
	
	@Bean(name="jsFundTransactionSpider")
	public JsFundTransactionSpider jsFundTransactionSpider() {
		return new JsFundTransactionSpider();
	}
	
	@Bean(name="gfFundHoldingSpider")
	public GfFundHoldingSpider gfFundHoldingSpider() {
		return new GfFundHoldingSpider();
	}
	
	@Bean(name="gfFundTransactionSpider")
	public GfFundTransactionSpider gfFundTransactionSpider() {
		return new GfFundTransactionSpider();
	}
	
	@Bean(name="fullGoalHoldingSpider")
	public FullGoalHoldingSpider fullGoalHoldingSpider() {
		return new FullGoalHoldingSpider();
	}
	
	@Bean(name="fullGoalTransactionSpider")
	public FullGoalTransactionSpider fullGoalTransactionSpider() {
		return new FullGoalTransactionSpider();
	}
}
