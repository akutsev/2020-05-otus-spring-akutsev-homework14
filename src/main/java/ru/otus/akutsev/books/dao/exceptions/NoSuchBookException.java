package ru.otus.akutsev.books.dao.exceptions;

public class NoSuchBookException extends RuntimeException{
	public NoSuchBookException(String message) {
		super(message);
	}

	public NoSuchBookException() {
	}
}
