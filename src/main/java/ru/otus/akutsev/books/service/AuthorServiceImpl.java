package ru.otus.akutsev.books.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.akutsev.books.dao.AuthorSqlDao;
import ru.otus.akutsev.books.model.Author;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{

	private final AuthorSqlDao authorDao;

	public AuthorServiceImpl(AuthorSqlDao authorDao) {
		this.authorDao = authorDao;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Author> getAll() {
		return authorDao.findAll();
	}

	@Transactional
	@Override
	public Author save(Author author) {
		return authorDao.save(author);
	}
}
