import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('TicketType e2e test', () => {
  const ticketTypePageUrl = '/ticket/ticket-type';
  const ticketTypePageUrlPattern = new RegExp('/ticket/ticket-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ticketTypeSample = {
    key: 'ivory Phased Granite',
    type: 'haptic monitor Home',
    weight: 87027,
    description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    parentType: 'cyan',
    isActive: false,
    created: '2023-02-01T11:42:30.979Z',
    modified: '2023-01-31T21:33:59.598Z',
  };

  let ticketType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/ticket/api/ticket-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/ticket/api/ticket-types').as('postEntityRequest');
    cy.intercept('DELETE', '/services/ticket/api/ticket-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ticketType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/ticket/api/ticket-types/${ticketType.id}`,
      }).then(() => {
        ticketType = undefined;
      });
    }
  });

  it('TicketTypes menu should load TicketTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ticket/ticket-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TicketType').should('exist');
    cy.url().should('match', ticketTypePageUrlPattern);
  });

  describe('TicketType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ticketTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TicketType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ticket/ticket-type/new$'));
        cy.getEntityCreateUpdateHeading('TicketType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/ticket/api/ticket-types',
          body: ticketTypeSample,
        }).then(({ body }) => {
          ticketType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/ticket/api/ticket-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ticketType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ticketTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TicketType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ticketType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketTypePageUrlPattern);
      });

      it('edit button click should load edit TicketType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TicketType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketTypePageUrlPattern);
      });

      it('edit button click should load edit TicketType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TicketType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketTypePageUrlPattern);
      });

      it('last delete button click should delete instance of TicketType', () => {
        cy.intercept('GET', '/services/ticket/api/ticket-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ticketType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketTypePageUrlPattern);

        ticketType = undefined;
      });
    });
  });

  describe('new TicketType page', () => {
    beforeEach(() => {
      cy.visit(`${ticketTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TicketType');
    });

    it('should create an instance of TicketType', () => {
      cy.get(`[data-cy="key"]`).type('invoice').should('have.value', 'invoice');

      cy.get(`[data-cy="type"]`).type('Tuna').should('have.value', 'Tuna');

      cy.get(`[data-cy="weight"]`).type('13768').should('have.value', '13768');

      cy.get(`[data-cy="color"]`).type('purple').should('have.value', 'purple');

      cy.get(`[data-cy="icon"]`).type('Terrace').should('have.value', 'Terrace');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="parentType"]`).type('THX upward-trending').should('have.value', 'THX upward-trending');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click().should('be.checked');

      cy.get(`[data-cy="created"]`).type('2023-01-31T20:43').blur().should('have.value', '2023-01-31T20:43');

      cy.get(`[data-cy="modified"]`).type('2023-02-01T02:06').blur().should('have.value', '2023-02-01T02:06');

      cy.get(`[data-cy="archived"]`).type('2023-02-01T03:31').blur().should('have.value', '2023-02-01T03:31');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ticketType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ticketTypePageUrlPattern);
    });
  });
});
