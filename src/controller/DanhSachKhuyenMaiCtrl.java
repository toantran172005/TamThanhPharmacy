package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import dao.KhuyenMaiDAO;
import entity.KhachHang;
import entity.KhuyenMai;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DanhSachKhuyenMaiCtrl {
	
	@FXML
	public TableView<KhuyenMai> tblKhuyenMai;
	@FXML
	public TableColumn<KhuyenMai, Boolean> colSelect;
	@FXML
	public TableColumn<KhuyenMai, String> colMaKM;
	@FXML
	public TableColumn<KhuyenMai, String> colTenKM;
	@FXML
	public TableColumn<KhuyenMai, String> colPhuongThucKM;
	@FXML
	public TableColumn<KhuyenMai, Integer> colMucKM;
	@FXML
	public TableColumn<KhuyenMai, String> colNgayBD;
	@FXML
	public TableColumn<KhuyenMai, String> colNgayKT;
	@FXML
	public TableColumn<KhuyenMai, String> colTrangThai;
	@FXML 
	public TableColumn<KhuyenMai, Void> colHoatDong;
	
	@FXML
	public Button btnThem;
	
	@FXML
	public Button btnXemChiTiet;
	@FXML
	public Button btnLamMoi;
	public ComboBox<String> cmbTrangThai;
	@FXML 
	public DatePicker dpNgay;
	
	public TrangChuQLCtrl trangChuQLCtrl = new TrangChuQLCtrl();
	public TextField txtTenKM;
	
	public KhuyenMaiDAO kmDao = new KhuyenMaiDAO();
	public ToolCtrl tool = new ToolCtrl();
	
	public ObservableList<KhuyenMai> listKM = FXCollections.observableArrayList();
	
	public void initialize() {
		listKM = kmDao.layDanhSachKM();
		setDataChoTable(listKM);	
		setItemsChoCombobox();
		ganSuKien();
	}
	
	public void ganSuKien() {
		cmbTrangThai.setOnAction(event ->{
			locTheoTrangThai();
		});
		
		dpNgay.setOnAction(event -> {
			locTheoNgay();
		});
		
		btnLamMoi.setOnAction(event->{
			lamMoiBang();
		});
		
		btnXemChiTiet.setOnAction(event -> {
			chuyenDenChiTietKM();
		});
	}
	
	public void setItemsChoCombobox() {
		cmbTrangThai.getItems().addAll("Tất cả","Đã kết thúc", "Hoạt động");
		cmbTrangThai.setValue("Tất cả");
	}

	public void chuyenDenChiTietKM() {
		List<KhuyenMai> danhSachChon = tblKhuyenMai.getItems()
		        .stream()
		        .filter(KhuyenMai::isSelected)
		        .toList();

		if (danhSachChon.isEmpty()) {
		    tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 khách hàng để xem chi tiết.", false);
		    return;
		}

		if (danhSachChon.size() > 1) {
		    tool.hienThiThongBao("Lỗi", "Chỉ được chọn 1 khách hàng để xem chi tiết.", false);
		    return;
		}

		KhuyenMai km = danhSachChon.get(0);

		if (trangChuQLCtrl != null) {
	        ChiTietKhuyenMaiCtrl ctKMCtrl = trangChuQLCtrl.doiCenterPane("/fxml/ChiTietKhuyenMaiThuoc.fxml");
	        ctKMCtrl.hienThiThongTin(km);
	        ctKMCtrl.setTrangChuQLCtrl(trangChuQLCtrl);

	    } else {
	        tool.hienThiThongBao("Lỗi hệ thống",
	                "Controller cha chưa được set. Vui lòng kiểm tra cách load FXML.", false);
	    }
	}
	
//	public void hienThiTrangTaoKM() {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemKhuyenMai.fxml"));
//			Parent root = loader.load();
//			Stage stage = new Stage();
//			stage.setTitle("Thêm khuyến mãi");
//			stage.setScene(new Scene(root));
//			stage.sizeToScene();
//			stage.centerOnScreen();
//			stage.setResizable(false);
//			
//			//stage.initModality(Modality.APPLICATION_MODAL); //Chặn thao tác trên màn hình chính khi pop up đang mở
//			
//			Stage currentStage = (Stage) btnTaoKM.getScene().getWindow();
//			stage.initOwner(currentStage);
//			
//			stage.showAndWait();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void setDataChoTable(ObservableList<KhuyenMai> list) {
		// Checkbox
		setUpColCheckBox();
		// Các cột data
		colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
		colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
		colPhuongThucKM.setCellValueFactory(new PropertyValueFactory<>("phuongThucKM"));
		colMucKM.setCellValueFactory(new PropertyValueFactory<>("mucKM"));
		colNgayBD.setCellValueFactory(cellData -> {
			LocalDate date = cellData.getValue().getNgayBD();
			return new SimpleStringProperty(tool.dinhDangLocalDate(date));
		});
		colNgayKT.setCellValueFactory(cellData -> {
			LocalDate date = cellData.getValue().getNgayKT();
			return new SimpleStringProperty(tool.dinhDangLocalDate(date));
		});
		colTrangThai.setCellValueFactory(cellData -> {
			KhuyenMai km = cellData.getValue();
			return new SimpleStringProperty(setTrangThai(km));
		});
		
		// Cột hoạt động
		setupColHoatDong();
		// Đưa data lên table
		tblKhuyenMai.setItems(list);
	}
	
	
	//Hàm lọc dữ liệu theo ngày
	public void locTheoNgay() {
		LocalDate ngay = dpNgay.getValue();
		
		if(ngay == null) {
			setDataChoTable(listKM);
			return;
		}
		ObservableList<KhuyenMai> listLoc = listKM.filtered(km -> 
        (ngay.isAfter(km.getNgayBD()) || ngay.isEqual(km.getNgayBD())) &&
        (ngay.isBefore(km.getNgayKT()) || ngay.isEqual(km.getNgayKT()))
    );

    setDataChoTable(listLoc);
		
	}
	
	public void locTheoTrangThai() {
		String trangThai = cmbTrangThai.getValue();

		if (trangThai == null || trangThai.isEmpty()) {
			return;
		}

		if (trangThai.equals("Tất cả")) {
			setDataChoTable(listKM);
			return;
		}

		ObservableList<KhuyenMai> listLoc = listKM.filtered(km-> {
			LocalDate today = LocalDate.now();
			LocalDate ngayKT = km.getNgayKT();
			if (trangThai.equals("Hoạt động")) {
				return !today.isAfter(ngayKT);
			} else if (trangThai.equals("Đã kết thúc")) {
				return today.isAfter(ngayKT);
			}
			return true;
		});
		setDataChoTable(listLoc);
	}


	public void setUpColCheckBox() {
		colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		colSelect.setCellFactory(tc -> {
			CheckBoxTableCell<KhuyenMai, Boolean> cell = new CheckBoxTableCell<>() {
				@Override
				public void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty) {
						TableRow<KhuyenMai> row = getTableRow();
						if (row != null) {
							KhuyenMai km = row.getItem();
							if (km != null) {
								km.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
									if (isNowSelected) {
										tblKhuyenMai.getSelectionModel().select(km);
									} else {
										tblKhuyenMai.getSelectionModel()
												.clearSelection(tblKhuyenMai.getItems().indexOf(km));
									}
								});
							}
						}
					}
				}
			};
			return cell;
		});

		tblKhuyenMai.setRowFactory(tv -> {
			TableRow<KhuyenMai> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					KhuyenMai km = row.getItem();
					km.setSelected(!km.isSelected());
				}
			});
			return row;
		});
	}

	public void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<KhuyenMai, Void>() {
			private final Button btn = new Button();

			{
				btn.getStyleClass().add("btnXoaVaHoanTac");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> {
					KhuyenMai km = getTableView().getItems().get(getIndex());

					if (km.isTrangThai()) { // xử lý xóa 

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
					KhuyenMai km = getTableView().getItems().get(getIndex());
					if (km != null) {
						btn.setText(km.isTrangThai() ? "Xóa" : "Hoàn Tác");
					}
					setGraphic(btn);
				}
			}
		});
	}
	
