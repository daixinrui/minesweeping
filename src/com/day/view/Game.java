package com.day.view;

import com.day.bean.Coordinate;
import com.day.bean.ScoreRecord;
import com.day.control.FileOperation;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * @Author: day
 * @Date: Created in 2020/11/30 上午11:21
 * @Description:
 * @Version: 1.0
 */
public class Game extends JFrame {

    private Box bottomHorizontalBox;    //底部水平箱，用来存放计时器

    private JPanel panel;    //  面板
    private JButton[][] buttons;        //网格按钮
    private boolean[][] isBomb;      //标记数组，用来标记是否为雷
    private int rows = 9;               //网格行数，初始时为9
    private int columns = 9;             //网格列数，初始时为9
    private int bombNum = 10;           //雷的总数，初始时为10
    private int remainingBomb = 10;     //剩余雷数

    private boolean[][] isChecked;     //标记数组，用来记录每个按钮是否被检查过

    private JLabel timeRecord;      //计时标签
    private JLabel secondCount;     //计时器
    private JLabel timeUnit;        //时间单位
    private Timer time;             //定时器

    private String winnerName;      //破纪录者姓名

    private JLabel showRemainingBomb;       //剩余雷数的标签
    private JLabel remainingBombNumber;     //剩余雷数的更新标签

    /**
     * 获取窗口本身，当作作为JDialog的拥有者使用
     * @return
     */
    private JFrame getMyself() {
        return  this;
    }

    /**
     * 初始化游戏窗口
     * @param rows 游戏行数
     * @param columns 游戏列数
     */
    public Game(int rows,int columns) {
        setTitle("扫雷");
        setSize(22 * rows, 22 * columns + 65);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initMenu();
        initGame(rows, columns);
        setVisible(true);
    }

