package ru.otus.akutsev.books.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.akutsev.books.dao.AuthorSqlDao;
import ru.otus.akutsev.books.dao.BookSqlDao;
import ru.otus.akutsev.books.dao.GenreSqlDao;
import ru.otus.akutsev.books.model.*;

import java.util.HashMap;

@Configuration
public class ConfigJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	private final Logger logger = LoggerFactory.getLogger("Batch");

	@Bean
	public MongoItemReader<AuthorDto> authorReader (MongoTemplate mongoTemplate) {
		return new MongoItemReaderBuilder<AuthorDto>()
				.name("authorReader")
				.template(mongoTemplate)
				.collection("authors")
				.targetType(AuthorDto.class)
				.jsonQuery("{}")
				.sorts(new HashMap<>())
				.build();
	}

	@Bean
	public MongoItemReader<GenreDto> genreReader (MongoTemplate mongoTemplate) {
		return new MongoItemReaderBuilder<GenreDto>()
				.name("genreReader")
				.template(mongoTemplate)
				.collection("genres")
				.targetType(GenreDto.class)
				.jsonQuery("{}")
				.sorts(new HashMap<>())
				.build();
	}

	@Bean
	public MongoItemReader<BookDto> bookReader (MongoTemplate mongoTemplate) {
		return new MongoItemReaderBuilder<BookDto>()
				.name("bookReader")
				.template(mongoTemplate)
				.collection("books")
				.targetType(BookDto.class)
				.jsonQuery("{}")
				.sorts(new HashMap<>())
				.build();
	}

	@Bean
	public ItemProcessor<AuthorDto, Author> authorConverter() {
		return new ProcessorAuthor();
	}

	@Bean
	public ItemProcessor<GenreDto, Genre> genreConverter() {
		return new ProcessorGenre();
	}

	@Bean
	public ItemProcessor<BookDto, Book> bookConverter() {
		return new ProcessorBook();
	}

	@Bean
	public RepositoryItemWriter<Author> authorWriter(AuthorSqlDao authorSqlDao) {
		RepositoryItemWriter<Author> writer = new RepositoryItemWriter<>();

		writer.setRepository(authorSqlDao);
		writer.setMethodName("save");

		return writer;
	}

	@Bean
	public RepositoryItemWriter<Genre> genreWriter(GenreSqlDao genreSqlDao) {
		RepositoryItemWriter<Genre> writer = new RepositoryItemWriter<>();

		writer.setRepository(genreSqlDao);
		writer.setMethodName("save");

		return writer;
	}

	@Bean
	public RepositoryItemWriter<Book> bookWriter(BookSqlDao bookSqlDao) {
		RepositoryItemWriter<Book> writer = new RepositoryItemWriter<>();

		writer.setRepository(bookSqlDao);
		writer.setMethodName("save");

		return writer;
	}

	@Bean("authorsMigration")
	public Step authorsMigration(RepositoryItemWriter<Author> authorWriter, MongoItemReader<AuthorDto> authorReader,
								 ItemProcessor<AuthorDto, Author> authorProcessor) {
		return stepBuilderFactory.get("authorsMigration")
				.<AuthorDto, Author>chunk(3)
				.reader(authorReader)
				.processor(authorProcessor)
				.writer(authorWriter)
				.build();
	}

	@Bean("genresMigration")
	public Step genresMigration(RepositoryItemWriter<Genre> genreWriter, MongoItemReader<GenreDto> genreReader,
								 ItemProcessor<GenreDto, Genre> genreProcessor) {
		return stepBuilderFactory.get("genresMigration")
				.<GenreDto, Genre>chunk(3)
				.reader(genreReader)
				.processor(genreProcessor)
				.writer(genreWriter)
				.build();
	}

	@Bean("booksMigration")
	public Step booksMigration(RepositoryItemWriter<Book> bookWriter, MongoItemReader<BookDto> bookReader,
							   ItemProcessor<BookDto, Book> bookProcessor) {
		return stepBuilderFactory.get("booksMigration")
				.<BookDto, Book>chunk(3)
				.reader(bookReader)
				.processor(bookProcessor)
				.writer(bookWriter)
				.build();
	}

	@Bean
	public Job migrateBooksWithNestedData(@Qualifier("authorsMigration") Step authorsMigration,
										  @Qualifier("genresMigration") Step genreMigration,
										  @Qualifier("booksMigration") Step bookMigration) {
		return jobBuilderFactory.get("migrateBooksWithNestedData")
				.incrementer(new RunIdIncrementer())
				.flow(authorsMigration)
				.next(genreMigration)
				.next(bookMigration)
				.end()
				.listener(new JobExecutionListener() {
					@Override
					public void beforeJob(JobExecution jobExecution) {
						logger.info("Начало job");
					}

					@Override
					public void afterJob(JobExecution jobExecution) {
						logger.info("Конец job");
					}
				})
				.build();
	}

}
