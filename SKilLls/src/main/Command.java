package main;

/**
 * @author Armin Hüneburg
 * @since 01.09.15.
 *
 * ENUM for the different commands the editor can do.
 * These commands are only for editing tools.
 */
public enum Command {
    delete,             //Index = 0
    rename,             //1
    addType,            //2
    removeType,         //3
    addField,           //4
    removeField,        //5
    addFieldHint,       //6
    removeFieldHint,    //7
    addTypeHint,        //8
    removeTypeHint,     //9
    setDefaults         //10
}
