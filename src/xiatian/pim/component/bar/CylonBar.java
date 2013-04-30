package xiatian.pim.component.bar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

/**
 * Creates an animated component that paints a cylon-like progress bar
 *
 * @author shaines
 */
public class CylonBar extends AbstractAnimatedComponent {
    private static final long serialVersionUID = -1106349232621063391L;

    /**
     * The background color of the entire component
     */
    protected Color bgColor = Color.gray;

    /**
     * The background color of the bars
     */
    protected Color barBgColor = Color.lightGray;

    /**
     * Enumeration to defined the four states of a bar
     */
    public enum State {
        OFF, LOW, MEDIUM, HIGH
    }

    ;

    /**
     * Maps our states to their colors. When a bar color is set, ...
     */
    protected Map<State, Color> colorMap = new HashMap<State, Color>();

    /**
     * The states of each bar in the cylon eye
     */
    protected List<Bar> bars = new ArrayList<Bar>();

    /**
     * Denotes whether or not each bar's rectangle has been initialized - set the
     * first time the panel is painted
     */
    protected boolean rectsInitialized = false;

    /**
     * The current size of the panel, used to compare to the painted size to
     * determine whether or not the panel has been resized
     */
    protected Dimension size;

    /**
     * Enumeration to determine which direction the cylon eye is moving
     */
    public enum Direction {
        LEFT, RIGHT
    }

    ;

    /**
     * The position of the head of the cylon eye, from 0 .. 9
     */
    private int pos = 0;

    /**
     * The direction that the cylon eye is moving, right or left
     */
    private Direction direction = Direction.RIGHT;

    /**
     * Denotes whether or not the cylon eye is running
     */
    private boolean running = false;

    /**
     * A back buffer, used to reduce flicker during painting
     */
    private BufferedImage backBuffer;

    /**
     * Creates a new CylonBar with the specified bar color
     *
     * @param barColor
     */
    public CylonBar(Color barColor) {
        setBarColor(barColor);
        initializeBars();

        // define our minimum size
        this.setMinimumSize(new Dimension(80, 20));
        this.setPreferredSize(new Dimension(80, 20));
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return this.running;
    }

    /**
     * Returns the background color for the CylonBar component
     *
     * @return
     */
    public Color getBackgroundColor() {
        return bgColor;
    }

    /**
     * Sets the background color for the CylonBar component
     *
     * @param bgColor
     */
    public void setBackgroundColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * Returns the background color of the bar in the CylonBar component
     *
     * @return
     */
    public Color getBarBackgroundColor() {
        return barBgColor;
    }

    /**
     * Sets the background color of the bar in the CylonBar Component
     *
     * @param barBgColor
     */
    public void setBarBackgroundColor(Color barBgColor) {
        this.barBgColor = barBgColor;
    }

    /**
     * Sets the color of the bar, where the color represents the HIGH state and
     * the MEDIUM and LOW states are obtained by calling getLighterColor() on the
     * specified color
     *
     * @param color
     */
    public void setBarColor(Color color) {
        colorMap.put(State.HIGH, color);
        colorMap.put(State.MEDIUM, getLighterColor(color));
        colorMap.put(State.LOW, getLighterColor(colorMap.get(State.MEDIUM)));
    }

    /**
     * Makes the color lighter by increasing its Red, Green, and Blue values by 40
     * and stopping at 255
     *
     * @param color The color to make lighter
     * @return Returns a lighter copy of the color
     */
    protected Color getLighterColor(Color color) {
        int r = color.getRed() + 40;
        int g = color.getGreen() + 40;
        int b = color.getBlue() + 40;

        return new Color(r <= 255 ? r : 255, g <= 255 ? g : 255, b <= 255 ? b : 255);

    }

