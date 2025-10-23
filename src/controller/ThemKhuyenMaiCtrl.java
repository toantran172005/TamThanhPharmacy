package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import dao.ThuocDAO;
import entity.KhuyenMai;
import entity.Thuoc;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class ThemKhuyenMaiCtrl {
	
	@FXML
	public TableView<Object[]> tblThuocKhuyenMai;
	@FXML
	public TableColumn<Object[], Boolean> colSelect;
	@FXML
	public TableColumn<Object[], String> colMaThuoc;
	@FXML
	public TableColumn<Object[], String> colTenThuoc;
	@FXML
	public TableColumn<Object[], String> colLoaiThuoc;
	@FXML
	public TableColumn<Object[], String> colDonVi;
	@FXML
	public TableColumn<Object[], Double> colDonGia;

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
	@FXML
	public CheckBox chkSelect;
	
	public ToolCtrl tool = new ToolCtrl();
	public ThuocDAO thuocDAO = new ThuocDAO();
	public ObservableList<Object[]> list = FXCollections.observableArrayList();
	private final Map<Object[], BooleanProperty> select = new HashMap<>();
	
	public void initialize() {
		list = thuocDAO.layDanhSachThuocChoKM();
		tool.dinhDangDatePicker(dpNgayBD);
		tool.dinhDangDatePicker(dpNgayKT);
		ganSuKien();
		setDataChoTable(list);
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
	
	public void setDataChoTable(ObservableList<Object[]> list) {
		// Checkbox
		setUpColCheckBox();
		// Các cột data
		 colMaThuoc.setCellValueFactory(cellData ->
         	new SimpleStringProperty(cellData.getValue()[0].toString()));
		 colTenThuoc.setCellValueFactory(cellData ->
         	new SimpleStringProperty(cellData.getValue()[1].toString()));
		 colLoaiThuoc.setCellValueFactory(cellData ->
		 	new SimpleStringProperty(cellData.getValue()[2].toString()));
		 colDonVi.setCellValueFactory(cellData ->
		 	new SimpleStringProperty(cellData.getValue()[3].toString()));
		 colDonGia.setCellValueFactory(cellData ->
         	new SimpleDoubleProperty((Double) cellData.getValue()[4]).asObject());
		// Đưa data lên table
		 tblThuocKhuyenMai.setItems(list);
	}
	
	
	public void setUpColCheckBox() {		
		//CellValueFactory để quản lý trạng thái từng dòng
		colSelect.setCellValueFactory(cell -> 
				select.computeIfAbsent(cell.getValue(), r -> new SimpleBooleanProperty(false)));
		
		//Tạo ô checkbox cho từng dòng
		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
		
		//Cho phép click vào từng dòng để chọn checkbox
		tblThuocKhuyenMai.setRowFactory(tv -> {
			TableRow<Object[]> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty()) {
					Object[] item = row.getItem();
					BooleanProperty prop = select.get(item);
					if(prop != null) prop.set(!prop.get());
				}
			});
			return row;
		});
		
		//Click chọn/ bỏ chọn tất cả
		chkSelect.selectedProperty().addListener((obs, oldVal, newVal) -> {
			for(Object[] item : tblThuocKhuyenMai.getItems()) {
				BooleanProperty prop = select.get(item);
				if(prop != null && prop.get() != newVal) prop.set(newVal);
			}
		});
		
		//Bỏ chọn tất cả khi có 1 dòng bỏ chón
		tblThuocKhuyenMai.getItems().addListener((ListChangeListener<Object[]>) change -> {
	        while (change.next()) {
	            if (change.wasAdded()) {
	                for (Object[] item : change.getAddedSubList()) {
	                    select.computeIfAbsent(item, r -> new SimpleBooleanProperty(false))
	                            .addListener((o, ov, nv) -> {
	                                boolean allSelected = tblThuocKhuyenMai.getItems().stream()
	                                        .allMatch(i -> select.get(i).get());
	                                chkSelect.setSelected(allSelected);
	                            });
	                }
	            }
	        }
	    });
		
	}
	
}






























