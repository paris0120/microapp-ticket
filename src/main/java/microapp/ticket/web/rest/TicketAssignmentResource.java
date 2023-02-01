package microapp.ticket.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.ticket.repository.TicketAssignmentRepository;
import microapp.ticket.service.TicketAssignmentService;
import microapp.ticket.service.dto.TicketAssignmentDTO;
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
 * REST controller for managing {@link microapp.ticket.domain.TicketAssignment}.
 */
@RestController
@RequestMapping("/api")
public class TicketAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(TicketAssignmentResource.class);

    private static final String ENTITY_NAME = "ticketTicketAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketAssignmentService ticketAssignmentService;

    private final TicketAssignmentRepository ticketAssignmentRepository;

    public TicketAssignmentResource(
        TicketAssignmentService ticketAssignmentService,
        TicketAssignmentRepository ticketAssignmentRepository
    ) {
        this.ticketAssignmentService = ticketAssignmentService;
        this.ticketAssignmentRepository = ticketAssignmentRepository;
    }

    /**
     * {@code POST  /ticket-assignments} : Create a new ticketAssignment.
     *
     * @param ticketAssignmentDTO the ticketAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketAssignmentDTO, or with status {@code 400 (Bad Request)} if the ticketAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticket-assignments")
    public Mono<ResponseEntity<TicketAssignmentDTO>> createTicketAssignment(@Valid @RequestBody TicketAssignmentDTO ticketAssignmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save TicketAssignment : {}", ticketAssignmentDTO);
        if (ticketAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ticketAssignmentService
            .save(ticketAssignmentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ticket-assignments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ticket-assignments/:id} : Updates an existing ticketAssignment.
     *
     * @param id the id of the ticketAssignmentDTO to save.
     * @param ticketAssignmentDTO the ticketAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the ticketAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticket-assignments/{id}")
    public Mono<ResponseEntity<TicketAssignmentDTO>> updateTicketAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketAssignmentDTO ticketAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TicketAssignment : {}, {}", id, ticketAssignmentDTO);
        if (ticketAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ticketAssignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ticketAssignmentService
                    .update(ticketAssignmentDTO)
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
     * {@code PATCH  /ticket-assignments/:id} : Partial updates given fields of an existing ticketAssignment, field will ignore if it is null
     *
     * @param id the id of the ticketAssignmentDTO to save.
     * @param ticketAssignmentDTO the ticketAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the ticketAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ticket-assignments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TicketAssignmentDTO>> partialUpdateTicketAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketAssignmentDTO ticketAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TicketAssignment partially : {}, {}", id, ticketAssignmentDTO);
        if (ticketAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ticketAssignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TicketAssignmentDTO> result = ticketAssignmentService.partialUpdate(ticketAssignmentDTO);

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
     * {@code GET  /ticket-assignments} : get all the ticketAssignments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketAssignments in body.
     */
    @GetMapping("/ticket-assignments")
    public Mono<List<TicketAssignmentDTO>> getAllTicketAssignments() {
        log.debug("REST request to get all TicketAssignments");
        return ticketAssignmentService.findAll().collectList();
    }

    /**
     * {@code GET  /ticket-assignments} : get all the ticketAssignments as a stream.
     * @return the {@link Flux} of ticketAssignments.
     */
    @GetMapping(value = "/ticket-assignments", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TicketAssignmentDTO> getAllTicketAssignmentsAsStream() {
        log.debug("REST request to get all TicketAssignments as a stream");
        return ticketAssignmentService.findAll();
    }

    /**
     * {@code GET  /ticket-assignments/:id} : get the "id" ticketAssignment.
     *
     * @param id the id of the ticketAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticket-assignments/{id}")
    public Mono<ResponseEntity<TicketAssignmentDTO>> getTicketAssignment(@PathVariable Long id) {
        log.debug("REST request to get TicketAssignment : {}", id);
        Mono<TicketAssignmentDTO> ticketAssignmentDTO = ticketAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketAssignmentDTO);
    }

    /**
     * {@code DELETE  /ticket-assignments/:id} : delete the "id" ticketAssignment.
     *
     * @param id the id of the ticketAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticket-assignments/{id}")
    public Mono<ResponseEntity<Void>> deleteTicketAssignment(@PathVariable Long id) {
        log.debug("REST request to delete TicketAssignment : {}", id);
        return ticketAssignmentService
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
