package at.Cinemaland.Service;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Repository.ActorRepository;
import at.Cinemaland.Security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/** Actors of the logged-in user only. */
@Service
public class ActorService {

    final ActorRepository actorRepository;
    final FileDataService fileDataService;
    final CurrentUser currentUser;

    public ActorService(ActorRepository actorRepository, FileDataService fileDataService, CurrentUser currentUser) {
        this.actorRepository = actorRepository;
        this.fileDataService = fileDataService;
        this.currentUser = currentUser;
    }

    /** The actor, or null if it doesn't exist or belongs to another user. */
    public Actor getActor(String id) {
        return actorRepository.findByIdAndUserId(id, currentUser.id()).orElse(null);
    }

    public Actor createActor(Actor actor, MultipartFile image) throws IOException {
        Actor entity = createActor(actor);
        fileDataService.createFileData(image, entity.getId());
        return entity;
    }

    public Actor updateActor(String actorId, Actor actor) {
        Actor actorToUpdate = getActor(actorId);
        if (actorToUpdate != null) {
            actorToUpdate.setName(actor.getName());
            actorToUpdate.setAge(actor.getAge());
            actorToUpdate.setHeight(actor.getHeight());
            return actorRepository.save(actorToUpdate);
        }
        return null;
    }

    public Actor createActor(Actor actor) {
        actor.setId(null); // a new actor always gets a new ID
        actor.setUserId(currentUser.id());
        return actorRepository.save(actor);
    }

    /** Deletes the actor if it belongs to the current user. Returns false if not found. */
    public boolean deleteActor(String id) {
        Actor actor = getActor(id);
        if (actor == null) {
            return false;
        }
        fileDataService.deleteFileByRelId(id);
        actorRepository.delete(actor);
        return true;
    }

    public List<Actor> getActors() {
        return actorRepository.findByUserId(currentUser.id());
    }
}
