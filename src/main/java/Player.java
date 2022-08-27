import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSliderUI;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Player extends JFrame{

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private final Icon playIcon = newIcon("play");
    private final Icon pauseIcon = newIcon("pause");
    private final Icon previousIcon = newIcon("previous");
    private final Icon nextIcon = newIcon("next");
    private final Icon fullscreenIcon = newIcon("fullscreen");

    private final JButton playPauseButton;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JButton fullscreenButton;
    private final JSlider statusBar;
    private final JLabel timeElapsed;
    private final JLabel videoLength;

    boolean isPlaying = true;
    boolean isFullScreen = false;

    public JPanel contentPane = new JPanel();
    public JPanel controlsPane = new JPanel();
    public JPanel sliderPane = new JPanel(new BorderLayout());
    public int durMin;
    public int durSec;

    public long realSec;
    public long curMin;
    public long curSec;

    public long startTime;

    public boolean stopLoop = false;

    public int season;
    public int episode;

    public Player(int season, int episode, int sec) {

        this.season = season;
        this.episode = episode;


        realSec = sec;

        durMin = Data.dur[season][episode] / 60;
        durSec = Data.dur[season][episode] % 60;

        String cpStart = returnPath();
        String cpEnd = ".mp4";

        String tempS;
        if(episode+1 < 10){
            tempS = "e0";
        }else{
            tempS = "e";
        }

        String loadMedia = cpStart + (season+1) + "\\" + "s0" + (season+1) + tempS + (episode+1) + " " +
        Data.episodes[season][episode] + cpEnd;

        setTitle("Season " + (season+1) + ", Episode " + (episode+1) + ": " + Data.episodes[season][episode]);
        setBounds(100, 100, 800, 550);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        contentPane.setLayout(new BorderLayout());

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent(
                null,
                null,
                new AdaptiveFullScreenStrategy(this),
                null,
                null
        );
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

        playPauseButton = new PlayerButton(true, "play/pause");
        playPauseButton.setIcon(pauseIcon);
        previousButton = new PlayerButton(false, "previous episode");
        previousButton.setIcon(previousIcon);
        nextButton = new PlayerButton(false, "next episode");
        nextButton.setIcon(nextIcon);
        fullscreenButton = new PlayerButton(false, "fullscreen");
        fullscreenButton.setIcon(fullscreenIcon);
        timeElapsed = new JLabel("" + realSec);

        if(durSec < 10) {
            videoLength = new JLabel(durMin + ":0" + durSec);
        }else{
            videoLength = new JLabel(durMin + ":" + durSec);
        }

        statusBar = new JSlider(0, Data.dur[season][episode], 0);

        statusBar.setUI(new MyBasicSliderUI(statusBar));
        statusBar.setFocusable(false);

        sliderPane.setPreferredSize(new Dimension(400, 50));
        sliderPane.add(statusBar);


        controlsPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 5);
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        controlsPane.add(playPauseButton, c);
        c.insets = new Insets(18, 5, 5, 5);
        c.gridx = 1;
        controlsPane.add(previousButton, c);
        c.gridx = 2;
        controlsPane.add(nextButton, c);
        c.insets = new Insets(21, 5, 5, 5);
        c.gridx = 3;
        controlsPane.add(timeElapsed, c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 4;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        controlsPane.add(sliderPane, c);
        c.insets = new Insets(21, 5, 5, 5);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 5;
        c.weightx = 0.0;
        controlsPane.add(videoLength, c);
        c.insets = new Insets(10, 5, 10, 10);
        c.gridx = 6;
        c.fill = GridBagConstraints.BOTH;
        controlsPane.add(fullscreenButton, c);

        contentPane.add(controlsPane, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanUp();
            }
        });

        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.mediaPlayer().controls().pause();
                isPlaying = !isPlaying;
                if(isPlaying){
                    startTime = System.nanoTime();
                    playPauseButton.setIcon(pauseIcon);
                }else{
                    playPauseButton.setIcon(playIcon);
                }
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(episode > 0) {
                    new Player(season, episode - 1, 0);
                    cleanUp();
                }else {
                    if (season > 0) {
                        new Player(season - 1, Data.episodes[season - 1].length - 1, 0);
                        cleanUp();
                    }
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(episode < Data.episodes[season].length-1) {
                    new Player(season, episode + 1, 0);
                    cleanUp();
                }else {
                    if (season < 8) {
                        new Player(season + 1, 0, 0);
                        cleanUp();
                    }
                }
            }
        });

        fullscreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.mediaPlayer().fullScreen().set(true);
                isFullScreen = !isFullScreen;
                controlsPane.setVisible(false);
            }
        });

        statusBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newPosition = statusBar.getValue();
                long change = newPosition - realSec;

                mediaPlayerComponent.mediaPlayer().controls().skipTime(change * 1000);
                realSec =  newPosition;
            }
        });

        contentPane.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "exitFullscreen");
        contentPane.getActionMap().put("exitFullscreen", new exitFullScreen());

        setContentPane(contentPane);
        setVisible(true);
        mediaPlayerComponent.mediaPlayer().media().play(loadMedia);
        if(sec > 5){
            sec -= 5;
        }

        mediaPlayerComponent.mediaPlayer().controls().skipTime(sec * 1000);
        startTime = System.currentTimeMillis();

        new SwingWorker(){
            @Override
            protected Object doInBackground() throws Exception {
                while(true){
                    if(stopLoop)
                        break;
                    try{
                        Thread.sleep(1000);
                        if(isPlaying) {
                            realSec++;
                        }
                        curMin = realSec / 60;
                        curSec = realSec % 60;
                        statusBar.setValue((int)(realSec));
                        if(curSec < 10) {
                            timeElapsed.setText(curMin + ":0" + curSec);
                        }else{
                            timeElapsed.setText(curMin + ":" + curSec);
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();

    }

    private class exitFullScreen extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            mediaPlayerComponent.mediaPlayer().fullScreen().set(false);
            controlsPane.setVisible(true);
        }
    }

    private class PlayerButton extends JButton {

        private PlayerButton(boolean big, String tooltip) {

            Border margin;
            if(big){
                margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
            }else{
                margin = BorderFactory.createEmptyBorder(6, 6, 6, 6);
            }
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
            Border line = new LineBorder(Color.BLACK,2);
            Border compound = new CompoundBorder(line, margin);
            setBorder(compound);
            ToolTipManager.sharedInstance().setInitialDelay(0);
            setToolTipText(tooltip);
            setHideActionText(true);
        }
    }

    private Icon newIcon(String name) {
        return new ImageIcon(getClass().getResource( name + ".png"));
    }

    public String returnPath(){
        String filepath = new String();
        try {
            File myObj = new File("filepath.txt");
            Scanner myReader = new Scanner(myObj);

            filepath = myReader.nextLine();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return filepath;
    }

    public void cleanUp(){
        Data.current[0] = season;
        Data.current[1] = episode;
        Data.current[2] = (int) realSec - 2;

        stopLoop = true;
        mediaPlayerComponent.release();
        dispose();
    }

    class MyBasicSliderUI extends BasicSliderUI {
        Image im;
        public MyBasicSliderUI(JSlider js)
        {
            super(js);
        }
        public void paintThumb(Graphics g)
        {
            try
            {
                URL url = new URL(getClass().getResource("slider.png"), "slider.png");
                im = ImageIO.read(url);
            }
            catch(Exception e){e.printStackTrace();}
            Rectangle thumb = thumbRect;
            g.drawImage(im,thumb.x,thumb.y,null);
        }
    }

    public static void main(String[] args) throws IOException, FontFormatException {
        new Player(0, 0, 0);
    }
}