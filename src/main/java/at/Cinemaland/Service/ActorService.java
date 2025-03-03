package at.Cinemaland.Service;

import at.Cinemaland.Model.Actor;
import at.Cinemaland.Repository.ActorRepository;
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

    public void createActor(Actor actor, String movieId, MultipartFile image) throws IOException {
        actor.setMovieId(movieId);
        Actor entity = actorRepository.save(actor);
        fileDataService.createFileData(image, entity.getId());

    }

    public Actor updateActor(String movieId, String ActorId, Actor actor) {
        actor.setId(ActorId);
        actor.setMovieId(movieId);
        return actorRepository.save(actor);
    }

    public void deleteActor(String id) {
        fileDataService.deleteFileByRelId(id);
        actorRepository.deleteById(id);
    }

    public Actor createActor(Actor actor, String movieId) {
        actor.setMovieId(movieId);
        return actorRepository.save(actor);
    }

    public Iterable<Actor> getMovieActors(String movieId) {
        return actorRepository.findByMovieId(movieId);
    }

}
