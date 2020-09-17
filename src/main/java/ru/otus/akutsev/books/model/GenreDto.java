package ru.otus.akutsev.books.model;

public class GenreDto {
	private String _id;
	private String genreName;

	public GenreDto() {
	}

	public GenreDto(String _id, String genreName) {
		this._id = _id;
		this.genreName = genreName;
	}

	public GenreDto(String genreName) {
		this.genreName = genreName;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
}
