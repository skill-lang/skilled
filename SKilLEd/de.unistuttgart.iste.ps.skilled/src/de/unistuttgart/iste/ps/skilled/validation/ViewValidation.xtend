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
class ViewValidation extends AbstractDeclarativeValidator {

	var ArrayList<TypeDeclaration> TypesSearched;
	public static val IS_NO_SUPERTYPE = 'noSupertypeViewError'

	override register(EValidatorRegistrar registar) {}

	@Check
	def validateViews(TypeDeclaration td) {
		for (Field f : td.fields) {
			// Check if Field is a View
			if ((f.fieldcontent instanceof View)) {
				var boolean isUsertype = false;
				var View v = f.fieldcontent as View
				if (v.fieldtype instanceof DeclarationReference) {
					var DeclarationReference tdr = v.fieldtype as DeclarationReference
					if (tdr.type instanceof Usertype) {
						var Usertype usertype = tdr.type as Usertype
						isUsertype = true
						println("....")
						// view A.x as B c;:
						var String v1 = (v.fieldtype as DeclarationReference).type.name // b
						var String v2 = f.fieldcontent.name // c
						var String supertype = (v.fieldcontent.fieldcontent.eContainer.eContainer as Declaration).name; // a
						var String supertypeVarname = v.fieldcontent.fieldcontent.name; // x
						TypesSearched = new ArrayList<TypeDeclaration>;
						var TypeDeclaration supertypeDec = searchSupertype(supertype, td)
						if (supertypeDec == null) {
							// Error: A not a supertype of td
							error("Error: " + supertype + " is not a supertype of " + td.name + ".", v,
								SKilLPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)

						} else {
							// print("TYPEFOUND: "+ supertypeDec.name)
							var Field supertypeVar = searchVar(supertypeVarname, supertypeDec);
							if (supertypeVar == null) {
								// Error: x not a var in A
								error("Error: " + supertypeVarname + " is not a Variable in " + supertype + ".", v,
									SKilLPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)

							} else {
								var Usertype usertypeSupertypeVar = null;
								var boolean supertypeVarIsUsertype = false;
								if (supertypeVar.fieldcontent.fieldtype instanceof DeclarationReference) {
									print("ISDeclarationRef")
									var DeclarationReference usertypeDeclaration = f.fieldcontent.
										fieldtype as DeclarationReference;
									print(usertypeDeclaration.type.name)
									if (usertypeDeclaration.type instanceof Usertype) {
										print(usertypeDeclaration.type.name + " is a Usertype")
										usertypeSupertypeVar = usertypeDeclaration.type as Usertype
										supertypeVarIsUsertype = true;
									}
								}
								if (!supertypeVarIsUsertype) {
									error("Error: " + supertypeVarname + " is not a Usertype variable.", v,
										SKilLPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)
								} else {
									if (usertype.name != usertypeSupertypeVar.name) {
										if (searchSupertype(usertypeSupertypeVar.name, usertype) == null) {
											error("Error: " + usertypeSupertypeVar.name + " is not a supertype of" + usertype.name +
											".", v, SKilLPackage.Literals.VIEW.getEStructuralFeature(1),IS_NO_SUPERTYPE)

										}
									}
								}
							}
						}
					}
				}
				if (!isUsertype) {
					// Error: not a usertype!
				}

				}
			}
		}

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
		 * supertypes of dec
		 * 
		 * @param dec The declaration as start of the search
		 * @param name The name of the searched type
		 * @return TypeDeclaration with the name name
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