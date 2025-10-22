package controller;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dao.KhachHangDAO;
import entity.KhachHang;
import entity.KhachHang;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.beans.property.BooleanProperty;

public class TimKiemKHCtrl {

	@FXML
	public TableView<KhachHang> tblKhachHang;

	@FXML
	public TableColumn<KhachHang, Boolean> colSelect;
	@FXML
	public TableColumn<KhachHang, String> colMaKH;
	@FXML
	public TableColumn<KhachHang, String> colTenKH;
	@FXML
	public TableColumn<KhachHang, String> colSdt;
	@FXML
	public TableColumn<KhachHang, Integer> colTuoi;
	@FXML
	public TableColumn<KhachHang, Void> colHoatDong;
	@FXML
	public Button btnLamMoi, btnXemChiTiet, btnLichSuXoa, btnXoaTatCa;
	@FXML
	public ImageView imgXoaTatCa;

	@FXML
	public TextField txtTenKH, txtSdt;

	public TrangChuQLCtrl trangChuQLCtrl;
	public TrangChuNVCtrl trangChuNVCtrl;

	public KhachHangDAO khDAO = new KhachHangDAO();
	public ToolCtrl toolCtrl = new ToolCtrl();
	public boolean tblDangLam = true;

	public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();

	public void initialize() {
		listKH = khDAO.layListKhachHang();
		locTatCa();
		setUpTextFieldVaButton();
	}

	public void setUpTextFieldVaButton() {
		btnXoaTatCa.setVisible(false);
		txtTenKH.setOnAction(event -> locTatCa()); // txt tìm kiếm theo tên khách hàng
		txtSdt.setOnAction(event -> locTatCa()); // txt tìm kiếm theo số điện thoại
		btnLamMoi.setOnAction(event -> lamMoiBang()); // btn làm mới lại table
		btnXemChiTiet.setOnAction(event -> chuyenDenTrangChiTiet());
		tblKhachHang.setOnMouseClicked(e -> hienThiNutXoaNhieu());
		btnXoaTatCa.setOnAction(event -> xoaNhieu());
		btnLichSuXoa.setOnAction(event -> hienThiLichSuXoa());
	}

