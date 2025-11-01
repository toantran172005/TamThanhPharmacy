package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.ToolCtrl;
import java.awt.*;

/**
 * Giữ bố cục hai cột song song như bản gốc,
 * nhưng bố trí spacing, alignment đẹp – đều – gọn như ChiTietPhieuKNHT.
 */
public class ChiTietKeThuoc_GUI extends JPanel {

    private JTextField txtMaKe, txtSucChua;
    private JTextArea txaMoTa;
    private JComboBox<String> cmbLoaiKe, cmbTrangThai;
    private JTable tblChiTietKT;
    private JButton btnCapNhat, btnThoat;
    private ToolCtrl tool = new ToolCtrl();

    public ChiTietKeThuoc_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== TOP =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // --- Title ---
        JLabel lblTitle = new JLabel("CHI TIẾT KỆ THUỐC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        top.add(lblTitle, BorderLayout.NORTH);

        // --- GRID FORM (2 cột, đẹp đều) ---
        JPanel grid = new JPanel(new GridLayout(3, 2, 40, 20)); // 3 hàng, 2 cột
        grid.setBackground(Color.WHITE);

        // ==== Dòng 1 ====
        grid.add(taoDong("Mã kệ:", txtMaKe = tool.taoTextField("")));
        grid.add(taoDong("Loại kệ:", cmbLoaiKe = tool.taoComboBox(
                new String[] { "Chọn loại kệ", "Kệ thuốc tây", "Kệ đông y", "Kệ mỹ phẩm" })));

        // ==== Dòng 2 ====
        JScrollPane scrollMoTa = new JScrollPane(txaMoTa = tool.taoTextArea(80));
        scrollMoTa.setPreferredSize(new Dimension(280, 85));
        grid.add(taoDong("Mô tả:", scrollMoTa));
        grid.add(taoDong("Trạng thái:", cmbTrangThai = tool.taoComboBox(
                new String[] { "Hoạt động", "Ngừng hoạt động" })));

        // ==== Dòng 3 ====
        grid.add(taoDong("Sức chứa:", txtSucChua = tool.taoTextField("")));
        grid.add(new JLabel()); // ô trống bên phải

        top.add(grid, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] cols = { "STT", "Mã thuốc", "Tên thuốc", "Trạng thái" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblChiTietKT = new JTable(model);
        tblChiTietKT.setRowHeight(38);
        tblChiTietKT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblChiTietKT.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        tblChiTietKT.setSelectionBackground(new Color(0xE3F2FD));
        tblChiTietKT.setGridColor(new Color(0xDDDDDD));
        tblChiTietKT.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblChiTietKT.getColumnModel().getColumn(0).setPreferredWidth(97);
        tblChiTietKT.getColumnModel().getColumn(1).setPreferredWidth(199);
        tblChiTietKT.getColumnModel().getColumn(2).setPreferredWidth(435);
        tblChiTietKT.getColumnModel().getColumn(3).setPreferredWidth(160);
        

        JScrollPane scrollTable = new JScrollPane(tblChiTietKT);
        scrollTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 30, 10, 30),
                BorderFactory.createLineBorder(new Color(0xCCCCCC))));
        
     // === Make table pure white like top/bottom ===
        tblChiTietKT.setBackground(Color.WHITE);
        tblChiTietKT.setOpaque(true);
        tblChiTietKT.getTableHeader().setBackground(Color.WHITE);
        tblChiTietKT.getTableHeader().setOpaque(true);

        // Cả phần scroll bao quanh table cũng phải trắng
        scrollTable.getViewport().setBackground(Color.WHITE);
        scrollTable.setBackground(Color.WHITE);

        
        add(scrollTable, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 15));
        bottom.setBackground(Color.WHITE);
        btnCapNhat = tool.taoButton("Cập nhật", "/picture/keThuoc/edit.png");
        btnThoat = tool.taoButton("Quay lại", "/picture/trangChu/signOut.png");
        bottom.add(btnCapNhat);
        bottom.add(btnThoat);
        add(bottom, BorderLayout.SOUTH);
    }

    /** 
     * Tạo 1 dòng gồm label và component, spacing đẹp và label tự giãn theo độ dài chữ.
     */
    private JPanel taoDong(String text, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        row.setBackground(Color.WHITE);
        JLabel lbl = tool.taoLabel(text);
        lbl.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        comp.setPreferredSize(new Dimension(250, comp.getPreferredSize().height));
        row.add(lbl);
        row.add(comp);
        return row;
    }
}
