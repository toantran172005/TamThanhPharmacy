package controller;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.ChiTietNhanVien_GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ChiTietNhanVienCtrl {
	public ChiTietNhanVien_GUI gui;
	public JComboBox<String> cmbGioiTinh;
	public JComboBox<String> cmbChucVu;
	public JButton btnChonAnh, btnCapNhat, btnLamMoi, btnLuu;
	public JTextField txtMaNV, txtTenNV, txtSdt, txtLuong, txtEmail, txtThue;
	public JDateChooser dtpNgaySinh, dtpNgayVaoLam;
	public JLabel lblTrangThai;
	public JLabel imgAnhNV;
	public String tenNV_goc, luong_goc, thue_goc, sdt_goc, chucVu_goc, gioiTinh_goc, email_goc;
	public Image anh_goc;
	public File fileAnhDaChon;
	public String duongDanAnh;
	public ToolCtrl tool = new ToolCtrl();
	public NhanVienDAO nvDao = new NhanVienDAO();

	public ChiTietNhanVienCtrl(ChiTietNhanVien_GUI gui) {
		this.gui = gui;

		// ánh xạ các component từ GUI
		this.cmbGioiTinh = gui.getCmbGioiTinh();
		this.cmbChucVu = gui.getCmbChucVu();
		this.btnChonAnh = gui.getBtnChonAnh();
		this.btnCapNhat = gui.getBtnCapNhat();
		this.btnLamMoi = gui.getBtnLamMoi();
		this.btnLuu = gui.getBtnLuu();
		this.txtMaNV = gui.getTxtMaNV();
		this.txtTenNV = gui.getTxtTenNV();
		this.txtSdt = gui.getTxtSdt();
		this.txtLuong = gui.getTxtLuong();
		this.txtEmail = gui.getTxtEmail();
		this.txtThue = gui.getTxtThue();
		this.dtpNgaySinh = gui.getDtpNgaySinh();
		this.dtpNgayVaoLam = gui.getDtpNgayVaoLam();
		this.lblTrangThai = gui.getLblTrangThai();
		this.imgAnhNV = gui.getImgAnhNV();

		suKien();
	}

	// ========== SỰ KIỆN ==========
	public void suKien() {
		setItemComboBoxGioiTinh();
		setItemComboBoxChucVu();
		khoiTaoCheDoEdit(false);

		btnLamMoi.addActionListener(e -> khoiPhucDuLieu());
		btnCapNhat.addActionListener(e -> khoiTaoCheDoEdit(true));
		btnChonAnh.addActionListener(e -> chonAnhMoi());
		btnLuu.addActionListener(e -> capNhatNhanVien());
	}

	// ========== COMBOBOX ==========
	public void setItemComboBoxGioiTinh() {
		cmbGioiTinh.removeAllItems();
		cmbGioiTinh.addItem("Nam");
		cmbGioiTinh.addItem("Nữ");
	}

	public void setItemComboBoxChucVu() {
		cmbChucVu.removeAllItems();
		cmbChucVu.addItem("Nhân viên quản lý");
		cmbChucVu.addItem("Nhân viên bán hàng");
	}

	// ========== HIỂN THỊ THÔNG TIN NV ==========
	public void setNhanVienHienTai(NhanVien nv) {
		if (nv == null)
			return;

		txtMaNV.setText(nv.getMaNV());
		txtTenNV.setText(nv.getTenNV());
		cmbChucVu.setSelectedItem(nv.getChucVu());
		txtSdt.setText(tool.chuyenSoDienThoai(nv.getSdt()));
		txtLuong.setText(tool.dinhDangVND(nv.getLuong()));
		cmbGioiTinh.setSelectedItem(nv.isGioiTinh() ? "Nam" : "Nữ");
		dtpNgaySinh.setDate(Date.from(nv.getNgaySinh().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dtpNgayVaoLam.setDate(Date.from(nv.getNgayVaoLam().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		txtEmail.setText(nvDao.layEmailNV(nv.getMaNV()));
		txtThue.setText(nv.getThue() != null ? nv.getThue().getTiLeThue() + "" : "");
		lblTrangThai.setText(nv.isTrangThai() ? "Còn làm" : "Đã nghỉ");
		imgAnhNV.setIcon(new ImageIcon(taiAnh(nv.getAnh())));

		luuDuLieuGoc();
	}

	// ========== TẢI HÌNH ẢNH ==========
	public Image taiAnh(String duongDanTuDB) {
	    try {
	        // nếu DB trả về null hoặc empty → dùng ảnh default
	        if (duongDanTuDB == null || duongDanTuDB.trim().isEmpty()) {
	            return new ImageIcon(System.getProperty("user.dir") + "/resource/picture/default.png").getImage();
	        }

	        File file = new File(System.getProperty("user.dir") + "/resource" + duongDanTuDB);

	        if (file.exists()) {
	            return new ImageIcon(file.getAbsolutePath()).getImage();
	        } else {
	            return new ImageIcon(System.getProperty("user.dir") + "/resource/picture/default.png").getImage();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ImageIcon(System.getProperty("user.dir") + "/resource/picture/default.png").getImage();
	    }
	}

	// ========== CHỌN ẢNH ==========
	public void chonAnhMoi() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Chọn ảnh nhân viên");

		chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fileAnhDaChon = chooser.getSelectedFile();
			imgAnhNV.setIcon(new ImageIcon(fileAnhDaChon.getAbsolutePath()));
		}
	}

	// ========== LƯU DỮ LIỆU GỐC ==========
	public void luuDuLieuGoc() {
		tenNV_goc = txtTenNV.getText();
		sdt_goc = txtSdt.getText();
		luong_goc = txtLuong.getText();
		email_goc = txtEmail.getText();
		chucVu_goc = (String) cmbChucVu.getSelectedItem();
		gioiTinh_goc = (String) cmbGioiTinh.getSelectedItem();

		Icon icon = imgAnhNV.getIcon();
		if (icon instanceof ImageIcon) {
			anh_goc = ((ImageIcon) icon).getImage();
		}
	}

	// ========== KHÔI PHỤC ==========
	public void khoiPhucDuLieu() {
		txtTenNV.setText(tenNV_goc);
		txtSdt.setText(sdt_goc);
		txtLuong.setText(luong_goc);
		txtEmail.setText(email_goc);
		cmbChucVu.setSelectedItem(chucVu_goc);
		cmbGioiTinh.setSelectedItem(gioiTinh_goc);
		imgAnhNV.setIcon(new ImageIcon(anh_goc));
	}

	// ========== LƯU ẢNH ==========
	public String luuAnh(String maNV) {
		try {
			String duongDanProject = System.getProperty("user.dir");
			File thuMuc = new File(duongDanProject, "resource/picture/nhanVien");

			if (fileAnhDaChon != null) {
				String extension = fileAnhDaChon.getName().substring(fileAnhDaChon.getName().lastIndexOf("."));
				String tenMoi = maNV + extension;

				File fileDich = new File(thuMuc, tenMoi);

				Files.copy(fileAnhDaChon.toPath(), fileDich.toPath(),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);

				return "/picture/nhanVien/" + tenMoi;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ========== SETUP CẬP NHẬT ==========
	public void khoiTaoCheDoEdit(boolean isEdit) {
		txtTenNV.setEditable(isEdit);
		txtLuong.setEditable(isEdit);
		txtSdt.setEditable(isEdit);
		txtEmail.setEditable(isEdit);

		cmbChucVu.setEnabled(isEdit);
		cmbGioiTinh.setEnabled(isEdit);

		btnLamMoi.setVisible(isEdit);
		btnChonAnh.setVisible(isEdit);
		btnLuu.setVisible(isEdit);

		if (isEdit == true) {
			btnCapNhat.setVisible(false);
		} else {
			btnCapNhat.setVisible(true);
		}
	}

	// ========== CẬP NHẬT NV ==========
	public void capNhatNhanVien() {
		try {
			if (!kiemTraHopLe())
				return;

			String maNV = txtMaNV.getText();
			String tenNV = txtTenNV.getText();
			String chucVu = (String) cmbChucVu.getSelectedItem();
			String sdt = tool.chuyenSoDienThoai(txtSdt.getText());
			String email = txtEmail.getText();
			boolean gioiTinh = cmbGioiTinh.getSelectedItem().equals("Nam");

			LocalDate ngaySinh = dtpNgaySinh.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate ngayVaoLam = dtpNgayVaoLam.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			double luong = tool.chuyenTienSangSo(txtLuong.getText());

			String anh;

			if (fileAnhDaChon != null) {
				anh = luuAnh(maNV); // ảnh mới
			} else {
				anh = nvDao.layAnhNV(maNV); // ảnh cũ từ DB
			}
			boolean trangThai = lblTrangThai.getText().equals("Còn làm");

			NhanVien nv = new NhanVien(maNV, tenNV, chucVu, ngaySinh, gioiTinh, sdt, ngayVaoLam, luong, null, trangThai,
					anh);

			boolean kq = nvDao.capNhatNhanVien(nv);

			if (kq)
				tool.hienThiThongBao("Thành công", "Cập nhật nhân viên thành công!", true);
			else
				tool.hienThiThongBao("Lỗi", "Không thể cập nhật nhân viên!", false);

			khoiTaoCheDoEdit(false);

		} catch (Exception e) {
			e.printStackTrace();
			tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi!", false);
		}
	}

	// ========== KIỂM TRA HỢP LỆ ==========
	public boolean kiemTraHopLe() {
		String ten = txtTenNV.getText().trim();

		if (ten.isEmpty()) {
			tool.hienThiThongBao("Lỗi", "Tên nhân viên không được để trống!", false);
			return false;
		}
		if (!ten.matches("^[\\p{L}\\s]+$")) {
			tool.hienThiThongBao("Lỗi", "Tên nhân viên chỉ chứa chữ cái!", false);
			return false;
		}

		String sdt = txtSdt.getText().trim();
		if (!sdt.matches("^0\\d{9}$")) {
			tool.hienThiThongBao("Lỗi", "SĐT phải có 10 chữ số!", false);
			return false;
		}

		String email = txtEmail.getText().trim();
		if (!email.isEmpty() && !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
			tool.hienThiThongBao("Lỗi", "Email không hợp lệ!", false);
			return false;
		}

		try {
			double luong = tool.chuyenTienSangSo(txtLuong.getText());
			if (luong <= 0) {
				tool.hienThiThongBao("Lỗi", "Lương phải > 0!", false);
				return false;
			}
		} catch (Exception e) {
			tool.hienThiThongBao("Lỗi", "Lương không hợp lệ!", false);
			return false;
		}

		return true;
	}
}
