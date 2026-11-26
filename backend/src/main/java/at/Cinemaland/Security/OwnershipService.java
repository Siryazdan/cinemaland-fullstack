package at.Cinemaland.Security;

import at.Cinemaland.Repository.ActorRepository;
import at.Cinemaland.Repository.MovieCategoryRepository;
import at.Cinemaland.Repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Images are linked to a movie, category or actor by its ID (relId).
 * This checks that such an ID belongs to the logged-in user before an image is changed.
 */
@Service
public class OwnershipService {

    private final MovieRepository movieRepository;
    private final MovieCategoryRepository movieCategoryRepository;
    private final ActorRepository actorRepository;
    private final CurrentUser currentUser;

    public OwnershipService(MovieRepository movieRepository, MovieCategoryRepository movieCategoryRepository,
                            ActorRepository actorRepository, CurrentUser currentUser) {
        this.movieRepository = movieRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.actorRepository = actorRepository;
        this.currentUser = currentUser;
    }

    public boolean ownsEntry(String relId) {
        if (relId == null) {
            return false;
        }
        String userId = currentUser.id();
        return movieRepository.existsByIdAndUserId(relId, userId)
                || movieCategoryRepository.existsByIdAndUserId(relId, userId)
                || actorRepository.existsByIdAndUserId(relId, userId);
    }

    /** Answers 404 (not 403) so nobody can find out which IDs exist for other users. */
    public void requireOwnsEntry(String relId) {
        if (!ownsEntry(relId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
}
