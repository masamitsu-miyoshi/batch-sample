package com.example.batch_sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Taskletってコントローラみたいなもの。
 */
@StepScope
@Component
public class AppTasklet implements Tasklet {

    private static final Logger logger =
            LoggerFactory.getLogger(AppTasklet.class);
	
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    MessageQue messageQue;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("SQL START");
		
		// SQL TEST
		
		var message = messageQue.pop();
		
		var first = jdbc.queryForList("SELECT * FROM TBL1");
		
		// 命令
		if (message != null) {
			jdbc.update("INSERT INTO TBL1 (COL1, COL2) VALUES (?,?)", first.size(), "abc");
			logger.info("SQL OK");
		}
		
		logger.info("CURRENT NUM:" + first.size());

		if (first.size() < 7) {
			// 繰り返しループ
			messageQue.push("NEXTDO");
			logger.info("under 7");
		}
		
		return RepeatStatus.FINISHED;
	}
}