//	public void chuyenDenTrangChiTiet() {
//
//		List<KhuyenMai> danhSachChon = tblKhuyenMai.getItems()
//		        .stream()
//		        .filter(KhuyenMai::isSelected)
//		        .toList();
//
//		if (danhSachChon.isEmpty()) {
//		    tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 khách hàng để xem chi tiết.", false);
//		    return;
//		}
//
//		if (danhSachChon.size() > 1) {
//		    tool.hienThiThongBao("Lỗi", "Chỉ được chọn 1 khách hàng để xem chi tiết.", false);
//		    return;
//		}
//
//		KhuyenMai KhuyenMai = danhSachChon.get(0);
//
//		if (trangChuQLCtrl != null) {
//	        ChiTietKhuyenMaiCtrl ctKHCtrl = trangChuQLCtrl.doiCenterPane("/fxml/ChiTietKhuyenMai.fxml");
//	        ctKHCtrl.hienThiThongTin(KhuyenMai);
//	        ctKHCtrl.setTrangChuQLCtrl(trangChuQLCtrl);
//
//	    } else if (trangChuNVCtrl != null) {
//	        ChiTietKhuyenMaiCtrl ctKHCtrl = trangChuNVCtrl.doiCenterPane("/fxml/ChiTietKhuyenMai.fxml");
//	        ctKHCtrl.hienThiThongTin(KhuyenMai);
//	        ctKHCtrl.setTrangChuNVCtrl(trangChuNVCtrl);
//
//	    } else {
//	        tool.hienThiThongBao("Lỗi hệ thống",
//	                "Controller cha chưa được set. Vui lòng kiểm tra cách load FXML.", false);
//	    }


	public void lamMoiBang() {
		listKM = kmDao.layDanhSachKM();
		setDataChoTable(listKM);
		cmbTrangThai.setValue("Tất cả");
		dpNgay.setValue(null);
	}
	
	public String setTrangThai(KhuyenMai km) {
		LocalDate today = LocalDate.now();
		LocalDate ngayKT = km.getNgayKT();
		
		String display = today.isAfter(ngayKT) ? "Đã kết thúc" : "Hoạt động";
		return display;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.trangChuQLCtrl = trangChuQLCtrl;
	}

}
