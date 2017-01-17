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
 *  Base of type and field restrictions.
 */
public class RestrictionAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.Restriction> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.Restriction[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.Restriction[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    RestrictionAccess(int poolIndex) {
        super(poolIndex, "restriction", new HashSet<String>(Arrays.asList(new String[] { "arguments", "name" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.Restriction[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.Restriction r = new de.unistuttgart.iste.ps.skilled.sir.Restriction(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.Restriction> f;
        switch (name) {
        case "arguments":
            f = new KnownField_Restriction_arguments(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        case "name":
            f = new KnownField_Restriction_name(string, 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Restriction> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Restriction> f;
        switch (name) {
        case "arguments":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Restriction>) new KnownField_Restriction_arguments((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Restriction>) new KnownField_Restriction_name((FieldType<java.lang.String>) type, ID, this);
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
     * @return a new Restriction instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.Restriction make() {
        de.unistuttgart.iste.ps.skilled.sir.Restriction rval = new de.unistuttgart.iste.ps.skilled.sir.Restriction();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.Restriction make(java.util.ArrayList<java.lang.String> arguments, java.lang.String name) {
        de.unistuttgart.iste.ps.skilled.sir.Restriction rval = new de.unistuttgart.iste.ps.skilled.sir.Restriction(-1, arguments, name);
        add(rval);
        return rval;
    }

    public RestrictionBuilder build() {
        return new RestrictionBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.Restriction());
    }

    /**
     * Builder for new Restriction instances.
     *
     * @author Timm Felden
     */
    public static final class RestrictionBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.Restriction> {

        protected RestrictionBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.Restriction, ? super de.unistuttgart.iste.ps.skilled.sir.Restriction> pool, de.unistuttgart.iste.ps.skilled.sir.Restriction instance) {
            super(pool, instance);
        }

        public RestrictionBuilder arguments(java.util.ArrayList<java.lang.String> arguments) {
            instance.setArguments(arguments);
            return this;
        }

        public RestrictionBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.Restriction make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.Restriction rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Restriction, de.unistuttgart.iste.ps.skilled.sir.Restriction> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.Restriction.SubType, de.unistuttgart.iste.ps.skilled.sir.Restriction> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.Restriction.SubType, de.unistuttgart.iste.ps.skilled.sir.Restriction> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Restriction.SubType, de.unistuttgart.iste.ps.skilled.sir.Restriction> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.Restriction[] data = ((RestrictionAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.Restriction.SubType r = new de.unistuttgart.iste.ps.skilled.sir.Restriction.SubType(this, i + 1);
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
