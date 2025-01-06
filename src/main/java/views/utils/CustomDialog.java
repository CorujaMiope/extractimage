package views.utils;

import enums.DIALOG_MODE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialog {

    public static int warningDialog(String title, String message, Component component){
        return JOptionPane.showConfirmDialog(
                component,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void errorDialog(String title, String message, Component component){
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(component), title);
        dialog.setSize(300,300);
        dialog.setLayout(new BorderLayout());

        var panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("[icone]"), 0);
        panel.add(new JLabel(message),1);

        dialog.add(panel, BorderLayout.NORTH);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static JFileChooser chooserDialog(Component component, DIALOG_MODE MODE){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(MODE.getTitleMode());

        switch (MODE){
            case OPEN_FILE -> {
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            }
            case SAVE_FILE -> {
                chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            }
        }

        chooser.showOpenDialog(component);
        chooser.setSize(new Dimension(600, 600));
        chooser.setBackground(Color.darkGray);

        return chooser;
    }
}
