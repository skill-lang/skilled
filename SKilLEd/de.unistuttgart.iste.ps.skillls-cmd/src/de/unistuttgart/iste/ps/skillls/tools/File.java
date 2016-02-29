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

public class File extends SkillObject {

    @Override
    public String skillName() {
        return "file";
    }

    /**
     * Create a new unmanaged File. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public File() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public File(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public File(long skillID, java.util.ArrayList<java.lang.String> dependencies, java.lang.String header, java.lang.String md5, java.lang.String path, java.lang.String timestamp) {
        super(skillID);
        this.dependencies = dependencies;
        this.header = header;
        this.md5 = md5;
        this.path = path;
        this.timestamp = timestamp;
    }

    protected java.util.ArrayList<java.lang.String> dependencies = null;

    final public java.util.ArrayList<java.lang.String> getDependencies() {
        return dependencies;
    }

    final public void setDependencies(java.util.ArrayList<java.lang.String> dependencies) {
        this.dependencies = dependencies;
    }

    protected java.lang.String header = null;

    final public java.lang.String getHeader() {
        return header;
    }

    final public void setHeader(java.lang.String header) {
        this.header = header;
    }

    protected java.lang.String md5 = null;

    final public java.lang.String getMd5() {
        return md5;
    }

    final public void setMd5(java.lang.String md5) {
        this.md5 = md5;
    }

    protected java.lang.String path = null;

    final public java.lang.String getPath() {
        return path;
    }

    final public void setPath(java.lang.String path) {
        this.path = path;
    }

    protected java.lang.String timestamp = null;

    final public java.lang.String getTimestamp() {
        return timestamp;
    }

    final public void setTimestamp(java.lang.String timestamp) {
        this.timestamp = timestamp;
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
        case "dependencies":
            return (T) dependencies;
        case "header":
            return (T) header;
        case "md5":
            return (T) md5;
        case "path":
            return (T) path;
        case "timestamp":
            return (T) timestamp;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("File(this: ").append(this);
        sb.append(", dependencies: ").append(dependencies);
        sb.append(", header: ").append(header);
        sb.append(", md5: ").append(md5);
        sb.append(", path: ").append(path);
        sb.append(", timestamp: ").append(timestamp);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends File implements NamedType {
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
