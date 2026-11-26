package at.Cinemaland.Repository;

import at.Cinemaland.Model.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** All queries are limited to one user's movies (userId), so nobody sees someone else's collection. */
@Repository
public interface MovieRepository extends BaseRepository<Movie, String> {

    // rating is stored as text, so compare it as a number ("10" would be smaller than "8.0" as text)
    @Query("select m from Movie m where m.userId = :userId and cast(m.rating as Double) >= :minRating order by cast(m.rating as Double) desc")
    List<Movie> findTopRated(@Param("userId") String userId, @Param("minRating") double minRating);

    List<Movie> findByUserId(String userId);

    Optional<Movie> findByIdAndUserId(String id, String userId);

    boolean existsByIdAndUserId(String id, String userId);

    List<Movie> findByUserIdAndMovieCategoryId(String userId, String movieCategoryId);

    List<Movie> findByUserIdAndTitleContainingIgnoreCase(String userId, String title);

    List<Movie> findByUserIdAndCategoryNameIgnoreCase(String userId, String categoryName);
}
