/*
 * Created by JFormDesigner on Fri Nov 29 09:17:14 BRT 2024
 */

package views;

import com.captureimage.extractimage.controller.Extractor;
import com.captureimage.extractimage.dto.ImagePropertyDTO;
import com.captureimage.extractimage.records.FileRecord;
import org.apache.pdfbox.util.StringUtil;
import org.bouncycastle.util.Strings;
import org.jdesktop.swingx.WrapLayout;
import org.springframework.security.web.util.UrlUtils;
import views.panels.ImagePanel;
import views.utils.CustomDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ADMIN
 */
public class FrmInitialScreean extends JFrame {

    private ExecutorService service = Executors.newCachedThreadPool();
    private List<ImagePropertyDTO> dtos = null;

    public FrmInitialScreean() {
        initComponents();
        setSize(new Dimension( 1080,720));
        pnlImages.setLayout(new WrapLayout(FlowLayout.LEFT, 5, 5));
        JScrollBar jScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        jScrollBar.setUnitIncrement(10);
        scroll.setVerticalScrollBar(jScrollBar);
    }

    private void buildImage(List<ImagePropertyDTO> dtos) throws IOException {
        for (ImagePropertyDTO image : dtos){

            BufferedImage read = ImageIO.read(new ByteArrayInputStream(image.getData()));
            ImagePanel imagePanel = new ImagePanel(image.getWidth(), image.getHeigh(), read);
            pnlImages.add(imagePanel, 0);

            SwingUtilities.invokeLater(()->{
                pnlImages.repaint();
                pnlImages.revalidate();
            });
        }
    }

    private void btnSelectArchiverActionPerformed() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecione o arquivo");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setSize(new Dimension(600, 600));
        chooser.setBackground(Color.darkGray);
        chooser.showOpenDialog(this);

        File file = chooser.getSelectedFile();

        if (file != null){
            service.execute(()->{
                Extractor extractor = new Extractor();
                try {
                    dtos = extractor.inspectType(new FileRecord(file.getPath()));
                    buildImage(dtos);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private void btnSelectArchiverWebActionPerformed() {
        if (!txtSearch.getText().trim().isBlank()){

            if (!UrlUtils.isAbsoluteUrl(txtSearch.getText())){
                CustomDialog.errorDialog("Erro", "Link inválido", this);
                return;
            }

            service.execute(()->{
                Extractor extractor = new Extractor();
                try {
                    dtos = extractor.inspectType(new FileRecord(txtSearch.getText()));
                    buildImage(dtos);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void btnSaveActionPerformed() {
        // TODO add your code here
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel2 = new JPanel();
        pnlButtons = new JPanel();
        btnSave = new JButton();
        btnSelectArchiver = new JButton();
        panel3 = new JPanel();
        panel4 = new JPanel();
        txtSearch = new JTextField();
        btnSelectArchiverWeb = new JButton();
        scroll = new JScrollPane();
        pnlImages = new JPanel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel2 ========
        {
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0, 0};
            ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
            ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

            //======== pnlButtons ========
            {
                pnlButtons.setLayout(new BorderLayout(5, 5));

                //---- btnSave ----
                btnSave.setText("Salvar em lote");
                btnSave.setBorder(new EmptyBorder(5, 5, 5, 5));
                btnSave.addActionListener(e -> btnSaveActionPerformed());
                pnlButtons.add(btnSave, BorderLayout.PAGE_END);

                //---- btnSelectArchiver ----
                btnSelectArchiver.setText("Selecionar Arquivo");
                btnSelectArchiver.setBorder(new EmptyBorder(5, 5, 5, 5));
                btnSelectArchiver.addActionListener(e -> btnSelectArchiverActionPerformed());
                pnlButtons.add(btnSelectArchiver, BorderLayout.PAGE_START);
            }
            panel2.add(pnlButtons, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(15, 5, 0, 10), 0, 0));

            //======== panel3 ========
            {
                panel3.setLayout(new BorderLayout());

                //======== panel4 ========
                {
                    panel4.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel4.getLayout()).columnWidths = new int[] {0, 0, 0};
                    ((GridBagLayout)panel4.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)panel4.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
                    ((GridBagLayout)panel4.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
                    panel4.add(txtSearch, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //---- btnSelectArchiverWeb ----
                    btnSelectArchiverWeb.setText("Selecionar arquivo na web");
                    btnSelectArchiverWeb.setBorder(new EmptyBorder(5, 5, 5, 5));
                    btnSelectArchiverWeb.addActionListener(e -> btnSelectArchiverWebActionPerformed());
                    panel4.add(btnSelectArchiverWeb, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                panel3.add(panel4, BorderLayout.SOUTH);

                //======== scroll ========
                {
                    scroll.setBorder(null);

                    //======== pnlImages ========
                    {
                        pnlImages.setLayout(new FlowLayout());
                    }
                    scroll.setViewportView(pnlImages);
                }
                panel3.add(scroll, BorderLayout.CENTER);
            }
            panel2.add(panel3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 0, 5, 5), 0, 0));
        }
        contentPane.add(panel2, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }



    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel2;
    private JPanel pnlButtons;
    private JButton btnSave;
    private JButton btnSelectArchiver;
    private JPanel panel3;
    private JPanel panel4;
    private JTextField txtSearch;
    private JButton btnSelectArchiverWeb;
    private JScrollPane scroll;
    private JPanel pnlImages;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
