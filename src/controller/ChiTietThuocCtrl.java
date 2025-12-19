package controller;

import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.DonViTinhDAO;
import dao.KeThuocDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.KeThuoc;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.TimKiemThuoc_GUI;

public class ChiTietThuocCtrl {

	public ToolCtrl tool = new ToolCtrl();
	public ThuocDAO thDAO = new ThuocDAO();
	public KeThuocDAO keDAO = new KeThuocDAO();
	public ThueDAO thueDAO = new ThueDAO();
	public DonViTinhDAO dvtDAO = new DonViTinhDAO();
	public ChiTietThuoc_GUI ctThuoc;
	public String duongDanAnhHienTai = null;
	
	public ChiTietThuocCtrl(ChiTietThuoc_GUI ctThuoc) {
		this.ctThuoc = ctThuoc;
	}
	
	//Hiển thị chi tiết thuốc
	public void xemChiTietThuoc(String maThuoc) {
		if(maThuoc == null) {
			tool.hienThiThongBao("Lỗi!", "Mã thuốc không hợp lệ", false);
			return;
		}
		
		try {
			ArrayList<Thuoc> list = thDAO.layListThuocHoanChinh();
			for(Thuoc t : list) {
				if(maThuoc.equalsIgnoreCase(t.getMaThuoc())) {
					//lấy đường dẫn ảnh
					String anhPath = t.getAnh();
	                ImageIcon icon = null;
	                if (anhPath != null && anhPath.startsWith("/picture/")) {
	                    java.net.URL imgURL = getClass().getResource(anhPath);
	                    if (imgURL != null) {
	                        icon = new ImageIcon(imgURL);
	                    } else {
	                        tool.hienThiThongBao("Lỗi", "Không tìm thấy ảnh", false);
	                    }
	                }
	                
	                //Đổ dữ liệu
	                String thue = t.getThue().getTiLeThue()*100 + "%";
	                Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
	                ctThuoc.lblAnh.setIcon(new ImageIcon(img));
	                ctThuoc.txtMaSP.setText(maThuoc);
	                ctThuoc.txtTenSP.setText(t.getTenThuoc());
	                ctThuoc.txtDangThuoc.setText(t.getDangThuoc());
	                ctThuoc.txtGiaBan.setText(tool.dinhDangVND(t.getGiaBan()));
	                ctThuoc.dpHanSuDung.setDate(tool.localDateSangUtilDate(t.getHanSuDung()));
	                ctThuoc.cmbDonViTinh.setSelectedItem(t.getDvt().getTenDVT());
	                ctThuoc.cmbThue.setSelectedItem(thue);
	                ctThuoc.cmbKeThuoc.setSelectedItem(t.getKeThuoc().getLoaiKe());
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Chọn ảnh
    public void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh thuốc");
        // Chỉ chọn file ảnh
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (JPG, PNG)", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            try {
                //Xác định thư mục đích (src/picture/thuoc/)
                String projectPath = System.getProperty("user.dir"); 
                String folderPath = projectPath + "\\src\\picture\\thuoc";
                
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs(); // Tạo thư mục nếu chưa có
                }

                // Tạo file đích
                String fileName = selectedFile.getName(); 
                File destFile = new File(folderPath + "\\" + fileName);

                // Copy file vào thư mục dự án
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                duongDanAnhHienTai = "/picture/thuoc/" + fileName;

                hienThiAnhLenLabel(duongDanAnhHienTai);

            } catch (Exception e) {
                e.printStackTrace();
                tool.hienThiThongBao("Lỗi", "Lỗi khi lưu ảnh: " + e.getMessage(), false);
            }
        }
    }

