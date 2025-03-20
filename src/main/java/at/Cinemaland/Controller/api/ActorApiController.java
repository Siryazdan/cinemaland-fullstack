package at.Cinemaland.Controller.api;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Service.ActorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/actors/")
public class ActorApiController {

    final ActorService actorService;

    public ActorApiController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    public Actor createActor(@RequestBody Actor actor) {
        return actorService.createActor(actor);
    }

    @GetMapping
    public List<Actor> getActors() {
        return actorService.getActors();
    }

    @GetMapping("/{id}")
    public Actor getActor(@PathVariable String id) {
        return actorService.getActor(id);
    }

    @PutMapping("/{id}")
    public Actor updateActor(@PathVariable String id, @RequestBody Actor actor) {
        return actorService.updateActor(id, actor);
    }

    @DeleteMapping("/{id}")
    public void deleteActor(@PathVariable String id) {
        actorService.deleteActor(id);
    }
}