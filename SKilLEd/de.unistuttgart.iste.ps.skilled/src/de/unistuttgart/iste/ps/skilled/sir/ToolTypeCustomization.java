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
 *  this type is used to allow tools to define their own non-standard set of hints and restrictions
 */
public class ToolTypeCustomization extends SkillObject implements Annotations {
    private static final long serialVersionUID = 0x5c11L + ((long) "tooltypecustomization".hashCode()) << 32;

    @Override
    public String skillName() {
        return "tooltypecustomization";
    }

    /**
     * Create a new unmanaged ToolTypeCustomization. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public ToolTypeCustomization() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public ToolTypeCustomization(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public ToolTypeCustomization(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        super(skillID);
        this.hints = hints;
        this.restrictions = restrictions;
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
        StringBuilder sb = new StringBuilder("ToolTypeCustomization(this: ").append(this);
        sb.append(", hints: ").append(hints);
        sb.append(", restrictions: ").append(restrictions);
        return sb.append(")").toString();
    }

    @Override
    public ToolTypeCustomization self() {
        return this;
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends ToolTypeCustomization implements NamedType {
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