    /**
     * Paints the cylon bar
     */
    public void paint(Graphics g) {
        // Get the size of our component
        Dimension currentSize = getSize();

        if (size == null || backBuffer == null || currentSize.width != size.width || currentSize.height != size.height) {
            // We have to create a new back buffer if this is the first time we're
            // running, one does not exist
            // or we've resized...
            backBuffer = new BufferedImage(currentSize.width, currentSize.height, BufferedImage.TYPE_USHORT_565_RGB);
        }
        Graphics2D g2 = (Graphics2D) backBuffer.getGraphics();

        // Fill in the background
        g2.setColor(bgColor);
        g2.fillRect(0, 0, currentSize.width, currentSize.height);

        // See if we need to recompute the rectangles that host our cylon bars
        if (!rectsInitialized || size == null || currentSize.width != size.width || currentSize.height != size.height) {
            // Update our size
            size = currentSize;

            // We want 10 bars so divide the width (minus buffer) by 10, including
            // buffering between each bar and on the ends
            int segmentWidth = (size.width - 20) / 10;
            int spacerWidth = (int) ((double) segmentWidth * 0.20d);
            int barWidth = segmentWidth - spacerWidth;

            // Observe our 10 pixel buffer
            int x = 10;

            // Compute the height
            int y = (int) (size.height / 10.0);
            int barHeight = (int) (size.height * 0.80d);

            // We have to build the rectangles for each of our bars, either because
            // they
            // have not been initialized or because the panel has resized
            for (int i = 0; i < 10; i++) {
                Bar bar = bars.get(i);
                bar.setRect(new Rectangle(x + (i * barWidth) + (i * spacerWidth), y, barWidth, barHeight));
            }

            // Reset these state variables
            rectsInitialized = true;
        }

        // Draw the bars
        for (Bar bar : bars) {
            drawBar(g2, getStateColor(bar.getState()), bar.getRect());
        }

        // Blit the backbuffer to the graphics display
        g.drawImage(backBuffer, 0, 0, size.width, size.height, null);
    }

    /**
     * Returns the color that corresponds to the specified state
     *
     * @param state
     * @return
     */
    private Color getStateColor(State state) {
        if (state == State.OFF) {
            return barBgColor;
        } else {
            return colorMap.get(state);
        }
    }

