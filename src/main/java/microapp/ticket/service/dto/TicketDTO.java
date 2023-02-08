package microapp.ticket.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import microapp.ticket.service.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A DTO for the {@link microapp.ticket.domain.Ticket} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketDTO implements Serializable {

    private Long id;

    private String username;

    private String userFirstName;

    private String userLastName;

    @NotNull(message = "must not be null")
    private String userDisplayName;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String title;

    @Lob
    private String content;

    @NotNull(message = "must not be null")
    private String typeKey;

    @NotNull(message = "must not be null")
    private String workflowStatusKey;

    @NotNull(message = "must not be null")
    private Integer priorityLevel;

    @Lob
    private String tags;

    @NotNull(message = "must not be null")
    private Integer totalComments;

    private UUID uuid;

    @NotNull(message = "must not be null")
    private Instant created;

    @NotNull(message = "must not be null")
    private Instant modified;

    @NotNull(message = "must not be null")
    private Instant updated;

    private Instant closed;

    private Instant archived;

    private List<TagDTO> tagList;

    private TicketPriorityDTO ticketPriority;

    private TicketTypeDTO ticketType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getWorkflowStatusKey() {
        return workflowStatusKey;
    }

    public void setWorkflowStatusKey(String workflowStatusKey) {
        this.workflowStatusKey = workflowStatusKey;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getClosed() {
        return closed;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }

    public Instant getArchived() {
        return archived;
    }

    public void setArchived(Instant archived) {
        this.archived = archived;
    }

    public List<TagDTO> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDTO> tagList) {
        this.tagList = tagList;
    }

    public TicketPriorityDTO getTicketPriority() {
        return ticketPriority;
    }

    public void setTicketPriority(TicketPriorityDTO ticketPriority) {
        this.ticketPriority = ticketPriority;
    }

    public TicketTypeDTO getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeDTO ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDTO)) {
            return false;
        }

        TicketDTO ticketDTO = (TicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", userFirstName='" + getUserFirstName() + "'" +
            ", userLastName='" + getUserLastName() + "'" +
            ", userDisplayName='" + getUserDisplayName() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", typeKey='" + getTypeKey() + "'" +
            ", workflowStatusKey='" + getWorkflowStatusKey() + "'" +
            ", priorityLevel=" + getPriorityLevel() +
            ", tags='" + getTags() + "'" +
            ", totalComments=" + getTotalComments() +
            ", uuid='" + getUuid() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", closed='" + getClosed() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
