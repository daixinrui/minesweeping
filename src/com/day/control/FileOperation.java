package com.day.control;

import com.day.bean.Account;
import com.day.bean.ScoreRecord;

import java.io.*;
import java.util.HashMap;

/**
 * @Author: day
 * @Date: Created in 2020/11/30 上午11:22
 * @Description: 用于文件的读写，包括登录时用户名和密码的读写，以及查看排行榜时从磁盘读写
 * @Version: 1.0
 */
public class FileOperation {

    private static String pathName;

    /**
     * 将新注册的用户名和密码写入磁盘保存
     * @param account 新注册的账户
     * @param pathName 写入的文件路径
     */
    public static void FileWrite(Account account, String pathName) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathName,true));     //以追加的形式写文件

            writer.write(account.getUser());
//            writer.flush();
            writer.write(" ");
//            writer.flush();
            writer.write(account.getPassword());
//            writer.flush();
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将本地保存的用户名和对应的密码读入内存进行登录和注册验证
     * @param hashMap 将用户名和密码作为一个键值对读入内存
     * @param pathName 文件路径
     */
    public static void FileRead(HashMap<String, char[]> hashMap, String pathName) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(pathName));

            String userName, string;
            char[] password;

            while((string = reader.readLine()) != null) {
                String[] s = string.split(" ");
                userName = s[0];
                password = s[1].toCharArray();
                hashMap.put(userName, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取磁盘中排行榜文件的姓名和用时信息，存储在一个ScoreRecord数组中
     * @param pathName 文件路径
     * @return 存储ScoreRecord信息的ScoreRecord数组
     */
    public static ScoreRecord[] RankRead(String pathName) {

        ScoreRecord[] records = new ScoreRecord[3];

        for (int i = 0; i < records.length; i++) {
            records[i] = new ScoreRecord();
        }

        FileOperation.RankFileRead(records, pathName);

        return records;
    }

    /**
     * 将新产生的排行榜写入磁盘中保存
     * @param records 要写入的ScoreRecord数组
     * @param pathName 文件路径
     */
    public static void RankWrite(ScoreRecord[] records, String pathName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathName));

            for (int i = 0; i < records.length; i++) {
                writer.write(records[i].getName());
                writer.write(" ");
                writer.write(records[i].getTimeRecord() + "");
                if (i != records.length - 1) {
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 作为RankRead()方法的内部方法使用
     * @param records
     * @param pathName
     */
    private static void RankFileRead(ScoreRecord[] records, String pathName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(pathName));

            String name, string;
            int timeRecord;

            for (int i = 0; i < records.length; i++) {
                string = reader.readLine();
                String[] s = string.split(" ");
                name = s[0];
                timeRecord = Integer.parseInt(s[1]);
                records[i].setName(name);
                records[i].setTimeRecord(timeRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}