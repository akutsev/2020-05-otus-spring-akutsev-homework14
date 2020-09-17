package ru.otus.akutsev.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.akutsev.books.dao.exceptions.NoSuchBookException;
import ru.otus.akutsev.books.dao.BookSqlDao;
import ru.otus.akutsev.books.dao.CommentSqlDao;
import ru.otus.akutsev.books.model.Author;
import ru.otus.akutsev.books.model.Book;
import ru.otus.akutsev.books.model.Comment;
import ru.otus.akutsev.books.model.Genre;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private MutableAclService mutableAclService;
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
		if (book.getId() > 0) { return bookSqlDao.save(book); } //if existed book is saved

		Book newBook = bookSqlDao.save(book);

		boolean grantingLevTolstoy = !newBook.getAuthor().getName().equals(LEV_TOLSTOY_AUTHOR_RESTRICTION);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final Sid owner = new PrincipalSid( authentication );
		ObjectIdentity oid = new ObjectIdentityImpl( newBook.getClass(), newBook.getId() );
		final Sid userUser = new PrincipalSid("User");
		final Sid roleUser = new GrantedAuthoritySid("ROLE_USER");
		final Sid roleAdmin = new GrantedAuthoritySid("ROLE_ADMIN");
		MutableAcl acl = mutableAclService.createAcl( oid );
		acl.setOwner( owner );

		acl.insertAce( acl.getEntries().size(), BasePermission.WRITE, roleUser, grantingLevTolstoy );
		acl.insertAce( acl.getEntries().size(), BasePermission.WRITE, userUser, true );
		acl.insertAce( acl.getEntries().size(), BasePermission.WRITE, roleAdmin, true );

		return newBook;
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasPermission(#id, 'ru.otus.akutsev.books.model.Book', write)")
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
	@Secured("ROLE_ADMIN")
	public void deleteById (long id) {
		bookSqlDao.deleteById(id);
	}
}
