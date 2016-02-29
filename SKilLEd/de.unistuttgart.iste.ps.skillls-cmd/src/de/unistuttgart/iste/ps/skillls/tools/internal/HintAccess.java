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

public class HintAccess extends BasePool<de.unistuttgart.iste.ps.skillls.tools.Hint> {

    @Override
    final protected de.unistuttgart.iste.ps.skillls.tools.Hint[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skillls.tools.Hint[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    HintAccess(int poolIndex) {
        super(poolIndex, "hint", new HashSet<String>(Arrays.asList(new String[] { "name", "parent" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skillls.tools.Hint[] data() {
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

            de.unistuttgart.iste.ps.skillls.tools.Hint r = new de.unistuttgart.iste.ps.skillls.tools.Hint(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.Hint> f;
        switch (name) {
        case "name":
            f = new KnownField_Hint_name(string, 1 + dataFields.size(), this);
            break;

        case "parent":
            f = new KnownField_Hint_parent((FieldType<de.unistuttgart.iste.ps.skillls.tools.HintParent>)(owner().poolByName().get("hintparent")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Hint> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Hint> f;
        switch (name) {
        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Hint>) new KnownField_Hint_name((FieldType<java.lang.String>) type, ID, this);
            break;

        case "parent":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.Hint>) new KnownField_Hint_parent((FieldType<de.unistuttgart.iste.ps.skillls.tools.HintParent>) type, ID, this);
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
     * @return a new Hint instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Hint make() {
        de.unistuttgart.iste.ps.skillls.tools.Hint rval = new de.unistuttgart.iste.ps.skillls.tools.Hint();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.Hint make(java.lang.String name, de.unistuttgart.iste.ps.skillls.tools.HintParent parent) {
        de.unistuttgart.iste.ps.skillls.tools.Hint rval = new de.unistuttgart.iste.ps.skillls.tools.Hint(-1, name, parent);
        add(rval);
        return rval;
    }

    public HintBuilder build() {
        return new HintBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.Hint());
    }

    /**
     * Builder for new Hint instances.
     *
     * @author Timm Felden
     */
    public static final class HintBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.Hint> {

        protected HintBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.Hint, ? super de.unistuttgart.iste.ps.skillls.tools.Hint> pool, de.unistuttgart.iste.ps.skillls.tools.Hint instance) {
            super(pool, instance);
        }

        public HintBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        public HintBuilder parent(de.unistuttgart.iste.ps.skillls.tools.HintParent parent) {
            instance.setParent(parent);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Hint, de.unistuttgart.iste.ps.skillls.tools.Hint> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.Hint.SubType, de.unistuttgart.iste.ps.skillls.tools.Hint> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.Hint.SubType, de.unistuttgart.iste.ps.skillls.tools.Hint> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.Hint.SubType, de.unistuttgart.iste.ps.skillls.tools.Hint> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skillls.tools.Hint[] data = ((HintAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skillls.tools.Hint.SubType r = new de.unistuttgart.iste.ps.skillls.tools.Hint.SubType(this, i + 1);
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
