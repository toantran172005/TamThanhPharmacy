package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.HoaDonDAO;
import entity.KhachHang;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;

public class ThongKeHoaDonCtrl {
	public ToolCtrl tool = new ToolCtrl();
	public HoaDonDAO hdDAO = new HoaDonDAO();
	Map<String, Double> tongTienMap = new HashMap<>();
	Map<String, Integer> tongDonMap = new HashMap<>();

	@FXML
	public ScrollPane scrTKHD;
	@FXML
	public TableView<KhachHang> tblThongKeHD;
	@FXML
	public TableColumn<KhachHang, Integer> colSTT;
	@FXML
	public TableColumn<KhachHang, String> colTenKH;
	@FXML
	public TableColumn<KhachHang, String> colSdt;
	@FXML
	public TableColumn<KhachHang, Integer> colSoHD;
	@FXML
	public TableColumn<KhachHang, String> colTongMua;
	@FXML
	public Button btnThongKe, btnXuatExcel, btnInBaoCao, btnLamMoi;
	@FXML
	public Label lblTongDoanhThu, lblTongHD, lblTBDT;
	@FXML
	public DatePicker dpNgayBatDau, dpNgayKetThuc;
	@FXML
	public LineChart<String, Number> lineChartDTTheoNgay;
	@FXML
	public CategoryAxis xAxis;
	@FXML
	public NumberAxis yAxis;
	@FXML
	public ComboBox<String> cmbTopTK;

	public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();

	public void initialize() {
		setScrCuonNhanh();
		dinhDangNgayDatePicker();
		setHoatDong();
		setItemChoComboBox();
		cmbTopTK.setOnAction(e -> setDataChoTable());
		btnLamMoi.setOnAction(e-> lamMoi());
	}

