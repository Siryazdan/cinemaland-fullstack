package at.cinemaland.Repository;



import at.cinemaland.Model.Movie;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends BaseRepository<Movie, String> {
}
