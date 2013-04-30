package xiatian.pim.component.bar;

import javax.swing.JPanel;

/**
 * An abstract base class for creating animated components. It maintains its own
 * thread and invokes the derivative class's updateAnimation() method on the
 * configured timing interval.
 *
 * @author shaines
 */
public abstract class AbstractAnimatedComponent extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;

    /**
     * SLOW: invokes updateAnimation() every 1 second
     */
    public static int SLOW = 400;

    /**
     * MEDIUM_SLOW: invokes updateAnimation() every 700ms
     */
    public static int MEDIUM_SLOW = 300;

    /**
     * MEDIUM: invokes updateAnimzation() every 500ms
     */
    public static int MEDIUM = 200;

    /**
     * MEDIUM_FAST: invokes updateAnimation() every 300ms
     */
    public static int MEDIUM_FAST = 150;

    /**
     * FAST: invokes updateAnimzation() every 100ms
     */
    public static int FAST = 100;

    /**
     * The current speed of the animation
     */
    protected int speed = MEDIUM;

    /**
     * The thread that runs this animation
     */
    protected Thread thread = new Thread(this);

    /**
     * Internal state: is this animated component running?
     */
    protected boolean running = false;

    /**
     * Override this method to perform your animation tasks such as updating your
     * user interface and invoking repaint()
     */
    protected abstract void updateAnimation();

    /**
     * Returns the animation speed, which is defined as the number of milliseconds
     * between animation updates. Therefore, smaller = faster, larger = slower. As
     * a guide, you can consult the the predefined constants: SLOW, MEDIUM_SLOW,
     * MEDIUM, MEDIUM_FAST, or FAST.
     *
     * @return The animation speed
     */
    public int getAnimationSpeed() {
        return speed;
    }

    /**
     * Sets the animation speed, which is defined as the number of milliseconds
     * between animation updates. Therefore, smaller = faster, larger = slower. If
     * you need a guide, you can use one of the predefined constants: SLOW,
     * MEDIUM_SLOW, MEDIUM, MEDIUM_FAST, or FAST.
     *
     * @param speed
     */
    public void setAnimationSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Starts the animation
     */
    public void startAnimation() {
        thread.start();
    }

    /**
     * Stops the animation
     */
    public void stopAnimation() {
        running = false;
        thread.interrupt();
    }

    /**
     * Main thread's processing: invokes updateAnimation and sleeps for the
     * configured speed.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            updateAnimation();

            try {
                Thread.sleep(speed);
            } catch (Exception e) {
            }
        }
    }

}
