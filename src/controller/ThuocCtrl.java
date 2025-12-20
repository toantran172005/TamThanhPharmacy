package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import dao.DonViTinhDAO;
import dao.KeThuocDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.KeThuoc;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.ThemThuoc_GUI;
import gui.ThongKeThuoc_GUI;
import gui.TimKiemThuoc_GUI;

public class ThuocCtrl {
    
    public ThuocDAO thuocDAO = new ThuocDAO();
    public KeThuocDAO keThuocDao = new KeThuocDAO();
    public DonViTinhDAO dvtDao = new DonViTinhDAO();
    public ThueDAO thueDao = new ThueDAO();
    public ToolCtrl tool = new ToolCtrl();
    public ThongKeThuoc_GUI tkThuoc;
    public TimKiemThuoc_GUI thuoc;
    public ChiTietThuoc_GUI ctThuoc;
    public ThemThuoc_GUI themThuoc;
    public boolean isHienThi = true;
    public int dangNhap = 1;
    ArrayList<Thuoc> listThuoc = thuocDAO.layListThuocHoanChinh();
    
    public ThuocCtrl(ThongKeThuoc_GUI tkThuoc) {
        this.tkThuoc = tkThuoc;
    }
    
    public ThuocCtrl(ChiTietThuoc_GUI ctThuoc) {
        this.ctThuoc = ctThuoc;
    }
    
    public ThuocCtrl(TimKiemThuoc_GUI thuoc) {
        this.thuoc = thuoc;
    }
    
    public ThuocCtrl() {
        super();
    }

