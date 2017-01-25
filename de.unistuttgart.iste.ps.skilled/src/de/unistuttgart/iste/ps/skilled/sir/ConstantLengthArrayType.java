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
 * 
 *  @note  kind is always "array"
 */
public class ConstantLengthArrayType extends SingleBaseTypeContainer {
    private static final long serialVersionUID = 0x5c11L + ((long) "constantlengtharraytype".hashCode()) << 32;

    @Override
    public String skillName() {
        return "constantlengtharraytype";
    }

    /**
     * Create a new unmanaged ConstantLengthArrayType. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public ConstantLengthArrayType() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public ConstantLengthArrayType(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public ConstantLengthArrayType(long skillID, long length, de.unistuttgart.iste.ps.skilled.sir.GroundType base, java.lang.String kind) {
        super(skillID);
        this.length = length;
        this.base = base;
        this.kind = kind;
    }

    protected long length = 0;

    /**
     *  the constant length of this array
     */
    final public long getLength() {
        return length;
    }

    /**
     *  the constant length of this array
     */
    final public void setLength(long length) {
        this.length = length;
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
        case "length":
            return (T) (java.lang.Long) length;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("ConstantLengthArrayType(this: ").append(this);
        sb.append(", length: ").append(length);
        sb.append(", base: ").append(base);
        sb.append(", kind: ").append(kind);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends ConstantLengthArrayType implements NamedType {
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
