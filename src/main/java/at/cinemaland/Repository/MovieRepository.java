package at.cinemaland.Repository;


import at.cinemaland.Model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie, String> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByCategoryNameIgnoreCase(String categoryName);
}
