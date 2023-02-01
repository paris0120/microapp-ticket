package microapp.ticket.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Ticket.
 */
@Table("ticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("username")
    private String username;

    @Column("user_first_name")
    private String userFirstName;

    @Column("user_last_name")
    private String userLastName;

    @NotNull(message = "must not be null")
    @Column("user_display_name")
    private String userDisplayName;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    @Column("title")
    private String title;

    @Column("content")
    private String content;

    @NotNull(message = "must not be null")
    @Column("type_key")
    private String typeKey;

    @NotNull(message = "must not be null")
    @Column("workflow_status_key")
    private String workflowStatusKey;

    @NotNull(message = "must not be null")
    @Column("priority_level")
    private Integer priorityLevel;

    @Column("tags")
    private String tags;

    @NotNull(message = "must not be null")
    @Column("total_comments")
    private Integer totalComments;

    @Column("uuid")
    private UUID uuid;

    @NotNull(message = "must not be null")
    @Column("created")
    private Instant created;

    @NotNull(message = "must not be null")
    @Column("modified")
    private Instant modified;

    @NotNull(message = "must not be null")
    @Column("updated")
    private Instant updated;

    @Column("closed")
    private Instant closed;

    @Column("archived")
    private Instant archived;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public Ticket username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFirstName() {
        return this.userFirstName;
    }

    public Ticket userFirstName(String userFirstName) {
        this.setUserFirstName(userFirstName);
        return this;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return this.userLastName;
    }

    public Ticket userLastName(String userLastName) {
        this.setUserLastName(userLastName);
        return this;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserDisplayName() {
        return this.userDisplayName;
    }

    public Ticket userDisplayName(String userDisplayName) {
        this.setUserDisplayName(userDisplayName);
        return this;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getTitle() {
        return this.title;
    }

    public Ticket title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public Ticket content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeKey() {
        return this.typeKey;
    }

    public Ticket typeKey(String typeKey) {
        this.setTypeKey(typeKey);
        return this;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getWorkflowStatusKey() {
        return this.workflowStatusKey;
    }

    public Ticket workflowStatusKey(String workflowStatusKey) {
        this.setWorkflowStatusKey(workflowStatusKey);
        return this;
    }

    public void setWorkflowStatusKey(String workflowStatusKey) {
        this.workflowStatusKey = workflowStatusKey;
    }

    public Integer getPriorityLevel() {
        return this.priorityLevel;
    }

    public Ticket priorityLevel(Integer priorityLevel) {
        this.setPriorityLevel(priorityLevel);
        return this;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getTags() {
        return this.tags;
    }

    public Ticket tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getTotalComments() {
        return this.totalComments;
    }

    public Ticket totalComments(Integer totalComments) {
        this.setTotalComments(totalComments);
        return this;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Ticket uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Ticket created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public Ticket modified(Instant modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public Ticket updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getClosed() {
        return this.closed;
    }

    public Ticket closed(Instant closed) {
        this.setClosed(closed);
        return this;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }

    public Instant getArchived() {
        return this.archived;
    }

    public Ticket archived(Instant archived) {
        this.setArchived(archived);
        return this;
    }

    public void setArchived(Instant archived) {
        this.archived = archived;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
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
