package controller;

import java.time.format.DateTimeFormatter;

import dao.HoaDonDAO;
import entity.NhanVien;
import entity.Thuoc;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ChiTietHoaDonCtrl {
	@FXML
	public Button btnQuayLai;
	@FXML
	public Button btnInHoaDon;
	@FXML
	public Label lblDiaChi;
	@FXML
	public Label lblHotline;
	@FXML
	public Label lblMaHD;
	@FXML
	public Label lblNgayLap;
	@FXML
	public Label lblNhanVien;
	@FXML
	public Label lblKhachHang;
	@FXML
	public Label lblGhiChu;
	@FXML
	public Label lblTongTien;
	@FXML
	public Label lblTongTienGiam;
	@FXML
	public Label lblTienPhaiTra;
	@FXML
	public Label lblTienNhan;
	@FXML
	public Label lblTienThua;
	@FXML
	public TableView<Object[]> tblThuoc;
	@FXML
	public TableColumn<Object[], String> colTenThuoc;
	@FXML
	public TableColumn<Object[], String> colSoLuong;
	@FXML
	public TableColumn<Object[], String> colDonGia;
	@FXML
	public TableColumn<Object[], String> colThanhTien;

	private Object[] hoaDonDaChon;
	private TrangChuNVCtrl trangChuNVCtrl; // tham chiếu controller cha
	private TrangChuQLCtrl trangChuQLCtrl;
	private ToolCtrl toolCtrl = new ToolCtrl();
	private HoaDonDAO hdDAO = new HoaDonDAO();

	public void initialize() {
		btnQuayLai.setOnMouseClicked(e -> quayLai());
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}

	// ========== HIỂN THỊ THÔNG TIN CỦA NHÂN VIÊN ==========
	public void hienThiThongTinHoaDon(Object[] hd) {
	    this.hoaDonDaChon = hd;

	    // ====== Lấy dữ liệu từ mảng hd (có kiểm tra null) ======
	    String maHD = layGiaTri(hd, 0);
	    String tenKH = layGiaTri(hd, 2);
	    String tenNV = layGiaTri(hd, 4);
	    String diaChi = layGiaTri(hd, 7);
	    String hotline = layGiaTri(hd, 10);
	    String ghiChu = layGiaTri(hd, 9);

	    lblMaHD.setText(maHD);
	    lblKhachHang.setText(tenKH);
	    lblNhanVien.setText(tenNV);
	    lblDiaChi.setText(diaChi);
	    lblHotline.setText(toolCtrl.chuyenSoDienThoai(hotline));
	    lblGhiChu.setText(ghiChu);

	    // ====== Ngày lập ======
	    if (hd[6] instanceof java.time.LocalDate ngayLap) {
	        lblNgayLap.setText(toolCtrl.dinhDangLocalDate(ngayLap));
	    } else {
	        lblNgayLap.setText("");
	    }

	    // ====== Load chi tiết thuốc ======
	    setDataChoTable(maHD);

	    // ====== Tính tiền ======
	    double tongTien = hdDAO.tinhTongTienTheoHoaDon(maHD);
	    lblTongTien.setText(toolCtrl.dinhDangVND(tongTien));

	    double tienNhan = 0;
	    if (hd[11] instanceof Number num) {
	        tienNhan = num.doubleValue();
	    }

	    lblTienNhan.setText(toolCtrl.dinhDangVND(tienNhan));

	    double tienPhaiTra = tongTien;
	    lblTienPhaiTra.setText(toolCtrl.dinhDangVND(tienPhaiTra));

	    double tienThua = tienNhan - tienPhaiTra;
	    lblTienThua.setText(toolCtrl.dinhDangVND(tienThua));
	}


	public void setDataChoTable(String maHD) {    
	    ObservableList<Object[]> listCT = hdDAO.layDanhSachThuocTheoHoaDon(maHD);

	    colTenThuoc.setCellValueFactory(cellData ->
	        new SimpleStringProperty(layGiaTri(cellData.getValue(), 2)));

	    colSoLuong.setCellValueFactory(cellData ->
	        new SimpleStringProperty(layGiaTri(cellData.getValue(), 3)));

	    colDonGia.setCellValueFactory(cellData -> {
	        Object gia = cellData.getValue()[4];
	        double donGia = gia instanceof Number ? ((Number) gia).doubleValue() : 0;
	        return new SimpleStringProperty(toolCtrl.dinhDangVND(donGia));
	    });

	    colThanhTien.setCellValueFactory(cellData -> {
	        Object tt = cellData.getValue()[5];
	        double thanhTien = tt instanceof Number ? ((Number) tt).doubleValue() : 0;
	        return new SimpleStringProperty(toolCtrl.dinhDangVND(thanhTien));
	    });

	    tblThuoc.setItems(listCT);
	}

	
	// ================== HÀM TIỆN ÍCH ==================
	private String layGiaTri(Object[] arr, int index) {
	    if (arr == null || index < 0 || index >= arr.length || arr[index] == null)
	        return "";
	    return arr[index].toString();
	}
	
	// ========== QUAY LẠI TRANG TÌM KIẾM THUỐC ==========
	private void quayLai() {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/TimKiemHD.fxml", TimKiemHDCtrl.class);
		} else if (trangChuQLCtrl != null) {
			trangChuQLCtrl.moTrang("/fxml/TimKiemHD.fxml", TimKiemHDCtrl.class);
		} else {
			System.out.println("⚠ Không có tham chiếu TrangChu");
		}
	}
}
