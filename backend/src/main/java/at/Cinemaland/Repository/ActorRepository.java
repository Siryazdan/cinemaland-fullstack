package at.Cinemaland.Repository;

import at.Cinemaland.Model.Actor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends BaseRepository<Actor, String> {

    List<Actor> findByUserId(String userId);

    Optional<Actor> findByIdAndUserId(String id, String userId);

    boolean existsByIdAndUserId(String id, String userId);

    List<Actor> findByIdInAndUserId(Collection<String> ids, String userId);
}
