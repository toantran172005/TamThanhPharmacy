package controller;

import java.time.LocalDate;

import entity.KhuyenMai;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class ThemKhuyenMaiCtrl {
	
	@FXML
	public TableView<KhuyenMai> tblThuocKhuyenMai;
	@FXML
	public TableColumn<KhuyenMai, Boolean> colSelect;
	@FXML
	public TableColumn<KhuyenMai, String> colMaThuoc;
	@FXML
	public TableColumn<KhuyenMai, String> colTenThuoc;
	@FXML
	public TableColumn<KhuyenMai, String> colLoaiThuoc;
	@FXML
	public TableColumn<KhuyenMai, String> colDonVi;
	@FXML
	public TableColumn<KhuyenMai, String> colDonGia;

	@FXML
	public Button btnTru;
	@FXML
	public Button btnCong;
	@FXML
	public Button btnThem;
	@FXML
	public Button btnLamMoi;
	@FXML
	public TextField txtTenKhuyenMai;
	@FXML
	public TextField txtPhuongThuc;
	@FXML
	public TextField txtMucKhuyenMai;
	@FXML
	public DatePicker dpNgayBD;
	@FXML
	public DatePicker dpNgayKT;
	
	public void initialize() {
		ganSuKien();
	}
	
	public void ganSuKien() {
		
		btnCong.setOnAction(event -> {
			tangGiamSoTrongTextField(true);
		});
		
		btnTru.setOnAction(event -> {
			tangGiamSoTrongTextField(false);
		});
	}
	
	public void tangGiamSoTrongTextField(boolean count) {
		try {
			int value = 0;
			
			if(!txtMucKhuyenMai.getText().isEmpty()) {
				value = Integer.parseInt(txtMucKhuyenMai.getText());
			} else {
				value = 0;
			}
			
			if(count) {
				value++;
				txtMucKhuyenMai.setText(String.valueOf(value));
			} else {
				value--;
				txtMucKhuyenMai.setText(String.valueOf(value));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setDataChoTable(ObservableList<KhuyenMai> list) {
		// Checkbox
		setUpColCheckBox();
		// Các cột data
//		colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
//		colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
//		colPhuongThucKM.setCellValueFactory(new PropertyValueFactory<>("phuongThucKM"));
//		colMucKM.setCellValueFactory(new PropertyValueFactory<>("mucKM"));
//		colNgayBD.setCellValueFactory(cellData -> {
//			LocalDate date = cellData.getValue().getNgayBD();
//			return new SimpleStringProperty(tool.dinhDangLocalDate(date));
//		});
//		colNgayKT.setCellValueFactory(cellData -> {
//			LocalDate date = cellData.getValue().getNgayKT();
//			return new SimpleStringProperty(tool.dinhDangLocalDate(date));
//		});
//		colTrangThai.setCellValueFactory(cellData -> {
//			KhuyenMai km = cellData.getValue();
//			return new SimpleStringProperty(setTrangThai(km));
//		});
		
		// Cột hoạt động
		setupColHoatDong();
		// Đưa data lên table
		tblKhuyenMai.setItems(list);
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
	
}
