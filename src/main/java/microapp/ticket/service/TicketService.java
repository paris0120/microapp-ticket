package microapp.ticket.service;

import java.time.Instant;
import java.util.UUID;
import microapp.ticket.domain.Ticket;
import microapp.ticket.repository.TicketRepository;
import microapp.ticket.security.SecurityUtils;
import microapp.ticket.service.dto.TicketDTO;
import microapp.ticket.service.mapper.TicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Ticket}.
 */
@Service
@Transactional
public class TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    /**
     * Save a ticket.
     *
     * @param ticketDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketDTO> save(TicketDTO ticketDTO) {
        log.debug("Request to save Ticket : {}", ticketDTO);
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(username -> {
                ticketDTO.setUsername(username);
                ticketDTO.setUserDisplayName(username);
                ticketDTO.setUserFirstName("");
                ticketDTO.setUserLastName("");
                ticketDTO.setWorkflowStatusKey("OPEN");
                ticketDTO.setPriorityLevel(0);
                ticketDTO.setCreated(Instant.now());
                ticketDTO.setModified(Instant.now());
                ticketDTO.setUpdated(Instant.now());
                ticketDTO.setClosed(null);
                ticketDTO.setArchived(null);
                ticketDTO.setUuid(UUID.randomUUID());
                return ticketRepository.save(ticketMapper.toEntity(ticketDTO)).map(ticketMapper::toDto);
            });
    }

    //
    //    /**
    //     * Update a ticket.
    //     *
    //     * @param ticketDTO the entity to save.
    //     * @return the persisted entity.
    //     */
    //    public Mono<TicketDTO> update(TicketDTO ticketDTO) {
    //        log.debug("Request to update Ticket : {}", ticketDTO);
    //        return ticketRepository.save(ticketMapper.toEntity(ticketDTO)).map(ticketMapper::toDto);
    //    }
    //
    //    /**
    //     * Partially update a ticket.
    //     *
    //     * @param ticketDTO the entity to update partially.
    //     * @return the persisted entity.
    //     */
    //    public Mono<TicketDTO> partialUpdate(TicketDTO ticketDTO) {
    //        log.debug("Request to partially update Ticket : {}", ticketDTO);
    //
    //        return ticketRepository
    //            .findById(ticketDTO.getId())
    //            .map(existingTicket -> {
    //                ticketMapper.partialUpdate(existingTicket, ticketDTO);
    //
    //                return existingTicket;
    //            })
    //            .flatMap(ticketRepository::save)
    //            .map(ticketMapper::toDto);
    //    }

    /**
     * Partially update a ticket.
     *
     * @param ticketDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TicketDTO> update(TicketDTO ticketDTO) {
        log.debug("Request to partially update Ticket : {}", ticketDTO);

        return ticketRepository
            .findById(ticketDTO.getId())
            .map(existingTicket -> {
                existingTicket.setContent(ticketDTO.getContent());
                existingTicket.setTitle(ticketDTO.getTitle());
                existingTicket.setModified(Instant.now());
                return existingTicket;
            })
            .flatMap(ticketRepository::save)
            .map(ticketMapper::toDto);
    }

    public Mono<TicketDTO> manage(TicketDTO ticketDTO) {
        log.debug("Request to partially update Ticket : {}", ticketDTO);
        return ticketRepository
            .findById(ticketDTO.getId())
            .map(existingTicket -> {
                existingTicket.setPriorityLevel(ticketDTO.getPriorityLevel());
                existingTicket.setWorkflowStatusKey(ticketDTO.getWorkflowStatusKey());
                existingTicket.setUpdated(Instant.now());
                return existingTicket;
            })
            .flatMap(ticketRepository::save)
            .map(ticketMapper::toDto);
    }

    /**
     * Get all the tickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TicketDTO> findAllMy(String status, Pageable pageable) {
        if (status.equalsIgnoreCase("open")) {
            log.debug("Request to get all open Tickets");
            return SecurityUtils
                .getCurrentUserLogin()
                .flatMapMany(username -> {
                    return ticketRepository.findAllByUsernameAndClosedIs(username, null, pageable).map(ticketMapper::toDto);
                });
        } else {
            log.debug("Request to get all closed Tickets");
            return SecurityUtils
                .getCurrentUserLogin()
                .flatMapMany(username -> {
                    return ticketRepository
                        .findAllByUsernameAndClosedIsNotAndArchivedIs(username, null, null, pageable)
                        .map(ticketMapper::toDto);
                });
        }
    }

    /**
     * Get all the tickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TicketDTO> findAllMyTickets(Pageable pageable) {
        log.debug("Request to get all Tickets");
        return ticketRepository.findAllBy(pageable).map(ticketMapper::toDto);
    }

    /**
     * Returns the number of tickets available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ticketRepository.count();
    }

    /**
     * Get one ticket by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TicketDTO> findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        return ticketRepository.findById(id).map(ticketMapper::toDto);
    }

    /**
     * Delete the ticket by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        return ticketRepository.deleteById(id);
    }
}
