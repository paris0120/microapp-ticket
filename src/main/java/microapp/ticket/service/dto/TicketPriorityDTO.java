package microapp.ticket.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.ticket.domain.TicketPriority} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketPriorityDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer priorityLevel;

    @NotNull(message = "must not be null")
    private String priority;

    private String color;

    private String icon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketPriorityDTO)) {
            return false;
        }

        TicketPriorityDTO ticketPriorityDTO = (TicketPriorityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketPriorityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketPriorityDTO{" +
            "id=" + getId() +
            ", priorityLevel=" + getPriorityLevel() +
            ", priority='" + getPriority() + "'" +
            ", color='" + getColor() + "'" +
            ", icon='" + getIcon() + "'" +
            "}";
    }
}
