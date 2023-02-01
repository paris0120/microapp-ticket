package microapp.ticket.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import microapp.ticket.IntegrationTest;
import microapp.ticket.domain.TicketPriority;
import microapp.ticket.repository.EntityManager;
import microapp.ticket.repository.TicketPriorityRepository;
import microapp.ticket.service.dto.TicketPriorityDTO;
import microapp.ticket.service.mapper.TicketPriorityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TicketPriorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TicketPriorityResourceIT {

    private static final Integer DEFAULT_PRIORITY_LEVEL = 1;
    private static final Integer UPDATED_PRIORITY_LEVEL = 2;

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ticket-priorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketPriorityRepository ticketPriorityRepository;

    @Autowired
    private TicketPriorityMapper ticketPriorityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TicketPriority ticketPriority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketPriority createEntity(EntityManager em) {
        TicketPriority ticketPriority = new TicketPriority()
            .priorityLevel(DEFAULT_PRIORITY_LEVEL)
            .priority(DEFAULT_PRIORITY)
            .color(DEFAULT_COLOR)
            .icon(DEFAULT_ICON);
        return ticketPriority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketPriority createUpdatedEntity(EntityManager em) {
        TicketPriority ticketPriority = new TicketPriority()
            .priorityLevel(UPDATED_PRIORITY_LEVEL)
            .priority(UPDATED_PRIORITY)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON);
        return ticketPriority;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TicketPriority.class).block();
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
        ticketPriority = createEntity(em);
    }

    @Test
    void createTicketPriority() throws Exception {
        int databaseSizeBeforeCreate = ticketPriorityRepository.findAll().collectList().block().size();
        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeCreate + 1);
        TicketPriority testTicketPriority = ticketPriorityList.get(ticketPriorityList.size() - 1);
        assertThat(testTicketPriority.getPriorityLevel()).isEqualTo(DEFAULT_PRIORITY_LEVEL);
        assertThat(testTicketPriority.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTicketPriority.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTicketPriority.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    void createTicketPriorityWithExistingId() throws Exception {
        // Create the TicketPriority with an existing ID
        ticketPriority.setId(1L);
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        int databaseSizeBeforeCreate = ticketPriorityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPriorityLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketPriorityRepository.findAll().collectList().block().size();
        // set the field null
        ticketPriority.setPriorityLevel(null);

        // Create the TicketPriority, which fails.
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketPriorityRepository.findAll().collectList().block().size();
        // set the field null
        ticketPriority.setPriority(null);

        // Create the TicketPriority, which fails.
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTicketPrioritiesAsStream() {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        List<TicketPriority> ticketPriorityList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TicketPriorityDTO.class)
            .getResponseBody()
            .map(ticketPriorityMapper::toEntity)
            .filter(ticketPriority::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(ticketPriorityList).isNotNull();
        assertThat(ticketPriorityList).hasSize(1);
        TicketPriority testTicketPriority = ticketPriorityList.get(0);
        assertThat(testTicketPriority.getPriorityLevel()).isEqualTo(DEFAULT_PRIORITY_LEVEL);
        assertThat(testTicketPriority.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTicketPriority.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTicketPriority.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    void getAllTicketPriorities() {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        // Get all the ticketPriorityList
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
            .value(hasItem(ticketPriority.getId().intValue()))
            .jsonPath("$.[*].priorityLevel")
            .value(hasItem(DEFAULT_PRIORITY_LEVEL))
            .jsonPath("$.[*].priority")
            .value(hasItem(DEFAULT_PRIORITY))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON));
    }

    @Test
    void getTicketPriority() {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        // Get the ticketPriority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ticketPriority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ticketPriority.getId().intValue()))
            .jsonPath("$.priorityLevel")
            .value(is(DEFAULT_PRIORITY_LEVEL))
            .jsonPath("$.priority")
            .value(is(DEFAULT_PRIORITY))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON));
    }

    @Test
    void getNonExistingTicketPriority() {
        // Get the ticketPriority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTicketPriority() throws Exception {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();

        // Update the ticketPriority
        TicketPriority updatedTicketPriority = ticketPriorityRepository.findById(ticketPriority.getId()).block();
        updatedTicketPriority.priorityLevel(UPDATED_PRIORITY_LEVEL).priority(UPDATED_PRIORITY).color(UPDATED_COLOR).icon(UPDATED_ICON);
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(updatedTicketPriority);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketPriorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
        TicketPriority testTicketPriority = ticketPriorityList.get(ticketPriorityList.size() - 1);
        assertThat(testTicketPriority.getPriorityLevel()).isEqualTo(UPDATED_PRIORITY_LEVEL);
        assertThat(testTicketPriority.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTicketPriority.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTicketPriority.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    void putNonExistingTicketPriority() throws Exception {
        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();
        ticketPriority.setId(count.incrementAndGet());

        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketPriorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTicketPriority() throws Exception {
        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();
        ticketPriority.setId(count.incrementAndGet());

        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTicketPriority() throws Exception {
        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();
        ticketPriority.setId(count.incrementAndGet());

        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTicketPriorityWithPatch() throws Exception {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();

        // Update the ticketPriority using partial update
        TicketPriority partialUpdatedTicketPriority = new TicketPriority();
        partialUpdatedTicketPriority.setId(ticketPriority.getId());

        partialUpdatedTicketPriority.color(UPDATED_COLOR).icon(UPDATED_ICON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicketPriority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketPriority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
        TicketPriority testTicketPriority = ticketPriorityList.get(ticketPriorityList.size() - 1);
        assertThat(testTicketPriority.getPriorityLevel()).isEqualTo(DEFAULT_PRIORITY_LEVEL);
        assertThat(testTicketPriority.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTicketPriority.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTicketPriority.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    void fullUpdateTicketPriorityWithPatch() throws Exception {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();

        // Update the ticketPriority using partial update
        TicketPriority partialUpdatedTicketPriority = new TicketPriority();
        partialUpdatedTicketPriority.setId(ticketPriority.getId());

        partialUpdatedTicketPriority
            .priorityLevel(UPDATED_PRIORITY_LEVEL)
            .priority(UPDATED_PRIORITY)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicketPriority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketPriority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
        TicketPriority testTicketPriority = ticketPriorityList.get(ticketPriorityList.size() - 1);
        assertThat(testTicketPriority.getPriorityLevel()).isEqualTo(UPDATED_PRIORITY_LEVEL);
        assertThat(testTicketPriority.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTicketPriority.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTicketPriority.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    void patchNonExistingTicketPriority() throws Exception {
        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();
        ticketPriority.setId(count.incrementAndGet());

        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ticketPriorityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTicketPriority() throws Exception {
        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();
        ticketPriority.setId(count.incrementAndGet());

        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTicketPriority() throws Exception {
        int databaseSizeBeforeUpdate = ticketPriorityRepository.findAll().collectList().block().size();
        ticketPriority.setId(count.incrementAndGet());

        // Create the TicketPriority
        TicketPriorityDTO ticketPriorityDTO = ticketPriorityMapper.toDto(ticketPriority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketPriorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TicketPriority in the database
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTicketPriority() {
        // Initialize the database
        ticketPriorityRepository.save(ticketPriority).block();

        int databaseSizeBeforeDelete = ticketPriorityRepository.findAll().collectList().block().size();

        // Delete the ticketPriority
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ticketPriority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TicketPriority> ticketPriorityList = ticketPriorityRepository.findAll().collectList().block();
        assertThat(ticketPriorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
