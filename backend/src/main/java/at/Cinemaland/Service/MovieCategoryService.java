package at.Cinemaland.Service;


import at.Cinemaland.Model.Movie;
import at.Cinemaland.Model.MovieCategory;
import at.Cinemaland.Repository.MovieCategoryRepository;
import at.Cinemaland.Repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieCategoryService {

    private final MovieCategoryRepository movieCategoryRepository;
    private final FileDataService fileDataService;
    private final MovieRepository movieRepository;

    public MovieCategoryService(MovieCategoryRepository movieCategoryRepository, FileDataService fileDataService,
                                MovieRepository movieRepository) {
        this.movieCategoryRepository = movieCategoryRepository;
        this.fileDataService = fileDataService;
        this.movieRepository = movieRepository;
    }


    public List<MovieCategory> getCategories() {
        return movieCategoryRepository.findAll();
    }

    public MovieCategory createCategory(MovieCategory category) {
        category.setId(null); // a new category always gets a new ID
        return movieCategoryRepository.save(category);
    }

    public MovieCategory getCategory(String id) {
        return movieCategoryRepository.findById(id).orElse(null);
    }


    public MovieCategory updateCategory(String id, String name) {
        MovieCategory category = movieCategoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setName(name);
            return movieCategoryRepository.save(category);
        }
        return null;
    }


    public void deleteCategory(String id) {
        // Movies in this category are kept, they just have no category anymore
        // (otherwise the database foreign key blocks the delete)
        List<Movie> movies = movieRepository.findByMovieCategoryId(id);
        for (Movie movie : movies) {
            movie.setMovieCategoryId(null);
            movie.setCategory(null);
        }
        movieRepository.saveAll(movies);

        fileDataService.deleteFileByRelId(id);
        movieCategoryRepository.deleteById(id);
    }
}
