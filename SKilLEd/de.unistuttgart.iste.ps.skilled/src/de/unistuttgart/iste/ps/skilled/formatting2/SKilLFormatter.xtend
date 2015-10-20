/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.formatting2;

import com.google.inject.Inject;
import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype;
import de.unistuttgart.iste.ps.skilled.sKilL.Basetype;
import de.unistuttgart.iste.ps.skilled.sKilL.Constant;
import de.unistuttgart.iste.ps.skilled.sKilL.Data;
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Field;
import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.sKilL.Hint;
import de.unistuttgart.iste.ps.skilled.sKilL.Include;
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile;
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype;
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype;
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction;
import de.unistuttgart.iste.ps.skilled.sKilL.Settype;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;
import de.unistuttgart.iste.ps.skilled.services.SKilLGrammarAccess;
import org.eclipse.xtext.formatting2.AbstractFormatter2;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.RestrictionArgument
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference

/**
 * @author Marco Link
 */
class SKilLFormatter extends AbstractFormatter2 {

	@Inject extension SKilLGrammarAccess

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
		include.regionForKeyword('include').append[oneSpace]
		include.regionForKeyword('with').append[oneSpace]
		include.regionForKeyword('include').prepend[noSpace]
		include.regionForKeyword('with').prepend[noSpace]
		for (IncludeFile includeFiles : include.getIncludeFiles()) {
			format(includeFiles, document);
		}
		include.append[newLines = 2]
	}

	def dispatch void format(Typedef typedef, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		typedef.prepend[noSpace]
		typedef.regionForKeyword('typedef').append[oneSpace]
		for (Restriction restrictions : typedef.getRestrictions()) {
			restrictions.prepend[newLine; increaseIndentation]
			format(restrictions, document);
			restrictions.append[newLine; decreaseIndentation]
		}
		for (Hint hints : typedef.getHints()) {
			hints.prepend[newLine; increaseIndentation]
			format(hints, document);
			hints.append[newLine; decreaseIndentation]
		}
		typedef.regionForEObject()
		format(typedef.getFieldtype(), document);
		typedef.regionForKeyword(";").append[newLines = 2]
	}

	def dispatch void format(Enumtype enumtype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		enumtype.prepend[noSpace]
		enumtype.regionForKeyword("{").append[newLine; increaseIndentation]
		enumtype.regionForKeyword("enum").prepend[newLine]
		for (Field fields : enumtype.getFields()) {
			format(fields, document);
		}
		enumtype.regionForKeyword("}").prepend[decreaseIndentation]
		enumtype.regionForKeyword("}").append[newLines = 2]
	}

	def dispatch void format(Interfacetype interfacetype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		interfacetype.prepend[noSpace]
		interfacetype.regionForKeyword("{").append[newLine; increaseIndentation]
		for (TypeDeclarationReference supertypes : interfacetype.getSupertypes()) {
			format(supertypes, document);
		}
		for (Field fields : interfacetype.getFields()) {
			format(fields, document);
		}
		interfacetype.regionForKeyword("}").prepend[decreaseIndentation]
		interfacetype.regionForKeyword("}").append[newLines = 2]
	}

	def dispatch void format(Usertype usertype, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		usertype.prepend[noSpace]
		usertype.regionForKeyword("{").append[newLine; increaseIndentation]

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
		usertype.regionForKeyword("}").prepend[decreaseIndentation]
		usertype.regionForKeyword("}").append[newLines = 2]
	}

	def dispatch void format(Field field, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		field.prepend[noSpace]
		for (Restriction restrictions : field.getRestrictions()) {
			format(restrictions, document);
		}
		for (Hint hints : field.getHints()) {
			format(hints, document);
		}
		format(field.getFieldcontent(), document);
		field.append[newLine]
	}

	def dispatch void format(Restriction restriction, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		restriction.regionForKeyword("@").prepend[noSpace]
		restriction.regionForKeyword("@").append[noSpace]
		for (RestrictionArgument restrictionArguments : restriction.getRestrictionArguments()) {
			format(restrictionArguments, document);
		}
		restriction.append[newLine]
	}

	def dispatch void format(Hint hint, extension IFormattableDocument document) {
		hint.regionForKeyword("!").prepend[noSpace]
		hint.regionForKeyword("!").append[noSpace]
		hint.append[newLine]
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
}
