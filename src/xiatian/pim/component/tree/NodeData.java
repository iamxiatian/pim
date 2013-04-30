package xiatian.pim.component.tree;

import java.io.Serializable;

import javax.swing.ImageIcon;

import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;
import xiatian.pim.util.BlankUtils;
import xiatian.pim.util.ImageUtils;

public class NodeData implements Serializable {
    private static final long serialVersionUID = -4728445320831177638L;
    private Journal journal;
    private boolean expanded; //是否已经展开
    //private int childrenCount;

    public NodeData(Journal journal) {
        this.journal = journal;
    }

    public ImageIcon getIcon() {
        if (PimDb.getInstance().getJournalChildrenCount(journal.getId()) > 0) {
            if (!BlankUtils.isBlank(journal.getPassword())) {
                if (journal.isOpened()) {
                    return ImageUtils.createImageIcon("/images/small/unlock.png", journal.getTitle());
                } else {
                    return ImageUtils.createImageIcon("/images/small/lock.png", journal.getTitle());
                }
            }
            if (expanded) {
                return ImageUtils.createImageIcon("/images/small/folder.png", journal.getTitle());
            } else {
                return ImageUtils.createImageIcon("/images/small/folder2.png", journal.getTitle());
            }
        } else {
            return ImageUtils.createImageIcon("/images/small/leaf.png", journal.getTitle());
        }
    }

    public String getName() {
        return journal.getTitle();
    }

    public Journal getJournal() {
        return journal;
    }

    public String toString() {
        return journal.getTitle();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getChildrenCount() {
        return PimDb.getInstance().getJournalChildrenCount(journal.getId());
    }

}
