package org.axway.grapes.core.materials.cases;

import org.axway.grapes.core.db.datamodel.DbArtifact;
import org.axway.grapes.core.db.datamodel.DbLicense;
import org.axway.grapes.core.db.datamodel.DbModule;

import java.util.List;

public interface DependencyCase {

	public List<DbArtifact> dbArtifactsToLoad();
    public List<DbModule> dbModulesToLoad();
    public List<DbLicense> dbLicensesToLoad();
	
}
