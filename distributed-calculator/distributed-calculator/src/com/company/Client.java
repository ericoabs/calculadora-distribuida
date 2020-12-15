package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket basicSocket;
    private Socket specialSocket;
    private OutputStream basicOutputStream;
    private OutputStream specialOutputStream;
    private Writer basicOutputStreamWriter;
    private Writer specialOutputStreamWriter;
    private BufferedWriter basicBufferedWriter;
    private BufferedWriter specialBufferedWriter;
    private final Scanner scanner;

    public Client() {
        System.out.println("Como utilizar as operações:");
        System.out.println("Soma: n1 + n2");
        System.out.println("Subtração: n1 - n2");
        System.out.println("Multiplicação: n1 * n2");
        System.out.println("Divisão: n1 / n2");
        System.out.println("Exponenciação: n1 ^ n2");
        System.out.println("Raiz: n1 _ n2");
        System.out.println("Porcentagem: n1 % n2");
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) throws IOException{

        Client client = new Client();
        client.connect();
        client.reader();
        client.listener();
        client.quit();
    }

    private void reader() throws IOException {
        System.out.println("Digite a expressão: ");
        String expression = scanner.nextLine();
        distributor(expression);
    }

    public void connect() throws IOException{

        basicSocket = new Socket("localhost", 3322);
        basicOutputStream = basicSocket.getOutputStream();
        basicOutputStreamWriter = new OutputStreamWriter(basicOutputStream);
        basicBufferedWriter = new BufferedWriter(basicOutputStreamWriter);
        basicBufferedWriter.flush();

        specialSocket = new Socket("localhost", 3323);
        specialOutputStream = specialSocket.getOutputStream();
        specialOutputStreamWriter = new OutputStreamWriter(specialOutputStream);
        specialBufferedWriter = new BufferedWriter(specialOutputStreamWriter);
        specialBufferedWriter.flush();

    }

    private void distributor(String expressao) throws IOException {

        if (expressao.contains("%") | expressao.contains("^") | expressao.contains("_")) {
            System.out.println("Expressão indo para especial:\r\n");
            sendSpecial(expressao);
        } else if (expressao.contains("+") | expressao.contains("-") | expressao.contains("/") | expressao.contains("*")){
            System.out.println("Expressão indo para básico:\r\n");
            sendBasic(expressao);
        } else {
            System.out.println("Erro na sintáxe, operador inválido\r\n");
        }

    }

    public void sendBasic(String mensagem) throws IOException {

        basicBufferedWriter.write(mensagem + "\r\n");
        basicBufferedWriter.flush();

    }

    public void sendSpecial(String mensagem) throws IOException {

        specialBufferedWriter.write(mensagem + "\r\n");
        specialBufferedWriter.flush();

    }

    public void listener() throws IOException {

        InputStream basicInputStream = basicSocket.getInputStream();
        InputStreamReader basicInputStreamReader = new InputStreamReader(basicInputStream);
        BufferedReader basicBufferedReader = new BufferedReader(basicInputStreamReader);

        InputStream specialInputStream = specialSocket.getInputStream();
        InputStreamReader specialInputStreamReader = new InputStreamReader(specialInputStream);
        BufferedReader specialBufferedReader = new BufferedReader(specialInputStreamReader);

        String message = "";

        while (!message.equalsIgnoreCase("sair")){

            if (basicBufferedReader.ready()) {

                message = basicBufferedReader.readLine();

                if (message.equalsIgnoreCase("sair")) {

                    System.out.println("Servidor caiu\r\n");

                } else {

                    System.out.println(message + "\r\n");

                }

                this.reader();
            }

            if (specialBufferedReader.ready()) {

                message = specialBufferedReader.readLine();

                if (message.equalsIgnoreCase("sair")) {

                    System.out.println("Servidor caiu\r\n");

                } else {

                    System.out.println(message + "\r\n");

                }

                this.reader();
            }

        }

    }
www
    public void quit() throws IOException {
        System.out.println("Desconectado");
        basicBufferedWriter.close();
        specialBufferedWriter.close();
        basicOutputStreamWriter.close();
        specialOutputStreamWriter.close();
        basicOutputStream.close();
        specialOutputStream.close();
        basicSocket.close();
        specialSocket.close();
        System.exit(0);

    }
}
