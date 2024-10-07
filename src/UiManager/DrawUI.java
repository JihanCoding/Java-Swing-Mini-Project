package UiManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class DrawUI extends JFrame {
    private static DrawUI instance; // 싱글턴 인스턴스
    public JPanel backgroundPanel;
    private ImageIcon backgroundIcon;

    // 프레임을 생성하는 메서드
    public DrawUI(String location, int width, int height) {
        // 기본적인 JFrame 설정
        setTitle("스마트인재개발원 TEAM E1I4-1 : Java Falling Text Game");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // 사용자가 화면 크기를 조절하지 못하게 설정
        setLayout(null);

        // 배경 이미지 로드
        backgroundIcon = new ImageIcon(getClass().getResource(location));

        // 커스텀 JPanel 생성
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        setContentPane(layeredPane);

        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null); // 레이아웃 매니저 비활성화
        backgroundPanel.setBounds(0, 0, width, height);
        backgroundPanel.setOpaque(false); // 패널을 투명하게 설정
        add(backgroundPanel);

        // 화면을 생성하고 보이기
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    // 싱글턴 인스턴스를 반환하는 메서드
    public static DrawUI getInstance(String location, int width, int height) {
        if (instance == null) {
            instance = new DrawUI(location, width, height);
        }
        return instance;
    }
    // 배경 이미지를 변경하는 메서드 추가
    public void setBackgroundImage(String imagePath) {
        backgroundIcon = new ImageIcon(getClass().getResource(imagePath));
        backgroundPanel.repaint(); // 배경 이미지를 변경한 후 패널을 다시 그리기
    }
    // 플레이스홀더가 있는 텍스트 필드 생성 메서드
    public JTextField createPlaceholderField(String placeholder, int x, int y, int width, int height) {
        JTextField field = new JTextField(placeholder);
        field.setBounds(x, y, width, height);
        field.setFont(new Font("Roboto", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        addPlaceholderBehavior(field, placeholder);
        return field;
    }

    // 플레이스홀더가 있는 비밀번호 필드 생성 메서드
    public JPasswordField createPlaceholderPasswordField(String placeholder, int x, int y, int width, int height) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setBounds(x, y, width, height);
        field.setFont(new Font("Roboto", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        addPlaceholderBehavior(field, placeholder);
        return field;
    }

    // 플레이스홀더 동작 추가 메서드
    private void addPlaceholderBehavior(JTextComponent field, String placeholderText) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (field.getText().equals(placeholderText)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
        });

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholderText);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // 패널 생성 메서드 (이미지 백그라운드)
    public JPanel createPanel(String imagePath, int x, int y, int width, int height) {
        return new JPanel() {
            private Image panelBackgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();

            {
                setLayout(null);
                setBounds(x, y, width, height);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(panelBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    // 이미지를 포함한 버튼 생성 메서드
    public JButton createImageButton(String imgLocation, String text, int x, int y, int width, int height) {
        JButton button = drawButton(imgLocation, x, y, width, height);
        button.setText(text);
        button.setFont(new Font("Roboto", Font.BOLD, 18));
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        return button;
    }

    // 버튼 생성 메서드
    public JButton drawButton(String imgLocation, int x, int y, int width, int height) {
        ImageIcon icon = resizeIcon(new ImageIcon(getClass().getResource(imgLocation)), width, height);
        JButton button = new JButton(icon);
        BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        button.setBounds(x, y, width, height);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(null);
        button.setFocusPainted(false);
        BufferedImage pressedImage = createDarkenedImage(img, 0.8f);
        buttonEffect(button, img, pressedImage);
        return button;
    }

    // 이미지 크기를 버튼 크기에 맞게 조절하는 메서드
    public ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    // 이미지를 어둡게 만드는 메서드
    private BufferedImage createDarkenedImage(BufferedImage image, float scale) {
        RescaleOp op = new RescaleOp(scale, 0, null);
        return op.filter(image, null);
    }

    private void buttonEffect(JButton button, BufferedImage img, BufferedImage pressedImage) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(new ImageIcon(pressedImage)); // 눌렸을 때 어두운 이미지로 변경
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setIcon(new ImageIcon(img)); // 버튼에서 손을 뗐을 때 원래 이미지로 복원
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(new ImageIcon(pressedImage)); // 마우스를 올렸을 때 어두운 이미지로 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(new ImageIcon(img)); // 마우스가 벗어났을 때 원래 이미지로 복원
            }
        });
    }
}
