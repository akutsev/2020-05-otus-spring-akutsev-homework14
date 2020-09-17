package ru.otus.akutsev.books.model;

public class BookDto {
//	private String id;
//	private String name;
//	private String authorMongoId;
//	private String genreMongoId;
//	private List<String> comments;
//
//	public BookDto(String name, String authorMongoId, String genreName, List<String> comments) {
//		this.name = name;
//		this.authorMongoId = authorMongoId;
//		this.genreMongoId = genreName;
//		this.comments = comments;
//	}

	private String _id;
	private String name;
	private AuthorDto author;
	private GenreDto genre;

	public BookDto(String _id, String name, AuthorDto author, GenreDto genre) {
		this._id = _id;
		this.name = name;
		this.author = author;
		this.genre = genre;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AuthorDto getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDto author) {
		this.author = author;
	}

	public GenreDto getGenre() {
		return genre;
	}

	public void setGenre(GenreDto genre) {
		this.genre = genre;
	}
}
