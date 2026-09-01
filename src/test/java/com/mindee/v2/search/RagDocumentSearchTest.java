package com.mindee.v2.search;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.search.ragdocuments.RagDocumentSearchResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

public class RagDocumentSearchTest {

  @Test
  public void ragDocumentSearchResponse_LoadsLocally() throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath("search/rag_documents.json"));
    RagDocumentSearchResponse response = localResponse
      .deserializeResponse(RagDocumentSearchResponse.class);

    assertNotNull(response);

    assertEquals(3, response.getRagDocuments().size());
    assertEquals(3, response.getPagination().getTotalItems());
    assertEquals(1, response.getPagination().getPage());
    assertEquals(50, response.getPagination().getPerPage());
    assertEquals(1, response.getPagination().getTotalPages());

    var firstItem = response.getRagDocuments().get(0);
    assertEquals("cc831599-c545-48b7-aa27-6d7ccd5b8d32", firstItem.getId());
    assertEquals("12345678-1234-1234-1234-123456789abc", firstItem.getModelId());
    assertEquals("invoice_01.pdf", firstItem.getFilename());
    assertEquals(OffsetDateTime.parse("2026-06-30T13:13:46.168586Z"), firstItem.getCreatedAt());
    assertEquals(0, firstItem.getTotalMatches());
    assertNull(firstItem.getLastMatchAt());
    assertEquals("Processing", firstItem.getStatus());

    var secondItem = response.getRagDocuments().get(1);
    assertEquals("27467e4c-5602-4315-90d9-3d2da69b05ab", secondItem.getId());
    assertEquals("12345678-1234-1234-1234-123456789abc", secondItem.getModelId());
    assertEquals("invoice_02.pdf", secondItem.getFilename());
    assertEquals(OffsetDateTime.parse("2026-06-30T13:13:46.168586Z"), secondItem.getCreatedAt());
    assertEquals(0, secondItem.getTotalMatches());
    assertNull(secondItem.getLastMatchAt());
    assertEquals("Draft", secondItem.getStatus());

    var thirdItem = response.getRagDocuments().get(2);
    assertEquals("a6bcae7d-0439-476b-8a63-5a39ec05dc21", thirdItem.getId());
    assertEquals("12345678-1234-1234-1234-jobid1234567", thirdItem.getModelId());
    assertEquals("invoice_03.pdf", thirdItem.getFilename());
    assertEquals(OffsetDateTime.parse("2026-06-17T14:35:46.228006Z"), thirdItem.getCreatedAt());
    assertEquals(5, thirdItem.getTotalMatches());
    assertEquals(OffsetDateTime.parse("2026-06-18T14:35:46.248006Z"), thirdItem.getLastMatchAt());
    assertEquals("Active", thirdItem.getStatus());
  }
}
