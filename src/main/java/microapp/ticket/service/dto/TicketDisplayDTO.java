package microapp.ticket.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link microapp.ticket.domain.Ticket} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketDisplayDTO implements Serializable {

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

    private String typeKey;
    private String type;
    private String typeColor;
    private String typeIcon;

    private String workflowStatusKey;
    private String workflowStatus;
    private String workflowStatusColor;
    private String workflowStatusIcon;

    private Integer priorityLevel;
    private Integer priority;
    private Integer priorityColor;
    private Integer priorityIcon;

    private List<TagDTO> tags;

    private Integer totalComments;
    private UUID uuid;

    private Instant created;

    private Instant modified;

    private Instant updated;

    private Instant closed;

    private Instant archived;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(String typeIcon) {
        this.typeIcon = typeIcon;
    }

    public String getWorkflowStatusKey() {
        return workflowStatusKey;
    }

    public void setWorkflowStatusKey(String workflowStatusKey) {
        this.workflowStatusKey = workflowStatusKey;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getWorkflowStatusColor() {
        return workflowStatusColor;
    }

    public void setWorkflowStatusColor(String workflowStatusColor) {
        this.workflowStatusColor = workflowStatusColor;
    }

    public String getWorkflowStatusIcon() {
        return workflowStatusIcon;
    }

    public void setWorkflowStatusIcon(String workflowStatusIcon) {
        this.workflowStatusIcon = workflowStatusIcon;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(Integer priorityColor) {
        this.priorityColor = priorityColor;
    }

    public Integer getPriorityIcon() {
        return priorityIcon;
    }

    public void setPriorityIcon(Integer priorityIcon) {
        this.priorityIcon = priorityIcon;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDisplayDTO)) {
            return false;
        }

        TicketDisplayDTO ticketDTO = (TicketDisplayDTO) o;
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
