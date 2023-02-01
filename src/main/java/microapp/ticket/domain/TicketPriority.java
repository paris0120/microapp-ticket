package microapp.ticket.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TicketPriority.
 */
@Table("ticket_priority")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketPriority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("priority_level")
    private Integer priorityLevel;

    @NotNull(message = "must not be null")
    @Column("priority")
    private String priority;

    @Column("color")
    private String color;

    @Column("icon")
    private String icon;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TicketPriority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriorityLevel() {
        return this.priorityLevel;
    }

    public TicketPriority priorityLevel(Integer priorityLevel) {
        this.setPriorityLevel(priorityLevel);
        return this;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPriority() {
        return this.priority;
    }

    public TicketPriority priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getColor() {
        return this.color;
    }

    public TicketPriority color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return this.icon;
    }

    public TicketPriority icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketPriority)) {
            return false;
        }
        return id != null && id.equals(((TicketPriority) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketPriority{" +
            "id=" + getId() +
            ", priorityLevel=" + getPriorityLevel() +
            ", priority='" + getPriority() + "'" +
            ", color='" + getColor() + "'" +
            ", icon='" + getIcon() + "'" +
            "}";
    }
}
