import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class Menu extends JFrame {

    EpMenu[] seasons = new EpMenu[9];

    JLabel[] buttonArray = new JLabel[9];

    Font officeFont = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("reg.ttf"));

    JLabel officeTitle = new JLabel("The Office", SwingConstants.CENTER);

    JButton contButton = new OfficeButton("Continue Watching: " + Data.episodes[Data.current[0]][Data.current[1]], null, 20);

    JButton backButton = new OfficeButton("back", null, 15);

    CardLayout cards;
    JPanel cardPanel = new JPanel();
    JPanel backPanel = new JPanel();

    int count = 0;

    public Menu() throws IOException, FontFormatException {

        readFile();

        for(int i = 0; i < 9; i++)
            buttonArray[i] = new SeasonButton("S" + (i + 1) + ".jpg");

        cards = new CardLayout();
        cardPanel.setLayout(cards);
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,12,12,12);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(officeFont);
        officeTitle.setFont(new Font(officeFont.getFontName(), Font.PLAIN, 75));
        officeTitle.setForeground(Color.black);
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        mainMenu.add(officeTitle, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0,12,20,12);
        c.fill = GridBagConstraints.HORIZONTAL;
        mainMenu.add(contButton, c);

        c.insets = new Insets(12,12,12,12);
        c.gridwidth = 1;
        for(int i = 2; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if(count > 8)
                    break;
                c.gridy = i;
                c.gridx = j;
                mainMenu.add(buttonArray[count], c);
                count++;
            }
        }

        cardPanel.add(mainMenu, "MainMenu");

        GridLayout backLayout = new GridLayout(0,5);
        backPanel.setLayout(backLayout);
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        backPanel.add(backButton);
        backPanel.setVisible(false);

        for(int i = 0; i < 9; i++){
            String sNumber;
            switch(i) {
                case 0: sNumber = "One";
                    break;
                case 1: sNumber = "Two";
                    break;
                case 2: sNumber = "Three";
                    break;
                case 3: sNumber = "Four";
                    break;
                case 4: sNumber = "Five";
                    break;
                case 5: sNumber = "Six";
                    break;
                case 6: sNumber = "Seven";
                    break;
                case 7: sNumber = "Eight";
                    break;
                case 8: sNumber = "Nine";
                    break;
                default: sNumber = "NaN";
                    break;
            }
            seasons[i] = new EpMenu("Season " + sNumber, Data.episodes[i], Data.desc[i]);
            cardPanel.add(seasons[i], "Season" + (i+1));
        }

        getContentPane().add(backPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.SOUTH);
        pack();
        setBounds(100, 100, 725, 750);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initEvent();

        ActionListener task = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                contButton.setText("Continue Watching: " + Data.episodes[Data.current[0]][Data.current[1]]);
            }
        };
        Timer timer = new Timer(1000 ,task); // Execute task each 100 miliseconds
        timer.setRepeats(true);
        timer.start();
    }

    private void initEvent(){
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(1);
            }
        });
        contButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Player(Data.current[0], Data.current[1], Data.current[2]);
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cards.show(cardPanel, "MainMenu");
                backPanel.setVisible(false);
            }
        });

        for(int i = 0; i < 9; i++){
            int tempi = i;
            buttonArray[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String page = "Season" + (tempi + 1);
                    cards.show(cardPanel, page);
                    backPanel.setVisible(true);
                }
            });
        }
        
        for(int i = 0; i < Data.episodes.length; i++){
            for(int j = 0; j < Data.episodes[i].length; j++){
                int tempi = i;
                int tempj = j;
                seasons[i].getButton(tempj).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String tempS;

                        new Player(tempi, tempj, 0);
                    }
                });
            }
        }
    }

    public static void readFile() {
        try {
            File myObj = new File("current.txt");
            Scanner myReader = new Scanner(myObj);

            for(int i = 0; i < 3; i++) {
                Data.current[i] = Integer.parseInt(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, FontFormatException {
        new Menu();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    FileWriter fw = new FileWriter("current.txt");

                    for (int i = 0; i < 3; i++) {
                        fw.write(Integer.toString(Data.current[i]) + System.lineSeparator());
                    }

                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
