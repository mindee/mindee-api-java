package com.mindee.parsing.common.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.geometry.PolygonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Represents a page.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrPage {

  @JsonProperty("all_words")
  private ArrayList<Word> words = new ArrayList<>();

  /**
   * Order all the words on the page into lines.
   */
  public ArrayList<ArrayList<Word>> toLines() {
    Word current = null;
    ArrayList<Integer> indexes = new ArrayList<>();
    ArrayList<ArrayList<Word>> lines = new ArrayList<>();

    this.words.sort((word1, word2) -> PolygonUtils.CompareOnY(word1.getPolygon(), word2.getPolygon()));

    // go through each word ...
    for (Iterator<Word> allWordsIter = this.words.iterator(); allWordsIter.hasNext();) {
      allWordsIter.next();
      ArrayList<Word> line = new ArrayList<>();
      int idx = 0;
      // ... and compare it to all other words.
      for (Word word : this.words) {
        idx += 1;
        if (!indexes.contains(idx)) {
          if (current == null) {
            current = word;
            indexes.add(idx);
            line = new ArrayList<>();
            line.add(word);
          } else {
            // Is the current word's centroid within the Y boundaries of the next word?
            boolean currentInNext = PolygonUtils.isPointInPolygonY(
                PolygonUtils.getCentroid(current.getPolygon()),
                word.getPolygon()
            );
            // Is the next word's centroid within the Y boundaries of the current word?
            boolean nextInCurrent = PolygonUtils.isPointInPolygonY(
                PolygonUtils.getCentroid(word.getPolygon()),
                current.getPolygon()
            );
            // We need to check both to eliminate any issues due to word order.
            if (currentInNext || nextInCurrent) {
              line.add(word);
              indexes.add(idx);
            }
          }
        }
      }
      current = null;
      if (!line.isEmpty()) {
        line.sort((word1, word2) -> PolygonUtils.CompareOnX(word1.getPolygon(), word2.getPolygon()));
        lines.add(line);
      }
    }
    return lines;
  }

  public String toString() {
    return this.toLines().stream()
        .map(words -> words.stream().map(Word::getText).collect(Collectors.joining(" ")))
        .collect(Collectors.joining(String.format("%n")));
  }
}
