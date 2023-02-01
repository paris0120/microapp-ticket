package microapp.ticket.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import microapp.ticket.domain.TicketAssignment;
import microapp.ticket.repository.TicketAssignmentRepository;
import microapp.ticket.service.dto.TicketAssignmentDTO;
import microapp.ticket.service.mapper.TicketAssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TicketAssignment}.
 */
@Service
@Transactional
public class TicketAssignmentService {

    private final Logger log = LoggerFactory.getLogger(TicketAssignmentService.class);

    private final TicketAssignmentRepository ticketAssignmentRepository;

    private final TicketAssignmentMapper ticketAssignmentMapper;

    public TicketAssignmentService(TicketAssignmentRepository ticketAssignmentRepository, TicketAssignmentMapper ticketAssignmentMapper) {
        this.ticketAssignmentRepository = ticketAssignmentRepository;
        this.ticketAssignmentMapper = ticketAssignmentMapper;
    }

    /**
     * Save a ticketAssignment.
     *
     * @param ticketAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketAssignmentDTO> save(TicketAssignmentDTO ticketAssignmentDTO) {
        log.debug("Request to save TicketAssignment : {}", ticketAssignmentDTO);
        return ticketAssignmentRepository.save(ticketAssignmentMapper.toEntity(ticketAssignmentDTO)).map(ticketAssignmentMapper::toDto);
    }

    /**
     * Update a ticketAssignment.
     *
     * @param ticketAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketAssignmentDTO> update(TicketAssignmentDTO ticketAssignmentDTO) {
        log.debug("Request to update TicketAssignment : {}", ticketAssignmentDTO);
        return ticketAssignmentRepository.save(ticketAssignmentMapper.toEntity(ticketAssignmentDTO)).map(ticketAssignmentMapper::toDto);
    }

    /**
     * Partially update a ticketAssignment.
     *
     * @param ticketAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TicketAssignmentDTO> partialUpdate(TicketAssignmentDTO ticketAssignmentDTO) {
        log.debug("Request to partially update TicketAssignment : {}", ticketAssignmentDTO);

        return ticketAssignmentRepository
            .findById(ticketAssignmentDTO.getId())
            .map(existingTicketAssignment -> {
                ticketAssignmentMapper.partialUpdate(existingTicketAssignment, ticketAssignmentDTO);

                return existingTicketAssignment;
            })
            .flatMap(ticketAssignmentRepository::save)
            .map(ticketAssignmentMapper::toDto);
    }

    /**
     * Get all the ticketAssignments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TicketAssignmentDTO> findAll() {
        log.debug("Request to get all TicketAssignments");
        return ticketAssignmentRepository.findAll().map(ticketAssignmentMapper::toDto);
    }

    /**
     * Returns the number of ticketAssignments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ticketAssignmentRepository.count();
    }

    /**
     * Get one ticketAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TicketAssignmentDTO> findOne(Long id) {
        log.debug("Request to get TicketAssignment : {}", id);
        return ticketAssignmentRepository.findById(id).map(ticketAssignmentMapper::toDto);
    }

    /**
     * Delete the ticketAssignment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TicketAssignment : {}", id);
        return ticketAssignmentRepository.deleteById(id);
    }
}
