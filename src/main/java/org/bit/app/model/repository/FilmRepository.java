package org.bit.app.model.repository;


import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.projection.Projection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bit.app.model.Film;
import org.bit.app.model.Film$;

import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class FilmRepository {

    private static final int PAGE_SIZE = 15;

    @Inject
    JPAStreamer jpaStreamer;

    public Optional<Film> getFilmById(Long filmId) {
        return jpaStreamer.stream(Film.class)
                .filter(film -> film.getId().equals(filmId))
                .findFirst();
    }

    public Stream<Film> paged(long page, short minLength) {
        long offset = PAGE_SIZE * Math.max(0, page - 1);
        return jpaStreamer.stream(Projection.select(Film$.id, Film$.title, Film$.length))
                .filter(film -> film.getLength() >= minLength)
                .sorted(Film$.length)
                .skip(offset)
                .limit(PAGE_SIZE);
    }
}
