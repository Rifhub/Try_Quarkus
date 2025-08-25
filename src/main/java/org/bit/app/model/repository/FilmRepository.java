package org.bit.app.model.repository;


import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bit.app.model.Film;
import org.bit.app.model.Film$;

import java.util.Optional;

@ApplicationScoped
public class FilmRepository {

    @Inject
    JPAStreamer jpaStreamer;

    public Optional<Film> getFilmById(Long filmId) {
        return jpaStreamer.stream(Film.class)
                .filter(film -> film.getId().equals(filmId))
                .findFirst();
    }
}