    // Hàm phụ để hiển thị ảnh an toàn
    private void hienThiAnhLenLabel(String path) {
        try {
            ImageIcon icon = null;
            if (path != null && !path.isEmpty()) {
                java.net.URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    icon = new ImageIcon(imgURL);
                } else {
                     String projectPath = System.getProperty("user.dir");
                     icon = new ImageIcon(projectPath + "/src" + path);
                }
            }
            
            if (icon == null || icon.getImage() == null) {
                 ctThuoc.lblAnh.setIcon(null);
                 ctThuoc.lblAnh.setText("Không có ảnh");
            } else {
                Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                ctThuoc.lblAnh.setIcon(new ImageIcon(img));
                ctThuoc.lblAnh.setText("");
            }
        } catch (Exception e) {
             ctThuoc.lblAnh.setIcon(null);
        }
    }
	
	//cập nhật thuốc
	public void xuLyCapNhat() {
        String trangThaiHienTai = ctThuoc.btnCapNhat.getText();

        if (trangThaiHienTai.equalsIgnoreCase("Cập nhật")) {
            ctThuoc.thietLapKhoaChinhSua(true);
            
            ctThuoc.btnCapNhat.setText("Lưu");
            String giaHienTai = ctThuoc.txtGiaBan.getText();
            
            try {
                String giaSo = giaHienTai.replaceAll("[^0-9]", ""); 
                ctThuoc.txtGiaBan.setText(giaSo);
            } catch (Exception e) {
                ctThuoc.txtGiaBan.setText("0");
            }
            
            ctThuoc.txtTenSP.requestFocus();

        } else {
            if (kiemTraDuLieuDauVao()) { 
            	
                Thuoc t = layThuocTuGiaoDien();
               
                if (t == null) return; 
                
                boolean ketQua = thDAO.capNhatThuoc(t); 

                if (ketQua) {
                    tool.hienThiThongBao("Thành công", "Cập nhật thông tin thuốc thành công!", true);
                    
                    ctThuoc.thietLapKhoaChinhSua(false);
                    ctThuoc.btnCapNhat.setText("Cập nhật");
                    ctThuoc.txtGiaBan.setText(tool.dinhDangVND(t.getGiaBan()));
                } else {
                    tool.hienThiThongBao("Thất bại", "Cập nhật thất bại, vui lòng kiểm tra lại!", false);
                }
            }
        }
    }

    // Hàm lấy dữ liệu từ Form (Helper)
	private Thuoc layThuocTuGiaoDien() {
        try {
            String maThuoc = ctThuoc.txtMaSP.getText();
            String tenThuoc = ctThuoc.txtTenSP.getText().trim();
            String dangThuoc = ctThuoc.txtDangThuoc.getText().trim();
            String giaBanStr = ctThuoc.txtGiaBan.getText().trim().replaceAll("[^0-9]", "");
            double giaBan = Double.parseDouble(giaBanStr);
            
            java.util.Date date = ctThuoc.dpHanSuDung.getDate();
            LocalDate hanSuDung = tool.utilDateSangLocalDate(date);

            String tenDVT = ctThuoc.cmbDonViTinh.getSelectedItem().toString();
            DonViTinh dvt = dvtDAO.timTheoTen(tenDVT); 

            String tiLeThueStr = ctThuoc.cmbThue.getSelectedItem().toString().replace("%", "");
            double tiLeThue = Double.parseDouble(tiLeThueStr) / 100;
            Thue thue = thueDAO.timTheoTen(tiLeThue);

            String tenKe = ctThuoc.cmbKeThuoc.getSelectedItem().toString();
            KeThuoc ke = keDAO.timTheoTen(tenKe); 

            String tenQG = ctThuoc.cmbQuocGia.getSelectedItem().toString();
            String maQG = thDAO.layMaQuocGiaTheoTen(tenQG);
            QuocGia qg = thDAO.layQuocGiaTheoMa(maQG);

            Thuoc t = new Thuoc();
            t.setMaThuoc(maThuoc);
            t.setTenThuoc(tenThuoc);
            t.setDangThuoc(dangThuoc);
            t.setGiaBan(giaBan);
            t.setHanSuDung(hanSuDung);
            t.setDvt(dvt);
            t.setThue(thue);
            t.setKeThuoc(ke);
            t.setQuocGia(qg);
            t.setTrangThai(true);     
            t.setAnh(duongDanAnhHienTai); 
            
            return t;

        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi nhập liệu", "Giá bán phải là số!", false);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Hàm kiểm tra rỗng
    private boolean kiemTraDuLieuDauVao() {
        if (ctThuoc.txtTenSP.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Tên thuốc không được để trống", false);
            ctThuoc.txtTenSP.requestFocus();
            return false;
        }
        if (ctThuoc.txtGiaBan.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Giá bán không được để trống", false);
            ctThuoc.txtGiaBan.requestFocus();
            return false;
        }
        if (ctThuoc.dpHanSuDung.getDate() == null) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn hạn sử dụng", false);
            return false;
        }
        return true;
    }
	
	//quay lại trang tìm kiếm
	public void quayLaiTrangTimKiem() {
		tool.doiPanel(ctThuoc, new TimKiemThuoc_GUI());
	}
	
}
