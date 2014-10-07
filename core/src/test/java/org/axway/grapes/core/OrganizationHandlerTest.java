package org.axway.grapes.core;


import org.axway.grapes.core.db.RepositoryHandler;
import org.axway.grapes.core.db.datamodel.DbArtifact;
import org.axway.grapes.core.db.datamodel.DbModule;
import org.axway.grapes.core.db.datamodel.DbOrganization;
import org.axway.grapes.core.exceptions.GrapesException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrganizationHandlerTest {


    @Test
    public void checkStoreOrganization(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        final DbOrganization dbOrganization = new DbOrganization();
        handler.store(dbOrganization);

        verify(repositoryHandler, times(1)).store(dbOrganization);
    }


    @Test
    public void getAllOrganizationNames(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        handler.getOrganizationNames();

        verify(repositoryHandler, times(1)).getOrganizationNames();
    }

    @Test
    public void getAnExistingOrganization() throws GrapesException {
        final DbOrganization organization = new DbOrganization();
        organization.setName("test");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        when(repositoryHandler.getOrganization(organization.getName())).thenReturn(organization);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        final DbOrganization gotOrganization = handler.getOrganization(organization.getName());

        assertNotNull(gotOrganization);
        assertEquals(organization, gotOrganization);
        verify(repositoryHandler, times(1)).getOrganization(organization.getName());
    }

    @Test
    public void getAnOrganizationThatDoesNotExist(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        GrapesException exception = null;

        try {
            handler.getOrganization("doesNotExist");
        }catch (GrapesException e){
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    public void deleteAnExistingOrganization() throws GrapesException {
        final DbOrganization organization = new DbOrganization();
        organization.setName("test");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        when(repositoryHandler.getOrganization(organization.getName())).thenReturn(organization);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        handler.deleteOrganization(organization.getName());

        verify(repositoryHandler, times(1)).deleteOrganization(organization.getName());
    }

    @Test
    public void deleteAnOrganizationThatDoesNotExist(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        GrapesException exception = null;

        try {
            handler.deleteOrganization("doesNotExist");
        }catch (GrapesException e){
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    public void addCorporateGroupId() throws GrapesException {
        final DbOrganization dbOrganization= new DbOrganization();
        dbOrganization.setName("organization1");
        dbOrganization.getCorporateGroupIdPrefixes().add("org.test");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        ArgumentCaptor<DbOrganization> captor = ArgumentCaptor.forClass(DbOrganization.class);
        when(repositoryHandler.getOrganization(dbOrganization.getName())).thenReturn(dbOrganization);

        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        handler.addCorporateGroupId(dbOrganization.getName(), "com.test");

        verify(repositoryHandler, times(1)).store(captor.capture());
        assertTrue(captor.getValue().getCorporateGroupIdPrefixes().contains("com.test"));

        verify(repositoryHandler, times(1)).addModulesOrganization("com.test", dbOrganization);
    }

    @Test
    public void addCorporateGroupIdThatAlreadyExist() throws GrapesException {
        final DbOrganization dbOrganization= new DbOrganization();
        dbOrganization.setName("organization1");
        dbOrganization.getCorporateGroupIdPrefixes().add("org.test");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        ArgumentCaptor<DbOrganization> captor = ArgumentCaptor.forClass(DbOrganization.class);
        when(repositoryHandler.getOrganization(dbOrganization.getName())).thenReturn(dbOrganization);

        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        handler.addCorporateGroupId(dbOrganization.getName(), "org.test");

        verify(repositoryHandler, never()).store(captor.capture());
    }

    @Test
    public void removeCorporateGroupId() throws GrapesException {
        final DbOrganization dbOrganization= new DbOrganization();
        dbOrganization.setName("organization1");
        dbOrganization.getCorporateGroupIdPrefixes().add("org.test");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        ArgumentCaptor<DbOrganization> captor = ArgumentCaptor.forClass(DbOrganization.class);
        when(repositoryHandler.getOrganization(dbOrganization.getName())).thenReturn(dbOrganization);

        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        handler.removeCorporateGroupId(dbOrganization.getName(), "org.test");

        verify(repositoryHandler, times(1)).store((DbOrganization)captor.capture());
        assertFalse(captor.getValue().getCorporateGroupIdPrefixes().contains("org.test"));

        verify(repositoryHandler, times(1)).removeModulesOrganization("org.test", dbOrganization);
    }

    @Test
    public void removeCorporateGroupIdThatDoesNotExist() throws GrapesException {
        final DbOrganization dbOrganization= new DbOrganization();
        dbOrganization.setName("organization1");
        dbOrganization.getCorporateGroupIdPrefixes().add("org.test");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        ArgumentCaptor<DbOrganization> captor = ArgumentCaptor.forClass(DbOrganization.class);
        when(repositoryHandler.getOrganization(dbOrganization.getName())).thenReturn(dbOrganization);

        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        handler.removeCorporateGroupId(dbOrganization.getName(), "com.test");

        verify(repositoryHandler, never()).store(captor.capture());
    }

    @Test
    public void getMatchingOrganizationOfAModuleWhenIsAlreadyOne() throws GrapesException {
        final DbOrganization organization = new DbOrganization();
        organization.setName("test");

        final DbModule module = new DbModule();
        module.setOrganization(organization.getName());

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        when(repositoryHandler.getOrganization(organization.getName())).thenReturn(organization);
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        final DbOrganization gotOrganization = handler.getMatchingOrganization(module);

        verify(repositoryHandler, times(1)).getOrganization(organization.getName());
        verify(repositoryHandler, never()).getAllOrganizations();
        assertEquals(organization, gotOrganization);
    }

    @Test
    public void getMatchingOrganizationOfAModule() throws GrapesException {
        final DbModule module = new DbModule();
        final DbArtifact artifact = new DbArtifact();
        artifact.setGroupId(GrapesTestUtils.CORPORATE_GROUPID_4TEST);
        module.addArtifact(artifact);

        final RepositoryHandler repositoryHandler = GrapesTestUtils.getRepoHandlerMock();
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        final DbOrganization gotOrganization = handler.getMatchingOrganization(module);

        verify(repositoryHandler, times(1)).getAllOrganizations();
        assertEquals(GrapesTestUtils.ORGANIZATION_NAME_4TEST, gotOrganization.getName());
    }

    @Test
    public void getMatchingOrganizationOfAModuleWhenThereIsNone() throws GrapesException {
        final DbModule module = new DbModule();
        final DbArtifact artifact = new DbArtifact();
        artifact.setGroupId("unknown.production");
        module.addArtifact(artifact);

        final RepositoryHandler repositoryHandler = GrapesTestUtils.getRepoHandlerMock();
        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);

        final DbOrganization organization = handler.getMatchingOrganization(new DbModule());

        assertNull(organization);
    }

}