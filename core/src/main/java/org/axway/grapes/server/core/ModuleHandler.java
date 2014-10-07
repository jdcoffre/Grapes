package org.axway.grapes.server.core;

import org.axway.grapes.server.core.exceptions.GrapesException;
import org.axway.grapes.server.core.options.FiltersHolder;
import org.axway.grapes.server.db.DataUtils;
import org.axway.grapes.server.db.RepositoryHandler;
import org.axway.grapes.server.db.datamodel.DbArtifact;
import org.axway.grapes.server.db.datamodel.DbLicense;
import org.axway.grapes.server.db.datamodel.DbModule;
import org.axway.grapes.server.db.datamodel.DbOrganization;

import java.util.ArrayList;
import java.util.List;

/**
 * Module Handler
 *
 * <p>Manages all operation regarding Modules. It can, get/update Modules of the database.</p>
 *
 * @author jdcoffre
 */
public class ModuleHandler {

    private final RepositoryHandler repositoryHandler;

    public ModuleHandler(final RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    /**
     * Add/update module in the database
     *
     * @param dbModule DbModule
     */
    public void store(final DbModule dbModule){
        repositoryHandler.store(dbModule);
    }

    /**
     * Returns the available module names regarding the filters
     *
     * @param filters FiltersHolder
     * @return List<String>
     */
    public List<String> getModuleNames(final FiltersHolder filters) {
        return repositoryHandler.getModuleNames(filters);
    }

    /**
     * Returns the available module names regarding the filters
     *
     * @param name String
     * @param filters FiltersHolder
     * @return List<String>
     */
    public List<String> getModuleVersions(final String name, final FiltersHolder filters) throws GrapesException {
        final List<String> versions = repositoryHandler.getModuleVersions(name, filters);

        if(versions.isEmpty()){
            throw new GrapesException("Module " + name + " does not exist.");
        }

        return versions;
    }

    /**
     * Returns a module
     *
     * @param moduleId String
     * @return DbModule
     */
    public DbModule getModule(final String moduleId) throws GrapesException {
        final DbModule dbModule = repositoryHandler.getModule(moduleId);

        if(dbModule == null){
            throw new GrapesException("Module " + moduleId + " does not exist.");
        }

        return dbModule;
    }

    /**
     * Delete a module
     *
     * @param moduleId String
     */
    public void deleteModule(final String moduleId) throws GrapesException {
        final DbModule module = getModule(moduleId);
        repositoryHandler.deleteModule(module.getId());

        for(String gavc: DataUtils.getAllArtifacts(module)){
            repositoryHandler.deleteArtifact(gavc);
        }
    }

    /**
     * Return a licenses view of the targeted module
     *
     * @param moduleId String
     * @return List<DbLicense>
     */
    public List<DbLicense> getModuleLicenses(final String moduleId) throws GrapesException {
        final DbModule module = getModule(moduleId);

        final List<DbLicense> licenses = new ArrayList<DbLicense>();
        final FiltersHolder filters = new FiltersHolder();
        final ArtifactHandler artifactHandler = new ArtifactHandler(repositoryHandler);

        for(String gavc: DataUtils.getAllArtifacts(module)){
            licenses.addAll(artifactHandler.getArtifactLicenses(gavc, filters));
        }

        return licenses;
    }

    /**
     * Perform the module promotion
     *
     * @param moduleId String
     */
    public void promoteModule(final String moduleId) throws GrapesException {
        final DbModule module = getModule(moduleId);

        for(String gavc: DataUtils.getAllArtifacts(module)){
            final DbArtifact artifact = repositoryHandler.getArtifact(gavc);
            artifact.setPromoted(true);
            repositoryHandler.store(artifact);
        }

        repositoryHandler.promoteModule(module);
    }

    public DbOrganization getOrganization(final DbModule module) throws GrapesException {
        if(module.getOrganization() == null ||
                module.getOrganization().isEmpty()){
            final DbOrganization organization = new DbOrganization();
            organization.setName("No organization registered");
            return organization;
        }

        final OrganizationHandler handler = new OrganizationHandler(repositoryHandler);
        return handler.getOrganization(module.getOrganization());
    }

    /**
     * Provides a list of module regarding the filters
     *
     * @param filters FiltersHolder
     * @return List<DbModule>
     */
    public List<DbModule> getModules(final FiltersHolder filters) {
        return repositoryHandler.getModules(filters);
    }

}
