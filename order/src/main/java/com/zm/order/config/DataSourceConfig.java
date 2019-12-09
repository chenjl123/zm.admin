package com.zm.order.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

/**
 * 数据库链接配置
 */
@Configuration
public class DataSourceConfig {

    /**
     * 初始化连接池
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 设置数据源代理
     * @return
     */
    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSourceProxy() {
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy();
        transactionAwareDataSourceProxy.setTargetDataSource(druidDataSource());
        return transactionAwareDataSourceProxy;
    }

    /**
     * 设置ActiveRecord
     * @return
     */
    @Bean
    public ActiveRecordPlugin activeRecordPlugin() {
        ActiveRecordPlugin arp  = new ActiveRecordPlugin(transactionAwareDataSourceProxy());
        arp.setDialect(new MysqlDialect());
        arp.setShowSql(true);
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
        arp.start();
        return arp;
    }

    /**
     *设置事务管理
     * @return
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(transactionAwareDataSourceProxy());
        return dataSourceTransactionManager;
    }

}
