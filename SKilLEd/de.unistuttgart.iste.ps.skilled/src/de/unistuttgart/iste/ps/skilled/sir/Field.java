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


public class Field extends FieldLike {
    private static final long serialVersionUID = 0x5c11L + ((long) "field".hashCode()) << 32;

    @Override
    public String skillName() {
        return "field";
    }

    /**
     * Create a new unmanaged Field. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Field() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Field(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Field(long skillID, boolean isAuto, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        super(skillID);
        this.isAuto = isAuto;
        this.comment = comment;
        this.name = name;
        this.type = type;
        this.hints = hints;
        this.restrictions = restrictions;
    }

    protected boolean isAuto = false;

    /**
     *  true, iff the field is an auto field
     */
    final public boolean getIsAuto() {
        return isAuto;
    }

    /**
     *  true, iff the field is an auto field
     */
    final public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
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
        case "isauto":
            return (T) (java.lang.Boolean) isAuto;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Field(this: ").append(this);
        sb.append(", isAuto: ").append(isAuto);
        sb.append(", comment: ").append(comment);
        sb.append(", name: ").append(name);
        sb.append(", type: ").append(type);
        sb.append(", hints: ").append(hints);
        sb.append(", restrictions: ").append(restrictions);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Field implements NamedType {
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
