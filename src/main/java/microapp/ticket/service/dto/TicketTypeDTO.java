package microapp.ticket.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.ticket.domain.TicketType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketTypeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String key;

    @NotNull(message = "must not be null")
    private String type;

    @NotNull(message = "must not be null")
    private Integer weight;

    private String color;

    private String icon;

    @Lob
    private String description;

    @NotNull(message = "must not be null")
    private Boolean isActive;

    @NotNull(message = "must not be null")
    private Instant created;

    @NotNull(message = "must not be null")
    private Instant modified;

    private Instant archived;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getArchived() {
        return archived;
    }

    public void setArchived(Instant archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketTypeDTO)) {
            return false;
        }

        TicketTypeDTO ticketTypeDTO = (TicketTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketTypeDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", type='" + getType() + "'" +
            ", weight=" + getWeight() +
            ", color='" + getColor() + "'" +
            ", icon='" + getIcon() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
