package controller;

import java.io.IOException;
import java.util.List;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TrangChuCtrl {

	@FXML
	public 	VBox vbTrangChu, vbHangHoa, vbKhachHang, vbHoaDon;

	public VBox vbHienTai;
	public Label MenuConHienTai;
	public static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
	public boolean isSelected = false;

	@FXML
	public BorderPane mainPane;
	public Node mainPaneCenter;

	public void initialize() {
		mainPaneCenter = mainPane.getCenter();
		setThuocTinhMainMenu();
	}

	public void setThuocTinhMainMenu() {
		setMauClickVaMenuCon(vbTrangChu, List.of());
		setMauClickVaMenuCon(vbHangHoa, List.of("Danh sách hàng hóa", "Nhập hàng hóa", "Đặt hàng hóa"));
		setMauClickVaMenuCon(vbKhachHang, List.of("Danh sách khách hàng", "Khiếu nại & Hỗ trợ"));
		setMauClickVaMenuCon(vbHoaDon, List.of("Danh sách hóa đơn", "Đổi - Trả"));
	}

	@SuppressWarnings("unused")
	public void setMauClickVaMenuCon(VBox box, List<String> menuCon) {
		box.getStyleClass().add("vbox-menu");
		box.setOnMouseClicked(e -> menuChaClicked(box, menuCon));
	}

	public void menuChaClicked(VBox box, List<String> menuCon) {
		if (vbHienTai != null && vbHienTai != box) {
			vbHienTai.pseudoClassStateChanged(SELECTED, false);

			if (!vbHienTai.getChildren().isEmpty()
					&& vbHienTai.getChildren().get(vbHienTai.getChildren().size() - 1) instanceof VBox) {
				vbHienTai.getChildren().remove(vbHienTai.getChildren().size() - 1);
			}
		}

		if (vbHienTai == box && isSelected) {
			if (!box.getChildren().isEmpty() && box.getChildren().get(box.getChildren().size() - 1) instanceof VBox) {
				box.getChildren().remove(box.getChildren().size() - 1);
			}
			box.pseudoClassStateChanged(SELECTED, false);
			vbHienTai = null;
			isSelected = false;
			return;
		}

		box.pseudoClassStateChanged(SELECTED, true);

		VBox subMenu = new VBox();
		subMenu.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 5; -fx-spacing: 5;");
		subMenu.setTranslateY(5);

		for (String name : menuCon) {
			subMenu.getChildren().add(taoMenuCon(name));
		}

		box.getChildren().add(subMenu);

		vbHienTai = box;
		isSelected = true;
	}

	public Label taoMenuCon(String name) {
		Label lbl = new Label(name);
		lbl.setId(name);
		lbl.getStyleClass().add("submenu-label");
		lbl.setStyle("-fx-padding: 8 5 8 10; -fx-cursor: hand; -fx-font-size: 15px; -fx-font-weight: bold;");
		lbl.setMaxWidth(Double.MAX_VALUE);

		lbl.setOnMouseClicked(ev -> {
			menuConClick(lbl);
			ev.consume();
		});

		return lbl;
	}

	public void menuConClick(Label lbl) {
		switch (lbl.getId()) {
		case "Danh sách hàng hóa":
			doiCenterPane("/fxml/DanhSachHH.fxml");
			break;
		case "Nhập hàng hóa":
			
			break;
		case "Đặt hàng hóa":
			
			break;
		case "Danh sách khách hàng":
			doiCenterPane("/fxml/DanhSachKH.fxml");
			break;
		case "Khiếu nại & Hỗ trợ":
			
			break;
		case "Danh sách hóa đơn":
			doiCenterPane("/fxml/DanhSachHD.fxml");
			break;
		case "Đổi - Trả":
			
			break;
		}

		if (MenuConHienTai != null) {
			MenuConHienTai.pseudoClassStateChanged(SELECTED, false);
		}

		lbl.pseudoClassStateChanged(SELECTED, true);
		MenuConHienTai = lbl;
	}

	public void quayLaiTrangChu() {
		mainPane.setCenter(mainPaneCenter);
	}

	public void doiCenterPane(String fxmlPath) {
		try {
			Parent newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
			mainPane.setCenter(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
