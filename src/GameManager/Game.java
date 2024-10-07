package GameManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import DBManager.Controller;
import DBManager.MemberVO;
import SoundManager.EffectController;
import SoundManager.SoundController;
import SoundManager.SoundVO;

public class Game extends JFrame implements ActionListener {

	private ArrayList<String> words = new ArrayList<>();
	private Controller con = new Controller();
	// 타이머 객체: 게임 진행을 제어하는 타이머와 텍스트 생성 타이머
	private Timer gameTimer;
	private Timer textTimer;

	// 패널 객체: 메인 패널과 텍스트 패널
	private JPanel mainPanel;
	private JPanel textPanel;
	private JPanel scorePanel;

	private JLabel scoreTextLabel;
	private JLabel scoreImageLabel;

	private JLabel stageTextLabel;
	private JLabel stageImageLabel;

	public static String logined_id;
	public static String logined_pw;
	public static int logined_score;
	// 정답을 입력할 텍스트 필드
	private JTextField inputField;
	// 텍스트 리스트: 화면에 표시될 텍스트 객체들을 저장하는 리스트
	private ArrayList<Text> textList;

	// 게임 설정: 텍스트 이동 속도, 레벨당 생성되는 텍스트 수, 남은 시간
	private int textSpeed;
	private int textsPerLevel;
	private int timeLeft;
	private int score = 0;
	private int maxscore = 0;

	private boolean stageone = false;
	private boolean stagetwo = false;
	private boolean stagethree = false;
	private boolean stagefour = false;

	// 텍스트 간의 최소 간격 설정
	private static final int MIN_X_DISTANCE = 90;
	private static final int MIN_Y_DISTANCE = 90;

