package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.grammar.SKilLParser;
import de.unistuttgart.iste.ps.skillls.grammar.SKilLParserBaseListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * Created on 13.10.15.
 *
 * @author Armin Hüneburg
 */
public class SkillExtractListener extends SKilLParserBaseListener {
    private FileOutputStream outFile;
    private File inFile;
    private Tool tool;

    /**
     * Constructor.
     * Sets the values of the instance attributes.
     * @param outFile the file the extraction should be places in.
     * @param inFile the file that should be parsed.
     * @param tool tool that contains the data for the specifications and tools.
     */
    public SkillExtractListener(FileOutputStream outFile, File inFile, Tool tool) {
        this.outFile = outFile;
        this.inFile = inFile;
        this.tool = tool;
    }

    /**
     * Method called when the complete file is parsed.
     * @param fileContext The parsed content of the file.
     */
    @Override
    public void exitFile(SKilLParser.FileContext fileContext) {
        try {
        	de.unistuttgart.iste.ps.skillls.tools.File file = null;
            for (de.unistuttgart.iste.ps.skillls.tools.File f : tool.getFiles()) {
                if (Paths.get(f.getPath()).relativize(Paths.get(inFile.getAbsolutePath())).toString().isEmpty()) {
                    file = f;
                    break;
                }
            }
            for (TerminalNode commentLine : fileContext.header().HEAD_COMMENT()) {
                outFile.write(commentLine.getSymbol().getText().getBytes());
            }
            for (SKilLParser.IncludeContext include : fileContext.header().include()) {
                StringBuilder line = new StringBuilder();
                line.append("\n");
                line.append(include.includeWord().getText());
                line.append(" ");
                for (TerminalNode i : include.StringLiteral()) {
                    String text = i.getSymbol().getText().substring(1, i.getSymbol().getText().length() - 1);
                    if (file == null)
                        return;
                    for (String dep : file.getDependencies()) {
                        if (dep.endsWith(text)) {
                            line.append(i.getSymbol().getText());
                            line.append(" ");
                        }
                    }
                }
                if (line.toString().trim().endsWith(include.includeWord().getText())) {
                    line.setLength(line.length() - include.includeWord().getText().length() - 1);
                }
                line.append("\n");
                outFile.write(line.toString().getBytes());
            }
            fileContext.declaration().forEach((ctx) -> {
                try {
                    processDeclaration(ctx);
                } catch (IOException e) {
                    ExceptionHandler.handle(e, "Temporary File could not be created. File: " + inFile.getName());
                }
            });
        } catch (IOException e) {
            ExceptionHandler.handle(e, "Temporary File could not be created. File: " + inFile.getName());
        }
    }

    /**
     * Decides which processing method for a typedeclaration is to be used.
     * @param ctx The context of the declaration.
     * @throws IOException thrown if a exception occurred while writing a type to the outfile.
     */
    private void processDeclaration(SKilLParser.DeclarationContext ctx) throws IOException {
        if (ctx.interfacetype() != null) {
            processInterface(ctx.interfacetype());
        } else if (ctx.usertype() != null) {
            processUsertype(ctx.usertype());
        } else if (ctx.enumtype() != null) {
            processEnumtype(ctx.enumtype());
        } else if (ctx.typedef() != null) {
            processTypedef(ctx.typedef());
        }
    }