	public void setDataChoTable(ObservableList<KhachHang> list) {
		// Checkbox
		setUpColCheckBox();
		// Các cột data
		colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
		colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
		colSdt.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleStringProperty(toolCtrl.chuyenSoDienThoai(kh.getSdt()));
		});
		colTuoi.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
		// Cột hoạt động
		setupColHoatDong();
		tblKhachHang.setItems(list);
	}

	public void setUpColCheckBox() {
		CheckBox chkChonTatCa = new CheckBox();
		chkChonTatCa.setAlignment(Pos.CENTER);
		colSelect.setGraphic(chkChonTatCa);

		chkChonTatCa.setOnAction(e -> {
			boolean isSelected = chkChonTatCa.isSelected();
			for (KhachHang kh : tblKhachHang.getItems()) {
				kh.setSelected(isSelected);
			}
			tblKhachHang.refresh();

			hienThiNutXoaNhieu();
		});

		colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		colSelect.setCellFactory(tc -> {
			CheckBoxTableCell<KhachHang, Boolean> cell = new CheckBoxTableCell<>() {
				@Override
				public void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty) {
						TableRow<KhachHang> row = getTableRow();
						if (row != null) {
							KhachHang kh = row.getItem();
							if (kh != null) {
								kh.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
									if (isNowSelected) {
										tblKhachHang.getSelectionModel().select(kh);
									} else {
										tblKhachHang.getSelectionModel()
												.clearSelection(tblKhachHang.getItems().indexOf(kh));
									}
								});
							}
						}
					}
				}
			};
			return cell;
		});

		tblKhachHang.setRowFactory(tv -> {
			TableRow<KhachHang> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					KhachHang kh = row.getItem();
					kh.setSelected(!kh.isSelected());
				}
			});
			return row;
		});
	}

	public void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<KhachHang, Void>() {
			private final Button btn = new Button();

			{
				btn.getStyleClass().add("btnXoaVaHoanTac");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> {
					KhachHang kh = getTableView().getItems().get(getIndex());

					if (kh.isTrangThai()) { // xử lý xóa
						if(toolCtrl.hienThiXacNhan("Xóa khách hàng", "Xác nhận xóa khách hàng tên: "+kh.getTenKH()+"?")) {
							xoaKhachHang(kh);
						}
					} else { // xử lý hoàn tác
						if(toolCtrl.hienThiXacNhan("Khôi phục khách hàng", "Xác nhận khôi phục khách hàng tên: "+kh.getTenKH()+"?")) {
							hoanTacKhachHang(kh);
						}
					}

					getTableView().refresh();
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					KhachHang kh = getTableView().getItems().get(getIndex());
					if (kh != null) {
						btn.setText(kh.isTrangThai() ? "Xóa" : "Hoàn Tác");
					}
					setGraphic(btn);
				}
			}
		});
	}
	
	public void hienThiLichSuXoa() {
		for (KhachHang kh : tblKhachHang.getItems()) {
	        kh.setSelected(false);
	    }
		btnXoaTatCa.setVisible(false);
	    tblKhachHang.refresh();
	    
		if(tblDangLam) {
			btnLichSuXoa.setText("Danh sách hiện tại");
			btnXoaTatCa.setText("Khôi phục tất cả");
			tblDangLam = false;
			Image image = new Image(getClass().getResourceAsStream("/picture/nhanVien/system-restore.png"));
			imgXoaTatCa.setImage(image);
			lamMoiBang();
		} else {
			btnLichSuXoa.setText("Lịch sử xóa");
			btnXoaTatCa.setText("Xóa tất cả");
			tblDangLam = true;
			Image image = new Image(getClass().getResourceAsStream("/picture/khachHang/trash.png"));
			imgXoaTatCa.setImage(image);
			lamMoiBang();
		}
	}

	public List<KhachHang> hienThiNutXoaNhieu() {
		List<KhachHang> danhSachChon = tblKhachHang.getItems().stream().filter(KhachHang::isSelected).toList();

		if (danhSachChon.size() >= 2) {
			btnXoaTatCa.setVisible(true);
			return danhSachChon;
		}

		else {
			btnXoaTatCa.setVisible(false);
			return null;
		}
	}

	public void lamMoiBang() {
		listKH = khDAO.layListKhachHang();
		setGiaTriMacDinh();
		locTatCa();
	}

	public void setGiaTriMacDinh() {
		txtTenKH.setText("");
		txtSdt.setText("");
	}
	
