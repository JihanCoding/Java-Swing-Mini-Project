package SoundManager;

import javazoom.jl.player.MP3Player;

public class SoundDAO {
	String comPath = "../MiniProject/src/sound/";
	
	public void play(MP3Player mp3, SoundVO sound) {
		mp3.play(comPath+sound.getName());
	}
	
	
	
	


}
