package ru.otus.akutsev.books.batch;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.akutsev.books.model.Genre;
import ru.otus.akutsev.books.model.GenreDto;

public class ProcessorGenre implements ItemProcessor<GenreDto, Genre> {
	@Override
	public Genre process(GenreDto genreDto) throws Exception {
		return new Genre(genreDto.getGenreName());
	}
}
