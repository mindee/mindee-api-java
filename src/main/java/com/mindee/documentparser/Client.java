package com.mindee.documentparser;

import com.mindee.http.DocumentParsingHttpClient;
import com.mindee.http.Endpoint;
import com.mindee.http.MindeeHttpClient;
import com.mindee.model.documenttype.BaseDocumentResponse;
import com.mindee.utils.FileUtils;
import com.mindee.utils.PDFUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.Value;

public class Client {


  private final DocumentParsingHttpClient httpClient;
  private final Map<DocumentKey, DocumentConfig> configMap = new HashMap<>();
  private final Map<Class, DocumentConfig> mapByResponseTypes = new HashMap<>();
  private final String mindeeApiKey;


  public Client() {
    this.httpClient = new MindeeHttpClient();
    mindeeApiKey = DocumentConfigFactory.getApiKeyFromEnvironmentVariable();
    configureClient();
  }

  public Client(DocumentParsingHttpClient httpClient) {
    this.httpClient = httpClient;
    mindeeApiKey = DocumentConfigFactory.getApiKeyFromEnvironmentVariable();
    configureClient();
  }

  public Client(String mindeeApiKey) {
    this.mindeeApiKey = mindeeApiKey;
    this.httpClient = new MindeeHttpClient();
    configureClient();
  }

  public Client(String mindeeApiKey, DocumentParsingHttpClient httpClient) {
    this.mindeeApiKey = mindeeApiKey;
    this.httpClient = httpClient;
    configureClient();
  }

  public DocumentClient loadDocument(File file) {
    return new DocumentClient(this.httpClient, this::documentConfigFromKey, new FileInput(file,
      file.getName()), DocumentConfigFactory::parseParametersForResponseType);
  }

  public DocumentClient loadDocument(byte[] fileAsByteArray, String filename) {
    return new DocumentClient(this.httpClient, this::documentConfigFromKey,
      new FileInput(fileAsByteArray, filename), DocumentConfigFactory::parseParametersForResponseType);
  }

  public DocumentClient loadDocument(String fileAsBase64, String filename) {
    return new DocumentClient(this.httpClient, this::documentConfigFromKey,
      new FileInput(fileAsBase64, filename), DocumentConfigFactory::parseParametersForResponseType);
  }

  private void configureClient() {
    if (mindeeApiKey == null) {
      throw new RuntimeException("No API key Configured.");
    }

    for(Class responseType:DocumentConfigFactory.offTheShelfResponseTypes())
    {
      mapByResponseTypes.put(responseType,DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(responseType,mindeeApiKey));
    }

  }

  private DocumentConfig documentConfigFromKey(Class responseType, DocumentKey documentKey) {

    if(mapByResponseTypes.containsKey(responseType))
      return mapByResponseTypes.get(responseType);

    String accountName = documentKey.getAccountName();
    String documentType = documentKey.getDocumentType();
    if (accountName != null) {
      DocumentKey key = new DocumentKey(accountName, documentType);
      if (configMap.containsKey(key)) {
        return configMap.get(key);
      } else {
        DocumentConfig configFromApiKey = DocumentConfigFactory.getDocumentConfigFromApiKey(
          mindeeApiKey,
          documentType, accountName);
        configMap.put(key, configFromApiKey);
        return configFromApiKey;
      }
    }
    List<DocumentConfig> configs = new ArrayList<>();
    List<DocumentKey> keys = new ArrayList<>();
    for (Map.Entry<DocumentKey, DocumentConfig> entry : configMap.entrySet()) {
      DocumentKey key = entry.getKey();
      if (key.getDocumentType().equals(documentType)) {
        configs.add(entry.getValue());
        keys.add(key);
      }

    }
    if (configs.size() == 1) {
      return configs.get(0);
    } else if (configs.size() == 0) {
      throw new RuntimeException(String.format(
        "Account name/owner is needed for document types %s to use default apikeys "
          + "or api keys configured in environemnt variables", documentType));
    } else {
      throw new RuntimeException(
        String.format("Duplicate configuration detected for document type %s. "
            + "Please provide an account name parameter, one of, %s", documentType,
          keys.stream().map((item) -> item.getAccountName()).collect(
            Collectors.joining(", "))));
    }
  }


  @Value
  private static final class DocumentKey {

    private final String accountName;
    private final String documentType;

    private DocumentKey(String accountName, String documentType) {
      this.accountName = accountName;
      this.documentType = documentType;
    }
  }

  private static final class FileInput {

    private File file;
    private byte[] fileAsByteArray;
    private String base64String;
    private String filename;
    private FileInputType type;

    private FileInput(File file, String filename) {
      this.file = file;
      this.filename = filename;
      this.type = FileInputType.FILE;
    }

    private FileInput(byte[] fileAsByteArray, String filename) {
      this.fileAsByteArray = fileAsByteArray;
      this.filename = filename;
      this.type = FileInputType.BYTE_ARRAY;
    }

    private FileInput(String fileAsBase64, String filename) {
      this.base64String = fileAsBase64;
      this.filename = filename;
      this.type = FileInputType.BASE64;
    }

    private String getFilename() {
      return this.filename;
    }

