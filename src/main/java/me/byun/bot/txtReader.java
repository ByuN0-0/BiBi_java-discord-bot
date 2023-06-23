package me.byun.bot;

import java.io.*;
import java.util.stream.Collectors;

public class txtReader {
    public static String readToken(String tokenName){ //resources 폴더에서 토큰을 가져옴
        InputStream inputStream = Main.class.getResourceAsStream(tokenName);
        return readFile(inputStream);
    }
    public static String readFile(String filePath) { //파일 경로를 입력받아서 파일을 읽음
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
    public static String readFile(InputStream inputStream){ //
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining());
    }
}
