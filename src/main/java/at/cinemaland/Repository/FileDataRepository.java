package at.cinemaland.Repository;




import at.cinemaland.Model.FileData;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends BaseRepository<FileData, String> {
    FileData findByRelId(String id);
}
