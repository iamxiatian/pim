package xiatian.pim.component.table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.RowFilter;

import xiatian.pim.util.PinyinUtils;

public class MyRowFilter extends RowFilter<Object, Object> {
    private int[] columns;
    private Matcher matcher;
    private int pinyinColumnIndex = 0;

    public MyRowFilter(String regex, int pinyinColumnIndex, int... columns) {
        this.columns = columns;
        this.pinyinColumnIndex = pinyinColumnIndex;
        Pattern pattern = Pattern.compile(regex);

        if (regex == null) {
            throw new IllegalArgumentException("Pattern must be non-null");
        }
        matcher = pattern.matcher("");
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    public boolean include(Entry<? extends Object, ? extends Object> value) {
        int count = value.getValueCount();
        if (columns.length > 0) {
            for (int i = columns.length - 1; i >= 0; i--) {
                int index = columns[i];
                if (index < count) {
                    if (include(value, index)) {
                        return true;
                    }
                }
            }
        } else {
            while (--count >= 0) {
                if (include(value, count)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean include(Entry<? extends Object, ? extends Object> value, int index) {
        matcher.reset(value.getStringValue(index).toLowerCase());
        boolean finded = matcher.find();
        if (!finded && pinyinColumnIndex == index) {
            String word = value.getStringValue(index).toLowerCase();
            String pinyin = PinyinUtils.getInstance().getPinyin(word, false);
            matcher.reset(pinyin);
            finded = matcher.find();
            if (!finded) {
                pinyin = PinyinUtils.getInstance().getPinyin(word, true);
                matcher.reset(pinyin);
                finded = matcher.find();
            }
        }
        return finded;
    }
}