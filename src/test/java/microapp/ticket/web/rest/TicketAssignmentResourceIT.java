package microapp.ticket.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import microapp.ticket.IntegrationTest;
import microapp.ticket.domain.TicketAssignment;
import microapp.ticket.repository.EntityManager;
import microapp.ticket.repository.TicketAssignmentRepository;
import microapp.ticket.service.dto.TicketAssignmentDTO;
import microapp.ticket.service.mapper.TicketAssignmentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TicketAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TicketAssignmentResourceIT {

    private static final Long DEFAULT_ISSUE_ID = 1L;
    private static final Long UPDATED_ISSUE_ID = 2L;

    private static final UUID DEFAULT_ISSUE_UUID = UUID.randomUUID();
    private static final UUID UPDATED_ISSUE_UUID = UUID.randomUUID();

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROLE_WEIGHT = 1;
    private static final Integer UPDATED_ROLE_WEIGHT = 2;

    private static final String DEFAULT_DEPARTMENT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEPARTMENT_WEIGHT = 1;
    private static final Integer UPDATED_DEPARTMENT_WEIGHT = 2;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACCEPTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCEPTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LEFT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LEFT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ticket-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketAssignmentRepository ticketAssignmentRepository;

    @Autowired
    private TicketAssignmentMapper ticketAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TicketAssignment ticketAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketAssignment createEntity(EntityManager em) {
        TicketAssignment ticketAssignment = new TicketAssignment()
            .issueId(DEFAULT_ISSUE_ID)
            .issueUuid(DEFAULT_ISSUE_UUID)
            .username(DEFAULT_USERNAME)
            .roleKey(DEFAULT_ROLE_KEY)
            .roleWeight(DEFAULT_ROLE_WEIGHT)
            .departmentKey(DEFAULT_DEPARTMENT_KEY)
            .departmentWeight(DEFAULT_DEPARTMENT_WEIGHT)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED)
            .accepted(DEFAULT_ACCEPTED)
            .left(DEFAULT_LEFT)
            .closed(DEFAULT_CLOSED)
            .archived(DEFAULT_ARCHIVED);
        return ticketAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketAssignment createUpdatedEntity(EntityManager em) {
        TicketAssignment ticketAssignment = new TicketAssignment()
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .username(UPDATED_USERNAME)
            .roleKey(UPDATED_ROLE_KEY)
            .roleWeight(UPDATED_ROLE_WEIGHT)
            .departmentKey(UPDATED_DEPARTMENT_KEY)
            .departmentWeight(UPDATED_DEPARTMENT_WEIGHT)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .accepted(UPDATED_ACCEPTED)
            .left(UPDATED_LEFT)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);
        return ticketAssignment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TicketAssignment.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        ticketAssignment = createEntity(em);
    }

    @Test
    void createTicketAssignment() throws Exception {
        int databaseSizeBeforeCreate = ticketAssignmentRepository.findAll().collectList().block().size();
        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeCreate + 1);
        TicketAssignment testTicketAssignment = ticketAssignmentList.get(ticketAssignmentList.size() - 1);
        assertThat(testTicketAssignment.getIssueId()).isEqualTo(DEFAULT_ISSUE_ID);
        assertThat(testTicketAssignment.getIssueUuid()).isEqualTo(DEFAULT_ISSUE_UUID);
        assertThat(testTicketAssignment.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTicketAssignment.getRoleKey()).isEqualTo(DEFAULT_ROLE_KEY);
        assertThat(testTicketAssignment.getRoleWeight()).isEqualTo(DEFAULT_ROLE_WEIGHT);
        assertThat(testTicketAssignment.getDepartmentKey()).isEqualTo(DEFAULT_DEPARTMENT_KEY);
        assertThat(testTicketAssignment.getDepartmentWeight()).isEqualTo(DEFAULT_DEPARTMENT_WEIGHT);
        assertThat(testTicketAssignment.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicketAssignment.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testTicketAssignment.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testTicketAssignment.getLeft()).isEqualTo(DEFAULT_LEFT);
        assertThat(testTicketAssignment.getClosed()).isEqualTo(DEFAULT_CLOSED);
        assertThat(testTicketAssignment.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    void createTicketAssignmentWithExistingId() throws Exception {
        // Create the TicketAssignment with an existing ID
        ticketAssignment.setId(1L);
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        int databaseSizeBeforeCreate = ticketAssignmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIssueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setIssueId(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setIssueUuid(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setUsername(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRoleKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setRoleKey(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRoleWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setRoleWeight(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDepartmentKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setDepartmentKey(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDepartmentWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setDepartmentWeight(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setCreated(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        ticketAssignment.setModified(null);

        // Create the TicketAssignment, which fails.
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTicketAssignmentsAsStream() {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        List<TicketAssignment> ticketAssignmentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TicketAssignmentDTO.class)
            .getResponseBody()
            .map(ticketAssignmentMapper::toEntity)
            .filter(ticketAssignment::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(ticketAssignmentList).isNotNull();
        assertThat(ticketAssignmentList).hasSize(1);
        TicketAssignment testTicketAssignment = ticketAssignmentList.get(0);
        assertThat(testTicketAssignment.getIssueId()).isEqualTo(DEFAULT_ISSUE_ID);
        assertThat(testTicketAssignment.getIssueUuid()).isEqualTo(DEFAULT_ISSUE_UUID);
        assertThat(testTicketAssignment.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTicketAssignment.getRoleKey()).isEqualTo(DEFAULT_ROLE_KEY);
        assertThat(testTicketAssignment.getRoleWeight()).isEqualTo(DEFAULT_ROLE_WEIGHT);
        assertThat(testTicketAssignment.getDepartmentKey()).isEqualTo(DEFAULT_DEPARTMENT_KEY);
        assertThat(testTicketAssignment.getDepartmentWeight()).isEqualTo(DEFAULT_DEPARTMENT_WEIGHT);
        assertThat(testTicketAssignment.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicketAssignment.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testTicketAssignment.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testTicketAssignment.getLeft()).isEqualTo(DEFAULT_LEFT);
        assertThat(testTicketAssignment.getClosed()).isEqualTo(DEFAULT_CLOSED);
        assertThat(testTicketAssignment.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    void getAllTicketAssignments() {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        // Get all the ticketAssignmentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(ticketAssignment.getId().intValue()))
            .jsonPath("$.[*].issueId")
            .value(hasItem(DEFAULT_ISSUE_ID.intValue()))
            .jsonPath("$.[*].issueUuid")
            .value(hasItem(DEFAULT_ISSUE_UUID.toString()))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].roleKey")
            .value(hasItem(DEFAULT_ROLE_KEY))
            .jsonPath("$.[*].roleWeight")
            .value(hasItem(DEFAULT_ROLE_WEIGHT))
            .jsonPath("$.[*].departmentKey")
            .value(hasItem(DEFAULT_DEPARTMENT_KEY))
            .jsonPath("$.[*].departmentWeight")
            .value(hasItem(DEFAULT_DEPARTMENT_WEIGHT))
            .jsonPath("$.[*].created")
            .value(hasItem(DEFAULT_CREATED.toString()))
            .jsonPath("$.[*].modified")
            .value(hasItem(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.[*].accepted")
            .value(hasItem(DEFAULT_ACCEPTED.toString()))
            .jsonPath("$.[*].left")
            .value(hasItem(DEFAULT_LEFT.toString()))
            .jsonPath("$.[*].closed")
            .value(hasItem(DEFAULT_CLOSED.toString()))
            .jsonPath("$.[*].archived")
            .value(hasItem(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    void getTicketAssignment() {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        // Get the ticketAssignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ticketAssignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ticketAssignment.getId().intValue()))
            .jsonPath("$.issueId")
            .value(is(DEFAULT_ISSUE_ID.intValue()))
            .jsonPath("$.issueUuid")
            .value(is(DEFAULT_ISSUE_UUID.toString()))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.roleKey")
            .value(is(DEFAULT_ROLE_KEY))
            .jsonPath("$.roleWeight")
            .value(is(DEFAULT_ROLE_WEIGHT))
            .jsonPath("$.departmentKey")
            .value(is(DEFAULT_DEPARTMENT_KEY))
            .jsonPath("$.departmentWeight")
            .value(is(DEFAULT_DEPARTMENT_WEIGHT))
            .jsonPath("$.created")
            .value(is(DEFAULT_CREATED.toString()))
            .jsonPath("$.modified")
            .value(is(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.accepted")
            .value(is(DEFAULT_ACCEPTED.toString()))
            .jsonPath("$.left")
            .value(is(DEFAULT_LEFT.toString()))
            .jsonPath("$.closed")
            .value(is(DEFAULT_CLOSED.toString()))
            .jsonPath("$.archived")
            .value(is(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    void getNonExistingTicketAssignment() {
        // Get the ticketAssignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTicketAssignment() throws Exception {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();

        // Update the ticketAssignment
        TicketAssignment updatedTicketAssignment = ticketAssignmentRepository.findById(ticketAssignment.getId()).block();
        updatedTicketAssignment
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .username(UPDATED_USERNAME)
            .roleKey(UPDATED_ROLE_KEY)
            .roleWeight(UPDATED_ROLE_WEIGHT)
            .departmentKey(UPDATED_DEPARTMENT_KEY)
            .departmentWeight(UPDATED_DEPARTMENT_WEIGHT)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .accepted(UPDATED_ACCEPTED)
            .left(UPDATED_LEFT)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(updatedTicketAssignment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketAssignmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
        TicketAssignment testTicketAssignment = ticketAssignmentList.get(ticketAssignmentList.size() - 1);
        assertThat(testTicketAssignment.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testTicketAssignment.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testTicketAssignment.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTicketAssignment.getRoleKey()).isEqualTo(UPDATED_ROLE_KEY);
        assertThat(testTicketAssignment.getRoleWeight()).isEqualTo(UPDATED_ROLE_WEIGHT);
        assertThat(testTicketAssignment.getDepartmentKey()).isEqualTo(UPDATED_DEPARTMENT_KEY);
        assertThat(testTicketAssignment.getDepartmentWeight()).isEqualTo(UPDATED_DEPARTMENT_WEIGHT);
        assertThat(testTicketAssignment.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicketAssignment.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicketAssignment.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testTicketAssignment.getLeft()).isEqualTo(UPDATED_LEFT);
        assertThat(testTicketAssignment.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testTicketAssignment.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void putNonExistingTicketAssignment() throws Exception {
        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();
        ticketAssignment.setId(count.incrementAndGet());

        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketAssignmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTicketAssignment() throws Exception {
        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();
        ticketAssignment.setId(count.incrementAndGet());

        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTicketAssignment() throws Exception {
        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();
        ticketAssignment.setId(count.incrementAndGet());

        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTicketAssignmentWithPatch() throws Exception {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();

        // Update the ticketAssignment using partial update
        TicketAssignment partialUpdatedTicketAssignment = new TicketAssignment();
        partialUpdatedTicketAssignment.setId(ticketAssignment.getId());

        partialUpdatedTicketAssignment
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .roleKey(UPDATED_ROLE_KEY)
            .roleWeight(UPDATED_ROLE_WEIGHT)
            .departmentKey(UPDATED_DEPARTMENT_KEY)
            .modified(UPDATED_MODIFIED)
            .accepted(UPDATED_ACCEPTED)
            .left(UPDATED_LEFT)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicketAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
        TicketAssignment testTicketAssignment = ticketAssignmentList.get(ticketAssignmentList.size() - 1);
        assertThat(testTicketAssignment.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testTicketAssignment.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testTicketAssignment.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTicketAssignment.getRoleKey()).isEqualTo(UPDATED_ROLE_KEY);
        assertThat(testTicketAssignment.getRoleWeight()).isEqualTo(UPDATED_ROLE_WEIGHT);
        assertThat(testTicketAssignment.getDepartmentKey()).isEqualTo(UPDATED_DEPARTMENT_KEY);
        assertThat(testTicketAssignment.getDepartmentWeight()).isEqualTo(DEFAULT_DEPARTMENT_WEIGHT);
        assertThat(testTicketAssignment.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicketAssignment.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicketAssignment.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testTicketAssignment.getLeft()).isEqualTo(UPDATED_LEFT);
        assertThat(testTicketAssignment.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testTicketAssignment.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void fullUpdateTicketAssignmentWithPatch() throws Exception {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();

        // Update the ticketAssignment using partial update
        TicketAssignment partialUpdatedTicketAssignment = new TicketAssignment();
        partialUpdatedTicketAssignment.setId(ticketAssignment.getId());

        partialUpdatedTicketAssignment
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .username(UPDATED_USERNAME)
            .roleKey(UPDATED_ROLE_KEY)
            .roleWeight(UPDATED_ROLE_WEIGHT)
            .departmentKey(UPDATED_DEPARTMENT_KEY)
            .departmentWeight(UPDATED_DEPARTMENT_WEIGHT)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .accepted(UPDATED_ACCEPTED)
            .left(UPDATED_LEFT)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicketAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
        TicketAssignment testTicketAssignment = ticketAssignmentList.get(ticketAssignmentList.size() - 1);
        assertThat(testTicketAssignment.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testTicketAssignment.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testTicketAssignment.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTicketAssignment.getRoleKey()).isEqualTo(UPDATED_ROLE_KEY);
        assertThat(testTicketAssignment.getRoleWeight()).isEqualTo(UPDATED_ROLE_WEIGHT);
        assertThat(testTicketAssignment.getDepartmentKey()).isEqualTo(UPDATED_DEPARTMENT_KEY);
        assertThat(testTicketAssignment.getDepartmentWeight()).isEqualTo(UPDATED_DEPARTMENT_WEIGHT);
        assertThat(testTicketAssignment.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicketAssignment.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicketAssignment.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testTicketAssignment.getLeft()).isEqualTo(UPDATED_LEFT);
        assertThat(testTicketAssignment.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testTicketAssignment.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void patchNonExistingTicketAssignment() throws Exception {
        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();
        ticketAssignment.setId(count.incrementAndGet());

        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ticketAssignmentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTicketAssignment() throws Exception {
        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();
        ticketAssignment.setId(count.incrementAndGet());

        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTicketAssignment() throws Exception {
        int databaseSizeBeforeUpdate = ticketAssignmentRepository.findAll().collectList().block().size();
        ticketAssignment.setId(count.incrementAndGet());

        // Create the TicketAssignment
        TicketAssignmentDTO ticketAssignmentDTO = ticketAssignmentMapper.toDto(ticketAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketAssignmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TicketAssignment in the database
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTicketAssignment() {
        // Initialize the database
        ticketAssignmentRepository.save(ticketAssignment).block();

        int databaseSizeBeforeDelete = ticketAssignmentRepository.findAll().collectList().block().size();

        // Delete the ticketAssignment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ticketAssignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TicketAssignment> ticketAssignmentList = ticketAssignmentRepository.findAll().collectList().block();
        assertThat(ticketAssignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
