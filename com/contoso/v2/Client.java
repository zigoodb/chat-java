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
        ServerHandler serverHandler = new ServerHandler(socket);
        serverHandler.start();

        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            PrintWriter printWriter = new PrintWriter(bufferedWriter, true);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                //输出客户端的内容
                printWriter.println(message);

            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    class ServerHandler extends Thread {

        private Socket socket;
        public ServerHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
              
                while (true) {
                    //缓冲字符是可以按行读取的
                    String line = bufferedReader.readLine();
                    System.out.println("服务器说：" + line);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
