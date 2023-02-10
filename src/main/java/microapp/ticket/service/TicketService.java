package microapp.ticket.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.PostConstruct;
import microapp.ticket.domain.Ticket;
import microapp.ticket.repository.TicketPriorityRepository;
import microapp.ticket.repository.TicketRepository;
import microapp.ticket.repository.TicketTypeRepository;
import microapp.ticket.security.SecurityUtils;
import microapp.ticket.service.dto.TicketDTO;
import microapp.ticket.service.dto.TicketPriorityDTO;
import microapp.ticket.service.dto.TicketTypeDTO;
import microapp.ticket.service.mapper.TicketMapper;
import microapp.ticket.service.mapper.TicketPriorityMapper;
import microapp.ticket.service.mapper.TicketTypeMapper;
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

    private final TicketTypeRepository ticketTypeRepository;
    private final TicketTypeMapper ticketTypeMapper;

    private final TicketPriorityRepository ticketPriorityRepository;

    private final TicketPriorityMapper ticketPriorityMapper;
    private HashMap<String, TicketTypeDTO> ticketTypes = null;
    private HashMap<Integer, TicketPriorityDTO> priorities = null;

    public TicketService(
        TicketRepository ticketRepository,
        TicketMapper ticketMapper,
        TicketTypeRepository ticketTypeRepository,
        TicketTypeMapper ticketTypeMapper,
        TicketPriorityRepository ticketPriorityRepository,
        TicketPriorityMapper ticketPriorityMapper
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
        this.ticketPriorityRepository = ticketPriorityRepository;
        this.ticketPriorityMapper = ticketPriorityMapper;
    }

    public void refreshTicketTypes() {
        ticketTypes = new HashMap<>();
        ticketTypeRepository
            .findAll()
            .map(ticketTypeMapper::toDto)
            .subscribe(type -> {
                ticketTypes.put(type.getKey(), type);
            });
    }

    public void refreshPriorities() {
        priorities = new HashMap<>();
        ticketPriorityRepository
            .findAll()
            .map(ticketPriorityMapper::toDto)
            .subscribe(priority -> {
                priorities.put(priority.getPriorityLevel(), priority);
            });
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
                return ticketRepository.save(ticketMapper.toEntity(ticketDTO)).map(ticketMapper::toDto).map(this::addObject);
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
            .map(ticketMapper::toDto)
            .map(this::addObject);
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
            .map(ticketMapper::toDto)
            .map(this::addObject);
    }

    private TicketDTO addObject(TicketDTO ticketDTO) {
        ticketDTO.setTicketType(this.ticketTypes.get(ticketDTO.getTypeKey()));
        ticketDTO.setTicketPriority(this.priorities.get(ticketDTO.getPriorityLevel()));
        return ticketDTO;
    }

    /**
     * Get all the tickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TicketDTO> findAllMy(String status, Pageable pageable) {
        if (ticketTypes == null) refreshTicketTypes();
        if (priorities == null) refreshPriorities();
        if (status.equalsIgnoreCase("open")) {
            log.debug("Request to get all open Tickets");
            return SecurityUtils
                .getCurrentUserLogin()
                .flatMapMany(username -> {
                    return ticketRepository
                        .findAllByUsernameAndClosedIs(username, null, pageable)
                        .map(ticketMapper::toDto)
                        .map(this::addObject);
                });
        } else {
            log.debug("Request to get all closed Tickets");
            return SecurityUtils
                .getCurrentUserLogin()
                .flatMapMany(username -> {
                    return ticketRepository
                        .findAllByUsernameAndClosedIsNotAndArchivedIs(username, null, null, pageable)
                        .map(ticketMapper::toDto)
                        .map(this::addObject);
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
        return ticketRepository.findAllBy(pageable).map(ticketMapper::toDto).map(this::addObject);
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
        return ticketRepository.findById(id).map(ticketMapper::toDto).map(this::addObject);
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
