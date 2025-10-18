package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.KhachHangDAO;
import entity.KhachHang;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
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

public class ThongKeKhachHangCtrl {

	public ToolCtrl tool = new ToolCtrl();
	public KhachHangDAO khDAO = new KhachHangDAO();
	Map<String, Double> tongTienMap = new HashMap<>();
	Map<String, Integer> tongDonMap = new HashMap<>();

	@FXML
	public ScrollPane scrTKKH;
	@FXML
	public TableView<KhachHang> tblThongKeKH;
	@FXML
	public TableColumn<KhachHang, Integer> colSTT;
	@FXML
	public TableColumn<KhachHang, String> colMaKH;
	@FXML
	public TableColumn<KhachHang, String> colTenKH;
	@FXML
	public TableColumn<KhachHang, String> colSdt;
	@FXML
	public TableColumn<KhachHang, Integer> colSoLanMua;
	@FXML
	public TableColumn<KhachHang, String> colTongChiTieu;
	@FXML
	public ComboBox<String> cmbThongKeTop;
	@FXML
	public Button btnThongKe, btnXuatExcel, btnInBaoCao, btnLamMoi;
	@FXML
	public Label lblTongKH, lblTongSLM, lblTongCT, lblChiTieuTB;
	@FXML
	public DatePicker dpNgayBD, dpNgayKT;
	@FXML
	public BarChart<String, Number> barChartTopKH;

	@FXML
	public CategoryAxis xAxis;

	@FXML
	public NumberAxis yAxis;

	public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();

	public void initialize() {
		setScrCuonNhanh();
		dinhDangNgayDatePicker();
		setItemChoComboBox();
		setHoatDong();
	}

	public void setDataChoTable() {
		if (listKH == null || listKH.isEmpty()) {
			tblThongKeKH.getItems().clear();
			return;
		}

		colSTT.setCellValueFactory(cellData -> {
			return new SimpleIntegerProperty(tblThongKeKH.getItems().indexOf(cellData.getValue()) + 1).asObject();
		});

		colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
		colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
		colSdt.setCellValueFactory(cellData -> {
			return new SimpleStringProperty(tool.chuyenSoDienThoai(cellData.getValue().getSdt()));
		});
		colTongChiTieu.setCellValueFactory(cellData -> new SimpleStringProperty(
				tool.dinhDangVND(tongTienMap.getOrDefault(cellData.getValue().getMaKH(), 0.0))));
		colSoLanMua.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tongDonMap.getOrDefault(cellData.getValue().getMaKH(), 0))
						.asObject());

		tblThongKeKH.setItems(listKH);
	}

	public void setScrCuonNhanh() {
		scrTKKH.addEventFilter(ScrollEvent.SCROLL, event -> {
			double deltaY = event.getDeltaY() * 5;
			scrTKKH.setVvalue(scrTKKH.getVvalue() - deltaY / scrTKKH.getContent().getBoundsInLocal().getHeight());
			event.consume();
		});

		tblThongKeKH.setOnScroll(e -> {
			double deltaY = e.getDeltaY() * 3;
			tblThongKeKH.scrollTo((int) (tblThongKeKH.getFocusModel().getFocusedIndex() - deltaY / 40));
		});

	}

	public void dinhDangNgayDatePicker() {
		tool.dinhDangDatePicker(dpNgayBD);
		tool.dinhDangDatePicker(dpNgayKT);
	}

	public void setHoatDong() {
		btnThongKe.setOnAction(event -> thongKeKhachHang());
	}

	public void thongKeKhachHang() {
		LocalDate ngayBD = dpNgayBD.getValue();
		LocalDate ngayKT = dpNgayKT.getValue();

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

		listKH = khDAO.layListKHThongKe(ngayBD, ngayKT);
		for (KhachHang kh : listKH) {
			double tongTien = khDAO.layTongTien(kh.getMaKH());
			int tongDon = khDAO.layTongDonHang(kh.getMaKH());
			tongTienMap.put(kh.getMaKH(), tongTien);
			tongDonMap.put(kh.getMaKH(), tongDon);
		}
		setDataChoLabel();
		setDataChoBarChart();
		setDataChoTable();
	}

	public void setDataChoBarChart() {
		barChartTopKH.getData().clear();

		List<KhachHang> top5 = listKH.stream()
				.sorted((kh1, kh2) -> Double.compare(kh2.getTongTien(), kh1.getTongTien())).limit(5).toList();

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Top 5 khách hàng chi tiêu cao nhất");

		for (KhachHang kh : top5) {
			series.getData().add(new XYChart.Data<>(kh.getTenKH(), kh.getTongTien()));
		}

		barChartTopKH.getData().add(series);

		xAxis.setLabel("Tên khách hàng");
		yAxis.setLabel("Tổng chi tiêu (VND)");
		barChartTopKH.setLegendVisible(false);
	}

	public void setDataChoLabel() {
		lblTongKH.setText(String.valueOf(listKH.size()));
		int tongSLM = 0;
		for (KhachHang kh : listKH) {
			tongSLM += kh.getTongDonHang();
		}
		lblTongSLM.setText(String.valueOf(tongSLM));
		double tongCT = 0;
		for (KhachHang kh : listKH) {
			tongCT += kh.getTongTien();
		}
		lblTongCT.setText(tool.dinhDangVND(tongCT));
		lblChiTieuTB.setText(tool.dinhDangVND(tongCT / listKH.size()));
	}

	public void setItemChoComboBox() {
		cmbThongKeTop.getItems().addAll("1", "5", "10", "20");
	}

}
