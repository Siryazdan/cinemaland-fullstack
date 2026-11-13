package at.Cinemaland.Controller.api;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorApiController {

    final ActorService actorService;

    public ActorApiController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public Actor createActor(@RequestBody Actor actor) {
        return actorService.createActor(actor);
    }

    @GetMapping({"", "/"})
    public List<Actor> getActors() {
        return actorService.getActors();
    }

    @GetMapping("/{id}")
    public Actor getActor(@PathVariable("id") String id) {
        Actor actor = actorService.getActor(id);
        if (actor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
        }
        return actor;
    }

    @PutMapping("/{id}")
    public Actor updateActor(@PathVariable("id") String id, @RequestBody Actor actor) {
        Actor updated = actorService.updateActor(id, actor);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable("id") String id) {
        actorService.deleteActor(id);
    }
}
