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

/**
 * This class contains the Validation rules for Cyclic Types. 
 * 
 * @author Jan Berberich
 * 
 */
class CyclicTypesValidator  extends AbstractDeclarativeValidator {

	public static val CYCLIC_TYPES = 'cyclicTypes'
	public static val TYPE_IS_HIS_OWN_PARENT = 'cycleError'
	public static var index = 0
	public static var Set<CyclicTypesNode> edges 
	public static var Stack<CyclicTypesNode> nodes_stack = new Stack()
	public static var CyclicTypesNode firstnode;
	

	override register(EValidatorRegistrar registar){
		
	}		
	@Check
	def searchCyclicType(TypeDeclaration dec){
	edges = new HashSet<CyclicTypesNode>;	//Set with all created graph nodes
	index = 0								//Index counter for Tarjan's strongly connected components algorithm
	nodes_stack = new Stack();			
	//Creates the Graph
	firstnode = new CyclicTypesNode(dec, -1)		
	edges.add(firstnode)	
	addNode(firstnode)	
	
	//Apply Tarjan's strongly connected components algorithm
	//For each strongly connected component of the Graph ->CyclicComponent Method
	for(CyclicTypesNode node:edges){
		if(node.getindex == -1){
			strongconnect(node)
		}			
	}
	
	//Test if Node is his own parent	
	if(firstnode.getsuccessors.contains(firstnode)){
		error("Error: type can't be his own parent." , firstnode.typeDeclaration, SKilLPackage.Literals.DECLARATION__NAME, TYPE_IS_HIS_OWN_PARENT, firstnode.typeDeclaration.name)
	}		
	}	
	def strongconnect(CyclicTypesNode node){
		node.setindex(index)
		node.setlowlink(index)
		index++
		nodes_stack.push(node)
		node.setonstack(true)
		for(CyclicTypesNode successor: node.getsuccessors){
			if(successor.getindex() == -1){
				strongconnect(successor)
				if(successor.getlowlink()<node.getlowlink()){
					node.setlowlink(successor.getlowlink())
				}
			}else if(successor.getonstack){
				if(successor.getindex()< node.getlowlink()){
					node.setlowlink(successor.getindex())
				}
			}	
		}		
		if(node.getlowlink() == node.getindex()){
			// New Cyclic component
			var Set<CyclicTypesNode> cyclic = new HashSet<CyclicTypesNode>
			var CyclicTypesNode nextnode = nodes_stack.pop;
			while((!nextnode.equals(node))&&(nextnode!= null)){
				cyclic.add(nextnode)
				nextnode = nodes_stack.pop;
			} 		
			if(nextnode.equals(node)){
				cyclic.add(nextnode)
			}	
			connectedcomponent(cyclic)
		}
	
	}

	/*
	 * This Method gets a new Node that was added and adds all supertypes.
	 * The Node should already be in the Nodeset.
	 * 
	 */
	def void addNode(CyclicTypesNode addednode){
	var boolean tested = false //Will be set as true if the node is found in the Set
	//Checks if there is already a Node for the Supertype.
	for(TypeDeclarationReference ref : addednode.typeDeclaration.supertypes){
		val String name = ref.type.name;	//Name of the Declaration
		for(CyclicTypesNode node: edges){
			//node is the node for the DeclarationReference ref
			if(node.typeDeclaration.name.equals(name)){
				tested = true;
				addednode.addsuccessor(node);
			}
		}
		//Add a new Node if the Node was not found in the NodeSet.
		if(tested == false){
			var CyclicTypesNode new_node = new CyclicTypesNode(ref.type, -1)
			edges.add(new_node)
			addednode.addsuccessor(new_node)					
			addNode(new_node)
		}
	}
	}

	/*
	 * This Method gets the strongly connected component and 
	 * creates the error message if there is an error.
	 *  
	 */
	def connectedcomponent(Set<CyclicTypesNode> nodes){
		//If the strongly connected component has more than 1 Element, there is a Cycle.
		//For every Node that is not part of a Cycle, there is a component with only this Node as Element.
		if(nodes.size> 1){
			//If firstnode is in the Cycle -> Error Message at this Place
			if(nodes.contains(firstnode)){
				//Create Array with the names of the Types of the component for the Quickfix
				var int counter = 0
				var String[] array_nodes = newArrayOfSize(nodes.size)
				for(CyclicTypesNode n: nodes){
					array_nodes.set(counter, n.typeDeclaration.name)
					counter++
				}
				//Error Message
				error("Error: can't use extend in a Cycle" , firstnode.typeDeclaration, SKilLPackage.Literals.DECLARATION__NAME, CYCLIC_TYPES, array_nodes)
				}
			}
		}
	
}