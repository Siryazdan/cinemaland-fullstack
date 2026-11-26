package at.Cinemaland.Controller.api;

import at.Cinemaland.Model.FileData;
import at.Cinemaland.Security.OwnershipService;
import at.Cinemaland.Service.FileDataService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/files/")
public class FileDataApiController {

    final FileDataService fileDataService;
    final OwnershipService ownershipService;

    public FileDataApiController(FileDataService fileDataService, OwnershipService ownershipService) {
        this.fileDataService = fileDataService;
        this.ownershipService = ownershipService;
    }

    /** Upload an image for one of your own movies, categories or actors (relId). */
    @PostMapping("{relId}")
    public FileData createFileData(@PathVariable("relId") String relId, @RequestParam("file") MultipartFile file) throws IOException {
        ownershipService.requireOwnsEntry(relId);
        return fileDataService.createFileData(file, relId);
    }

    /** Only the files of your own collection. */
    @GetMapping()
    public List<FileData> getFiles() {
        return StreamSupport.stream(fileDataService.getFiles().spliterator(), false)
                .filter(file -> ownershipService.ownsEntry(file.getRelId()))
                .toList();
    }

    @GetMapping("{id}")
    public FileData getFile(@PathVariable("id") String id) {
        FileData file = fileDataService.getFileData(id);
        if (file == null || !ownershipService.ownsEntry(file.getRelId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
        return file;
    }

    /** Replace the image of one of your own entries. {id} is the entry's ID (relId), as before. */
    @PutMapping("{id}")
    public FileData updateFile(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) throws IOException {
        ownershipService.requireOwnsEntry(id);
        return fileDataService.replaceOrUpdateFileData(file, id);
    }

    @DeleteMapping("{relId}")
    public void deleteFile(@PathVariable("relId") String relId) {
        ownershipService.requireOwnsEntry(relId);
        fileDataService.deleteFileByRelId(relId);
    }

    /** Public: used directly in <img> tags. File IDs are random UUIDs. */
    @GetMapping("{id}/_stream")
    public ResponseEntity<Resource> streamFile(@PathVariable("id") String id) throws IOException {
        return fileDataService.streamFile(id);
    }
}
