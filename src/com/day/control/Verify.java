package com.day.control;

import com.day.bean.Account;
import com.day.view.Game;
import com.day.view.Prompt;

import java.util.HashMap;

/**
 * @Author: day
 * @Date: Created in 2020/11/30 下午1:29
 * @Description: 用来进行登录和注册的验证
 * @Version: 1.0
 */
public class Verify {

    /**
     * 登录验证，确定账户是否存在以及密码是否正确
     * @param account
     */
    public static boolean LoginVerify(Account account) {

        boolean flag = true;
        HashMap<String, char[]> hashMap = new HashMap<>();
        FileOperation.FileRead(hashMap, "/Users/day/IdeaProjects/minesweeping/src/Account.txt");

        //若没有用户，则创建新用户
        if (hashMap.size() == 0) {
            FileOperation.FileWrite(account, "/Users/day/IdeaProjects/minesweeping/src/Account.txt");
            //进入游戏界面
            Game game = new Game(9,9);
        }

        boolean UserIsExit = hashMap.containsKey(account.getUser());

        if (UserIsExit) {       //用户名存在，验证密码是否正确
            char[] correctPassword = hashMap.get(account.getUser());
            if (correctPassword.length != account.getPassword().length) {
                flag = false;
            } else {
                for (int i = 0; i < correctPassword.length; i++) {
                    if (correctPassword[i] != account.getPassword()[i]){
                        flag = false;
                        break;
                    }
                }
            }
            if (!flag) {
                Prompt.LoginAndRegisterDialog("密码错误！");
            } else {

                //进入游戏
                Game game = new Game(9,9);
            }
        } else {    //用户名不存在，弹窗警告，回到登录界面
            //弹出对话窗口，提示账户不存在，提供一个确定按钮，回到登录界面
            Prompt.LoginAndRegisterDialog( "账户不存在！");
            flag = false;
        }
        return flag;
    }

    /**
     * 注册验证，确保用户名不会和已经存在的用户名冲突
     * @param account 新注册的账户
     */
    public static boolean RegisterVerify(Account account) {

        boolean flag = true;

        //用户名中不能包含空格
        char[] charArray = account.getUser().toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == ' ') {
                Prompt.LoginAndRegisterDialog("用户名格式错误！");
                return false;
            }
        }

        //密码中不能包含空格
        for (int i = 0; i < account.getPassword().length; i++) {
            if (account.getPassword()[i] == ' ') {
                Prompt.LoginAndRegisterDialog("密码格式错误！");
                return false;
            }
        }

        //若用户名和密码都符合规范，再进行用户名是否重复的验证
        HashMap<String, char[]> hashMap = new HashMap<>();
        FileOperation.FileRead(hashMap, "/Users/day/IdeaProjects/minesweeping/src/Account.txt");

        boolean userIsExit = hashMap.containsKey(account.getUser());

        if (userIsExit) {   //用户名冲突，弹窗警告，回到登录界面

            //弹出对话窗口，说明账户已存在，提供一个确定按钮，回到登录界面
            Prompt.LoginAndRegisterDialog("账户已存在！");
            flag = false;

        } else {    //注册成功，进入游戏界面

            FileOperation.FileWrite(account, "/Users/day/IdeaProjects/minesweeping/src/Account.txt");
            //进入游戏界面
            Game game = new Game(9,9);
        }
        return flag;
    }
}