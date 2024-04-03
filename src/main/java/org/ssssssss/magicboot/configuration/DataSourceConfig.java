package org.ssssssss.magicboot.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.ssssssss.magicapi.datasource.model.MagicDynamicDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    @Primary  //标记主数据源，当存在多个DataSource Bean时，默认使用该数据源
    @Bean(name = "master")  //创建一个名为"master"的DataSource Bean
    @Qualifier("master")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.master")  //将属性文件配置注入到DataSource实例中
    public DataSource magicDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "gaussdb")
    @Qualifier("gaussdb")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.gaussdb")
    public DataSource pmDataSource(){
        return DataSourceBuilder.create().build();
    }

    // 创建一个MagicDynamicDataSource类型的Bean，用于管理多个数据源并实现动态切换
    @Bean
    public MagicDynamicDataSource magicDynamicDataSource(){
        MagicDynamicDataSource dynamicDataSource = new MagicDynamicDataSource();
        // 将默认数据源设置为m1，并将gaussdb的DataSource加入到动态数据源管理器中
        dynamicDataSource.setDefault(magicDataSource());
        dynamicDataSource.add("gaussdb",pmDataSource());
        return dynamicDataSource;
    }
}