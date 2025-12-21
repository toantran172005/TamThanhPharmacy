package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.KhuyenMaiDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.Thuoc;
import gui.ChiTietKhuyenMai_GUI;
import gui.DanhSachKhuyenMai_GUI;
import gui.ThemKhuyenMai_GUI;

public class KhuyenMaiCtrl {

	public boolean dangSetTenKM = false;
	public static String maKMHienTai;
	public DanhSachKhuyenMai_GUI kmGUI;
	public ChiTietKhuyenMai_GUI ctGUI;
	public ThemKhuyenMai_GUI themKmGUI;
	public KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
	public ThuocDAO thuocDao = new ThuocDAO();
	public ToolCtrl tool = new ToolCtrl();

	ArrayList<KhuyenMai> listKM = new ArrayList<KhuyenMai>();
	private ArrayList<Thuoc> listThuocTam;
	
	public KhuyenMaiCtrl(DanhSachKhuyenMai_GUI danhSachKhuyenMai_GUI) {
		this.kmGUI = danhSachKhuyenMai_GUI;
	}

	public KhuyenMaiCtrl(ChiTietKhuyenMai_GUI chiTietKhuyenMai_GUI) {
		this.ctGUI = chiTietKhuyenMai_GUI;
	}

	public KhuyenMaiCtrl(ThemKhuyenMai_GUI themKhuyenMai_GUI) {
		this.themKmGUI = themKhuyenMai_GUI;
	}

	//Đổ dữ liệu lên bảng
	public void setDataChoTable() {
		kmDAO.capNhatTrangThaiHetHan();
		listKM = kmDAO.layDanhSachKM();
		DefaultTableModel model = (DefaultTableModel) kmGUI.tblKhuyenMai.getModel();
		model.setRowCount(0);
		for(KhuyenMai km : listKM) {
			model.addRow(new Object[] {
					km.getMaKM(),
					km.getTenKM(),
					km.getLoaiKM(),
					km.getMucKhuyenMai(),
					tool.dinhDangLocalDate(km.getNgayBD()),
					tool.dinhDangLocalDate(km.getNgayKT()),
					km.getTrangThaiHD()
			});
			
		}
	}

	//Cập nhật bảng
	public void capNhatLaiTable() {
	    if (this.kmGUI != null) {
	        DefaultTableModel model = (DefaultTableModel) kmGUI.tblKhuyenMai.getModel();
	        model.setRowCount(0);
	        setDataChoTable(); 
	    }
	}
	
	//Xem chi tiết KM
	public void xemChiTietKM(JPanel parentPanel) {
	    int selectedRow = kmGUI.tblKhuyenMai.getSelectedRow();
	    if(selectedRow == -1) {
	        tool.hienThiThongBao("Lỗi!", "Vui lòng chọn 1 khuyến mãi để xem chi tiết", false);
	        return;
	    } 

	    this.maKMHienTai = kmGUI.tblKhuyenMai.getValueAt(selectedRow, 0).toString();

	    KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(KhuyenMaiCtrl.maKMHienTai);
	    if(km == null) return;
	    ArrayList<Object[]> listThuoc = kmDAO.layDanhSachChiTiet(KhuyenMaiCtrl.maKMHienTai);
	    ChiTietKhuyenMai_GUI ctGUI = new ChiTietKhuyenMai_GUI();
	    
	    ctGUI.txtTenKM.setText(km.getTenKM());
	    ctGUI.cmbLoaiKM.setSelectedItem(km.getLoaiKM());
	    ctGUI.txtMucKM.setText(String.valueOf(km.getMucKM()));
	    ctGUI.txtSoLuongMua.setText(String.valueOf(km.getSoLuongMua()));
	    ctGUI.txtSoLuongTang.setText(String.valueOf(km.getSoLuongTang()));
	    ctGUI.dpNgayBD.setDate(tool.localDateSangUtilDate(km.getNgayBD()));
	    ctGUI.dpNgayKT.setDate(tool.localDateSangUtilDate(km.getNgayKT()));

	    // Đổ dữ liệu vào bảng
	    DefaultTableModel model = (DefaultTableModel) ctGUI.tblChiTietKM.getModel();
	    model.setRowCount(0); 
	    for(Object[] rowThuoc : listThuoc) {
	        model.addRow(new Object[] {              
	            rowThuoc[4],         
	            rowThuoc[5],         
	            km.getLoaiKM(),      
	            km.getMucKM() > 0 ? km.getMucKM() + "%" : "Mua " + km.getSoLuongMua() + " tặng " + km.getSoLuongTang(),
	            "Đang áp dụng"        
	        });
	    }
	    
	    tool.doiPanel(parentPanel, ctGUI); 
	}
	
