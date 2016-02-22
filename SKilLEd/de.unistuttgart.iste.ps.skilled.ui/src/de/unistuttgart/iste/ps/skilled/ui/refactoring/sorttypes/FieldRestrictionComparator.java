package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import java.util.Comparator;

import de.unistuttgart.iste.ps.skilled.sKilL.Restriction;

/**
 * sorts field restrictions according to the order specified in the SKilL specification
 * 
 * @author Tobias Heck
 *
 */
public class FieldRestrictionComparator implements Comparator<Restriction> {

    @Override
    public int compare(Restriction o1, Restriction o2) {
        return getOrder(o2) - getOrder(o1);
    }

    private static int getOrder(Restriction o1) {
        switch (o1.getRestrictionName().toLowerCase()) {
            case "nonnull": return 0;
            case "default": return 1;
            case "range": return 3;
            case "coding": return 5;
            case "constantlengthpointer": return 7;
            case "oneof": return 9;
        }
        return -1;
    }

}
