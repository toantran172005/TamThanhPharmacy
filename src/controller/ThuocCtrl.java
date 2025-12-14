package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.record.RKRecord;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.List;

import dao.DonViTinhDAO;
import dao.KeThuocDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.KeThuoc;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.ThemThuoc_GUI;
import gui.ThongKeThuoc_GUI;
import gui.TimKiemThuoc_GUI;

public class ThuocCtrl {
	
	private ThuocDAO thuocDAO = new ThuocDAO();
	private KeThuocDAO keThuocDao = new KeThuocDAO();
	private DonViTinhDAO dvtDao = new DonViTinhDAO();
	private ToolCtrl tool = new ToolCtrl();
	private ThongKeThuoc_GUI tkThuoc;
	private TimKiemThuoc_GUI thuoc;
	private ChiTietThuoc_GUI ctThuoc;
	private ThemThuoc_GUI themThuoc;
	
	public ThuocCtrl(ThongKeThuoc_GUI tkThuoc) {
		this.tkThuoc = tkThuoc;
	}
	
	public ThuocCtrl(ChiTietThuoc_GUI ctThuoc) {
		this.ctThuoc = ctThuoc;
	}
	
	public ThuocCtrl(TimKiemThuoc_GUI thuoc) {
		this.thuoc = thuoc;
	}
	
	public ThuocCtrl() {
		super();
		// TODO Auto-generated constructor stub
	}


	//chọn file 
	public File chonFile() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	//set loại thuốc
	public void setKeThuoc() {
		ArrayList<KeThuoc> listKT = keThuocDao.layListKeThuoc();
		thuoc.cmbLoaiThuoc.addItem("Tất cả");
		for(KeThuoc kt : listKT) {
			thuoc.cmbLoaiThuoc.addItem(kt.getLoaiKe());
		}
	}

	//Thống kê thuốc
	public ArrayList<Thuoc> layListThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		ArrayList<Thuoc> listTK = new  ArrayList<Thuoc>();
		try {
			listTK = thuocDAO.layListTHThongKe(ngayBD, ngayKT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listTK;
	}
	
	//xoá thuốc
	public void xoaThuoc() {
		int selectedRow = thuoc.tblThuoc.getSelectedRow();
		if(selectedRow == -1) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 thuốc để xem chi tiết", false);
			return;
		}
		int modelRow = thuoc.tblThuoc.convertRowIndexToModel(selectedRow);
		String maThuoc = thuoc.tblThuoc.getModel().getValueAt(selectedRow, 0).toString();
		
		int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xoá thuốc này?", "Xác nhận!", JOptionPane.YES_NO_OPTION);
		if(confirm == JOptionPane.YES_OPTION) {
			if(thuocDAO.xoaThuoc(maThuoc)) {
				tool.hienThiThongBao("Thông báo", "Xoá thành công!", true);
				thuoc.onBtnLamMoi();
			} else {
				tool.hienThiThongBao("Thông báo", "Xoá thất bại!", false);
			}
		}
		
			
	}
	
	
	//Xem thuốc đã xoá
	public void xemThuocDaXoa() {
		ArrayList<Thuoc> listDaXoa = thuocDAO.layDanhSachDaXoa();
		thuoc.model.setRowCount(0);
		for(Thuoc t : listDaXoa) {
			thuoc.model.addRow(new Object[] {
					t.getMaThuoc(),
					t.getTenThuoc(),
					t.getKeThuoc().getLoaiKe(),
					tool.dinhDangVND(t.getGiaBan()),
					t.getSoLuong(),
					t.getNoiSanXuat(),
					t.getDvt().getTenDVT(),
					t.getHanSuDung()
			});
		}
		
		thuoc.btnXemChiTiet.setVisible(false);
		thuoc.btnLichSuXoa.setVisible(false);
		thuoc.btnXoa.setVisible(false);
		thuoc.btnHoanTac.setVisible(true);
		thuoc.btnQuayLai.setVisible(true);
		
	}
	
