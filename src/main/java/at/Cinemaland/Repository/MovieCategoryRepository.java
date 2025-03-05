package at.cinemaland.Repository;


import at.cinemaland.Model.MovieCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCategoryRepository extends BaseRepository<MovieCategory, String> {
}
