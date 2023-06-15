package com.mindee.parsing.common.ocr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.geometry.PolygonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Represents a page.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrPage {

  /**
   * All the words on the page, in semi-random order.
   */
  @JsonProperty("all_words")
  private List<Word> allWords = new ArrayList<>();
  private List<List<Word>> allLines = new ArrayList<>();

  @JsonCreator
  public OcrPage(@JsonProperty("all_words") List<Word> allWords) {
    this.allWords = allWords;

    // make sure words are sorted from top to bottom
    this.allWords.sort(
        (word1, word2) -> PolygonUtils.CompareOnY(word1.getPolygon(), word2.getPolygon())
    );
  }

  /**
   * Determine if two words are on the same line.
   */
  private boolean areWordsOnSameLine(Word currentWord, Word nextWord) {
    boolean currentInNext = PolygonUtils.isPointInPolygonY(
        currentWord.getPolygon().getCentroid(),
        nextWord.getPolygon()
    );
    boolean nextInCurrent = PolygonUtils.isPointInPolygonY(
        nextWord.getPolygon().getCentroid(),
        currentWord.getPolygon()
    );
    // We need to check both to eliminate any issues due to word order.
    return currentInNext || nextInCurrent;
  }

  /**
   * Order all the words on the page into lines.
   */
  protected List<List<Word>> toLines() {
    Word current = null;
    List<Integer> indexes = new ArrayList<>();
    List<List<Word>> lines = new ArrayList<>();

    // go through each word ...
    for (Word ignored : this.allWords) {
      ArrayList<Word> line = new ArrayList<>();
      int idx = 0;
      // ... and compare it to all other words.
      for (Word word : this.allWords) {
        idx += 1;
        if (indexes.contains(idx)) {
          continue;
        }
        if (current == null) {
          current = word;
          indexes.add(idx);
          line = new ArrayList<>();
          line.add(word);
        } else if (this.areWordsOnSameLine(current, word)) {
          line.add(word);
          indexes.add(idx);
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

  /**
   * All the words on the page, ordered in lines.
   */
  public List<List<Word>> getAllLines() {
    if (this.allLines.isEmpty()) {
      this.allLines = this.toLines();
    }
    return this.allLines;
  }

  public String toString() {
    return this.getAllLines().stream()
        .map(words -> words.stream().map(Word::getText).collect(Collectors.joining(" ")))
        .collect(Collectors.joining(String.format("%n")));
  }
}
