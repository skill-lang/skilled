/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
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

public class BuildInformationAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.BuildInformation[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.BuildInformation[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    BuildInformationAccess(int poolIndex) {
        super(poolIndex, "buildinformation", new HashSet<String>(Arrays.asList(new String[] { "language", "options", "output" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.BuildInformation[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.BuildInformation r = new de.unistuttgart.iste.ps.skilled.sir.BuildInformation(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> f;
        switch (name) {
        case "language":
            f = new KnownField_BuildInformation_language(string, 1 + dataFields.size(), this);
            break;

        case "options":
            f = new KnownField_BuildInformation_options(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        case "output":
            f = new KnownField_BuildInformation_output((FieldType<de.unistuttgart.iste.ps.skilled.sir.FilePath>)(owner().poolByName().get("filepath")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> f;
        switch (name) {
        case "language":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.BuildInformation>) new KnownField_BuildInformation_language((FieldType<java.lang.String>) type, ID, this);
            break;

        case "options":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.BuildInformation>) new KnownField_BuildInformation_options((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        case "output":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.BuildInformation>) new KnownField_BuildInformation_output((FieldType<de.unistuttgart.iste.ps.skilled.sir.FilePath>) type, ID, this);
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
     * @return a new BuildInformation instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.BuildInformation make() {
        de.unistuttgart.iste.ps.skilled.sir.BuildInformation rval = new de.unistuttgart.iste.ps.skilled.sir.BuildInformation();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.BuildInformation make(java.lang.String language, java.util.ArrayList<java.lang.String> options, de.unistuttgart.iste.ps.skilled.sir.FilePath output) {
        de.unistuttgart.iste.ps.skilled.sir.BuildInformation rval = new de.unistuttgart.iste.ps.skilled.sir.BuildInformation(-1, language, options, output);
        add(rval);
        return rval;
    }

    public BuildInformationBuilder build() {
        return new BuildInformationBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.BuildInformation());
    }

    /**
     * Builder for new BuildInformation instances.
     *
     * @author Timm Felden
     */
    public static final class BuildInformationBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> {

        protected BuildInformationBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.BuildInformation, ? super de.unistuttgart.iste.ps.skilled.sir.BuildInformation> pool, de.unistuttgart.iste.ps.skilled.sir.BuildInformation instance) {
            super(pool, instance);
        }

        public BuildInformationBuilder language(java.lang.String language) {
            instance.setLanguage(language);
            return this;
        }

        public BuildInformationBuilder options(java.util.ArrayList<java.lang.String> options) {
            instance.setOptions(options);
            return this;
        }

        public BuildInformationBuilder output(de.unistuttgart.iste.ps.skilled.sir.FilePath output) {
            instance.setOutput(output);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.BuildInformation make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.BuildInformation rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.BuildInformation, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.BuildInformation.SubType, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.BuildInformation.SubType, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.BuildInformation.SubType, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.BuildInformation[] data = ((BuildInformationAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.BuildInformation.SubType r = new de.unistuttgart.iste.ps.skilled.sir.BuildInformation.SubType(this, i + 1);
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