	//Lọc theo trạng thái
	public void locKMTheoTrangThai() {
		String trangThai = kmGUI.cmbTrangThai.getSelectedItem().toString();
		ArrayList<KhuyenMai> listKM = kmDAO.layDanhSachKM();
		ArrayList<KhuyenMai> listLoc = new ArrayList<KhuyenMai>();
		
		if(trangThai.equalsIgnoreCase("Tất cả")) {
			listLoc = listKM;
		} else {
			for(KhuyenMai km : listKM) {
				if(km.getTrangThaiHD().equalsIgnoreCase(trangThai)) {
					listLoc.add(km);
				}
			}
		}
		
		DefaultTableModel model = (DefaultTableModel) kmGUI.tblKhuyenMai.getModel();
	    model.setRowCount(0); 
	    for(KhuyenMai km : listLoc) {
	    	model.addRow(new Object[]{
    			km.getMaKM(),
				km.getTenKM(),
				km.getLoaiKM(),
				km.getMucKhuyenMai(),
				km.getNgayBD(),
				km.getNgayKT(),
				km.getTrangThaiHD()
	    	});
	    }
		
	}
	
	//Lọc KM theo ngày
	public void locKMTheoNgay() {
	    java.util.Date utilDate = kmGUI.dpNgay.getDate();
	    if (utilDate == null) return;
	    LocalDate ngayChon = tool.utilDateSangLocalDate(utilDate);
	    if (listKM == null || listKM.isEmpty()) {
	        listKM = kmDAO.layDanhSachKM();
	    }
	    DefaultTableModel model = (DefaultTableModel) kmGUI.tblKhuyenMai.getModel();
	    model.setRowCount(0);

	    for (KhuyenMai km : listKM) {
	        boolean batDau = ngayChon.isEqual(km.getNgayBD()) || ngayChon.isAfter(km.getNgayBD());
	        boolean ketThuc = ngayChon.isEqual(km.getNgayKT()) || ngayChon.isBefore(km.getNgayKT());

	        if (batDau && ketThuc) {
	            model.addRow(new Object[] {
	                km.getMaKM(),
	                km.getTenKM(),
	                km.getLoaiKM(),
	                km.getMucKhuyenMai(),
	                km.getNgayBD(),
	                km.getNgayKT(),
	                km.getTrangThaiHD()
	            });
	        }
	    }
	}
	
	//Lọc theo tên
	public void timKiemNhanhTheoTen() {
	    String tuKhoa = kmGUI.txtTenKM.getText().trim().toLowerCase();

	    if (listKM == null || listKM.isEmpty()) {
	        listKM = kmDAO.layDanhSachKM();
	    }

	    DefaultTableModel model = (DefaultTableModel) kmGUI.tblKhuyenMai.getModel();
	    model.setRowCount(0);
	    for (KhuyenMai km : listKM) {
	        if (km.getTenKM().toLowerCase().contains(tuKhoa)) {
	            model.addRow(new Object[] {
	                km.getMaKM(),
	                km.getTenKM(),
	                km.getLoaiKM(),
	                km.getMucKhuyenMai(),
	                km.getNgayBD(),
	                km.getNgayKT(),
	                km.getTrangThaiHD()
	            });
	        }
	    }
	}
	
