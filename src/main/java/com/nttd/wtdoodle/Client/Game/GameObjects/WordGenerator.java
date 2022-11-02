package com.nttd.wtdoodle.Client.Game.GameObjects;

import com.nttd.wtdoodle.ResourceLocator;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/*
`````This is a helper class that is used to provide three words.
``````````````And the drawer needs to choose from them.
*/
public class WordGenerator {
    ArrayList<String> words = new ArrayList<>();


    public WordGenerator(){
        InputStream is = ResourceLocator.class.getResourceAsStream("words.txt");
        assert is != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String word;
        while(true){
            try {
                if ((word = bufferedReader.readLine()) == null) break;
                words.add(word);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public String getThreeRandomWords(){
        Random r = new Random();


        StringBuilder selectedWords = new StringBuilder();
        int rand;
        for (int i = 0 ; i < 2 ; i++){
            rand = r.nextInt(words.size());
            selectedWords.append(words.get(rand)).append(",");
        }
        rand = r.nextInt(words.size());
        selectedWords.append(words.get(rand));
        return selectedWords.toString();
    }

//    public static void main(String[] args) {
//        PenInfo p = new PenInfo(1,2,3,true,new PenColor(1,2,3));
//        Message m = new Message(Message.type.general,89,"Vinayak","Kushwaha",null);
//        System.out.println(m);
//    }

}
