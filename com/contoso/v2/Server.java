package com.contoso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    ServerSocket serverSocket;

    public Server() {
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start() {
        System.out.println("服务器已启动...");   
        try {
            while (true) {
                System.out.println("等待客户端的连接......");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端已连接！");
                //一个socket就交给一个线程去完成
                Thread clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    //创建一个内部类，专门用来处理客户端连接的
    class ClientHandler extends Thread {

        private Socket socket;
        private String host;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.host = socket.getInetAddress().getHostAddress();
        }

        @Override
        public void run() {
            try {
                //从获取的客户端对象中，获取数据，前提是获取IO流
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                OutputStream outputStream = socket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                PrintWriter printWriter = new PrintWriter(bufferedWriter, true);
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    //缓冲字符是可以按行读取的
                    String line = bufferedReader.readLine();
                    System.out.println("客户端" + host+"说：" + line);

                    String message = scanner.nextLine();
                    //输出客户端的内容
                    printWriter.println(message);

                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("端口号已申请成功！");
        server.start();
    }
}
