package com.day.view;

import javax.swing.*;

/**
 * @Author: day
 * @Date: Created in 2020/11/30 下午3:27
 * @Description: 包括各种弹窗提示与警告
 * @Version: 1.0
 */
public class Prompt {

    private static JFrame frame;

    private static String[] choices = {"退出游戏", "再来一局"};
    /**
     * 登录与注册失败的弹窗警告
     * @param promptInformation
     */
    public static void LoginAndRegisterDialog(String promptInformation) {
        JOptionPane.showMessageDialog(frame, promptInformation, "警告", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 游戏失败的弹窗提示
     */
    public static int LoseGame(JFrame frame) {
        return JOptionPane.showOptionDialog(frame, "很遗憾宁失败了！", "游戏结束", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, Prompt.choices, Prompt.choices[1]);
    }

    /**
     * 游戏胜利的弹窗提示
     */
    public static int WinGame(JFrame frame) {
        return JOptionPane.showOptionDialog(frame, "恭喜您取得胜利！", "游戏结束", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
    }
}
