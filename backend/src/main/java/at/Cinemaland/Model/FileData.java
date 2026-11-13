package at.Cinemaland.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
// Hibernate lazy-loading proxies have extra fields that Jackson can't turn into JSON
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileData {

    @Id
    private String id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    private String filePath;
    private String fileName;
    private String fileExtension;
    private String mimetype;
    private Long fileSize;

    private LocalDateTime createdAt;

    @Column(name = "rel_id")
    private String relId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore // don't show the server's folder structure in the API
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }
}
