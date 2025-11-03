package controller;

import java.util.ArrayList;

import dao.KeThuocDAO;
import entity.KeThuoc;
import gui.DanhSachKeThuoc_GUI;

public class DanhSachKeThuocCtrl {

    public DanhSachKeThuoc_GUI ktGUI;
    public KeThuocDAO keDAO = new KeThuocDAO();
    public ToolCtrl tool = new ToolCtrl();

    public ArrayList<KeThuoc> listKe;

    public DanhSachKeThuocCtrl(DanhSachKeThuoc_GUI ktGUI) {
        super();
        this.ktGUI = ktGUI;
        listKe = layListKeThuoc();
    }

    public void lamMoi() {
        ktGUI.cmbLoaiKe.setSelectedItem("Tất cả");
        ktGUI.cmbSucChua.setSelectedItem("Tất cả");
        locTatCa();
    }

    public void locTatCa() {
        ArrayList<KeThuoc> ketQua = new ArrayList<>();

        String loaiKe = ktGUI.cmbLoaiKe.getSelectedItem().toString();
        String sucChuaStr = ktGUI.cmbSucChua.getSelectedItem().toString();

        for (KeThuoc ke : listKe) {
            boolean trungLoaiKe = loaiKe.equals("Tất cả") || ke.getLoaiKe().equalsIgnoreCase(loaiKe);

            boolean trungSucChua = true;
            if (!sucChuaStr.equals("Tất cả")) {
                int max = Integer.parseInt(sucChuaStr.replace("<", "").trim());
                trungSucChua = ke.getSucChua() < max;
            }

            if (trungLoaiKe && trungSucChua) {
                ketQua.add(ke);
            }
        }

        setDataChoTable(ketQua);
    }

    public ArrayList<KeThuoc> layListKeThuoc() {
        return keDAO.layListKeThuoc();
    }
    
    public ArrayList<String> layListTenKe() {
    	return keDAO.layTatCaKeThuoc();
    }

    public void setDataChoTable(ArrayList<KeThuoc> list) {
        ktGUI.model.setRowCount(0);

        for (KeThuoc ke : list) {
            Object[] row = {
                ke.getMaKe(),
                ke.getLoaiKe(),
                ke.getSucChua(),
                ke.getMoTa(),
                ke.isTrangThai() ? "Hoạt động" : "Đã xóa"
            };
            ktGUI.model.addRow(row);
        }
    }
}
