package gui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import controller.ToolCtrl;

public class TrangChuQL_GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    public ToolCtrl tool = new ToolCtrl();
    private JPanel leftPanel, topPanel, contentPanel;
    private JLabel lblTenHieuThuoc, lblTenNV, lblChucVu;
    private JLabel imgTaiKhoan, imgDangXuat, imgLogo;
    private Map<String, JPanel> menuItems = new HashMap<>();
    private Map<String, String> iconPaths = new HashMap<>();
    private Map<String, JPanel> panelMapping = new HashMap<>();
    private String selectedMenu = "";
    Font font1 = new Font("Arial", Font.BOLD, 18);
    Font font2 = new Font("Arial", Font.PLAIN, 15);

    public TrangChuQL_GUI() {
        setTitle("Quản lý hiệu thuốc Tam Thanh");
        setSize(1500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // ========== THANH TRÊN ==========
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        // LEFT SIDE
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoPanel.setBackground(Color.WHITE);
        imgLogo = new JLabel(setUpIcon("/picture/trangChu/logo.jpg", 40, 40));
        lblTenHieuThuoc = new JLabel("NHÀ THUỐC TAM THANH");
        lblTenHieuThuoc.setFont(font1);
        logoPanel.add(imgLogo);
        logoPanel.add(lblTenHieuThuoc);
        topPanel.add(logoPanel, BorderLayout.WEST);

        // RIGHT SIDE
        JPanel nvPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        nvPanel.setBackground(Color.WHITE);
        lblTenNV = new JLabel("Trần Thanh Toàn");
        lblTenNV.setFont(font2);
        lblChucVu = new JLabel("Nhân viên bán hàng");
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(lblTenNV);
        namePanel.add(lblChucVu);
        imgTaiKhoan = new JLabel(setUpIcon("/picture/trangChu/user.png", 20, 20));
        imgDangXuat = new JLabel(setUpIcon("/picture/trangChu/signOut.png", 20, 20));
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

        // ========== THANH MENU TRÁI ==========
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 247, 250));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(leftPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.WEST);

        // ========== KHU VỰC CHÍNH ==========
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // ========== THÊM MENU CHÍNH ==========
        thietLapMenu("Trang chủ", "/picture/trangChu/dashboard.png", taoPanelTrangChu());
        thietLapMenu("Thuốc", "/picture/trangChu/addMedicine.png", taoPanelTam("Thuốc"));
        thietLapMenu("Kệ Thuốc", "/picture/trangChu/shelf.png", taoPanelTam("Kệ Thuốc"));
        thietLapMenu("Khách Hàng", "/picture/trangChu/customer.png", taoPanelTam("Khách Hàng"));
        thietLapMenu("Hóa Đơn", "/picture/trangChu/order.png", taoPanelTam("Hóa Đơn"));
        thietLapMenu("Nhân Viên", "/picture/trangChu/nhanVien.png", taoPanelTam("Nhân Viên"));
        thietLapMenu("Khuyến Mãi", "/picture/trangChu/voucher.png", taoPanelTam("Khuyến Mãi"));

        setThuocTinhMainMenu();
        hienThiTrangChu();
        taoMappingPanel();

        setVisible(true);
    }

    /** Ánh xạ tên menu hoặc menu con sang panel tương ứng */
    private void taoMappingPanel() {
        // Menu chính
        panelMapping.put("Trang chủ", taoPanelTrangChu());
        panelMapping.put("Thuốc", taoPanelTam("Thuốc"));
        panelMapping.put("Kệ Thuốc", taoPanelTam("Kệ Thuốc"));
        panelMapping.put("Khách Hàng", taoPanelTam("Khách Hàng"));
        panelMapping.put("Hóa Đơn", taoPanelTam("Hóa Đơn"));
        panelMapping.put("Nhân Viên", taoPanelTam("Nhân Viên"));
        panelMapping.put("Khuyến Mãi", taoPanelTam("Khuyến Mãi"));

        // Menu con
        panelMapping.put("Tìm kiếm thuốc", new TimKiemThuoc_GUI());
        panelMapping.put("Thêm thuốc", new ThemThuoc_GUI());
        panelMapping.put("Nhập thuốc", new NhapThuoc_GUI());
        panelMapping.put("Đặt thuốc bán", new DatThuoc_GUI());
        panelMapping.put("Thống kê thuốc", new ThongKeThuoc_GUI());
        panelMapping.put("Đơn vị", new DonVi_GUI());
        panelMapping.put("Danh sách kệ", new DanhSachKeThuoc_GUI());
        panelMapping.put("Thêm kệ thuốc", taoPanelTam("Thêm kệ thuốc"));
        panelMapping.put("Tìm kiếm khách hàng", new TimKiemKH_GUI());
        panelMapping.put("Thêm khách hàng", new ThemKhachHang_GUI());
        panelMapping.put("Khiếu nại & Hỗ trợ", new DanhSachKhieuNaiVaHoTroHK_GUI());
        panelMapping.put("Thống kê khách hàng", new ThongKeKhachHang_GUI());
        panelMapping.put("Tìm kiếm hóa đơn", new TimKiemHD_GUI(this));
        panelMapping.put("Danh sách phiếu đặt thuốc", new TimKiemPhieuDatHang_GUI(this));
        panelMapping.put("Lập hóa đơn", new LapHoaDon_GUI());
        panelMapping.put("Đặt thuốc", new LapPhieuDatHang_GUI());
        panelMapping.put("Danh sách phiếu đổi trả", new TimKiemPhieuDoiTra_GUI(this));
        panelMapping.put("Thống kê hóa đơn", new ThongKeHoaDon_GUI());
        panelMapping.put("Tìm kiếm nhân viên", new TimKiemNV_GUI(this));
        panelMapping.put("Thêm nhân viên", new ThemNhanVien_GUI());
        panelMapping.put("Danh sách khuyến mãi", new DanhSachKhuyenMai_GUI());
        panelMapping.put("Thêm Khuyến Mãi", new ThemKhuyenMai_GUI());
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
        label.setFont(font2);
        item.add(icon, gbc);
        gbc.gridx = 1;
        item.add(label, gbc);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                highlightSelectedMenu(text);

                // CHỈ "TRANG CHỦ" MỚI ĐỔI NỘI DUNG
                if ("Trang chủ".equals(text)) {
                    JPanel p = panelMapping.getOrDefault(text, taoPanelTam(text));
                    setUpNoiDung(p);
                }
                // Các menu khác → chỉ mở submenu, không đổi nội dung
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

    // ================== MENU CON (CĂN GIỮA DỌC, CĂN TRÁI NGANG) ==================
    public void setMauClickVaMenuCon(JPanel menuChinh, List<String> menuCons) {
        if (menuChinh == null) return;

        JPanel container = (JPanel) menuChinh.getParent();
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setBackground(new Color(245, 247, 250));
        subMenuPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        subMenuPanel.setVisible(false);

        for (String sub : menuCons) {
            JPanel item = new JPanel(new GridBagLayout());
            item.setPreferredSize(new Dimension(220, 40));
            item.setMaximumSize(new Dimension(220, 40));
            item.setBackground(Color.WHITE);
            item.setOpaque(true);

            // Shadow + padding
            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
            ));

            // CĂN GIỮA DỌC, CĂN TRÁI NGANG
            GridBagConstraints gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = 0;
            gbcLabel.weightx = 1.0;
            gbcLabel.weighty = 1.0;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(0, 0, 0, 0);

            JLabel lbl = new JLabel(sub);
            lbl.setFont(font2);
            lbl.setForeground(Color.BLACK);
            item.add(lbl, gbcLabel);

            // === HOVER + SELECTED ===
            item.addMouseListener(new MouseAdapter() {
                private boolean isSelected = false;

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isSelected) {
                        lbl.setForeground(new Color(0, 120, 215));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isSelected) {
                        lbl.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // Reset tất cả
                    for (Component comp : subMenuPanel.getComponents()) {
                        if (comp instanceof JPanel) {
                            JPanel p = (JPanel) comp;
                            JLabel l = (JLabel) p.getComponent(0);
                            l.setForeground(Color.BLACK);
                            l.setFont(font2);
                        }
                    }

                    // Chọn hiện tại
                    isSelected = true;
                    lbl.setForeground(Color.decode("#00ADFE"));
                    lbl.setFont(font2.deriveFont(Font.BOLD));

                    JPanel p = panelMapping.getOrDefault(sub, taoPanelTam(sub));
                    setUpNoiDung(p);
                    selectedMenu = sub;

                    // Tự động mở submenu khi chọn menu con
                    subMenuPanel.setVisible(true);
                    leftPanel.revalidate();
                    leftPanel.repaint();
                }
            });

            subMenuPanel.add(item);
        }

        container.add(subMenuPanel);

        // Mở/đóng submenu khi click menu chính
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

    // ================== MENU CHÍNH & MENU CON ==================
    public void setThuocTinhMainMenu() {
        setMauClickVaMenuCon(menuItems.get("Trang chủ"), List.of());
        setMauClickVaMenuCon(menuItems.get("Thuốc"),
                List.of("Tìm kiếm thuốc", "Thêm thuốc", "Nhập thuốc", "Đặt thuốc bán", "Thống kê thuốc", "Đơn vị"));
        setMauClickVaMenuCon(menuItems.get("Kệ Thuốc"), List.of("Danh sách kệ", "Thêm kệ thuốc"));
        setMauClickVaMenuCon(menuItems.get("Khách Hàng"),
                List.of("Tìm kiếm khách hàng", "Thêm khách hàng", "Khiếu nại & Hỗ trợ", "Thống kê khách hàng"));
        setMauClickVaMenuCon(menuItems.get("Hóa Đơn"), List.of("Tìm kiếm hóa đơn", "Danh sách phiếu đặt thuốc",
                "Lập hóa đơn", "Đặt thuốc", "Danh sách phiếu đổi trả", "Thống kê hóa đơn"));
        setMauClickVaMenuCon(menuItems.get("Nhân Viên"), List.of("Tìm kiếm nhân viên", "Thêm nhân viên"));
        setMauClickVaMenuCon(menuItems.get("Khuyến Mãi"), List.of("Danh sách khuyến mãi", "Thêm Khuyến Mãi"));
    }

    // ================== HỖ TRỢ ==================
    public void highlightSelectedMenu(String text) {
        // Reset menu chính
        for (Map.Entry<String, JPanel> entry : menuItems.entrySet()) {
            JPanel item = entry.getValue();
            JLabel label = (JLabel) item.getComponent(1);
            if (entry.getKey().equals(text)) {
                selectedMenu = text;
                item.setBackground(Color.decode("#00ADFE"));
                label.setForeground(Color.WHITE);
            } else {
                item.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }
        }

        // Nếu là menu con → không cần reset menu chính
        if (menuItems.containsKey(text)) return;

        // Reset tất cả menu con
        Component[] containers = leftPanel.getComponents();
        for (Component c : containers) {
            if (c instanceof JPanel) {
                JPanel container = (JPanel) c;
                if (container.getComponentCount() > 1) {
                    JPanel subMenu = (JPanel) container.getComponent(1);
                    for (Component sub : subMenu.getComponents()) {
                        if (sub instanceof JPanel) {
                            JPanel item = (JPanel) sub;
                            JLabel lbl = (JLabel) item.getComponent(0);
                            lbl.setForeground(Color.BLACK);
                            lbl.setFont(font2);
                        }
                    }
                }
            }
        }
    }

    public void setUpNoiDung(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private ImageIcon setUpIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(imgURL);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void hienThiTrangChu() {
        setUpNoiDung(taoPanelTrangChu());
        highlightSelectedMenu("Trang chủ");
    }

    public JPanel taoPanelTam(String title) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Đây là trang: " + title));
        return panel;
    }

    public JPanel taoPanelTrangChu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lblAnh = new JLabel(setUpIcon("/picture/trangChu/AnhTrangChu.png", 1200, 550));
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblAnh, BorderLayout.CENTER);

        JButton btnThemKH = tool.taoButton("Thêm khách hàng", "/picture/trangChu/addCustomer.png");
        JButton btnThemHD = tool.taoButton("Thêm hoá đơn", "/picture/trangChu/addOrder.png");
        JButton btnThemThuoc = tool.taoButton("Thêm thuốc", "/picture/trangChu/addMedicine.png");
        JPanel pnThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        pnThaoTac.setBackground(Color.WHITE);
        pnThaoTac.add(btnThemKH);
        pnThaoTac.add(btnThemHD);
        pnThaoTac.add(btnThemThuoc);

        JPanel pnBottom = new JPanel(new BorderLayout());
        pnBottom.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Thao tác nhanh:", JLabel.CENTER);
        lblTitle.setFont(font2);
        lblTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
        pnBottom.add(lblTitle, BorderLayout.NORTH);
        pnBottom.add(pnThaoTac, BorderLayout.CENTER);
        panel.add(pnBottom, BorderLayout.SOUTH);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new TrangChuQL_GUI().setVisible(true);
        });
    }
}