package at.Cinemaland.Controller.api;

import at.Cinemaland.Model.FileData;
import at.Cinemaland.Service.FileDataService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files/")
public class FileDataApiController {

    final FileDataService fileDataService;

    public FileDataApiController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }

    @PostMapping("{relId}")
    public FileData createFileData(@PathVariable("relId") String relId, @RequestBody MultipartFile file) throws IOException {
        return fileDataService.createFileData(file, relId);
    }

    @GetMapping()
    public Iterable<FileData> getFiles() {
        return fileDataService.getFiles();
    }

    @GetMapping("{id}")
    public FileData getFiles(@PathVariable("id") String id) {
        return fileDataService.getFileData(id);
    }

    @PutMapping("{id}")
    public FileData updateFile(@PathVariable("id") String id, @RequestBody MultipartFile file) throws IOException {
        return fileDataService.replaceOrUpdateFileData(file, id);
    }

    @DeleteMapping("{relId}")
    public void deleteFile(@PathVariable("relId") String relId) {
        fileDataService.deleteFileByRelId(relId);
    }


    @GetMapping("{id}/_stream")
    public ResponseEntity<Resource> streamFile(@PathVariable("id") String id) throws IOException {
        return fileDataService.streamFile(id);
    }

}