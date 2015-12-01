package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent;
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Enuminstance;

public class SKilLQualifiedNameConverters extends DefaultDeclarativeQualifiedNameProvider {
	
	QualifiedName qualifiedName(Fieldcontent field) {
		EObject e = field.eContainer();
		Declaration d = null;
		while (!Declaration.class.isInstance(e)) {
			e = e.eContainer();
		}
		if (e == null) {
			QualifiedName q = QualifiedName.create(makeEquivalent(field.getName()));
			return q;
		}
		d = (Declaration) e;
		QualifiedName q = QualifiedName.create(makeEquivalent(d.getName()), makeEquivalent(field.getName()));
        return q;
    }
	
	QualifiedName qualifiedName(Enuminstance enuminstance) {
		EObject e = enuminstance.eContainer();
		Declaration d = null;
		while (!Declaration.class.isInstance(e)) {
			e = e.eContainer();
		}
		if (e == null) {
			QualifiedName q = QualifiedName.create(makeEquivalent(enuminstance.getName()));
			return q;
		}
		d = (Declaration) e;
		QualifiedName q = QualifiedName.create(makeEquivalent(d.getName()), makeEquivalent(enuminstance.getName()));
        return q;
    }
	
	private String makeEquivalent(String string) {
		string = string.toLowerCase();
    	int index = 0;
    	while (string.charAt(index) == '_') {
    		index++;
    		if (index == string.length()) return string;
    	}
    	boolean wasUnderscore = true;
    	while (index < string.length()) {
    		if (string.charAt(index) != '_') {
    			index++;
    			wasUnderscore = false;
    			continue;
    		}
    		if (wasUnderscore == false) {
    			wasUnderscore = true;
    			string = string.substring(0,index) + string.substring(index + 1);
    			continue;
    		}
    		index++;
    	}
    	if (!string.matches("_*")) {
    		while (string.charAt(string.length() - 1) == '_')
    			string = string.substring(0, string.length() - 1);
    	}
    	return string;
	}

}
