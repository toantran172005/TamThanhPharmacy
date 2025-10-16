package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import entity.NhanVien;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import controller.ToolCtrl;

public class ChiTietNhanVienCtrl {
	@FXML public Button btnQuayLai;
	@FXML public Button btnCapNhat;
	@FXML private Label lblMaNV;
	@FXML private Label lblTenNV;
	@FXML private Label lblSdt;
	@FXML private Label lblGioiTinh;
	@FXML private Label lblChucVu;
	@FXML private Label lblNgaySinh;
	@FXML private Label lblNgayVaoLam;
	@FXML private Label lblEmail;
	@FXML private Label lblThue;
	@FXML private Label lblLuong;
	@FXML private Label lblTrangThai;

	private NhanVien nhanVienDaChon;
	private TrangChuNVCtrl trangChuNVCtrl; // tham chiếu controller cha
	private TrangChuQLCtrl trangChuQLCtrl;
	private ToolCtrl toolCtrl = new ToolCtrl();

	public void initialize() {
		btnQuayLai.setOnMouseClicked(e -> quayLai());
		btnCapNhat.setOnMouseClicked(e -> capNhat());
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}

	// ========== HIỂN THỊ THÔNG TIN CỦA NHÂN VIÊN ==========
	public void hienThiThongTinNhanVien(NhanVien nv) {
		this.nhanVienDaChon = nv;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // định dạng ngày

		lblMaNV.setText(nv.getMaNV());
		lblTenNV.setText(nv.getTenNV());
		lblSdt.setText(toolCtrl.chuyenSoDienThoai(nv.getSdt()));
		lblGioiTinh.setText(nv.isGioiTinh() ? "Nam" : "Nữ");
		lblChucVu.setText(nv.getChucVu());

		// Ép kiểu Date sang String có định dạng
		lblNgaySinh.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().format(dateFormatter) : "");
		lblNgayVaoLam.setText(nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().format(dateFormatter) : "");

		// Sửa lại phần Email (bạn để nhầm getChucVu)
//		    lblEmail.setText(nv.getEmail());

		// Thue có thể null, nên check trước
		lblThue.setText(nv.getThue() != null ? String.valueOf(nv.getThue().getTiLeThue()) : "");

		lblLuong.setText(toolCtrl.dinhDangVND(nv.getLuong()));
		lblTrangThai.setText(nv.isTrangThai() ? "Còn làm" : "Đã nghỉ");
	}

	// ========== QUAY LẠI TRANG TÌM KIẾM NHÂN VIÊN ==========
	private void quayLai() {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/TimKiemNhanVien.fxml", TimKiemNhanVienCtrl.class);
		} else if (trangChuQLCtrl != null) {
			trangChuQLCtrl.moTrang("/fxml/TimKiemNhanVien.fxml", TimKiemNhanVienCtrl.class);
		} else {
			System.out.println("⚠ Không có tham chiếu TrangChu");
		}
	}

	// ========== MỞ TRANG CẬP NHẬT NHÂN VIÊN ==========
	private void capNhat() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CapNhatNhanVien.fxml"));
	        Parent root = loader.load();

	        CapNhatNhanVienCtrl controller = loader.getController();
	        controller.setNhanVienHienTai(nhanVienDaChon);

	        if (trangChuNVCtrl != null) {
	            controller.setTrangChuNVCtrl(trangChuNVCtrl);
	            trangChuNVCtrl.moTrangDaTai(root);
	        } else if (trangChuQLCtrl != null) {
	            controller.setTrangChuQLCtrl(trangChuQLCtrl);
	            trangChuQLCtrl.moTrangDaTai(root);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
