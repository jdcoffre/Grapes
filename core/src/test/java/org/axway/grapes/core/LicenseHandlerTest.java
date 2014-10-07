package org.axway.grapes.core;


import org.axway.grapes.core.db.RepositoryHandler;
import org.axway.grapes.core.db.datamodel.DbArtifact;
import org.axway.grapes.core.db.datamodel.DbLicense;
import org.axway.grapes.core.exceptions.GrapesException;
import org.axway.grapes.core.options.FiltersHolder;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class LicenseHandlerTest {

    @Test
    public void checkStoreLicense(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);

        final DbLicense dbLicense = new DbLicense();
        handler.store(dbLicense);

        verify(repositoryHandler, times(1)).store(dbLicense);
    }

    @Test
    public void getLicenseNames(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);
        final FiltersHolder filters = mock(FiltersHolder.class);

        handler.getLicensesNames(filters);

        verify(repositoryHandler, times(1)).getLicenseNames(filters);
    }

    @Test
    public void getAnExistingLicense() throws GrapesException {
        final DbLicense license = new DbLicense();
        license.setName("test");
        license.setLongName("Grapes Test License");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        when(repositoryHandler.getLicense(license.getName())).thenReturn(license);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);

        final DbLicense gotLicense = handler.getLicense(license.getName());

        assertNotNull(gotLicense);
        assertEquals(license, gotLicense);
        verify(repositoryHandler, times(1)).getLicense(license.getName());
    }

    @Test
    public void getALicenseThatDoesNotExist(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);
        GrapesException exception = null;

        try {
            handler.getLicense("doesNotExist");
        }catch (GrapesException e){
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    public void deleteAnExistingLicense() throws GrapesException {
        final DbLicense license = new DbLicense();
        license.setName("test");
        license.setLongName("Grapes Test License");

        final DbArtifact artifact = new DbArtifact();
        artifact.setGroupId("org.axway.grapes.test");
        artifact.setArtifactId("tested");
        artifact.setVersion("1.5.9");
        artifact.addLicense(license.getName());

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        when(repositoryHandler.getLicense(license.getName())).thenReturn(license);
        when(repositoryHandler.getArtifacts(any(FiltersHolder.class))).thenReturn(Collections.singletonList(artifact));
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);

        handler.deleteLicense(license.getName());

        verify(repositoryHandler, times(1)).deleteLicense(license.getName());
        verify(repositoryHandler, times(1)).removeLicenseFromArtifact(artifact, license.getName());
    }

    @Test
    public void deleteAnArtifactThatDoesNotExist(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);
        GrapesException exception = null;

        try {
            handler.deleteLicense("doesNotExist");
        }catch (GrapesException e){
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    public void approveALicense() throws GrapesException {
        final DbLicense license = new DbLicense();
        license.setName("test");
        license.setLongName("Grapes Test License");

        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        when(repositoryHandler.getLicense(license.getName())).thenReturn(license);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);

        handler.approveLicense(license.getName(), true);
        verify(repositoryHandler, times(1)).approveLicense(license, true);

        handler.approveLicense(license.getName(), false);
        verify(repositoryHandler, times(1)).approveLicense(license, false);
    }

    @Test
    public void approveALicenseThatDoesNotExist(){
        final RepositoryHandler repositoryHandler = mock(RepositoryHandler.class);
        final LicenseHandler handler = new LicenseHandler(repositoryHandler);
        GrapesException exception = null;

        try {
            handler.approveLicense("doesNotExist", true);
        }catch (GrapesException e){
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    public void resolveLicense(){
        final DbLicense license = new DbLicense();
        license.setName("Test");
        license.setRegexp("\\w*");

        final RepositoryHandler repoHandler = mock(RepositoryHandler.class);
        when(repoHandler.getAllLicenses()).thenReturn(Collections.singletonList(license));

        final LicenseHandler licenseHandler = new LicenseHandler(repoHandler);

        assertEquals(license, licenseHandler.resolve(license.getName()));
    }

    @Test
    public void ifLicenseDoesNotHaveRegexpItUsesLicenseName(){
        final DbLicense license = new DbLicense();
        license.setName("Test");

        final RepositoryHandler repoHandler = mock(RepositoryHandler.class);
        when(repoHandler.getAllLicenses()).thenReturn(Collections.singletonList(license));

        final LicenseHandler licenseHandler = new LicenseHandler(repoHandler);

        assertEquals(license, licenseHandler.resolve(license.getName()));
        assertEquals(null, licenseHandler.resolve("Test2"));
    }

    @Test
    public void doesNotFailEvenWithWrongPattern(){
        final DbLicense license = new DbLicense();
        license.setName("Test");
        license.setRegexp("x^[");

        final RepositoryHandler repoHandler = mock(RepositoryHandler.class);
        when(repoHandler.getAllLicenses()).thenReturn(Collections.singletonList(license));

        final LicenseHandler licenseHandler = new LicenseHandler(repoHandler);

        Exception exception = null;
        DbLicense resolvedLicense = null;

        try{
            resolvedLicense = licenseHandler.resolve(license.getName());
        }
        catch (Exception e){
            exception = e;
        }

        assertEquals(null, exception);
        assertEquals(null, resolvedLicense);
    }

    @Test
    public void getAllTheAvailableLicenses(){
        final DbLicense license = new DbLicense();
        license.setName("Test");

        final RepositoryHandler repoHandler = mock(RepositoryHandler.class);
        when(repoHandler.getAllLicenses()).thenReturn(Collections.singletonList(license));

        final LicenseHandler licenseHandler = new LicenseHandler(repoHandler);

        assertEquals(1, licenseHandler.getLicenses().size());
    }
}