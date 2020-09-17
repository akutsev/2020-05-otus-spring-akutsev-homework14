package ru.otus.akutsev.books.dao.exceptions;

public class NoSuchGenreException extends RuntimeException{
	public NoSuchGenreException() {
	}

	public NoSuchGenreException(String message) {
		super(message);
	}
}
