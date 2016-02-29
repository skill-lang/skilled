/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.AutoField;
import de.ust.skill.common.java.internal.fieldTypes.*;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.restrictions.FieldRestriction;

public class ToolAccess extends BasePool<de.unistuttgart.iste.ps.skillls.tools.Tool> {

    @Override
    final protected de.unistuttgart.iste.ps.skillls.tools.Tool[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skillls.tools.Tool[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    ToolAccess(int poolIndex) {
        super(poolIndex, "tool", new HashSet<String>(Arrays.asList(new String[] { "files", "generator", "language", "module", "name", "outpath", "types" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skillls.tools.Tool[] data() {
        return data;
    }

    @Override
    public void insertInstances() {
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skillls.tools.Tool r = new de.unistuttgart.iste.ps.skillls.tools.Tool(i + 1);
            data[i] = r;
            staticData.add(r);

            i += 1;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addKnownField(
        String name,
        de.ust.skill.common.java.internal.fieldTypes.StringType string,
        de.ust.skill.common.java.internal.fieldTypes.Annotation annotation) {

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.Tool> f;
        switch (name) {
        case "files":
            f = new KnownField_Tool_files(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skillls.tools.File>)(owner().poolByName().get("file"))), 1 + dataFields.size(), this);
            break;

        case "generator":
            f = new KnownField_Tool_generator((FieldType<de.unistuttgart.iste.ps.skillls.tools.Generator>)(owner().poolByName().get("generator")), 1 + dataFields.size(), this);
            break;

        case "language":
            f = new KnownField_Tool_language(string, 1 + dataFields.size(), this);
            break;

        case "module":
            f = new KnownField_Tool_module(string, 1 + dataFields.size(), this);
            break;

        case "name":
            f = new KnownField_Tool_name(string, 1 + dataFields.size(), this);
            break;

        case "outpath":
            f = new KnownField_Tool_outPath(string, 1 + dataFields.size(), this);
            break;

        case "types":
            f = new KnownField_Tool_types(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skillls.tools.Type>)(owner().poolByName().get("type"))), 1 + dataFields.size(), this);
            break;

        default:
            super.addKnownField(name, string, annotation);
            return;
        }
        if (!(f instanceof AutoField))
            dataFields.add(f);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool> f;
        switch (name) {
        case "files":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_files((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File>>) type, ID, this);
            break;

        case "generator":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_generator((FieldType<de.unistuttgart.iste.ps.skillls.tools.Generator>) type, ID, this);
            break;

        case "language":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_language((FieldType<java.lang.String>) type, ID, this);
            break;

        case "module":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_module((FieldType<java.lang.String>) type, ID, this);
            break;

        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_name((FieldType<java.lang.String>) type, ID, this);
            break;

        case "outpath":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_outPath((FieldType<java.lang.String>) type, ID, this);
            break;

        case "types":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Tool>) new KnownField_Tool_types((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type>>) type, ID, this);
            break;

        default:
            return super.addField(ID, type, name, restrictions);
        }

        for (FieldRestriction<?> r : restrictions)
            f.addRestriction(r);
        dataFields.add(f);
        return f;
    }

    /**
     * @return a new Tool instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Tool make() {
        de.unistuttgart.iste.ps.skillls.tools.Tool rval = new de.unistuttgart.iste.ps.skillls.tools.Tool();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.Tool make(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> files, de.unistuttgart.iste.ps.skillls.tools.Generator generator, java.lang.String language, java.lang.String module, java.lang.String name, java.lang.String outPath, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> types) {
        de.unistuttgart.iste.ps.skillls.tools.Tool rval = new de.unistuttgart.iste.ps.skillls.tools.Tool(-1, files, generator, language, module, name, outPath, types);
        add(rval);
        return rval;
    }

    public ToolBuilder build() {
        return new ToolBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.Tool());
    }

    /**
     * Builder for new Tool instances.
     *
     * @author Timm Felden
     */
    public static final class ToolBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.Tool> {

        protected ToolBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.Tool, ? super de.unistuttgart.iste.ps.skillls.tools.Tool> pool, de.unistuttgart.iste.ps.skillls.tools.Tool instance) {
            super(pool, instance);
        }

        public ToolBuilder files(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> files) {
            instance.setFiles(files);
            return this;
        }

        public ToolBuilder generator(de.unistuttgart.iste.ps.skillls.tools.Generator generator) {
            instance.setGenerator(generator);
            return this;
        }

        public ToolBuilder language(java.lang.String language) {
            instance.setLanguage(language);
            return this;
        }

        public ToolBuilder module(java.lang.String module) {
            instance.setModule(module);
            return this;
        }

        public ToolBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        public ToolBuilder outPath(java.lang.String outPath) {
            instance.setOutPath(outPath);
            return this;
        }

        public ToolBuilder types(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> types) {
            instance.setTypes(types);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Tool, de.unistuttgart.iste.ps.skillls.tools.Tool> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Tool.SubType, de.unistuttgart.iste.ps.skillls.tools.Tool> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.Tool.SubType, de.unistuttgart.iste.ps.skillls.tools.Tool> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Tool.SubType, de.unistuttgart.iste.ps.skillls.tools.Tool> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skillls.tools.Tool[] data = ((ToolAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skillls.tools.Tool.SubType r = new de.unistuttgart.iste.ps.skillls.tools.Tool.SubType(this, i + 1);
                data[i] = r;
                staticData.add(r);

                i += 1;
            }
        }
    }

    /**
     * punch a hole into the java type system :)
     */
    @SuppressWarnings("unchecked")
    static <T, U> FieldType<T> cast(FieldType<U> f) {
        return (FieldType<T>) f;
    }
}
