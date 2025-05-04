package at.Cinemaland.Controller.web;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Service.ActorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ActorController {


    final private ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/actors") // URL to access the actor list
    public String getActors(Model model) {
        List<Actor> actors = actorService.getActors();
        model.addAttribute("actors", actors);
        return "actor-list";
    }
}