package Repository;

import Model.Movie;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends BaseRepository<Movie, String> {
}
