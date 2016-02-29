package de.unistuttgart.iste.ps.skilled.ui.refactoring.extractspecification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.XtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype;
import de.unistuttgart.iste.ps.skilled.sKilL.Basetype;
import de.unistuttgart.iste.ps.skilled.sKilL.BuiltInType;
import de.unistuttgart.iste.ps.skilled.sKilL.Constant;
import de.unistuttgart.iste.ps.skilled.sKilL.Data;
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Field;
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent;
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype;
import de.unistuttgart.iste.ps.skilled.sKilL.Hint;
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype;
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype;
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction;
import de.unistuttgart.iste.ps.skilled.sKilL.Settype;
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;
import de.unistuttgart.iste.ps.skilled.sKilL.View;
import de.unistuttgart.iste.ps.skilled.ui.quickfix.SKilLQuickfixProvider;


public class ExtractSpecification {

    @Inject
    static ValidationTestHelper helper;
    
    static de.unistuttgart.iste.ps.skilled.sKilL.File SKilLFile = null;

    public static void run(TypeDeclaration[] checkedDeclarations, File newFile) {
        // write extracted declarations
        FileWriter fw;
        try {
            fw = new FileWriter(newFile, false);
            for (TypeDeclaration decl : checkedDeclarations) {
                if (decl.getComment() != null) {
                    fw.write(decl.getComment() + System.lineSeparator());
                }
                if (decl instanceof Usertype) {
                    for (Hint hint : ((Usertype) decl).getHints()) {
                        fw.write("!" + hint.getHintName() + System.lineSeparator());
                    }
                    for (Restriction restriction : ((Usertype) decl).getRestrictions()) {
                        fw.write("@" + restriction.getRestrictionName() + System.lineSeparator());
                    }
                }
                fw.write(decl.getName() + " ");
                for (TypeDeclarationReference supertype : decl.getSupertypes()) {
                    fw.write(": " + supertype.getType().getName() + " ");
                }
                fw.write("{" + System.lineSeparator());
                for (Field field : decl.getFields()) {
                    if (field.getComment() != null) {
                        fw.write("  " + field.getComment() + System.lineSeparator());
                    }
                    for (Hint hint : field.getHints()) {
                        fw.write("  !" + hint.getHintName() + System.lineSeparator());
                    }
                    for (Restriction restriction : field.getRestrictions()) {
                        fw.write("  @" + restriction.getRestrictionName() + System.lineSeparator());
                    }
                    Fieldcontent fieldcontent = field.getFieldcontent();
                    if (fieldcontent instanceof View) {
                        fw.write("  view ");
                        fw.write(((DeclarationReference) ((View) fieldcontent).getFieldcontent().getFieldcontent()
                                .getFieldtype()).getType().getName());
                        fw.write(".");
                        fw.write(((View) fieldcontent).getFieldcontent().getFieldcontent().getName());
                        fw.write(" as ");
                        fw.write(((DeclarationReference) ((View) fieldcontent).getFieldtype()).getType().getName());
                        fw.write(" ");
                        fw.write(((View) fieldcontent).getName());
                        fw.write(";" + System.lineSeparator());
                    } else if (fieldcontent instanceof Constant) {
                        fw.write("  const ");
                        fw.write(((Integertype) ((Constant) fieldcontent).getFieldtype()).getType().getName());
                        fw.write(" ");
                        fw.write(((Constant) fieldcontent).getName());
                        fw.write(" = ");
                        fw.write(((Constant) fieldcontent).getValue().toString());
                        fw.write(";" + System.lineSeparator());
                    } else if (fieldcontent instanceof Data) {
                        if (((Data) fieldcontent).isIsAuto()) {
                            fw.write("  auto ");
                        } else {
                            fw.write("  ");
                        }
                        Fieldtype type = fieldcontent.getFieldtype();
                        if (type instanceof Maptype) {
                            fw.write("map<");
                            for (Basetype basetype : ((Maptype) type).getBasetypes()) {
                                if (basetype instanceof BuiltInType) {
                                    fw.write(getBuiltInTypeName((BuiltInType) basetype));
                                } else {
                                    fw.write(((DeclarationReference) basetype).getType().getName());
                                }
                                if (((Maptype) type).getBasetypes()
                                        .indexOf(basetype) != ((Maptype) type).getBasetypes().size() - 1) {
                                    fw.write(", ");
                                }
                            }
                            fw.write("> ");
                        } else if (type instanceof Settype) {
                            fw.write("set<");
                            Basetype basetype = ((Settype) type).getBasetype();
                            if (basetype instanceof BuiltInType) {
                                fw.write(getBuiltInTypeName((BuiltInType) basetype));
                            } else {
                                fw.write(((DeclarationReference) basetype).getType().getName());
                            }
                            fw.write("> ");
                        } else if (type instanceof Listtype) {
                            fw.write("list<");
                            Basetype basetype = ((Listtype) type).getBasetype();
                            if (basetype instanceof BuiltInType) {
                                fw.write(getBuiltInTypeName((BuiltInType) basetype));
                            } else {
                                fw.write(((DeclarationReference) basetype).getType().getName());
                            }
                            fw.write("> ");
                        } else if (type instanceof Arraytype) {
                            Basetype basetype = ((Listtype) type).getBasetype();
                            if (basetype instanceof BuiltInType) {
                                fw.write(getBuiltInTypeName((BuiltInType) basetype));
                            } else {
                                fw.write(((DeclarationReference) basetype).getType().getName());
                            }
                            fw.write("[");
                            if (((Arraytype) type).getLength() != null) {
                                fw.write(((Arraytype) type).getLength().toString());
                            }
                            fw.write("] ");
                        } else if (type instanceof DeclarationReference) {
                            fw.write(((DeclarationReference) type).getType().getName() + " ");
                        } else {
                            fw.write(getBuiltInTypeName((BuiltInType) type) + " ");
                        }
                        fw.write(fieldcontent.getName() + ";" + System.lineSeparator());
                    }
                }
                fw.write("}" + System.lineSeparator() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // refresh workspace
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IPath basePath = root.getLocation();
        String projectName = ExtractSpecificationDialog.currentFilePath.segment(basePath.segmentCount());
        try {
            root.getProject(projectName).refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException e) {
            e.printStackTrace();
        }
        // open new file
        IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor("a.skill");
        Constructor<org.eclipse.core.internal.resources.File> constructor = (Constructor<org.eclipse.core.internal.resources.File>) org.eclipse.core.internal.resources.File.class
                .getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        IPath path = root.getLocation();
        String shortPath = newFile.toString().split(path.lastSegment())[1];
        org.eclipse.core.internal.resources.File file;
        try {
            file = constructor.newInstance(new org.eclipse.core.runtime.Path(shortPath),
                    ResourcesPlugin.getWorkspace());
            ExtractSpecificationDialog.page.openEditor(new FileEditorInput(file), desc.getId());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        
        // organize imports
        IXtextDocument xtextDocument = EditorUtils.getActiveXtextEditor().getDocument();
        
        xtextDocument.modify(new IUnitOfWork<Void, XtextResource>() {
            @Override
            public java.lang.Void exec(XtextResource state) throws Exception {
                SKilLFile = (de.unistuttgart.iste.ps.skilled.sKilL.File) state.getContents().get(0);
                new SKilLQuickfixProvider().organizeImports(SKilLFile);
                return null;
            }
        });
    }

    private static String getBuiltInTypeName(BuiltInType basetype) {
        if (basetype instanceof Integertype) {
            return ((Integertype) basetype).getType().getName();
        } else if (basetype instanceof Floattype) {
            return ((Floattype) basetype).getType().getName();
        } else if (basetype instanceof Stringtype) {
            return "string";
        } else if (basetype instanceof Annotationtype) {
            return "annotation";
        } else {
            return "boolean";
        }
    }

}
