import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bit.app.model.Film;
import org.bit.app.model.repository.FilmRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Path("/")
public class FilmResource {

    @Inject
    FilmRepository filmRepository;

    @GET
    @Path("/helloWorld")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello World";
    }
    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilm(@PathParam("filmId") Long filmId) {
        return filmRepository.getFilmById(filmId)
                .map(Film::getTitle)
                .orElse("Film not found");
    }

    @GET
    @Path("/filmPage/{page}/{minlength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String paged(@PathParam("page")long page, @PathParam("minlength") short minLength){
        return filmRepository.paged(page, minLength)
                .map(film -> String.format("%s (%d min)", film.getTitle(),film.getLength()))
                .collect(Collectors.joining("\n"));
    }

}
