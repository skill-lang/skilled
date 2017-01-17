/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import de.unistuttgart.iste.ps.skilled.sir.internal.SkillState;
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
     * @return an access for all BuildInformations in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.BuildInformationAccess BuildInformations();

    /**
     * @return an access for all Comments in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.CommentAccess Comments();

    /**
     * @return an access for all CommentTags in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.CommentTagAccess CommentTags();

    /**
     * @return an access for all CustomFieldOptions in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.CustomFieldOptionAccess CustomFieldOptions();

    /**
     * @return an access for all FieldLikes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.FieldLikeAccess FieldLikes();

    /**
     * @return an access for all CustomFields in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.CustomFieldAccess CustomFields();

    /**
     * @return an access for all Fields in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.FieldAccess Fields();

    /**
     * @return an access for all FieldViews in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.FieldViewAccess FieldViews();

    /**
     * @return an access for all FilePaths in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.FilePathAccess FilePaths();

    /**
     * @return an access for all Hints in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.HintAccess Hints();

    /**
     * @return an access for all Identifiers in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.IdentifierAccess Identifiers();

    /**
     * @return an access for all Restrictions in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.RestrictionAccess Restrictions();

    /**
     * @return an access for all Tools in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.ToolAccess Tools();

    /**
     * @return an access for all ToolTypeCustomizations in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.ToolTypeCustomizationAccess ToolTypeCustomizations();

    /**
     * @return an access for all Types in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.TypeAccess Types();

    /**
     * @return an access for all BuiltinTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.BuiltinTypeAccess BuiltinTypes();

    /**
     * @return an access for all MapTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.MapTypeAccess MapTypes();

    /**
     * @return an access for all SingleBaseTypeContainers in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.SingleBaseTypeContainerAccess SingleBaseTypeContainers();

    /**
     * @return an access for all ConstantLengthArrayTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.ConstantLengthArrayTypeAccess ConstantLengthArrayTypes();

    /**
     * @return an access for all SimpleTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.SimpleTypeAccess SimpleTypes();

    /**
     * @return an access for all ConstantIntegers in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.ConstantIntegerAccess ConstantIntegers();

    /**
     * @return an access for all UserdefinedTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.UserdefinedTypeAccess UserdefinedTypes();

    /**
     * @return an access for all ClassTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.ClassTypeAccess ClassTypes();

    /**
     * @return an access for all EnumTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.EnumTypeAccess EnumTypes();

    /**
     * @return an access for all InterfaceTypes in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.InterfaceTypeAccess InterfaceTypes();

    /**
     * @return an access for all TypeDefinitions in this state
     */
    public de.unistuttgart.iste.ps.skilled.sir.internal.TypeDefinitionAccess TypeDefinitions();
}
