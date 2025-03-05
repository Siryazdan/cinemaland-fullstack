package at.cinemaland.Service;

import at.cinemaland.Model.MovieCategory;

import at.cinemaland.Repository.MovieCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieCategoryService {

    private final MovieCategoryRepository movieCategoryRepository;
    private final FileDataService fileDataService;

    public MovieCategoryService(MovieCategoryRepository movieCategoryRepository, FileDataService fileDataService) {
        this.movieCategoryRepository = movieCategoryRepository;
        this.fileDataService = fileDataService;
    }


    public List<MovieCategory> getCategories() {
        return movieCategoryRepository.findAll();
    }

    public MovieCategory createCategory(MovieCategory category) {
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
        fileDataService.deleteFileByRelId(id);
        movieCategoryRepository.deleteById(id);
    }
}
