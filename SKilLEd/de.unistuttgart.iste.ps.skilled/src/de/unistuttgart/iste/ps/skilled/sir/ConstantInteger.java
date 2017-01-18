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
 *  representation of constant integers
 */
public class ConstantInteger extends SimpleType {
    private static final long serialVersionUID = 0x5c11L + ((long) "constantinteger".hashCode()) << 32;

    @Override
    public String skillName() {
        return "constantinteger";
    }

    /**
     * Create a new unmanaged ConstantInteger. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public ConstantInteger() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public ConstantInteger(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public ConstantInteger(long skillID, long value, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        super(skillID);
        this.value = value;
        this.name = name;
    }

    protected long value = 0;

    final public long getValue() {
        return value;
    }

    final public void setValue(long value) {
        this.value = value;
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
        case "value":
            return (T) (java.lang.Long) value;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("ConstantInteger(this: ").append(this);
        sb.append(", value: ").append(value);
        sb.append(", name: ").append(name);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends ConstantInteger implements NamedType {
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
