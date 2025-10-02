package org.bit.app.model.repository;


import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bit.app.model.Film;
import org.bit.app.model.Film$;

import java.math.BigDecimal;
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

    public Stream<Film> getFilm(short minLength) {
        return jpaStreamer.stream(Film.class)
                .filter(film -> film.getLength() >= minLength)
                .sorted(Film$.length);
    }

    public Stream<Film> paged(long page, short minLength) {
        long offset = PAGE_SIZE * Math.max(0, page - 1);
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be >= 1");
        }
        return jpaStreamer.stream(Projection.select(Film$.id, Film$.title, Film$.length, Film$.releaseYear))
                .filter(Film$.length.greaterThan(minLength))
                .sorted(Film$.length)
                .skip(offset)
                .limit(PAGE_SIZE);
    }


    public Stream<Film> actors(String actorName, short minLength, Integer year) {
        final StreamConfiguration<Film> sc
                = StreamConfiguration.of(Film.class).joining(Film$.actors);
        return jpaStreamer.stream(sc)
                .filter(Film$.title.startsWith(actorName).and(Film$.length.greaterThan(minLength)).and(Film$.releaseYear.greaterThan(year)))
                .sorted(Film$.length.reversed());
    }

    @Transactional
    public void updateRentalRate(short minLength, Float rate) {
         jpaStreamer.stream(Film.class)
                .filter(Film$.length.greaterThan(minLength))
                .forEach(f -> {
                    f.setRentalRate(BigDecimal.valueOf(rate));
                });
    }
}
