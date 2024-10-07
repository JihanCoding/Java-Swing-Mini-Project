package UiManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import GameManager.State;
import SoundManager.SoundController;
import SoundManager.SoundVO;

public class Caution {

	private DrawUI drawUI;
	private boolean isCheck;
    public Caution() {
    	SoundController.bgmPlay(new SoundVO("caution.mp3", "커션bgm"));
    	isCheck = true;
    	System.out.println("실행");
    	if(isCheck) {
        	drawUI = DrawUI.getInstance("/images/cautionBack.png", 1366, 768);
        	addImageMouseListener();  // 마우스 이벤트 리스너 추가
    	}
    }

    private void addImageMouseListener() {
        drawUI.backgroundPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                synchronized(this) {
                	if(isCheck)
                	{	 System.out.println("클릭");
                		  SoundController.effectPlay(new SoundVO("Button.mp3", "버튼클릭음"));
                          drawUI.backgroundPanel.setVisible(false); // 이미지를 눌렀을 때 이미지를 숨김
                          State.isCaution = false;
                          State.checkState();
                          System.out.println(State.isCaution);
                          isCheck = false;
                         
//                          drawUI.backgroundPanel.removeAll();
                	}
                }
            }
        });
    }
}