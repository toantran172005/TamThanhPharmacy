package controller;

import dao.KhachHangDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.KhachHang;

import javafx.beans.property.ReadOnlyDoubleWrapper;

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
    public TableColumn<KhachHang, Double> colTongTien;
    @FXML
    public TableColumn<KhachHang, String> colTrangThai;
    @FXML
    public TableColumn<KhachHang, Void> colHoatDong;
    
    public KhachHangDAO khDAO = new KhachHangDAO();
    
   public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();
	
	public void initialize() {
		listKH = khDAO.layListKhachHang();
		setItemCmbTrangThai();
		setDataChoTable();
	}
	
	public void setDataChoTable() {
		
		// Checkbox
		colSelect.setCellValueFactory(new PropertyValueFactory<>("selected"));
		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
		
		// Data
		colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
		colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
		colSdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
		colTuoi.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
		colTrangThai.setCellValueFactory(cellData -> {
		    KhachHang kh = cellData.getValue();
		    Boolean trangThai = kh.isTrangThai();
		    String display = "0".equals(trangThai) ? "Ngừng hoạt động" : "Hoạt động";
		    return new ReadOnlyObjectWrapper(display);
		});
		colTongDonHang.setCellValueFactory(cellData -> {

			return new SimpleIntegerProperty(10).asObject();
			
		});
		
		// Cột hoạt động
		
		
		tblKhachHang.setItems(listKH);
	}
	
	public void setItemCmbTrangThai() {
		cmbTrangThai.getItems().addAll(
		        "Hoạt động",
		        "Ngừng hoạt động"
		    );
	}
	
}
