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
 * A TicketAssignment.
 */
@Table("ticket_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("issue_id")
    private Long issueId;

    @NotNull(message = "must not be null")
    @Column("issue_uuid")
    private UUID issueUuid;

    @NotNull(message = "must not be null")
    @Column("username")
    private String username;

    @NotNull(message = "must not be null")
    @Column("role_key")
    private String roleKey;

    @NotNull(message = "must not be null")
    @Column("role_weight")
    private Integer roleWeight;

    @NotNull(message = "must not be null")
    @Column("department_key")
    private String departmentKey;

    @NotNull(message = "must not be null")
    @Column("department_weight")
    private Integer departmentWeight;

    @NotNull(message = "must not be null")
    @Column("created")
    private Instant created;

    @NotNull(message = "must not be null")
    @Column("modified")
    private Instant modified;

    @Column("accepted")
    private Instant accepted;

    @Column("microapp_left")
    private Instant left;

    @Column("closed")
    private Instant closed;

    @Column("archived")
    private Instant archived;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TicketAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIssueId() {
        return this.issueId;
    }

    public TicketAssignment issueId(Long issueId) {
        this.setIssueId(issueId);
        return this;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public UUID getIssueUuid() {
        return this.issueUuid;
    }

    public TicketAssignment issueUuid(UUID issueUuid) {
        this.setIssueUuid(issueUuid);
        return this;
    }

    public void setIssueUuid(UUID issueUuid) {
        this.issueUuid = issueUuid;
    }

    public String getUsername() {
        return this.username;
    }

    public TicketAssignment username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleKey() {
        return this.roleKey;
    }

    public TicketAssignment roleKey(String roleKey) {
        this.setRoleKey(roleKey);
        return this;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Integer getRoleWeight() {
        return this.roleWeight;
    }

    public TicketAssignment roleWeight(Integer roleWeight) {
        this.setRoleWeight(roleWeight);
        return this;
    }

    public void setRoleWeight(Integer roleWeight) {
        this.roleWeight = roleWeight;
    }

    public String getDepartmentKey() {
        return this.departmentKey;
    }

    public TicketAssignment departmentKey(String departmentKey) {
        this.setDepartmentKey(departmentKey);
        return this;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey;
    }

    public Integer getDepartmentWeight() {
        return this.departmentWeight;
    }

    public TicketAssignment departmentWeight(Integer departmentWeight) {
        this.setDepartmentWeight(departmentWeight);
        return this;
    }

    public void setDepartmentWeight(Integer departmentWeight) {
        this.departmentWeight = departmentWeight;
    }

    public Instant getCreated() {
        return this.created;
    }

    public TicketAssignment created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public TicketAssignment modified(Instant modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getAccepted() {
        return this.accepted;
    }

    public TicketAssignment accepted(Instant accepted) {
        this.setAccepted(accepted);
        return this;
    }

    public void setAccepted(Instant accepted) {
        this.accepted = accepted;
    }

    public Instant getLeft() {
        return this.left;
    }

    public TicketAssignment left(Instant left) {
        this.setLeft(left);
        return this;
    }

    public void setLeft(Instant left) {
        this.left = left;
    }

    public Instant getClosed() {
        return this.closed;
    }

    public TicketAssignment closed(Instant closed) {
        this.setClosed(closed);
        return this;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }

    public Instant getArchived() {
        return this.archived;
    }

    public TicketAssignment archived(Instant archived) {
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
        if (!(o instanceof TicketAssignment)) {
            return false;
        }
        return id != null && id.equals(((TicketAssignment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketAssignment{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueUuid='" + getIssueUuid() + "'" +
            ", username='" + getUsername() + "'" +
            ", roleKey='" + getRoleKey() + "'" +
            ", roleWeight=" + getRoleWeight() +
            ", departmentKey='" + getDepartmentKey() + "'" +
            ", departmentWeight=" + getDepartmentWeight() +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", accepted='" + getAccepted() + "'" +
            ", left='" + getLeft() + "'" +
            ", closed='" + getClosed() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
