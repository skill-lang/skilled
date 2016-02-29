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

public class HintParent extends SkillObject {

    @Override
    public String skillName() {
        return "hintparent";
    }

    /**
     * Create a new unmanaged HintParent. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public HintParent() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public HintParent(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public HintParent(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
        super(skillID);
        this.hints = hints;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> getHints() {
        return hints;
    }

    final public void setHints(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
        this.hints = hints;
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
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("HintParent(this: ").append(this);
        sb.append(", hints: ").append(hints);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends HintParent implements NamedType {
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
