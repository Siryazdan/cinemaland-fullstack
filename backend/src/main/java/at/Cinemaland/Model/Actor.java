package at.Cinemaland.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Actor {

    @Id
    private String id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    private String name;

    private int age;

    private int height;

    @ManyToOne(fetch = FetchType.LAZY)
    // the image is looked up by its rel_id - not a real foreign key (an entry may have no image)
    @JoinColumn(name = "id", referencedColumnName = "rel_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FileData image;

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


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public FileData getImage() {
        return image;
    }

    public void setImage(FileData image) {
        this.image = image;
    }

    /** Owner of this entry - every user has their own collection. Never sent to or read from API clients. */
    @JsonIgnore
    @Column(name = "user_id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