    /**
     * 初始化顶部菜单栏
     */
    private void initMenu() {

        //菜单栏
        JMenuBar menuBar = new JMenuBar();

        JMenu menu1 = new JMenu("游戏");
        //"游戏"和"帮助"下拉菜单
        JMenu menu2 = new JMenu("帮助");

        //"新游戏"、"难度选择"、"排行榜"、"help"、"关于"菜单
        JMenuItem item1 = new JMenuItem("新游戏");
        JMenuItem item2 = new JMenuItem("难度选择");
        JMenuItem item3 = new JMenuItem("排行榜");
        JMenuItem item4 = new JMenuItem("Help");
        JMenuItem item5 = new JMenuItem("关于");

        menu1.add(item1);
        menu1.addSeparator();   //添加菜单项分界线
        menu1.add(item2);
        menu1.addSeparator();
        menu1.add(item3);
        menu2.add(item4);
        menu2.addSeparator();
        menu2.add(item5);

        menuBar.add(menu1);
        menuBar.add(menu2);

        setJMenuBar(menuBar);

        //设置快捷键
        item1.setAccelerator(KeyStroke.getKeyStroke("F1"));
        item2.setAccelerator(KeyStroke.getKeyStroke("F2"));
        item3.setAccelerator(KeyStroke.getKeyStroke("F3"));

        //设置监听器
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                time.stop();
                RestartGame(rows, columns);
            }
        });

        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                time.stop();
                DifficultySelection();
            }
        });

        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                time.stop();
                Rank();
            }
        });

        item4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                time.stop();
                Help();
            }
        });

        item5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                time.stop();
                About();
            }
        });


    }

    /**
     * 初始化游戏界面
     * @param rows 游戏行数
     * @param columns 游戏列数
     */
    private void initGame(int rows, int columns) {

        isChecked = new boolean[rows][columns];

        //初始化标志数组
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                isChecked[i][j] = false;
            }
        }
        setSize(22 * rows, 22 * columns + 65);      //设置游戏窗口大小
        setTimer();     //设置计时器
        setBombNumber();
        setButtons();       //设置矿区按钮并埋雷
    }

    /**
     * 设置初始化游戏时雷的总数
     */
    private void setBombNumber() {
        if (rows == 9) {
            remainingBomb = bombNum = 10;
            remainingBombNumber.setText("10");
        } else if (rows == 16) {
            remainingBomb = bombNum = 40;
            remainingBombNumber.setText("40");
        } else {
            remainingBomb = bombNum = 99;
            remainingBombNumber.setText("99");
        }
    }

    /**
     * 计时器，记录游戏从开始到结束所用时间，单位为秒(s)
     */
    private void setTimer() {
        timeRecord = new JLabel("用时：");
        secondCount = new JLabel("0");
        timeUnit = new JLabel("s");
        showRemainingBomb = new JLabel("剩余雷数：");
        remainingBombNumber = new JLabel(String.valueOf(remainingBomb));


        //设置计时器，每过一秒时间加一，以s为单位
        time = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int second = Integer.parseInt(secondCount.getText());
                secondCount.setText(String.valueOf(second + 1));
            }
        });

        //将计时器的组成部分添加到底部水平箱中
        bottomHorizontalBox = Box.createHorizontalBox();
        bottomHorizontalBox.add(Box.createHorizontalStrut(10));
        bottomHorizontalBox.add(timeRecord);
        bottomHorizontalBox.add(secondCount);
        bottomHorizontalBox.add(timeUnit);
        bottomHorizontalBox.add(Box.createHorizontalStrut(50));
        bottomHorizontalBox.add(showRemainingBomb);
        bottomHorizontalBox.add(remainingBombNumber);

        add(bottomHorizontalBox, BorderLayout.SOUTH);       //将水平箱添加到游戏窗口的底部
    }

    /**
     * 设置矿区按钮，并为每个按钮添加监听器，点击不同按钮触发不同反应
     */
    private void setButtons() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns,2,2));     //将面板设置为网格状布局，并在中间留下2单位的间隔

        buttons = new JButton[rows][columns];
        isBomb = new boolean[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/block.png"));  //设置按钮图案
                isBomb[i][j] = false;       //初始默认没有埋雷

                //为每个按钮添加监听器以触发不同结果
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        //点击游戏区域内的任意一个按钮，开始计时
                        if (secondCount.getText().equals("0")) {
                            time.start();
                        }

                        JButton triggerButton = (JButton) e.getSource();    //获取触发的按钮

                        for (int k = 0; k < rows; k++) {
                            for (int l = 0; l < columns; l++) {
                                if (triggerButton == buttons[k][l]) {       //找到被点击的按钮
                                    if (e.getButton() == MouseEvent.BUTTON1) {
                                        if (isBomb[k][l]) {
                                            ExplosionAllBomb();     //引爆所有的雷
                                            time.stop();    //停止计时
                                            int choice = Prompt.LoseGame(getMyself());//弹窗，提示游戏失败，提供两个选项：退出游戏，再来一局

                                            //判断玩家选择
                                            if (choice == 0) {
                                                System.exit(0);
                                            } else {
                                                RestartGame(rows, columns);
                                            }
                                        } else {
                                            SearchAround(k, l);     //搜索附近的区域，显示出不是雷的部分
                                        }
                                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                                        if (isBomb[k][l]) {
                                            Flag(k, l);     //插旗子，减剩余雷数
                                            isChecked[k][l] = true;     //设置标志位为已检查

                                            //判断剩余雷数，获取游戏结束的条件
                                            if (remainingBomb == 0) {
                                                time.stop();
                                                //比较用时，如果比排行榜的小，就弹窗让玩家输入名字，并记录进排行榜
                                                String secondCountText = secondCount.getText();
                                                int timeCost = Integer.parseInt(secondCountText);

                                                if (rows == 9) {
                                                    RankOperation(timeCost, "/Users/day/IdeaProjects/minesweeping/src/EasyModeRank");
                                                } else if (rows == 16) {
                                                    RankOperation(timeCost, "/Users/day/IdeaProjects/minesweeping/src/MediumModeRank");
                                                } else {
                                                    RankOperation(timeCost, "/Users/day/IdeaProjects/minesweeping/src/DifficultModeRank");
                                                }
                                            }
                                        } else {
                                            ExplosionAllBomb();     //引爆所有的雷
                                            time.stop();    //停止计时
                                            int choice = Prompt.LoseGame(getMyself());//弹窗，提示游戏失败，提供两个选项：退出游戏，再来一局

                                            //判断玩家选择
                                            if (choice == 0) {
                                                System.exit(0);
                                            } else {
                                               RestartGame(rows, columns);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

                panel.add(buttons[i][j]);       //将矿区按钮添加到面板容器中
            }
        }

        BuryBomb();     //埋雷

        add(panel, BorderLayout.CENTER);    //将面板容器添加到窗口的中心区域
    }

    /**
     * 游戏胜利时，若用时破纪录，则弹窗提示玩家输入姓名，记录进排行榜
     */
    private void WinnerRecord() {
        JDialog winnerDialog = new JDialog(this, "游戏胜利", true);

        JPanel winnerPanel = new JPanel();

        JLabel label1 = new JLabel("恭喜您破纪录了！留下大名！");
        JLabel label2 = new JLabel("姓名：");

        JTextField winnerNameText = new JTextField(10);

        JButton winButton = new JButton("确定");

        winButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                winnerName = winnerNameText.getText();
                winnerDialog.dispose();
            }
        });

        Box box1 = Box.createVerticalBox();
        Box box2 = Box.createHorizontalBox();
        Box box3 = Box.createHorizontalBox();

        winnerDialog.setBounds(700, 400, 250, 180);
        winnerDialog.setLocationRelativeTo(null);
        winnerDialog.setDefaultCloseOperation(winnerDialog.DISPOSE_ON_CLOSE);

        box2.add(label2);
        box2.add(winnerNameText);

        box1.add(Box.createVerticalStrut(20));
        box1.add(label1);
//        box1.add(Box.createVerticalStrut(10));
        box1.add(box2);
        box1.add(Box.createVerticalStrut(20));
        box3.add(winButton);

        winnerPanel.add(box1);
        winnerPanel.add(box2);
        winnerPanel.add(box3);

        winnerDialog.setContentPane(winnerPanel);

        winnerDialog.setVisible(true);
    }

    /**
     * 插旗子，减雷数
     * @param x 被扫出的雷的横坐标
     * @param y 被扫出的雷的纵坐标
     */
    private void Flag(int x, int y) {
        buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/redFlag.png"));
        if (!isChecked[x][y]){
            remainingBomb--;
            remainingBombNumber.setText(String.valueOf(remainingBomb));
        }
        //更新显示界面的剩余雷数
    }

    /**
     * 扫描四周，显示没有雷的区域，并标出周围雷的数量
     * @param x 目标按钮的横坐标
     * @param y 目标按钮的纵坐标
     */
    private void SearchAround(int x, int y) {

        int bombNumber = 0;     //初始时周围的雷的数量

        isChecked[x][y] = true;     //将自身设置为被检查过，避免递归时重复检查形成死循环

        //获取周围位置的坐标
        Coordinate[] nearCoordinate = getNearCoordinate(x, y);

        //对比周围位置和雷的坐标，若重合，则说明周围有雷，记录雷的数量
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (isBomb[i][j]) {
                    for (int k = 0; k < nearCoordinate.length; k++) {
                        if (nearCoordinate[k].getAbscissa() == i && nearCoordinate[k].getOrdinate() == j) {
                            bombNumber++;
                            break;
                        }
                    }
                }
            }
        }

        //对不同的雷的数量更改不同的图片
        switch (bombNumber) {
            case 1:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/1.png"));
                break;
            case 2:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/2.png"));
                break;
            case 3:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/3.png"));
                break;
            case 4:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/4.png"));
                break;
            case 5:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/5.png"));
                break;
            case 6:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/6.png"));
                break;
            case 7:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/7.png"));
                break;
            case 8:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/8.png"));
                break;
            case 0:
                buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/0.png"));

                //周围没有雷时，递归下去扫描周围的雷
                for (int i = 0; i < nearCoordinate.length; i++) {
                    if (nearCoordinate[i].getAbscissa() == -1 || nearCoordinate[i].getOrdinate() == -1 || isChecked[nearCoordinate[i].getAbscissa()][nearCoordinate[i].getOrdinate()])
                        continue;
                    SearchAround(nearCoordinate[i].getAbscissa(), nearCoordinate[i].getOrdinate());
                }
                break;
        }
    }

    /**
     * 获取以(x,y)为中心的周围8个点的坐标参数，超出边界范围的点的横纵坐标一律设为-1
     * @param x 中心横坐标
     * @param y 中心纵坐标
     * @return
     */
    private Coordinate[] getNearCoordinate(int x, int y) {
        Coordinate[] coordinates = new Coordinate[8];

        //将周围所有的横纵坐标都初始化为-1
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = new Coordinate();
        }

        if (x >= 1 && y >= 1) {
            coordinates[0].setAbscissa(x - 1);
            coordinates[0].setOrdinate(y - 1);
        }

        if (x >= 1) {
            coordinates[1].setAbscissa(x - 1);
            coordinates[1].setOrdinate(y);
        }

        if (x >= 1 && y + 1 < columns) {
            coordinates[2].setAbscissa(x - 1);
            coordinates[2].setOrdinate(y + 1);
        }

        if (x >= 1) {
            coordinates[3].setAbscissa(x);
            coordinates[3].setOrdinate(y - 1);
        }

        if (y + 1 < columns) {
            coordinates[4].setAbscissa(x);
            coordinates[4].setOrdinate(y + 1);
        }

        if (x + 1 < rows && y >= 1) {
            coordinates[5].setAbscissa(x + 1);
            coordinates[5].setOrdinate(y - 1);
        }

        if (x + 1 < rows) {
            coordinates[6].setAbscissa(x + 1);
            coordinates[6].setOrdinate(y);
        }

        if (x + 1 < rows && y + 1 < columns) {
            coordinates[7].setAbscissa(x + 1);
            coordinates[7].setOrdinate(y + 1);
        }

        return coordinates;
    }

    /**
     * 引爆所有的雷
     */
    private void ExplosionAllBomb() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (isBomb[i][j]) {
                    buttons[i][j].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/bombReveal.png"));
                }
            }
        }
    }

    /**
     * 埋雷操作
     */
    private void BuryBomb() {
        for (int k = 0; k < bombNum; k++) {
            while (true) {
                int x = (int) (Math.random() * rows);     //随机行
                int y = (int) (Math.random() * columns);  //随机列

                //若本来没有雷，则在该按钮下埋雷
                if (!isBomb[x][y]){
                    isBomb[x][y] = true;
//                    buttons[x][y].setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/bomb.png"));
                    break;
                }
            }
        }
    }

    /**
     * 难度选择界面
     */
    private void DifficultySelection() {

        JFrame difficultySelectionFrame = new JFrame();

        JPanel difficultyChoicePanel = new JPanel();

        ButtonGroup buttonGroup = new ButtonGroup();

        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        JRadioButton easyButton = new JRadioButton("初级：9X9网格，10颗雷");
        JRadioButton mediumButton = new JRadioButton("中级：16X16网格，40颗雷");
        JRadioButton difficultButton = new JRadioButton("高级：25X25网格，99颗雷");

        final JRadioButton[] selectedButton = {easyButton};    //记录被选中的单选按钮，默认为简单模式

        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();

        difficultySelectionFrame.setTitle("难度选择");      //设置窗口标题
        difficultySelectionFrame.setBounds(700, 400, 300, 230);     //设置窗口大小与位置
        difficultySelectionFrame.setLocationRelativeTo(null);
        difficultySelectionFrame.setDefaultCloseOperation(difficultySelectionFrame.DISPOSE_ON_CLOSE);       //设置默认关闭方式

        selectedButton[0].setSelected(true);    //设置默认情况下选择了easyButton

        easyButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                selectedButton[0] = (JRadioButton) itemEvent.getSource();
            }
        });

        mediumButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                selectedButton[0] = (JRadioButton) itemEvent.getSource();
            }
        });

        difficultButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                selectedButton[0] = (JRadioButton) itemEvent.getSource();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedButton[0] == easyButton) {
                    rows = 9;
                    columns = 9;
                    bombNum = 10;
                    remainingBomb = 10;
                } else if (selectedButton[0] == mediumButton) {
                    rows = 16;
                    columns = 16;
                    bombNum = 40;
                    remainingBomb = 40;
                } else {
                    rows = 25;
                    columns = 25;
                    bombNum = 99;
                    remainingBomb = 99;
                }

                RestartGame(rows, columns);
                difficultySelectionFrame.dispose();
            }
        });

        buttonGroup.add(easyButton);
        buttonGroup.add(mediumButton);
        buttonGroup.add(difficultButton);

        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(easyButton);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(mediumButton);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(difficultButton);
        verticalBox.add(Box.createVerticalStrut(30));

        horizontalBox.add(confirmButton);
        horizontalBox.add(Box.createHorizontalStrut(80));
        horizontalBox.add(cancelButton);

        difficultyChoicePanel.add(verticalBox);
        difficultyChoicePanel.add(horizontalBox);

        difficultySelectionFrame.setContentPane(difficultyChoicePanel);

        difficultySelectionFrame.setVisible(true);
    }

    /**
     * 重新开始游戏，初始化整个游戏
     * @param rows 游戏行数
     * @param columns 游戏列数
     */
    private void RestartGame(int rows, int columns) {
        remove(panel);
        remove(bottomHorizontalBox);
        initGame(rows, columns);
        setVisible(true);
    }

    /**
     * 排行操作，对比玩家用时与排行榜的最长用时，如果玩家用时小于排行榜的最长用时，则更新排行榜，排行榜中只有前三名的成绩
     * @param timeCost 玩家用时
     * @param pathName 文件路径
     */
    private void RankOperation(int timeCost, String pathName) {
        ScoreRecord[] records = FileOperation.RankRead(pathName);

        List<ScoreRecord> list = Arrays.asList(records);
        Collections.sort(list);
        records = list.toArray(new ScoreRecord[0]);

        if (timeCost > records[2].getTimeRecord()) {

            int choice = Prompt.WinGame(getMyself());//弹窗，游戏胜利，提供选项：退出游戏，再来一局

            //判断玩家选择
            if (choice == 0) {
                System.exit(0);
            } else {
                RestartGame(rows, columns);
            }
        } else {
            //弹窗，让玩家输入姓名，记录进排行榜
            WinnerRecord();

            records[2].setName(winnerName);
            records[2].setTimeRecord(timeCost);

            List<ScoreRecord> list1 = Arrays.asList(records);
            Collections.sort(list1);
            records = list1.toArray(new ScoreRecord[records.length]);

            FileOperation.RankWrite(records, pathName);

            RestartGame(rows, columns);
        }
    }

    /**
     * 排行榜界面，根据不同的游戏难度显示不同的排行榜
     */
    private void Rank() {

        JDialog rankDialog = new JDialog(getMyself(), true);
        JPanel rankPanel = new JPanel();

        JLabel recordFirst = new JLabel();
        JLabel recordSecond = new JLabel();
        JLabel recordThird = new JLabel();

        Box box = Box.createVerticalBox();

        JButton confirmButton = new JButton("确定");

        rankDialog.setBounds(700, 400, 250, 160);
        rankDialog.setDefaultCloseOperation(rankDialog.DISPOSE_ON_CLOSE);
        rankDialog.setLocationRelativeTo(null);

        if (rows == 9) {
            rankDialog.setTitle("初级模式排行榜");

            ShowRank(recordFirst, recordSecond, recordThird, "/Users/day/IdeaProjects/minesweeping/src/EasyModeRank");

        } else if (rows == 16) {
            rankDialog.setTitle("中级模式排行榜");

            ShowRank(recordFirst, recordSecond, recordThird, "/Users/day/IdeaProjects/minesweeping/src/MediumModeRank");

        } else {
            rankDialog.setTitle("高级模式排行榜");

            ShowRank(recordFirst, recordSecond, recordThird, "/Users/day/IdeaProjects/minesweeping/src/DifficultModeRank");

        }

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                rankDialog.dispose();
                time.start();
            }
        });

        box.add(Box.createVerticalStrut(10));
        box.add(recordFirst);
        box.add(Box.createVerticalStrut(5));
        box.add(recordSecond);
        box.add(Box.createVerticalStrut(5));
        box.add(recordThird);
        box.add(Box.createVerticalStrut(20));

        box.add(confirmButton);

        rankPanel.add(box);

        rankDialog.setContentPane(rankPanel);

        rankDialog.setVisible(true);
    }

    /**
     * 作为Rank()的内部方法使用，用来读取不同的排行榜文件
     * @param recordFirst 第一名
     * @param recordSecond 第二名
     * @param recordThird 第三名
     * @param pathName 文件路径
     */
    private void ShowRank(JLabel recordFirst, JLabel recordSecond, JLabel recordThird, String pathName) {
        ScoreRecord[] records = FileOperation.RankRead(pathName);

        List<ScoreRecord> list = Arrays.asList(records);
        Collections.sort(list);
        records = list.toArray(new ScoreRecord[records.length]);

        recordFirst.setText("第一名:" + records[0].getName() + "  用时:" + records[0].getTimeRecord() + "s");
        recordSecond.setText("第二名:" + records[1].getName() + "  用时:" + records[1].getTimeRecord() + "s");
        recordThird.setText("第三名:" + records[2].getName() + "  用时:" + records[2].getTimeRecord() + "s");
    }

    /**
     * 关于界面，用来显示游戏版本和开发者信息
     */
    private void About() {

        JDialog aboutDialog = new JDialog(getMyself(), "关于", true);
        aboutDialog.setBounds(700, 400, 280, 240);
        aboutDialog.setLocationRelativeTo(null);
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        Box jBox = Box.createVerticalBox();
        JButton button = new JButton("确定");
        JLabel title = new JLabel("本程序由Java语言编写！", JLabel.CENTER);

        jBox.add(Box.createHorizontalStrut(35));
        title.setIcon(new ImageIcon("/Users/day/IdeaProjects/minesweeping/images/index.jpg"));
        jBox.add(title);
        jBox.add(Box.createHorizontalStrut(5));
        jBox.add(new JLabel("——————————————", JLabel.CENTER));
        jBox.add(new JLabel("名称：扫雷"));
        jBox.add(Box.createHorizontalStrut(10));
        jBox.add(new JLabel("版本：1.0"));
        jBox.add(Box.createVerticalStrut(5));
        jBox.add(new JLabel("开发者：Day"));
        jBox.add(Box.createVerticalStrut(5));
        jBox.add(new JLabel("联系方式：351277210@qq.com"));
        jBox.add(Box.createVerticalStrut(5));
        jBox.add(new JLabel("版权所有，未经许可，禁止作为商用！",JLabel.CENTER));
        jBox.add(Box.createVerticalStrut(8));

        panel2.add(button);
        jBox.add(panel2);
        panel1.add(jBox);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aboutDialog.dispose();
                time.start();
            }
        });

        aboutDialog.add(panel1);
        aboutDialog.setVisible(true);
    }

    /**
     * 帮助界面，用于显示游戏规则，帮助玩家快速上手
     */
    private void Help() {
        JDialog helpDialog = new JDialog(getMyself(), "游戏规则", true);

        helpDialog.setBounds(700, 400, 400, 200);
        helpDialog.setLocationRelativeTo(null);
        helpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel rules1 = new JLabel("点击左键翻开一格，如果踩到地雷，则游戏结束；");
        JLabel rules2 = new JLabel("如果没有踩到雷，则显示该位置周围的8块区域内的地雷数。");
        JLabel rules3 = new JLabel("点击右键标记地雷，如果标记正确则游戏继续；");
        JLabel rules4 = new JLabel("如果标记错误则游戏结束。");

        rules1.setFont(new Font("宋体", Font.PLAIN, 13));
        rules2.setFont(new Font("宋体", Font.PLAIN, 13));
        rules3.setFont(new Font("宋体", Font.PLAIN, 13));
        rules4.setFont(new Font("宋体", Font.PLAIN, 13));

        Box verticalBox = Box.createVerticalBox();

        JPanel rulePanel = new JPanel();
        JPanel panel0 = new JPanel();

        JButton confirmButton = new JButton("确定");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                helpDialog.dispose();
                time.start();
            }
        });

        verticalBox.add(Box.createVerticalStrut(20));
        verticalBox.add(rules1);
        verticalBox.add(Box.createVerticalStrut(5));
        verticalBox.add(rules2);
        verticalBox.add(Box.createVerticalStrut(5));
        verticalBox.add(rules3);
        verticalBox.add(Box.createVerticalStrut(5));
        verticalBox.add(rules4);
        verticalBox.add(Box.createVerticalStrut(20));

        panel0.add(confirmButton);
        verticalBox.add(panel0);

        rulePanel.add(verticalBox);

        helpDialog.setContentPane(rulePanel);

        helpDialog.setVisible(true);
    }
}