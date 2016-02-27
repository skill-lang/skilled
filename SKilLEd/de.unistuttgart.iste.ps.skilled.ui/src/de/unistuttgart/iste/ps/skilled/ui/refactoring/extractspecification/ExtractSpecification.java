package de.unistuttgart.iste.ps.skilled.ui.refactoring.extractspecification;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;

public class ExtractSpecification {
    
    @Inject
    static ValidationTestHelper helper;

    public static void run(TypeDeclaration[] checkedDeclarations, File newFile, IPath currentFilePath) {
        //write extracted declarations
        
    }

}
