/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.formatting2;

import de.unistuttgart.iste.ps.skilled.skill.Arraytype
import de.unistuttgart.iste.ps.skilled.skill.Basetype
import de.unistuttgart.iste.ps.skilled.skill.Constant
import de.unistuttgart.iste.ps.skilled.skill.Data
import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.Enuminstance
import de.unistuttgart.iste.ps.skilled.skill.Enumtype
import de.unistuttgart.iste.ps.skilled.skill.Field
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.skill.Hint
import de.unistuttgart.iste.ps.skilled.skill.HintArgument
import de.unistuttgart.iste.ps.skilled.skill.Include
import de.unistuttgart.iste.ps.skilled.skill.IncludeFile
import de.unistuttgart.iste.ps.skilled.skill.Interfacetype
import de.unistuttgart.iste.ps.skilled.skill.Listtype
import de.unistuttgart.iste.ps.skilled.skill.Maptype
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.RestrictionArgument
import de.unistuttgart.iste.ps.skilled.skill.Settype
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import de.unistuttgart.iste.ps.skilled.skill.View
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument

class SkillFormatter extends AbstractFormatter2 {

	def dispatch void format(File file, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Include includes : file.getIncludes()) {
			format(includes, document);
		}
		for (Declaration declarations : file.getDeclarations()) {
			format(declarations, document);
		}
	}

	def dispatch void format(Include include, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (IncludeFile includeFiles : include.getIncludeFiles()) {
			format(includeFiles, document);
		}
	}

	def dispatch void format(Typedef typedef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Restriction restrictions : typedef.getRestrictions()) {
			format(restrictions, document);
		}
		for (Hint hints : typedef.getHints()) {
			format(hints, document);
		}
		format(typedef.getFieldtype(), document);
	}

	def dispatch void format(Enumtype enumtype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Enuminstance instances : enumtype.getInstances()) {
			format(instances, document);
		}
		for (Field fields : enumtype.getFields()) {
			format(fields, document);
		}
	}

	def dispatch void format(Interfacetype interfacetype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (TypeDeclarationReference supertypes : interfacetype.getSupertypes()) {
			format(supertypes, document);
		}
		for (Field fields : interfacetype.getFields()) {
			format(fields, document);
		}
	}

	def dispatch void format(Usertype usertype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Restriction restrictions : usertype.getRestrictions()) {
			format(restrictions, document);
		}
		for (Hint hints : usertype.getHints()) {
			format(hints, document);
		}
		for (TypeDeclarationReference supertypes : usertype.getSupertypes()) {
			format(supertypes, document);
		}
		for (Field fields : usertype.getFields()) {
			format(fields, document);
		}
	}

	def dispatch void format(Field field, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Restriction restrictions : field.getRestrictions()) {
			format(restrictions, document);
		}
		for (Hint hints : field.getHints()) {
			format(hints, document);
		}
		format(field.getFieldcontent(), document);
	}

	def dispatch void format(View view, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(view.getFieldcontent(), document);
		format(view.getFieldtype(), document);
	}

	def dispatch void format(Constant constant, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(constant.getFieldtype(), document);
	}

	def dispatch void format(Data data, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(data.getFieldtype(), document);
	}

	def dispatch void format(Maptype maptype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (Basetype basetypes : maptype.getBasetypes()) {
			format(basetypes, document);
		}
	}

	def dispatch void format(Settype settype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(settype.getBasetype(), document);
	}

	def dispatch void format(Listtype listtype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(listtype.getBasetype(), document);
	}

	def dispatch void format(Arraytype arraytype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(arraytype.getBasetype(), document);
	}

	def dispatch void format(Restriction restriction, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (RestrictionArgument restrictionArguments : restriction.getRestrictionArguments()) {
			format(restrictionArguments, document);
		}
	}

	def dispatch void format(RestrictionArgument restrictionargument, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(restrictionargument.getValueType(), document);
	}

	def dispatch void format(Hint hint, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		for (HintArgument hintArguments : hint.getHintArguments()) {
			format(hintArguments, document);
		}
	}

	def dispatch void format(HintArgument hintargument, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		format(hintargument.getValueType(), document);
	}
}
