package controller;

import java.io.File;
import java.util.List;

import dao.DonViTinhDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.DonViTinh;
import entity.NhanVien;
import entity.Thue;
import entity.Thuoc;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LapHoaDonCtrl {
	@FXML
	public ComboBox<Thuoc> cmbSanPham;
	@FXML
	public ComboBox<DonViTinh> cmbDonVi;
	@FXML
	public ComboBox<String> cmbHTThanhToan;
	@FXML
	public Button btnXuatHD;
	@FXML
	public Button btnThem;
	@FXML
	public Button btnLamMoi;
	@FXML
	public TextField txtTenKH;
	@FXML
	public TextField txtSdt;
	@FXML
	public TextField txtSoLuong;
	@FXML
	public Label lblTongTien;
	@FXML
	public Label lblTongTienGiam;
	@FXML
	public Label lblTienPhaiTra;
	@FXML
	public TextField txtTienNhan;
	@FXML
	public Label lblTienThua;
	@FXML
	public TableView<Object[]> tblThuoc;
	@FXML
	public TableColumn<Object[], Boolean> colSTT;
	@FXML
	public TableColumn<Object[], String> colTenThuoc;
	@FXML
	public TableColumn<Object[], String> colSoLuong;
	@FXML
	public TableColumn<Object[], String> colDonVi;
	@FXML
	public TableColumn<Object[], String> colDonGia;
	@FXML
	public TableColumn<Object[], String> colThanhTien;
	@FXML
	public TableColumn<Object[], Void> colHoatDong;

	private TrangChuNVCtrl trangChuNVCtrl;
	private TrangChuQLCtrl trangChuQLCtrl;
	private ToolCtrl toolCtrl = new ToolCtrl();

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}

	public void initialize() {
		setAction();
	}

	public void setAction() {
		setItemComboBoxSanPham();
		setItemComboBoxDonVi();
		setItemComboBoxHTThanhToan();
		btnThem.setOnAction(e -> xuLyThemThuocVaoBang());
		setUpTableColumns();
		setupColHoatDong();
		setupColSTT();
		btnLamMoi.setOnAction(e -> lamMoi());
		cmbHTThanhToan.valueProperty().addListener((obs, oldVal, newVal) -> {
			tinhTienThua();
		});
		txtTienNhan.textProperty().addListener((obs, oldVal, newVal) -> {
			tinhTienThua();
		});
	}

	// ========================================================
	// ========== SETUP COMBO_BOX SẢN PHẨM ==========
	public void setItemComboBoxSanPham() {
		ThuocDAO thuocDAO = new ThuocDAO();
		List<Thuoc> dsThuoc = thuocDAO.layListThuoc();

		cmbSanPham.getItems().clear();
		cmbSanPham.getItems().addAll(dsThuoc);

		thietLapHienThiTenThuoc();

		thietLapTimKiemThuoc(dsThuoc);
	}

	// ========== HIỂN THỊ TÊN SẢN PHẨM LÊN COMBO_BOX==========
	private void thietLapHienThiTenThuoc() {
		cmbSanPham.setCellFactory(param -> new ListCell<Thuoc>() {
			@Override
			protected void updateItem(Thuoc item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : item.getTenThuoc());
			}
		});

		cmbSanPham.setButtonCell(new ListCell<Thuoc>() {
			@Override
			protected void updateItem(Thuoc item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : item.getTenThuoc());
			}
		});
	}

	// ========== SETUP TÌM KIẾM SẢN PHẨM TRÊN COMBO_BOX ==========
	private void thietLapTimKiemThuoc(List<Thuoc> dsThuoc) {
		cmbSanPham.setEditable(true);
		ObservableList<Thuoc> allThuoc = FXCollections.observableArrayList(dsThuoc);
		final boolean[] isAdjusting = { false };

		cmbSanPham.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			if (isAdjusting[0])
				return;

			Platform.runLater(() -> {
				isAdjusting[0] = true;

				if (newValue == null || newValue.isEmpty()) {
					cmbSanPham.setItems(allThuoc);
				} else {
					String lowerCase = newValue.toLowerCase();
					ObservableList<Thuoc> filtered = allThuoc
							.filtered(t -> t.getTenThuoc().toLowerCase().contains(lowerCase));

					cmbSanPham
							.setItems(filtered.size() > 10 ? FXCollections.observableArrayList(filtered.subList(0, 10))
									: filtered);
				}
				if (cmbSanPham.isFocused()) {
					cmbSanPham.show();
				}
				isAdjusting[0] = false;
			});
		});
	}

	// ========== SETUP COMBO_BOX ĐƠN VỊ ==========
	public void setItemComboBoxDonVi() {
		DonViTinhDAO dvtDAO = new DonViTinhDAO();
		List<DonViTinh> dsDVT = dvtDAO.layListDVT();

		cmbDonVi.getItems().clear();
		cmbDonVi.getItems().addAll(dsDVT);

		if (!dsDVT.isEmpty()) {
			cmbDonVi.setValue(dsDVT.get(0));
		}
	}

	// ========================================================
	// ========== ĐƯA DỮ LIỆU XUỐNG BẢNG ==========
	private void xuLyThemThuocVaoBang() {
		Object selected = cmbSanPham.getValue();

		if (selected instanceof String) {
			String tenNhap = ((String) selected).trim().toLowerCase();
			for (Thuoc t : cmbSanPham.getItems()) {
				if (t.getTenThuoc().toLowerCase().equals(tenNhap)) {
					selected = t;
					cmbSanPham.setValue(t);
					break;
				}
			}
		}

		if (!(selected instanceof Thuoc)) {
			toolCtrl.hienThiThongBao("Thông báo", "Vui lòng chọn thuốc hợp lệ từ danh sách!", false);
			return;
		}
		Thuoc thuoc = (Thuoc) selected;

		DonViTinh dvt = cmbDonVi.getValue();
		String soLuongStr = txtSoLuong.getText().trim();

		if (soLuongStr.isEmpty()) {
			toolCtrl.hienThiThongBao("Thông báo", "Vui lòng nhập số lượng!", false);
			return;
		}

		int soLuong;
		try {
			soLuong = Integer.parseInt(soLuongStr);
			if (soLuong <= 0) {
				toolCtrl.hienThiThongBao("Thông báo", "Số lượng phải lớn hơn 0!", false);
				return;
			}
		} catch (NumberFormatException e) {
			toolCtrl.hienThiThongBao("Thông báo", "Số lượng không hợp lệ!", false);
			return;
		}

		double donGia = thuoc.getGiaBan();
		double thanhTien = donGia * soLuong;

		// ✅ Tạo Object[] thay vì ThuocTrongHD
		Object[] thuocHD = new Object[] { thuoc.getMaThuoc(), // 0
				thuoc.getTenThuoc(), // 1
				soLuong, // 2
				dvt != null ? dvt.getTenDVT() : "", // 3
				donGia, // 4
				thanhTien // 5
		};

		// ✅ Thêm vào bảng
		ObservableList<Object[]> dsThuocHD = tblThuoc.getItems();
		if (dsThuocHD == null) {
			dsThuocHD = FXCollections.observableArrayList();
			tblThuoc.setItems(dsThuocHD);
		}

		// Kiểm tra trùng thuốc
		boolean daCo = false;
		for (Object[] t : dsThuocHD) {
			if (t[0].equals(thuocHD[0])) {
				int soLuongCu = (int) t[2];
				soLuongCu += soLuong;
				t[2] = soLuongCu;
				t[5] = ((double) t[4]) * soLuongCu; // cập nhật thành tiền
				daCo = true;
				break;
			}
		}

		if (!daCo) {
			dsThuocHD.add(thuocHD);
		}

		tblThuoc.refresh();
		tinhTongTien();

		txtSoLuong.clear();
		cmbSanPham.getEditor().clear();
		cmbSanPham.setValue(null);
	}

	// ========== HIỂN THỊ DỮ LIỆU LÊN CỘT ==========
	private void setUpTableColumns() {
		colTenThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1].toString()));

		colSoLuong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2].toString()));

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
	}

	// ========== SETUP CỘT STT TRONG TABLE ==========
	private void setupColSTT() {
		colSTT.setCellFactory(col -> new TableCell<Object[], Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || getTableRow() == null) {
					setText(null);
				} else {
					setText(String.valueOf(getIndex() + 1));
				}
			}
		});
	}

	// ========== SETUP CỘT HOẠT ĐỘNG TRONG TABLE ==========
	public void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<Object[], Void>() {
			private final Button btn = new Button("Xóa");

			{
				btn.getStyleClass().add("btnXoa");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> {
					Object[] thuocTHD = getTableView().getItems().get(getIndex());
					String tenThuoc = thuocTHD[1].toString();
					// xử lý xóa
					if (toolCtrl.hienThiXacNhan("Xóa thuốc", "Xác nhận xóa thuốc: " + tenThuoc + "?")) {
						xoaThuocTrongHD(thuocTHD);
					}
					getTableView().refresh();
				});
			}

			@Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            setGraphic(empty ? null : btn);
	        }
	    });
	}

	// ========== XỬ LÝ XOÁ 1 DÒNG TRONG BẢNG ==========
	private void xoaThuocTrongHD(Object[] thuocHD) {
	    ObservableList<Object[]> dsThuocHD = tblThuoc.getItems();
	    dsThuocHD.remove(thuocHD);
	    tblThuoc.refresh();
	    tinhTongTien(); 
	}

	// ========================================================
	// ========== SETUP COMBO_BOX THANH TOÁN ==========
	public void setItemComboBoxHTThanhToan() {
		cmbHTThanhToan.getItems().addAll("Tiền mặt", "Chuyển khoản");
		cmbHTThanhToan.setValue("Tiền mặt");
	}

	// ========== TÍNH TỔNG TIỀN ==========
	private void tinhTongTien() {
	    double tongTien = 0;
	    double tongTienGiam = 0;
	    double tienPhaiTra = 0;

	    for (Object[] thuocHD : tblThuoc.getItems()) {
	        Object thanhTienObj = thuocHD[5];
	        double thanhTien = thanhTienObj instanceof Number ? ((Number) thanhTienObj).doubleValue() : 0;
	        tongTien += thanhTien;
	    }

	    lblTongTien.setText(String.format("%,.0f VNĐ", tongTien));

	    tienPhaiTra = tongTien - tongTienGiam;
	    lblTienPhaiTra.setText(String.format("%,.0f VNĐ", tienPhaiTra));
	}


	private void tinhTienThua() {
		if (!"Tiền mặt".equals(cmbHTThanhToan.getValue())) {
			txtTienNhan.setDisable(true);
			txtTienNhan.clear();
			lblTienThua.setText("0 VNĐ");
			return;
		} else {
			txtTienNhan.setDisable(false);
		}

		String textTienNhan = txtTienNhan.getText().trim();

		if (textTienNhan.isEmpty()) {
			lblTienThua.setText("0 VNĐ");
			return;
		}

		String textTienPhaiTra = lblTienPhaiTra.getText().replace("VNĐ", "").replace(".", "").replace(",", "").trim();
		double tienPhaiTra = 0;
		try {
			tienPhaiTra = Double.parseDouble(textTienPhaiTra);
		} catch (NumberFormatException e) {
			tienPhaiTra = 0;
		}

		double tienNhan;
		try {
			tienNhan = Double.parseDouble(textTienNhan);
		} catch (NumberFormatException e) {
			toolCtrl.hienThiThongBao("Lỗi nhập liệu", "Số tiền nhận không hợp lệ!", false);
			lblTienThua.setText("0 VNĐ");
			return;
		}

		// Tính tiền thừa
		double tienThua = tienNhan - tienPhaiTra;
		lblTienThua.setText(String.format("%,.0f VNĐ", tienThua));
	}

	// ========== LÀM MỚI ==========
	private void lamMoi() {
		txtSdt.setText("");
		txtTenKH.setText("");
		txtSoLuong.setText("");
		cmbSanPham.setValue(null);
		setItemComboBoxDonVi();
		cmbHTThanhToan.setValue("Tiền mặt");
		tblThuoc.getItems().clear();
		lblTongTien.setText("0 VNĐ");
		lblTongTienGiam.setText("0 VNĐ");
		lblTienPhaiTra.setText("0 VNĐ");
		txtTienNhan.setText("");
		lblTienThua.setText("0 VNĐ");
	}

}
