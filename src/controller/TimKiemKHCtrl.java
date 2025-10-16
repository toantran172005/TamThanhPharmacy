package controller;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dao.KhachHangDAO;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.BooleanProperty;

public class TimKiemKHCtrl {

	@FXML
	public ComboBox<String> cmbTrangThai;

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
	public TableColumn<KhachHang, Integer> colTongDonHang;
	@FXML
	public TableColumn<KhachHang, String> colTongTienHang;
	@FXML
	public TableColumn<KhachHang, String> colTrangThai;
	@FXML
	public TableColumn<KhachHang, Void> colHoatDong;
	@FXML
	public Button btnLamMoi, btnXemChiTiet;

	@FXML
	public TextField txtTenKH, txtSdt;

	public TrangChuQLCtrl trangChuQLCtrl;
	public TrangChuNVCtrl trangChuNVCtrl;

	public KhachHangDAO khDAO = new KhachHangDAO();
	public ToolCtrl toolCtrl = new ToolCtrl();

	public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();

	public ObservableList<KhachHang> listKHThongKe = FXCollections.observableArrayList(); // gồm mã, tên, tổng đơn, tổng tiền

	public void initialize() {
		listKH = khDAO.layListKhachHang();
		listKHThongKe = khDAO.layListKHThongKe();
		setItemCmbTrangThai();
		locTatCa();
		setUpTextFieldVaButton();
	}

	public void setUpTextFieldVaButton() {
		txtTenKH.setOnAction(event -> locTatCa()); // txt tìm kiếm theo tên khách hàng
		txtSdt.setOnAction(event -> locTatCa()); // txt tìm kiếm theo số điện thoại
		btnLamMoi.setOnAction(event -> lamMoiBang()); // btn làm mới lại table
		btnXemChiTiet.setOnAction(event -> chuyenDenTrangChiTiet());
		cmbTrangThai.setOnAction(event -> locTatCa());
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
		colTrangThai.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleStringProperty(setTrangThai(kh));
		});
		colTongDonHang.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleIntegerProperty(tinhTongDonHang(kh)).asObject();
		});
		colTongTienHang.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleStringProperty(tinhTongTien(kh));
		});
		// Cột hoạt động
		setupColHoatDong();
		// Đưa data lên table
		tblKhachHang.setEditable(true);
		tblKhachHang.setItems(list);
	}

	public void setUpColCheckBox() {
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

					} else { // xử lý hoàn tác 

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

	public void lamMoiBang() {
		listKH = khDAO.layListKhachHang();
		setGiaTriMacDinh();
		locTatCa();
	}
	
	public void setGiaTriMacDinh() {
		cmbTrangThai.setValue("Hoạt động");
		txtTenKH.setText("");
		txtSdt.setText("");
	}

	public void chuyenDenTrangChiTiet() {

		List<KhachHang> danhSachChon = tblKhachHang.getItems()
		        .stream()
		        .filter(KhachHang::isSelected)
		        .toList();

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
	        toolCtrl.hienThiThongBao("Lỗi hệ thống",
	                "Controller cha chưa được set. Vui lòng kiểm tra cách load FXML.", false);
	    }

	}

	public void locTatCa() {
	    String trangThai = cmbTrangThai.getValue();
	    String sdt = toolCtrl.chuyenSoDienThoai(txtSdt.getText().trim().toLowerCase());
	    String tenNhap = txtTenKH.getText().trim().toLowerCase();

	    ObservableList<KhachHang> listLoc = listKH.filtered(kh -> {
	        boolean hopTrangThai = true;
	        boolean hopSdt = true;
	        boolean hopTen = true;

	        //  Lọc theo trạng thái 
	        if (trangThai != null && !trangThai.equals("Tất cả")) {
	            if (trangThai.equals("Hoạt động")) {
	                hopTrangThai = kh.isTrangThai();
	            } else if (trangThai.equals("Ngừng hoạt động")) {
	                hopTrangThai = !kh.isTrangThai();
	            }
	        }

	        //  Lọc theo số điện thoại 
	        if (!sdt.isEmpty()) {
	            hopSdt = kh.getSdt() != null && kh.getSdt().toLowerCase().contains(sdt);
	        }

	        //  Lọc theo tên khách hàng 
	        if (!tenNhap.isEmpty()) {
	            hopTen = kh.getTenKH() != null && kh.getTenKH().toLowerCase().contains(tenNhap);
	        }

	        return hopTrangThai && hopSdt && hopTen;
	    });

	    setDataChoTable(listLoc);
	}


	public String tinhTongTien(KhachHang kh) {
		KhachHang tempKH = null;
		for (KhachHang KH : listKHThongKe) {
			if (KH.getMaKH().equalsIgnoreCase(kh.getMaKH())) {
				tempKH = KH;
				break;
			}
		}
		return toolCtrl.dinhDangVND(tempKH.getTongTien());
	}

	public String setTrangThai(KhachHang kh) {
		Boolean trangThai = true;
		String display = !trangThai.equals(kh.isTrangThai()) ? "Ngừng hoạt động" : "Hoạt động";
		return display;
	}

	public int tinhTongDonHang(KhachHang kh) {
		KhachHang tempKH = null;
		for (KhachHang KH : listKHThongKe) {
			if (KH.getMaKH().equalsIgnoreCase(kh.getMaKH())) {
				tempKH = KH;
				break;
			}
		}
		return tempKH.getTongDonHang();
	}

	public void setItemCmbTrangThai() {
		cmbTrangThai.getItems().addAll("Tất cả", "Hoạt động", "Ngừng hoạt động");
		cmbTrangThai.setValue("Hoạt động");
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.trangChuQLCtrl = trangChuQLCtrl;
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.trangChuNVCtrl = trangChuNVCtrl;
	}

}
