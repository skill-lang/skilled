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


public class FieldLike extends SkillObject implements Annotations {
    private static final long serialVersionUID = 0x5c11L + ((long) "fieldlike".hashCode()) << 32;

    @Override
    public String skillName() {
        return "fieldlike";
    }

    /**
     * Create a new unmanaged FieldLike. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public FieldLike() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public FieldLike(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public FieldLike(long skillID, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        super(skillID);
        this.comment = comment;
        this.name = name;
        this.type = type;
        this.hints = hints;
        this.restrictions = restrictions;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.Comment comment = null;

    /**
     *  the comment provided by the user or null
     */
    final public de.unistuttgart.iste.ps.skilled.sir.Comment getComment() {
        return comment;
    }

    /**
     *  the comment provided by the user or null
     */
    final public void setComment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
        this.comment = comment;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.Identifier name = null;

    /**
     *  the skill name
     */
    final public de.unistuttgart.iste.ps.skilled.sir.Identifier getName() {
        return name;
    }

    /**
     *  the skill name
     */
    final public void setName(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        this.name = name;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.Type type = null;

    /**
     *  The type of the field.
     */
    final public de.unistuttgart.iste.ps.skilled.sir.Type getType() {
        return type;
    }

    /**
     *  The type of the field.
     */
    final public void setType(de.unistuttgart.iste.ps.skilled.sir.Type type) {
        this.type = type;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> getHints() {
        return hints;
    }

    final public void setHints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
        this.hints = hints;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> getRestrictions() {
        return restrictions;
    }

    final public void setRestrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        this.restrictions = restrictions;
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
        case "comment":
            return (T) comment;
        case "name":
            return (T) name;
        case "type":
            return (T) type;
        case "hints":
            return (T) hints;
        case "restrictions":
            return (T) restrictions;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("FieldLike(this: ").append(this);
        sb.append(", comment: ").append(comment);
        sb.append(", name: ").append(name);
        sb.append(", type: ").append(type);
        sb.append(", hints: ").append(hints);
        sb.append(", restrictions: ").append(restrictions);
        return sb.append(")").toString();
    }

    @Override
    public FieldLike self() {
        return this;
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends FieldLike implements NamedType {
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