	//quay lại trang tìm kiếm
	public void quayLaiTrangTimKiem() {
		setDataChoTable();
		thuoc.btnXemChiTiet.setVisible(true);
		thuoc.btnLichSuXoa.setVisible(true);
		thuoc.btnXoa.setVisible(true);
		thuoc.btnHoanTac.setVisible(false);
		thuoc.btnQuayLai.setVisible(false);
	}
	
	//đổ dữ liệu lên bảng tìm kiếm thuốc
	public void setDataChoTable() {
		ArrayList<Thuoc> listThuoc = thuocDAO.layListThuocHoanChinh();
		thuoc.model.setRowCount(0);
		for(Thuoc t : listThuoc) {
			thuoc.model.addRow(new Object[] {
					t.getMaThuoc(),
					t.getTenThuoc(),
					t.getKeThuoc().getLoaiKe(),
					tool.dinhDangVND(t.getGiaBan()),
					t.getSoLuong(),
					t.getNoiSanXuat(),
					t.getDvt().getTenDVT(),
					t.getHanSuDung()
			});
		}
	}
	
	//xử lý thống kê
	public void onThongKe() {
		 LocalDate bd = tool.utilDateSangLocalDate(tkThuoc.dpNgayBD.getDate());
	     LocalDate kt = tool.utilDateSangLocalDate(tkThuoc.dpNgayKT.getDate());
	     int stt = 1;
	     
	     if(bd == null) {
	    	 bd = LocalDate.of(2025, 01, 01);
	     } 
	     
	     if(kt == null) {
	    	 kt = LocalDate.now();
	     }
	     
	     if(bd == null && kt == null) {
	    	 bd = LocalDate.of(2025, 01, 01);
	    	 kt = LocalDate.now();
	     }
	    
	     
	    ArrayList<Thuoc> listTK = layListThongKe(bd, kt);
	    tkThuoc.pnlChart.repaint();
	    
	    
	    //Đưa dữ liệu cho dataset để vẽ biểu đồ
	    tkThuoc.dataset.clear();
	    for(Thuoc t : listTK) {
	    	tkThuoc.dataset.addValue(t.getSoLuongBan(), "Số lượng bán", t.getTenThuoc());
	    }
	    
	    //Đổ dữ liệu lên bảng
	    for(Thuoc t : listTK) {
	    	tkThuoc.model.addRow(new Object[] {
	    			stt++,
	    			t.getMaThuoc(),
	    			t.getTenThuoc(),
	    			t.getSoLuongBan(),
	    			tool.dinhDangVND(t.getDoanhThu())
	    	});
	    }
	}
	
	//Làm mới thống kê
	public void onLamMoi() {
        tkThuoc.dpNgayBD.setDate(null);
        tkThuoc.dpNgayKT.setDate(null);
        ((DefaultTableModel) tkThuoc.tblThongKe.getModel()).setRowCount(0);
    }
	
	//xử lý xem chi tiết
	public void xemChiTiet() {
		int selectedRow = thuoc.tblThuoc.getSelectedRow();
		if(selectedRow == -1) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 thuốc để xem chi tiết", false);
			return;
		}
		
		int modelRow = thuoc.tblThuoc.convertRowIndexToModel(selectedRow);
		String maThuoc = thuoc.tblThuoc.getModel().getValueAt(selectedRow, 0).toString();
		