    /**
     * Helper method that draws an individual cylon bar
     *
     * @param g
     * @param c
     * @param rect
     */
    protected void drawBar(Graphics2D g, Color c, Rectangle rect) {
        drawBar(g, c, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Helper method that draws an individual cylon bar
     *
     * @param g      The graphics context
     * @param c      The color of the bar
     * @param x      The x-coordinate
     * @param y      The y-coordinate
     * @param width  The width of the bar
     * @param height The height of the bar
     */
    protected void drawBar(Graphics2D g, Color c, int x, int y, int width, int height) {
        // Set our color
        g.setColor(c);

        // Draw our bar
        g.fillRect(x, y, width, height);
    }

    /**
     * Determine which bars need to be repainted
     */
    public void updateAnimation() {
        // Invoke the algorithm for updating the cylon bars
        traditional();

        // Intelligently determine what to repaint
        boolean repaintAll = false;
        int x = -1;
        int y = -1;
        int x2 = -1;
        int y2 = -1;
        for (Bar bar : bars) {
            if (bar.isStateModified()) {
                // Update our coordinates
                Rectangle rect = bar.getRect();
                if (rect == null) {
                    repaintAll = true;
                } else {
                    if (x == -1 || rect.x < x) {
                        x = rect.x;
                    }
                    if (y == -1 || rect.y < y) {
                        y = rect.y;
                    }
                    if (x2 == -1 || rect.x + rect.width > x2) {
                        x2 = rect.x + rect.width;
                    }
                    if (y2 == -1 || rect.y + rect.height > y2) {
                        y2 = rect.y + rect.height;
                    }
                }

                // Reset this bar's modified state
                bar.setStateModified(false);
            }
        }

        if (repaintAll) {
            // Repaint the entire panel
            repaint();
        } else {
            // Repaint only the area that needs to be repainted
            repaint(x, y, x2 - x, y2 - y);
        }
    }

    /**
     * We override the stopAnimation() method so that we can reset the state of
     * the cylon bars
     */
    public void stopAnimation() {
        // Let the parent class stop the animation
        super.stopAnimation();

        // Reset the bars
        for (Bar bar : bars) {
            bar.setState(State.OFF);
        }

        // Repaint the component with all of the bars in the OFF state
        repaint();
    }

    /**
     * Creates the bar list and initializes all bars to an OFF state
     */
    private void initializeBars() {
        // Delete any existing states
        if (bars.size() > 0) {
            bars.clear();
        }

        // Turn off all eyes
        for (int i = 0; i < 10; i++) {
            bars.add(new Bar(State.OFF));
        }
    }

    /**
     * Implements a traditional cylon eye algorithm
     */
    private void traditional() {
        // Adjust the position of the eye
        if (direction == Direction.RIGHT) {
            // Handle moving right
            if (pos < 9) {
                pos++;
            } else {
                direction = Direction.LEFT;
            }
        } else {
            // Handle moving left
            if (pos > 0)
                pos--;
            else
                direction = Direction.RIGHT;
        }

        // Update the states of the bars based on the position
        for (int i = 0; i < 10; i++) {
            if (pos == i) {
                bars.get(i).setState(State.HIGH);
            } else if ((direction == Direction.RIGHT && i == pos - 1) || (direction == Direction.LEFT && i == pos + 1)) {
                bars.get(i).setState(State.MEDIUM);
            } else if ((direction == Direction.RIGHT && i == pos - 1) || (direction == Direction.LEFT && i == pos + 1)) {
                bars.get(i).setState(State.MEDIUM);
            } else if ((direction == Direction.RIGHT && i == pos - 2) || (direction == Direction.LEFT && i == pos + 2)) {
                bars.get(i).setState(State.LOW);
            } else {
                bars.get(i).setState(State.OFF);
            }
        }
    }

    /**
     * An internal class that represents the state of an individual cylon bar
     *
     * @author shaines
     */
    class Bar {
        private State state;
        private Rectangle rect;
        private boolean stateModified;
        private boolean sizeModified;

        public Bar(State state) {
            this.state = state;
        }

        public boolean isRectDefined() {
            return rect != null;
        }

        public void setRect(Rectangle rect) {
            this.rect = rect;
        }

        public Rectangle getRect() {
            return rect;
        }

        public boolean isStateModified() {
            return stateModified;
        }

        public void setStateModified(boolean stateModified) {
            this.stateModified = stateModified;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            if (this.state != state) {
                stateModified = true;
                this.state = state;
            }
        }

        public void setSizeModified() {
            sizeModified = true;
        }

        public boolean isSizeModified() {
            return sizeModified;
        }
    }

    public static void main(String[] args) {
        CylonBar cb1 = new CylonBar(Color.red);
        cb1.setAnimationSpeed(CylonBar.FAST);

        CylonBar cb2 = new CylonBar(Color.blue);
        cb2.setBackgroundColor(Color.white);
        cb2.setAnimationSpeed(CylonBar.MEDIUM_FAST);

        CylonBar cb3 = new CylonBar(Color.green);
        cb3.setBackgroundColor(Color.white);
        cb3.setAnimationSpeed(CylonBar.MEDIUM);

        CylonBar cb4 = new CylonBar(Color.CYAN);
        cb4.setBackgroundColor(Color.black);
        cb4.setBarBackgroundColor(Color.blue);
        cb4.setAnimationSpeed(CylonBar.MEDIUM_SLOW);

        CylonBar cb5 = new CylonBar(Color.YELLOW);
        cb5.setBackgroundColor(Color.black);
        cb5.setBarBackgroundColor(Color.white);
        cb5.setAnimationSpeed(CylonBar.SLOW);

        JFrame frame = new JFrame("Cylon Test");
        frame.setLayout(new GridLayout(5, 1));
        frame.getContentPane().add(cb1);
        frame.getContentPane().add(cb2);
        frame.getContentPane().add(cb3);
        frame.getContentPane().add(cb4);
        frame.getContentPane().add(cb5);

        frame.setSize(400, 250);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width / 2 - 200, d.height / 2 - 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start our cylon eye
        cb1.startAnimation();
        cb2.startAnimation();
        cb3.startAnimation();
        cb4.startAnimation();
        cb5.startAnimation();

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }

        cb2.stopAnimation();
        cb3.stopAnimation();
        cb4.stopAnimation();
        cb5.stopAnimation();
    }
}