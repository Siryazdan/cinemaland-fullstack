package at.Cinemaland.Model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class MovieCategory {

    @Id
    private String id;

    public MovieCategory(String name) {
        this.name = name;
    }

    public MovieCategory() {

    }

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "rel_id", insertable = false, updatable = false)
    private FileData file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileData getFile() {
        return file;
    }

    public void setFile(FileData file) {
        this.file = file;
    }
}
