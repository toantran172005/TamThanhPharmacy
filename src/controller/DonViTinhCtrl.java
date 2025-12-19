package controller;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dao.DonViTinhDAO;
import entity.DonViTinh;
import gui.DonVi_GUI;

public class DonViTinhCtrl {
	
	public DonVi_GUI dvGUI;
	public ToolCtrl tool = new ToolCtrl();
	public DonViTinhDAO dvDAO = new DonViTinhDAO();
	ArrayList<DonViTinh> listDVT = new ArrayList<DonViTinh>();
	public boolean isHienThi = true;
	
	public DonViTinhCtrl(DonVi_GUI donVi_GUI) {
		this.dvGUI = donVi_GUI;
	}
	
    
    //Đưa dữ liệu lên bảng
    public void setDataChoTable(ArrayList<DonViTinh> list) {
        DefaultTableModel model = (DefaultTableModel) dvGUI.tblDonVi.getModel();
        model.setRowCount(0); 
        int stt = 1;
        
        for(DonViTinh dvt : list) {
            model.addRow(new Object[] {
                stt++,
                dvt.getMaDVT(),
                dvt.getTenDVT()
            });
        }
    }
	
	//Hàm lọc tổng hợp (Tìm kiếm theo tên + Trạng thái hiển thị)
    public void locTatCa(boolean isHienThi) {
        ArrayList<DonViTinh> fullList;
        if (isHienThi) {
            fullList = dvDAO.layListDVT();
        } else {
            fullList = dvDAO.layDanhSachDaXoa();
        }

        ArrayList<DonViTinh> ketQua = new ArrayList<>();
        String tuKhoa = dvGUI.txtTimDV.getText().trim().toLowerCase();

        if (fullList != null) {
            for (DonViTinh dvt : fullList) {
                boolean matchTen = tuKhoa.isEmpty() 
                        || dvt.getTenDVT().toLowerCase().contains(tuKhoa);
               
                boolean matchTrangThai = (dvt.isTrangThai() == isHienThi);

                if (matchTen && matchTrangThai) {
                    ketQua.add(dvt);
                }
            }
        }
        setDataChoTable(ketQua);
    }

    //Thêm đơn vị
    public void themDonVi() {
        String tenDV = dvGUI.txtTenDV.getText().trim();
        
        // Kiểm tra rỗng
        if (tenDV.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên đơn vị!", false);
            dvGUI.txtTenDV.requestFocus();
            return;
        }

        // Kiểm tra trùng tên
        if (dvDAO.timTheoTen(tenDV) != null) {
            tool.hienThiThongBao("Lỗi", "Tên đơn vị đã tồn tại!", false);
            return;
        }
        
        String maDV = tool.taoKhoaChinh("DVT"); 
        DonViTinh dvt = new DonViTinh(maDV, tenDV, true);

        if (dvDAO.themDVT(dvt)) {
            tool.hienThiThongBao("Thành công", "Thêm đơn vị thành công!", true);
            dvGUI.txtTenDV.setText("");
            locTatCa(isHienThi);
        } else {
            tool.hienThiThongBao("Thất bại", "Thêm đơn vị thất bại!", false);
        }
    }

    //Xử lý nút Xóa / Khôi phục
    public void xoaDonVi() {
        int selectedRow = dvGUI.tblDonVi.getSelectedRow();
        if (selectedRow == -1) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn đơn vị cần thao tác!", false);
            return;
        }

        String maDVT = dvGUI.tblDonVi.getValueAt(selectedRow, 1).toString();
        
        if (dvGUI.btnXoa.getText().equalsIgnoreCase("Xoá")) {
            //Xoá
            if (tool.hienThiXacNhan("Xóa đơn vị", "Xác nhận xóa đơn vị này?", null)) {
                if (dvDAO.xoaDVT(maDVT)) {
                    tool.hienThiThongBao("Thông báo", "Đã xóa đơn vị thành công!", true);
                    locTatCa(isHienThi); 
                } else {
                    tool.hienThiThongBao("Lỗi", "Xóa thất bại!", false);
                }
            }
        } else {
            //Khôi phục
            if (tool.hienThiXacNhan("Khôi phục đơn vị", "Xác nhận khôi phục đơn vị này?", null)) {
                if (dvDAO.khoiPhucDVT(maDVT)) {
                    tool.hienThiThongBao("Thông báo", "Đã khôi phục đơn vị thành công!", true);
                    locTatCa(isHienThi);
                } else {
                    tool.hienThiThongBao("Lỗi", "Khôi phục thất bại!", false);
                }
            }
        }
    }

    //Xử lý nút Lịch sử xóa
    public void xuLyBtnLichSuXoa() {
        isHienThi = !isHienThi; 
        
        dvGUI.btnLichSuXoa.setText(!isHienThi ? "Danh sách hiện tại" : "Lịch sử xoá");
        dvGUI.btnXoa.setText(!isHienThi ? "Khôi phục" : "Xoá");
        
        locTatCa(isHienThi);
    }
    
    //Làm mới (Reset form tìm kiếm)
    public void lamMoi() {
        dvGUI.txtTimDV.setText("");
        if (!isHienThi) {
            xuLyBtnLichSuXoa(); 
        } else {
            locTatCa(true);
        }
    }

}
