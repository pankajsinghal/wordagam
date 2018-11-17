package io.github.gravetii.game;

import io.github.gravetii.dictionary.Dictionary;
import io.github.gravetii.util.*;

import java.util.*;

import static io.github.gravetii.util.Constants.WORDS_COUNT_HIGH;
import static io.github.gravetii.util.Constants.WORDS_COUNT_LOW;

public class Game {

  private io.github.gravetii.dictionary.Dictionary dictionary;

  private GridUnit[][] grid;

  private Map<String, Integer> wordPoints;
  private int totalPoints;
  private Set<String> allWords;
  private Quality quality;

  public Game(Dictionary dictionary) {
    this.dictionary = dictionary;
    this.grid = new GridUnit[4][4];
    this.wordPoints = new HashMap<>();
    this.wordPoints.put("", 0);
    this.totalPoints = 0;
    this.allWords = new HashSet<>();
    this.create();
    this.crawl();
    this.quality = assignQuality();
  }

  public boolean exists(String word) {
    return this.allWords.contains(word);
  }

  public int getTotalPoints() {
    return this.totalPoints;
  }

  public int getWordPoints(String word) {
    return this.wordPoints.get(word);
  }

  public GridUnit[][] getGrid() {
    return grid;
  }

  public GridUnit getGridUnit(GridPoint point) {
    return grid[point.x][point.y];
  }

  private void create() {
    List<Alphabet> weightedAlphabets = Alphabet.getWeightedAlphabetsAsList();
    Random random = new Random();
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        int idx = random.nextInt(weightedAlphabets.size());
        grid[i][j] = new GridUnit(weightedAlphabets.get(idx), new GridPoint(i, j));
      }
    }
  }

  private boolean isValidWord(String word) {
    return word.length() >= Constants.MIN_WORD_LENGTH
        && this.dictionary.search(word)
        && !allWords.contains(word);
  }

  private void crawl(GridPoint point, String prefix, boolean[][] visited) {
    int x = point.x;
    int y = point.y;
    GridUnit unit = grid[x][y];
    visited[x][y] = true;

    String word = prefix + unit.getLetter();
    if (!this.dictionary.prefix(word)) {
      return;
    }

    int points = this.wordPoints.get(prefix) + unit.getPoints();
    this.wordPoints.put(word, points);
    if (isValidWord(word)) {
      this.allWords.add(word);
      this.totalPoints += wordPoints.get(word);
    }

    for (GridPoint n : point.getNeighbors()) {
      if (!visited[n.x][n.y]) {
        boolean[][] v = visited.clone();
        this.crawl(n, word, v);
      }
    }
  }

  private void crawl() {
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        boolean visited[][] = new boolean[4][4];
        for (boolean[] row : visited) {
          Arrays.fill(row, false);
        }

        this.crawl(new GridPoint(i, j), "", visited);
      }
    }
  }

  private Quality assignQuality() {
    int sz = allWords.size();
    Quality q;

    if (sz >= WORDS_COUNT_HIGH) {
      q = Quality.HIGH;
    } else if (sz <= WORDS_COUNT_LOW) {
      q = Quality.LOW;
    } else {
      q = Quality.MEDIUM;
    }

    return q;
  }

  public Quality getQuality() {
    return quality;
  }

  @Override
  public String toString() {
    return "Game{"
        + "totalPoints="
        + totalPoints
        + ", allWords="
        + allWords
        + ", quality="
        + quality
        + '}';
  }
}
