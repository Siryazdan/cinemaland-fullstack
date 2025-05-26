package at.Cinemaland.Repository;


import at.Cinemaland.Model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie, String> {
    List<Movie> findByRatingGreaterThanEqual(String rating);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByCategoryNameIgnoreCase(String categoryName);
}
