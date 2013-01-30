package com.darkprograms.mp3retrieval;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 11/7/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MP3Connection {

    private String songName;


    public MP3Connection (String songName){
       setSongName(songName);
    }

    private void setSongName(String songName){
        this.songName = songName;
    }

    public String getSongName(){
        return songName;
    }

    public Song retrieveSong(){
        try{
            BufferedReader bufferedReader = getReader(getSongName());
            String line;
            while(!(line = bufferedReader.readLine()).contains("<!-- count ok -->")){

                if(line.contains("The mp3s below require to be registered and logged in ")){
                 return new Song(null, "4shared");
             }else
                if(line.contains("You can also try some of this:")){
                    String suggestedSong = line.split(">")[3].replace("</a", "");
                    //System.out.println(suggestedSong);
                    String songUrl = getSongUrl(suggestedSong);
                    if(songUrl == null){
                        return new Song(null, "4shared");
                    }else
                    return new Song(getSongUrl(suggestedSong), "suggested");
                }else
              if(line.contains("<!-- info mp3 here -->")){
                  return new Song(getSongUrl(getSongName()), "correct");
              }
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new Song("", "fail");
    }

    private String getSongUrl(String songName){


        try{

        BufferedReader reader = getReader(songName);
        String line;

        while(!(line = reader.readLine()).contains("target=\"_blank\" style=\"color:green;\">Download</a>")){
            if(line.contains("The mp3s below require to be registered and logged in")){
                reader.close();
                return null;
            }
        }
            reader.close();
        return line.split("\"")[3];

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private BufferedReader getReader(String songName){
        try{
        String parsedSong = songName.toLowerCase().replace("(", "").replace(")", "").replace("'", "").replace("&", "")
                .replace(" ", "_").replace("%", "").replace("/", "").replace("-", "").replace(".", "").replace("!", "")
                .replace("[", "").replace("]", "");
        URLConnection urlConnection = new URL(Constants.BASE_URL + parsedSong + Constants.END_URL).openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return reader;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
