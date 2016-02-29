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

public class FileAccess extends BasePool<de.unistuttgart.iste.ps.skillls.tools.File> {

    @Override
    final protected de.unistuttgart.iste.ps.skillls.tools.File[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skillls.tools.File[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    FileAccess(int poolIndex) {
        super(poolIndex, "file", new HashSet<String>(Arrays.asList(new String[] { "dependencies", "header", "md5", "path", "timestamp" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skillls.tools.File[] data() {
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

            de.unistuttgart.iste.ps.skillls.tools.File r = new de.unistuttgart.iste.ps.skillls.tools.File(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.File> f;
        switch (name) {
        case "dependencies":
            f = new KnownField_File_dependencies(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        case "header":
            f = new KnownField_File_header(string, 1 + dataFields.size(), this);
            break;

        case "md5":
            f = new KnownField_File_md5(string, 1 + dataFields.size(), this);
            break;

        case "path":
            f = new KnownField_File_path(string, 1 + dataFields.size(), this);
            break;

        case "timestamp":
            f = new KnownField_File_timestamp(string, 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File> f;
        switch (name) {
        case "dependencies":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File>) new KnownField_File_dependencies((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        case "header":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File>) new KnownField_File_header((FieldType<java.lang.String>) type, ID, this);
            break;

        case "md5":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File>) new KnownField_File_md5((FieldType<java.lang.String>) type, ID, this);
            break;

        case "path":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File>) new KnownField_File_path((FieldType<java.lang.String>) type, ID, this);
            break;

        case "timestamp":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.File>) new KnownField_File_timestamp((FieldType<java.lang.String>) type, ID, this);
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
     * @return a new File instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.File make() {
        de.unistuttgart.iste.ps.skillls.tools.File rval = new de.unistuttgart.iste.ps.skillls.tools.File();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.File make(java.util.ArrayList<java.lang.String> dependencies, java.lang.String header, java.lang.String md5, java.lang.String path, java.lang.String timestamp) {
        de.unistuttgart.iste.ps.skillls.tools.File rval = new de.unistuttgart.iste.ps.skillls.tools.File(-1, dependencies, header, md5, path, timestamp);
        add(rval);
        return rval;
    }

    public FileBuilder build() {
        return new FileBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.File());
    }

    /**
     * Builder for new File instances.
     *
     * @author Timm Felden
     */
    public static final class FileBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.File> {

        protected FileBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.File, ? super de.unistuttgart.iste.ps.skillls.tools.File> pool, de.unistuttgart.iste.ps.skillls.tools.File instance) {
            super(pool, instance);
        }

        public FileBuilder dependencies(java.util.ArrayList<java.lang.String> dependencies) {
            instance.setDependencies(dependencies);
            return this;
        }

        public FileBuilder header(java.lang.String header) {
            instance.setHeader(header);
            return this;
        }

        public FileBuilder md5(java.lang.String md5) {
            instance.setMd5(md5);
            return this;
        }

        public FileBuilder path(java.lang.String path) {
            instance.setPath(path);
            return this;
        }

        public FileBuilder timestamp(java.lang.String timestamp) {
            instance.setTimestamp(timestamp);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.File, de.unistuttgart.iste.ps.skillls.tools.File> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.File.SubType, de.unistuttgart.iste.ps.skillls.tools.File> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.File.SubType, de.unistuttgart.iste.ps.skillls.tools.File> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.File.SubType, de.unistuttgart.iste.ps.skillls.tools.File> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skillls.tools.File[] data = ((FileAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skillls.tools.File.SubType r = new de.unistuttgart.iste.ps.skillls.tools.File.SubType(this, i + 1);
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
