package org.bit.app.model.repository;


import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.inject.Inject;
import org.bit.app.model.Film;

import java.util.Optional;

public class FilmRepository {

    @Inject
    JPAStreamer jpaStreamer;

    public Optional<Film> getFilm(short id) {
        return jpaStreamer.stream(Film.class)
                .filter(f -> f.getId() == id)
                .findFirst();
    }
}
