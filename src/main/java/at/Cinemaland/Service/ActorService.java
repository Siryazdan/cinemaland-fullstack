package at.cinemaland.Service;

import at.cinemaland.Model.Actor;
import at.cinemaland.Repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public void deleteActor(String id) {
        fileDataService.deleteFileByRelId(id);
        actorRepository.deleteById(id);
    }

    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

}
