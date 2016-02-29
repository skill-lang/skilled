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

public class Hint extends SkillObject {

    @Override
    public String skillName() {
        return "hint";
    }

    /**
     * Create a new unmanaged Hint. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Hint() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Hint(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Hint(long skillID, java.lang.String name, de.unistuttgart.iste.ps.skillls.tools.HintParent parent) {
        super(skillID);
        this.name = name;
        this.parent = parent;
    }

    protected java.lang.String name = null;

    final public java.lang.String getName() {
        return name;
    }

    final public void setName(java.lang.String name) {
        this.name = name;
    }

    protected de.unistuttgart.iste.ps.skillls.tools.HintParent parent = null;

    final public de.unistuttgart.iste.ps.skillls.tools.HintParent getParent() {
        return parent;
    }

    final public void setParent(de.unistuttgart.iste.ps.skillls.tools.HintParent parent) {
        this.parent = parent;
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
        case "name":
            return (T) name;
        case "parent":
            return (T) parent;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Hint(this: ").append(this);
        sb.append(", name: ").append(name);
        sb.append(", parent: ").append(parent);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Hint implements NamedType {
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
