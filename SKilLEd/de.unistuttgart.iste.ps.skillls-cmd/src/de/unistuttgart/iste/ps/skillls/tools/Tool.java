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

public class Tool extends SkillObject {

    @Override
    public String skillName() {
        return "tool";
    }

    /**
     * Create a new unmanaged Tool. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Tool() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Tool(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Tool(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> files, de.unistuttgart.iste.ps.skillls.tools.Generator generator, java.lang.String language, java.lang.String module, java.lang.String name, java.lang.String outPath, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> types) {
        super(skillID);
        this.files = files;
        this.generator = generator;
        this.language = language;
        this.module = module;
        this.name = name;
        this.outPath = outPath;
        this.types = types;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> files = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> getFiles() {
        return files;
    }

    final public void setFiles(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> files) {
        this.files = files;
    }

    protected de.unistuttgart.iste.ps.skillls.tools.Generator generator = null;

    final public de.unistuttgart.iste.ps.skillls.tools.Generator getGenerator() {
        return generator;
    }

    final public void setGenerator(de.unistuttgart.iste.ps.skillls.tools.Generator generator) {
        this.generator = generator;
    }

    protected java.lang.String language = null;

    final public java.lang.String getLanguage() {
        return language;
    }

    final public void setLanguage(java.lang.String language) {
        this.language = language;
    }

    protected java.lang.String module = null;

    final public java.lang.String getModule() {
        return module;
    }

    final public void setModule(java.lang.String module) {
        this.module = module;
    }

    protected java.lang.String name = null;

    final public java.lang.String getName() {
        return name;
    }

    final public void setName(java.lang.String name) {
        this.name = name;
    }

    protected java.lang.String outPath = null;

    final public java.lang.String getOutPath() {
        return outPath;
    }

    final public void setOutPath(java.lang.String outPath) {
        this.outPath = outPath;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> types = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> getTypes() {
        return types;
    }

    final public void setTypes(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> types) {
        this.types = types;
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
        case "files":
            return (T) files;
        case "generator":
            return (T) generator;
        case "language":
            return (T) language;
        case "module":
            return (T) module;
        case "name":
            return (T) name;
        case "outpath":
            return (T) outPath;
        case "types":
            return (T) types;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Tool(this: ").append(this);
        sb.append(", files: ").append(files);
        sb.append(", generator: ").append(generator);
        sb.append(", language: ").append(language);
        sb.append(", module: ").append(module);
        sb.append(", name: ").append(name);
        sb.append(", outPath: ").append(outPath);
        sb.append(", types: ").append(types);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Tool implements NamedType {
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
