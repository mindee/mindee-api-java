package com.mindee;

import com.mindee.v1.cli.CommandLineInterfaceProducts;
import com.mindee.v1.cli.ProductProcessor;
import com.mindee.v2.cli.ClassificationCommand;
import com.mindee.v2.cli.CropCommand;
import com.mindee.v2.cli.ExtractionCommand;
import com.mindee.v2.cli.OcrCommand;
import com.mindee.v2.cli.SearchModelsCommand;
import com.mindee.v2.cli.SplitCommand;
import java.lang.reflect.Method;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

/**
 * Top-level Mindee CLI entry point.
 * <p>
 * V2 commands (search-models, classification, crop, extraction, ocr, split) are available at the
 * root level. V1 product commands are available under the {@code v1} subcommand.
 */
@Command(
    name = "mindee",
    description = "Mindee API client",
    mixinStandardHelpOptions = true,
    subcommands = { CommandLine.HelpCommand.class }
)
public class CommandLineInterface implements Runnable {

  @Spec
  private CommandSpec spec;

  /**
   * Main entry point for the Mindee CLI.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    CommandLineInterface root = new CommandLineInterface();
    CommandLine rootCmd = new CommandLine(root);

    // V2 commands at root
    rootCmd.addSubcommand("search-models", new CommandLine(new SearchModelsCommand()));
    rootCmd.addSubcommand("classification", new CommandLine(new ClassificationCommand()));
    rootCmd.addSubcommand("crop", new CommandLine(new CropCommand()));
    rootCmd.addSubcommand("extraction", new CommandLine(new ExtractionCommand()));
    rootCmd.addSubcommand("ocr", new CommandLine(new OcrCommand()));
    rootCmd.addSubcommand("split", new CommandLine(new SplitCommand()));

    // V1 commands under the "v1" subcommand
    var v1Cli = new com.mindee.v1.CommandLineInterface();
    var v1Cmd = new CommandLine(v1Cli);
    v1Cmd.getCommandSpec().name("v1");
    v1Cmd.getCommandSpec().usageMessage().description("Mindee V1 product commands.");
    var products = new CommandLineInterfaceProducts((ProductProcessor) v1Cli);
    for (Method method : CommandLineInterfaceProducts.class.getDeclaredMethods()) {
      if (method.isAnnotationPresent(CommandLine.Command.class)) {
        CommandLine.Command annotation = method.getAnnotation(CommandLine.Command.class);
        String subcommandName = annotation.name();
        var subCmd = new CommandLine(
          new com.mindee.v1.CommandLineInterface.ProductCommandHandler(products, method)
        );
        subCmd.getCommandSpec().usageMessage().description(annotation.description());
        v1Cmd.addSubcommand(subcommandName, subCmd);
      }
    }
    rootCmd.addSubcommand("v1", v1Cmd);

    System.exit(rootCmd.execute(args));
  }

  @Override
  public void run() {
    spec.commandLine().usage(System.out);
  }
}
