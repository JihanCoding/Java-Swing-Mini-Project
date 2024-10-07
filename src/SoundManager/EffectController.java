package SoundManager;

import javazoom.jl.player.MP3Player;

public class EffectController {
	public static SoundDAO sound = new SoundDAO();
	public static MP3Player effectPlayer = new MP3Player();
	
	public void effectPlay(SoundVO vo) {
		if(effectPlayer!=null&&effectPlayer.isPlaying()) {
			effectPlayer.stop();
		}
		sound.play(effectPlayer, vo);
		System.out.println("버튼");
	}
}
