package io.github.gravetii.service;

import io.github.gravetii.util.Trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class WordService {

    private static final Logger logger = Logger.getLogger(WordService.class.getCanonicalName());

    private static final String WORDS_FILE = "words.txt";

    private Trie trie;

    public WordService() {
        BufferedReader reader = null;

        try {
            this.trie = new Trie();
            InputStream istream = ClassLoader.getSystemResourceAsStream(WORDS_FILE);
            reader = new BufferedReader(new InputStreamReader(istream));
            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim();
                trie.insert(word);
            }

            logger.info("Completed loading all words into the trie.");
        }
        catch (IOException e) {
            logger.info("Error: Could not load all words into the trie.");
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e) {
                logger.info("Error: Could not successfully close reader.");
            }
        }
    }

    public boolean search(String word) {
        return this.trie.search(word);
    }

    public boolean prefix(String word) {
        return this.trie.prefix(word);
    }

    public static void main(String[] args) throws Exception {
        WordService service = new WordService();
        System.out.println(service.search("mat"));
        Thread.sleep(1000);
        System.out.println(service.search("mat"));
        System.out.println(service.search("got"));
        System.out.println(service.search("hip"));
        System.out.println(service.search("asdaegff"));
    }
}
