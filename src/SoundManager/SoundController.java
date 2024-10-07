package SoundManager;

import javazoom.jl.player.MP3Player;

public class SoundController {
	public static SoundDAO sound = new SoundDAO();
	public static MP3Player effectPlayer = new MP3Player();
	public static MP3Player bgmPlayer = new MP3Player();
	
	public static void bgmPlay(SoundVO vo) {
		if(bgmPlayer!=null&&bgmPlayer.isPlaying()) {
			bgmPlayer.stop();
		}
		sound.play(bgmPlayer, vo);
		System.out.println("bgm스타트");
		
	}
	public static void effectPlay(SoundVO vo) {
		if(effectPlayer!=null&&effectPlayer.isPlaying()) {
			effectPlayer.stop();
		}
		sound.play(effectPlayer, vo);
		System.out.println("버튼");
	}
}
