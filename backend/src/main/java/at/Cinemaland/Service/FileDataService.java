package at.Cinemaland.Service;

import at.Cinemaland.Model.FileData;
import at.Cinemaland.Repository.FileDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileDataService {

    private static final Logger log = LoggerFactory.getLogger(FileDataService.class);

    @Value("${file.upload-directory}")
    private String uploadDir;

    private final FileDataRepository fileDataRepository;

    public FileDataService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    /**
     * Saves the uploaded file for the given movie/actor/category (relId).
     * An existing file for the same relId is replaced.
     * Returns null if no file was uploaded.
     */
    public FileData createFileData(MultipartFile file, String relId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        deleteFileByRelId(relId); // remove the old image (database row + file on disk)

        FileData fileData = new FileData();
        fileData.setRelId(relId);
        fileData.setFileName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        fileData.setFileExtension(getFileExtension(file.getOriginalFilename()));
        fileData.setFileSize(file.getSize());
        fileData.setMimetype(file.getContentType() != null ? file.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE);
        fileData.setFilePath(saveFileToLocalSystem(file, fileData.getFileExtension()));

        return fileDataRepository.save(fileData);
    }

    /** Replaces the file for relId. If no new file was uploaded, the current one is kept. */
    public FileData replaceOrUpdateFileData(MultipartFile file, String relId) throws IOException {
        if (file == null || file.isEmpty()) {
            return getFileDataByRelId(relId);
        }
        return createFileData(file, relId);
    }

    private String saveFileToLocalSystem(MultipartFile file, String extension) throws IOException {
        Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(directory);

        // Random name: two uploads with the same name don't overwrite each other,
        // and a file name like "../../x" can't write outside the upload folder
        Path target = directory.resolve(UUID.randomUUID() + extension);
        file.transferTo(target);

        return target.toString();
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            // keep only simple extensions like ".jpg"
            if (ext.matches("\\.[a-z0-9]{1,8}")) {
                return ext;
            }
        }
        return "";
    }

    public FileData getFileData(String id) {
        return fileDataRepository.findById(id).orElse(null);
    }

    private FileData getFileDataByRelId(String relId) {
        return fileDataRepository.findByRelId(relId);
    }

    public void deleteFileByRelId(String relId) {
        FileData file = fileDataRepository.findByRelId(relId);
        if (file == null) return;
        fileDataRepository.deleteById(file.getId());
        try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
        } catch (IOException e) {
            log.warn("Could not delete file {}: {}", file.getFilePath(), e.getMessage());
        }
    }

    public Iterable<FileData> getFiles() {
        return fileDataRepository.findAll();
    }

    /** Sends the file to the browser so it can be shown directly (e.g. in an <img> tag). */
    public ResponseEntity<Resource> streamFile(String id) throws IOException {
        FileData fileData = getFileData(id);
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(fileData.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = fileData.getMimetype();
        }
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(contentType);
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                // "inline" = show in the browser, "attachment" would force a download
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(fileData.getFileName()).build().toString())
                .body(resource);
    }
}
