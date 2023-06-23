package me.byun.bot;

import java.io.*;

public class txtReader {
    public static String readToken(String tokenName){
        InputStream inputStream = Main.class.getResourceAsStream(tokenName);
        return readFile(inputStream);
    }
    public static String readFile(String filePath) {
        StringBuilder strBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                strBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBuilder.toString();
    }
    public static String readFile(InputStream inputStream) {
        StringBuilder strBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                strBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBuilder.toString();
    }
}
