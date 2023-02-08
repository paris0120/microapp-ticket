package microapp.ticket.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import microapp.ticket.domain.TicketPriority;
import microapp.ticket.repository.TicketPriorityRepository;
import microapp.ticket.service.dto.TicketPriorityDTO;
import microapp.ticket.service.mapper.TicketPriorityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TicketPriority}.
 */
@Service
@Transactional
public class TicketPriorityService {

    private final Logger log = LoggerFactory.getLogger(TicketPriorityService.class);

    private final TicketPriorityRepository ticketPriorityRepository;

    private final TicketPriorityMapper ticketPriorityMapper;

    public TicketPriorityService(TicketPriorityRepository ticketPriorityRepository, TicketPriorityMapper ticketPriorityMapper) {
        this.ticketPriorityRepository = ticketPriorityRepository;
        this.ticketPriorityMapper = ticketPriorityMapper;
    }

    /**
     * Save a ticketPriority.
     *
     * @param ticketPriorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketPriorityDTO> save(TicketPriorityDTO ticketPriorityDTO) {
        log.debug("Request to save TicketPriority : {}", ticketPriorityDTO);
        return ticketPriorityRepository.save(ticketPriorityMapper.toEntity(ticketPriorityDTO)).map(ticketPriorityMapper::toDto);
    }

    /**
     * Update a ticketPriority.
     *
     * @param ticketPriorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketPriorityDTO> update(TicketPriorityDTO ticketPriorityDTO) {
        log.debug("Request to update TicketPriority : {}", ticketPriorityDTO);
        return ticketPriorityRepository.save(ticketPriorityMapper.toEntity(ticketPriorityDTO)).map(ticketPriorityMapper::toDto);
    }

    /**
     * Partially update a ticketPriority.
     *
     * @param ticketPriorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TicketPriorityDTO> partialUpdate(TicketPriorityDTO ticketPriorityDTO) {
        log.debug("Request to partially update TicketPriority : {}", ticketPriorityDTO);

        return ticketPriorityRepository
            .findById(ticketPriorityDTO.getId())
            .map(existingTicketPriority -> {
                ticketPriorityMapper.partialUpdate(existingTicketPriority, ticketPriorityDTO);

                return existingTicketPriority;
            })
            .flatMap(ticketPriorityRepository::save)
            .map(ticketPriorityMapper::toDto);
    }

    /**
     * Get all the ticketPriorities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TicketPriorityDTO> findAll() {
        log.debug("Request to get all TicketPriorities");
        return ticketPriorityRepository.findAll().map(ticketPriorityMapper::toDto);
    }

    /**
     * Returns the number of ticketPriorities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ticketPriorityRepository.count();
    }

    /**
     * Get one ticketPriority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TicketPriorityDTO> findOne(Long id) {
        log.debug("Request to get TicketPriority : {}", id);
        return ticketPriorityRepository.findById(id).map(ticketPriorityMapper::toDto);
    }

    /**
     * Delete the ticketPriority by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TicketPriority : {}", id);
        return ticketPriorityRepository.deleteById(id);
    }
}
