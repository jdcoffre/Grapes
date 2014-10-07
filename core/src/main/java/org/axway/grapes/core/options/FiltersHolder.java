package org.axway.grapes.core.options;


import org.axway.grapes.commons.api.ServerAPI;
import org.axway.grapes.core.db.datamodel.DbDependency;
import org.axway.grapes.core.db.datamodel.DbLicense;
import org.axway.grapes.core.options.filters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  Holds the filters.
 *
 * @author jdcoffre
 */
public class FiltersHolder {

    private final List<Filter> filters = new ArrayList<Filter>();

    private final ScopeHandler scopeHandler = new ScopeHandler();
    private final Decorator decorator = new Decorator();
    private final DepthHandler depthHandler = new DepthHandler();
    private CorporateFilter corporateFilter;

    public ScopeHandler getScopeHandler(){
        return scopeHandler;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public DepthHandler getDepthHandler() {
        return depthHandler;
    }

    public CorporateFilter getCorporateFilter() {
        return corporateFilter;
    }

    public void setCorporateFilter(final CorporateFilter filter) {
        corporateFilter = filter;
    }

    public void addFilter(final Filter newFilter) {
        Filter toRemove = null;

        for(Filter filter: filters){
            if(filter.getClass().equals(newFilter.getClass())){
                toRemove = filter;
            }
        }

        if(toRemove != null){
            filters.remove(toRemove);
        }

        filters.add(newFilter);
    }

	public void init(final Map<String, String> queryParameters) {
        scopeHandler.init(queryParameters);
        decorator.init(queryParameters);
        depthHandler.init(queryParameters);

        final String approved = queryParameters.get(ServerAPI.APPROVED_PARAM);
        if(approved != null){
            filters.add(new ApprovedFilter(Boolean.valueOf(approved)));
        }

        final String promoted = queryParameters.get(ServerAPI.PROMOTED_PARAM);
        if(promoted != null){
            filters.add(new PromotedFilter(Boolean.valueOf(promoted)));
        }

        final String doNotUse = queryParameters.get(ServerAPI.DO_NOT_USE);
        if(doNotUse != null){
            filters.add(new DoNotUseFilter(Boolean.valueOf(doNotUse)));
        }

        final String gavc = queryParameters.get(ServerAPI.GAVC);
        if(gavc != null){
            filters.add(new GavcFilter(gavc));
        }

        final String hasLicense = queryParameters.get(ServerAPI.HAS_LICENSE_PARAM);
        if(hasLicense != null){
            filters.add(new HasLicenseFilter(Boolean.valueOf(hasLicense)));
        }

        final String toBeValidated = queryParameters.get(ServerAPI.TO_BE_VALIDATED_PARAM);
        if(toBeValidated != null){
            filters.add(new ToBeValidatedFilter(Boolean.valueOf(toBeValidated)));
        }

        final String licenseId = queryParameters.get(ServerAPI.LICENSE_ID_PARAM);
        if(licenseId != null){
            filters.add(new LicenseIdFilter(licenseId));
        }

        final String classifier = queryParameters.get(ServerAPI.CLASSIFIER_PARAM);
        if(classifier != null){
            filters.add(new ClassifierFilter(classifier));
        }

        final String extension = queryParameters.get(ServerAPI.EXTENSION_PARAM);
        if(extension != null){
            filters.add(new ExtensionFilter(extension));
        }

        final String type = queryParameters.get(ServerAPI.TYPE_PARAM);
        if(type != null){
            filters.add(new TypeFilter(type));
        }

        final String version = queryParameters.get(ServerAPI.VERSION_PARAM);
        if(version != null){
            filters.add(new VersionFilter(version));
        }

        final String artifactId = queryParameters.get(ServerAPI.ARTIFACTID_PARAM);
        if(artifactId != null){
            filters.add(new ArtifactIdFilter(artifactId));
        }

        final String groupId = queryParameters.get(ServerAPI.GROUPID_PARAM);
        if(groupId != null){
            filters.add(new GroupIdFilter(groupId));
        }

        final String name = queryParameters.get(ServerAPI.NAME_PARAM);
        if(name != null){
            filters.add(new ModuleNameFilter(name));
        }

        final String organization = queryParameters.get(ServerAPI.ORGANIZATION_PARAM);
        if(organization != null){
            filters.add(new OrganizationFilter(organization));
        }
	}

    public boolean shouldBeInReport(final DbLicense license) {
        for(Filter filter: filters){
            if(!filter.filter(license)){
                return false;
            }
        }

        return true;
    }

    /**
     * Check if a dependency matches the filters
     *
     * @param dependency
     *
     * @return boolean
     */
    public boolean shouldBeInReport(final DbDependency dependency) {
        if(dependency == null){
            return false;
        }
        if(dependency.getTarget() == null){
            return false;
        }
        if(corporateFilter != null){
            if(!decorator.getShowThirdparty() && !corporateFilter.filter(dependency)){
                return false;
            }
            if(!decorator.getShowCorporate() && corporateFilter.filter(dependency)){
                return false;
            }
        }

        if(!scopeHandler.filter(dependency)){
            return false;
        }

        return true;
    }

	/**
	 * Generates a Map of query parameters for Artifact regarding the filters
     *
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getArtifactFieldsFilters() {
		final Map<String, Object> params = new HashMap<String, Object>();

        for(Filter filter: filters){
            params.putAll(filter.artifactFilterFields());
        }

		return params;
	}

    /**
     * Generates a Map of query parameters for Module regarding the filters
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> getModuleFieldsFilters() {
        final Map<String, Object> params = new HashMap<String, Object>();

        for(Filter filter: filters){
            params.putAll(filter.moduleFilterFields());
        }

        return params;
    }
}