import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bit.app.model.Film;
import org.bit.app.model.repository.FilmRepository;

import java.util.List;
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
    public Response paged(@PathParam("page") long page, @PathParam("minlength") short minLength) {
        if (page < 1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Page number must be >= 1")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        String body = filmRepository.paged(page, minLength)
                .map(film -> String.format(
                        "%s %s (%s min)",
                        String.valueOf(film.getTitle()),
                        String.valueOf(film.getReleaseYear()),
                        film.getLength() == null ? "?" : String.valueOf(film.getLength())
                ))
                .collect(Collectors.joining("\n"));

        return Response.ok(body, MediaType.TEXT_PLAIN).build();
    }



    @GET
    @Path("/actors/{actorName}/{minlength}/{year}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(@PathParam("actorName")String actorName, @PathParam("minlength") short minLength,@PathParam("year") Integer year){
        return filmRepository.actors(actorName, minLength, year)
                .map(film -> String.format("%s %s (%d min): %s ",
                        film.getTitle(),
                        film.getReleaseYear(),
                        film.getLength(),
                        film.getActors().stream()
                                .map(a ->String.format("%s %s",a.getFirstName(),a.getLastName()))
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/update/{minlength}/{rate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(@PathParam("minlength")short minLength, @PathParam("rate") Float rate){
        filmRepository.updateRentalRate(minLength, rate);
        return filmRepository.getFilm(minLength)
                .map(film -> String.format("%s (%d min) - $%f", film.getTitle(),film.getLength(),film.getRentalRate()))
                .collect(Collectors.joining("\n"));
    }

}
