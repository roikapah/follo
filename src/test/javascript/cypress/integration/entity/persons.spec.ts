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

describe('Persons e2e test', () => {
  const personsPageUrl = '/persons';
  const personsPageUrlPattern = new RegExp('/persons(\\?.*)?$');
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
    cy.intercept('GET', '/api/persons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/persons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/persons/*').as('deleteEntityRequest');
  });

  it('should load Persons', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('persons');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Persons').should('exist');
    cy.url().should('match', personsPageUrlPattern);
  });

  it('should load details Persons page', function () {
    cy.visit(personsPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('persons');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', personsPageUrlPattern);
  });

  it('should load create Persons page', () => {
    cy.visit(personsPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Persons');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', personsPageUrlPattern);
  });

  it('should load edit Persons page', function () {
    cy.visit(personsPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Persons');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', personsPageUrlPattern);
  });

  it('should create an instance of Persons', () => {
    cy.visit(personsPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Persons');

    cy.get(`[data-cy="name"]`).type('enterprise card Buckinghamshire').should('have.value', 'enterprise card Buckinghamshire');

    cy.get(`[data-cy="email"]`).type('Lucious.Erdman@hotmail.com').should('have.value', 'Lucious.Erdman@hotmail.com');

    cy.get(`[data-cy="role"]`).select('ADMIN');

    cy.get(`[data-cy="phoneNumber"]`).type('input').should('have.value', 'input');

    cy.get(`[data-cy="address"]`).type('Associate').should('have.value', 'Associate');

    cy.get(`[data-cy="createOn"]`).type('2021-08-16T11:37').should('have.value', '2021-08-16T11:37');

    cy.get(`[data-cy="updatedOn"]`).type('2021-08-16T15:26').should('have.value', '2021-08-16T15:26');

    cy.setFieldSelectToLastOfEntity('department');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', personsPageUrlPattern);
  });

  it('should delete last instance of Persons', function () {
    cy.intercept('GET', '/api/persons/*').as('dialogDeleteRequest');
    cy.visit(personsPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('persons').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personsPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
