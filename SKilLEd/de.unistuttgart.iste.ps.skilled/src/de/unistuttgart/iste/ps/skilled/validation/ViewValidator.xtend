package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Field
import de.unistuttgart.iste.ps.skilled.skill.Interfacetype
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import de.unistuttgart.iste.ps.skilled.skill.View
import de.unistuttgart.iste.ps.skilled.skill.impl.InterfacetypeImpl
import java.util.ArrayList
import java.util.List
import org.eclipse.xtext.validation.Check

/**
 * This class contains the validation methods for validating views.
 * @author Jan Berberich
 */
class ViewValidator extends AbstractSKilLComposedValidatorPart {

  val List<TypeDeclaration> typesSearched = new ArrayList
  public static val IS_NO_SUPERTYPE = 'noSupertypeViewError'
  public static val VIEW_ERROR = 'viewError'

  /**
   * This methods checks all views in checkedTypeDeclaration.
   * @param CheckedTypeDeclaration The TypeDeclaration the checked views are in.
   * 
   */
  @Check
  def validateViews(TypeDeclaration checkedTypeDeclaration) {
    for (Field f : checkedTypeDeclaration.fields) {
      // Check if Field is a View
      if ((f.fieldcontent instanceof View)) {
        var boolean isUsertype = false; // becomes true if the as variable is a Usertype
        var View viewToCheck = f.fieldcontent as View
        if (viewToCheck.fieldtype instanceof DeclarationReference) {
          var DeclarationReference viewFieldtypeDeclarationReference = viewToCheck.fieldtype as DeclarationReference
          if ((viewFieldtypeDeclarationReference.type instanceof Usertype) ||
            (viewFieldtypeDeclarationReference.type instanceof Interfacetype)) {
            var TypeDeclaration usertype = viewFieldtypeDeclarationReference.type as TypeDeclaration
            isUsertype = true
            // view A.x as B c;:
            var String supertypeTypename = (viewToCheck.fieldcontent.fieldcontent.eContainer.eContainer as Declaration).
              name; // a
            var String supertypeVarname = viewToCheck.fieldcontent.fieldcontent.name; // x
            typesSearched.clear
            var TypeDeclaration supertypeDec = searchSupertype(supertypeTypename, checkedTypeDeclaration)
            if (supertypeDec == null) {
              // Error: A not a supertype of td
              if (supertypeTypename.equals(checkedTypeDeclaration.name)) {
                error("Error: you must reference a type with a different name than the type the view is in.",
                  viewToCheck, SkillPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)
              } else {
                error("Error: " + supertypeTypename + " is not a supertype of " + checkedTypeDeclaration.name + ".",
                  viewToCheck, SkillPackage.Literals.VIEW.getEStructuralFeature(2), IS_NO_SUPERTYPE)
              }

            } else {
              var Field supertypeVar = searchVar(supertypeVarname, supertypeDec);
              if (supertypeVar == null) {
                // Error: x not a var in A
                error("Error: " + supertypeVarname + " is not a Variable in " + supertypeTypename + ".", viewToCheck,
                  SkillPackage.Literals.VIEW.getEStructuralFeature(2), VIEW_ERROR)

              } else {
                var Declaration usertypeSupertypeVar = null;
                var boolean supertypeVarIsUsertype = false;
                if (supertypeVar.fieldcontent.fieldtype instanceof DeclarationReference) {
                  var DeclarationReference usertypeDeclarationSupertype = supertypeVar.fieldcontent.
                    fieldtype as DeclarationReference;
                  if ((usertypeDeclarationSupertype.type instanceof Usertype) ||
                    (usertypeDeclarationSupertype.type instanceof InterfacetypeImpl)) {
                    supertypeVarIsUsertype = true;
                    usertypeSupertypeVar = usertypeDeclarationSupertype.type;
                  }

                }
                // If SupertypeVar isn't a usertype => error else check if the type of supertypeVar is a supertype of the as var.
                if (!supertypeVarIsUsertype) {
                  error("Error: " + supertypeVarname + " is not a Usertype or Interface variable.", viewToCheck,
                    SkillPackage.Literals.VIEW.getEStructuralFeature(2), VIEW_ERROR)
                } else if (usertype.name != usertypeSupertypeVar.name) {
                  typesSearched.clear
                  if (searchSupertype(usertypeSupertypeVar.name, usertype) == null) {
                    error("Error: " + usertypeSupertypeVar.name + " is not a supertype of " + usertype.name + ".",
                      viewToCheck, SkillPackage.Literals.VIEW.getEStructuralFeature(1), VIEW_ERROR)
                  }
                }
              }
            }
          }
        }
        if (!isUsertype) {
          // Error: not a usertype!
          error("Error: not a usertype variable.", viewToCheck, SkillPackage.Literals.VIEW.getEStructuralFeature(1),
            VIEW_ERROR)
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
        return f
      }
    }
    return null
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
    typesSearched.add(dec)
    for (TypeDeclarationReference tdr : dec.supertypes) {
      if (!typesSearched.contains(tdr.type)) {
        typesSearched.add(tdr.type)
        if (tdr.type.name.equals(name)) {
          return tdr.type
        } else {
          var TypeDeclaration td = searchSupertype(name, tdr.type)
          if (td != null) {
            return td
          }
        }
      }
    }
    return null
  }
}