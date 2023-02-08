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

describe('TicketAssignment e2e test', () => {
  const ticketAssignmentPageUrl = '/ticket/ticket-assignment';
  const ticketAssignmentPageUrlPattern = new RegExp('/ticket/ticket-assignment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ticketAssignmentSample = {
    issueId: 57742,
    issueUuid: 'b65891d7-d714-4865-84e9-7b1ddb0a1a07',
    username: 'withdrawal deploy value-added',
    roleKey: 'Rubber Account Tuna',
    roleWeight: 5975,
    assignedByUsername: 'alarm withdrawal 1080p',
    departmentKey: 'extensible hierarchy',
    departmentWeight: 73022,
    created: '2023-02-07T15:25:38.322Z',
    modified: '2023-02-07T04:59:22.070Z',
  };

  let ticketAssignment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/ticket/api/ticket-assignments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/ticket/api/ticket-assignments').as('postEntityRequest');
    cy.intercept('DELETE', '/services/ticket/api/ticket-assignments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ticketAssignment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/ticket/api/ticket-assignments/${ticketAssignment.id}`,
      }).then(() => {
        ticketAssignment = undefined;
      });
    }
  });

  it('TicketAssignments menu should load TicketAssignments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ticket/ticket-assignment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TicketAssignment').should('exist');
    cy.url().should('match', ticketAssignmentPageUrlPattern);
  });

  describe('TicketAssignment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ticketAssignmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TicketAssignment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ticket/ticket-assignment/new$'));
        cy.getEntityCreateUpdateHeading('TicketAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketAssignmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/ticket/api/ticket-assignments',
          body: ticketAssignmentSample,
        }).then(({ body }) => {
          ticketAssignment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/ticket/api/ticket-assignments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ticketAssignment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ticketAssignmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TicketAssignment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ticketAssignment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketAssignmentPageUrlPattern);
      });

      it('edit button click should load edit TicketAssignment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TicketAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketAssignmentPageUrlPattern);
      });

      it('edit button click should load edit TicketAssignment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TicketAssignment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketAssignmentPageUrlPattern);
      });

      it('last delete button click should delete instance of TicketAssignment', () => {
        cy.intercept('GET', '/services/ticket/api/ticket-assignments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ticketAssignment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ticketAssignmentPageUrlPattern);

        ticketAssignment = undefined;
      });
    });
  });

  describe('new TicketAssignment page', () => {
    beforeEach(() => {
      cy.visit(`${ticketAssignmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TicketAssignment');
    });

    it('should create an instance of TicketAssignment', () => {
      cy.get(`[data-cy="issueId"]`).type('963').should('have.value', '963');

      cy.get(`[data-cy="issueUuid"]`)
        .type('306aa794-0c2e-4aea-95b5-a9806e24395c')
        .invoke('val')
        .should('match', new RegExp('306aa794-0c2e-4aea-95b5-a9806e24395c'));

      cy.get(`[data-cy="username"]`).type('Product').should('have.value', 'Product');

      cy.get(`[data-cy="roleKey"]`).type('client-driven system').should('have.value', 'client-driven system');

      cy.get(`[data-cy="roleWeight"]`).type('65163').should('have.value', '65163');

      cy.get(`[data-cy="assignedByUsername"]`).type('distributed').should('have.value', 'distributed');

      cy.get(`[data-cy="departmentKey"]`).type('Granite 1080p Designer').should('have.value', 'Granite 1080p Designer');

      cy.get(`[data-cy="departmentWeight"]`).type('27570').should('have.value', '27570');

      cy.get(`[data-cy="created"]`).type('2023-02-07T05:51').blur().should('have.value', '2023-02-07T05:51');

      cy.get(`[data-cy="modified"]`).type('2023-02-07T12:27').blur().should('have.value', '2023-02-07T12:27');

      cy.get(`[data-cy="accepted"]`).type('2023-02-07T04:23').blur().should('have.value', '2023-02-07T04:23');

      cy.get(`[data-cy="left"]`).type('2023-02-07T00:25').blur().should('have.value', '2023-02-07T00:25');

      cy.get(`[data-cy="closed"]`).type('2023-02-07T02:43').blur().should('have.value', '2023-02-07T02:43');

      cy.get(`[data-cy="archived"]`).type('2023-02-07T06:10').blur().should('have.value', '2023-02-07T06:10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ticketAssignment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ticketAssignmentPageUrlPattern);
    });
  });
});
