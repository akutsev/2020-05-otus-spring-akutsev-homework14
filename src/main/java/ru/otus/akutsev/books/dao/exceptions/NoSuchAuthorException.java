package ru.otus.akutsev.books.dao.exceptions;

public class NoSuchAuthorException extends RuntimeException {
	public NoSuchAuthorException(String message) {
		super(message);
	}

	public NoSuchAuthorException() {
	}
}
