package xiatian.pim.component.bar;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A Status Bar is defined as follows:
 * <p/>
 * [main component ................. ] [secondary 0] [secondary 1] ...
 * [secondary n]
 * <p/>
 * Components can be any JComponent derivative, although more commonly a JLabel
 * will be used, which support both an icon and a text label.
 *
 * @author shaines
 */
public class JStatusBar extends JPanel {

    private static final long serialVersionUID = 3830820091707669197L;

    /**
     * Maps a 0-based index to a JLabel that contains the text for the section
     */
    private Map<Integer, JComponent> componentMap = new HashMap<Integer, JComponent>();

    /**
     * The font of the components in the status bar
     */
    private Font statusFont = new Font("Arial", Font.PLAIN, 12);

    /**
     * The label that contains the main text of the status bar
     */
    private JComponent mainComponent;

    /**
     * The right panel, initially unused
     */
    //private JPanel rightPanel = null;

    /**
     * Creates a new JStatusBar. If you use this constructor, be sure to
     * eventually call init() to configure the status bar components.
     */
    public JStatusBar() {
    }

    /**
     * Creates a new JStatusBar that hosts only one component.
     *
     * @param mainComponent The main component, which is places as a left justified component
     */
    public JStatusBar(JComponent mainComponent) {
        init(mainComponent, null);
    }

    /**
     * Creates a new JStatusBar with a main componennt and a list of secondary
     * components
     *
     * @param mainComponent
     * @param secondaryComponents
     */
    public JStatusBar(JComponent mainComponent, List<JComponent> secondaryComponents) {
        init(mainComponent, secondaryComponents);
    }

    /**
     * Initializes the JStatusBar with the specified main component and list of
     * secondary components (in which the secondaryComponents list may be null if
     * there are no secondary components)
     *
     * @param mainComponent       The main component, which is presented on the left side of the
     *                            status bar and occupies all unused space
     * @param secondaryComponents A list of secondary components which are stored and accessed via a
     *                            0-indexed list through the getSecondaryComponent() method
     */
    public void init(JComponent mainComponent, List<JComponent> secondaryComponents) {
        // Save the main component
        this.mainComponent = mainComponent;

        // Configure the JStatusBar to use a BorderLayout
        setLayout(new BorderLayout());

        // The main component goes in the center region, the others go to the right
        mainComponent.setFont(statusFont);
        add(buildPanel(mainComponent), BorderLayout.CENTER);

        // Add all of our secondary components
        if (secondaryComponents != null && secondaryComponents.size() > 0) {
            int index = 0;
            JPanel outerPanel = this;
            for (JComponent component : secondaryComponents) {
                // Update the component map
                component.setFont(statusFont);
                componentMap.put(index++, component);

                // Add the new component to the center of a new JPanel
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(buildPanel(component), BorderLayout.CENTER);

                // Add the new JPanel to the EAST of the outer panel
                outerPanel.add(panel, BorderLayout.EAST);

                // Update our outer panel reference to point to the newly created JPanel
                outerPanel = panel;
            }

        }
    }

    /**
     * Returns a reference to the main component of the status bar
     *
     * @return A reference to the main component
     */
    public JComponent getMainComponent() {
        return mainComponent;
    }

    /**
     * Returns a reference to the specified secondary component of the status bar
     *
     * @param index The 0-based index, in which the most left secondary component is
     *              placed at index 0
     * @return A reference to the requested component
     */
    public JComponent getSecondaryComponent(int index) {
        if (index < componentMap.size()) {
            return componentMap.get(index);
        }
        return null;
    }

    /**
     * Builds a status panel container, which is indicated by a lowered bevel
     * border
     *
     * @param The component to add to the newly constructed panel
     * @return A JPanel that is left justified with a lowered bevel border
     */
    private JPanel buildPanel(JComponent component) {
        // Create a left-justified flow panel
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Set the border to look de-pressed
        panel.setBorder(BorderFactory.createLoweredBevelBorder());

        // Add our component to the panel
        panel.add(component);

        // Return the panel
        return panel;
    }
}