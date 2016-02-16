package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import java.util.ArrayList
import java.util.HashSet
import java.util.List
import java.util.Set
import java.util.Stack
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar

/**
 * This class contains the Validation rules for Cyclic Types. 
 * 
 * @author Jan Berberich
 * 
 */
class InheritenceValidator extends AbstractDeclarativeValidator {

	public static val CYCLIC_TYPES = 'cyclicTypes'
	public static val TYPE_IS_HIS_OWN_PARENT = 'cycleError'
	public static val MULTIPLE_INHERITENCE = 'inheritenceError'
	public static val MULTIPLE_INHERITENCE_ERROR = "multipleInheritence2"
	public var index = 0
	private val Set<CyclicTypesNode> edges = new HashSet
	private val Stack<CyclicTypesNode> nodes_stack = new Stack
	private val Set<String> declarationsVisited = new HashSet
	public var CyclicTypesNode firstnode;
	public var boolean cyclic;

	override register(EValidatorRegistrar registar) {}

	@Check
	def searchCyclicType(TypeDeclaration dec) {
		// Index counter for Tarjan's strongly connected components algorithm
		index = 0
		edges.clear
		nodes_stack.clear
		declarationsVisited.clear
		// If there is a Cycle that includes dec, cyclic will be set and MultipleInheritence will not be validated for dec.		
		cyclic = false
		// Creates the Graph
		firstnode = new CyclicTypesNode(dec, -1)
		edges.add(firstnode)
		addNode(firstnode)

		// Apply Tarjan's strongly connected components algorithm
		// For each strongly connected component of the Graph ->CyclicComponent Method
		for (node : edges) {
			if (node.getindex == -1) {
				strongconnect(node)
			}
		}

		// Test if Node is his own parent	
		if (firstnode.getsuccessors.contains(firstnode)) {
			error("Error: type can't be his own parent.", firstnode.typeDeclaration,
				SKilLPackage.Literals.DECLARATION__NAME, TYPE_IS_HIS_OWN_PARENT, firstnode.typeDeclaration.name)
			cyclic = true;
		}
		if (!cyclic) {
			multipleInheritence(firstnode.typeDeclaration)
		}
	}

	/**
	 * Validates Multiple Inheritence for TypeDeclaration dec
	 * 
	 */
	def void multipleInheritence(TypeDeclaration dec) {
		var boolean error = false // Becomes true if an multipleInheritence Error is found for dec
		if (dec.supertypes.size > 1) {
			var Set<TypeDeclaration> inheritedNoninterfaceSupertypes = new HashSet
			for (declarationReference : dec.supertypes) {
				if (declarationReference.type instanceof Interfacetype) {
					inheritedNoninterfaceSupertypes.addAll(numberOfSupertypes(declarationReference.type))
				} else {
					inheritedNoninterfaceSupertypes.add(declarationReference.type)
				}
			}
			if (inheritedNoninterfaceSupertypes.size > 1) {
				// There are more than 1 non-Interface Supertypes for firstnode -> Check if they have a minimum
				var boolean minimum = false // Will become true if there is a minimum, else there is an error
				for (TypeDeclaration declaration : inheritedNoninterfaceSupertypes) {
					if (checkMinimum(declaration, inheritedNoninterfaceSupertypes)) {
						minimum = true
					}
				}
				if (!minimum) {
					// No minimum found -> Error
					error("Error: Multiple Inheritence is not allowed.", firstnode.typeDeclaration,
						SKilLPackage.Literals.DECLARATION__NAME, MULTIPLE_INHERITENCE, firstnode.typeDeclaration.name)
					error = true;
				}
			}

		}

		// temporary validation if there are 2 or more non-interface Supertypes if no error was found
		if (!error) {
			checkMultipleInheritence(dec)
		}
	}

	def checkMultipleInheritence(TypeDeclaration dec) {
		if (!checkSupertypes(dec)) {
			// More than one non-interface Supertype
			error("Error: Inheritence error.", dec, SKilLPackage.Literals.DECLARATION__NAME, MULTIPLE_INHERITENCE_ERROR,
				dec.name)
		}
	}

	/**
	 * 
	 * @param dec The Declaration to check.
	 * @return False if inheritence error exists; else true.
	 * 
	 */
	def boolean checkSupertypes(TypeDeclaration declaration) {
		var Set<TypeDeclaration> interfaces = new HashSet
		var Set<TypeDeclaration> types = new HashSet
		for (typeDeclarationReference : declaration.supertypes) {
			if (typeDeclarationReference.type instanceof Interfacetype) {
				interfaces.add(typeDeclarationReference.type)
			} else {
				types.add(typeDeclarationReference.type)
			}
		}

		// More than one non-interface supertype=> error
		return !(types.size > 1)
	}

