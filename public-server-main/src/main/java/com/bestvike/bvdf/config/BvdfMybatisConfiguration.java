package com.bestvike.bvdf.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(value = "com.bestvike.bvdf.dao")
public class BvdfMybatisConfiguration implements ApplicationContextAware {

	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	}

	@Bean(name = "bvdfDataSource")
	@ConfigurationProperties(prefix = "datasources.bvdf")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "bvdfSqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		sqlSessionFactoryBean.setTypeAliasesPackage("com.bestvike.bvdf.entity");
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/*.xml"));
		tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
//		// 设置驼峰不自动转下划线
//		Config config = new Config();
//		config.setStyle(Style.normal);
//		configuration.setConfig(config);
		sqlSessionFactoryBean.setConfiguration(configuration);

		// 分页插件
		Interceptor interceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("helperDialect", "oracle");
		// properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("params", "pageNum=page;pageSize=limit;");
		interceptor.setProperties(properties);
		sqlSessionFactoryBean.setPlugins(new Interceptor[]{interceptor});
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "bvdfSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name = "bvdfTransactionManager")
	@Primary
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}