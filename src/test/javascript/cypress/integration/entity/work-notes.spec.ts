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

describe('WorkNotes e2e test', () => {
  const workNotesPageUrl = '/work-notes';
  const workNotesPageUrlPattern = new RegExp('/work-notes(\\?.*)?$');
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
    cy.intercept('GET', '/api/work-notes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/work-notes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/work-notes/*').as('deleteEntityRequest');
  });

  it('should load WorkNotes', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('work-notes');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkNotes').should('exist');
    cy.url().should('match', workNotesPageUrlPattern);
  });

  it('should load details WorkNotes page', function () {
    cy.visit(workNotesPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('workNotes');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workNotesPageUrlPattern);
  });

  it('should load create WorkNotes page', () => {
    cy.visit(workNotesPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('WorkNotes');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workNotesPageUrlPattern);
  });

  it('should load edit WorkNotes page', function () {
    cy.visit(workNotesPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('WorkNotes');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workNotesPageUrlPattern);
  });

  it('should create an instance of WorkNotes', () => {
    cy.visit(workNotesPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('WorkNotes');

    cy.get(`[data-cy="text"]`).type('Pennsylvania').should('have.value', 'Pennsylvania');

    cy.get(`[data-cy="createOn"]`).type('2021-08-16T02:13').should('have.value', '2021-08-16T02:13');

    cy.setFieldSelectToLastOfEntity('createBy');

    cy.setFieldSelectToLastOfEntity('task');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workNotesPageUrlPattern);
  });

  it('should delete last instance of WorkNotes', function () {
    cy.intercept('GET', '/api/work-notes/*').as('dialogDeleteRequest');
    cy.visit(workNotesPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('workNotes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', workNotesPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
