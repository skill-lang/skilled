/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;


/**
 *  a map type
 */
public class MapType extends BuiltinType {
    private static final long serialVersionUID = 0x5c11L + ((long) "maptype".hashCode()) << 32;

    @Override
    public String skillName() {
        return "maptype";
    }

    /**
     * Create a new unmanaged MapType. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public MapType() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public MapType(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public MapType(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.GroundType> base) {
        super(skillID);
        this.base = base;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.GroundType> base = null;

    /**
     *  base types of the map in order of appearance
     */
    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.GroundType> getBase() {
        return base;
    }

    /**
     *  base types of the map in order of appearance
     */
    final public void setBase(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.GroundType> base) {
        this.base = base;
    }

    /**
     * unchecked conversions are required, because the Java type system known
     * nothing of our invariants
     * 
     * @note to self: Boxing bei primitiven beachten!
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(FieldDeclaration<T> field) {
        switch (field.name()) {
        case "base":
            return (T) base;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("MapType(this: ").append(this);
        sb.append(", base: ").append(base);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends MapType implements NamedType {
        private final StoragePool<?, ?> τPool;

        /** internal use only!!! */
        public SubType(StoragePool<?, ?> τPool, long skillID) {
            super(skillID);
            this.τPool = τPool;
        }

        @Override
        public StoragePool<?, ?> τPool() {
          return τPool;
        }

        @Override
        public String skillName() {
            return τPool.name();
        }

        @Override
        public String toString() {
            return skillName() + "#" + skillID;
        }
    }
}
