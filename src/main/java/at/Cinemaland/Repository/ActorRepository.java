package at.cinemaland.Repository;


import at.cinemaland.Model.Actor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends BaseRepository<Actor, String> {
}
