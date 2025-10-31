package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;

public class TrangChuNV_GUI extends JFrame implements ActionListener, MouseListener{
    private JPanel leftPanel, topPanel, contentPanel;
    private JLabel lblTenHieuThuoc, lblTenNV, lblChucVu;
    private JLabel imgTaiKhoan, imgDangXuat, imgLogo;
    private Map<String, JPanel> menuItems = new HashMap<>();
    private Map<String, String> iconPaths = new HashMap<>();

    private String selectedMenu = ""; // menu đang được chọn

    private final Color COLOR_SELECTED = new Color(0, 173, 254);
    private final Color COLOR_HOVER = new Color(230, 245, 255);
    private final Color COLOR_DEFAULT = Color.WHITE;

    public TrangChuNV_GUI() {
        setTitle("Quản lý hiệu thuốc Tam Thanh");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // ========== THANH TRÊN ==========
        taoTopPanel();

        // ========== THANH MENU TRÁI ==========
        taoLeftPanel();

        // ========== KHU VỰC CHÍNH ==========
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // ========== THÊM MENU CHÍNH ==========
        thietLapMenu("Trang chủ", "/img/dashboard.png", taoPanelTrangChu());
        thietLapMenu("Thuốc", "/img/addMedicine.png", taoPanelTam("Thuốc"));
        thietLapMenu("Kệ Thuốc", "/img/shelf.png", taoPanelTam("Kệ Thuốc"));
        thietLapMenu("Khách Hàng", "/img/customer.png", taoPanelTam("Khách Hàng"));
        thietLapMenu("Hóa Đơn", "/img/order.png", taoPanelTam("Hóa Đơn"));

        setThuocTinhMainMenu();

        // Hiển thị trang chủ mặc định
        hienThiTrangChu();

        setVisible(true);
    }

    // ================== THANH TRÊN ==================
    private void taoTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        // LEFT
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoPanel.setBackground(Color.WHITE);
        imgLogo = new JLabel(setUpIcon("/img/logo.jpg", 40, 40));
        lblTenHieuThuoc = new JLabel("NHÀ THUỐC TAM THANH");
        lblTenHieuThuoc.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoPanel.add(imgLogo);
        logoPanel.add(lblTenHieuThuoc);
        topPanel.add(logoPanel, BorderLayout.WEST);

