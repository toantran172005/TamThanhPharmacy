package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import dao.ThueDAO;
import entity.Thue;
import gui.Thue_GUI;

public class ThueCtrl {
    
    public Thue_GUI thueGUI;
    public ThueDAO thueDAO = new ThueDAO();
    public ToolCtrl tool = new ToolCtrl(); 

    public ThueCtrl(Thue_GUI thue_GUI) {
        this.thueGUI = thue_GUI;
    }
    
    // Hàm tải dữ liệu lên bảng
    public void loadData() {
        ArrayList<Thue> list = thueDAO.layListThue();
        hienThiLenBang(list);
    }

    // Hàm hiển thị danh sách lên bảng
    private void hienThiLenBang(ArrayList<Thue> list) {
        DefaultTableModel model = (DefaultTableModel) thueGUI.tblThue.getModel();
        model.setRowCount(0);
        for (Thue t : list) {
            double tyLeHienThi = t.getTiLeThue() * 100;
            String tyLeStr;
            if (tyLeHienThi == (long) tyLeHienThi) {
                tyLeStr = String.format("%d%%", (long) tyLeHienThi);
            } else {
                tyLeStr = String.format("%s%%", tyLeHienThi);
            }
            
            model.addRow(new Object[] {
                t.getMaThue(),
                t.getLoaiThue(),
                tyLeStr,
                t.getMoTa()
            });
        }
    }

    // Xử lý thêm thuế mới
    public void themThue() {
        String loaiThue = thueGUI.txtLoaiThue.getText().trim();
        String tyLeStr = thueGUI.txtTyLeThue.getText().trim();
        String moTa = thueGUI.txtMoTa.getText().trim();

        if (loaiThue.isEmpty() || tyLeStr.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên loại thuế và tỷ lệ!", false);
            return;
        }

        try {
            double tyLeInput = Double.parseDouble(tyLeStr);
            if (tyLeInput < 0) {
                tool.hienThiThongBao("Lỗi", "Tỷ lệ thuế không được âm!", false);
                return;
            }
            
            double tyLeLuu = tyLeInput / 100.0;

            String maThue = tool.taoKhoaChinh("T");
            Thue t = new Thue(maThue, loaiThue, tyLeLuu, moTa);

            if (thueDAO.themThue(t)) {
                tool.hienThiThongBao("Thành công", "Thêm loại thuế mới thành công!", true);
                lamMoi();
                loadData();
            } else {
                tool.hienThiThongBao("Thất bại", "Thêm thất bại!", false);
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi", "Tỷ lệ thuế phải là số!", false);
        }
    }

    // Xử lý cập nhật thuế
    public void suaThue() {
        int row = thueGUI.tblThue.getSelectedRow();
        if (row < 0) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn dòng cần sửa!", false);
            return;
        }

        String maThue = thueGUI.txtMaThue.getText().trim();
        String loaiThue = thueGUI.txtLoaiThue.getText().trim();
        String tyLeStr = thueGUI.txtTyLeThue.getText().trim();
        String moTa = thueGUI.txtMoTa.getText().trim();

        try {
            double tyLeInput = Double.parseDouble(tyLeStr);
            double tyLeLuu = tyLeInput / 100.0;

            Thue t = new Thue(maThue, loaiThue, tyLeLuu, moTa);

            if (thueDAO.capNhatThue(t)) {
                tool.hienThiThongBao("Thành công", "Cập nhật thành công!", true);
                lamMoi();
                loadData();
            } else {
                tool.hienThiThongBao("Thất bại", "Cập nhật thất bại!", false);
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi", "Tỷ lệ thuế không hợp lệ!", false);
        }
    }

    // Xử lý xóa thuế
    public void xoaThue() {
        int row = thueGUI.tblThue.getSelectedRow();
        if (row < 0) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn dòng cần xóa!", false);
            return;
        }

        String maThue = thueGUI.txtMaThue.getText();
        
        int confirm = JOptionPane.showConfirmDialog(thueGUI, 
                "Bạn có chắc chắn muốn xóa loại thuế này?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (thueDAO.xoaThue(maThue)) {
                    tool.hienThiThongBao("Thành công", "Xóa thành công!", true);
                    lamMoi();
                    loadData();
                } else {
                    tool.hienThiThongBao("Thất bại", "Không tìm thấy dữ liệu để xóa!", false);
                }
                
            } catch (java.sql.SQLException e) {
                if (e.getErrorCode() == 547) {
                    tool.hienThiThongBao("Cảnh báo", 
                        "Không thể xóa loại thuế này vì đang được áp dụng cho Thuốc hoặc Nhân Viên!", 
                        false);
                } else {
                    e.printStackTrace(); 
                    tool.hienThiThongBao("Lỗi hệ thống", "Lỗi cơ sở dữ liệu: " + e.getMessage(), false);
                }
            }
        }
    }

    // Làm mới form
    public void lamMoi() {
        thueGUI.txtMaThue.setText("Tự động tạo...");
        thueGUI.txtLoaiThue.setText("");
        thueGUI.txtTyLeThue.setText("");
        thueGUI.txtMoTa.setText("");
        thueGUI.txtTimKiem.setText("");
        thueGUI.tblThue.clearSelection();
        loadData(); 
    }

    // Tìm kiếm
    public void timKiem() {
        String tuKhoa = thueGUI.txtTimKiem.getText().trim();
        ArrayList<Thue> list = thueDAO.timKiem(tuKhoa);
        hienThiLenBang(list);
    }
}