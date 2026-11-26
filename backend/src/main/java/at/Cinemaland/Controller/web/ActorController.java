package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Model.Movie;
import at.Cinemaland.Service.ActorService;
import at.Cinemaland.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActorController {

    private final ActorService actorService;
    private final MovieService movieService;

    public ActorController(ActorService actorService, MovieService movieService) {
        this.actorService = actorService;
        this.movieService = movieService;
    }

    @GetMapping("/actors")
    public String getActors(Model model) {
        List<Actor> actors = actorService.getActors().stream()
                .sorted(Comparator.comparing(Actor::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();

        // which of my movies does each actor appear in?
        Map<String, List<Movie>> moviesByActor = new HashMap<>();
        for (Movie movie : movieService.getMovies()) {
            for (Actor actor : movie.getActors()) {
                moviesByActor.computeIfAbsent(actor.getId(), id -> new ArrayList<>()).add(movie);
            }
        }

        model.addAttribute("actors", actors);
        model.addAttribute("moviesByActor", moviesByActor);
        return "actor-list";
    }
}
