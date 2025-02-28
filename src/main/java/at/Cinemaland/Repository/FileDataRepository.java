package at.Cinemaland.Repository;


import at.Cinemaland.Model.FileData;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends BaseRepository<FileData, String> {
    FileData findByRelId(String id);
}
