package UiManager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScreenLobby extends JFrame {

	private JLayeredPane layeredPane;

	public ScreenLobby() {
		// 기본적인 JFrame 설정
		setTitle("Game Home Screen with Background");
		setSize(1366, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false); // 사용자가 화면 크기를 조절하지 못하게 설정

		// 레이아웃 매니저 사용 안함 (절대 위치 사용)
		setLayout(null);

		// 배경 이미지 로드
		ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/images/background.png"));

		// 커스텀 JPanel 생성

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(1366, 768));

		setContentPane(layeredPane);
		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// 배경 이미지 그리기
				g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
			}
		};
		backgroundPanel.setLayout(null); // 레이아웃 매니저 비활성화
		backgroundPanel.setBounds(0, 0, 1366, 768);
		backgroundPanel.setOpaque(false); // 패널을 투명하게 설정
		add(backgroundPanel);

		// 상점 누나
		ImageIcon girl = resizeIcon(new ImageIcon(getClass().getResource("/images/girlNomal.png")), 580, 684);
		JLabel girlLabel = new JLabel(girl, JLabel.CENTER);
		girlLabel.setBounds(-100, 100, 580, 684); // 화면 중앙에 배치 (1366x768 해상도 기준)

		// 기존의 backgroundPanel에 추가
		backgroundPanel.add(girlLabel);
		// 레이아웃 갱신 및 화면 다시 그리기
		backgroundPanel.revalidate();
		backgroundPanel.repaint();

		// 랭크 리스트
		ImageIcon rankList = resizeIcon(new ImageIcon(getClass().getResource("/images/rankList.png")), 500, 600);
		JLabel rankListLabel = new JLabel(rankList, JLabel.CENTER);
		rankListLabel.setBounds(500, 80, 500, 600); // 화면 중앙에 배치 (1366x768 해상도 기준)

		// 기존의 backgroundPanel에 추가
		backgroundPanel.add(rankListLabel);
		// 레이아웃 갱신 및 화면 다시 그리기
		backgroundPanel.revalidate();
		backgroundPanel.repaint();

		// 게임 시작 버튼
		ImageIcon startIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/start_button2.png")), 250, 177);
		JButton startButton = new JButton(startIcon);
		BufferedImage startImage = new BufferedImage(startIcon.getIconWidth(), startIcon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = startImage.createGraphics();
		startIcon.paintIcon(null, g, 0, 0);
		g.dispose();
		startButton.setFont(new Font("Arial", Font.PLAIN, 24));
		startButton.setBounds(1080, 530, 250, 177); // x, y, width, height
		startButton.setContentAreaFilled(false); // 버튼의 배경 투명하게 설정
		startButton.setBorderPainted(false); // 버튼 테두리 제거
		startButton.setBorder(null); // 버튼의 기본 테두리 제거
//        startButton.setFocusPainted(false);      // 포커스 시 나타나는 테두리 제거
		backgroundPanel.add(startButton);
		BufferedImage pressedStartImage = createDarkenedImage(startImage, 0.8f);

		// 사운드 ON 버튼
		ImageIcon soundOnIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/soundOn.png")), 75, 75);
		JButton soundOnButton = new JButton(soundOnIcon);
		soundOnButton.setBounds(1050, 10, 75, 75); // x, y, width, height
		soundOnButton.setContentAreaFilled(false); // 버튼의 배경 투명하게 설정
		soundOnButton.setBorderPainted(false); // 버튼 테두리 제거
		soundOnButton.setBorder(null); // 버튼의 기본 테두리 제거
//        startButton.setFocusPainted(false);      // 포커스 시 나타나는 테두리 제거
		backgroundPanel.add(soundOnButton);

		// 사운드 OFF 버튼
		ImageIcon soundOffIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/soundOff.png")), 75, 75);
		JButton soundOffButton = new JButton(soundOffIcon);
		soundOffButton.setBounds(1150, 10, 75, 75); // x, y, width, height
		soundOffButton.setContentAreaFilled(false); // 버튼의 배경 투명하게 설정
		soundOffButton.setBorderPainted(false); // 버튼 테두리 제거
		soundOffButton.setBorder(null); // 버튼의 기본 테두리 제거
//        startButton.setFocusPainted(false);      // 포커스 시 나타나는 테두리 제거
		backgroundPanel.add(soundOffButton);

		// 종료 버튼
		ImageIcon exitIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/exit_button.png")), 75, 80);
		JButton exitButton = new JButton(exitIcon);
		exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
		exitButton.setBounds(1250, 10, 75, 80); // x, y, width, height
		exitButton.setContentAreaFilled(false); // 버튼의 배경 투명하게 설정
		exitButton.setBorderPainted(false); // 버튼 테두리 제거
		exitButton.setBorder(null); // 버튼의 기본 테두리 제거
//        startButton.setFocusPainted(false);      // 포커스 시 나타나는 테두리 제거
		backgroundPanel.add(exitButton);

		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				startButton.setIcon(new ImageIcon(pressedStartImage)); // 눌렸을 때 어두운 이미지로 변경
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				startButton.setIcon(new ImageIcon(startImage)); // 버튼에서 손을 뗐을 때 원래 이미지로 복원
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				startButton.setIcon(new ImageIcon(pressedStartImage)); // 눌렸을 때 어두운 이미지로 변경// 마우스를 올렸을 때 다른 효과를 주고 싶다면 이곳에 추가
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startButton.setIcon(new ImageIcon(startImage)); // 마우스가 벗어났을 때 원래 이미지로 복원
			}
		});
		// 버튼 이벤트 설정
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 게임 시작 로직
				JOptionPane.showMessageDialog(null, "Starting Game...");
			}
		});

		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 프로그램 종료 로직
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	// 이미지 크기를 버튼 크기에 맞게 조절하는 메서드
	private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	// 이미지를 어둡게 만드는 메서드
	private BufferedImage createDarkenedImage(BufferedImage image, float scale) {
		RescaleOp op = new RescaleOp(scale, 0, null);
		return op.filter(image, null);
	}

	public static void main(String[] args) {
		// 홈 화면을 생성하고 보이기
		SwingUtilities.invokeLater(() -> {
			ScreenLobby homeScreen = new ScreenLobby();
			homeScreen.setVisible(true);
		});
	}
}
