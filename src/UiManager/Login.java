package UiManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import DBManager.Controller;
import DBManager.DBManager;
import DBManager.MemberVO;
import GameManager.Game;
import GameManager.State;
import SoundManager.SoundController;
import SoundManager.SoundVO;

public class Login {

	private ArrayList<MemberVO> list;
	private JPanel loginPanel;
	private JPanel rankPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton signupButton;
	private DrawUI drawUI;
	public static SceenManager sceen;
	private DBManager db;
	public static boolean isCheck = true;
	private static final int TEXT_PADDING = 10; // 텍스트 배경 패딩 설정

	public Login() {
		// DrawUI의 생성자 호출: 배경 이미지와 창의 크기 설정
		drawUI = DrawUI.getInstance("/images/ingameBack.png", 1366, 768);
		drawUI.setBackgroundImage("/images/ingameBack.png");
		if(isCheck)
		{
			SoundController.bgmPlay(new SoundVO("login.mp3", "로그인bgm"));
			isCheck =false;
		}
		
		
		drawUI.backgroundPanel.setOpaque(false);
		// 로그인 패널 생성 (이미지로 배경 설정)
		loginPanel = drawUI.createPanel("/images/loginpanel.png", 458, 204, 450, 300);
		
		
		// 로그인 패널 요소 추가
		createLoginPanelComponents();
		createRankPanelComponents();
		
		drawUI.backgroundPanel.add(loginPanel);
		drawUI.backgroundPanel.add(rankPanel);
		// 상점 누나
		ImageIcon girl = drawUI.resizeIcon(new ImageIcon(getClass().getResource("/images/girlNomal.png")), 580, 684);
		JLabel girlLabel = new JLabel(girl, JLabel.CENTER);
		girlLabel.setBounds(-100, 100, 560, 684); // 화면 중앙에 배치 (1366x768 해상도 기준)

		// 기존의 backgroundPanel에 추가
		drawUI.backgroundPanel.add(girlLabel);
		// 레이아웃 갱신 및 화면 다시 그리기
		drawUI.backgroundPanel.revalidate();
		drawUI.backgroundPanel.repaint();
		

		drawUI.setVisible(true);
		drawUI.backgroundPanel.setVisible(true);

	}

