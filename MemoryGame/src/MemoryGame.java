import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemoryGame extends JFrame{
    JPanel mainPanel = new JPanel();
    JPanel headPanel = new JPanel();
    JPanel tablePanel = new JPanel();
    BorderLayout MainLayout = new BorderLayout();
    GridLayout smallLayout = new GridLayout(4,4,1,1);
    GridLayout normalLayout = new GridLayout(6,6,1,1);
    GridLayout bigLayout = new GridLayout(8,8,1,1);

    JLabel lbTime, lbScore, lbSkill;
    JButton newGame;
    HashMap<String, Integer> buttonsList = new HashMap<>();
    ArrayList<JButton> jButtonArrayList = new ArrayList<>();
    int sizeOfTable = 8;
    int result = 0;
    int countClicks = 0;
    int sec = 0;
    int minute = 0;
    static String time;
    String buttonNameOfFirst = "";
    JButton firstButton = new JButton();
    JButton secondButton = new JButton();
    final Choice choice = new Choice();

    private JPanel mainFrame;

    public MemoryGame() {
        this.setName("mainFrame");
        this.setTitle("Memory game");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        startGame();
        timer();
        this.setVisible(true);
    }

    public void initComponents(){
        mainPanel.setLayout(MainLayout);
        mainFrame.setLayout(MainLayout);

        choice.add("small size");
        choice.add("normal size");
        choice.add("big size");
        choice.setSize(100,30);

        choice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ("small size" == e.getItem()) createTable(8);

                if ("normal size" == e.getItem()) createTable(18);

                if ("big size" == e.getItem()) createTable(32);
            }
        });

        lbSkill = new JLabel();lbScore = new JLabel();lbTime = new JLabel();
        newGame = new JButton("New Game");

        lbSkill.add(choice);
        lbScore.setText("score: 0");
        lbTime.setText("time: 00:00");

        lbTime.setOpaque(true);
        lbScore.setOpaque(true);
        lbSkill.setOpaque(true);

        headPanel.add(lbTime);
        headPanel.add(newGame);
        headPanel.add(choice);
        headPanel.add(lbScore);

        mainPanel.add(headPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        this.setContentPane(mainPanel);
    }

    public void startGame() {
        sec = 0;
        minute = 0;
        createTable(sizeOfTable);
    }

    public void createTable(int n){
        result = 0;
        lbScore.setText("score: " + result);
        sizeOfTable = n;
        buttonsList.clear();
        jButtonArrayList.clear();
        tablePanel.removeAll();
        switch (n){
            case 8:  tablePanel.setLayout(smallLayout);break;
            case 18:  tablePanel.setLayout(normalLayout);break;
            case 32:  tablePanel.setLayout(bigLayout);break;
            default: tablePanel.setLayout(smallLayout);
        }

        repaint();
        for (int i = 10; i < n + 10; i++) {
            for (int j = 0; j < 2; j++) {
                JButton button = new JButton(String.valueOf(i) + String.valueOf(j));
                button.setName("button" + String.valueOf(i) + String.valueOf(j));
                button.setFont(new Font("Serif", Font.BOLD, 32));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        checkMatch(button.getName(), e.getSource());
                    }
                });
                tablePanel.add(button);
                //button.setText(" " + String.valueOf(i)); // show all numbers (testing cheat)
                button.setText("?");
                buttonsList.put(button.getName(), Integer.parseInt(String.valueOf(i) + String.valueOf(j)) );
                jButtonArrayList.add(button);

                Collections.shuffle(jButtonArrayList);
            }
        }
        for (int i = 0;i < jButtonArrayList.size();i++) {
            tablePanel.add(jButtonArrayList.get(i));
        }
        repaint();
    }

    public void checkMatch(String buttonName , Object firstButton1){

        if (countClicks == 2){
            countClicks = 0;
            firstButton.setText("?");
            secondButton.setText("?");
            firstButton.setBackground(null);
            secondButton.setBackground(null);
        }
        if (countClicks == 0){
            countClicks++;
            firstButton =  (JButton) firstButton1;
            firstButton.setText("" + firstButton.getName().substring(6,8));
            firstButton.setBackground(Color.red);

            return;
        }
        else if (countClicks == 1){
            countClicks++;
            secondButton =  (JButton) firstButton1;
            secondButton.setText("" + secondButton.getName().substring(6,8));
            secondButton.setBackground(Color.red);

            if (firstButton.getName().substring(0,8).contains(buttonName.substring(0,8))){
                result++;
                lbScore.setText("score: " + result);
                JButton hitButton1 = firstButton;
                JButton hitButton2 = secondButton;
                hitButton1.setText("" + firstButton.getName().substring(6,8));
                hitButton1.setBackground(Color.green);
                hitButton1.setEnabled(false);
                hitButton2.setText("" + secondButton.getName().substring(6,8));
                hitButton2.setBackground(Color.green);
                hitButton2.setEnabled(false);
                firstButton = new JButton();
                secondButton = new JButton();
            }
            if (result == sizeOfTable){
                JOptionPane.showMessageDialog(mainPanel,"YOU WIN" + "\ntime: " + minute + ":" + sec);
                startGame();
            }

        }
        else {
            countClicks = 0;
        }

    }
    public void timer(){
        Timer t = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    sec++;
                    if (sec >= 60) {
                        sec = 0;
                        minute++;
                    }
                    String time = "time: " + String.format("%02d", minute) + ":" + String.format("%02d", sec);
                    lbTime.setText(time);
            }
        });
        t.start();
    }


}
