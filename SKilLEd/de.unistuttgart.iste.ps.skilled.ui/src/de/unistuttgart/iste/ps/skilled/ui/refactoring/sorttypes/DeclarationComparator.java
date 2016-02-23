package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import java.util.Comparator;
import java.util.HashSet;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.validation.InheritenceValidator;


/**
 * sorts declarations in the following oder: <br>
 * (1) Enums <br>
 * (2) Typedefs <br>
 * (3) Types <br>
 * <br>
 * Types are sorted according to their inheritance hierarchy.<br>
 * Enums, Typedefs and independent Types are sorted by their name.<br>
 * 
 * @author Tobias Heck
 *
 */
public class DeclarationComparator implements Comparator<Declaration> {

    @Override
    public int compare(Declaration arg0, Declaration arg1) {

        // put enums first
        if (arg0 instanceof Enumtype && !(arg1 instanceof Enumtype)) {
            return -1;
        }
        if (arg1 instanceof Enumtype && !(arg0 instanceof Enumtype)) {
            return 1;
        }

        // put typedefs second
        if (arg0 instanceof Typedef && !(arg1 instanceof Typedef)) {
            return -1;
        }
        if (arg1 instanceof Typedef && !(arg0 instanceof Typedef)) {
            return 1;
        }

        // sort type declarations by inheritance hierarchy
        if (arg0 instanceof TypeDeclaration) {
            if (isParent((TypeDeclaration) arg0, (TypeDeclaration) arg1)) {
                return -1;
            } else if (isParent((TypeDeclaration) arg1, (TypeDeclaration) arg0)) {
                return 1;
            }
        }

        // sort enums, typedefs and independant types by name
        return arg0.getName().compareTo(arg1.getName());
    }

    private static boolean isParent(TypeDeclaration arg0, TypeDeclaration arg1) {
        return InheritenceValidator.searchSupertype(arg0.getName(), arg1, new HashSet<TypeDeclaration>());
    }

}