	public void setDataChoTable() {
		if (listKH == null || listKH.isEmpty()) {
			tblThongKeHD.getItems().clear();
			return;
		}

		ObservableList<KhachHang> tempList = FXCollections.observableArrayList(listKH);

		// Sắp xếp listKH theo tổng tiền mua giảm dần
		tempList.sort((kh1, kh2) -> Double.compare(tongTienMap.getOrDefault(kh2.getMaKH(), 0.0),
				tongTienMap.getOrDefault(kh1.getMaKH(), 0.0)));

		// Giới hạn chỉ 10 khách hàng đầu tiên
		if (cmbTopTK.getValue() != null) {
			int top = Integer.parseInt(cmbTopTK.getValue());
			if (tempList.size() > top) {
				tempList = FXCollections.observableArrayList(tempList.subList(0, top));
			}
		}

		colSTT.setCellValueFactory(cellData -> {
			return new SimpleIntegerProperty(tblThongKeHD.getItems().indexOf(cellData.getValue()) + 1).asObject();
		});

		colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
		colSdt.setCellValueFactory(cellData -> {
			return new SimpleStringProperty(tool.chuyenSoDienThoai(cellData.getValue().getSdt()));
		});
		colTongMua.setCellValueFactory(cellData -> new SimpleStringProperty(
				tool.dinhDangVND(tongTienMap.getOrDefault(cellData.getValue().getMaKH(), 0.0))));
		colSoHD.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tongDonMap.getOrDefault(cellData.getValue().getMaKH(), 0))
						.asObject());

		tblThongKeHD.setItems(tempList);

	}

	public void setScrCuonNhanh() {
		scrTKHD.addEventFilter(ScrollEvent.SCROLL, event -> {
			double deltaY = event.getDeltaY() * 5;
			scrTKHD.setVvalue(scrTKHD.getVvalue() - deltaY / scrTKHD.getContent().getBoundsInLocal().getHeight());
			event.consume();
		});

		tblThongKeHD.setOnScroll(e -> {
			double deltaY = e.getDeltaY() * 3;
			tblThongKeHD.scrollTo((int) (tblThongKeHD.getFocusModel().getFocusedIndex() - deltaY / 40));
		});

	}

	public void dinhDangNgayDatePicker() {
		tool.dinhDangDatePicker(dpNgayBatDau);
		tool.dinhDangDatePicker(dpNgayKetThuc);
	}

	public void setHoatDong() {
		btnThongKe.setOnAction(event -> thongKeKhachHang());
	}

	public void thongKeKhachHang() {
		LocalDate ngayBD = dpNgayBatDau.getValue();
		LocalDate ngayKT = dpNgayKetThuc.getValue();

		if (ngayBD == null) {
			tool.hienThiThongBao("Ngày bắt đầu rỗng", "Vui lòng chọn ngày bắt đầu!", false);
			return;
		} else if (ngayKT == null) {
			tool.hienThiThongBao("Ngày kết thúc rỗng", "Vui lòng chọn ngày kết thúc!", false);
			return;
		} else if (ngayBD.isAfter(ngayKT)) {
			tool.hienThiThongBao("Lỗi ngày", "Ngày bắt đầu phải trước ngày kết thúc!", false);
			return;
		}

		listKH = hdDAO.layListKHThongKe(ngayBD, ngayKT);
		for (KhachHang kh : listKH) {
			double tongTien = hdDAO.layTongTien(kh.getMaKH());
			int tongDon = hdDAO.layTongDonHang(kh.getMaKH());
			tongTienMap.put(kh.getMaKH(), tongTien);
			tongDonMap.put(kh.getMaKH(), tongDon);
		}
		setDataChoLabel();
		setDataChoLineChart();
		setDataChoTable();
	}

	public void setDataChoLineChart() {
		lineChartDTTheoNgay.getData().clear();

		LocalDate ngayBD = dpNgayBatDau.getValue();
		LocalDate ngayKT = dpNgayKetThuc.getValue();

		if (ngayBD == null || ngayKT == null || ngayBD.isAfter(ngayKT))
			return;

		// Tạo map đầy đủ các ngày trong khoảng
		Map<LocalDate, Double> doanhThuTheoNgay = new LinkedHashMap<>();
		LocalDate current = ngayBD;
		while (!current.isAfter(ngayKT)) {
			doanhThuTheoNgay.put(current, 0.0); // mặc định 0 nếu không có doanh thu
			current = current.plusDays(1);
		}

		// Lấy dữ liệu thực tế từ DB
		Map<LocalDate, Double> dbData = hdDAO.layDoanhThuTheoNgay(ngayBD, ngayKT);
		for (Map.Entry<LocalDate, Double> entry : dbData.entrySet()) {
			doanhThuTheoNgay.put(entry.getKey(), entry.getValue());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
		List<String> ngayLabels = doanhThuTheoNgay.keySet().stream().map(d -> d.format(formatter)).toList();

		// Cấu hình CategoryAxis để hiển thị tất cả ngày
		xAxis.setAutoRanging(false);
		xAxis.setCategories(FXCollections.observableArrayList(ngayLabels));
		xAxis.setTickLabelRotation(45);

		// 5Tạo series và add dữ liệu
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		for (Map.Entry<LocalDate, Double> entry : doanhThuTheoNgay.entrySet()) {
			series.getData().add(new XYChart.Data<>(entry.getKey().format(formatter), entry.getValue()));
		}

		lineChartDTTheoNgay.getData().add(series);

		// Ẩn các node tròn
		Platform.runLater(() -> {
			for (XYChart.Data<String, Number> d : series.getData()) {
				if (d.getNode() != null)
					d.getNode().setVisible(false);
			}
		});
	}

	public void setDataChoLabel() {
		int tongSoHoaDon = tongDonMap.values().stream().mapToInt(Integer::intValue).sum();
		lblTongHD.setText(String.valueOf(tongSoHoaDon));

		double tongDoanhThu = tongTienMap.values().stream().mapToDouble(Double::doubleValue).sum();
		lblTongDoanhThu.setText(tool.dinhDangVND(tongDoanhThu));

		lblTBDT.setText(tool.dinhDangVND(tongDoanhThu / (tongSoHoaDon == 0 ? 1 : tongSoHoaDon)));
	}

	public void setItemChoComboBox() {
		cmbTopTK.getItems().addAll("1", "5", "10", "20");
		cmbTopTK.setValue("1");
	}

	public void lamMoi() {
		tongTienMap.clear();
		tongDonMap.clear();
		if (listKH != null) listKH.clear();
		tblThongKeHD.getItems().clear();
		lineChartDTTheoNgay.getData().clear();
		lblTongDoanhThu.setText("0 ₫");
		lblTongHD.setText("0");
		lblTBDT.setText("0 ₫");
		dpNgayBatDau.setValue(null);
		dpNgayKetThuc.setValue(null);
		cmbTopTK.setValue("1");
	}
}
