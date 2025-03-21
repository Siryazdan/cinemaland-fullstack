package at.cinemaland.Controller.web;

import at.cinemaland.Model.FileData;
import at.cinemaland.Service.FileDataService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/files/")
public class FileController {

    final FileDataService fileDataService;

    public FileController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource> serveFileById(@PathVariable String id) throws IOException {

        FileData fileData = this.fileDataService.getFileData(id);

        Path filePath = Paths.get(fileData.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediatype = Files.probeContentType(filePath) != null
                ? MediaType.parseMediaType(Files.probeContentType(filePath))
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediatype)
                .header("Content-Disposition",
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }
}