	// 텍스트와 배경 이미지 사이의 여백
	private static final int TEXT_PADDING = 20;
	private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 32);
	// 동기화를 위한 객체 락
	private final Object lock = new Object();

	// 생성자: JFrame 초기 설정 및 게임 구성 요소 초기화
	public Game() {
		SoundController.bgmPlay(new SoundVO("ingame.mp3", "인게임bgm"));
		SoundController.effectPlay(new SoundVO("gamestart.mp3", "버튼클릭음"));
		setData();
		System.out.println(con.selectScore(new MemberVO(logined_id, logined_pw, null, 0, logined_score)));
		this.setVisible(true);
		setResizable(false);
		// 기본적인 JFrame 설정
		setTitle("스마트인재개발원 TEAM E1I4-1 : Java Falling Text Game");
		setSize(1366, 768); // 화면 크기 설정 (1366x768 해상도)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 버튼 누를 시 프로그램 종료
		setLocationRelativeTo(null); // 창을 화면 중앙에 위치시킴

		// JLayeredPane 생성: 여러 레이어에서 컴포넌트들을 쌓아올릴 수 있는 컨테이너
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(1366, 768)); // 레이어드 팬의 기본 크기 설정

		// 메인 패널 초기화 및 설정 (배경 이미지 포함)
		mainPanel = new JPanel() {
			private Image backgroundImage = new ImageIcon(getClass().getResource("/images/ingameBack.png")).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// 배경 이미지 그리기
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		scorePanel = new JPanel();
		// 텍스트 패널 초기화 및 설정 (텍스트 배경 이미지 포함)
		textPanel = new JPanel() {
			private Image backgroundImage = new ImageIcon(getClass().getResource("/images/cloud.png")).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				synchronized (lock) {
					g.setFont(new Font("Roboto", Font.BOLD, 24)); // 텍스트의 글꼴과 크기 설정
					g.setColor(Color.BLACK); // 텍스트 색상 설정

					// 텍스트 리스트에 있는 각 텍스트 객체에 대해 배경 이미지와 텍스트를 그리기
					for (Text text : textList) {
						// 텍스트 크기 계산
						FontMetrics metrics = g.getFontMetrics();
						int textWidth = metrics.stringWidth(text.getText()); // 텍스트 너비 계산
						int textHeight = metrics.getHeight(); // 텍스트 높이 계산

						// 텍스트 배경 이미지 크기 계산
						int backgroundWidth = textWidth + 2 * TEXT_PADDING; // 텍스트 너비에 패딩 추가
						int backgroundHeight = textHeight + 2 * TEXT_PADDING; // 텍스트 높이에 패딩 추가

						// 텍스트 배경 이미지 그리기 (텍스트보다 약간 크게)
						g.drawImage(backgroundImage, text.getX() - TEXT_PADDING,
								text.getY() - textHeight - TEXT_PADDING, backgroundWidth, backgroundHeight, this);

						// 텍스트를 배경 이미지의 정 가운데에 그리기
						int textX = text.getX() + (backgroundWidth - textWidth) / 2 - TEXT_PADDING;
						int textY = text.getY() - (backgroundHeight - textHeight) / 2 - TEXT_PADDING
								+ metrics.getAscent();
						g.drawString(text.getText(), textX, textY); // 텍스트 그리기
					}

				}
			}
		};

		// 이미지 아이콘 생성
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/scorewood.png"));

		// 이미지 크기 조절 (300x100)
		Image scaledImage = originalIcon.getImage().getScaledInstance(400, 150, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		int scorecenterX = (1366 - 1000) / 2;

		// 이미지용 JLabel 생성
		scoreImageLabel = new JLabel(scaledIcon);
		scoreImageLabel.setBounds(scorecenterX, 10, 1000, 100); // 이미지 좌표 및 크기 설정

		// 텍스트용 JLabel 생성
		scoreTextLabel = new JLabel("<html>현재 점수: 0<br>최고 점수: " + logined_score + "</html>");
		scoreTextLabel.setFont(new Font("Roboto", Font.BOLD, 26));
		scoreTextLabel.setForeground(Color.WHITE); // 텍스트 색상 설정
		scoreTextLabel.setBounds(scorecenterX + 400, 5, 300, 100); // 텍스트 좌표 및 크기 설정

		// 이미지 아이콘 생성
		ImageIcon stageoriginalIcon = new ImageIcon(getClass().getResource("/images/scorewood.png"));

		// 이미지 크기 조절 (300x100)
		Image stagescaledImage = stageoriginalIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		ImageIcon stagescaledIcon = new ImageIcon(stagescaledImage);
		int stagescorecenterX = (1366 - 2150) / 2;

		// 이미지용 JLabel 생성
		stageImageLabel = new JLabel(stagescaledIcon);
		stageImageLabel.setBounds(stagescorecenterX, 10, 1000, 100); // 이미지 좌표 및 크기 설정

		// 텍스트용 JLabel 생성
		stageTextLabel = new JLabel("Stage : 1");
		stageTextLabel.setFont(new Font("Roboto", Font.BOLD, 32));
		stageTextLabel.setForeground(Color.WHITE); // 텍스트 색상 설정
		stageTextLabel.setBounds(stagescorecenterX + 433, 5, 300, 100); // 텍스트 좌표 및 크기 설정

		// 패널에 라벨 추가
		scorePanel.setBounds(0, 0, 1366, 768); // 텍스트 패널의 위치와 크기 설정
		scorePanel.setOpaque(false); // 텍스트 패널을 투명하게 설정 (배경 이미지가 보여야 하므로)

		textPanel.setBounds(0, 0, 1366, 768); // 텍스트 패널의 위치와 크기 설정
		textPanel.setOpaque(false); // 텍스트 패널을 투명하게 설정 (배경 이미지가 보여야 하므로)

		inputField = new CenteredTextField(20);
		inputField.setBackground(Color.WHITE);
		inputField.setForeground(Color.BLACK);
		inputField.setFont(new Font("Roboto", Font.BOLD, 20));
		inputField.setBounds((1366 - 400) / 2, 660, 400, 60);
		inputField.addActionListener(this);

		// 입력 필드를 중앙에 위치시킴
		int inputFieldWidth = 300;
		int inputFieldHeight = 50;
		int centerX = (1366 - inputFieldWidth) / 2;
		int centerY = 660;
		inputField.setBounds(centerX, centerY, inputFieldWidth, inputFieldHeight);

		// 텍스트 리스트 초기화
		textList = new ArrayList<>();

		// 게임 설정 초기화
		textSpeed = 3;
		textsPerLevel = 1;

		// 메인 패널의 위치와 크기를 설정 (JFrame 크기에 맞춤)
		mainPanel.setBounds(0, 0, 1366, 768);

		// 게임 진행을 제어하는 타이머 설정 (100ms마다 updateGame()을 호출)
		gameTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGame(); // 게임 업데이트
			}
		});
		generateFallingTexts(4);
		gameTimer.start(); // 타이머 시작

		// 게임 진행을 제어하는 타이머 설정 (2000ms마다 텍스트 생성
		textTimer = new Timer(20000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateFallingTexts(textsPerLevel); // 새로운 텍스트 생성
			}
		});
		textTimer.start(); // 타이머 시작

		// JLayeredPane에 메인 패널과 텍스트 패널 추가
		layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER); // 메인 패널을 가장 아래 레이어에 추가
		layeredPane.add(textPanel, JLayeredPane.PALETTE_LAYER); // 텍스트 패널을 메인 패널 위의 레이어에 추가
		layeredPane.add(inputField, JLayeredPane.MODAL_LAYER); // 텍스트 패널을 메인 패널 위의 레이어에 추가
		layeredPane.add(scoreTextLabel, JLayeredPane.DRAG_LAYER); // 텍스트 패널을 메인 패널 위의 레이어에 추가
		layeredPane.add(scoreImageLabel, JLayeredPane.POPUP_LAYER); // 텍스트 패널을 메인 패널 위의 레이어에 추가
		layeredPane.add(stageTextLabel, JLayeredPane.DRAG_LAYER); // 텍스트 패널을 메인 패널 위의 레이어에 추가
		layeredPane.add(stageImageLabel, JLayeredPane.POPUP_LAYER); // 텍스트 패널을 메인 패널 위의 레이어에 추가
		// 레이아웃 설정
		mainPanel.setLayout(null); // 레이아웃 매니저를 사용하지 않음 (절대 위치 사용)

		// JFrame에 JLayeredPane 추가
		add(layeredPane);
	}

	// 게임 상태 업데이트: 텍스트를 아래로 이동시키고 화면을 다시 그리는 메서드
	private void updateGame() {
		synchronized (lock) { // 동기화 블록을 사용하여 여러 스레드에서의 충돌 방지
			Iterator<Text> iterator = textList.iterator();
			boolean gameOver = false;

			while (iterator.hasNext()) {
				Text text = iterator.next();

				if (score == 1000 && !stageone) {
					stageone = true;
					SoundController.effectPlay(new SoundVO("stage02.mp3", "버튼클릭음"));
					textSpeed = 5;
					stageTextLabel.setText("Stage : " + 2);

				} else if (score == 2000 && !stagetwo) {
					stagetwo = true;

					SoundController.effectPlay(new SoundVO("stage03.mp3", "버튼클릭음"));
					textSpeed = 7;
					stageTextLabel.setText("Stage : " + 3);

				} else if (score == 3000 && !stagethree) {
					stagethree = true;

					SoundController.effectPlay(new SoundVO("stage04.mp3", "버튼클릭음"));
					textSpeed = 9;
				
					stageTextLabel.setText("Stage : " + 4);

				}

				text.move(textSpeed); // 텍스트를 아래로 이동

				if (text.getY() > 768) { // 텍스트가 화면 아래로 사라지면
					SoundController.bgmPlay(new SoundVO("lose.mp3", "게임종료bgm"));
					iterator.remove(); // 리스트에서 텍스트 제거
					gameOver = true; // 게임 오버 상태를 기록
				}
			}

			// 게임이 종료된 경우 처리
			if (gameOver) {
				textList.clear(); // 반복이 끝난 후 리스트를 비움
				gameTimer.stop();
				textTimer.stop();
				

				// 커스텀 버튼 텍스트 배열
				String[] options = {"다시 시작", "종료"};

				int option = JOptionPane.showOptionDialog(
				    this,
				    "게임 오버!!\n게임을 다시 진행 하겠습니까?",
				    "Game Over",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options, // 커스텀 버튼 텍스트 배열
				    options[0] // 기본 선택 버튼 ("다시 시작")
				);

				if (option == JOptionPane.YES_OPTION) {
				    restartGame(); // 게임 상태 재설정 및 타이머 재시작
				    SoundController.effectPlay(new SoundVO("gamestart.mp3", "버튼클릭음"));
				    stageone = false;
				    stagetwo = false;
				    stagethree = false;
				} else {
				    if (logined_score < score) {
				        logined_score = score;
				    }
				    con.contUpdate(new MemberVO(logined_id, logined_pw, null, 0, logined_score));
				    System.exit(0); // 게임을 종료
				}
			}
		}

		textPanel.repaint(); // 텍스트 패널 다시 그리기
		scorePanel.repaint();
	}

	private void restartGame() {
		SoundController.bgmPlay(new SoundVO("ingame.mp3", "인게임bgm"));
		gameTimer.start();
		generateFallingTexts(4);
		textTimer.start();
		if (logined_score < score) {
			logined_score = score;
		}
		con.contUpdate(new MemberVO(logined_id, logined_pw, null, 0, logined_score));
		score = 0;
		textSpeed = 3;
		stageTextLabel.setText("Stage : " + 1);
		scoreTextLabel.setText("<html>현재 점수: " + score + "<br>최고 점수: " + logined_score + "</html>");
		inputField.setText(""); // 입력 필드 초기화
	}

	private JLabel createLabel(String text, Color clr, int x, int y, int width, int height, Font font) {
		JLabel label = new JLabel(text);
		label.setForeground(clr);
		label.setBounds(x, y, width, height);
		label.setFont(font);
		return label;
	}

	// 새로운 텍스트를 생성하는 메서드
	private void generateFallingTexts(int numberOfTexts) {
		Random rand = new Random(); // 무작위 위치 및 텍스트를 생성하기 위한 Random 객체

		for (int i = 0; i < numberOfTexts; i++) {
			String word = words.get(rand.nextInt(words.size())); // 무작위 단어 선택
			int x, y;
			boolean collision;
			int attempts = 0; // 시도 횟수 카운터
			final int MAX_ATTEMPTS = 100; // 최대 시도 횟수

			do {
				collision = false;
				x = rand.nextInt(1366 - 100); // 무작위 X 좌표
				y = -rand.nextInt(100); // 무작위 Y 좌표 (화면 위에서 시작하도록 음수 설정)

				synchronized (lock) { // 동기화 블록
					for (Text text : textList) {
						// 다른 텍스트와의 충돌 여부 검사
						if (Math.abs(text.getX() - x) < MIN_X_DISTANCE && Math.abs(text.getY() - y) < MIN_Y_DISTANCE) {
							collision = true; // 충돌이 발생하면 반복문 탈출
							break;
						}
					}
				}

				attempts++;
				if (attempts >= MAX_ATTEMPTS) {
					System.out.println("최대 시도 횟수 초과, 텍스트 생성을 건너뜀."); // 최대 시도 횟수 초과 시 텍스트 생성 건너뜀
					break;
				}
			} while (collision); // 충돌이 없는 위치를 찾을 때까지 반복

			if (!collision) {
				synchronized (lock) {
					if (attempts < MAX_ATTEMPTS) {
						textList.add(new Text(word, x, y, textSpeed)); // 충돌이 없으면 텍스트 리스트에 추가
						System.out.println("텍스트 생성 완료: " + word);
					}
				}
			}
		}
	}

	private boolean isLastActionCorrect = false; // 마지막 액션이 정답이었는지 추적

	@Override
	public void actionPerformed(ActionEvent e) {
	    SoundController.effectPlayer.stop();
	    SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
	    String input = inputField.getText().trim().toLowerCase();
	    Iterator<Text> iterator = textList.iterator();

	    boolean found = false;
	    while (iterator.hasNext()) {
	        Text text = iterator.next();
	        if (text.getText().toLowerCase().equals(input)) {
	            iterator.remove();
	            SoundController.effectPlay(new SoundVO("ingame_ok.mp3", "정답시"));
	            score += 100;
	            scoreTextLabel.setText("<html>현재 점수: " + score + "<br>최고 점수:" + logined_score + "</html>");
	            inputField.setText("");
	            found = true;
	            break;
	        }
	    }

	    if (!found) { // 정답을 못 맞춘 경우, 연속 입력 방지
	    	EffectController con = new EffectController();
	    	con.effectPlayer.play("../MiniProject/src/sound/ingame_x.mp3");
	        inputField.setText("");
	    }

	    // 텍스트가 제거되었으면 새로운 텍스트 생성
	    if (found) {
	        generateFallingTexts(1);
	    }
	}

	public void setData() {
		ArrayList<MemberVO> vo = con.conselectWordsAll();

		for (MemberVO x : vo) {
			String word = x.getWord().toLowerCase();
			words.add(word);
		}
	}

}