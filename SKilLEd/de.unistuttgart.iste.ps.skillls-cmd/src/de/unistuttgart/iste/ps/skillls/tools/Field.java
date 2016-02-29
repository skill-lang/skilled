/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;

public class Field extends HintParent {

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
    public Field(long skillID, java.lang.String comment, java.lang.String name, de.unistuttgart.iste.ps.skillls.tools.Field orig, java.util.ArrayList<java.lang.String> restrictions, de.unistuttgart.iste.ps.skillls.tools.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
        super(skillID);
        this.comment = comment;
        this.name = name;
        this.orig = orig;
        this.restrictions = restrictions;
        this.type = type;
        this.hints = hints;
    }

    protected java.lang.String comment = null;

    final public java.lang.String getComment() {
        return comment;
    }

    final public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    protected java.lang.String name = null;

    final public java.lang.String getName() {
        return name;
    }

    final public void setName(java.lang.String name) {
        this.name = name;
    }

    protected de.unistuttgart.iste.ps.skillls.tools.Field orig = null;

    final public de.unistuttgart.iste.ps.skillls.tools.Field getOrig() {
        return orig;
    }

    final public void setOrig(de.unistuttgart.iste.ps.skillls.tools.Field orig) {
        this.orig = orig;
    }

    protected java.util.ArrayList<java.lang.String> restrictions = null;

    final public java.util.ArrayList<java.lang.String> getRestrictions() {
        return restrictions;
    }

    final public void setRestrictions(java.util.ArrayList<java.lang.String> restrictions) {
        this.restrictions = restrictions;
    }

    protected de.unistuttgart.iste.ps.skillls.tools.Type type = null;

    final public de.unistuttgart.iste.ps.skillls.tools.Type getType() {
        return type;
    }

    final public void setType(de.unistuttgart.iste.ps.skillls.tools.Type type) {
        this.type = type;
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
        case "orig":
            return (T) orig;
        case "restrictions":
            return (T) restrictions;
        case "type":
            return (T) type;
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
        sb.append(", comment: ").append(comment);
        sb.append(", name: ").append(name);
        sb.append(", orig: ").append(orig);
        sb.append(", restrictions: ").append(restrictions);
        sb.append(", type: ").append(type);
        sb.append(", hints: ").append(hints);
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
