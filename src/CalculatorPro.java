import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CalculatorPro {
    private static final String PI = "3.141592653589793238462643383279";    //圆周率 π
    private static final String E = "2.718281828459045235360287471352";     //自然对数底数 e
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_WIDTH = screenSize.width;        //获取屏宽
    public static final int SCREEN_HEIGHT = screenSize.height;      //获取屏高

    private static final int DEFAULT_WIDTH = 1330;          //实际宽度 △ = -5
    private static final int DEFAULT_HEIGHT = 780;          //实际高度 △ = -56
    private static final int LEFT_MARGIN = 8;               //左边距
    private static final int TOP_MARGIN = 10;               //上边距
    private static final int LEFT_WIDTH = 800;              //左宽度
    private static final int LEFT_RIGHT_SPACE = 10;         //左右间隔
    private static final int LEFT_TOP_HEIGHT = 230;         //结果高度
    private static final int TOP_NUMBER_SPACE = 20;         //键区结果间隔
    private static final int RESULT_FIELD_LEFT_SPACE = 22;         //键区结果左间隔
    private static final int RESULT_FIELD_BOTTOM_SPACE = 20;       //键区结果底间隔
    private static final int RESULT_FIELD_HEIGHT = 80;       //键区结果高度
    private static final int BUTTON_PANEL_HEIGHT = 50;       //按钮行高度
    private static final int BUTTON_PANEL_SPACE = 15;        //按钮行间距

    private static final Font EQUATION_FONT = new Font("", Font.PLAIN, 24);     //算式文本字体
    private static final Font RESULT_FONT = new Font("", Font.BOLD, 32);        //结果文本字体
    private static final Font BUTTON_FONT_ENGLISH = new Font("", 3, 18);        //默认按钮字体
    private static final Font BUTTON_FONT_ENGLISH_lONG = new Font("", 3, 16);   //长按钮字体
    private static final Font BUTTON_FONT_CHINESE = new Font("", 1, 20);        //中文字体
    private static final Font BUTTON_FONT_NUMBER = new Font("", 1, 36);         //数字字体
    private static final Font resultFieldFont = new Font("", Font.BOLD, 36);    //结果文本字体

    private static ArrayList<JButton> rowOne = new ArrayList<>();       //以下，1-7 行面板的按钮容器
    private static ArrayList<JButton> rowTwo = new ArrayList<>();
    private static ArrayList<JButton> rowThree = new ArrayList<>();
    private static ArrayList<JButton> rowFour = new ArrayList<>();
    private static ArrayList<JButton> rowFive = new ArrayList<>();
    private static ArrayList<JButton> rowSix = new ArrayList<>();
    private static ArrayList<JButton> rowSeven = new ArrayList<>();

    private static JPanel buttonPanel01 = new JPanel();     //以下，1-7 行面板的声明
    private static JPanel buttonPanel02 = new JPanel();
    private static JPanel buttonPanel03 = new JPanel();
    private static JPanel buttonPanel04 = new JPanel();
    private static JPanel buttonPanel05 = new JPanel();
    private static JPanel buttonPanel06 = new JPanel();
    private static JPanel buttonPanel07 = new JPanel();
    private static LayoutManager buttonPanelLayout = new GridLayout(1, 10, 6, 0);   //设置按钮面板布局

    private static JLabel equationLabel = new JLabel("", SwingConstants.RIGHT);     //算式文本标签
    private static JLabel resultLabel = new JLabel("0", SwingConstants.RIGHT);      //结果文本标签

    private static StringBuffer tempGeneral = new StringBuffer("");       //通用字符串缓存变量
    private static StringBuffer equationText = new StringBuffer("");      //算式文本变量
    private static StringBuffer resultText = new StringBuffer("0");       //结果文本变量
    private static StringBuffer lastResult = new StringBuffer("");        //上次的计算结果，未赋值，默认 null
    private static JButton[] lastButtonClick = new JButton[1];      //上次点击的按钮，未赋值，默认 null
    private static JButton[] lastOperator = new JButton[1];         //上次点击的操作符，未赋值，默认 null
    private static boolean inputStatus = false;

    static {
        new ProcessResult();        //静态加载 ProcessResult 类，避免按等号键时才加载，造成等待时间。
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("计算器专业版 V1.0");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocation((SCREEN_WIDTH - DEFAULT_WIDTH) / 2 + 150, (SCREEN_HEIGHT - DEFAULT_HEIGHT) / 2);
        frame.setResizable(false);
        frame.setLayout(null);          //使用绝对位置进行布局，不允许默认布局。

        //Windows 观感
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();           //新建菜单栏对象 menuBar
        frame.setJMenuBar(menuBar);                  //把菜单栏对象添加到框架中。（否则不会显示菜单栏）

        JMenu fileMenu = new JMenu("文件(F)");
        fileMenu.setMnemonic('F');                   //设置 fileMenu 的助记符 F。（Alt + F 快捷打开）

        JMenuItem quitItem = fileMenu.add(new AbstractAction("退出(Q)") {     //菜单项 退出
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);                                     //被选中时退出程序
            }
        });
        quitItem.setMnemonic('Q');                                  //设置助记符 Q
        menuBar.add(fileMenu);                                      //把 fileMenu 菜单添加到菜单栏上

        JMenu helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic('H');
        JMenuItem helpItem = helpMenu.add(new JMenuItem("查看帮助(V)"));
        helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        helpItem.setMnemonic('V');
        helpItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {        // TODO: 2015/12/18 帮助说明。
                JOptionPane.showMessageDialog(frame, "使用说明：\n\n" +
                        "快捷键：\n\n");
            }
        });

        JMenuItem aboutItem = helpMenu.add(new JMenuItem("关于(A)..."));
        aboutItem.setMnemonic('A');
        aboutItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {        // TODO: 2015/12/18 关于详情
                JOptionPane.showMessageDialog(frame, "Windows 记事本\n\n" +
                        "版本：V1.0\n更新日期：2015年12月13日\n\n" +
                        "作者：jien217\nEmail:jien217@qq.com");
            }
        });
        menuBar.add(helpMenu);

        /**
         * 结果键区
         */
        JPanel leftTopPanel = new JPanel();
        leftTopPanel.setBackground(new Color(250, 230, 195));
        leftTopPanel.setBounds(LEFT_MARGIN, TOP_MARGIN, LEFT_WIDTH, LEFT_TOP_HEIGHT);
        Border topBorder = BorderFactory.createEtchedBorder();
        Border topBorderTittle = BorderFactory.createTitledBorder(topBorder, "计算结果");
        leftTopPanel.setBorder(topBorderTittle);

        resultLabel.setFont(resultFieldFont);
        resultLabel.setBounds(LEFT_MARGIN + RESULT_FIELD_LEFT_SPACE - 5,
                TOP_MARGIN + LEFT_TOP_HEIGHT - RESULT_FIELD_BOTTOM_SPACE - RESULT_FIELD_HEIGHT + 25,
                LEFT_MARGIN + LEFT_WIDTH - RESULT_FIELD_LEFT_SPACE * 2, RESULT_FIELD_HEIGHT);
        frame.add(resultLabel);

        equationLabel.setFont(EQUATION_FONT);
        resultLabel.setFont(RESULT_FONT);

        leftTopPanel.setLayout(new GridLayout(2, 1));
        leftTopPanel.add(equationLabel);
        frame.add(leftTopPanel);

        /**
         * 按钮区
         * buttonPanel02        TODO:   按钮区面板 ---1111111---，用于快速定位，不做它用。
         */
        buttonPanel01.setLayout(buttonPanelLayout);
        buttonPanel01.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);

        JButton btScience = new JButton("科学");
        JButton btCoder = new JButton("编程");
        JButton btStatistics = new JButton("统计");
        JButton btConstants = new JButton("常数");
        JButton btSave = new JButton("保存");
        JButton btDaiDing = new JButton("精度");
        JButton btFSE = new JButton("FSE");
        JButton btDRG = new JButton("Deg");
        JButton btRad = new JButton("Rad");
        JButton btGrad = new JButton("Grad");

        addRowOne(btScience);
        addRowOne(btCoder);
        addRowOne(btStatistics);
        addRowOne(btConstants);
        addRowOne(btSave);
        addRowOne(btDaiDing);
        addRowOne(btFSE);
        addRowOne(btDRG);
        addRowOne(btRad);
        addRowOne(btGrad);

        for (JButton button : rowOne) {
            button.setFont(BUTTON_FONT_CHINESE);
            button.setFocusable(false);
            button.setBackground(new Color(222, 121, 50));
        }
        btFSE.setFont(BUTTON_FONT_ENGLISH);
        btDRG.setFont(BUTTON_FONT_ENGLISH);
        btRad.setFont(BUTTON_FONT_ENGLISH);
        btGrad.setFont(BUTTON_FONT_ENGLISH);

        frame.add(buttonPanel01);


        /**
         * buttonPanel02        TODO:   按钮区面板 ---2222222---，用于快速定位，不做它用。
         */
        buttonPanel02.setLayout(buttonPanelLayout);
        buttonPanel02.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE + BUTTON_PANEL_HEIGHT + BUTTON_PANEL_SPACE),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);

        JButton btRand = new JButton("Rand");
        btRand.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.delete(0, resultText.length());      //将结果文本置零
                resultText.append(Math.random());               //添加随机数到结果文本
                resultLabel.setText(resultText.toString());     //显示结果文本
                inputStatus = false;            //非数字输入状态
                lastButtonClick[0] = btRand;    //上次点击
            }
        });

        JButton btPI = new JButton("π");
        btPI.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.delete(0, resultText.length());
                resultText.append(CalculatorPro.PI);
                resultLabel.setText(resultText.toString());
                inputStatus = false;
                lastButtonClick[0] = btPI;
            }
        });

        JButton btE = new JButton("e");
        btE.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.delete(0, resultText.length());
                resultText.append(CalculatorPro.E);
                resultLabel.setText(resultText.toString());
                inputStatus = false;
                lastButtonClick[0] = btE;
            }
        });

        JButton btEPowerX = new JButton("<html><font size=\"6\">e</font><sup>x</sup></html>");
        JButton btLn = new JButton("ln");
        JButton btDMS = new JButton("DMS");
        JButton btDMStoFraction = new JButton("DMS'");
        JButton btFraction = new JButton("a/b");
        JButton btMOD = new JButton("求余");

        JButton btCA = new JButton("CA");
        btCA.addActionListener(new AbstractAction() {               //CA 键的逻辑没有问题。
            @Override
            public void actionPerformed(ActionEvent e) {
                equationText.delete(0, equationText.length());      //删除算式文本
                resultText.delete(0, resultText.length());          //删除结果文本
                inputStatus = true;                 //数字输入状态 true
                resultLabel.setText("0");           //结果文本的显示置零
                equationLabel.setText("");          //算式文本无显示
                lastButtonClick[0] = btCA;          //上一次按键：CA
                lastOperator[0] = null;                 //上一次操作符：null
                lastResult = new StringBuffer("");     //上一次结果：空
            }
        });

        addRowTwo(btRand);
        addRowTwo(btPI);
        addRowTwo(btE);
        addRowTwo(btEPowerX);
        addRowTwo(btLn);
        addRowTwo(btDMS);
        addRowTwo(btDMStoFraction);
        addRowTwo(btFraction);
        addRowTwo(btMOD);
        addRowTwo(btCA);

        for (JButton button : rowTwo) {
            button.setFont(BUTTON_FONT_ENGLISH);
            button.setFocusable(false);
        }
        btRand.setFont(BUTTON_FONT_ENGLISH_lONG);
        btPI.setFont(new Font("Times New Roman", 1, 24));           //btPI
        btE.setFont(new Font("", 1, 24));                           //btE
        btDMS.setFont(BUTTON_FONT_ENGLISH_lONG);
        btDMStoFraction.setFont(BUTTON_FONT_ENGLISH_lONG);
        btMOD.setFont(BUTTON_FONT_CHINESE);
        btCA.setForeground(new Color(230, 50, 50));                  //btC
        btCA.setFont(new Font("", 1, 24));                  //btC

        frame.add(buttonPanel02);


        /**
         * buttonPanel03        TODO:   按钮区面板 ---3333333---，用于快速定位，不做它用。
         */
        buttonPanel03.setLayout(buttonPanelLayout);
        buttonPanel03.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE + BUTTON_PANEL_HEIGHT * 2 + BUTTON_PANEL_SPACE * 2),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);

        JButton btFactorial = new JButton("n!");
        btFactorial.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputStatus = false;
                makeStringBuilderEmpty(resultText);
                resultText.append(resultLabel.getText());
                if (isInteger(resultText.toString())) {
                    makeStringBuilderEmpty(equationText);
                    equationText.append(resultText).append("!=");
                    equationLabel.setText(equationText.toString());

                    makeStringBuilderEmpty(tempGeneral);
                    tempGeneral.append(calcFactorial(Integer.valueOf(resultText.toString())));
                    resultLabel.setText(tempGeneral.toString());
                    resultText = tempGeneral;
                }
            }
        });

        JButton btnCr = new JButton("nCr");
        JButton btnPr = new JButton("nPr");
        JButton bt10PowerX = new JButton("<html>10<sup>x</sup></html>");
        JButton btLog10 = new JButton("<html>log<sub>10</sub></html>");
        JButton btLog = new JButton("log");
        JButton btLeftBracket = new JButton("(");
        JButton btRightBracket = new JButton(")");
        JButton btPercent = new JButton("%");
        JButton btCE = new JButton("CE");
        btCE.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.delete(0, resultText.length());
                resultText.append(0);
                inputStatus = true;
                resultLabel.setText("0");
            }
        });

        addRowThree(btFactorial);
        addRowThree(btnCr);
        addRowThree(btnPr);
        addRowThree(bt10PowerX);
        addRowThree(btLog10);
        addRowThree(btLog);
        addRowThree(btLeftBracket);
        addRowThree(btRightBracket);
        addRowThree(btPercent);
        addRowThree(btCE);

        for (JButton button : rowThree) {
            button.setFont(BUTTON_FONT_ENGLISH);
            button.setFocusable(false);
        }
        btLeftBracket.setFont(new Font("", 1, 18));
        btRightBracket.setFont(new Font("", 1, 18));
        btCE.setForeground(new Color(230, 50, 50));
        btCE.setFont(new Font("", 1, 24));

        frame.add(buttonPanel03);

        /**
         * buttonPanel04        TODO:   按钮区面板 ---4444444---，用于快速定位，不做它用。
         */
        buttonPanel04.setLayout(buttonPanelLayout);
        buttonPanel04.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE + BUTTON_PANEL_HEIGHT * 3 + BUTTON_PANEL_SPACE * 3),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);

        JButton btSin = new JButton("sin");
        JButton btCos = new JButton("cos");
        JButton btTan = new JButton("tan");
        JButton btXSquare = new JButton("x²");
        JButton btSQRT = new JButton("√");

        JButton bt7 = new JButton("7");
        bt7.addActionListener(new InputNumberAction(7));

        JButton bt8 = new JButton("8");
        bt8.addActionListener(new InputNumberAction(8));

        JButton bt9 = new JButton("9");
        bt9.addActionListener(new InputNumberAction(9));

        JButton btDivide = new JButton("÷");
        btDivide.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operatorClicked("÷");
            }
        });

        JButton btBackspace = new JButton("退格");
        btBackspace.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backspaceButton();
            }
        });

        addRowFour(btSin);
        addRowFour(btCos);
        addRowFour(btTan);
        addRowFour(btXSquare);
        addRowFour(btSQRT);
        addRowFour(bt7);
        addRowFour(bt8);
        addRowFour(bt9);
        addRowFour(btDivide);
        addRowFour(btBackspace);

        for (JButton button : rowFour) {
            button.setFont(BUTTON_FONT_ENGLISH);
            button.setFocusable(false);
        }
        btXSquare.setFont(new Font("", 1, 23));
        bt7.setFont(BUTTON_FONT_NUMBER);
        bt8.setFont(BUTTON_FONT_NUMBER);
        bt9.setFont(BUTTON_FONT_NUMBER);
        btDivide.setFont(BUTTON_FONT_NUMBER);
        btBackspace.setFont(BUTTON_FONT_CHINESE);

        frame.add(buttonPanel04);

        /**
         * buttonPanel05        TODO:   按钮区面板 ---5555555---，用于快速定位，不做它用。
         */
        buttonPanel05.setLayout(buttonPanelLayout);
        buttonPanel05.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE + BUTTON_PANEL_HEIGHT * 4 + BUTTON_PANEL_SPACE * 4),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);

        JButton btArcSin = new JButton("sin'");
        JButton btArcCos = new JButton("cos'");
        JButton btArcTan = new JButton("tan'");
        JButton btXPower3 = new JButton("x³");
        JButton btXRoot3 = new JButton("<html><sup><font size=\"4\">3</font></sup><font size=\"6\">√x</fort></html>");

        JButton bt4 = new JButton("4");
        bt4.addActionListener(new InputNumberAction(4));

        JButton bt5 = new JButton("5");
        bt5.addActionListener(new InputNumberAction(5));

        JButton bt6 = new JButton("6");
        bt6.addActionListener(new InputNumberAction(6));

        JButton btMultiply = new JButton("×");
        btMultiply.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operatorClicked("×");
            }
        });

        JButton btDelete = new JButton("DEL");
        btDelete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });

        addRowFive(btArcSin);
        addRowFive(btArcCos);
        addRowFive(btArcTan);
        addRowFive(btXPower3);
        addRowFive(btXRoot3);
        addRowFive(bt4);
        addRowFive(bt5);
        addRowFive(bt6);
        addRowFive(btMultiply);
        addRowFive(btDelete);

        for (JButton button : rowFive) {
            button.setFont(BUTTON_FONT_ENGLISH);
            button.setFocusable(false);
        }
        btXPower3.setFont(new Font("", 1, 23));
        bt4.setFont(BUTTON_FONT_NUMBER);
        bt5.setFont(BUTTON_FONT_NUMBER);
        bt6.setFont(BUTTON_FONT_NUMBER);
        btMultiply.setFont(BUTTON_FONT_NUMBER);
        btDelete.setFont(new Font("", 1, 20));

        frame.add(buttonPanel05);

        /**
         * buttonPanel06        TODO:   按钮区面板 ---6666666---，用于快速定位，不做它用。
         */
        buttonPanel06.setLayout(buttonPanelLayout);
        buttonPanel06.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE + BUTTON_PANEL_HEIGHT * 5 + BUTTON_PANEL_SPACE * 5),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);
        JButton btSinh = new JButton("sinh");
        JButton btCosh = new JButton("cosh");
        JButton btTanh = new JButton("tanh");
        JButton btXPowerY = new JButton("<html><font size=\"6\">x</font><sup><font size=\"5\">y</font></sup></html>");
        JButton btXRootY = new JButton("<html><sup><font size=\"5\">y<font></sup><font size=\"6\">√x</font></html>");

        JButton bt1 = new JButton("1");
        bt1.addActionListener(new InputNumberAction(1));

        JButton bt2 = new JButton("2");
        bt2.addActionListener(new InputNumberAction(2));

        JButton bt3 = new JButton("3");
        bt3.addActionListener(new InputNumberAction(3));

        JButton btMinus = new JButton("-");
        btMinus.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operatorClicked("-");
            }
        });

        JButton btANS = new JButton("ANS");

        addRowSix(btSinh);
        addRowSix(btCosh);
        addRowSix(btTanh);
        addRowSix(btXPowerY);
        addRowSix(btXRootY);
        addRowSix(bt1);
        addRowSix(bt2);
        addRowSix(bt3);
        addRowSix(btMinus);
        addRowSix(btANS);

        for (JButton button : rowSix) {
            button.setFont(BUTTON_FONT_ENGLISH);
            button.setFocusable(false);
        }
        btSinh.setFont(BUTTON_FONT_ENGLISH_lONG);
        btCosh.setFont(BUTTON_FONT_ENGLISH_lONG);
        btTanh.setFont(BUTTON_FONT_ENGLISH_lONG);
        btXPowerY.setFont(new Font("", 1, 22));
        bt1.setFont(BUTTON_FONT_NUMBER);
        bt2.setFont(BUTTON_FONT_NUMBER);
        bt3.setFont(BUTTON_FONT_NUMBER);
        btMinus.setFont(BUTTON_FONT_NUMBER);
        btANS.setFont(new Font("", 1, 20));

        frame.add(buttonPanel06);

        /**
         * buttonPanel07        TODO:   按钮区面板 ---7777777---，用于快速定位，不做它用。
         */
        buttonPanel07.setLayout(buttonPanelLayout);
        buttonPanel07.setBounds(LEFT_MARGIN,
                (TOP_MARGIN + LEFT_TOP_HEIGHT + TOP_NUMBER_SPACE + BUTTON_PANEL_HEIGHT * 6 + BUTTON_PANEL_SPACE * 6),
                LEFT_WIDTH, BUTTON_PANEL_HEIGHT);
        JButton btArcSinh = new JButton("sinh'");
        JButton btArcCosh = new JButton("cosh'");
        JButton btArcTanh = new JButton("tanh'");
        JButton bt1DivideX = new JButton("1/x");
        JButton btExp = new JButton("Exp");

        JButton bt0 = new JButton("0");
        bt0.addActionListener(new InputNumberAction(0));

        JButton btSign = new JButton("±");

        JButton btDot = new JButton(".");
        btDot.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputStatus) {
                    if (resultText.toString().matches("\\d+[\\.]{0}\\d*")) {
                        resultText.append(".");
                        resultLabel.setText(resultText.toString());
                    } else if (resultText.length() == 0) {
                        resultText.append("0.");
                        resultLabel.setText(resultText.toString());
                    }
                }
            }
        });

        JButton btPlus = new JButton("+");
        btPlus.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operatorClicked("+");
            }
        });

        JButton btEquals = new JButton("=");
        btEquals.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!equationText.toString().contains("=")) {
                    equationText.append(resultText);
                    resultText.delete(0, resultText.length());
                    resultText.append(ProcessResult.processResult(equationText.toString()));
                    equationText.append("=");
                    equationLabel.setText(equationText.toString());
                    resultLabel.setText(resultText.toString());
                    inputStatus = false;
                }
            }
        });

        addRowSeven(btArcSinh);
        addRowSeven(btArcCosh);
        addRowSeven(btArcTanh);
        addRowSeven(bt1DivideX);
        addRowSeven(btExp);
        addRowSeven(bt0);
        addRowSeven(btSign);
        addRowSeven(btDot);
        addRowSeven(btPlus);
        addRowSeven(btEquals);

        for (JButton button : rowSeven) {
            button.setFont(BUTTON_FONT_ENGLISH);
            button.setFocusable(false);
        }
        btArcSinh.setFont(BUTTON_FONT_ENGLISH_lONG);
        btArcCosh.setFont(BUTTON_FONT_ENGLISH_lONG);
        btArcTanh.setFont(BUTTON_FONT_ENGLISH_lONG);
        bt1DivideX.setFont(new Font("", 1, 23));
        btExp.setFont(new Font("", 1, 22));
        bt0.setFont(BUTTON_FONT_NUMBER);
        btSign.setFont(BUTTON_FONT_NUMBER);
        btDot.setFont(BUTTON_FONT_NUMBER);
        btPlus.setFont(BUTTON_FONT_NUMBER);
        btEquals.setFont(BUTTON_FONT_NUMBER);

        frame.add(buttonPanel07);

        /**
         * 历史记录键区
         */
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(230, 230, 230));

        //设置右面板的位置和大小，此大小可变，由其他组件的大小决定
        rightPanel.setBounds((LEFT_MARGIN + LEFT_WIDTH + LEFT_RIGHT_SPACE),
                TOP_MARGIN,
                (DEFAULT_WIDTH - 5 - LEFT_MARGIN * 2 - LEFT_WIDTH - LEFT_RIGHT_SPACE),
                (DEFAULT_HEIGHT - 56 - TOP_MARGIN * 2 - 4));

        Border rightBorder = BorderFactory.createEtchedBorder();
        Border rightBorderTittle = BorderFactory.createTitledBorder(rightBorder, "历史记录");
        rightPanel.setBorder(rightBorderTittle);

        frame.add(rightPanel);
        frame.setVisible(true);
    }

    private static void addRowOne(JButton button) {
        rowOne.add(button);
        buttonPanel01.add(button);
    }

    private static void addRowTwo(JButton button) {
        rowTwo.add(button);
        buttonPanel02.add(button);
    }

    private static void addRowThree(JButton button) {
        rowThree.add(button);
        buttonPanel03.add(button);
    }

    private static void addRowFour(JButton button) {
        rowFour.add(button);
        buttonPanel04.add(button);
    }

    private static void addRowFive(JButton button) {
        rowFive.add(button);
        buttonPanel05.add(button);
    }

    private static void addRowSix(JButton button) {
        rowSix.add(button);
        buttonPanel06.add(button);
    }

    private static void addRowSeven(JButton button) {
        rowSeven.add(button);
        buttonPanel07.add(button);
    }

    /**
     * 输入数字的方法
     *
     * @param i 输入一个数字字符 i
     */
    private static void inputNumbers(int i) {
        if (inputStatus) {
            if (i != 0) {
                if (resultText.toString().equals("0") || resultText.toString().equals("-0")) {
                    resultText.delete(0, 1);                //这个判断也是醉了！！！半天才理清！
                }
                resultText.append(i);
            } else if (!resultText.toString().equals("0")) {
                resultText.append(i);
            } else {
                setResultTest_0();
            }
        } else {
            resultText.delete(0, resultText.length());
            resultText.append(i);
        }
        inputStatus = true;
        resultLabel.setText(resultText.toString());
    }

    /**
     * 将结果标签置零，设置可输入状态。
     */
    private static void setResultTest_0() {
        resultText.delete(0, resultText.length());
        resultText.append(0);
    }

    public static void setStringBuilderToZero(StringBuffer builder) {
        builder.delete(0, builder.length());
        builder.append(0);
    }

    public static void makeStringBuilderEmpty(StringBuffer builder) {
        builder.delete(0, builder.length());
    }

    public static void backspaceButton() {
        if (resultText.length() <= 0) return;

        if (resultText.length() == 1) {
            setResultTest_0();
            resultLabel.setText(resultText.toString());
            return;
        }
        resultText = new StringBuffer(resultText.substring(0, (resultText.length() - 1)));
        resultLabel.setText(resultText.toString());
    }

    public static String backspace(String toBeBackspace) {
        if (toBeBackspace.length() == 0) return "";
        return toBeBackspace.substring(0, (toBeBackspace.length() - 1));
    }

    private static void delete() {
        if (equationText.length() <= 0) return;

        if (resultText.toString().equals("0")) {
            equationText = new StringBuffer(equationText.substring(0, (equationText.length() - 1)));
            equationLabel.setText(equationText.toString());
        }
    }

    private static void operatorClicked(String operator) {
        inputStatus = false;
        if (equationText.toString().contains("=")) {
            equationText.delete(0, equationText.toString().indexOf("=") + 1);
        }
        equationText.append(resultText).append(operator);
        resultText.delete(0, resultText.length());
        resultText.append(0);
        equationLabel.setText(equationText.toString());
        resultLabel.setText("0");
    }

    private static boolean isInteger(String intOrNot) {
        return intOrNot.matches("\\d+");
    }

    private static void isNumber() {

    }

    public static long calcFactorial(int factorialScale) {
        if (factorialScale == 0) {
            return 1;
        }
        long sum = 1;
        for (int i = 1; i <= factorialScale; i++) {
            sum *= i;
        }
        return sum;
    }


    /**
     * inner class
     */
    static class InputNumberAction extends AbstractAction {
        int i;

        InputNumberAction(int i) {
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            inputNumbers(i);
        }
    }

}
