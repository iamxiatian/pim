package xiatian.pim.listener;

import javax.swing.JFrame;

import xiatian.pim.component.doc.DocView;
import xiatian.pim.component.tree.ContentTree;
import xiatian.pim.component.tree.NodeData;
import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;

public class PimController extends BaseController {

  private static PimController instance = new PimController();
  
  private PimController(){}
  
  public static PimController getInstance(){
    return instance;
  }
  
  public void setUp(PimDb pimDb, JFrame frame, ContentTree tree, DocView docView){
    setPimDb(pimDb);
    setJFrame(frame);
    setContentTree(tree);
    setDocView(docView);
  }
  
  public void treeNodeSelected(NodeData data){    
    Journal journal = data.getJournal();
    docView.setJournal(journal);
  }


}
