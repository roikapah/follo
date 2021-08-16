import { entityItemSelector } from '../../support/commands';
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

describe('Task e2e test', () => {
  const taskPageUrl = '/task';
  const taskPageUrlPattern = new RegExp('/task(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tasks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tasks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tasks/*').as('deleteEntityRequest');
  });

  it('should load Tasks', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('task');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Task').should('exist');
    cy.url().should('match', taskPageUrlPattern);
  });

  it('should load details Task page', function () {
    cy.visit(taskPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('task');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', taskPageUrlPattern);
  });

  it('should load create Task page', () => {
    cy.visit(taskPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Task');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', taskPageUrlPattern);
  });

  it('should load edit Task page', function () {
    cy.visit(taskPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Task');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', taskPageUrlPattern);
  });

  it('should create an instance of Task', () => {
    cy.visit(taskPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Task');

    cy.get(`[data-cy="description"]`).type('expedite').should('have.value', 'expedite');

    cy.get(`[data-cy="dueDate"]`).type('2021-08-15T22:55').should('have.value', '2021-08-15T22:55');

    cy.get(`[data-cy="estimatedTimeToComplete"]`).type('58274').should('have.value', '58274');

    cy.get(`[data-cy="estimatedTimeToCompleteTimeUnit"]`).type('COM').should('have.value', 'COM');

    cy.get(`[data-cy="isReadByAssignTo"]`).should('not.be.checked');
    cy.get(`[data-cy="isReadByAssignTo"]`).click().should('be.checked');

    cy.get(`[data-cy="isUrgent"]`).should('not.be.checked');
    cy.get(`[data-cy="isUrgent"]`).click().should('be.checked');

    cy.get(`[data-cy="isRejected"]`).should('not.be.checked');
    cy.get(`[data-cy="isRejected"]`).click().should('be.checked');

    cy.get(`[data-cy="isCompleted"]`).should('not.be.checked');
    cy.get(`[data-cy="isCompleted"]`).click().should('be.checked');

    cy.get(`[data-cy="completedOn"]`).type('2021-08-15T22:44').should('have.value', '2021-08-15T22:44');

    cy.get(`[data-cy="rejectedOn"]`).type('2021-08-16T18:21').should('have.value', '2021-08-16T18:21');

    cy.get(`[data-cy="createOn"]`).type('2021-08-16T04:04').should('have.value', '2021-08-16T04:04');

    cy.get(`[data-cy="updatedOn"]`).type('2021-08-16T13:29').should('have.value', '2021-08-16T13:29');

    cy.setFieldSelectToLastOfEntity('assignTo');

    cy.setFieldSelectToLastOfEntity('department');

    cy.setFieldSelectToLastOfEntity('area');

    cy.setFieldSelectToLastOfEntity('type');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', taskPageUrlPattern);
  });

  it('should delete last instance of Task', function () {
    cy.intercept('GET', '/api/tasks/*').as('dialogDeleteRequest');
    cy.visit(taskPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('task').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taskPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
