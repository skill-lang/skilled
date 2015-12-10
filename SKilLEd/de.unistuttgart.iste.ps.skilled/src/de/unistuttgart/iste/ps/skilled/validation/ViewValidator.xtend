package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.View
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import java.util.ArrayList
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage

/**
 * This class validates views.
 * 
 * @author Jan Berberich
 */
class ViewValidator extends AbstractDeclarativeValidator {

	var ArrayList<TypeDeclaration> TypesSearched;
	public static val IS_NO_SUPERTYPE = 'noSupertypeViewError'
	public static val VIEW_ERROR = 'viewError'

	override register(EValidatorRegistrar registar) {}

	/**
	 * This methods checks all views in td.
	 * @param CheckedTypeDeclaration The TypeDeclaration the checked views are in.
	 * 
	 */
	@Check
	def validateViews(TypeDeclaration CheckedTypeDeclaration) {
		for (Field f : CheckedTypeDeclaration.fields) {
			// Check if Field is a View
			if ((f.fieldcontent instanceof View)) {
				var boolean isUsertype = false;	//becomes true if the as variable is a Usertype
				var View v = f.fieldcontent as View
				if (v.fieldtype instanceof DeclarationReference) {
					var DeclarationReference tdr = v.fieldtype as DeclarationReference
					if (tdr.type instanceof Usertype) {
						var Usertype usertype = tdr.type as Usertype
						isUsertype = true
						// view A.x as B c;:
						var String supertypeTypename = (v.fieldcontent.fieldcontent.eContainer.eContainer as Declaration).name; // a
						var String supertypeVarname = v.fieldcontent.fieldcontent.name; // x
						TypesSearched = new ArrayList<TypeDeclaration>;
						var TypeDeclaration supertypeDec = searchSupertype(supertypeTypename, CheckedTypeDeclaration)
						if (supertypeDec == null) {
							// Error: A not a supertype of td
							if(supertypeTypename.equals(CheckedTypeDeclaration.name)){
								error("Error: you must reference a type with a different name than the type the view is in.", v,
									SKilLPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)
							}else{
								error("Error: " + supertypeTypename + " is not a supertype of " + CheckedTypeDeclaration.name + ".", v,
									SKilLPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)
							}

						} else {
							var Field supertypeVar = searchVar(supertypeVarname, supertypeDec);
							if (supertypeVar == null) {
								// Error: x not a var in A
								error("Error: " + supertypeVarname + " is not a Variable in " + supertypeTypename + ".", v,
									SKilLPackage.Literals.VIEW.getEStructuralFeature(2), VIEW_ERROR)

							} else {
								var Usertype usertypeSupertypeVar = null;
								var boolean supertypeVarIsUsertype = false;
								if (supertypeVar.fieldcontent.fieldtype instanceof DeclarationReference) {
									var DeclarationReference usertypeDeclarationSupertype = supertypeVar.fieldcontent.fieldtype as DeclarationReference;
									if (usertypeDeclarationSupertype.type instanceof Usertype) {
										usertypeSupertypeVar = usertypeDeclarationSupertype.type as Usertype
										supertypeVarIsUsertype = true;
									}
								}
								//If SupertypeVar isn't a usertype => error else check if the type of supertypeVar is a supertype of the as var.
								if (!supertypeVarIsUsertype) {
									error("Error: " + supertypeVarname + " is not a Usertype variable.", v,
										SKilLPackage.Literals.VIEW.getEStructuralFeature(2), VIEW_ERROR)
								} else 	if (usertype.name != usertypeSupertypeVar.name) {
									TypesSearched = new ArrayList<TypeDeclaration>;
										if (searchSupertype(usertypeSupertypeVar.name, usertype) == null) {
											error("Error: " + usertypeSupertypeVar.name + " is not a supertype of " + usertype.name +
											".", v, SKilLPackage.Literals.VIEW.getEStructuralFeature(1),VIEW_ERROR)

										}
									}
								
									
								
							}
						}
					}
				}
				if (!isUsertype) {
					// Error: not a usertype!
					error("Error: not a usertype variable.", v, SKilLPackage.Literals.VIEW.getEStructuralFeature(1),VIEW_ERROR)
				}

				}
			}
		}

		/**
		 * This method searches a Field in a TypeDeclaration. 
		 * @param name The name of the field.
		 * @param dec The TypeDeclaration where the field is searched.
		 * @return If a Field with the name name is found, this Field will be returned; else null.
		 * 
		 */
		def Field searchVar(String name, TypeDeclaration dec) {
			for (Field f : dec.fields) {
				if (f.fieldcontent.name.equals(name)) {
					return f;
				}
			}
			return null;
		}

		/**
		 * This method searches a TypeDeclaration with the name name in the 
		 * supertypes of dec.
		 * You should make sure that TypesSearched is initialized as an empty list before calling this method.
		 * 
		 * @param name The name of the searched type
		 * @param dec The declaration as start of the search
		 * @return TypeDeclaration with the name name, if it wasn't found, null.
		 */
		def TypeDeclaration searchSupertype(String name, TypeDeclaration dec) {
			TypesSearched.add(dec);
			for (TypeDeclarationReference tdr : dec.supertypes) {
				if (!TypesSearched.contains(tdr.type)) {
					TypesSearched.add(tdr.type)
					if (tdr.type.name.equals(name)) {
						return tdr.type
					} else {
						var TypeDeclaration td = searchSupertype(name, tdr.type);
						if (td != null) {
							return td;
						}
					}
				}
			}
			return null;
		}
	}