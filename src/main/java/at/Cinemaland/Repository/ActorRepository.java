package at.Cinemaland.Repository;

import at.Cinemaland.Model.Actor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends BaseRepository<Actor, String> {
    Iterable<Actor> findByMovieId(String movieId);
}
