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
 * string Generator.path
 */
final class KnownField_Generator_path extends FieldDeclaration<java.lang.String, de.unistuttgart.iste.ps.skillls.tools.Generator> implements
               KnownField<java.lang.String, de.unistuttgart.iste.ps.skillls.tools.Generator> {

    public KnownField_Generator_path(FieldType<java.lang.String> type, int index, GeneratorAccess owner) {
        super(type, "path", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skillls.tools.Generator> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((GeneratorAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        final StringPool sp = (StringPool)owner.owner().Strings();
        int count = (int) last.count;
        while (0 != count--) {
            is.next().setPath(sp.get(in.v64()));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final StringType t = (StringType) type;
        final de.unistuttgart.iste.ps.skillls.tools.Generator[] data = ((GeneratorAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            String v = (data[i]).getPath();
            if(null==v)
                result++;
            else
                result += t.singleOffset(v);
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skillls.tools.Generator[] data = ((GeneratorAccess) owner.basePool()).data();
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
            type.writeSingleField(data[i].getPath(), out);
        }
    }

    @Override
    public java.lang.String getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skillls.tools.Generator) ref).getPath();
    }

    @Override
    public void setR(SkillObject ref, java.lang.String value) {
        ((de.unistuttgart.iste.ps.skillls.tools.Generator) ref).setPath(value);
    }

    @Override
    public java.lang.String get(de.unistuttgart.iste.ps.skillls.tools.Generator ref) {
        return ref.getPath();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skillls.tools.Generator ref, java.lang.String value) {
        ref.setPath(value);
    }
}
