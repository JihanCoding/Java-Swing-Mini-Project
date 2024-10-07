package DBManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FileLoader {
	private static ArrayList<String> wordList = new ArrayList<>();
	private Controller con;
	public FileLoader()
	{
		setData();
	}
	public void setData() {
		String filePath = "C:\\Users\\abc\\Desktop\\Java Study\\MiniProject\\src\\data\\data.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 공백(스페이스, 탭, 줄바꿈 등)을 기준으로 단어를 분리
                String[] words = line.split(",");
                for (String word : words) {
                	con = new Controller();
                	System.out.println(word);
                	con.contInsertWords(new MemberVO(word));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력
        }
	}
	public ArrayList<String> getWords()
	{
		return wordList;
	}
}