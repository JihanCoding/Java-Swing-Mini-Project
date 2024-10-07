package DBManager;

public class MemberVO {
	private String id;
	private String pw;
	private String name;
	private int age;
	private int score;
	private String word;
	public MemberVO(String id, String pw, String name, int age, int score) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.age = age;
		this.score = score;
	}

	public MemberVO(String word) {
		this.word = word;
	}


	public String getId() {
		return id;
	}
	public String getPw() {
		return pw;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public int getScore() {
		return score;
	}
	public String getWord() {
		return word;
	}
	
}
