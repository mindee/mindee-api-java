package com.mindee.documentparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.http.DocumentParsingHttpClient;
import com.mindee.http.Endpoint;
import com.mindee.http.MindeeHttpClient;
import com.mindee.model.documenttype.BaseDocumentResponse;
import com.mindee.utils.PDFUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.Value;

public class Client {

  private static final String INVOICE = "invoice";
  private static final String RECEIPT = "receipt";
  private static final String PASSPORT = "passport";
  private static final String FINANCIAL_DOCUMENT = "financial_doc";
  private static final String MINDEE = "mindee";
  private final DocumentParsingHttpClient httpClient;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<DocumentKey, DocumentConfig> configMap = new HashMap<>();
  private String apiKey;


  public Client() {
    this.httpClient = new MindeeHttpClient();
  }

  public Client(DocumentParsingHttpClient httpClient) {
    this.httpClient = httpClient;
  }


  public Client configure(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  public Client configureInvoice(String apiKey) {
    DocumentConfig invoiceConfig = DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(
        INVOICE, MINDEE, apiKey);
    configMap.put(new DocumentKey(MINDEE, INVOICE), invoiceConfig);
    return this;
  }

  public Client configureReceipt(String apiKey) {
    DocumentConfig invoiceConfig = DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(
        RECEIPT, MINDEE, apiKey);
    configMap.put(new DocumentKey(MINDEE, RECEIPT), invoiceConfig);
    return this;
  }

  public Client configurePassport(String apiKey) {
    DocumentConfig invoiceConfig = DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(
        PASSPORT, MINDEE, apiKey);
    configMap.put(new DocumentKey(MINDEE, PASSPORT), invoiceConfig);
    return this;
  }

  public Client configureFinancialDoc(String invoiceApiKey, String receiptApiKey) {
    DocumentConfig financialDocConfig = DocumentConfigFactory.getDocumentConfigForOffTheShelfDocType(
        FINANCIAL_DOCUMENT, MINDEE,
        invoiceApiKey, receiptApiKey);
    configMap.put(new DocumentKey(MINDEE, FINANCIAL_DOCUMENT), financialDocConfig);
    return this;
  }

  public Client configureCustomDocument(String docType, String singular,
      String plural, String accountName, String apiKey,
      String verion) {
    DocumentConfig customConfig = DocumentConfigFactory.getDocumentConfigForCustomDocType(docType,
        accountName, apiKey, verion, singular, plural);
    configMap.put(new DocumentKey(accountName, docType), customConfig);
    return this;
  }

  public DocumentClient loadDocument(File file) {
    return new DocumentClient(this.httpClient, this::getDocumentConfig, new FileInput(file,
        file.getName()));
  }

  public DocumentClient loadDocument(byte[] fileAsByteArray, String filename) {
    return new DocumentClient(this.httpClient, this::getDocumentConfig,
        new FileInput(fileAsByteArray, filename));
  }

  public DocumentClient loadDocument(String fileAsBase64, String filename) {
    return new DocumentClient(this.httpClient, this::getDocumentConfig,
        new FileInput(fileAsBase64, filename));
  }


  private DocumentConfig getDocumentConfig(String documentType, String accountName) {
    DocumentConfig docConfigFromEnv = null;
    if (accountName != null) {
      DocumentKey key = new DocumentKey(accountName, documentType);
      if (configMap.containsKey(key)) {
        return configMap.get(key);
      } else if (this.apiKey != null) {
        DocumentConfig configFromApiKey = DocumentConfigFactory.getDocumentConfigFromApiKey(apiKey,
            documentType, accountName);
        configMap.put(key, configFromApiKey);
        return configFromApiKey;
      } else if ((docConfigFromEnv = DocumentConfigFactory.getDocumentConfigFromEnv(documentType,
          accountName)) != null) {
        configMap.put(key, docConfigFromEnv);
        return docConfigFromEnv;
      } else {
        throw new RuntimeException(
            String.format("Missing apikey for document type %s and account %s", documentType,
                accountName));
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
      if (!Arrays.asList(INVOICE, RECEIPT, FINANCIAL_DOCUMENT, PASSPORT).contains(documentType)) {
        throw new RuntimeException(String.format(
            "Account name/owner is needed for document types %s to use default apikeys "
                + "or api keys configured in environemnt variables", documentType));
      }
      if (this.apiKey != null) {
        DocumentKey key = new DocumentKey(MINDEE, documentType);
        DocumentConfig config = DocumentConfigFactory.getDocumentConfigFromApiKey(this.apiKey,
            documentType, MINDEE);
        configMap.put(key, config);
        return config;
      } else if (
          (docConfigFromEnv = DocumentConfigFactory.getDocumentConfigFromEnv(documentType, null))
              != null) {
        DocumentKey key = new DocumentKey(MINDEE, documentType);
        configMap.put(key, docConfigFromEnv);
        return docConfigFromEnv;
      } else {
        throw new RuntimeException(
            String.format("Missing apikey for document type %s and account %s", documentType,
                accountName));
      }
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
    private Path path;
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

    private List<Integer> getPagesToMerge(Integer cutMode, Integer numberOfPages) {
      Set<Integer> pages = new HashSet<>();
      pages.add(0);
      if (cutMode == 2 && numberOfPages >= 2) {
        pages.add(numberOfPages - 2);
      } else if (cutMode == 3 && numberOfPages >= 2) {
        pages.add(numberOfPages - 2);
        pages.add(numberOfPages - 1);
      }
      return new ArrayList<>(pages);
    }

    @SneakyThrows
    private InputStream getFileInputStream(Integer cutMode) {

      if (cutMode < 1 || cutMode > 3) {
        throw new IllegalArgumentException("cutMode should be one of 1,2,3");
      }

      InputStream inputStream;
      int countOfPages = 0;
      switch (type) {
        case FILE:
          if (PDFUtils.checkPdfOpen(file) && (countOfPages = PDFUtils.countPdfPages(file)) > 1) {
            byte[] mergedFile = PDFUtils.mergePdfPages(file,
                getPagesToMerge(cutMode, countOfPages));
            inputStream = new ByteArrayInputStream(mergedFile);
          } else {
            inputStream = new FileInputStream(file);
          }
          break;
        case BASE64:
          fileAsByteArray = Base64.getDecoder().decode(base64String.getBytes());
        case BYTE_ARRAY:
          if (PDFUtils.checkPdfOpen(fileAsByteArray)
              && (countOfPages = PDFUtils.countPdfPages(fileAsByteArray)) > 1) {
            byte[] mergedFile = PDFUtils.mergePdfPages(fileAsByteArray,
                getPagesToMerge(cutMode, countOfPages));
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

    private static final String WINDOWS_FILE_SEPARATOR = "\\";
    private static final String UNIX_FILE_SEPARATOR = "/";
    private static final String FILE_EXTENSION = ".";


    private final DocumentParsingHttpClient httpClient;
    private final BiFunction<String, String, DocumentConfig> docConfigKeyToDocConfigMapper;
    private final FileInput fileInput;


    private DocumentClient(DocumentParsingHttpClient httpClient,
        BiFunction<String, String, DocumentConfig> docConfigKeyToDocConfigMapper,
        FileInput fileInput) {
      this.httpClient = httpClient;
      this.docConfigKeyToDocConfigMapper = docConfigKeyToDocConfigMapper;
      this.fileInput = fileInput;

    }

    public <T extends BaseDocumentResponse> T parse(Class<T> type,
        ParseParameters parseParameters,
        Function<T, T> postProcessor) throws IOException {

      DocumentConfig<T> documentConfig = docConfigKeyToDocConfigMapper.apply(
          parseParameters.getDocumentType(),
          parseParameters.getAccountName());

      Endpoint endpoint = getEndpoint(parseParameters.getDocumentType(), type, documentConfig);
      Map response = httpClient.parse(fileInput.getFileInputStream(parseParameters.getCutMode()),
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

    private <T extends BaseDocumentResponse> Endpoint getEndpoint(String documentType,
        Class<T> type, DocumentConfig<T> documentConfig) {
      if (documentType.equalsIgnoreCase("financial_doc")) {
        if ("PDF".equalsIgnoreCase(getFileExtension(fileInput.getFilename()))) {
          return documentConfig.getEndpoints().get(0);
        } else {
          return documentConfig.getEndpoints().get(1);
        }
      } else {
        return documentConfig.getEndpoints().get(0);
      }
    }

    private String getFileExtension(String fileName) {
      if (fileName == null) {
        throw new IllegalArgumentException("fileName must not be null!");
      }

      String extension = "";

      int indexOfLastExtension = fileName.lastIndexOf(FILE_EXTENSION);

      // check last file separator, windows and unix
      int lastSeparatorPosWindows = fileName.lastIndexOf(WINDOWS_FILE_SEPARATOR);
      int lastSeparatorPosUnix = fileName.lastIndexOf(UNIX_FILE_SEPARATOR);

      // takes the greater of the two values, which mean last file separator
      int indexOflastSeparator = Math.max(lastSeparatorPosWindows, lastSeparatorPosUnix);

      // make sure the file extension appear after the last file separator
      if (indexOfLastExtension > indexOflastSeparator) {
        extension = fileName.substring(indexOfLastExtension + 1);
      }

      return extension;
    }
  }


}
