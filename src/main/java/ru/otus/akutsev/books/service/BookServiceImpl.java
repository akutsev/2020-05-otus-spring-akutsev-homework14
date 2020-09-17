package ru.otus.akutsev.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.akutsev.books.dao.BookSqlDao;
import ru.otus.akutsev.books.dao.CommentSqlDao;
import ru.otus.akutsev.books.dao.exceptions.NoSuchBookException;
import ru.otus.akutsev.books.model.Author;
import ru.otus.akutsev.books.model.Book;
import ru.otus.akutsev.books.model.Comment;
import ru.otus.akutsev.books.model.Genre;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private final BookSqlDao bookSqlDao;
	@Autowired
	private final CommentSqlDao commentSqlDao;

	@Autowired
	public BookServiceImpl(BookSqlDao bookSqlDao, CommentSqlDao commentSqlDao) {
		this.bookSqlDao = bookSqlDao;
		this.commentSqlDao = commentSqlDao;
	}

	private static final String LEV_TOLSTOY_AUTHOR_RESTRICTION = "Lev Tolstoy";

	@Override
	@Transactional
	public Book save(Book book) {
		return bookSqlDao.save(book);
	}

	@Override
	@Transactional(readOnly = true)
	public Book getAById(long id) {
		return bookSqlDao.findAById(id).orElseThrow(NoSuchBookException::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getAll() {
		return bookSqlDao.findAll();
	}

	@Override
	@Transactional
	public Book updateBook (Book book, String newName, Author newAuthor, Genre newGenre) {
		book.setAuthor(newAuthor);
		book.setGenre(newGenre);
		book.setName(newName);

		return bookSqlDao.save(book);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> getAllComments(Book book) {
		return commentSqlDao.findByBook(book);
	}

	@Override
	@Transactional
	public void deleteById (long id) {
		bookSqlDao.deleteById(id);
	}
}