        // RIGHT
        JPanel nvPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        nvPanel.setBackground(Color.WHITE);
        lblTenNV = new JLabel("Trần Thanh Toàn");
        lblTenNV.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblChucVu = new JLabel("Nhân viên bán hàng");
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(lblTenNV);
        namePanel.add(lblChucVu);
        imgTaiKhoan = new JLabel(setUpIcon("/img/user.png", 20, 20));
        imgDangXuat = new JLabel(setUpIcon("/img/signOut.png", 20, 20));
        imgDangXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nvPanel.add(namePanel);
        nvPanel.add(imgTaiKhoan);
        nvPanel.add(imgDangXuat);
        imgDangXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    new DangNhap_GUI().setVisible(true);
                }
            }
        });
        topPanel.add(nvPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    // ================== MENU TRÁI ==================
    private void taoLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 247, 250));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(leftPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        add(scroll, BorderLayout.WEST);
    }

 // ================== THÊM MENU ==================
    public void thietLapMenu(String text, String iconPath, JPanel linkedPanel) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(245, 247, 250));

        JPanel item = new JPanel(new GridBagLayout());
        item.setPreferredSize(new Dimension(220, 50));
        item.setMaximumSize(new Dimension(220, 50));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 8);
        JLabel icon = new JLabel(setUpIcon(iconPath, 22, 22));
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        item.add(icon, gbc);
        gbc.gridx = 1;
        item.add(label, gbc);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                highlightSelectedMenu(text);
                setUpNoiDung(linkedPanel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!text.equals(selectedMenu))
                    item.setBackground(new Color(230, 245, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!text.equals(selectedMenu))
                    item.setBackground(Color.WHITE);
            }
        });

        container.add(item);
        leftPanel.add(container);
        leftPanel.add(Box.createVerticalStrut(5));

        menuItems.put(text, item);
        iconPaths.put(text, iconPath);
    }

 // ================== MENU CON ==================
    public void setMauClickVaMenuCon(JPanel menuChinh, List<String> menuCons) {
        if (menuChinh == null) return;
        JPanel container = (JPanel) menuChinh.getParent();
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setBackground(new Color(245, 247, 250));
        subMenuPanel.setVisible(false);

        for (String sub : menuCons) {
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
            item.setPreferredSize(new Dimension(220, 50));
            item.setMaximumSize(new Dimension(220, 50));
            item.setBackground(new Color(250, 250, 250));
            JLabel lbl = new JLabel(sub);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            item.add(lbl);
            subMenuPanel.add(item);
        }

        container.add(subMenuPanel);

        menuChinh.addMouseListener(new MouseAdapter() {
            boolean isOpen = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                isOpen = !isOpen;
                subMenuPanel.setVisible(isOpen);
                leftPanel.revalidate();
                leftPanel.repaint();
            }
        });
    }

    // ================== MENU CHÍNH & CON ==================
    public void setThuocTinhMainMenu() {
        setMauClickVaMenuCon(menuItems.get("Trang chủ"), List.of());
        setMauClickVaMenuCon(menuItems.get("Thuốc"),
                List.of("Tìm kiếm thuốc", "Thêm thuốc", "Nhập thuốc", "Đặt thuốc bán"));
        setMauClickVaMenuCon(menuItems.get("Kệ Thuốc"), List.of("Danh sách kệ", "Thêm kệ thuốc"));
        setMauClickVaMenuCon(menuItems.get("Khách Hàng"),
                List.of("Tìm kiếm khách hàng", "Thêm khách hàng", "Khiếu nại & Hỗ trợ"));
        setMauClickVaMenuCon(menuItems.get("Hóa Đơn"),
                List.of("Tìm kiếm hóa đơn", "Danh sách phiếu đặt thuốc", "Lập hóa đơn", "Đặt thuốc",
                        "Danh sách phiếu đổi trả"));
    }

    // ================== TRANG CHỦ ==================
    public void hienThiTrangChu() {
    	setUpNoiDung(taoPanelTrangChu());
        highlightSelectedMenu("Trang chủ");
    }

    public JPanel taoPanelTrangChu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblAnh = new JLabel(setUpIcon("/img/AnhTrangChu.png", 1200, 550));
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblAnh, BorderLayout.CENTER);

        JPanel pnThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        pnThaoTac.setBackground(Color.WHITE);
        pnThaoTac.add(taoNutIcon("Thêm khách hàng", "/img/addCustomer.png"));
        pnThaoTac.add(taoNutIcon("Thêm hoá đơn", "/img/addOrder.png"));
        pnThaoTac.add(taoNutIcon("Thêm thuốc", "/img/addMedicine.png"));

        JPanel pnBottom = new JPanel(new BorderLayout());
        pnBottom.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Thao tác nhanh:", JLabel.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnBottom.add(lblTitle, BorderLayout.NORTH);
        pnBottom.add(pnThaoTac, BorderLayout.CENTER);
        panel.add(pnBottom, BorderLayout.SOUTH);

        return panel;
    }

    // ================== NÚT ICON ==================
    public JButton taoNutIcon(String text, String iconPath) {
        JButton button = new JButton(text, setUpIcon(iconPath, 24, 24));
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 247, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    // ================== HỖ TRỢ ==================
    public void highlightSelectedMenu(String text) {
        for (Map.Entry<String, JPanel> entry : menuItems.entrySet()) {
            JPanel item = entry.getValue();
            JLabel label = (JLabel) item.getComponent(1);

            if (entry.getKey().equals(text)) {
                selectedMenu = text;
                item.setBackground(Color.decode("#00ADFE"));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("SansSerif", Font.BOLD, 18));
            } else {
                item.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setFont(new Font("SansSerif", Font.PLAIN, 18));
            }
        }
    }
    
    public void setUpNoiDung(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public ImageIcon setUpIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(imgURL);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public JPanel taoPanelTam(String title) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Đây là trang: " + title));
        return panel;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
