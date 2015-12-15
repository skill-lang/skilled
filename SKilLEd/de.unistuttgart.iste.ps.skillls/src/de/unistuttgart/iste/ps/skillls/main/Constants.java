package de.unistuttgart.iste.ps.skillls.main;

/**
 * Class for saving constant variables like HELPTEXT
 *
 * @author Armin Hüneburg
 * @since 07.12.15
 */
public class Constants {
    /**
     * Text for the help message. Called with "skillls --help".
     */
    static final String HELPTEXT = "SYNOPSIS\n" + "       skillls\n" + "       [-a]  [--all]\n"
            + "       [-g  GENERATOR]  [--generator  GENERATOR]\n" + "       [-l  LANGUAGE] [--lang LANGUAGE]\n"
            + "       [-ls] [--list]\n" + "       [-o OUTPUT] [--output OUTPUT]\n" + "       [-x EXEC] [--exec EXEC]\n"
            + "       [-m MODULE] [--module MODULE]\n" + "       [-p PATH] [--path PATH]\n" + "       [TOOLS...]\n\n"
            + "DESCRIPTION\n" + "       SKilLls generates or lists tools with the given generator.\n"
            + "       It can also list all tools.\n\n" + "OPTIONS\n" + "       -a, --all\n"
            + "              When used with -ls/--list, lists all tools not regarding\n"
            + "              changes.  When used without -ls/--list, generates all\n"
            + "              tools not regarding changes.\n\n" + "       -g, --generator GENERATOR\n"
            + "              Needed for generating bindings. Path to the generator\n"
            + "              that is being used. Ignored with -ls/--list\n\n" + "       -l, --lang LANGUAGE\n"
            + "              Language the binding should be generated for.\n" + "              Ignored with -ls/--list.\n\n"
            + "       -o, --output OUTPUT\n" + "              The output directory for the binding.\n"
            + "              Ignored with -ls/--list.\n\n" + "       -x, --exec EXEC\n"
            + "              The execution environment for the generator,\n"
            + "              e.g. scala. Ignored with -ls/--list.\n\n" + "       -ls, --list\n"
            + "              Does not generate bindings but lists the tools.\n\n" + "       -m, --module MODULE\n"
            + "              The module the binding should be added to.\n" + "              Ignored with -ls/--list.\n\n"
            + "       -p, --path PATH\n" + "              The path the project is located at.\n\n" + "       TOOLS...\n"
            + "              The tools the options should be applied to.\n"
            + "              If no tools are given the options are applied to all\n" + "              available tools.\n\n\n"
            + "SIDE NOTE\n" + "       Single letter arguments can be combined. You can call SKilLls\n"
            + "       with following command:\n" + "       skillls -agl /path/to/generator Java /path/to/project\n"
            + "       This command generates all bindings for the tools of the project\n"
            + "       in Java. If l comes before g the language has to be given first.\n"
            + "       If no options are given the settings, stored in\n" + "       /path/to/project/.skills, are used.\n";
}
