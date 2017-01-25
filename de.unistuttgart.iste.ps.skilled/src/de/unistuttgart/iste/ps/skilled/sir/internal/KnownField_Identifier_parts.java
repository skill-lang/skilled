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
 * string[] Identifier.parts
 */
final class KnownField_Identifier_parts extends FieldDeclaration<java.util.ArrayList<java.lang.String>, de.unistuttgart.iste.ps.skilled.sir.Identifier> implements
               KnownField<java.util.ArrayList<java.lang.String>, de.unistuttgart.iste.ps.skilled.sir.Identifier> {

    public KnownField_Identifier_parts(FieldType<java.util.ArrayList<java.lang.String>> type, int index, IdentifierAccess owner) {
        super(type, "parts", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.Identifier> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((IdentifierAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setParts(type.readSingleField(in));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final SingleArgumentType<java.util.ArrayList<java.lang.String>, java.lang.String> t = (SingleArgumentType<java.util.ArrayList<java.lang.String>, java.lang.String>) type;

        final FieldType<java.lang.String> baseType = t.groundType;
        final de.unistuttgart.iste.ps.skilled.sir.Identifier[] data = ((IdentifierAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final java.util.ArrayList<? extends java.lang.String> v = (java.util.ArrayList<? extends java.lang.String>)(data[i]).getParts();
            if(null==v || v.isEmpty())
                result++;
            else {
                result += V64.singleV64Offset(v.size());
                result += baseType.calculateOffset((java.util.ArrayList<java.lang.String>)v);
            }
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skilled.sir.Identifier[] data = ((IdentifierAccess) owner.basePool()).data();
        int i;
        final int high;

        final Chunk last = dataChunks.getLast().c;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            i = (int) c.bpo;
            high = (int) (c.bpo + c.count);
        } else {
            i = 0;
            high = owner.size();
        }

        for (; i < high; i++) {
            type.writeSingleField(data[i].getParts(), out);
        }
    }

    @Override
    public java.util.ArrayList<java.lang.String> getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.Identifier) ref).getParts();
    }

    @Override
    public void setR(SkillObject ref, java.util.ArrayList<java.lang.String> value) {
        ((de.unistuttgart.iste.ps.skilled.sir.Identifier) ref).setParts(value);
    }

    @Override
    public java.util.ArrayList<java.lang.String> get(de.unistuttgart.iste.ps.skilled.sir.Identifier ref) {
        return ref.getParts();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.Identifier ref, java.util.ArrayList<java.lang.String> value) {
        ref.setParts(value);
    }
}
