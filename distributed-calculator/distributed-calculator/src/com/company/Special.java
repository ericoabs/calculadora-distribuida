package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Special extends Thread {

    private String error = "";
    private final Socket socket;
    private BufferedReader bufferedReader;

    public Special(Socket socket){
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
            int port = 3323;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor ativo na porta =" + port);

            while(true){
                System.out.println("Aguardando conexão...");
                Socket connection = serverSocket.accept();
                System.out.println("Cliente conectado...");
                Thread thread = new Special(connection);
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

        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String send(BufferedWriter bufferedWriter, String expression) throws  IOException
    {
        String result;

        if (expression.isEmpty()) {
            return "";
        }

        result = this.calc(expression);
        System.out.println(result);

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

        if (elements[1].equals("_")) {
            if (second < Double.parseDouble("0")) {
                this.error = "Resultante é número imaginário\r\n";
                return "";
            }

            if (first <= Double.parseDouble("0")) {
                this.error = "Índice não pode ser 0 ou negativo\r\n";
                return "";
            }

            result = Math.pow(second, 1/first);
            return "Resultado = " + result + "\r\n";
        }

        if (elements[1].equals("^")) {
            if (second < Double.parseDouble("0")) {
                this.error = "Expoente não pode ser negativo\r\n";
                return "";
            }

            result = Math.pow(first, second);
            return "Resultado = " + result + "\r\n";
        }

        if (elements[1].equals("%")) {
            if (first < Double.parseDouble("0")) {
                this.error = "Expoente não pode ser negativo\r\n";
                return "";
            }
            result = (second * first)/100;
            return "Resultado = " + result + "\r\n";
        }

        return "";
    }

}