package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.Trigger;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.unistuttgart.iste.ps.skilled.skill.Declaration;
import de.unistuttgart.iste.ps.skilled.skill.Enumtype;
import de.unistuttgart.iste.ps.skilled.skill.Field;
import de.unistuttgart.iste.ps.skilled.skill.File;
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.skill.Usertype;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;


/**
 * This class sorts the user types in a single file according to the type order defined in the SKilL specification.
 * 
 * @author Tobias Heck
 *
 */
public class SortTypes {

    @SuppressWarnings("static-method")
    public void run() {
    	if (EditorUtils.getActiveXtextEditor() == null || EditorUtils.getActiveXtextEditor().getDocument() == null) { return; }
        IXtextDocument xtextDocument = EditorUtils.getActiveXtextEditor().getDocument();
        xtextDocument.modify(new IUnitOfWork<Void, XtextResource>() {
            @Override
            public java.lang.Void exec(XtextResource state) throws Exception {
                File file = (File) new SKilLServices().getAll(state, false).toArray()[0];

                // sort types
                EList<Declaration> declarationList = file.getDeclarations();
                customSort(declarationList, new DeclarationComparator());

                for (Declaration decl : declarationList) {
                    EList<Field> fieldList = null;
                    if (decl instanceof TypeDeclaration) {
                        fieldList = ((TypeDeclaration) decl).getFields();
                    } else if (decl instanceof Enumtype) {
                        fieldList = ((Enumtype) decl).getFields();
                    } else {
                        continue;
                    }

                    // sort fields
                    ECollections.sort(fieldList, new FieldComparator(file));

                    // sort field restrictions
                    for (Field field : fieldList) {
                        ECollections.sort(field.getRestrictions(), new FieldRestrictionComparator());
                    }

                    // sort type restrictions
                    if (decl instanceof Usertype) {
                        ECollections.sort(((Usertype) decl).getRestrictions(), new TypeRestrictionComparator());
                    }

                }
                IBindingService bindingService = PlatformUI.getWorkbench().getAdapter(IBindingService.class);
                Binding[] bindings = bindingService.getBindings();
                TriggerSequence sequence = null;
                for (int i = 0; i < bindings.length; i++) {
                    ParameterizedCommand pCommand = bindings[i].getParameterizedCommand();
                    if (pCommand == null)
                        continue;
                    Command command = pCommand.getCommand();
                    if (command == null)
                        continue;
                    if (command.getId().equals("org.eclipse.jdt.ui.edit.text.java.format")) {
                        sequence = bindings[i].getTriggerSequence();
                        break;
                    }
                }
                if (sequence == null)
                    return null;
                Trigger[] triggers = sequence.getTriggers();
                String[] keys = ((KeyStroke) triggers[0]).toString().replace("CTRL", "CONTROL").replace("STRG", "CONTROL")
                        .split("\\+");
                int[] modifier = null;
                if (keys.length > 1) {
                    modifier = new int[keys.length - 1];
                    for (int i = 0; i < keys.length - 1; i++) {
                        /*Class keyEventClass = KeyEvent.class;
                        keyEventClass.getConstructor()*/
                        java.lang.reflect.Field field = KeyEvent.class.getField("VK_" + keys[i]);
                        
                        modifier[i] = field.getInt(null);
                    }
                }
                int key = ((KeyStroke) triggers[0]).getNaturalKey();
                EditorUtils.getActiveXtextEditor().setFocus();
                Robot robot = new Robot();
                robot.delay(150);
                if (modifier != null) {
                    for (int i = 0; i < modifier.length; i++) {
                        robot.keyPress(modifier[i]);
                    }
                }
                robot.keyPress(key);
                robot.keyRelease(key);
                if (modifier != null) {
                    for (int i = modifier.length - 1; i >= 0; i--) {
                        robot.keyRelease(modifier[i]);
                    }
                }
                return null;
                
                
            }

            private void customSort(EList<Declaration> declarationList, DeclarationComparator declarationComparator) {
                int size = declarationList.size();
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (i != j && declarationComparator.compare(declarationList.get(i), declarationList.get(j))
                                * (i - j) < 0) {
                            if (i < j) {
                                declarationList.move(j, i);
                                declarationList.move(i, j - 1);
                            } else {
                                declarationList.move(i, j);
                                declarationList.move(j, i - 1);
                            }
                        }
                    }
                }
            }
        });
    }

}
