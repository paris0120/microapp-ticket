package microapp.ticket.service;

import java.util.*;
import java.util.stream.Collectors;
import microapp.ticket.domain.TicketType;
import microapp.ticket.repository.TicketTypeRepository;
import microapp.ticket.service.dto.TicketTypeDTO;
import microapp.ticket.service.mapper.TicketTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TicketType}.
 */
@Service
@Transactional
public class TicketTypeService {

    private final Logger log = LoggerFactory.getLogger(TicketTypeService.class);

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketTypeMapper ticketTypeMapper;

    private List<TicketTypeDTO> list;

    private HashMap<String, TicketTypeDTO> map;

    public TicketTypeService(TicketTypeRepository ticketTypeRepository, TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    /**
     * Save a ticketType.
     *
     * @param ticketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketTypeDTO> save(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to save TicketType : {}", ticketTypeDTO);
        return ticketTypeRepository.save(ticketTypeMapper.toEntity(ticketTypeDTO)).map(ticketTypeMapper::toDto);
    }

    /**
     * Update a ticketType.
     *
     * @param ticketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TicketTypeDTO> update(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to update TicketType : {}", ticketTypeDTO);
        return ticketTypeRepository.save(ticketTypeMapper.toEntity(ticketTypeDTO)).map(ticketTypeMapper::toDto);
    }

    /**
     * Partially update a ticketType.
     *
     * @param ticketTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TicketTypeDTO> partialUpdate(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to partially update TicketType : {}", ticketTypeDTO);

        return ticketTypeRepository
            .findById(ticketTypeDTO.getId())
            .map(existingTicketType -> {
                ticketTypeMapper.partialUpdate(existingTicketType, ticketTypeDTO);

                return existingTicketType;
            })
            .flatMap(ticketTypeRepository::save)
            .map(ticketTypeMapper::toDto);
    }

    /**
     * Get all the ticketTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TicketTypeDTO> findAll() {
        log.debug("Request to get all TicketTypes");
        if (this.list == null) refreshTypeList();
        return Flux.fromIterable(this.list);
    }

    /**
     * Refresh ticket type list.
     */
    public void refreshTypeList() {
        ticketTypeRepository
            .findAll()
            .map(ticketTypeMapper::toDto)
            .collectList()
            .subscribe(types -> {
                this.list = new LinkedList<>();
                this.map = new HashMap<>();
                this.list = types;
                for (TicketTypeDTO type : types) map.put(type.getKey(), type);
                Collections.sort(types, (a, b) -> a.getWeight() - b.getWeight());
            });
    }

    public boolean hasKey(String key) {
        return map.containsKey(key);
    }

    public TicketTypeDTO getType(String key) {
        return map.get(key);
    }

    /**
     * Returns the number of ticketTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ticketTypeRepository.count();
    }

    /**
     * Get one ticketType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TicketTypeDTO> findOne(Long id) {
        log.debug("Request to get TicketType : {}", id);
        return ticketTypeRepository.findById(id).map(ticketTypeMapper::toDto);
    }

    /**
     * Delete the ticketType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TicketType : {}", id);
        return ticketTypeRepository.deleteById(id);
    }
}
