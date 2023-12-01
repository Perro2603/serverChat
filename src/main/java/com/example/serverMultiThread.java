package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.*;

public class serverMultiThread extends Thread{
    
    Socket client;
    ArrayList<Socket> listaSocket = new ArrayList<>();
    static ArrayList<String> listaNomi = new ArrayList<>();
    String scelta;
    

    public serverMultiThread(Socket c, ArrayList<Socket> s,ArrayList<String> no){

        this.client = c;
        this.listaSocket = s;
        this.listaNomi = no;
    }

    public void run(){

        try{

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            String nome = in.readLine();
            listaNomi.add(nome);

            while(true){

                scelta = in.readLine();
                if(scelta.equalsIgnoreCase("x")){
                    break;
                }

                String destinatario = "";
                String messaggio = "";
                

                switch(scelta){
                    
                    case "s":
                        
                        destinatario = in.readLine();
                        if(listaNomi.contains(destinatario)){
                            messaggio = in.readLine();
                            int i = listaNomi.indexOf(destinatario);
                            Socket S = listaSocket.get(i);
                            DataOutputStream outPut = new DataOutputStream(S.getOutputStream());
                            outPut.writeBytes(nome + ": " + messaggio + "\n");
                        } else {
                            out.writeBytes("pizza marmellata,metti un nomne decente");
                        }
                        
                        break;

                    case "t" :
                        messaggio = in.readLine();
                        for(Socket s : listaSocket){
                            DataOutputStream outBroadcast = new DataOutputStream(s.getOutputStream());
                            outBroadcast.writeBytes(nome + ": " + messaggio + "\n");
                        }
                        break;

                    case "l":
                        out.writeBytes("l"+"\n");

                        String temp = listaNomi.get(0);
                        for(int i=1; i <= listaNomi.size();i++){
                            temp += "," + listaNomi.get(i);
                        }
                        out.writeBytes(temp+"\n");
                        break;

                    default:
                        System.out.println("Wtf");
                        break;
                }   

            }
            client.close();
            

        }catch (Exception e) {
            System.out.println("errore " + e.getMessage());
        }
        



    }







}