    /**
     * Processes a typedef typedeclaration.
     * @param ctx the context of the typedef.
     * @throws IOException thrown if an exception occurs while writing to the outfile.
     */
    private void processTypedef(SKilLParser.TypedefContext ctx) throws IOException {
        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().toLowerCase().indexOf(ctx.name.getText().toLowerCase()) == "typedef ".length()) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(type.getComment());
        builder.append('\n');
        builder.append(type.getName());
        StringBuilder hintsRestrictions = new StringBuilder(0);
        for (Hint hint : type.getTypeHints()) {
            hintsRestrictions.append(hint.getName());
            hintsRestrictions.append('\n');
        }
        for (String restriction : type.getRestrictions()) {
            hintsRestrictions.append(restriction);
            hintsRestrictions.append('\n');
        }
        for (String extend : type.getExtends()) {
            builder.append(type.getExtends());
            builder.append(extend);
        }
        builder.append(";\n\n");
        outFile.write(String.format(builder.toString(), hintsRestrictions.length() == 0 ? "" : hintsRestrictions.toString())
                .getBytes());
    }

    /**
     * Processes an enumtype typedeclaration.
     * @param ctx The context of the typedeclaration.
     * @throws IOException thrown if an error occurs while writing to outfile.
     */
    private void processEnumtype(SKilLParser.EnumtypeContext ctx) throws IOException {
        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().toLowerCase().endsWith(ctx.name.getText().toLowerCase())) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(type.getComment());
        for (Hint hint : type.getTypeHints()) {
            builder.append(hint.getName());
            builder.append('\n');
        }
        for (String restriction : type.getRestrictions()) {
            builder.append(restriction);
            builder.append('\n');
        }
        builder.append(type.getName());
        for (String extend : type.getExtends()) {
            builder.append(" extends ");
            builder.append(extend);
        }
        builder.append(" {\n");
        for (Field field : type.getFields()) {
            builder.append("  ");
            builder.append(field.getComment());
            builder.append('\n');
            for (Hint hint : field.getFieldHints()) {
                builder.append("  ");
                builder.append(hint.getName());
                builder.append('\n');
            }
            for (String restriction : field.getRestrictions()) {
                builder.append("  ");
                builder.append(restriction);
                builder.append('\n');
            }
            builder.append("  ");
            builder.append(field.getName());
            builder.append(";\n");
        }
        builder.append("}\n\n");
        outFile.write(builder.toString().getBytes());
    }

    /**
     * Processes a usertype typedeclaration.
     * @param ctx The context of the typedeclaration.
     * @throws IOException Thrown when an exception occurs while writing to outfile.
     */
    private void processUsertype(SKilLParser.UsertypeContext ctx) throws IOException {
        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().toLowerCase().equals(ctx.name.getText().toLowerCase())) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(type.getComment());
        for (Hint hint : type.getTypeHints()) {
            builder.append(hint.getName());
            builder.append('\n');
        }
        for (String restriction : type.getRestrictions()) {
            builder.append(restriction);
            builder.append('\n');
        }
        builder.append(type.getName());
        for (SKilLParser.ExtensionContext extend : ctx.extension()) {
            builder.append(' ');
            builder.append(extend.extendWord().getText());
            builder.append(' ');
            builder.append(extend.Identifier().getSymbol().getText());
        }
        builder.append(" {\n");
        for (Field field : type.getFields()) {
            builder.append("  ");
            builder.append(field.getComment());
            builder.append('\n');
            for (Hint hint : field.getFieldHints()) {
                builder.append("  ");
                builder.append(hint.getName());
                builder.append('\n');
            }
            for (String restriction : field.getRestrictions()) {
                builder.append("  ");
                builder.append(restriction);
                builder.append('\n');
            }
            builder.append("  ");
            builder.append(field.getName());
            builder.append(";\n");
        }
        builder.append("}\n\n");
        outFile.write(builder.toString().getBytes());
    }

    /**
     * Processes an interfacetype typedeclaration.
     * @param ctx The context of the typedeclaration.
     * @throws IOException thrown if an exception occurs while writing to outfile.
     */
    private void processInterface(SKilLParser.InterfacetypeContext ctx) throws IOException {
        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().toLowerCase().endsWith(ctx.name.getText().toLowerCase())) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(type.getComment());
        for (Hint hint : type.getTypeHints()) {
            builder.append(hint.getName());
            builder.append('\n');
        }
        for (String restriction : type.getRestrictions()) {
            builder.append(restriction);
            builder.append('\n');
        }
        builder.append(type.getName());
        for (SKilLParser.ExtensionContext extend : ctx.extension()) {
            builder.append(extend.extendWord().getText());
            builder.append(' ');
            builder.append(extend.Identifier().getSymbol().getText());
        }
        builder.append(" {\n");
        for (Field field : type.getFields()) {
            builder.append("  ");
            builder.append(field.getComment());
            builder.append('\n');
            for (Hint hint : field.getFieldHints()) {
                builder.append("  ");
                builder.append(hint.getName());
                builder.append('\n');
            }
            for (String restriction : field.getRestrictions()) {
                builder.append("  ");
                builder.append(restriction);
                builder.append('\n');
            }
            builder.append("  ");
            builder.append(field.getName());
            builder.append(";\n");
        }
        builder.append("}\n\n");
        outFile.write(builder.toString().getBytes());
    }
}