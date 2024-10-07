package GameManager;

import UiManager.Login;
import UiManager.SceenManager;
import UiManager.SignUp;

public class State {
	public static boolean isCaution = true;
	public static boolean isLogin = false;
	public static boolean isSignUp = false;
	public static boolean isGame = false;
	public static boolean isCheck = true;
	public static SceenManager sceen;

	public static void checkState() {
		if (!isCaution) {
			isCaution = true;
			isLogin = true;
			Login login = new Login();

			// 커션 종료 됐을때 isCaution이 false가 되는데 계속 체크 스테이트를 하니까

			System.out.println("커션 종료");
		}
		if(!isLogin&&isSignUp) {
			sceen.sign = new SignUp();
		}
		if(isLogin&&!isSignUp) {
			sceen.login = new Login();
		}
		if(!isLogin&&isGame)
		{
			sceen.game = new Game();
		}

	}
}
