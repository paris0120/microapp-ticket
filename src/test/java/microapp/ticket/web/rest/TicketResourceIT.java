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
import microapp.ticket.domain.Ticket;
import microapp.ticket.repository.EntityManager;
import microapp.ticket.repository.TicketRepository;
import microapp.ticket.service.dto.TicketDTO;
import microapp.ticket.service.mapper.TicketMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TicketResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_WORKFLOW_STATUS_KEY = "AAAAAAAAAA";
    private static final String UPDATED_WORKFLOW_STATUS_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY_LEVEL = 1;
    private static final Integer UPDATED_PRIORITY_LEVEL = 2;

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_COMMENTS = 1;
    private static final Integer UPDATED_TOTAL_COMMENTS = 2;

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .username(DEFAULT_USERNAME)
            .userFirstName(DEFAULT_USER_FIRST_NAME)
            .userLastName(DEFAULT_USER_LAST_NAME)
            .userDisplayName(DEFAULT_USER_DISPLAY_NAME)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .typeKey(DEFAULT_TYPE_KEY)
            .workflowStatusKey(DEFAULT_WORKFLOW_STATUS_KEY)
            .priorityLevel(DEFAULT_PRIORITY_LEVEL)
            .tags(DEFAULT_TAGS)
            .totalComments(DEFAULT_TOTAL_COMMENTS)
            .uuid(DEFAULT_UUID)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED)
            .updated(DEFAULT_UPDATED)
            .closed(DEFAULT_CLOSED)
            .archived(DEFAULT_ARCHIVED);
        return ticket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .username(UPDATED_USERNAME)
            .userFirstName(UPDATED_USER_FIRST_NAME)
            .userLastName(UPDATED_USER_LAST_NAME)
            .userDisplayName(UPDATED_USER_DISPLAY_NAME)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .typeKey(UPDATED_TYPE_KEY)
            .workflowStatusKey(UPDATED_WORKFLOW_STATUS_KEY)
            .priorityLevel(UPDATED_PRIORITY_LEVEL)
            .tags(UPDATED_TAGS)
            .totalComments(UPDATED_TOTAL_COMMENTS)
            .uuid(UPDATED_UUID)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .updated(UPDATED_UPDATED)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);
        return ticket;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ticket.class).block();
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
        ticket = createEntity(em);
    }

    @Test
    void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().collectList().block().size();
        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTicket.getUserFirstName()).isEqualTo(DEFAULT_USER_FIRST_NAME);
        assertThat(testTicket.getUserLastName()).isEqualTo(DEFAULT_USER_LAST_NAME);
        assertThat(testTicket.getUserDisplayName()).isEqualTo(DEFAULT_USER_DISPLAY_NAME);
        assertThat(testTicket.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTicket.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTicket.getTypeKey()).isEqualTo(DEFAULT_TYPE_KEY);
        assertThat(testTicket.getWorkflowStatusKey()).isEqualTo(DEFAULT_WORKFLOW_STATUS_KEY);
        assertThat(testTicket.getPriorityLevel()).isEqualTo(DEFAULT_PRIORITY_LEVEL);
        assertThat(testTicket.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testTicket.getTotalComments()).isEqualTo(DEFAULT_TOTAL_COMMENTS);
        assertThat(testTicket.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testTicket.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicket.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testTicket.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testTicket.getClosed()).isEqualTo(DEFAULT_CLOSED);
        assertThat(testTicket.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    void createTicketWithExistingId() throws Exception {
        // Create the Ticket with an existing ID
        ticket.setId(1L);
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        int databaseSizeBeforeCreate = ticketRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setUserDisplayName(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setTitle(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setTypeKey(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWorkflowStatusKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setWorkflowStatusKey(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriorityLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setPriorityLevel(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalCommentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setTotalComments(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setCreated(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setModified(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().collectList().block().size();
        // set the field null
        ticket.setUpdated(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTickets() {
        // Initialize the database
        ticketRepository.save(ticket).block();

        // Get all the ticketList
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
            .value(hasItem(ticket.getId().intValue()))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].userFirstName")
            .value(hasItem(DEFAULT_USER_FIRST_NAME))
            .jsonPath("$.[*].userLastName")
            .value(hasItem(DEFAULT_USER_LAST_NAME))
            .jsonPath("$.[*].userDisplayName")
            .value(hasItem(DEFAULT_USER_DISPLAY_NAME))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT.toString()))
            .jsonPath("$.[*].typeKey")
            .value(hasItem(DEFAULT_TYPE_KEY))
            .jsonPath("$.[*].workflowStatusKey")
            .value(hasItem(DEFAULT_WORKFLOW_STATUS_KEY))
            .jsonPath("$.[*].priorityLevel")
            .value(hasItem(DEFAULT_PRIORITY_LEVEL))
            .jsonPath("$.[*].tags")
            .value(hasItem(DEFAULT_TAGS.toString()))
            .jsonPath("$.[*].totalComments")
            .value(hasItem(DEFAULT_TOTAL_COMMENTS))
            .jsonPath("$.[*].uuid")
            .value(hasItem(DEFAULT_UUID.toString()))
            .jsonPath("$.[*].created")
            .value(hasItem(DEFAULT_CREATED.toString()))
            .jsonPath("$.[*].modified")
            .value(hasItem(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.[*].updated")
            .value(hasItem(DEFAULT_UPDATED.toString()))
            .jsonPath("$.[*].closed")
            .value(hasItem(DEFAULT_CLOSED.toString()))
            .jsonPath("$.[*].archived")
            .value(hasItem(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    void getTicket() {
        // Initialize the database
        ticketRepository.save(ticket).block();

        // Get the ticket
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ticket.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ticket.getId().intValue()))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.userFirstName")
            .value(is(DEFAULT_USER_FIRST_NAME))
            .jsonPath("$.userLastName")
            .value(is(DEFAULT_USER_LAST_NAME))
            .jsonPath("$.userDisplayName")
            .value(is(DEFAULT_USER_DISPLAY_NAME))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT.toString()))
            .jsonPath("$.typeKey")
            .value(is(DEFAULT_TYPE_KEY))
            .jsonPath("$.workflowStatusKey")
            .value(is(DEFAULT_WORKFLOW_STATUS_KEY))
            .jsonPath("$.priorityLevel")
            .value(is(DEFAULT_PRIORITY_LEVEL))
            .jsonPath("$.tags")
            .value(is(DEFAULT_TAGS.toString()))
            .jsonPath("$.totalComments")
            .value(is(DEFAULT_TOTAL_COMMENTS))
            .jsonPath("$.uuid")
            .value(is(DEFAULT_UUID.toString()))
            .jsonPath("$.created")
            .value(is(DEFAULT_CREATED.toString()))
            .jsonPath("$.modified")
            .value(is(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.updated")
            .value(is(DEFAULT_UPDATED.toString()))
            .jsonPath("$.closed")
            .value(is(DEFAULT_CLOSED.toString()))
            .jsonPath("$.archived")
            .value(is(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    void getNonExistingTicket() {
        // Get the ticket
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTicket() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket).block();

        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).block();
        updatedTicket
            .username(UPDATED_USERNAME)
            .userFirstName(UPDATED_USER_FIRST_NAME)
            .userLastName(UPDATED_USER_LAST_NAME)
            .userDisplayName(UPDATED_USER_DISPLAY_NAME)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .typeKey(UPDATED_TYPE_KEY)
            .workflowStatusKey(UPDATED_WORKFLOW_STATUS_KEY)
            .priorityLevel(UPDATED_PRIORITY_LEVEL)
            .tags(UPDATED_TAGS)
            .totalComments(UPDATED_TOTAL_COMMENTS)
            .uuid(UPDATED_UUID)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .updated(UPDATED_UPDATED)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);
        TicketDTO ticketDTO = ticketMapper.toDto(updatedTicket);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTicket.getUserFirstName()).isEqualTo(UPDATED_USER_FIRST_NAME);
        assertThat(testTicket.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
        assertThat(testTicket.getUserDisplayName()).isEqualTo(UPDATED_USER_DISPLAY_NAME);
        assertThat(testTicket.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTicket.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTicket.getTypeKey()).isEqualTo(UPDATED_TYPE_KEY);
        assertThat(testTicket.getWorkflowStatusKey()).isEqualTo(UPDATED_WORKFLOW_STATUS_KEY);
        assertThat(testTicket.getPriorityLevel()).isEqualTo(UPDATED_PRIORITY_LEVEL);
        assertThat(testTicket.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testTicket.getTotalComments()).isEqualTo(UPDATED_TOTAL_COMMENTS);
        assertThat(testTicket.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testTicket.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicket.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicket.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testTicket.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testTicket.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void putNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();
        ticket.setId(count.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();
        ticket.setId(count.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();
        ticket.setId(count.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket).block();

        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket
            .userLastName(UPDATED_USER_LAST_NAME)
            .workflowStatusKey(UPDATED_WORKFLOW_STATUS_KEY)
            .priorityLevel(UPDATED_PRIORITY_LEVEL)
            .tags(UPDATED_TAGS)
            .totalComments(UPDATED_TOTAL_COMMENTS)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicket.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTicket.getUserFirstName()).isEqualTo(DEFAULT_USER_FIRST_NAME);
        assertThat(testTicket.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
        assertThat(testTicket.getUserDisplayName()).isEqualTo(DEFAULT_USER_DISPLAY_NAME);
        assertThat(testTicket.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTicket.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTicket.getTypeKey()).isEqualTo(DEFAULT_TYPE_KEY);
        assertThat(testTicket.getWorkflowStatusKey()).isEqualTo(UPDATED_WORKFLOW_STATUS_KEY);
        assertThat(testTicket.getPriorityLevel()).isEqualTo(UPDATED_PRIORITY_LEVEL);
        assertThat(testTicket.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testTicket.getTotalComments()).isEqualTo(UPDATED_TOTAL_COMMENTS);
        assertThat(testTicket.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testTicket.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicket.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicket.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testTicket.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testTicket.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void fullUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket).block();

        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket
            .username(UPDATED_USERNAME)
            .userFirstName(UPDATED_USER_FIRST_NAME)
            .userLastName(UPDATED_USER_LAST_NAME)
            .userDisplayName(UPDATED_USER_DISPLAY_NAME)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .typeKey(UPDATED_TYPE_KEY)
            .workflowStatusKey(UPDATED_WORKFLOW_STATUS_KEY)
            .priorityLevel(UPDATED_PRIORITY_LEVEL)
            .tags(UPDATED_TAGS)
            .totalComments(UPDATED_TOTAL_COMMENTS)
            .uuid(UPDATED_UUID)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .updated(UPDATED_UPDATED)
            .closed(UPDATED_CLOSED)
            .archived(UPDATED_ARCHIVED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicket.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTicket.getUserFirstName()).isEqualTo(UPDATED_USER_FIRST_NAME);
        assertThat(testTicket.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
        assertThat(testTicket.getUserDisplayName()).isEqualTo(UPDATED_USER_DISPLAY_NAME);
        assertThat(testTicket.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTicket.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTicket.getTypeKey()).isEqualTo(UPDATED_TYPE_KEY);
        assertThat(testTicket.getWorkflowStatusKey()).isEqualTo(UPDATED_WORKFLOW_STATUS_KEY);
        assertThat(testTicket.getPriorityLevel()).isEqualTo(UPDATED_PRIORITY_LEVEL);
        assertThat(testTicket.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testTicket.getTotalComments()).isEqualTo(UPDATED_TOTAL_COMMENTS);
        assertThat(testTicket.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testTicket.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicket.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicket.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testTicket.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testTicket.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void patchNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();
        ticket.setId(count.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ticketDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();
        ticket.setId(count.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().collectList().block().size();
        ticket.setId(count.incrementAndGet());

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTicket() {
        // Initialize the database
        ticketRepository.save(ticket).block();

        int databaseSizeBeforeDelete = ticketRepository.findAll().collectList().block().size();

        // Delete the ticket
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ticket.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ticket> ticketList = ticketRepository.findAll().collectList().block();
        assertThat(ticketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
