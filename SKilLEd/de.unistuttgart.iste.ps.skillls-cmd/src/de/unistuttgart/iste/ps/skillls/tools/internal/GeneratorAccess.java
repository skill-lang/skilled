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

public class GeneratorAccess extends BasePool<de.unistuttgart.iste.ps.skillls.tools.Generator> {

    @Override
    final protected de.unistuttgart.iste.ps.skillls.tools.Generator[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skillls.tools.Generator[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    GeneratorAccess(int poolIndex) {
        super(poolIndex, "generator", new HashSet<String>(Arrays.asList(new String[] { "execenv", "path" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skillls.tools.Generator[] data() {
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

            de.unistuttgart.iste.ps.skillls.tools.Generator r = new de.unistuttgart.iste.ps.skillls.tools.Generator(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.Generator> f;
        switch (name) {
        case "execenv":
            f = new KnownField_Generator_execEnv(string, 1 + dataFields.size(), this);
            break;

        case "path":
            f = new KnownField_Generator_path(string, 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Generator> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Generator> f;
        switch (name) {
        case "execenv":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Generator>) new KnownField_Generator_execEnv((FieldType<java.lang.String>) type, ID, this);
            break;

        case "path":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Generator>) new KnownField_Generator_path((FieldType<java.lang.String>) type, ID, this);
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
     * @return a new Generator instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Generator make() {
        de.unistuttgart.iste.ps.skillls.tools.Generator rval = new de.unistuttgart.iste.ps.skillls.tools.Generator();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.Generator make(java.lang.String execEnv, java.lang.String path) {
        de.unistuttgart.iste.ps.skillls.tools.Generator rval = new de.unistuttgart.iste.ps.skillls.tools.Generator(-1, execEnv, path);
        add(rval);
        return rval;
    }

    public GeneratorBuilder build() {
        return new GeneratorBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.Generator());
    }

    /**
     * Builder for new Generator instances.
     *
     * @author Timm Felden
     */
    public static final class GeneratorBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.Generator> {

        protected GeneratorBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.Generator, ? super de.unistuttgart.iste.ps.skillls.tools.Generator> pool, de.unistuttgart.iste.ps.skillls.tools.Generator instance) {
            super(pool, instance);
        }

        public GeneratorBuilder execEnv(java.lang.String execEnv) {
            instance.setExecEnv(execEnv);
            return this;
        }

        public GeneratorBuilder path(java.lang.String path) {
            instance.setPath(path);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Generator, de.unistuttgart.iste.ps.skillls.tools.Generator> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Generator.SubType, de.unistuttgart.iste.ps.skillls.tools.Generator> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.Generator.SubType, de.unistuttgart.iste.ps.skillls.tools.Generator> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Generator.SubType, de.unistuttgart.iste.ps.skillls.tools.Generator> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skillls.tools.Generator[] data = ((GeneratorAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skillls.tools.Generator.SubType r = new de.unistuttgart.iste.ps.skillls.tools.Generator.SubType(this, i + 1);
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
