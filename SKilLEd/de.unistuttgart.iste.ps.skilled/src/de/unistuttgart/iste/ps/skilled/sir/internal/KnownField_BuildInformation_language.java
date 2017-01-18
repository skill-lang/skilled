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
 * string BuildInformation.language
 */
final class KnownField_BuildInformation_language extends FieldDeclaration<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> implements
               KnownField<java.lang.String, de.unistuttgart.iste.ps.skilled.sir.BuildInformation> {

    public KnownField_BuildInformation_language(FieldType<java.lang.String> type, int index, BuildInformationAccess owner) {
        super(type, "language", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.BuildInformation> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((BuildInformationAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        final StringPool sp = (StringPool)owner.owner().Strings();
        int count = (int) last.count;
        while (0 != count--) {
            is.next().setLanguage(sp.get(in.v64()));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final StringType t = (StringType) type;
        final de.unistuttgart.iste.ps.skilled.sir.BuildInformation[] data = ((BuildInformationAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            String v = (data[i]).getLanguage();
            if(null==v)
                result++;
            else
                result += t.singleOffset(v);
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skilled.sir.BuildInformation[] data = ((BuildInformationAccess) owner.basePool()).data();
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
            type.writeSingleField(data[i].getLanguage(), out);
        }
    }

    @Override
    public java.lang.String getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.BuildInformation) ref).getLanguage();
    }

    @Override
    public void setR(SkillObject ref, java.lang.String value) {
        ((de.unistuttgart.iste.ps.skilled.sir.BuildInformation) ref).setLanguage(value);
    }

    @Override
    public java.lang.String get(de.unistuttgart.iste.ps.skilled.sir.BuildInformation ref) {
        return ref.getLanguage();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.BuildInformation ref, java.lang.String value) {
        ref.setLanguage(value);
    }
}