    //chọn file 
    public File chonFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if(option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    // set cmb kệ thuốc
    public void setCmbKeThuoc() {
        ArrayList<KeThuoc> listKT = keThuocDao.layListKeThuoc();
        thuoc.cmbLoaiThuoc.addItem("Tất cả");
        for (KeThuoc kt : listKT) {
            thuoc.cmbLoaiThuoc.addItem(kt.getLoaiKe());
        }
    }

    //Thống kê thuốc
    public ArrayList<Thuoc> layListThongKe(LocalDate ngayBD, LocalDate ngayKT) {
        ArrayList<Thuoc> listTK = new ArrayList<Thuoc>();
        try {
            listTK = thuocDAO.layListTHThongKe(ngayBD, ngayKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTK;
    }
    
    //xoá thuốc
    public void xoaThuoc() {
        listThuoc = thuocDAO.layListThuocHoanChinh();
        if (thuoc.btnXoa.getText().equalsIgnoreCase("Xoá")) {
            if (tool.hienThiXacNhan("Xóa thuốc", "Xác nhận xóa thuốc?", null)) {
                int viewRow = thuoc.tblThuoc.getSelectedRow();
                if (viewRow != -1) {
                    int modelRow = thuoc.tblThuoc.convertRowIndexToModel(viewRow);
                    Object maObj = thuoc.tblThuoc.getModel().getValueAt(modelRow, 0);
                    String maTh = maObj == null ? "" : maObj.toString();

                    if (thuocDAO.xoaThuoc(maTh)) {
                        tool.hienThiThongBao("Xóa thuốc", "Đã xóa thuốc thành công!", true);
                    }
                }
                locTatCa(isHienThi);
            }
        } else {
            if (tool.hienThiXacNhan("Khôi phục thuốc", "Xác nhận khôi phục thuốc?", null)) {
                int viewRow = thuoc.tblThuoc.getSelectedRow();
                if (viewRow != -1) {
                    int modelRow = thuoc.tblThuoc.convertRowIndexToModel(viewRow);
                    Object maObj = thuoc.tblThuoc.getModel().getValueAt(modelRow, 0);
                    String maTh = maObj == null ? "" : maObj.toString();

                    if (thuocDAO.khoiPhucTH(maTh)) {
                        tool.hienThiThongBao("Khôi phục thuốc", "Đã khôi phục thuốc thành công!", true);
                    }
                }
                locTatCa(isHienThi);
            }
        }
    }
    
    //xử lý thống kê
    public void onThongKe() {
         LocalDate bd = tool.utilDateSangLocalDate(tkThuoc.dpNgayBD.getDate());
         LocalDate kt = tool.utilDateSangLocalDate(tkThuoc.dpNgayKT.getDate());
         int stt = 1;
         
         if(bd == null) {
             bd = LocalDate.of(2025, 01, 01);
         } 
         
         if(kt == null) {
             kt = LocalDate.now();
         }
         
         if(bd == null && kt == null) {
             bd = LocalDate.of(2025, 01, 01);
             kt = LocalDate.now();
         }
          
         
        ArrayList<Thuoc> listTK = layListThongKe(bd, kt);
        tkThuoc.pnlChart.repaint();
         
         
        //Đưa dữ liệu cho dataset để vẽ biểu đồ
        tkThuoc.dataset.clear();
        for(Thuoc t : listTK) {
            tkThuoc.dataset.addValue(t.getSoLuongBan(), "Số lượng bán", t.getTenThuoc());
        }
         
        //Đổ dữ liệu lên bảng
        for(Thuoc t : listTK) {
            tkThuoc.model.addRow(new Object[] {
                    stt++,
                    t.getMaThuoc(),
                    t.getTenThuoc(),
                    t.getSoLuongBan(),
                    tool.dinhDangVND(t.getDoanhThu())
            });
        }
    }
    
    //Làm mới thống kê
    public void onLamMoi() {
        tkThuoc.dpNgayBD.setDate(null);
        tkThuoc.dpNgayKT.setDate(null);
        ((DefaultTableModel) tkThuoc.tblThongKe.getModel()).setRowCount(0);
    }
    
    //xử lý xem chi tiết
    public void xemChiTiet() {
        int selectedRow = thuoc.tblThuoc.getSelectedRow();
        if(selectedRow == -1) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 thuốc để xem chi tiết", false);
            return;
        }
        
        int modelRow = thuoc.tblThuoc.convertRowIndexToModel(selectedRow);
        String maThuoc = thuoc.tblThuoc.getModel().getValueAt(selectedRow, 0).toString();
        
        ChiTietThuoc_GUI ctThuoc_GUI = new ChiTietThuoc_GUI(maThuoc);
        tool.doiPanel(thuoc, ctThuoc_GUI);
    }
    
    //Lấy danh sách tên thuốc
    public ArrayList<String> layDSTenThuoc(){
        ArrayList<String> listTen = new ArrayList<>();
        ArrayList<Thuoc> list = thuocDAO.layListThuocHoanChinh();
        
        for(Thuoc t : list) {
            listTen.add(t.getTenThuoc());
        }
        
        return listTen;
    }
    

    public void xuLyBtnLichSuXoa() {
        isHienThi = !isHienThi;
        thuoc.btnLichSuXoa.setText(!isHienThi ? "Danh sách hiện tại" : "Lịch sử xoá");
        thuoc.btnXoa.setText(!isHienThi ? "Khôi phục" : "Xoá");
        locTatCa(isHienThi);
    }
    
    // Cập nhật hàm lọc tất cả 
    public void locTatCa(boolean isHienThi) {
        listThuoc = thuocDAO.layListThuocHoanChinh(); 
        ArrayList<Thuoc> ketQua = new ArrayList<>();

        String tuKhoaTen = thuoc.txtTenThuoc.getText().trim().toLowerCase(); 
        Object loaiObj = thuoc.cmbLoaiThuoc.getSelectedItem();
        String tuKhoaLoai = (loaiObj != null) ? loaiObj.toString().trim().toLowerCase() : "";
        
        if (tuKhoaLoai.equals("tất cả")) {
            tuKhoaLoai = "";
        }

        if (listThuoc != null) {
            for (Thuoc t : listThuoc) {
                boolean matchTen = tuKhoaTen.isEmpty() 
                        || t.getTenThuoc().toLowerCase().contains(tuKhoaTen);
                boolean matchLoai = tuKhoaLoai.isEmpty() 
                        || (t.getKeThuoc() != null && t.getKeThuoc().getLoaiKe().toLowerCase().contains(tuKhoaLoai));
                boolean matchTrangThai = (t.isTrangThai() == isHienThi);

                if (matchTen && matchLoai && matchTrangThai) {
                    ketQua.add(t);
                }
            }
        }
        setDataChoTable(ketQua);
    }
    
    public void setDataChoTable(ArrayList<Thuoc> list) {
        thuoc.model.setRowCount(0);
        for(Thuoc t : list) {
            thuoc.model.addRow(new Object[] {
                    t.getMaThuoc(),
                    t.getTenThuoc(),
                    t.getKeThuoc().getLoaiKe(),
                    tool.dinhDangVND(t.getGiaBan()),
                    t.getSoLuong(),
                    t.getNoiSanXuat(),
                    t.getDvt().getTenDVT(),
                    t.getHanSuDung()
            });
        }
    }
    
    //Tìm kiếm theo loại
    public ArrayList<Thuoc> layThuocTheoLoai() {
        ArrayList<Thuoc> listKQ = new ArrayList<Thuoc>();
        ArrayList<Thuoc> list = thuocDAO.layListThuocHoanChinh();
        String loai = thuoc.cmbLoaiThuoc.getSelectedItem().toString();
        
        if(loai == "Tất cả") {
            return list;
        }
        for(Thuoc t : list) {
            if(t.getKeThuoc().getLoaiKe().equalsIgnoreCase(loai)) {
                listKQ.add(t);
            }
        }
        
        return listKQ;
    }
    
    public void timKiemThuocTheoLoai() {
        
        ArrayList<Thuoc> list = layThuocTheoLoai();
        thuoc.model.setRowCount(0);
        for(Thuoc t : list) {
            thuoc.model.addRow(new Object[] {
                    t.getMaThuoc(),
                    t.getTenThuoc(),
                    t.getKeThuoc().getLoaiKe(),
                    tool.dinhDangVND(t.getGiaBan()),
                    t.getSoLuong(),
                    t.getNoiSanXuat(),
                    t.getDvt().getTenDVT(),
                    t.getHanSuDung()
            });
        }
    }
}