	//thiết lập trạng thái sửa
	public void thietLapTrangThaiSua(boolean status) {
	    ctGUI.txtTenKM.setEditable(status);
	    ctGUI.cmbLoaiKM.setEnabled(status);
	    ctGUI.dpNgayBD.setEnabled(status);
	    ctGUI.dpNgayKT.setEnabled(status);
	    ctGUI.cmbThemThuoc.setEnabled(status);
	    ctGUI.btnThemThuoc.setEnabled(status);
	    ctGUI.btnXoaThuoc.setEnabled(status);
	    
	    ctGUI.setTrangThaiCacNutBam(status);

	    if (status) {
	        ctGUI.btnCapNhat.setText("Lưu thay đổi");
	    } else {
	        ctGUI.btnCapNhat.setText("Cập nhật");
	    }
	}
	
	public void setDuLieuChoCmbThuoc(JComboBox<String> cmb) {
        cmb.removeAllItems();
        if (listThuocTam == null || listThuocTam.isEmpty()) {
            listThuocTam = thuocDao.layListThuocHoanChinh();
        }
        cmb.addItem("");
        for(Thuoc t : listThuocTam) {
            cmb.addItem(t.getTenThuoc());
        }
        caiDatGoiYThuoc(cmb);
    }

	//lưu cập nhật
	public void luuCapNhat() {
	    if (KhuyenMaiCtrl.maKMHienTai == null) {
	        tool.hienThiThongBao("Lỗi", "Không tìm thấy mã khuyến mãi để cập nhật!", false);
	        return;
	    }

	    if (ctGUI.dpNgayBD.getDate() == null || ctGUI.dpNgayKT.getDate() == null) {
	        tool.hienThiThongBao("Lỗi nhập liệu", "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!", false);
	        return;
	    }
	    // -----------------------------------------------

	    try {
	        String tenMoi = ctGUI.txtTenKM.getText().trim();
	        String loaiMoi = ctGUI.cmbLoaiKM.getSelectedItem().toString();

	        if (tenMoi.isEmpty()) {
	            tool.hienThiThongBao("Lỗi nhập liệu", "Tên khuyến mãi không được để trống", false);
	            return;
	        }

	        int mucMoi = 0;
	        int slMua = 0;
	        int slTang = 0;

	        if (loaiMoi.equalsIgnoreCase("Giảm giá")) {
	            try {
	                mucMoi = Integer.parseInt(ctGUI.txtMucKM.getText().trim());
	                slMua = 0; slTang = 0;
	            } catch (Exception e) {
	                mucMoi = 0;
	            }
	        } else {
	            try {
	                slMua = Integer.parseInt(ctGUI.txtSoLuongMua.getText().trim());
	                slTang = Integer.parseInt(ctGUI.txtSoLuongTang.getText().trim());
	                mucMoi = 0;
	            } catch (Exception e) {
	                slMua = 0; slTang = 0;
	            }
	        }

	        LocalDate ngayBDMoi = tool.utilDateSangLocalDate(ctGUI.dpNgayBD.getDate());
	        LocalDate ngayKTMoi = tool.utilDateSangLocalDate(ctGUI.dpNgayKT.getDate());
	        
	        if (ngayKTMoi.isBefore(ngayBDMoi)) {
	             tool.hienThiThongBao("Lỗi ngày", "Ngày kết thúc phải sau ngày bắt đầu!", false);
	             return;
	        }

	        KhuyenMai km = new KhuyenMai();
	        km.setMaKM(KhuyenMaiCtrl.maKMHienTai);
	        km.setTenKM(tenMoi);
	        km.setLoaiKM(loaiMoi);
	        km.setMucKM(mucMoi);
	        km.setSoLuongMua(slMua);
	        km.setSoLuongTang(slTang);
	        km.setNgayBD(ngayBDMoi);
	        km.setNgayKT(ngayKTMoi);
	        km.setTrangThai(true);

	        List<String> listMaThuoc = new ArrayList<>();
	        DefaultTableModel model = (DefaultTableModel) ctGUI.tblChiTietKM.getModel();
	        int rowCount = model.getRowCount();
	        for (int i = 0; i < rowCount; i++) {
	            String maThuoc = model.getValueAt(i, 0).toString();
	            listMaThuoc.add(maThuoc);
	        }

	        boolean check = kmDAO.capNhatKhuyenMai(km, listMaThuoc);

	        if (check) {
	            tool.hienThiThongBao("Thông báo", "Cập nhật khuyến mãi thành công!", true);
	            thietLapTrangThaiSua(false);
	        } else {
	            tool.hienThiThongBao("Lỗi", "Cập nhật thất bại! Vui lòng kiểm tra lại dữ liệu.", false);
	        }

	    } catch (NumberFormatException e) {
	        tool.hienThiThongBao("Lỗi định dạng", "Vui lòng nhập đúng định dạng số!", false);
	    } catch (Exception e) {
	        e.printStackTrace();
	        tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), false);
	    }
	}
	
	//Thêm thuốc vào bảng cho giao diện chi tiết
	public void themThuocVaoBang() {
		Object selectedItem = ctGUI.cmbThemThuoc.getSelectedItem();
		if (selectedItem == null) return;
		String tenThuoc = selectedItem.toString();
		DefaultTableModel model = (DefaultTableModel) ctGUI.tblChiTietKM.getModel();
		
		for (int i = 0; i < model.getRowCount(); i++) {
			String tenThuocTrongBang = model.getValueAt(i, 1).toString(); // Cột 1 là Tên thuốc
			if (tenThuoc.equals(tenThuocTrongBang)) {
				tool.hienThiThongBao("Trùng lặp", "Thuốc " + tenThuoc + " đã có trong danh sách áp dụng!", false);
				return;
			}
		}

		String maThuoc = thuocDao.layMaThuocTheoTen(tenThuoc);
		
		if (maThuoc == null) {
			tool.hienThiThongBao("Lỗi dữ liệu", "Không tìm thấy mã thuốc tương ứng trong CSDL!", false);
			return;
		}

		String loaiKM = ctGUI.cmbLoaiKM.getSelectedItem().toString();
		String hienThiMucKM = "";
		
		try {
			if (loaiKM.equalsIgnoreCase("Giảm giá")) {
				String val = ctGUI.txtMucKM.getText().trim();
				if (val.isEmpty() || Double.parseDouble(val) <= 0 || Double.parseDouble(val) > 100) {
					tool.hienThiThongBao("Lỗi nhập liệu", "Vui lòng nhập mức giảm giá hợp lệ (1-100%)!", false);
					ctGUI.txtMucKM.requestFocus();
					return;
				}
				hienThiMucKM = val + "%";
			} else {
				String muaStr = ctGUI.txtSoLuongMua.getText().trim();
				String tangStr = ctGUI.txtSoLuongTang.getText().trim();
				
				int mua = Integer.parseInt(muaStr);
				int tang = Integer.parseInt(tangStr);
				
				if (mua <= 0 || tang <= 0) {
					tool.hienThiThongBao("Lỗi nhập liệu", "Số lượng mua và tặng phải lớn hơn 0!", false);
					return;
				}
				hienThiMucKM = "Mua " + mua + " tặng " + tang;
			}
		} catch (NumberFormatException e) {
			tool.hienThiThongBao("Lỗi định dạng", "Vui lòng nhập đúng định dạng số!", false);
			return;
		}
		model.addRow(new Object[] {
			maThuoc,           
			tenThuoc,          
			loaiKM,            
			hienThiMucKM,      
			"Đang áp dụng"
		});
		ctGUI.cmbThemThuoc.setSelectedItem("");
	}
	
	//Thêm thuốc vào bảng cho giao diện thêm khuyến mãi
	public void themThuocVaoBangThemKM() {
	    Object selectedItem = themKmGUI.cmbThemThuoc.getSelectedItem();
	    if (selectedItem == null || selectedItem.toString().trim().isEmpty()) {
	        tool.hienThiThongBao("Cảnh báo", "Vui lòng chọn thuốc cần thêm!", false);
	        return;
	    }
	    String tenThuoc = selectedItem.toString();

	    DefaultTableModel model = (DefaultTableModel) themKmGUI.tblThuocKhuyenMai.getModel();

	    for (int i = 0; i < model.getRowCount(); i++) {
	        Object val = model.getValueAt(i, 1); 
	        if (val != null && tenThuoc.equals(val.toString())) {
	            tool.hienThiThongBao("Trùng lặp", "Thuốc này đã được thêm vào danh sách!", false);
	            return;
	        }
	    }

	    String maThuoc = thuocDao.layMaThuocTheoTen(tenThuoc);
	    Thuoc t = thuocDao.timThuocTheoMa(maThuoc);

	    if (t == null) {
	        tool.hienThiThongBao("Lỗi", "Không tìm thấy thông tin thuốc!", false);
	        return;
	    }
	    
	    String loaiKe = (t.getKeThuoc() != null && t.getKeThuoc().getLoaiKe() != null) 
	                    ? t.getKeThuoc().getLoaiKe() : "";
	                    
	    String tenDVT = (t.getDvt() != null && t.getDvt().getTenDVT() != null) 
	                    ? t.getDvt().getTenDVT() : "";

	    model.addRow(new Object[] {           
	        t.getMaThuoc(),    
	        t.getTenThuoc(),   
	        loaiKe,            
	        tenDVT,            
	        t.getGiaBan()      
	    });
	    themKmGUI.cmbThemThuoc.setSelectedItem("");
	}
	
	// Thêm khuyến mãi mới
	public void themKhuyenMai() {
		try {
			String tenKM = themKmGUI.txtTenKM.getText().trim();
			String loaiKM = themKmGUI.cmbPhuongThuc.getSelectedItem().toString();
			java.util.Date dateBD = themKmGUI.dpNgayBD.getDate();
			java.util.Date dateKT = themKmGUI.dpNgayKT.getDate();

			if (tenKM.isEmpty()) {
				tool.hienThiThongBao("Lỗi nhập liệu", "Vui lòng nhập tên khuyến mãi!", false);
				themKmGUI.txtTenKM.requestFocus();
				return;
			}
			if (dateBD == null || dateKT == null) {
				tool.hienThiThongBao("Lỗi nhập liệu", "Vui lòng chọn ngày bắt đầu và ngày kết thúc!", false);
				return;
			}
			
			LocalDate ngayBD = tool.utilDateSangLocalDate(dateBD);
			LocalDate ngayKT = tool.utilDateSangLocalDate(dateKT);
			
			if (ngayKT.isBefore(ngayBD)) {
				tool.hienThiThongBao("Lỗi ngày", "Ngày kết thúc phải sau ngày bắt đầu!", false);
				return;
			}
			
			int mucGiam = 0;
			int slMua = 0;
			int slTang = 0;

			if (loaiKM.equalsIgnoreCase("Giảm giá (%)")) {
				try {
					mucGiam = Integer.parseInt(themKmGUI.txtMucKM.getText().trim());
					if (mucGiam <= 0 || mucGiam > 100) {
						tool.hienThiThongBao("Lỗi số liệu", "Mức giảm giá phải từ 1% đến 100%!", false);
						return;
					}
				} catch (NumberFormatException e) {
					tool.hienThiThongBao("Lỗi định dạng", "Mức giảm giá phải là số nguyên!", false);
					return;
				}
			} else {
				try {
					slMua = Integer.parseInt(themKmGUI.txtSoLuongMua.getText().trim());
					slTang = Integer.parseInt(themKmGUI.txtSoLuongTang.getText().trim());
					if (slMua <= 0 || slTang <= 0) {
						tool.hienThiThongBao("Lỗi số liệu", "Số lượng mua và tặng phải lớn hơn 0!", false);
						return;
					}
				} catch (NumberFormatException e) {
					tool.hienThiThongBao("Lỗi định dạng", "Số lượng mua/tặng phải là số nguyên!", false);
					return;
				}
			}

			DefaultTableModel model = (DefaultTableModel) themKmGUI.tblThuocKhuyenMai.getModel();
			ArrayList<Object[]> listThuocChon = new ArrayList<>();
			int rowCount = model.getRowCount();
			
			if (rowCount == 0) {
				int confirm = javax.swing.JOptionPane.showConfirmDialog(themKmGUI, 
						"Bạn chưa chọn thuốc nào áp dụng khuyến mãi này.\nBạn có muốn tạo khuyến mãi rỗng không?", 
						"Cảnh báo", javax.swing.JOptionPane.YES_NO_OPTION);
				if (confirm != javax.swing.JOptionPane.YES_OPTION) {
					return;
				}
			}

			for (int i = 0; i < rowCount; i++) {
				String maThuoc = model.getValueAt(i, 0).toString();
				listThuocChon.add(new Object[] { maThuoc });
			}
			String maKM = tool.taoKhoaChinh("KM"); 

			KhuyenMai km = new KhuyenMai();
			km.setMaKM(maKM);
			km.setTenKM(tenKM);
			km.setLoaiKM(loaiKM.equalsIgnoreCase("Giảm giá (%)") ? "Giảm giá" : "Mua tặng");
			km.setMucKM(mucGiam);
			km.setSoLuongMua(slMua);
			km.setSoLuongTang(slTang);
			km.setNgayBD(ngayBD);
			km.setNgayKT(ngayKT);
			km.setTrangThai(true);

			boolean ketQua = kmDAO.themKM(km, listThuocChon);

			if (ketQua) {
				tool.hienThiThongBao("Thành công", "Thêm khuyến mãi mới thành công!", true);
				themKmGUI.lamMoi(); 
				model.setRowCount(0);
				capNhatLaiTable();
				
			} else {
				tool.hienThiThongBao("Thất bại", "Lỗi khi lưu vào cơ sở dữ liệu!", false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tool.hienThiThongBao("Lỗi hệ thống", "Đã xảy ra lỗi không mong muốn: " + e.getMessage(), false);
		}
	}
	
	
	//Xoá thuốc khỏi bảng chọn
	public void xoaThuocTuBang(JTable table) {
		int selectedRow = table.getSelectedRow();
		
		if(selectedRow == -1) {
			tool.hienThiThongBao("Lỗi!", "Vui lòng chọn 1 thuốc để xoá.", false);
			return;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, 
				"Bạn có chắc chắn muốn xoá thuốc này khỏi danh sách áp dụng không?",
				"Xác nhận!", JOptionPane.YES_NO_OPTION);
		
		if(confirm == JOptionPane.YES_OPTION) {
			try {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int modelRow = table.convertRowIndexToModel(selectedRow);
				model.removeRow(modelRow);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void caiDatGoiYThuoc(JComboBox<String> cmb) {
        cmb.setEditable(true);
        final JTextField textfield = (JTextField) cmb.getEditor().getEditorComponent();

        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Bỏ qua các phím điều hướng để người dùng có thể chọn bằng mũi tên
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || 
                    e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_LEFT || 
                    e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    String text = textfield.getText();
                    int caretPos = textfield.getCaretPosition(); 
                    ArrayList<String> ketQuaLoc = new ArrayList<>();
                    
                    if (listThuocTam == null) listThuocTam = thuocDao.layListThuocHoanChinh();

                    for (Thuoc t : listThuocTam) {
                        if (t.getTenThuoc().toLowerCase().contains(text.toLowerCase())) {
                            ketQuaLoc.add(t.getTenThuoc());
                        }
                    }
                    
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmb.getModel();
                    model.removeAllElements();
                    
                    for (String s : ketQuaLoc) {
                        model.addElement(s);
                    }

                    textfield.setText(text);
                    try {
                        textfield.setCaretPosition(caretPos); 
                    } catch (Exception ex) {    
                    }

                    if (!ketQuaLoc.isEmpty() && !text.isEmpty()) {
                        cmb.showPopup();
                    } else if (text.isEmpty()) {
                        cmb.hidePopup();
                    }
                });
            }
        });
    }
	
	
	
	
	
	
	
	
	
	
	
	
}
