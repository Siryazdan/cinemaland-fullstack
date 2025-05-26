package at.Cinemaland.Repository;


import at.Cinemaland.Model.MovieCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCategoryRepository extends BaseRepository<MovieCategory, String> {
}
