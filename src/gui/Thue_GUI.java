package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import controller.ThueCtrl;
import controller.ToolCtrl;

public class Thue_GUI extends JPanel {

    public JComboBox<String> cmbLoaiThuoc;
    public JTable tblThue;
    public DefaultTableModel model;
    public JButton btnTim, btnLoc, btnLuu;
    public JTextField txtTimKiem;

    public ToolCtrl tool = new ToolCtrl();
    public ThueCtrl thueCtrl;

    public Thue_GUI() {
        this.thueCtrl = new ThueCtrl(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ====== TOP PANEL ======
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30)); 

		// ===== Label tiêu đề =====
        JLabel lblTitle = new JLabel("QUẢN LÝ THUẾ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle);

		// ===== Bộ lọc =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        filterPanel.setBackground(Color.WHITE);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBox.setBackground(Color.WHITE);
        
        txtTimKiem = tool.taoTextField("Tìm theo tên hoặc mã thuốc...");
        txtTimKiem.setPreferredSize(new Dimension(250, 35));   
        
        searchBox.add(txtTimKiem);

        JPanel filterBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterBox.setBackground(Color.WHITE);

        cmbLoaiThuoc = new JComboBox<>();
        cmbLoaiThuoc.addItem("Tất cả loại thuốc"); 
        cmbLoaiThuoc.setPreferredSize(new Dimension(200, 35));

        filterBox.add(cmbLoaiThuoc);

        filterPanel.add(searchBox);
        filterPanel.add(filterBox);
        
        topPanel.add(filterPanel);
        
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(200, 200, 200));
        
        topPanel.add(Box.createVerticalStrut(15)); 
        topPanel.add(sep);
        
        add(topPanel, BorderLayout.NORTH);

        // ====== CENTER: Bảng ======
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15)); // Margin left/right 15

        JLabel lblTableTitle = new JLabel("Danh sách thuế và thuốc được áp dụng");
        lblTableTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblTableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        centerPanel.add(lblTableTitle, BorderLayout.NORTH);

        String[] cols = { "STT", "Tên thuốc", "Loại thuốc", "Thuế suất (%)", "Ghi chú", "Hoạt động" };
        model = new DefaultTableModel(cols, 0);

        tblThue = new JTable(model);
        tblThue.setRowHeight(38); 
        tblThue.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        tblThue.setBackground(Color.WHITE);
        tblThue.setGridColor(new Color(200, 200, 200));
        tblThue.setShowGrid(true);
        tblThue.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        tblThue.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblThue.setForeground(Color.BLACK);

        JTableHeader header = tblThue.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Times New Roman", Font.BOLD, 16)); // Slightly smaller than title
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tblThue.getColumnCount(); i++) {
            tblThue.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(tblThue);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);

        centerPanel.add(scroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ====== BOTTOM PANEL (Save Button) ======
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 100, 15)); // Right aligned, 100px padding right
        bottomPanel.setBackground(Color.WHITE);

        btnLuu = tool.taoButton("Lưu", "/picture/thuoc/diskette.png");
        btnLuu.setPreferredSize(new Dimension(100, 40)); // Slightly larger button
        
        bottomPanel.add(btnLuu);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Event Handlers =====
        ganSuKien();
    }

    public void ganSuKien() {
        // thueCtrl.loadData(); // Example
        // btnTim.addActionListener(e -> thueCtrl.timKiem());
        // btnLoc.addActionListener(e -> thueCtrl.locTheoLoai());
        // btnLuu.addActionListener(e -> thueCtrl.luuThue());
    }
}