import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OfficeButton extends JButton {
    Font officeFont = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("reg.ttf"));

    public OfficeButton(String text, String tooltip, int fontSize) throws IOException, FontFormatException {
        if(!text.equals("back")) {
            setMinimumSize(new Dimension(300, 50));
            setMaximumSize(new Dimension(300, 50));
            setPreferredSize(new Dimension(300, 50));
        }
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(officeFont);
        setFont(new Font(officeFont.getFontName(), Font.PLAIN, fontSize));
        setText(text);
        setFocusPainted(false);
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        Border line = new LineBorder(Color.BLACK,3);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        Border compound = new CompoundBorder(line, margin);
        setBorder(compound);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        setToolTipText(tooltip);
    }
}
