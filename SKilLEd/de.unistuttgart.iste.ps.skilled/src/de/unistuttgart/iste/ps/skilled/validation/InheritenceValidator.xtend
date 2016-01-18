package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.Check
import java.util.Set
import java.util.Stack
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import java.util.HashSet
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.EValidatorRegistrar
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype
import java.util.List
import java.util.ArrayList

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
	public var Set<CyclicTypesNode> edges
	public var Stack<CyclicTypesNode> nodes_stack = new Stack()
	public var CyclicTypesNode firstnode;
	public var boolean cyclic;
	public var Set<String> declarationsVisited

	override register(EValidatorRegistrar registar) {}

	@Check
	def searchCyclicType(TypeDeclaration dec) {
		// Set with all created graph nodes
		edges = new HashSet<CyclicTypesNode>;
		// Index counter for Tarjan's strongly connected components algorithm
		index = 0
		nodes_stack = new Stack();
		declarationsVisited = new HashSet<String>;
		// If there is a Cycle that includes dec, cyclic will be set and MultipleInheritence will not be validated for dec.		
		cyclic = false
		// Creates the Graph
		firstnode = new CyclicTypesNode(dec, -1)
		edges.add(firstnode)
		addNode(firstnode)

		// Apply Tarjan's strongly connected components algorithm
		// For each strongly connected component of the Graph ->CyclicComponent Method
		for (CyclicTypesNode node : edges) {
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
		var boolean error = false //Becomes true if an multipleInheritence Error is found for dec
		
		if (dec.supertypes.size > 1) {
			var Set<TypeDeclaration> inheritedNoninterfaceSupertypes = new HashSet<TypeDeclaration>
			for (TypeDeclarationReference r : dec.supertypes) {
				if (r.type instanceof Interfacetype) {
					inheritedNoninterfaceSupertypes.addAll(numberOfSupertypes(r.type))
				} else {
					inheritedNoninterfaceSupertypes.add(r.type)
				}
			}
			if (inheritedNoninterfaceSupertypes.size > 1) {
				// There are more than 1 non-Interface Supertypes for firstnode -> Check if they have a minimum
				var boolean minimum = false // Will become true if there is a minimum, else there is an error
				for (TypeDeclaration declara : inheritedNoninterfaceSupertypes) {
					if (checkMinimum(declara, inheritedNoninterfaceSupertypes)) {
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
		
		//temporary validation if there are 2 or more non-interface Supertypes if no error was found
		if(!error){
			checkMultipleInheritence(dec)
		}
	}
	
	
	def checkMultipleInheritence(TypeDeclaration dec) {
		if(checkSupertypes(dec)==false){
			//More than one non-interface Supertype
			error("Error: Inheritence error.", dec,
						SKilLPackage.Literals.DECLARATION__NAME, MULTIPLE_INHERITENCE_ERROR, dec.name)
		}
	}
	
	/**
	 * 
	 * @param dec The Declaration to check.
	 * @return False if inheritence error exists; else true.
	 * 
	 */
	def boolean checkSupertypes(TypeDeclaration dec){
		var Set<TypeDeclaration> interfaces = new HashSet<TypeDeclaration>();
		var Set<TypeDeclaration> types = new HashSet<TypeDeclaration>();
		for (TypeDeclarationReference tdr: dec.supertypes){
			if(tdr.type instanceof Interfacetype){
				interfaces.add(tdr.type)
			}else{
				types.add(tdr.type)
			}
		}
		if(types.size>1){
			//More than one non-interface supertype=> error
			return false
		}
		return true;
	}
	
	
	

	/**
	 * Checks if dec is a Minimum for the Nodes in declarations
	 */
	def boolean checkMinimum(TypeDeclaration dec, Set<TypeDeclaration> declarations) {
		for (TypeDeclaration type : declarations) {
			if (!type.name.equals(dec.name)) {
				var visited = new HashSet<TypeDeclaration>
				if (!searchSupertype(type.name, dec, visited)) {
					return false // type is no Supertype for dec -> dec is not a minimum for the Types in declarations
				}
			}
		}
		return true;
	}

	/**
	 *  Checks if there is a supertype with the name name that can be reached starting with dec
	 * 	visited contains all visited supertypes to avoid cycles
	 */
	def boolean searchSupertype(String name, TypeDeclaration dec, Set<TypeDeclaration> visited) {
		var boolean found = false
		for (TypeDeclarationReference d : dec.supertypes) {
			if (!visited.contains(d.type)) {
				visited.add(d.type)
				if (d.type.name.equals(name)) {
					return true; // Found Supertype
				} else {
					if (!found) {
						found = searchSupertype(name, d.type, visited) // Search the Supertypes of d for name
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
	def List<TypeDeclaration> numberOfSupertypes(TypeDeclaration dec) {
		var List<TypeDeclaration> noninterfaceSupertypesOfDeclaration = new ArrayList<TypeDeclaration>
		if (declarationsVisited.contains(dec.name)) {
			return noninterfaceSupertypesOfDeclaration // Cycle
		}
		declarationsVisited.add(dec.name)
		for (TypeDeclarationReference d : dec.supertypes) {
			if (d.type instanceof Interfacetype) {
				noninterfaceSupertypesOfDeclaration.addAll(numberOfSupertypes(d.type)) // Check Supertypes
			} else {
				noninterfaceSupertypesOfDeclaration.add(d.type)
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
			} else if (successor.getonstack) {
				if (successor.getindex() < node.getlowlink()) {
					node.setlowlink(successor.getindex())
				}
			}
		}
		if (node.getlowlink() == node.getindex()) {
			// New Cyclic component
			var Set<CyclicTypesNode> cyclic = new HashSet<CyclicTypesNode>
			var CyclicTypesNode nextnode = nodes_stack.pop;
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
	 * This Method gets a new Node that was added and adds all supertypes.
	 * The Node should already be in the Nodeset.
	 * 
	 */
	def void addNode(CyclicTypesNode addednode) {
		var boolean tested = false // Will be set as true if the node is found in the Set
		// Checks if there is already a Node for the Supertype.
		for (TypeDeclarationReference ref : addednode.typeDeclaration.supertypes) {
			val String name = ref.type.name; // Name of the Declaration
			for (CyclicTypesNode node : edges) {
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
	 * This Method gets the strongly connected component and 
	 * creates the error message if there is an error.
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
				for (CyclicTypesNode n : nodes) {
					array_nodes.set(counter, n.typeDeclaration.name)
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
