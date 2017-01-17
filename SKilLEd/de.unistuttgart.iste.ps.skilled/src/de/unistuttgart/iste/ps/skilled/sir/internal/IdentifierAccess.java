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
 *  A nicer version of a skill name that can be used to adapt to a target language naming convention.
 */
public class IdentifierAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.Identifier> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.Identifier[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.Identifier[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    IdentifierAccess(int poolIndex) {
        super(poolIndex, "identifier", new HashSet<String>(Arrays.asList(new String[] { "parts", "skillname" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.Identifier[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.Identifier r = new de.unistuttgart.iste.ps.skilled.sir.Identifier(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.Identifier> f;
        switch (name) {
        case "parts":
            f = new KnownField_Identifier_parts(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        case "skillname":
            f = new KnownField_Identifier_skillname(string, 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Identifier> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Identifier> f;
        switch (name) {
        case "parts":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Identifier>) new KnownField_Identifier_parts((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        case "skillname":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Identifier>) new KnownField_Identifier_skillname((FieldType<java.lang.String>) type, ID, this);
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
     * @return a new Identifier instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.Identifier make() {
        de.unistuttgart.iste.ps.skilled.sir.Identifier rval = new de.unistuttgart.iste.ps.skilled.sir.Identifier();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.Identifier make(java.util.ArrayList<java.lang.String> parts, java.lang.String skillname) {
        de.unistuttgart.iste.ps.skilled.sir.Identifier rval = new de.unistuttgart.iste.ps.skilled.sir.Identifier(-1, parts, skillname);
        add(rval);
        return rval;
    }

    public IdentifierBuilder build() {
        return new IdentifierBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.Identifier());
    }

    /**
     * Builder for new Identifier instances.
     *
     * @author Timm Felden
     */
    public static final class IdentifierBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.Identifier> {

        protected IdentifierBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.Identifier, ? super de.unistuttgart.iste.ps.skilled.sir.Identifier> pool, de.unistuttgart.iste.ps.skilled.sir.Identifier instance) {
            super(pool, instance);
        }

        public IdentifierBuilder parts(java.util.ArrayList<java.lang.String> parts) {
            instance.setParts(parts);
            return this;
        }

        public IdentifierBuilder skillname(java.lang.String skillname) {
            instance.setSkillname(skillname);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.Identifier make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.Identifier rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Identifier, de.unistuttgart.iste.ps.skilled.sir.Identifier> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.Identifier.SubType, de.unistuttgart.iste.ps.skilled.sir.Identifier> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.Identifier.SubType, de.unistuttgart.iste.ps.skilled.sir.Identifier> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Identifier.SubType, de.unistuttgart.iste.ps.skilled.sir.Identifier> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.Identifier[] data = ((IdentifierAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.Identifier.SubType r = new de.unistuttgart.iste.ps.skilled.sir.Identifier.SubType(this, i + 1);
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
