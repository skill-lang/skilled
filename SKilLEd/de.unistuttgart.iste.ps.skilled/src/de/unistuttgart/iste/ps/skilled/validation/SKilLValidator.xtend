/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.validation
import de.unistuttgart.iste.ps.skilled.sKilL.Constant
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInTypeReference
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype
import de.unistuttgart.iste.ps.skilled.sKilL.Basetype
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Settype
import org.eclipse.xtext.validation.ComposedChecks

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 * 
 * @author Marco Link
 * 
 */
 @ComposedChecks(validators =
#[CyclicTypesValidator
])
class SKilLValidator extends AbstractSKilLValidator {

	public static val INVALID_CONSTANT_TYPE = 'invalidConstantType'
	public static val INVALID_NESTED_TYPEDEF = 'invalidNestedTypedef'
		
	@Check
	def checkConstantHasAnInteger(Constant constant) {
		if (constant.fieldtype instanceof BuildInTypeReference) {
			val b = constant.fieldtype as BuildInTypeReference;
			switch (b.type) {
				case I8:
					return
				case I16:
					return
				case I32:
					return
				case I64:
					return
				case V64:
					return
				default: {
					error('Only an Integer can be const.', constant,
						SKilLPackage.Literals.CONSTANT.getEStructuralFeature("fieldtype"), INVALID_CONSTANT_TYPE)
				}
			}

		} else {
			error('Only an Integer can be const.', constant,
				SKilLPackage.Literals.CONSTANT.getEStructuralFeature("fieldtype"), INVALID_CONSTANT_TYPE)
		}
	}

	@Check
	def checkInvalidNestedTypedef(Fieldtype fieldtype) {
		if (fieldtype instanceof Listtype) {
			val listtype = fieldtype;
			if (listtype.basetype instanceof DeclarationReference) {
				val dr = listtype.basetype as DeclarationReference
				if (dr.type instanceof Typedef) {
					val td = dr.type as Typedef
					if ((td.fieldtype instanceof Listtype) || (td.fieldtype instanceof Maptype) ||
						(td.fieldtype instanceof Arraytype) || (td.fieldtype instanceof Settype)) {
						error('It is forbidden to nest containers inside of other containers.', listtype,
							SKilLPackage.Literals.LISTTYPE__BASETYPE, INVALID_NESTED_TYPEDEF)
					}
				}
			}
		} else if (fieldtype instanceof Arraytype) {
			val arraytype = fieldtype;
			if (arraytype.basetype instanceof DeclarationReference) {
				val dr = arraytype.basetype as DeclarationReference
				if (dr.type instanceof Typedef) {
					val td = dr.type as Typedef
					if ((td.fieldtype instanceof Listtype) || (td.fieldtype instanceof Maptype) ||
						(td.fieldtype instanceof Arraytype) || (td.fieldtype instanceof Settype)) {
						error('It is forbidden to nest containers inside of other containers.', arraytype,
							SKilLPackage.Literals.ARRAYTYPE__BASETYPE, INVALID_NESTED_TYPEDEF)
					}
				}
			}
		} else if (fieldtype instanceof Maptype) {
			val maptype = fieldtype;
			for (Basetype b : maptype.basetypes) {
				if (b instanceof DeclarationReference) {
					val dr = b
					if (dr.type instanceof Typedef) {
						val td = dr.type as Typedef
						if ((td.fieldtype instanceof Listtype) || (td.fieldtype instanceof Maptype) ||
							(td.fieldtype instanceof Arraytype) || (td.fieldtype instanceof Settype)) {
							error('It is forbidden to nest containers inside of other containers.', maptype,
								SKilLPackage.Literals.CONSTANT.getEStructuralFeature("basetypes"),
								INVALID_NESTED_TYPEDEF)
							}
						}
					}
				}
			} else if (fieldtype instanceof Settype) {
				val settype = fieldtype;
				if (settype.basetype instanceof DeclarationReference) {
					val dr = settype.basetype as DeclarationReference
					if (dr.type instanceof Typedef) {
						val td = dr.type as Typedef
						if ((td.fieldtype instanceof Listtype) || (td.fieldtype instanceof Maptype) ||
							(td.fieldtype instanceof Arraytype) || (td.fieldtype instanceof Settype)) {
							error('It is forbidden to nest containers inside of other containers.', settype,
								SKilLPackage.Literals.SETTYPE__BASETYPE, INVALID_NESTED_TYPEDEF)
						}
					}
				}
			}
		}
}

