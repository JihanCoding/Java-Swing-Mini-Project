package GameManager;

//텍스트를 나타내는 클래스
public class Text {
 private String text;  // 텍스트 내용
 private int x, y;     // 텍스트의 현재 X, Y 위치
 private int speed;    // 텍스트가 떨어지는 속도
 
 public Text(String text, int x, int y, int speed) {
     this.text = text;
     this.x = x;
     this.y = y;
     this.speed = speed;
 }
 
 public String getText() {
     return text;
 }
 
 public int getX() {
     return x;
 }
 
 public int getY() {
     return y;
 }
 
 public void move(int speed) {
     y += speed;  // Y 좌표를 speed만큼 증가시켜 텍스트가 아래로 이동하게 함
 }
}
