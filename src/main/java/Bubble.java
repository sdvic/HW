import java.awt.*;
import java.awt.geom.Ellipse2D;

import static java.awt.Toolkit.getDefaultToolkit;

class Bubble
{
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    int bubbleDiameter = screenWidth/30;
    int bubbleXpos;
    int bubbleYpos;
    Color backgroundColor;
    private int sensorBubblePitch = screenWidth/18;
    private int emitterBubblePitch = screenWidth/6;
    Ellipse2D.Double circle;
    private int leftMargin = screenWidth/10;

    public Bubble(int bubbleXpos, int bubbleYpos, Color backgroundColor)
    {
        this.bubbleXpos = bubbleXpos;
        circle = new Ellipse2D.Double(bubbleXpos, bubbleYpos, bubbleDiameter, bubbleDiameter);
        this.setBackgroundColor(backgroundColor);
    }
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }
}