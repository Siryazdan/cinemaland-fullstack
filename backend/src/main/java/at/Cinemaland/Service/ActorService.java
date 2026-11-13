package at.Cinemaland.Service;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ActorService {

    final ActorRepository actorRepository;
    final FileDataService fileDataService;

    public ActorService(ActorRepository actorRepository, FileDataService fileDataService) {
        this.actorRepository = actorRepository;
        this.fileDataService = fileDataService;
    }

    public Actor getActor(String id) {
        return actorRepository.findById(id).orElse(null);
    }

    public Actor createActor(Actor actor, MultipartFile image) throws IOException {
        actor.setId(null);
        Actor entity = actorRepository.save(actor);
        fileDataService.createFileData(image, entity.getId());

        return entity;
    }

    public Actor updateActor(String actorId, Actor actor) {
        Actor actorToUpdate = actorRepository.findById(actorId).orElse(null);
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
        return actorRepository.save(actor);
    }

    public void deleteActor(String id) {
        fileDataService.deleteFileByRelId(id);
        actorRepository.deleteById(id);
    }

    public List<Actor> getActors() {
        return actorRepository.findAll();
    }

}
