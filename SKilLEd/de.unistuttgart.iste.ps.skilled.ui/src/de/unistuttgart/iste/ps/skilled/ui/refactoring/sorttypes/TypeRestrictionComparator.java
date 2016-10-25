package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import java.util.Comparator;

import de.unistuttgart.iste.ps.skilled.skill.Restriction;


/**
 * sorts type restrictions according to the order specified in the SKilL specification
 * 
 * @author Tobias Heck
 *
 */
public class TypeRestrictionComparator implements Comparator<Restriction> {

    @Override
    public int compare(Restriction o1, Restriction o2) {
        return getOrder(o2) - getOrder(o1);
    }

    private static int getOrder(Restriction o1) {
        switch (o1.getRestrictionName().toLowerCase()) {
            case "unique":
                return 0;
            case "singleton":
                return 1;
            case "monotone":
                return 2;
            case "abstract":
                return 3;
            case "default":
                return 5;
        }
        return -1;
    }

}
