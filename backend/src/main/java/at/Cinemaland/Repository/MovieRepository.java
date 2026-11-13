package at.Cinemaland.Repository;


import at.Cinemaland.Model.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie, String> {
    // rating is stored as text, so compare it as a number ("10" would be smaller than "8.0" as text)
    @Query("select m from Movie m where cast(m.rating as Double) >= :minRating order by cast(m.rating as Double) desc")
    List<Movie> findTopRated(@Param("minRating") double minRating);

    List<Movie> findByMovieCategoryId(String movieCategoryId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByCategoryNameIgnoreCase(String categoryName);
}
