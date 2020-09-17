package ru.otus.akutsev.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.akutsev.books.dao.GenreSqlDao;
import ru.otus.akutsev.books.model.Genre;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{

	private final GenreSqlDao genreSqlDao;

	@Autowired
	public GenreServiceImpl(GenreSqlDao genreSqlDao) {
		this.genreSqlDao = genreSqlDao;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Genre> findAll() {
		return genreSqlDao.findAll();
	}

	@Transactional
	@Override
	public Genre save(Genre genre) {
		return genreSqlDao.save(genre);
	}
}
