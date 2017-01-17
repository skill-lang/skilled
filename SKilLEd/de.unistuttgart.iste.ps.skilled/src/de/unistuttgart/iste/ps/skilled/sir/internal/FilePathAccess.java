/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.AutoField;
import de.ust.skill.common.java.internal.fieldTypes.*;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.restrictions.FieldRestriction;

/**
 *  a path that can be used in the description of a build process
 */
public class FilePathAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.FilePath> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.FilePath[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.FilePath[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    FilePathAccess(int poolIndex) {
        super(poolIndex, "filepath", new HashSet<String>(Arrays.asList(new String[] { "isabsolut", "parts" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.FilePath[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.FilePath r = new de.unistuttgart.iste.ps.skilled.sir.FilePath(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.FilePath> f;
        switch (name) {
        case "isabsolut":
            f = new KnownField_FilePath_isAbsolut(BoolType.get(), 1 + dataFields.size(), this);
            break;

        case "parts":
            f = new KnownField_FilePath_parts(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FilePath> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FilePath> f;
        switch (name) {
        case "isabsolut":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FilePath>) new KnownField_FilePath_isAbsolut((FieldType<java.lang.Boolean>) type, ID, this);
            break;

        case "parts":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FilePath>) new KnownField_FilePath_parts((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
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
     * @return a new FilePath instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.FilePath make() {
        de.unistuttgart.iste.ps.skilled.sir.FilePath rval = new de.unistuttgart.iste.ps.skilled.sir.FilePath();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.FilePath make(boolean isAbsolut, java.util.ArrayList<java.lang.String> parts) {
        de.unistuttgart.iste.ps.skilled.sir.FilePath rval = new de.unistuttgart.iste.ps.skilled.sir.FilePath(-1, isAbsolut, parts);
        add(rval);
        return rval;
    }

    public FilePathBuilder build() {
        return new FilePathBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.FilePath());
    }

    /**
     * Builder for new FilePath instances.
     *
     * @author Timm Felden
     */
    public static final class FilePathBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.FilePath> {

        protected FilePathBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.FilePath, ? super de.unistuttgart.iste.ps.skilled.sir.FilePath> pool, de.unistuttgart.iste.ps.skilled.sir.FilePath instance) {
            super(pool, instance);
        }

        public FilePathBuilder isAbsolut(boolean isAbsolut) {
            instance.setIsAbsolut(isAbsolut);
            return this;
        }

        public FilePathBuilder parts(java.util.ArrayList<java.lang.String> parts) {
            instance.setParts(parts);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.FilePath make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.FilePath rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.FilePath, de.unistuttgart.iste.ps.skilled.sir.FilePath> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.FilePath.SubType, de.unistuttgart.iste.ps.skilled.sir.FilePath> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.FilePath.SubType, de.unistuttgart.iste.ps.skilled.sir.FilePath> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.FilePath.SubType, de.unistuttgart.iste.ps.skilled.sir.FilePath> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.FilePath[] data = ((FilePathAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.FilePath.SubType r = new de.unistuttgart.iste.ps.skilled.sir.FilePath.SubType(this, i + 1);
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
