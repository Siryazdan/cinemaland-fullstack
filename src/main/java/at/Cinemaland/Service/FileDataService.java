package at.Cinemaland.Service;


import at.Cinemaland.Model.FileData;

import at.Cinemaland.Repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDataService {

    @Value("${file.upload-directory}")
    private String uploadDir;

    private final FileDataRepository fileDataRepository;

    public FileDataService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    public FileData createFileData(MultipartFile file, String relId) throws IOException {

        FileData currentImage = getFileDataByRelId(relId);
        if (currentImage != null) {
            deleteFileData(currentImage.getId());
            try {
                Path path = Paths.get(currentImage.getFilePath());
                Files.delete(path);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


        FileData fileData = new FileData();
        fileData.setRelId(relId);
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileExtension(getFileExtension(file.getOriginalFilename()));
        fileData.setFileSize(file.getSize());
        fileData.setMimetype(file.getContentType());
        fileData.setFilePath(saveFileToLocalSystem(file));

        return fileDataRepository.save(fileData);
    }

    private String saveFileToLocalSystem(MultipartFile file) throws IOException {

        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        return filePath;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "";
    }

    public FileData getFileData(String id) {
        return fileDataRepository.findById(id).orElse(null);
    }

    public FileData replaceOrUpdateFileData(MultipartFile file, String id) throws IOException {
        FileData currentImage = getFileDataByRelId(id);
        FileData updatedFile = new FileData();

        if (currentImage != null && file.getSize() > 0) {
            deleteFileData(currentImage.getId());
            updatedFile = createFileData(file, id);
        }
        if (currentImage == null && file.getSize() > 0) {
            updatedFile = createFileData(file, id);
        }

        return updatedFile;
    }

    private void deleteFileData(String id) {
        fileDataRepository.deleteById(id);

    }

    private FileData getFileDataByRelId(String id) {
        return fileDataRepository.findByRelId(id);
    }

    public void deleteFileByRelId(String id) {
        FileData file = fileDataRepository.findByRelId(id);
        if (file == null) return;
        fileDataRepository.deleteById(file.getId());
        try {
            Path path = Paths.get(file.getFilePath());
            Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Iterable<FileData> getFiles() {
        return fileDataRepository.findAll();
    }

    public ResponseEntity<Resource> streamFile(String id) throws IOException {
        FileData fileData = getFileData(id);

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
