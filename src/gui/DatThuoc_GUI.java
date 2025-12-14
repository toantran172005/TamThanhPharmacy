package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.awt.*;

import controller.DatThuocBanCtrl;
import controller.ToolCtrl;

public class DatThuoc_GUI extends JPanel {

	public JComboBox<String> cmbThuoc;
	public JTextField txtSoLuongDat;
	public JButton btnTru, btnCong, btnThem, btnLamMoi, btnTaoPhieuDat, btnXoa;
	public JTable tblDatThuoc;
	public DefaultTableModel model;
	
	public DatThuocBanCtrl dtCtrl;
	public ToolCtrl tool = new ToolCtrl();

	public DatThuoc_GUI() {
		dtCtrl = new DatThuocBanCtrl(this);
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ====== TOP ======
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

		JLabel lblTitle = new JLabel("ĐẶT THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(15));

		// ====== Hàng nhập liệu ======
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
		row1.setBackground(Color.WHITE);

		// Cột 1: Thuốc
		JPanel colThuoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		colThuoc.setBackground(Color.WHITE);
		JLabel lblThuoc = tool.taoLabel("Thuốc:");
		cmbThuoc = new JComboBox<>();
		cmbThuoc.setEditable(true);
		cmbThuoc.setPreferredSize(new Dimension(180, 35));
		colThuoc.add(lblThuoc);
		colThuoc.add(cmbThuoc);

		// Cột 2: Số lượng đặt
		JPanel colSL = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		colSL.setBackground(Color.WHITE);
		JLabel lblSL = tool.taoLabel("Số lượng đặt:");

		btnTru = tool.taoButton(null,"/picture/hoaDon/minus-sign.png" );
		btnCong = tool.taoButton(null, "/picture/hoaDon/plus.png");

		txtSoLuongDat = new JTextField("1", 4);
		txtSoLuongDat.setHorizontalAlignment(SwingConstants.CENTER);
		txtSoLuongDat.setFont(new Font("Arial", Font.PLAIN, 15));
		txtSoLuongDat.setPreferredSize(new Dimension(60, 35));

		colSL.add(lblSL);
		colSL.add(btnTru);
		colSL.add(txtSoLuongDat);
		colSL.add(btnCong);

		// Cột 3: Buttons
		btnThem = tool.taoButton("Thêm", "/picture/khuyenMai/plus.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
		btnXoa = tool.taoButton("Xoá", "/picture/khachHang/trash.png");

		row1.add(colThuoc);
		row1.add(colSL);
		row1.add(btnThem);
		row1.add(btnLamMoi);
		row1.add(btnXoa);

		topPanel.add(row1);
		topPanel.add(Box.createVerticalStrut(10));
		topPanel.add(new JSeparator());

		add(topPanel, BorderLayout.NORTH);

		// ====== CENTER ======
		String[] cols = { "STT", "Mã thuốc", "Tên thuốc", "Đơn vị", "Số lượng đặt" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblDatThuoc = new JTable(model);
		tblDatThuoc.setRowHeight(38);
		tblDatThuoc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblDatThuoc.setSelectionBackground(new Color(0xE3F2FD));
		tblDatThuoc.setSelectionForeground(Color.BLACK);
		tblDatThuoc.setGridColor(new Color(0xDDDDDD));
		tblDatThuoc.setBackground(Color.WHITE);
		tblDatThuoc.setForeground(new Color(0x33, 0x33, 0x33));

		JTableHeader header = tblDatThuoc.getTableHeader();
		header.setFont(new Font("Times New Roman", Font.BOLD, 14));
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(0x33, 0x33, 0x33));
		header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

		//Căn giữa cho dữ liệu trong cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = tblDatThuoc.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        //Căn giữa cho tiêu đề table
        ((DefaultTableCellRenderer) tblDatThuoc.getTableHeader().getDefaultRenderer())
        .setHorizontalAlignment(SwingConstants.CENTER);
        
		JScrollPane scroll = new JScrollPane(tblDatThuoc);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scroll.setBackground(Color.WHITE);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		add(scroll, BorderLayout.CENTER);

		// ====== BOTTOM ======
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 15));
		bottom.setBackground(Color.WHITE);
		btnTaoPhieuDat = tool.taoButton("Tạo phiếu đặt", "/picture/keThuoc/edit.png");
		bottom.add(btnTaoPhieuDat);
		add(bottom, BorderLayout.SOUTH);

		// ====== EVENT DUMMY ======
		ganSuKien();
	}
	
	public void ganSuKien() {
		dtCtrl.setDataChoComboBox();
		btnLamMoi.addActionListener(e -> dtCtrl.lamMoi());
		btnCong.addActionListener(e -> {
			int sl = Integer.parseInt(txtSoLuongDat.getText().trim());
			txtSoLuongDat.setText(String.valueOf(sl + 1));
		});
		btnTru.addActionListener(e -> {
			int sl = Integer.parseInt(txtSoLuongDat.getText().trim());
			if (sl > 1)
				txtSoLuongDat.setText(String.valueOf(sl - 1));
		});
		
		btnTaoPhieuDat.addActionListener(e -> {
			tool.doiPanel(this, new PhieuDatThuoc_GUI(this));
		});
		
		btnThem.addActionListener(e -> dtCtrl.addThuocVaoTable());
		
		btnXoa.addActionListener(e -> dtCtrl.xoaThuocKhoiBang());
	}

}
