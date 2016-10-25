package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import java.util.Comparator;

import de.unistuttgart.iste.ps.skilled.skill.Annotationtype;
import de.unistuttgart.iste.ps.skilled.skill.Arraytype;
import de.unistuttgart.iste.ps.skilled.skill.Booleantype;
import de.unistuttgart.iste.ps.skilled.skill.Constant;
import de.unistuttgart.iste.ps.skilled.skill.Declaration;
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.skill.Field;
import de.unistuttgart.iste.ps.skilled.skill.Fieldcontent;
import de.unistuttgart.iste.ps.skilled.skill.Fieldtype;
import de.unistuttgart.iste.ps.skilled.skill.File;
import de.unistuttgart.iste.ps.skilled.skill.Floattype;
import de.unistuttgart.iste.ps.skilled.skill.Integertype;
import de.unistuttgart.iste.ps.skilled.skill.Listtype;
import de.unistuttgart.iste.ps.skilled.skill.Maptype;
import de.unistuttgart.iste.ps.skilled.skill.Stringtype;
import de.unistuttgart.iste.ps.skilled.skill.View;


/**
 * This class supplies a method to compare two fields and determine their order. The field order can be read in the SKilL
 * specification. If the fields are of the same type, their names are used instead to determine the order.
 * 
 * @author Tobias Heck
 *
 */
public class FieldComparator implements Comparator<Field> {

    /**
     * 
     * You may pass the file in which the fields are saved to establish an order <br>
     * between fields referencing different user types (as long as they are in the file). <br>
     * You may also pass <I>null</I> to have them sorted by name.
     */
    public FieldComparator(File file) {
        this.file = file;
    }

    File file;

    @Override
    public int compare(Field o1, Field o2) {
        int difference = getOrder(o1) - getOrder(o2);
        if (difference != 0)
            return difference;
        return o1.getFieldcontent().getName().compareTo(o2.getFieldcontent().getName());
    }

    private int getOrder(Field field) {
        Fieldcontent content = field.getFieldcontent();
        Fieldtype type = null;
        if (content instanceof View) {
            type = ((View) content).getFieldtype();
        }
        if (content instanceof Constant) {
            type = ((Constant) content).getFieldtype();
            switch (((Integertype) type).getType()) {
                case I8:
                    return 0;
                case I16:
                    return 1;
                case I32:
                    return 2;
                case I64:
                    return 3;
                case V64:
                    return 4;
            }
        }
        type = content.getFieldtype();
        if (type instanceof Annotationtype) {
            return 5;
        }
        if (type instanceof Booleantype) {
            return 6;
        }
        if (type instanceof Integertype) {
            switch (((Integertype) type).getType()) {
                case I8:
                    return 7;
                case I16:
                    return 8;
                case I32:
                    return 9;
                case I64:
                    return 10;
                case V64:
                    return 11;
            }
        }
        if (type instanceof Floattype) {
            switch (((Floattype) type).getType()) {
                case F32:
                    return 12;
                case F64:
                    return 13;
            }
        }
        if (type instanceof Stringtype) {
            return 14;
        }
        if (type instanceof Arraytype) {
            if (((Arraytype) type).getLength() != null) {
                return 15;
            }
            return 16;
        }
        if (type instanceof Listtype) {
            return 17;
        }
        if (type instanceof Maptype) {
            return 18;
        }
        return 32 + idxT(((DeclarationReference) type).getType());
    }

    private int idxT(Declaration type) {
        if (file == null)
            return -1;
        return file.getDeclarations().indexOf(type);
    }

}
