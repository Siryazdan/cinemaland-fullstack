package at.Cinemaland.Repository;

import at.Cinemaland.Model.MovieCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieCategoryRepository extends BaseRepository<MovieCategory, String> {

    List<MovieCategory> findByUserId(String userId);

    Optional<MovieCategory> findByIdAndUserId(String id, String userId);

    boolean existsByIdAndUserId(String id, String userId);
}
