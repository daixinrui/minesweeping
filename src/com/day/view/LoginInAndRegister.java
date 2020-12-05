package com.day.view;

import com.day.bean.Account;
import com.day.control.Verify;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: day
 * @Date: Created in 2020/11/30 上午11:20
 * @Description: 登录界面
 * @Version: 1.0
 */
public class LoginInAndRegister extends JFrame {

    private JButton login;      //"登录"按钮
    private JButton register;   //"注册"按钮

    private Box horizontalBox1;     //水平箱，用来存放用户名及其文本框
    private Box horizontalBox2;     //水平箱，用来存放密码及其密码框
    private Box horizontalBox3;     //水平箱，用来存放"登录"和"注册"按钮

    private JTextField userName;    //用户名文本框
    private JPasswordField password;    //密码密码框

    private JLabel label1;      //用户名
    private JLabel label2;      //密码

    private JPanel panel;       //面板，用来存放三个水平箱

    /**
     * 空参构造器，用来初始化登陆界面
     */
    public LoginInAndRegister() {
        setTitle("用户登录");
        setBounds(600, 300, 320, 135);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        login = new JButton("登录");
        register = new JButton("注册");

        horizontalBox1 = Box.createHorizontalBox();
        horizontalBox2 = Box.createHorizontalBox();
        horizontalBox3 = Box.createHorizontalBox();

        label1 = new JLabel("用户名");
        label2 = new JLabel("密码");

        panel = new JPanel();
        userName = new JTextField(10);
        password = new JPasswordField(10);

        login.addActionListener(new ActionListener() {      //"登录"按钮的监听器
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Account newAccount = new Account(getUserName().getText(), getPassword().getPassword());     //新建账户
                boolean loginSuccessfully = Verify.LoginVerify(newAccount);     //用户名与密码验证
                if (loginSuccessfully){     //验证通过，销毁登录窗口，进入游戏界面
                    dispose();
                }
            }
        });

        register.addActionListener(new ActionListener() {       //"注册"按钮的监听器
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Account newAccount = new Account(getUserName().getText(), getPassword().getPassword());
                boolean registerSuccessfully = Verify.RegisterVerify(newAccount);
                if (registerSuccessfully){
                    dispose();
                }
            }
        });

        horizontalBox1.add(label1);
        horizontalBox1.add(Box.createHorizontalStrut(17));
        horizontalBox1.add(userName);

        horizontalBox2.add(label2);
        horizontalBox2.add(Box.createHorizontalStrut(30));
        horizontalBox2.add(password);

        horizontalBox3.add(login);
        horizontalBox3.add(Box.createHorizontalStrut(40));
        horizontalBox3.add(register);

        panel.add(horizontalBox1);
        panel.add(horizontalBox2);
        panel.add(horizontalBox3);

        setContentPane(panel);

        setVisible(true);
    }

    public JTextField getUserName() {

        return userName;
    }

    public void setUserName(JTextField userName) {

        this.userName = userName;
    }

    public JPasswordField getPassword() {

        return password;
    }

    public void setPassword(JPasswordField password) {

        this.password = password;
    }
}