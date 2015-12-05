package de.unistuttgart.iste.ps.skilled.service;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;

import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.sKilL.Include;
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile;
import de.unistuttgart.iste.ps.skilled.service.Tarjan.StronglyConnectedComponent;
import de.unistuttgart.iste.ps.skilled.service.Tarjan.Vertex;


/**
 * Graphnode class for the dependency graph.
 * 
 * @author Marco Link
 *
 */
public class DependencyGraphNode extends Vertex {
    private File file;
    private URI fileURI;

    public DependencyGraphNode(File file) {
        super(file.eResource().getURI().path());
        this.file = file;
        this.fileURI = file.eResource().getURI();
    }

    public Set<StronglyConnectedComponent> getReferencedComponent() {
        Set<StronglyConnectedComponent> components = new HashSet<StronglyConnectedComponent>();
        for (Vertex v : getEdges()) {
            if (v.getRootContainer() != null) {
                components.add(v.getRootContainer());
            }
        }
        return components;
    }

    public Set<URI> getIncludedURIs() {
        Set<URI> uRIs = new HashSet<URI>();
        Set<String> includeStrings = new HashSet<String>();
        for (Include include : file.getIncludes()) {
            for (IncludeFile includeFile : include.getIncludeFiles()) {
                includeStrings.add(includeFile.getImportURI());
            }
        }
        for (String includeString : includeStrings) {
            uRIs.add(URI.createURI(includeString).resolve(getFileURI()));
        }
        return uRIs;
    }

    protected File getFile() {
        return file;
    }

    protected URI getFileURI() {
        return fileURI;
    }
}
