/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import de.unistuttgart.iste.ps.skillls.tools.internal.SkillState;
import de.ust.skill.common.java.api.SkillException;

/**
 * An abstract skill file that is hiding all the dirty implementation details
 * from you.
 *
 * @author Timm Felden
 */
public interface SkillFile extends de.ust.skill.common.java.api.SkillFile {

    /**
     * Create a new skill file based on argument path and mode.
     *
     * @throws IOException
     *             on IO and mode related errors
     * @throws SkillException
     *             on file or specification consistency errors
     */
    public static SkillFile open(String path, Mode... mode) throws IOException, SkillException {
        return SkillState.open(path, mode);
    }

    /**
     * Create a new skill file based on argument path and mode.
     *
     * @throws IOException
     *             on IO and mode related errors
     * @throws SkillException
     *             on file or specification consistency errors
     */
    public static SkillFile open(File path, Mode... mode) throws IOException, SkillException {
        return SkillState.open(path, mode);
    }

    /**
     * Create a new skill file based on argument path and mode.
     *
     * @throws IOException
     *             on IO and mode related errors
     * @throws SkillException
     *             on file or specification consistency errors
     */
    public static SkillFile open(Path path, Mode... mode) throws IOException, SkillException {
        return SkillState.open(path, mode);
    }

    /**
     * @return an access for all Files in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.FileAccess Files();

    /**
     * @return an access for all Generators in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.GeneratorAccess Generators();

    /**
     * @return an access for all Hints in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.HintAccess Hints();

    /**
     * @return an access for all HintParents in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.HintParentAccess HintParents();

    /**
     * @return an access for all Fields in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.FieldAccess Fields();

    /**
     * @return an access for all Types in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.TypeAccess Types();

    /**
     * @return an access for all Tools in this state
     */
    public de.unistuttgart.iste.ps.skillls.tools.internal.ToolAccess Tools();
}
