/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;


/**
 *  containers with a single base type
 */
public class SingleBaseTypeContainer extends BuiltinType {
    private static final long serialVersionUID = 0x5c11L + ((long) "singlebasetypecontainer".hashCode()) << 32;

    @Override
    public String skillName() {
        return "singlebasetypecontainer";
    }

    /**
     * Create a new unmanaged SingleBaseTypeContainer. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public SingleBaseTypeContainer() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public SingleBaseTypeContainer(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public SingleBaseTypeContainer(long skillID, de.unistuttgart.iste.ps.skilled.sir.GroundType base, java.lang.String kind) {
        super(skillID);
        this.base = base;
        this.kind = kind;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.GroundType base = null;

    /**
     *  the base type of this container
     */
    final public de.unistuttgart.iste.ps.skilled.sir.GroundType getBase() {
        return base;
    }

    /**
     *  the base type of this container
     */
    final public void setBase(de.unistuttgart.iste.ps.skilled.sir.GroundType base) {
        this.base = base;
    }

    protected java.lang.String kind = null;

    /**
     *  can be one of: "set", "array", "list"
     */
    final public java.lang.String getKind() {
        return kind;
    }

    /**
     *  can be one of: "set", "array", "list"
     */
    final public void setKind(java.lang.String kind) {
        this.kind = kind;
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
        case "kind":
            return (T) kind;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("SingleBaseTypeContainer(this: ").append(this);
        sb.append(", base: ").append(base);
        sb.append(", kind: ").append(kind);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends SingleBaseTypeContainer implements NamedType {
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
