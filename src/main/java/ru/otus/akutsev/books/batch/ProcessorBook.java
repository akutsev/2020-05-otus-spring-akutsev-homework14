package ru.otus.akutsev.books.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.akutsev.books.dao.exceptions.NoSuchAuthorException;
import ru.otus.akutsev.books.dao.exceptions.NoSuchBookException;
import ru.otus.akutsev.books.dao.AuthorSqlDao;
import ru.otus.akutsev.books.dao.GenreSqlDao;
import ru.otus.akutsev.books.dao.exceptions.NoSuchGenreException;
import ru.otus.akutsev.books.model.Author;
import ru.otus.akutsev.books.model.Book;
import ru.otus.akutsev.books.model.BookDto;
import ru.otus.akutsev.books.model.Genre;

public class ProcessorBook implements ItemProcessor<BookDto, Book> {

	@Autowired
	private AuthorSqlDao authorSqlDao;
	@Autowired
	private GenreSqlDao genreSqlDao;

	@Override
	public Book process(BookDto bookDto) throws Exception {
		Author author = authorSqlDao.findByName(bookDto.getAuthor().getName()).orElseThrow(NoSuchAuthorException::new);
		Genre genre = genreSqlDao.findByGenreName(bookDto.getGenre().getGenreName()).orElseThrow(NoSuchGenreException::new);

		return new Book(bookDto.getName(), author, genre);
	}
}
