package com.mindee.cli;

import com.mindee.parsing.common.Inference;
import java.io.File;
import java.io.IOException;
import picocli.CommandLine;

/**
 * Interface to process CLI products.
 */
@CommandLine.Command(
    name = "CLI",
    scope = CommandLine.ScopeType.INHERIT,
    subcommands = {CommandLine.HelpCommand.class},
    description = "Invoke Off The Shelf API for invoice, receipt, and passports"
)
public interface ProductProcessor {
  /**
   * @param productClass Product class to be processed for synchronous products.
   * @param file Input file.
   * @param <T> Type of the product.
   * @return A string representation of the result of the parsing.
   * @throws IOException Throws if the parsing goes wrong.
   */
  <T extends Inference<?, ?>> String standardProductOutput(Class<T> productClass, File file) throws IOException;

  /**
   * @param productClass Product class to be processed for asynchronous products.
   * @param file Input file.
   * @param <T> Type of the product.
   * @return A string representation of the result of the parsing.
   * @throws IOException Throws if the parsing goes wrong.
   * @throws InterruptedException Throws if the polling is interrupted.
   */
  <T extends Inference<?, ?>> String standardProductAsyncOutput(Class<T> productClass, File file) throws IOException, InterruptedException;
}
