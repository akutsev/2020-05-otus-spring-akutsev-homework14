package ru.otus.akutsev.books.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.akutsev.books.dao.AuthorSqlDao;
import ru.otus.akutsev.books.dao.BookSqlDao;
import ru.otus.akutsev.books.dao.GenreSqlDao;
import ru.otus.akutsev.books.service.BookService;

import java.util.Collection;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@SpringBatchTest
@DisplayName("SpringBatch тест")
public class BatchTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;
	@Autowired
	private BookSqlDao bookSqlDao;

	@BeforeEach
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	@DisplayName("Тест наличия всех книг из MongoDB")
	public void whenBooksMigrationExecuted_thenSuccess() {
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("booksMigration");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		assertEquals(1, actualStepExecutions.size());
		assertEquals("COMPLETED", actualExitStatus.getExitCode());
		assertEquals(9, bookSqlDao.findAll().size());
	}

}