//	public void xoaNhieu() {
//		List<KhachHang> danhSachChon = tblKhachHang.getItems()
//		        .stream()
//		        .filter(KhachHang::isSelected)
//		        .toList();
//		
//		if(danhSachChon.size() >= 2) {
//			btnLamMoi.setVisible(true);
//		} else {
//			btnLamMoi.setVisible(false);
//		}
//	}

	public void chuyenDenTrangChiTiet() {

		List<KhachHang> danhSachChon = tblKhachHang.getItems().stream().filter(KhachHang::isSelected).toList();

		if (danhSachChon.isEmpty()) {
			toolCtrl.hienThiThongBao("Lỗi", "Vui lòng chọn 1 khách hàng để xem chi tiết.", false);
			return;
		}

		if (danhSachChon.size() > 1) {
			toolCtrl.hienThiThongBao("Lỗi", "Chỉ được chọn 1 khách hàng để xem chi tiết.", false);
			return;
		}

		KhachHang khachHang = danhSachChon.get(0);

		if (trangChuQLCtrl != null) {
			ChiTietKhachHangCtrl ctKHCtrl = trangChuQLCtrl.doiCenterPane("/fxml/ChiTietKhachHang.fxml");
			ctKHCtrl.hienThiThongTin(khachHang);
			ctKHCtrl.setTrangChuQLCtrl(trangChuQLCtrl);

		} else if (trangChuNVCtrl != null) {
			ChiTietKhachHangCtrl ctKHCtrl = trangChuNVCtrl.doiCenterPane("/fxml/ChiTietKhachHang.fxml");
			ctKHCtrl.hienThiThongTin(khachHang);
			ctKHCtrl.setTrangChuNVCtrl(trangChuNVCtrl);

		} else {
			toolCtrl.hienThiThongBao("Lỗi hệ thống", "Controller cha chưa được set. Vui lòng kiểm tra cách load FXML.",
					false);
		}

	}

	public void locTatCa() {
		String sdt = toolCtrl.chuyenSoDienThoai(txtSdt.getText().trim().toLowerCase());
		String tenNhap = txtTenKH.getText().trim().toLowerCase();

		ObservableList<KhachHang> listLoc = listKH.filtered(kh -> {
			boolean hopTrangThai = true;
			boolean hopSdt = true;
			boolean hopTen = true;

			if (btnLichSuXoa.getText().equals("Lịch sử xóa")) {
				hopTrangThai = kh.isTrangThai();
			} else {
				hopTrangThai = !kh.isTrangThai();
			}

			// Lọc theo số điện thoại
			if (!sdt.isEmpty()) {
				hopSdt = kh.getSdt() != null && kh.getSdt().toLowerCase().contains(sdt);
			}

			// Lọc theo tên khách hàng
			if (!tenNhap.isEmpty()) {
				hopTen = kh.getTenKH() != null && kh.getTenKH().toLowerCase().contains(tenNhap);
			}

			return hopTrangThai && hopSdt && hopTen;
		});

		setDataChoTable(listLoc);
	}

	public void xoaNhieu() {
		List<KhachHang> listkh = hienThiNutXoaNhieu();

		if (listkh.isEmpty()) {
			toolCtrl.hienThiThongBao("Thông báo", "Vui lòng chọn ít nhất một nhân viên để xoá.", false);
			return;
		}
		if (tblDangLam) {
			// === TRƯỜNG HỢP ĐANG LÀM → XOÁ ===
			boolean xacNhan = toolCtrl.hienThiXacNhan("Xác nhận xoá",
					"Bạn có chắc muốn xoá " + listkh.size() + " nhân viên đã chọn?");
			if (!xacNhan)
				return;

			for (KhachHang kh : listkh) {
				if (kh.isTrangThai()) {
					xoaKhachHang(kh);
				}
			}
			toolCtrl.hienThiThongBao("Thành công", "Đã xoá thành công " + listkh.size() + " khách hàng.", true);
		} else {
			// === TRƯỜNG HỢP ĐÃ NGHỈ → KHÔI PHỤC ===
			boolean xacNhan = toolCtrl.hienThiXacNhan("Xác nhận khôi phục",
					"Bạn có chắc muốn khôi phục " + listkh.size() + " nhân viên đã chọn?");
			if (!xacNhan)
				return;

			for (KhachHang kh : listkh) {
				if (!kh.isTrangThai()) {
					hoanTacKhachHang(kh);
				}
			}
			toolCtrl.hienThiThongBao("Thành công", "Đã khôi phục thành công " + listkh.size() + " khách hàng.", true);
		}

		// Ẩn nút Xóa/KHôi phục và làm mới bảng
		btnXoaTatCa.setVisible(false);
		lamMoiBang();
	}

	public void xoaKhachHang(KhachHang kh) {
		if (khDAO.xoaKhachHang(kh.getMaKH())) {
			kh.setTrangThai(false);
		} else {
			toolCtrl.hienThiThongBao("Lỗi xóa khách hàng", "Không thể xóa khách hàng tên: " + kh.getTenKH(), false);
		}
		lamMoiBang();
	}

	public void hoanTacKhachHang(KhachHang kh) {
		if (khDAO.khoiPhucKhachHang(kh.getMaKH())) {
			kh.setTrangThai(true);
		} else {
			toolCtrl.hienThiThongBao("Lỗi khôi phục khách hàng", "Không thể khôi phục khách hàng tên: " + kh.getTenKH(),
					false);
		}
		lamMoiBang();
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.trangChuQLCtrl = trangChuQLCtrl;
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.trangChuNVCtrl = trangChuNVCtrl;
	}

}
