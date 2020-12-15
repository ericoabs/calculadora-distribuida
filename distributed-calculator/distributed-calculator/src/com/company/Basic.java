package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Basic extends Thread {

    private final Socket socket;
    private BufferedReader bufferedReader;
    private String error;

    public Basic(Socket socket){
        this.socket = socket;
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String []args) {

        try{
            int port = 3322;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor ativo na porta: " + port);
            while(true){
                System.out.println("Aguardando conexão...");
                Socket connection = serverSocket.accept();
                System.out.println("Cliente conectado...");
                Thread thread = new Basic(connection);
                thread.start();
            }

        }catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public void run(){

        try{
            OutputStream outputStream =  this.socket.getOutputStream();
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            String expression;

            while(true)
            {
                expression = bufferedReader.readLine();
                String result = send(bufferedWriter, expression);
                if (!(result.isEmpty() | result.isBlank())) {
                    System.out.println(result);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String send(BufferedWriter bufferedWriter, String expression) throws  IOException
    {
        String result;

        if (expression.isEmpty()) {
            return "";
        }
        result = this.calc(expression);

        if (!(result.isEmpty())) {
            bufferedWriter.write(result);
        } else {
            bufferedWriter.write(this.error);
        }

        bufferedWriter.flush();

        return result;
    }

    private String calc(String expression) throws ArithmeticException
    {
        if (!expression.contains(" ")) {
            this.error = "Erro na sitaxe da expressão\r\n";
            return "";
        }

        double result;

        expression = expression.trim();

        String[] elements = expression.split(" ");

        if (Arrays.stream(elements).count() < 3) {
            this.error = "Erro na sitaxe da expressão\r\n";
            return "";
        }

        double first = Double.parseDouble(elements[0]);

        double second = Double.parseDouble(elements[2]);

        if (elements[1].equals("+")) {
            result = first + second;
            return "Resultado = " + result + "\r\n";
        }

        if (elements[1].equals("-")) {
            result = first - second;
            return "Resultado = " + result + "\r\n";
        }

        if (elements[1].equals("/")) {
            if (second == 0) {
                return "";
            }
            result = first / second;
            return "Resultado = " + result + "\r\n";
        }

        if (elements[1].equals("*")) {
            result = first * second;
            return "Resultado = " + result + "\r\n";
        }

        return "";
    }
}