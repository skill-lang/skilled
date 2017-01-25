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
 * bool FilePath.isAbsolut
 */
final class KnownField_FilePath_isAbsolut extends FieldDeclaration<java.lang.Boolean, de.unistuttgart.iste.ps.skilled.sir.FilePath> implements
               KnownBooleanField<de.unistuttgart.iste.ps.skilled.sir.FilePath> {

    public KnownField_FilePath_isAbsolut(FieldType<java.lang.Boolean> type, int index, FilePathAccess owner) {
        super(type, "isabsolut", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.FilePath> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((FilePathAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setIsAbsolut(in.bool());
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        return range.count;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skilled.sir.FilePath[] data = ((FilePathAccess) owner.basePool()).data();
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
            out.bool(data[i].getIsAbsolut());
        }
    }

    @Override
    public java.lang.Boolean getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.FilePath) ref).getIsAbsolut();
    }

    @Override
    public void setR(SkillObject ref, java.lang.Boolean value) {
        ((de.unistuttgart.iste.ps.skilled.sir.FilePath) ref).setIsAbsolut(value);
    }

    @Override
    public boolean get(de.unistuttgart.iste.ps.skilled.sir.FilePath ref) {
        return ref.getIsAbsolut();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.FilePath ref, boolean value) {
        ref.setIsAbsolut(value);
    }
}
