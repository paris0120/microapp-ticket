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

describe('Ticket e2e test', () => {
  const ticketPageUrl = '/ticket/ticket';
  const ticketPageUrlPattern = new RegExp('/ticket/ticket(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ticketSample = {
    userDisplayName: 'invoice',
    title: 'lime Awesome RSS',
    content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    typeKey: 'Street',
    workflowStatusKey: 'Salad Tools',
    priorityLevel: 49242,
    totalComments: 85101,
    created: '2023-02-01T10:37:32.236Z',
    modified: '2023-01-31T14:45:45.589Z',
    updated: '2023-01-31T21:04:43.543Z',
  };

  let ticket;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/ticket/api/tickets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/ticket/api/tickets').as('postEntityRequest');
    cy.intercept('DELETE', '/services/ticket/api/tickets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ticket) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/ticket/api/tickets/${ticket.id}`,
      }).then(() => {
        ticket = undefined;
      });
    }
  });

  it('Tickets menu should load Tickets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ticket/ticket');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ticket').should('exist');
    cy.url().should('match', ticketPageUrlPattern);
  });

  describe('Ticket page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ticketPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ticket page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ticket/ticket/new$'));
        cy.getEntityCreateUpdateHeading('Ticket');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/ticket/api/tickets',
          body: ticketSample,
        }).then(({ body }) => {
          ticket = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/ticket/api/tickets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/ticket/api/tickets?page=0&size=20>; rel="last",<http://localhost/services/ticket/api/tickets?page=0&size=20>; rel="first"',
              },
              body: [ticket],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ticketPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ticket page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ticket');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketPageUrlPattern);
      });

      it('edit button click should load edit Ticket page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ticket');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketPageUrlPattern);
      });

      it('edit button click should load edit Ticket page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ticket');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketPageUrlPattern);
      });

      it('last delete button click should delete instance of Ticket', () => {
        cy.intercept('GET', '/services/ticket/api/tickets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ticket').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketPageUrlPattern);

        ticket = undefined;
      });
    });
  });

  describe('new Ticket page', () => {
    beforeEach(() => {
      cy.visit(`${ticketPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ticket');
    });

    it('should create an instance of Ticket', () => {
      cy.get(`[data-cy="username"]`).type('Stand-alone Metal Bedfordshire').should('have.value', 'Stand-alone Metal Bedfordshire');

      cy.get(`[data-cy="userFirstName"]`).type('Representative').should('have.value', 'Representative');

      cy.get(`[data-cy="userLastName"]`).type('Licensed Wooden Security').should('have.value', 'Licensed Wooden Security');

      cy.get(`[data-cy="userDisplayName"]`).type('generate project users').should('have.value', 'generate project users');

      cy.get(`[data-cy="title"]`).type('lime').should('have.value', 'lime');

      cy.get(`[data-cy="content"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="typeKey"]`).type('bluetooth Computer').should('have.value', 'bluetooth Computer');

      cy.get(`[data-cy="workflowStatusKey"]`).type('invoice Multi-channelled').should('have.value', 'invoice Multi-channelled');

      cy.get(`[data-cy="priorityLevel"]`).type('27988').should('have.value', '27988');

      cy.get(`[data-cy="tags"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="totalComments"]`).type('38440').should('have.value', '38440');

      cy.get(`[data-cy="uuid"]`)
        .type('fce308d4-2b65-48f5-8a37-96c340da4681')
        .invoke('val')
        .should('match', new RegExp('fce308d4-2b65-48f5-8a37-96c340da4681'));

      cy.get(`[data-cy="created"]`).type('2023-02-01T11:38').blur().should('have.value', '2023-02-01T11:38');

      cy.get(`[data-cy="modified"]`).type('2023-02-01T01:20').blur().should('have.value', '2023-02-01T01:20');

      cy.get(`[data-cy="updated"]`).type('2023-01-31T17:38').blur().should('have.value', '2023-01-31T17:38');

      cy.get(`[data-cy="closed"]`).type('2023-02-01T04:25').blur().should('have.value', '2023-02-01T04:25');

      cy.get(`[data-cy="archived"]`).type('2023-02-01T07:08').blur().should('have.value', '2023-02-01T07:08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ticket = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ticketPageUrlPattern);
    });
  });
});
