package microapp.ticket.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.ticket.repository.TicketTypeRepository;
import microapp.ticket.service.TicketTypeService;
import microapp.ticket.service.dto.TicketTypeDTO;
import microapp.ticket.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link microapp.ticket.domain.TicketType}.
 */
@RestController
@RequestMapping("/api")
public class TicketTypeResource {

    private final Logger log = LoggerFactory.getLogger(TicketTypeResource.class);

    private static final String ENTITY_NAME = "ticketTicketType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketTypeService ticketTypeService;

    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeResource(TicketTypeService ticketTypeService, TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeService = ticketTypeService;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    /**
     * {@code POST  /ticket-types} : Create a new ticketType.
     *
     * @param ticketTypeDTO the ticketTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketTypeDTO, or with status {@code 400 (Bad Request)} if the ticketType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticket-types")
    public Mono<ResponseEntity<TicketTypeDTO>> createTicketType(@Valid @RequestBody TicketTypeDTO ticketTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TicketType : {}", ticketTypeDTO);
        if (ticketTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ticketTypeService
            .save(ticketTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ticket-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ticket-types/:id} : Updates an existing ticketType.
     *
     * @param id the id of the ticketTypeDTO to save.
     * @param ticketTypeDTO the ticketTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketTypeDTO,
     * or with status {@code 400 (Bad Request)} if the ticketTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticket-types/{id}")
    public Mono<ResponseEntity<TicketTypeDTO>> updateTicketType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketTypeDTO ticketTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TicketType : {}, {}", id, ticketTypeDTO);
        if (ticketTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ticketTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ticketTypeService
                    .update(ticketTypeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /ticket-types/:id} : Partial updates given fields of an existing ticketType, field will ignore if it is null
     *
     * @param id the id of the ticketTypeDTO to save.
     * @param ticketTypeDTO the ticketTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketTypeDTO,
     * or with status {@code 400 (Bad Request)} if the ticketTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ticket-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TicketTypeDTO>> partialUpdateTicketType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketTypeDTO ticketTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TicketType partially : {}, {}", id, ticketTypeDTO);
        if (ticketTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ticketTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TicketTypeDTO> result = ticketTypeService.partialUpdate(ticketTypeDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /ticket-types} : get all the ticketTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketTypes in body.
     */
    @GetMapping("/ticket-types")
    public Mono<List<TicketTypeDTO>> getAllTicketTypes() {
        log.debug("REST request to get all TicketTypes");
        return ticketTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /ticket-types} : get all the ticketTypes as a stream.
     * @return the {@link Flux} of ticketTypes.
     */
    @GetMapping(value = "/ticket-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TicketTypeDTO> getAllTicketTypesAsStream() {
        log.debug("REST request to get all TicketTypes as a stream");
        return ticketTypeService.findAll();
    }

    /**
     * {@code GET  /ticket-types/:id} : get the "id" ticketType.
     *
     * @param id the id of the ticketTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticket-types/{id}")
    public Mono<ResponseEntity<TicketTypeDTO>> getTicketType(@PathVariable Long id) {
        log.debug("REST request to get TicketType : {}", id);
        Mono<TicketTypeDTO> ticketTypeDTO = ticketTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketTypeDTO);
    }

    /**
     * {@code DELETE  /ticket-types/:id} : delete the "id" ticketType.
     *
     * @param id the id of the ticketTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticket-types/{id}")
    public Mono<ResponseEntity<Void>> deleteTicketType(@PathVariable Long id) {
        log.debug("REST request to delete TicketType : {}", id);
        return ticketTypeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
