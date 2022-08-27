import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class EpMenu extends JPanel {
    public ArrayList<JButton> buttonArray = new ArrayList<>();

    private Font officeFont = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("reg.ttf"));

    public JButton getButton(int b){
        return buttonArray.get(b);
    }

    public EpMenu(String seasonName, String[] episodeNames, String[] episodeDesc) throws IOException, FontFormatException {

        int number = episodeNames.length;
        int count = 0;
        for(int i = 0; i < number; i++)
            buttonArray.add(new OfficeButton((i+1) + ". " + episodeNames[i], episodeDesc[i], 16));

        setLayout(new GridBagLayout());

        JPanel episodePanel = new JPanel();
        episodePanel.setLayout(new GridLayout(0,2,10,10));

        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(10,10,10,10);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        JLabel officeTitle = new JLabel(seasonName, SwingConstants.CENTER);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(officeFont);
        officeTitle.setFont(new Font(officeFont.getFontName(), Font.PLAIN, 75));
        officeTitle.setForeground(Color.black);
        add(officeTitle, c);

        for(int i = 0; i < number; i++) {
            episodePanel.add(buttonArray.get(count));
            count++;
        }
        episodePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(episodePanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        if(!seasonName.equals("Season One")) {
            scroll.setMinimumSize(new Dimension(675, 500));
            scroll.setMaximumSize(new Dimension(675, 500));
            scroll.setPreferredSize(new Dimension(675, 500));
        }
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        add(scroll, c);

    }
}
