package com.example.batch_sample;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class AppErrTasklet implements Tasklet {
	
    @Autowired
    JdbcTemplate jdbc;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("SQL START");
		
		// SQL TEST

		var first = jdbc.queryForList("SELECT * FROM TBL1");
		System.out.println("FIRST NUM:" + first.size());

		
		for (var i = 0; i < 10; i++) {
			jdbc.update("INSERT INTO TBL1 (COL1, COL2) VALUES (?,?)", i, "abc");
		}
		
		var last = jdbc.queryForList("SELECT * FROM TBL1");
		var lastSize = last.size();
		System.out.println("LAST NUM:" + lastSize);
		
		
		
		System.out.println("SQL OK");
		
		
		if (200 <= lastSize)
			throw new Exception("overflow data.");
		
		return RepeatStatus.FINISHED; // continuableでcommitが走る。
	}
}
