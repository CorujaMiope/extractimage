package views.panels;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {

    private static final BasicStroke STROKE = new BasicStroke(2);

    private int arcWidth;
    private int arcHeight;

    public RoundedBorder(){
        this(15, 15);
    }



    public RoundedBorder(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getForeground());
        g2.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int padding = Math.max(arcWidth, arcHeight) / 2;
        return new Insets(padding, padding, padding, padding);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = insets.top = insets.bottom = Math.max(arcWidth, arcHeight) / 2;
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
