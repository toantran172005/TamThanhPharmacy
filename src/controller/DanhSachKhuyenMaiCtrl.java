package controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.swing.text.TabableView;

import dao.KhuyenMaiDAO;
import entity.KhachHang;
import entity.KhuyenMai;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	public Button btnTaoKM;
	@FXML
	public Button btnXemChiTiet;
	@FXML
	public TextField txtTenKM;
	
	public KhuyenMaiDAO kmDao = new KhuyenMaiDAO();
	public ToolCtrl tool = new ToolCtrl();
	
	public ObservableList<KhuyenMai> listKM = FXCollections.observableArrayList();
	
	public void initialize() {
		listKM = kmDao.layDanhSachKM();
		setDataChoTable(listKM);
	}

	public void chuyenDenChiTietKM() {
		
	}
	
	public void hienThiTrangTaoKM() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemKhuyenMai.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Thêm khuyến mãi");
			stage.setScene(new Scene(root));
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.setResizable(false);
			
			//stage.initModality(Modality.APPLICATION_MODAL); //Chặn thao tác trên màn hình chính khi pop up đang mở
			
			Stage currentStage = (Stage) btnTaoKM.getScene().getWindow();
			stage.initOwner(currentStage);
			
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
		tblKhuyenMai.setEditable(true);
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

	public void lamMoiBang() {
		listKM = kmDao.layDanhSachKM();
		txtTenKM.setText("");
	}
	
	public String setTrangThai(KhuyenMai km) {
		LocalDate today = LocalDate.now();
		LocalDate ngayKT = km.getNgayKT();
		
		String display = today.isAfter(ngayKT) ? "Kết thúc" : "Đang áp dụng";
		return display;
	}
	
}
