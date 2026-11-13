package at.Cinemaland.Controller.web;

import at.Cinemaland.Service.FileDataService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/files/")
public class FileController { // URL to access the file (used by the Thymeleaf pages)

    final FileDataService fileDataService;

    public FileController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource> serveFileById(@PathVariable String id) throws IOException {
        return fileDataService.streamFile(id);
    }
}