	private void createRankPanelComponents() {
	    rankPanel = new JPanel() {
	        private Image backgroundImage = new ImageIcon(getClass().getResource("/images/lastrankpanel.png")).getImage();
	        private Image textBackgroundImage = new ImageIcon(getClass().getResource("/images/optionBackground.png")).getImage(); // 텍스트 배경 이미지

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            // 패널 배경 이미지 그리기
	            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); 

	            g.setFont(new Font("Roboto", Font.PLAIN, 16)); // 텍스트 글꼴 설정
	            g.setColor(Color.WHITE); // 텍스트 색상 설정

	            // 패널의 너비를 계산
	            int panelWidth = getWidth();
	            int y = 100; // 첫 번째 텍스트의 y 좌표 설정

	            list = new ArrayList<MemberVO>();
	            if (setRangking().size() > 8) {
	                for (int i = 0; i < 8; i++) {
	                    list.add(setRangking().get(i));
	                }
	            } else {
	                for (int i = 0; i < setRangking().size(); i++) {
	                    list.add(setRangking().get(i));
	                }
	            }

	            // 랭킹 리스트 텍스트 그리기
	            for (int i = 0; i < list.size(); i++) {
	                // 텍스트 내용 생성
	                String rank = (i + 1) + "등 : " + list.get(i).getId() + "님 " + "최고점수: " + list.get(i).getScore();
	                
	                // 텍스트 크기 계산
	                FontMetrics metrics = g.getFontMetrics();
	                int textWidth = metrics.stringWidth(rank); // 텍스트 너비 계산
	                int textHeight = metrics.getHeight(); // 텍스트 높이 계산

	                // 배경 이미지 크기 계산
	                int backgroundWidth = 360; // 고정된 배경 너비
	                int backgroundHeight = textHeight + 2 * 10; // 텍스트 높이에 패딩 추가 (TEXT_PADDING 대신 직접 수치 사용)

	                // 텍스트와 배경을 패널의 중앙에 배치하기 위한 X 좌표 계산
	                int x = (panelWidth - backgroundWidth) / 2; // 패널 기준으로 가운데 정렬

	                // 텍스트 배경 이미지 그리기
	                g.drawImage(textBackgroundImage, x, y - textHeight - 5, backgroundWidth, backgroundHeight, this); 

	                // 텍스트 그리기
	                g.setColor(Color.WHITE); // 텍스트 색상 설정
	                g.drawString(rank, x + 10, y); // 텍스트 그리기

	                // 다음 텍스트를 위한 Y 좌표 이동
	                y += textHeight + 40; // 텍스트 간의 간격 설정
	            }
	        }
	    };

	    rankPanel.setBorder(null);
	    rankPanel.setOpaque(false);

	    // Layout, 위치 및 크기 설정
	    rankPanel.setLayout(null);
	    rankPanel.setBounds(930, 50, 400, 600); // 패널의 위치와 크기 설정

	    // rankPanel이 제대로 보이도록 설정
	    rankPanel.setVisible(true);
	    drawUI.backgroundPanel.add(rankPanel);
	}
	private void createLoginPanelComponents() {
		// 사용자 이름 텍스트 필드
		usernameField = drawUI.createPlaceholderField("사용자 이름", 50, 80, 340, 40);
		usernameField.setForeground(Color.BLACK);
		addPlaceholderReset(usernameField, "사용자 이름");

		
		// 글자수 제한을 위한 DocumentFilter 설정 (예: 10자로 제한)
        ((AbstractDocument) usernameField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private int limit = 10; // 글자 수 제한

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= limit) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() + text.length() - length <= limit) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
		loginPanel.add(usernameField);
		// 비밀번호 필드
		passwordField = drawUI.createPlaceholderPasswordField("비밀번호", 50, 140, 340, 40);
		passwordField.setForeground(Color.BLACK);
		addPlaceholderReset(passwordField, "비밀번호");
		 ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
	            private int limit = 20; // 글자 수 제한

	            @Override
	            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
	                if (fb.getDocument().getLength() + string.length() <= limit) {
	                    super.insertString(fb, offset, string, attr);
	                }
	            }

	            @Override
	            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	                if (fb.getDocument().getLength() + text.length() - length <= limit) {
	                    super.replace(fb, offset, length, text, attrs);
	                }
	            }
	        });
		loginPanel.add(passwordField);
		 addEnterKeyLoginAction();
		// 로그인 버튼 추가
		loginButton = drawUI.createImageButton("/images/optionBackground.png", "로그인", 75, 230, 140, 40);
		loginButton.setForeground(Color.WHITE);
		loginButton.addActionListener(e -> {
			
			String username = usernameField.getText();
			char[] passwords = passwordField.getPassword();
			String password = new String(passwords);
			username = username.toUpperCase();
			password = password.toUpperCase();
			System.out.println(password);
			db.con = new Controller();
			if (!username.equals(null) && !password.equals(null)) {
				String check = db.con.contSelect(new MemberVO(username, password, null, 0, 0));
				System.out.println(username);
				System.out.println(check);
				if (username.equals(check)) {
					// 다음화면
					SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
					Game.logined_id = username;
					Game.logined_pw = password;
					Game.logined_score = db.con.selectScore(new MemberVO(username, password, null, 0, 0));

					drawUI.backgroundPanel.removeAll();
					State.isLogin = false;
					State.isGame = true;
					drawUI.setVisible(false);
					State.checkState();
				} else {
					SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
					JOptionPane.showMessageDialog(drawUI, "잘못된 사용자 이름 또는 비밀번호입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				}
			}
			

		});
		loginPanel.add(loginButton);
		loginPanel.setBorder(null);
		loginPanel.setOpaque(false);

		// 회원가입 버튼 추가
		signupButton = drawUI.createImageButton("/images/optionBackground.png", "회원가입", 235, 230, 140, 40);
		signupButton.setForeground(Color.WHITE);
		signupButton.addActionListener(e -> switchToSignUpPanel());
		State.isCheck = false;
		loginPanel.add(signupButton);
	}
	private void handleLogin() {
	    String username = usernameField.getText();
	    char[] passwords = passwordField.getPassword();
	    String password = new String(passwords);
	    username = username.toUpperCase();
	    password = password.toUpperCase();

	    db.con = new Controller();
	    if (!username.equals("") && !password.equals("")) {
	        String check = db.con.contSelect(new MemberVO(username, password, null, 0, 0));
	        if (username.equals(check)) {
	            SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
	            Game.logined_id = username;
	            Game.logined_pw = password;
	            Game.logined_score = db.con.selectScore(new MemberVO(username, password, null, 0, 0));

	            drawUI.backgroundPanel.removeAll();
	            State.isLogin = false;
	            State.isGame = true;
	            drawUI.setVisible(false);
	            State.checkState();
	        } else {
	            SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
	            JOptionPane.showMessageDialog(drawUI, "잘못된 사용자 이름 또는 비밀번호입니다.", "오류", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}

	private void addEnterKeyLoginAction() {
	    ActionListener loginActionListener = e -> handleLogin();

	    // 사용자 이름 필드에서 엔터 키 입력시 로그인
	    usernameField.addActionListener(loginActionListener);

	    // 비밀번호 필드에서 엔터 키 입력시 로그인
	    passwordField.addActionListener(loginActionListener);
	    
	}
	
	private void switchToSignUpPanel() {
		SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
		drawUI.backgroundPanel.removeAll();
		State.isLogin = false;
		State.isSignUp = true;
		State.checkState();
	}

	// 플레이스홀더 초기화 및 포커스 아웃시 재설정 로직 추가 메소드
	private void addPlaceholderReset(JTextField textField, String placeholder) {
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setText(placeholder);
				}
			}
		});
	}

	public ArrayList<MemberVO> setRangking() {
		db.con = new Controller();
		ArrayList<MemberVO> vo = db.con.congetRanking();
		return vo;
	}
}
