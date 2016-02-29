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

public class Generator extends SkillObject {

    @Override
    public String skillName() {
        return "generator";
    }

    /**
     * Create a new unmanaged Generator. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Generator() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Generator(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Generator(long skillID, java.lang.String execEnv, java.lang.String path) {
        super(skillID);
        this.execEnv = execEnv;
        this.path = path;
    }

    protected java.lang.String execEnv = null;

    final public java.lang.String getExecEnv() {
        return execEnv;
    }

    final public void setExecEnv(java.lang.String execEnv) {
        this.execEnv = execEnv;
    }

    protected java.lang.String path = null;

    final public java.lang.String getPath() {
        return path;
    }

    final public void setPath(java.lang.String path) {
        this.path = path;
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
        case "execenv":
            return (T) execEnv;
        case "path":
            return (T) path;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Generator(this: ").append(this);
        sb.append(", execEnv: ").append(execEnv);
        sb.append(", path: ").append(path);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Generator implements NamedType {
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