    private List<Integer> getPagesToMerge(PageOptions pageOptions, Integer numberOfPages) {
      Set<Integer> pages =  pageOptions
        .getPages().stream()
        .filter((x) -> x > (numberOfPages)*(-1) && x<= (numberOfPages -1) )
        .map(x -> (numberOfPages + x)%numberOfPages)
        .collect(Collectors.toSet());
      List<Integer> allPages = IntStream.range(0,numberOfPages).boxed().collect(Collectors.toList());

      switch (pageOptions.getMode()){
        case KEEP_ONLY_LISTED_PAGES:
          return new ArrayList<>(pages);
        case REMOVE_LISTED_PAGES:
          allPages.removeAll(pages);
          return allPages;
        default:
          return allPages;
      }

    }

    @SneakyThrows
    private InputStream getFileInputStream(PageOptions pageOptions) {

      InputStream inputStream;
      int countOfPages = 0;
      switch (type) {
        case FILE:
          if (pageOptions !=null && PDFUtils.checkPdfOpen(file) && (countOfPages = PDFUtils.countPdfPages(file))
            > pageOptions.getOnMinPages()) {
            byte[] mergedFile = PDFUtils.mergePdfPages(file,
              getPagesToMerge(pageOptions, countOfPages));
            inputStream = new ByteArrayInputStream(mergedFile);
          } else {
            inputStream = new FileInputStream(file);
          }
          break;
        case BASE64:
          fileAsByteArray = Base64.getDecoder().decode(base64String.getBytes());
        case BYTE_ARRAY:
          if (pageOptions !=null && PDFUtils.checkPdfOpen(fileAsByteArray)
            && (countOfPages = PDFUtils.countPdfPages(fileAsByteArray))
            > pageOptions.getOnMinPages()) {
            byte[] mergedFile = PDFUtils.mergePdfPages(fileAsByteArray,
              getPagesToMerge(pageOptions, countOfPages));
            inputStream = new ByteArrayInputStream(mergedFile);
          } else {
            inputStream = new ByteArrayInputStream(fileAsByteArray);
          }
          break;
        default:
          inputStream = null;

      }
      return inputStream;
    }

    private enum FileInputType {
      FILE,
      BYTE_ARRAY,
      BASE64,
      PATH
    }
  }

  public static final class DocumentClient {

    private final DocumentParsingHttpClient httpClient;
    private final BiFunction<Class, DocumentKey, DocumentConfig> docConfigKeyToDocConfigMapper;
    private final FileInput fileInput;
    private final Function<Class, ParseParameters> defaultParseParametersFromClass;


    private DocumentClient(DocumentParsingHttpClient httpClient,
      BiFunction<Class, DocumentKey, DocumentConfig> docConfigKeyToDocConfigMapper,
      FileInput fileInput,
      Function<Class, ParseParameters> defaultParseParametersFromClass) {
      this.httpClient = httpClient;
      this.docConfigKeyToDocConfigMapper = docConfigKeyToDocConfigMapper;
      this.fileInput = fileInput;
      this.defaultParseParametersFromClass = defaultParseParametersFromClass;
    }

    public <T extends BaseDocumentResponse> T parse(Class<T> type,
      ParseParameters parseParameters,
      Function<T, T> postProcessor) throws IOException {

      DocumentConfig<T> documentConfig = docConfigKeyToDocConfigMapper.apply(type,
        new DocumentKey(parseParameters.getAccountName(),parseParameters.getDocumentType()));

      Endpoint endpoint = getEndpoint(parseParameters.getDocumentType(), type, documentConfig);
      Map response = httpClient.parse(fileInput.getFileInputStream(parseParameters.getPageOptions()),
        fileInput.getFilename(),
        endpoint.getApiKey(),
        EndpointUtils.buildUrl(endpoint),
        parseParameters.getIncludeWords());

      T documentResponse = documentConfig.getConverter().apply(type, response);
      return documentConfig.getBuiltInPostProcessing().andThen(postProcessor)
        .apply(documentResponse);
    }

    public <T extends BaseDocumentResponse> T parse(Class<T> type, ParseParameters parseParameters)
      throws IOException {
      return this.parse(type, parseParameters, UnaryOperator.identity());
    }

    public <T extends BaseDocumentResponse> T parse(Class<T> type, Function<T, T> postProcessor)
      throws IOException {
      ParseParameters parseParameters = defaultParseParametersFromClass.apply(type);
      if (parseParameters == null) {
        throw new RuntimeException(String.format(
          "Default ParseParameters are not available for Response type %s - Use overloaded parse methods to pass ParseParameters"
          , type.getName()));
      }
      return this.parse(type, parseParameters, postProcessor);
    }

    public <T extends BaseDocumentResponse> T parse(Class<T> type)
      throws IOException {
      ParseParameters parseParameters = defaultParseParametersFromClass.apply(type);
      if (parseParameters == null) {
        throw new RuntimeException(String.format(
          "Default ParseParameters are not available for Response type %s - Use overloaded parse methods to pass ParseParameters"
          , type.getName()));
      }
      return this.parse(type, parseParameters, UnaryOperator.identity());
    }


    private <T extends BaseDocumentResponse> Endpoint getEndpoint(String documentType,
      Class<T> type, DocumentConfig<T> documentConfig) {
      if ( documentConfig.getEndpoints().size() > 1) {
        if ("PDF".equalsIgnoreCase(FileUtils.getFileExtension(fileInput.getFilename()))) {
          return documentConfig.getEndpoints().get(0);
        } else {
          return documentConfig.getEndpoints().get(1);
        }
      } else {
        return documentConfig.getEndpoints().get(0);
      }
    }
  }
}
