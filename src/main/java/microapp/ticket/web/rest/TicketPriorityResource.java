package microapp.ticket.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.ticket.repository.TicketPriorityRepository;
import microapp.ticket.service.TicketPriorityService;
import microapp.ticket.service.dto.TicketPriorityDTO;
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
 * REST controller for managing {@link microapp.ticket.domain.TicketPriority}.
 */
@RestController
@RequestMapping("/api")
public class TicketPriorityResource {

    private final Logger log = LoggerFactory.getLogger(TicketPriorityResource.class);

    private static final String ENTITY_NAME = "ticketTicketPriority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketPriorityService ticketPriorityService;

    private final TicketPriorityRepository ticketPriorityRepository;

    public TicketPriorityResource(TicketPriorityService ticketPriorityService, TicketPriorityRepository ticketPriorityRepository) {
        this.ticketPriorityService = ticketPriorityService;
        this.ticketPriorityRepository = ticketPriorityRepository;
    }

    /**
     * {@code POST  /ticket-priorities} : Create a new ticketPriority.
     *
     * @param ticketPriorityDTO the ticketPriorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketPriorityDTO, or with status {@code 400 (Bad Request)} if the ticketPriority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticket-priorities")
    public Mono<ResponseEntity<TicketPriorityDTO>> createTicketPriority(@Valid @RequestBody TicketPriorityDTO ticketPriorityDTO)
        throws URISyntaxException {
        log.debug("REST request to save TicketPriority : {}", ticketPriorityDTO);
        if (ticketPriorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketPriority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ticketPriorityService
            .save(ticketPriorityDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ticket-priorities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ticket-priorities/:id} : Updates an existing ticketPriority.
     *
     * @param id the id of the ticketPriorityDTO to save.
     * @param ticketPriorityDTO the ticketPriorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketPriorityDTO,
     * or with status {@code 400 (Bad Request)} if the ticketPriorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketPriorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticket-priorities/{id}")
    public Mono<ResponseEntity<TicketPriorityDTO>> updateTicketPriority(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketPriorityDTO ticketPriorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TicketPriority : {}, {}", id, ticketPriorityDTO);
        if (ticketPriorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketPriorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ticketPriorityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ticketPriorityService
                    .update(ticketPriorityDTO)
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
     * {@code PATCH  /ticket-priorities/:id} : Partial updates given fields of an existing ticketPriority, field will ignore if it is null
     *
     * @param id the id of the ticketPriorityDTO to save.
     * @param ticketPriorityDTO the ticketPriorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketPriorityDTO,
     * or with status {@code 400 (Bad Request)} if the ticketPriorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketPriorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketPriorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ticket-priorities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TicketPriorityDTO>> partialUpdateTicketPriority(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketPriorityDTO ticketPriorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TicketPriority partially : {}, {}", id, ticketPriorityDTO);
        if (ticketPriorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketPriorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ticketPriorityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TicketPriorityDTO> result = ticketPriorityService.partialUpdate(ticketPriorityDTO);

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
     * {@code GET  /ticket-priorities} : get all the ticketPriorities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketPriorities in body.
     */
    @GetMapping("/ticket-priorities")
    public Mono<List<TicketPriorityDTO>> getAllTicketPriorities() {
        log.debug("REST request to get all TicketPriorities");
        return ticketPriorityService.findAll().collectList();
    }

    /**
     * {@code GET  /ticket-priorities} : get all the ticketPriorities as a stream.
     * @return the {@link Flux} of ticketPriorities.
     */
    @GetMapping(value = "/ticket-priorities", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TicketPriorityDTO> getAllTicketPrioritiesAsStream() {
        log.debug("REST request to get all TicketPriorities as a stream");
        return ticketPriorityService.findAll();
    }

    /**
     * {@code GET  /ticket-priorities/:id} : get the "id" ticketPriority.
     *
     * @param id the id of the ticketPriorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketPriorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticket-priorities/{id}")
    public Mono<ResponseEntity<TicketPriorityDTO>> getTicketPriority(@PathVariable Long id) {
        log.debug("REST request to get TicketPriority : {}", id);
        Mono<TicketPriorityDTO> ticketPriorityDTO = ticketPriorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketPriorityDTO);
    }

    /**
     * {@code DELETE  /ticket-priorities/:id} : delete the "id" ticketPriority.
     *
     * @param id the id of the ticketPriorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticket-priorities/{id}")
    public Mono<ResponseEntity<Void>> deleteTicketPriority(@PathVariable Long id) {
        log.debug("REST request to delete TicketPriority : {}", id);
        return ticketPriorityService
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