		ChiTietThuoc_GUI ctThuoc_GUI = new ChiTietThuoc_GUI(maThuoc);
		tool.doiPanel(thuoc, ctThuoc_GUI);
	}
	
	//Set cmbTenThuoc
	public void setTenThuoc() {
		ArrayList<Thuoc> list = thuocDAO.layListThuocHoanChinh();
		thuoc.cmbTenThuoc.addItem("");
		for(Thuoc t : list) {
			thuoc.cmbTenThuoc.addItem(t.getTenThuoc());
		}
	}
	
	//Lấy danh sách tên thuốc
	public ArrayList<String> layDSTenThuoc(){
		ArrayList<String> listTen = new ArrayList<>();
		ArrayList<Thuoc> list = thuocDAO.layListThuocHoanChinh();
		
		for(Thuoc t : list) {
			listTen.add(t.getTenThuoc());
		}
		
		return listTen;
	}
	
	//Hoàn tác thuốc đã xoá
	public void hoanTacThuoc() {
		int selectedRow = thuoc.tblThuoc.getSelectedRow();
		if(selectedRow == -1) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 thuốc để hoàn tác", false);
			return;
		}
		int modelRow = thuoc.tblThuoc.convertRowIndexToModel(selectedRow);
		String maThuoc = thuoc.tblThuoc.getModel().getValueAt(selectedRow, 0).toString();
		
		int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn hoàn tác thuốc này?", "Xác nhận!", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
	        if (thuocDAO.khoiPhucTH(maThuoc)) {
	            thuoc.model.removeRow(modelRow);
	            tool.hienThiThongBao("Thông báo", "Hoàn tác thành công!", true);
	            if (thuoc.model.getRowCount() == 0) {
	                tool.hienThiThongBao("Thông báo", "Không còn thuốc nào trong lịch sử xoá", true);
	            }
	        } else {
	            tool.hienThiThongBao("Thông báo", "Hoàn tác thất bại!", false);
	        }
	    }
	}
	
	//Gợi ý tên thuốc
//	public void goiYTenThuoc(JComboBox<String> comboBox, ArrayList<String> dsTenThuoc) {
//
//	    comboBox.setEditable(true);
//
//	    JTextField txt = (JTextField) comboBox.getEditor().getEditorComponent();
//	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
//
//	    comboBox.setModel(model);
//
//	    txt.getDocument().addDocumentListener(new DocumentListener() {
//	        @Override
//	        public void insertUpdate(DocumentEvent e) { loc(); }
//
//	        @Override
//	        public void removeUpdate(DocumentEvent e) { loc(); }
//
//	        @Override
//	        public void changedUpdate(DocumentEvent e) {}
//
//	        private void loc() {
//	            SwingUtilities.invokeLater(() -> {
//	                String text = txt.getText().toLowerCase();
//
//	                model.removeAllElements();
//
//	                if (text.isEmpty()) {
//	                    comboBox.hidePopup();
//	                    return;
//	                }
//
//	                for (String ten : dsTenThuoc) {
//	                    if (ten.toLowerCase().startsWith(text)) {
//	                        model.addElement(ten);
//	                    }
//	                }
//
//	                txt.setText(text);
//	                txt.setCaretPosition(text.length());
//
//	                if (model.getSize() > 0) {
//	                    comboBox.showPopup();
//	                } else {
//	                    comboBox.hidePopup();
//	                }
//	            });
//	        }
//	    });
//	}




	//Tìm kiếm theo tên
	public void timKiemThuocTheoTen() {
		
		
	}
	
	//Tìm kiếm theo loại
	public ArrayList<Thuoc> layThuocTheoLoai() {
		ArrayList<Thuoc> listKQ = new ArrayList<Thuoc>();
		ArrayList<Thuoc> list = thuocDAO.layListThuocHoanChinh();
		String loai = thuoc.cmbLoaiThuoc.getSelectedItem().toString();
		
		if(loai == "Tất cả") {
			return list;
		}
		for(Thuoc t : list) {
			if(t.getKeThuoc().getLoaiKe().equalsIgnoreCase(loai)) {
				listKQ.add(t);
			}
		}
		
		return listKQ;
	}
	
	public void timKiemThuocTheoLoai() {
		
		ArrayList<Thuoc> list = layThuocTheoLoai();
		thuoc.model.setRowCount(0);
		for(Thuoc t : list) {
			thuoc.model.addRow(new Object[] {
					t.getMaThuoc(),
					t.getTenThuoc(),
					t.getKeThuoc().getLoaiKe(),
					tool.dinhDangVND(t.getGiaBan()),
					t.getSoLuong(),
					t.getNoiSanXuat(),
					t.getDvt().getTenDVT(),
					t.getHanSuDung()
			});
		}
	}
	
	
	
}
