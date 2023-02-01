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
import java.util.concurrent.atomic.AtomicLong;
import microapp.ticket.IntegrationTest;
import microapp.ticket.domain.TicketType;
import microapp.ticket.repository.EntityManager;
import microapp.ticket.repository.TicketTypeRepository;
import microapp.ticket.service.dto.TicketTypeDTO;
import microapp.ticket.service.mapper.TicketTypeMapper;
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
 * Integration tests for the {@link TicketTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TicketTypeResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ticket-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TicketType ticketType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketType createEntity(EntityManager em) {
        TicketType ticketType = new TicketType()
            .key(DEFAULT_KEY)
            .type(DEFAULT_TYPE)
            .weight(DEFAULT_WEIGHT)
            .color(DEFAULT_COLOR)
            .icon(DEFAULT_ICON)
            .description(DEFAULT_DESCRIPTION)
            .parentType(DEFAULT_PARENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED)
            .archived(DEFAULT_ARCHIVED);
        return ticketType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketType createUpdatedEntity(EntityManager em) {
        TicketType ticketType = new TicketType()
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .weight(UPDATED_WEIGHT)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON)
            .description(UPDATED_DESCRIPTION)
            .parentType(UPDATED_PARENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .archived(UPDATED_ARCHIVED);
        return ticketType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TicketType.class).block();
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
        ticketType = createEntity(em);
    }

    @Test
    void createTicketType() throws Exception {
        int databaseSizeBeforeCreate = ticketTypeRepository.findAll().collectList().block().size();
        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTicketType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicketType.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testTicketType.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTicketType.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testTicketType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicketType.getParentType()).isEqualTo(DEFAULT_PARENT_TYPE);
        assertThat(testTicketType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTicketType.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicketType.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testTicketType.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    void createTicketTypeWithExistingId() throws Exception {
        // Create the TicketType with an existing ID
        ticketType.setId(1L);
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        int databaseSizeBeforeCreate = ticketTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setKey(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setType(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setWeight(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkParentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setParentType(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setIsActive(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setCreated(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().collectList().block().size();
        // set the field null
        ticketType.setModified(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTicketTypesAsStream() {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        List<TicketType> ticketTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TicketTypeDTO.class)
            .getResponseBody()
            .map(ticketTypeMapper::toEntity)
            .filter(ticketType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(ticketTypeList).isNotNull();
        assertThat(ticketTypeList).hasSize(1);
        TicketType testTicketType = ticketTypeList.get(0);
        assertThat(testTicketType.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTicketType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicketType.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testTicketType.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTicketType.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testTicketType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicketType.getParentType()).isEqualTo(DEFAULT_PARENT_TYPE);
        assertThat(testTicketType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTicketType.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicketType.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testTicketType.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    void getAllTicketTypes() {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        // Get all the ticketTypeList
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
            .value(hasItem(ticketType.getId().intValue()))
            .jsonPath("$.[*].key")
            .value(hasItem(DEFAULT_KEY))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.[*].parentType")
            .value(hasItem(DEFAULT_PARENT_TYPE))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.[*].created")
            .value(hasItem(DEFAULT_CREATED.toString()))
            .jsonPath("$.[*].modified")
            .value(hasItem(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.[*].archived")
            .value(hasItem(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    void getTicketType() {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        // Get the ticketType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ticketType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ticketType.getId().intValue()))
            .jsonPath("$.key")
            .value(is(DEFAULT_KEY))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.parentType")
            .value(is(DEFAULT_PARENT_TYPE))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.created")
            .value(is(DEFAULT_CREATED.toString()))
            .jsonPath("$.modified")
            .value(is(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.archived")
            .value(is(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    void getNonExistingTicketType() {
        // Get the ticketType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();

        // Update the ticketType
        TicketType updatedTicketType = ticketTypeRepository.findById(ticketType.getId()).block();
        updatedTicketType
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .weight(UPDATED_WEIGHT)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON)
            .description(UPDATED_DESCRIPTION)
            .parentType(UPDATED_PARENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .archived(UPDATED_ARCHIVED);
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(updatedTicketType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTicketType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicketType.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testTicketType.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTicketType.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicketType.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTicketType.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicketType.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicketType.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void putNonExistingTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();
        ticketType.setId(count.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ticketTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();
        ticketType.setId(count.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();
        ticketType.setId(count.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTicketTypeWithPatch() throws Exception {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();

        // Update the ticketType using partial update
        TicketType partialUpdatedTicketType = new TicketType();
        partialUpdatedTicketType.setId(ticketType.getId());

        partialUpdatedTicketType.key(UPDATED_KEY).color(UPDATED_COLOR).description(UPDATED_DESCRIPTION).parentType(UPDATED_PARENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicketType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTicketType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicketType.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testTicketType.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTicketType.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicketType.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testTicketType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTicketType.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTicketType.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testTicketType.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    void fullUpdateTicketTypeWithPatch() throws Exception {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();

        // Update the ticketType using partial update
        TicketType partialUpdatedTicketType = new TicketType();
        partialUpdatedTicketType.setId(ticketType.getId());

        partialUpdatedTicketType
            .key(UPDATED_KEY)
            .type(UPDATED_TYPE)
            .weight(UPDATED_WEIGHT)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON)
            .description(UPDATED_DESCRIPTION)
            .parentType(UPDATED_PARENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .archived(UPDATED_ARCHIVED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTicketType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTicketType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicketType.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testTicketType.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTicketType.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicketType.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTicketType.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTicketType.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testTicketType.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    void patchNonExistingTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();
        ticketType.setId(count.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ticketTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();
        ticketType.setId(count.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().collectList().block().size();
        ticketType.setId(count.incrementAndGet());

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ticketTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTicketType() {
        // Initialize the database
        ticketTypeRepository.save(ticketType).block();

        int databaseSizeBeforeDelete = ticketTypeRepository.findAll().collectList().block().size();

        // Delete the ticketType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ticketType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll().collectList().block();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
