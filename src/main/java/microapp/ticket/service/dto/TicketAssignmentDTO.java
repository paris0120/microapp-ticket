package microapp.ticket.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.ticket.domain.TicketAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketAssignmentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long issueId;

    @NotNull(message = "must not be null")
    private UUID issueUuid;

    @NotNull(message = "must not be null")
    private String username;

    @NotNull(message = "must not be null")
    private String roleKey;

    @NotNull(message = "must not be null")
    private Integer roleWeight;

    @NotNull(message = "must not be null")
    private Boolean isManager;

    @NotNull(message = "must not be null")
    private String departmentKey;

    @NotNull(message = "must not be null")
    private Integer departmentWeight;

    @NotNull(message = "must not be null")
    private String assignedByUsername;

    @NotNull(message = "must not be null")
    private Instant created;

    @NotNull(message = "must not be null")
    private Instant modified;

    private Instant accepted;

    private Instant left;

    private Instant closed;

    private Instant archived;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public UUID getIssueUuid() {
        return issueUuid;
    }

    public void setIssueUuid(UUID issueUuid) {
        this.issueUuid = issueUuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Integer getRoleWeight() {
        return roleWeight;
    }

    public void setRoleWeight(Integer roleWeight) {
        this.roleWeight = roleWeight;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey;
    }

    public Integer getDepartmentWeight() {
        return departmentWeight;
    }

    public void setDepartmentWeight(Integer departmentWeight) {
        this.departmentWeight = departmentWeight;
    }

    public String getAssignedByUsername() {
        return assignedByUsername;
    }

    public void setAssignedByUsername(String assignedByUsername) {
        this.assignedByUsername = assignedByUsername;
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

    public Instant getAccepted() {
        return accepted;
    }

    public void setAccepted(Instant accepted) {
        this.accepted = accepted;
    }

    public Instant getLeft() {
        return left;
    }

    public void setLeft(Instant left) {
        this.left = left;
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
        if (!(o instanceof TicketAssignmentDTO)) {
            return false;
        }

        TicketAssignmentDTO ticketAssignmentDTO = (TicketAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketAssignmentDTO{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueUuid='" + getIssueUuid() + "'" +
            ", username='" + getUsername() + "'" +
            ", roleKey='" + getRoleKey() + "'" +
            ", roleWeight=" + getRoleWeight() +
            ", isManager='" + getIsManager() + "'" +
            ", departmentKey='" + getDepartmentKey() + "'" +
            ", departmentWeight=" + getDepartmentWeight() +
            ", assignedByUsername='" + getAssignedByUsername() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", accepted='" + getAccepted() + "'" +
            ", left='" + getLeft() + "'" +
            ", closed='" + getClosed() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
