package com.contoso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;

    public Client() {
        try {
            System.out.println("客户端已启动");
            socket = new Socket("127.0.0.1", 8888);
            System.out.println("服务器连接成功");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            PrintWriter printWriter = new PrintWriter(bufferedWriter, true);

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                //输出客户端的内容
                printWriter.println(message);

                //缓冲字符是可以按行读取的
                String line = bufferedReader.readLine();
                System.out.println("服务器说：" + line);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