	/**
	 * Checks if dec is a Minimum for the Nodes in declarations
	 */
	def boolean checkMinimum(TypeDeclaration declaration, Set<TypeDeclaration> declarations) {
		for (TypeDeclaration type : declarations) {
			if (!type.name.equals(declaration.name) &&
				!searchSupertype(type.name, declaration, new HashSet<TypeDeclaration>)) {
				return false // type is no Supertype for dec -> dec is not a minimum for the Types in declarations
			}

		}
		return true;
	}

	/**
	 *  Checks if there is a supertype with the name name that can be reached starting with dec
	 * 	visited contains all visited supertypes to avoid cycles
	 */
	def boolean searchSupertype(String name, TypeDeclaration declaration, Set<TypeDeclaration> visited) {
		var boolean found = false
		for (declarationReference : declaration.supertypes) {
			if (!visited.contains(declarationReference.type)) {
				visited.add(declarationReference.type)
				if (declarationReference.type.name.equals(name)) {
					return true // Found Supertype
				} else {
					if (!found) {
						found = searchSupertype(name, declarationReference.type, visited) // Search the Supertypes of d for name
					}
				}
			}
		}
		return found
	}

	/**
	 * This Method returns all non-interface supertypes of the declaration and its supertypes.
	 * 
	 */
	def List<TypeDeclaration> numberOfSupertypes(TypeDeclaration declaration) {
		val List<TypeDeclaration> noninterfaceSupertypesOfDeclaration = new ArrayList
		if (declarationsVisited.contains(declaration.name)) {
			return noninterfaceSupertypesOfDeclaration // Cycle
		}
		declarationsVisited.add(declaration.name)
		for (declarationReference : declaration.supertypes) {
			if (declarationReference.type instanceof Interfacetype) {
				noninterfaceSupertypesOfDeclaration.addAll(numberOfSupertypes(declarationReference.type))
			} else {
				noninterfaceSupertypesOfDeclaration.add(declarationReference.type)
			}
		}
		return noninterfaceSupertypesOfDeclaration
	}

	/**
	 * The strongconnect method from Tarjan's strongly connected components algorithm
	 * 
	 */
	def void strongconnect(CyclicTypesNode node) {
		node.setindex(index)
		node.setlowlink(index)
		index++
		nodes_stack.push(node)
		node.setonstack(true)
		for (CyclicTypesNode successor : node.getsuccessors) {
			if (successor.getindex() == -1) {
				strongconnect(successor)
				if (successor.getlowlink() < node.getlowlink()) {
					node.setlowlink(successor.getlowlink())
				}
			} else if (successor.getonstack && successor.getindex() < node.getlowlink()) {
				node.setlowlink(successor.getindex())
			}
		}
		if (node.getlowlink() == node.getindex()) {
			// New Cyclic component
			val Set<CyclicTypesNode> cyclic = new HashSet
			var CyclicTypesNode nextnode = nodes_stack.pop
			nextnode.setonstack(false)
			while ((nextnode != null) && (!nextnode.equals(node))) {
				cyclic.add(nextnode)
				nextnode = nodes_stack.pop;
				nextnode.setonstack(false)
			}
			if (nextnode.equals(node)) {
				cyclic.add(nextnode)
			}
			connectedcomponent(cyclic)
		}

	}

	/**
	 * Gets a new Node that was added and adds all supertypes.
	 * The Node should already be in the Nodeset.
	 * 
	 */
	def void addNode(CyclicTypesNode addednode) {
		var boolean tested = false // Will be set as true if the node is found in the Set
		// Checks if there is already a Node for the Supertype.
		for (TypeDeclarationReference ref : addednode.typeDeclaration.supertypes) {
			val String name = ref.type.name; // Name of the Declaration
			for (node : edges) {
				// node is the node for the DeclarationReference ref
				if (node.typeDeclaration.name.equals(name)) {
					tested = true;
					addednode.addsuccessor(node);
				}
			}
			// Add a new Node if the Node was not found in the NodeSet.
			if (tested == false) {
				var CyclicTypesNode new_node = new CyclicTypesNode(ref.type, -1)
				edges.add(new_node)
				addednode.addsuccessor(new_node)
				addNode(new_node)
			}
		}
	}

	/**
	 * Gets the strongly connected component and creates the error message if there is an error.
	 *  
	 */
	def connectedcomponent(Set<CyclicTypesNode> nodes) {
		// If the strongly connected component has more than 1 Element, there is a Cycle.
		// For every Node that is not part of a Cycle, there is a component with only this Node as Element.
		if (nodes.size > 1) {
			// If firstnode is in the Cycle -> Error Message at this Place
			if (nodes.contains(firstnode)) {
				// Create Array with the names of the Types of the component for the Quickfix
				var int counter = 0
				var String[] array_nodes = newArrayOfSize(nodes.size)
				for (node : nodes) {
					array_nodes.set(counter, node.typeDeclaration.name)
					counter++
				}
				// Error Message
				error("Error: can't use extend in a Cycle", firstnode.typeDeclaration,
					SKilLPackage.Literals.DECLARATION__NAME, CYCLIC_TYPES, array_nodes)
				cyclic = true
			}
		}
	}

}
