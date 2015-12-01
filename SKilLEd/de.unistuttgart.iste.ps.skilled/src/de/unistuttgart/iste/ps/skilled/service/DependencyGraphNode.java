package de.unistuttgart.iste.ps.skilled.service;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;

import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.sKilL.Include;
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile;


/**
 * Graphnode class for the dependency graph. It holds the information about the included files and their URIs.
 * 
 * @author Marco Link
 *
 */
public class DependencyGraphNode {
    private File file;
    private URI fileURI;
    private Set<DependencyGraphNode> directIncludes;
    private Set<DependencyGraphNode> indirectIncludes;
    private Set<DependencyGraphNode> allIncludes;

    public DependencyGraphNode(File file, URI fileURI) {
        this.file = file;
        this.fileURI = fileURI;
        resetIncludes();
    }

    protected void resetIncludes() {
        directIncludes = new HashSet<DependencyGraphNode>();
        indirectIncludes = new HashSet<DependencyGraphNode>();
        allIncludes = new HashSet<DependencyGraphNode>();
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

    protected Set<DependencyGraphNode> getDirectIncludes() {
        return directIncludes;
    }

    protected Set<DependencyGraphNode> getIndirectIncludes() {
        return indirectIncludes;
    }

    protected Set<DependencyGraphNode> getAllIncludes() {
        return allIncludes;
    }

    protected Set<DependencyGraphNode> getDirectAndIndirectIncludes() {
        Set<DependencyGraphNode> directAndIndirectIncludes = new HashSet<DependencyGraphNode>();
        directAndIndirectIncludes.addAll(directIncludes);
        directAndIndirectIncludes.addAll(indirectIncludes);
        return directAndIndirectIncludes;
    }

    protected boolean addToDirectIncludes(DependencyGraphNode directIncludedNode) {
        return directIncludes.add(directIncludedNode);
    }

    protected boolean addToIndirectIncludes(DependencyGraphNode indirectIncludedNode) {
        return indirectIncludes.add(indirectIncludedNode);
    }

    protected boolean addToAllIncludes(DependencyGraphNode includedNode) {
        return allIncludes.add(includedNode);
    }

    protected void setDirectIncludes(Set<DependencyGraphNode> directIncludes) {
        this.directIncludes = directIncludes;
    }

    protected void setIndirectIncludes(Set<DependencyGraphNode> indirectIncludes) {
        this.indirectIncludes = indirectIncludes;
    }

    protected void setAllIncludes(Set<DependencyGraphNode> allIncludes) {
        this.allIncludes = allIncludes;
    }
}
