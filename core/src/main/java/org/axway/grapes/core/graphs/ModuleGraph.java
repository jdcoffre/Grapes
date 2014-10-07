package org.axway.grapes.core.graphs;

import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.core.db.datamodel.DbArtifact;
import org.axway.grapes.core.db.datamodel.DbModule;

public class ModuleGraph extends AbstractGraph {

	@Override
	public String getId(final DbModule module) {
		return module.getName();
	}

	@Override
	public String getId(final DbArtifact artifact) {
		return artifact.getArtifactId();
	}

	@Override
	public String getId(final Artifact artifact) {
		return artifact.getArtifactId();
	}

}
