package de.unistuttgart.iste.ps.skilled.validation

import java.util.Set
import java.util.HashSet
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration

/**
 * Nodes for the Graph for CyclicTypes Validation
 * @author Jan Berberich
 * 
 */
class CyclicTypesNode {
	private var int index // Index -1 -> undefined
	private var int lowlink
	private var TypeDeclaration type
	private var boolean onstack
	private var Set<CyclicTypesNode> successors

	new(TypeDeclaration type, int i) {
		index = i
		this.type = type
		onstack = false
		successors = new HashSet<CyclicTypesNode>
	}

	def void setonstack(boolean stack) {
		onstack = stack
	}

	def TypeDeclaration getTypeDeclaration() {
		return type
	}

	def int getindex() {
		return index
	}

	def int getlowlink() {
		return lowlink
	}

	def void setindex(int i) {
		index = i
	}

	def void setlowlink(int l) {
		lowlink = l
	}

	def void addsuccessor(CyclicTypesNode node) {
		successors.add(node)
	}

	def Set<CyclicTypesNode> getsuccessors() {
		return successors
	}

	def boolean getonstack() {
		return onstack
	}
}