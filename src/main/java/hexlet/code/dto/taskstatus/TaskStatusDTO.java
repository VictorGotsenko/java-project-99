package hexlet.code.dto.taskstatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TaskStatusDTO {

    private Long id;
    private String name;
    private String slug;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;


    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return String
     */
    public String getSlug() {
        return slug;
    }

    /**
     *
     * @param slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     *
     * @return LocalDateTime
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * For test, then createdAt is String Shape pattern = "yyyy-MM-dd".
     *
     * @param createdAt
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = LocalDateTime.parse(createdAt + "T00:00");
    }

}
