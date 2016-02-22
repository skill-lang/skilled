package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Field;
import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;

/**
 * This class sorts the user types in a single
 * file according to the type order defined
 * in the SKilL specification.
 * 
 * @author Tobias Heck
 *
 */
public class SortTypes {

    public void run() {
        IXtextDocument xtextDocument = EditorUtils.getActiveXtextEditor().getDocument();
        xtextDocument.modify(new IUnitOfWork<Void, XtextResource>() {
            @Override
            public java.lang.Void exec(XtextResource state) throws Exception {
                File file = (File) new SKilLServices().getAll(state).toArray()[0];
                
                //sort types
                EList<Declaration> declarationList = file.getDeclarations();
                ECollections.sort(declarationList, new DeclarationComparator());
                for (Declaration decl : declarationList) {
                    if (decl instanceof TypeDeclaration) {
                        
                        //sort fields
                        EList<Field> fieldList = ((TypeDeclaration) decl).getFields();
                        ECollections.sort(fieldList, new FieldComparator(file));
                        
                        //sort field restrictions
                        for (Field field : fieldList) {
                            ECollections.sort(field.getRestrictions(), new FieldRestrictionComparator());
                        }
                        
                        //sort type restrictions
                        if (decl instanceof Usertype) {
                            ECollections.sort(((Usertype) decl).getRestrictions(), new TypeRestrictionComparator());
                        }
                    } else if (decl instanceof Enumtype) {
                        
                        //sort fields
                        EList<Field> fieldList = ((Enumtype) decl).getFields();
                        ECollections.sort(fieldList, new FieldComparator(file));
                        
                        //sort field restrictions
                        for (Field field : fieldList) {
                            ECollections.sort(field.getRestrictions(), new FieldRestrictionComparator());
                        }
                    }
                }
                return null;
            }
        });
    }

}
