package xiatian.pim.component.doc.highlighter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class WordSearcher {

    public static class Word {
        private String word;
        private Color color;

        public Word() {
        }

        public Word(String word, Color color) {
            this.word = word;
            this.color = color;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    //key为颜色的数值
    Map<Integer, Highlighter.HighlightPainter> painters = new HashMap<Integer, Highlighter.HighlightPainter>();

    public WordSearcher(JTextComponent comp) {
        this.comp = comp;
        //this.painter = new UnderlineHighlighter.UnderlineHighlightPainter(painterColor);
    }

    public int search(Word word) {
        List<Word> words = new ArrayList<Word>();
        words.add(word);
        return search(words);
    }

    // Search for a word and return the offset of the
    // first occurrence. Highlights are added for all
    // occurrences found.
    public int search(List<Word> words) {
        int firstOffset = -1;
        Highlighter highlighter = comp.getHighlighter();

        // Remove any existing highlights for last word
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() instanceof UnderlineHighlighter.UnderlineHighlightPainter) {
                //highlighter.removeHighlight(h);
            }
        }

        if (words == null || words.size() == 0) {
            return -1;
        }

        // Look for the word we are given - insensitive search
        String content = null;
        try {
            Document d = comp.getDocument();
            content = d.getText(0, d.getLength()).toLowerCase();
        } catch (BadLocationException e) {
            // Cannot happen
            return -1;
        }

        for (Word word : words) {
            String wordString = word.getWord().toLowerCase();
            int wordSize = wordString.length();

            int lastIndex = 0;

            while ((lastIndex = content.indexOf(wordString, lastIndex)) != -1) {
                int endIndex = lastIndex + wordSize;
                try {
                    Highlighter.HighlightPainter painter = painters.get(word.getColor().getRGB());
                    if (painter == null) {
                        painter = new UnderlineHighlighter.UnderlineHighlightPainter(word.getColor());
                        painters.put(word.getColor().getRGB(), painter);
                    }
                    highlighter.addHighlight(lastIndex, endIndex, painter);
                } catch (BadLocationException e) {
                    // Nothing to do
                }
                if (firstOffset == -1) {
                    firstOffset = lastIndex;
                }
                lastIndex = endIndex;
            }
        }

        return firstOffset;
    }

    protected JTextComponent comp;

}