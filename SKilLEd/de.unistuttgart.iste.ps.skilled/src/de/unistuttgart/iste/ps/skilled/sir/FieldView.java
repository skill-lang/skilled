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
 *  a view onto a field
 *  @note  some components such as annotations must not be set by views
 */
public class FieldView extends FieldLike {
    private static final long serialVersionUID = 0x5c11L + ((long) "fieldview".hashCode()) << 32;

    @Override
    public String skillName() {
        return "fieldview";
    }

    /**
     * Create a new unmanaged FieldView. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public FieldView() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public FieldView(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public FieldView(long skillID, de.unistuttgart.iste.ps.skilled.sir.FieldLike target, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        super(skillID);
        this.target = target;
        this.comment = comment;
        this.name = name;
        this.type = type;
        this.hints = hints;
        this.restrictions = restrictions;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.FieldLike target = null;

    /**
     *  the viewed component
     */
    final public de.unistuttgart.iste.ps.skilled.sir.FieldLike getTarget() {
        return target;
    }

    /**
     *  the viewed component
     */
    final public void setTarget(de.unistuttgart.iste.ps.skilled.sir.FieldLike target) {
        this.target = target;
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
        case "target":
            return (T) target;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("FieldView(this: ").append(this);
        sb.append(", target: ").append(target);
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
    public static final class SubType extends FieldView implements NamedType {
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
