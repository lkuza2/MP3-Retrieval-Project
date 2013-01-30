package com.darkprograms.mp3retrievalproject;

import com.darkprograms.mp3retrieval.MP3Connection;
import com.darkprograms.mp3retrieval.Song;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 11/7/12
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainUtil {

    private static MainUtil instance;

    public static MainUtil getInstance() {
        if (instance == null) {
            instance = new MainUtil();
        }
        return instance;
    }

    private MainUtil() {

    }

    private String location = "C:\\Users\\User\\Desktop\\songs";
    private HashMap<String, String> songs = new HashMap<String, String>();

    public void init() {
        try{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Working Directory: ");
        location = scanner.nextLine();
        System.out.println();

        int lines = countLines();
        int current = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(location + "/" + "list.txt"));
        String line;
        System.out.print("Downloaded "+ current + "/" + lines);
            while((line = bufferedReader.readLine()) != null){
                downloadSong(line);
                current++;
                System.out.print("\rDownloaded "+ current + "/" + lines);
                System.out.flush();
            }
        File suggested = new File(location + "/suggested.txt");
            File failed = new File(location + "/failed.txt");
            File shared = new File(location + "/4shared.txt");

            BufferedWriter bufferedWriterSuggested = new BufferedWriter(new FileWriter(suggested));
            BufferedWriter bufferedWriterFailed = new BufferedWriter(new FileWriter(failed));
            BufferedWriter bufferedWriterShared = new BufferedWriter(new FileWriter(shared));



            Iterator<String> iterator = songs.keySet().iterator();
            while(iterator.hasNext()){
                String song = iterator.next();
                if(songs.get(song).equals("failed")){
                     bufferedWriterFailed.write(song);
                    bufferedWriterFailed.newLine();
                }
                if(songs.get(song).equals("4shared")){
                    bufferedWriterShared.write(song);
                    bufferedWriterShared.newLine();
                }
                if(songs.get(song).equals("suggested")){
                    bufferedWriterSuggested.write(song);
                    bufferedWriterSuggested.newLine();
                }

            }
            bufferedWriterFailed.close();
            bufferedWriterShared.close();
            bufferedWriterSuggested.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void downloadSong(String songName) {
        String parsedSong = songName.toLowerCase().replace("(", "").replace(")", "").replace("'", "").replace("&", "")
                .replace(" ", "_");
        MP3Connection connection = new MP3Connection(songName);
        Song song = connection.retrieveSong();
        if (song.getResponse() == "fail" || song.getResponse() == "4shared") {
            songs.put(songName, song.getResponse());
            return;
        }
        String url = song.getUrl();

        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);

            InputStream inputStream = urlConnection.getInputStream();
            byte buffer[] = new byte[256];
            File  file = new File(location + "/"+parsedSong+".mp3");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int read;

            while((read = inputStream.read(buffer, 0, 256)) != -1){
                fileOutputStream.write(buffer, 0, read);
            }
            inputStream.close();
            fileOutputStream.close();

           if(song.getResponse().equals("suggested")){
           songs.put(songName, "suggested");
           }else{
               songs.put(songName, "correct");

           }
        } catch (Exception ex) {
            songs.put(songName, "failed");
           // ex.printStackTrace();
        }

    }

    private int countLines(){
        int lines = 0;

        try{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(location + "/" + "list.txt"));
        while(bufferedReader.readLine() != null){
            lines++;
        }
            return lines;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return lines;
    }

}
