package com.example.batch_sample;

import javax.sql.DataSource;

import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


@Configuration
public class MetadataConfiguration {
	
	/**
	 * メタデータを無効化
	 * @return
	 */
	@BatchDataSource
    @Bean
    public DataSource metaDataSource() {
		var a = new TaskletStep();
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }
	
    @Primary //メインテーブルの接続先に指定
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mainDataSource(){
        return DataSourceBuilder.create().build();
    }
}
