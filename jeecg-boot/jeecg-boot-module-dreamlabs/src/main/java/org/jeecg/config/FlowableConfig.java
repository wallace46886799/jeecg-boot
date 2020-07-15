package org.jeecg.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Author: fumin
 * @Description:
 * @Date: Create in 2019/5/20 10:26
 */
@Configuration
public class FlowableConfig {
	/*
	 * @Bean public void processEngine(DataSourceTransactionManager
	 * transactionManager, DataSource dataSource) throws IOException {
	 * SpringProcessEngineConfiguration configuration = new
	 * SpringProcessEngineConfiguration(); //自动部署已有的流程文件 Resource[] resources = new
	 * PathMatchingResourcePatternResolver().getResources(ResourceLoader.
	 * CLASSPATH_URL_PREFIX + "processes/*.bpmn");
	 * configuration.setTransactionManager(transactionManager); // 执行工作流对应的数据源
	 * configuration.setDataSource(dataSource); // 是否自动创建流程引擎表
	 * configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.
	 * DB_SCHEMA_UPDATE_TRUE); configuration.setDeploymentResources(resources);
	 * //configuration.setDbIdentityUsed(false);
	 * //configuration.setAsyncExecutorActivate(false); // 流程历史等级
	 * //configuration.setHistoryLevel(HistoryLevel.FULL); // 流程图字体
	 * configuration.setActivityFontName("宋体");
	 * configuration.setAnnotationFontName("宋体");
	 * configuration.setLabelFontName("宋体"); //return configuration; }
	 * 
	 * @Bean public RepositoryService repositoryService(ProcessEngine processEngine)
	 * { return processEngine.getRepositoryService(); }
	 * 
	 * @Bean public RuntimeService runtimeService(ProcessEngine processEngine) {
	 * return processEngine.getRuntimeService(); }
	 * 
	 * @Bean public TaskService taskService(ProcessEngine processEngine) { return
	 * processEngine.getTaskService(); }
	 * 
	 * @Bean public HistoryService historyService(ProcessEngine processEngine) {
	 * return processEngine.getHistoryService(); }
	 * 
	 * @Bean public ManagementService managementService(ProcessEngine processEngine)
	 * { return processEngine.getManagementService(); }
	 * 
	 * @Bean public IdentityService identityService(ProcessEngine processEngine) {
	 * return processEngine.getIdentityService(); }
	 * 
	 * @Bean public FormService formService(ProcessEngine processEngine) { return
	 * processEngine.getFormService(); }
	 */
    
}
