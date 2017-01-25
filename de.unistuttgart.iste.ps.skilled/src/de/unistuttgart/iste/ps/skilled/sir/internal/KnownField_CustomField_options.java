/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.io.IOException;
import java.util.Iterator;

import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.*;
import de.ust.skill.common.java.internal.fieldTypes.Annotation;
import de.ust.skill.common.java.internal.fieldTypes.MapType;
import de.ust.skill.common.java.internal.fieldTypes.SingleArgumentType;
import de.ust.skill.common.java.internal.fieldTypes.StringType;
import de.ust.skill.common.java.internal.fieldTypes.V64;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.internal.parts.Chunk;
import de.ust.skill.common.java.internal.parts.SimpleChunk;
import de.ust.skill.common.java.iterators.IterableArrayView;
import de.ust.skill.common.jvm.streams.MappedInStream;
import de.ust.skill.common.jvm.streams.MappedOutStream;


/**
 * customfieldoption[] CustomField.options
 */
final class KnownField_CustomField_options extends FieldDeclaration<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>, de.unistuttgart.iste.ps.skilled.sir.CustomField> implements
               KnownField<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>, de.unistuttgart.iste.ps.skilled.sir.CustomField> {

    public KnownField_CustomField_options(FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>> type, int index, CustomFieldAccess owner) {
        super(type, "options", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.CustomField> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((CustomFieldAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setOptions(type.readSingleField(in));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final SingleArgumentType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>, de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> t = (SingleArgumentType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>, de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>) type;

        final FieldType<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> baseType = t.groundType;
        final de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final java.util.ArrayList<? extends de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> v = (java.util.ArrayList<? extends de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>)((de.unistuttgart.iste.ps.skilled.sir.CustomField)data[i]).getOptions();
            if(null==v || v.isEmpty())
                result++;
            else {
                result += V64.singleV64Offset(v.size());
                result += baseType.calculateOffset((java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption>)v);
            }
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess) owner.basePool()).data();
        int i;
        final int high;

        final Chunk last = dataChunks.getLast().c;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            i = (int) c.bpo;
            high = (int) (c.bpo + c.count);
        } else {
            i = owner.size() > 0 ? (int) owner.iterator().next().getSkillID() - 1 : 0;
            high = i + owner.size();
        }

        for (; i < high; i++) {
            type.writeSingleField(((de.unistuttgart.iste.ps.skilled.sir.CustomField)data[i]).getOptions(), out);
        }
    }

    @Override
    public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.CustomField) ref).getOptions();
    }

    @Override
    public void setR(SkillObject ref, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> value) {
        ((de.unistuttgart.iste.ps.skilled.sir.CustomField) ref).setOptions(value);
    }

    @Override
    public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> get(de.unistuttgart.iste.ps.skilled.sir.CustomField ref) {
        return ref.getOptions();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.CustomField ref, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CustomFieldOption> value) {
        ref.setOptions(value);
    }
}
