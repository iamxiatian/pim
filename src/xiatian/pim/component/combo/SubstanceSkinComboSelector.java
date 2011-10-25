package xiatian.pim.component.combo;

//import java.awt.Component;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
import javax.swing.JComboBox;
//import javax.swing.JList;
//import javax.swing.SwingUtilities;
//
//import org.jvnet.substance.SubstanceLookAndFeel;
//import org.jvnet.substance.api.SubstanceSkin;
//import org.jvnet.substance.api.renderers.SubstanceDefaultComboBoxRenderer;
//import org.jvnet.substance.skin.SkinInfo;

public class SubstanceSkinComboSelector extends JComboBox {
//  private static final long serialVersionUID = -8243602226347633554L;
//
//  public static String getCurrentSkinName(){
//    SubstanceSkin currentSkin = SubstanceLookAndFeel.getCurrentSkin();
//    return currentSkin.getDisplayName();
//  }
//  
//  public static void setCurrentSkinName(String name){
//    if(name==null) return;
//    for(SkinInfo skin:SubstanceLookAndFeel.getAllSkins().values()){
//      if(skin.getDisplayName().equalsIgnoreCase(name)){
//        final String classname = skin.getClassName();
//        SwingUtilities.invokeLater(new Runnable() {
//          @Override
//          public void run() {
//            SubstanceLookAndFeel.setSkin(classname);
//          }
//        });
//        break;
//      }
//    }
//  }
//  
//  public SubstanceSkinComboSelector() {
//    // populate the combobox    
//    super(new ArrayList<SkinInfo>(SubstanceLookAndFeel.getAllSkins().values()).toArray());
//    // set the current skin as the selected item
//    SubstanceSkin currentSkin = SubstanceLookAndFeel.getCurrentSkin();   
//    if(currentSkin!=null){      
//      for (SkinInfo skinInfo : SubstanceLookAndFeel.getAllSkins().values()) {
//        if (skinInfo.getDisplayName().compareTo(currentSkin.getDisplayName()) == 0) {
//          this.setSelectedItem(skinInfo);
//          break;
//        }
//      }
//    }
//    // set custom renderer to show the skin display name
//    this.setRenderer(new SubstanceDefaultComboBoxRenderer(this) {
//      private static final long serialVersionUID = -4719457220631534702L;
//
//      @Override
//      public Component getListCellRendererComponent(JList list,
//          Object value, int index, boolean isSelected,
//          boolean cellHasFocus) {
//        return super.getListCellRendererComponent(list, ((SkinInfo) value).getDisplayName(), index, isSelected,
//            cellHasFocus);
//      }
//    });
//    // add an action listener to change skin based on user selection
//    this.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        SwingUtilities.invokeLater(new Runnable() {
//          @Override
//          public void run() {
//            SubstanceLookAndFeel.setSkin(((SkinInfo) SubstanceSkinComboSelector.this.getSelectedItem()).getClassName());
//          }
//        });
//      }
//    });
//  }
}
