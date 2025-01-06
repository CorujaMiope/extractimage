package views.panels;

import views.utils.CustomDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ImagePanel extends JPanel implements Comparable<ImagePanel> {

    private int width;
    private int height;
    private int x;
    private int y;
    private String path;
    private BufferedImage image;

    public ImagePanel(int width, int height, BufferedImage image) {
        this.width = width;
        this.height = height;
        this.image = image;

        this.x = Math.round(width * 0.3f);
        this.y = Math.round(height * 0.3f);

        Dimension preferredSize = new Dimension(x, y);
        setPreferredSize(preferredSize);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    actionPerformed();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void actionPerformed() throws IOException {
        int dialog = CustomDialog.warningDialog(
                "Salvar individualmente",
                "Deseja salvar essa imagem individualmente?",
                this);

        if (dialog == JOptionPane.YES_OPTION){
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvar");
            chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
            chooser.setSize(new Dimension(600, 600));
            chooser.setBackground(Color.darkGray);
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            chooser.showOpenDialog(this);

            File file = new File(chooser.getSelectedFile().getPath() + ".jpg");
            boolean save = ImageIO.write(image, "jpg", file);

            if (save){
                System.out.println("Salvou");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            Graphics2D g1 = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();
            double scaleX = width / (double) image.getWidth(null);
            double scaleY = height / (double) image.getHeight(null);
            g1.scale(scaleX, scaleY);
            g1.drawImage(image, 0, 0, null);

            RenderingHints rh = g1.getRenderingHints();
            rh.put (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g1.setRenderingHints (rh);

            g1.dispose();

        }
    }

    @Override
    public int compareTo(ImagePanel o) {
        return this.equals(o) ? 1 : 0;
    }

}
