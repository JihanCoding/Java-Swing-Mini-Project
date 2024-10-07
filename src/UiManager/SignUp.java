package UiManager;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import DBManager.Controller;
import DBManager.MemberVO;
import GameManager.State;
import SoundManager.SoundController;
import SoundManager.SoundVO;

public class SignUp {

	private JPanel signUpPanel;
	private DrawUI drawUI;
	private String id,pw;
	
	private int age;
	public SignUp() {
		// DrawUI의 생성자 호출: 배경 이미지와 창의 크기 설정

		drawUI = DrawUI.getInstance("/images/background.png", 1366, 768);

		// 회원가입 패널 생성 (초기에는 보이지 않게 설정)
		signUpPanel = drawUI.createPanel("/images/signuppanel.png", 458, 200, 450, 400);
		// 회원가입 패널 요소 추가
		createSignUpPanelComponents();
		signUpPanel.setVisible(false);
		signUpPanel.setOpaque(false);
		drawUI.backgroundPanel.add(signUpPanel);
		drawUI.backgroundPanel.repaint();

//		// 화면을 보이도록 설정
//		drawUI.setBackgroundImage("/images/background.png");
		signUpPanel.setVisible(true);
//		drawUI.backgroundPanel.setOpaque(false);
//		drawUI.backgroundPanel.setVisible(true);
	}

	private void createSignUpPanelComponents() {
		// 아이디 텍스트 필드
		JTextField signupUsernameField = drawUI.createPlaceholderField("사용자 이름", 50, 80, 340, 40);
		addPlaceholderReset(signupUsernameField,"사용자 이름");
		// 글자수 제한을 위한 DocumentFilter 설정 (예: 10자로 제한)
        ((AbstractDocument) signupUsernameField.getDocument()).setDocumentFilter(new DocumentFilter() {
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
		signUpPanel.add(signupUsernameField);

		// 비밀번호 필드
		JPasswordField signupPasswordField = drawUI.createPlaceholderPasswordField("비밀번호", 50, 140, 340, 40);
		addPlaceholderReset(signupPasswordField,"비밀번호");
		// 글자수 제한을 위한 DocumentFilter 설정 (예: 10자로 제한)
        ((AbstractDocument) signupPasswordField.getDocument()).setDocumentFilter(new DocumentFilter() {
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
		signUpPanel.add(signupPasswordField);

		// 이름 텍스트 필드
		JTextField nameField = drawUI.createPlaceholderField("이름", 50, 200, 340, 40);
		addPlaceholderReset(nameField,"이름");
		// 글자수 제한을 위한 DocumentFilter 설정 (예: 10자로 제한)
        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private int limit = 5; // 글자 수 제한

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
		signUpPanel.add(nameField);

		// 나이 텍스트 필드
		JTextField ageField = drawUI.createPlaceholderField("나이", 50, 260, 340, 40);
		addPlaceholderReset(ageField,"나이");
		// 글자수 제한을 위한 DocumentFilter 설정 (예: 10자로 제한)
        ((AbstractDocument) ageField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private int limit = 3; // 글자 수 제한

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
		signUpPanel.add(ageField);

		
		// 회원가입 버튼 추가
		JButton submitButton = drawUI.createImageButton("/images/optionBackground.png", "제출", 70, 320, 140, 40);
		submitButton.setForeground(Color.WHITE);
		submitButton.addActionListener(e -> {
			SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
			// 필수 필드가 비어 있는지 확인
			System.out.println(signupUsernameField.getText());
			char[] passwords = signupPasswordField.getPassword();
			String password = new String(passwords);
			if (signupUsernameField.getText().equals("사용자 이름") || password.equals("비밀번호")
					|| nameField.getText().equals("이름") || ageField.getText().equals("나이")) {
				JOptionPane.showMessageDialog(drawUI, "모든 필드를 입력해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
				System.out.println("들어옴");
			} else {
					Controller con = new Controller();
					ArrayList<MemberVO> vo = con.contSelectAll();
					System.out.println("멤버 전체의 정보를 출력합니다.");
					id = signupUsernameField.getText();
					id = id.toUpperCase();
					System.out.println(id);
					pw = password;
					pw = pw.toUpperCase();
					System.out.println(pw);
					boolean isCheck=false;
					for(MemberVO x : vo)
					{
						
						try {
							System.out.print(x.getId());
//							System.out.println(id+x.getId());
						    age = Integer.valueOf(ageField.getText()).intValue();
						    if(id.equals(x.getId())) {
								JOptionPane.showMessageDialog(drawUI, "이미 존재하는 아이디입니다.", "오류", JOptionPane.ERROR_MESSAGE);
								isCheck=true;
								break;
							}
	
						} catch (NumberFormatException a) {
						    // 변환이 실패한 경우 처리
						    JOptionPane.showMessageDialog(drawUI, "니 나이는 문자형이니?", "오류", JOptionPane.ERROR_MESSAGE);
						    isCheck=true;
						    break;
						    // 오류 처리 로직을 추가하거나 사용자에게 오류 메시지를 표시할 수 있음
						}
						
					}
					if(!isCheck) {
						con.contInsert(new MemberVO(id, pw, nameField.getText(), age, 0));
						JOptionPane.showMessageDialog(drawUI, "회원가입 성공!", "성공", JOptionPane.INFORMATION_MESSAGE);
						
						System.out.println("로그인패널로");
					}
					switchToLoginPanel(); // 회원가입 후 로그인 패널로 돌아가기
			}
		});
		signUpPanel.add(submitButton);

		// 뒤로가기 버튼 추가
		JButton backButton = drawUI.createImageButton("/images/optionBackground.png", "뒤로가기", 240, 320, 140, 40);
		backButton.setForeground(Color.WHITE);
//		Login.isCheck = true;
		backButton.addActionListener(e -> switchToLoginPanel());
		signUpPanel.add(backButton);
	}

	private void switchToLoginPanel() {
		SoundController.effectPlay(new SoundVO("button.mp3", "버튼클릭음"));
		drawUI.backgroundPanel.removeAll();
		State.isLogin = true;
		State.isSignUp = false;
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
}
