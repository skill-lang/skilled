/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

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
 * string[] Field.restrictions
 */
final class KnownField_Field_restrictions extends FieldDeclaration<java.util.ArrayList<java.lang.String>, de.unistuttgart.iste.ps.skillls.tools.Field> implements
               KnownField<java.util.ArrayList<java.lang.String>, de.unistuttgart.iste.ps.skillls.tools.Field> {

    public KnownField_Field_restrictions(FieldType<java.util.ArrayList<java.lang.String>> type, int index, FieldAccess owner) {
        super(type, "restrictions", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skillls.tools.Field> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((FieldAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setRestrictions(type.readSingleField(in));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final SingleArgumentType<java.util.ArrayList<java.lang.String>, java.lang.String> t = (SingleArgumentType<java.util.ArrayList<java.lang.String>, java.lang.String>) type;
        final FieldType<java.lang.String> baseType = t.groundType;
        final de.unistuttgart.iste.ps.skillls.tools.HintParent[] data = ((HintParentAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final java.util.ArrayList<java.lang.String> v = ((de.unistuttgart.iste.ps.skillls.tools.Field)data[i]).getRestrictions();
            if(null==v)
                result++;
            else {
                result += V64.singleV64Offset(v.size());
                result += baseType.calculateOffset(v);
            }
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skillls.tools.HintParent[] data = ((HintParentAccess) owner.basePool()).data();
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
            type.writeSingleField(((de.unistuttgart.iste.ps.skillls.tools.Field)data[i]).getRestrictions(), out);
        }
    }

    @Override
    public java.util.ArrayList<java.lang.String> getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skillls.tools.Field) ref).getRestrictions();
    }

    @Override
    public void setR(SkillObject ref, java.util.ArrayList<java.lang.String> value) {
        ((de.unistuttgart.iste.ps.skillls.tools.Field) ref).setRestrictions(value);
    }

    @Override
    public java.util.ArrayList<java.lang.String> get(de.unistuttgart.iste.ps.skillls.tools.Field ref) {
        return ref.getRestrictions();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skillls.tools.Field ref, java.util.ArrayList<java.lang.String> value) {
        ref.setRestrictions(value);
    }
}