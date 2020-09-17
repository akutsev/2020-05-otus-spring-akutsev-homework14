package ru.otus.akutsev.books.batch;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.akutsev.books.model.Author;
import ru.otus.akutsev.books.model.AuthorDto;

public class ProcessorAuthor implements ItemProcessor<AuthorDto, Author> {
	@Override
	public Author process(AuthorDto authorDto) throws Exception {
		return new Author(authorDto.getName());
	}
}
