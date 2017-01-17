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


public class BuildInformation extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "buildinformation".hashCode()) << 32;

    @Override
    public String skillName() {
        return "buildinformation";
    }

    /**
     * Create a new unmanaged BuildInformation. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public BuildInformation() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public BuildInformation(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public BuildInformation(long skillID, java.lang.String language, java.util.ArrayList<java.lang.String> options, de.unistuttgart.iste.ps.skilled.sir.FilePath output) {
        super(skillID);
        this.language = language;
        this.options = options;
        this.output = output;
    }

    protected java.lang.String language = null;

    /**
     *  the name of the language to be used. It is explicitly discouraged to use all languages. Create different build
     *  informations for every language instead, as the output directory should be changed.
     */
    final public java.lang.String getLanguage() {
        return language;
    }

    /**
     *  the name of the language to be used. It is explicitly discouraged to use all languages. Create different build
     *  informations for every language instead, as the output directory should be changed.
     */
    final public void setLanguage(java.lang.String language) {
        this.language = language;
    }

    protected java.util.ArrayList<java.lang.String> options = null;

    /**
     *  options are processed as if they were entered in the command line interface
     *  @note  options consisting of multiple strings will be stored in multiple strings in this form
     */
    final public java.util.ArrayList<java.lang.String> getOptions() {
        return options;
    }

    /**
     *  options are processed as if they were entered in the command line interface
     *  @note  options consisting of multiple strings will be stored in multiple strings in this form
     */
    final public void setOptions(java.util.ArrayList<java.lang.String> options) {
        this.options = options;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.FilePath output = null;

    /**
     *  the output directory passed to the target generator
     */
    final public de.unistuttgart.iste.ps.skilled.sir.FilePath getOutput() {
        return output;
    }

    /**
     *  the output directory passed to the target generator
     */
    final public void setOutput(de.unistuttgart.iste.ps.skilled.sir.FilePath output) {
        this.output = output;
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
        case "language":
            return (T) language;
        case "options":
            return (T) options;
        case "output":
            return (T) output;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("BuildInformation(this: ").append(this);
        sb.append(", language: ").append(language);
        sb.append(", options: ").append(options);
        sb.append(", output: ").append(output);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends BuildInformation implements NamedType {
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
