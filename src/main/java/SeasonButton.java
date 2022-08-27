import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SeasonButton extends JLabel {

    public SeasonButton(String s) throws IOException {
        Image im = ImageIO.read(ClassLoader.getSystemResourceAsStream(s));
        ImageIcon ii = new ImageIcon(im);
        setIcon(ii);
    }
}
