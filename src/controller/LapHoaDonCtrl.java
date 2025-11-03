package controller;

import dao.*;
import entity.*;
import gui.LapHoaDon_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.tools.Tool;

import java.awt.Color;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class LapHoaDonCtrl {
    private LapHoaDon_GUI gui;
    private ToolCtrl tool = new ToolCtrl();
    private TrangChuQL_GUI trangChuQL;
    private TrangChuNV_GUI trangChuNV;

    private ThuocDAO thuocDAO = new ThuocDAO();
    private KhachHangDAO khDAO = new KhachHangDAO();
    private NhanVienDAO nvDAO = new NhanVienDAO();
    private DonViTinhDAO dvtDAO = new DonViTinhDAO();
    private KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
    private HoaDonDAO hdDAO = new HoaDonDAO();

    private List<KhachHang> dsKhachHang;
    private List<Thuoc> dsThuoc;
    private DefaultTableModel tableModel;

    private boolean dangSetTenKH = false;
    private boolean dangSetSdtKH = false;

    public LapHoaDonCtrl() { this(null); }

    public LapHoaDonCtrl(LapHoaDon_GUI gui) {
        this.gui = gui;
        if (gui == null) return;
        this.trangChuQL = gui.getMainFrame();
        this.trangChuNV = gui.getMainFrameNV();
        this.tableModel = (DefaultTableModel) gui.getTblThuoc().getModel();
        init();
    }

    private void init() {
        loadData();
        setupEvents();
        setupAutoComplete();
    }

    // === TẢI DỮ LIỆU ===
    private void loadData() {
        dsKhachHang = khDAO.layListKhachHang();
        dsThuoc = thuocDAO.layListThuoc();

        gui.getCmbSanPham().removeAllItems();
        for (Thuoc t : dsThuoc) gui.getCmbSanPham().addItem(t.getTenThuoc());

        List<DonViTinh> dsDVT = dvtDAO.layListDVT();
        gui.getCmbDonVi().removeAllItems();
        for (DonViTinh dvt : dsDVT) gui.getCmbDonVi().addItem(dvt.getTenDVT());
        if (!dsDVT.isEmpty()) gui.getCmbDonVi().setSelectedIndex(0);

        gui.getCmbHTThanhToan().setSelectedItem("Tiền mặt");
    }

    // === SỰ KIỆN ===
    private void setupEvents() {
        gui.getBtnThem().addActionListener(e -> xuLyThemThuocVaoBang());
        gui.getBtnLamMoi().addActionListener(e -> lamMoi());
        gui.getBtnTaoHD().addActionListener(e -> xuLyXuatHoaDon());
        gui.getCmbHTThanhToan().addActionListener(e -> tinhTienThua());
        gui.getTxtTienNhan().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { tinhTienThua(); }
        });
    }

    // === AUTOCOMPLETE ===
    private void setupAutoComplete() {
        gui.getTxtTenKH().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (dangSetTenKH || gui.getTxtTenKH().getText().trim().isEmpty()) return;
                String input = gui.getTxtTenKH().getText().trim().toLowerCase();
                List<KhachHang> ketQua = dsKhachHang.stream()
                    .filter(kh -> kh.getTenKH().toLowerCase().contains(input))
                    .limit(5).toList();
                if (!ketQua.isEmpty()) showSuggestion(gui.getTxtTenKH(), ketQua, true);
            }
        });

        gui.getTxtSdt().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (dangSetSdtKH || gui.getTxtSdt().getText().trim().isEmpty()) return;
                String input = gui.getTxtSdt().getText().trim();
                List<KhachHang> ketQua = dsKhachHang.stream()
                    .filter(kh -> tool.chuyenSoDienThoai(kh.getSdt()).contains(input))
                    .limit(5).toList();
                if (!ketQua.isEmpty()) showSuggestion(gui.getTxtSdt(), ketQua, false);
            }
        });
    }

    private void showSuggestion(JTextField tf, List<KhachHang> list, boolean isTen) {
        JPopupMenu pop = new JPopupMenu();
        for (KhachHang kh : list) {
            String text = isTen 
                ? kh.getTenKH() + " - " + tool.chuyenSoDienThoai(kh.getSdt())
                : tool.chuyenSoDienThoai(kh.getSdt()) + " - " + kh.getTenKH();
            JMenuItem item = new JMenuItem(text);
            item.addActionListener(e -> {
                if (isTen) {
                    dangSetSdtKH = true;
                    gui.getTxtTenKH().setText(kh.getTenKH());
                    gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
                    gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
                    gui.getTxtTuoi().setEditable(false);
                    dangSetSdtKH = false;
                } else {
                    dangSetTenKH = true;
                    gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
                    gui.getTxtTenKH().setText(kh.getTenKH());
                    gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
                    gui.getTxtTuoi().setEditable(false);
                    dangSetTenKH = false;
                }
                pop.setVisible(false);
            });
            pop.add(item);
        }
        SwingUtilities.invokeLater(() -> pop.show(tf, 0, tf.getHeight()));
    }

    // === THÊM THUỐC ===
    private void xuLyThemThuocVaoBang() {
        String tenThuoc = gui.getCmbSanPham().getEditor().getItem().toString().trim();
        if (tenThuoc.isEmpty()) { tool.hienThiThongBao("Lỗi", "Chọn thuốc!", false); return; }

        Thuoc thuoc = dsThuoc.stream()
            .filter(t -> t.getTenThuoc().equalsIgnoreCase(tenThuoc))
            .findFirst().orElse(null);
        if (thuoc == null) { tool.hienThiThongBao("Lỗi", "Thuốc không tồn tại!", false); return; }

        int sl;
        try {
            sl = Integer.parseInt(gui.getTxtSoLuong().getText().trim());
            if (sl <= 0 || sl > thuocDAO.laySoLuongTon(thuoc.getMaThuoc())) throw new Exception();
        } catch (Exception ex) {
            tool.hienThiThongBao("Lỗi", "Số lượng không hợp lệ hoặc vượt tồn kho!", false);
            return;
        }

        String tenDVT = (String) gui.getCmbDonVi().getSelectedItem();
        double donGia = thuoc.getGiaBan();
        double thanhTien = donGia * sl;
        String ghiChu = "Không có";

        // Thêm dòng
        Object[] row = { tableModel.getRowCount() + 1, thuoc.getTenThuoc(), sl, tenDVT,
            tool.dinhDangVND(donGia), tool.dinhDangVND(thanhTien), ghiChu, "Xóa" };
        tableModel.addRow(row);
        setupXoaButton();
        tinhTongTien();
        gui.getTxtSoLuong().setText("");
    }

    private void setupXoaButton() {
        gui.getTblThuoc().getColumn("Hoạt động").setCellRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JButton btn = new JButton("Xóa");
            btn.setForeground(Color.RED);
            btn.addActionListener(e -> {
                if (tool.hienThiXacNhan("Xác nhận", "Xóa dòng này?", null)) {
                    tableModel.removeRow(row);
                    capNhatSTT();
                    tinhTongTien();
                }
            });
            return btn;
        });
    }

    private void capNhatSTT() {
        for (int i = 0; i < tableModel.getRowCount(); i++) tableModel.setValueAt(i+1, i, 0);
    }

    private void tinhTongTien() {
        double tong = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object giaTri = tableModel.getValueAt(i, 5); // Cột chứa giá tiền
            if (giaTri != null) {
                String text = giaTri.toString().trim();
                double gia = tool.chuyenTienSangSo(text);
                tong += gia;
            }
        }

        gui.getLblTongTien().setText(tool.dinhDangVND(tong));
    }


    private void tinhTienThua() {
        // Nếu không chọn "Tiền mặt" → không có tiền thừa
        if (!"Tiền mặt".equals(gui.getCmbHTThanhToan().getSelectedItem())) {
            gui.getLblTienThua().setText("0 VNĐ");
            return;
        }

        try {
            double tong = tool.chuyenTienSangSo(gui.getLblTongTien().getText());
            double nhan = tool.chuyenTienSangSo(gui.getTxtTienNhan().getText());

            double tienThua = nhan - tong;
            if (tienThua < 0) tienThua = 0; // tránh âm

            // Hiển thị lại bằng định dạng VND
            gui.getLblTienThua().setText(tool.dinhDangVND(tienThua));

        } catch (Exception e) {
            gui.getLblTienThua().setText("0 VNĐ");
        }
    }


    private void lamMoi() {
        gui.getTxtSdt().setText("");
        gui.getTxtTenKH().setText("");
        gui.getTxtTuoi().setText("");
        gui.getTxtTuoi().setEditable(true);
        gui.getTxtSoLuong().setText("");
        gui.getTxtTienNhan().setText("");
        tableModel.setRowCount(0);
        tinhTongTien();
    }

    private void xuLyXuatHoaDon() {
        try {
            // ==== 1. Kiểm tra dữ liệu ====
            if (tableModel.getRowCount() == 0) {
                tool.hienThiThongBao("Thông báo", "Chưa có thuốc trong hóa đơn!", false);
                return;
            }

            String tenKH = gui.getTxtTenKH().getText().trim();
            String sdt = gui.getTxtSdt().getText().trim();
            String tuoiText = gui.getTxtTuoi().getText().trim();
            String hinhThucTT = (String) gui.getCmbHTThanhToan().getSelectedItem();

            if (tenKH.isEmpty() || sdt.isEmpty()) {
                tool.hienThiThongBao("Thông báo", "Vui lòng nhập đầy đủ thông tin khách hàng!", false);
                return;
            }

            int tuoi;
            try {
                tuoi = Integer.parseInt(tuoiText);
                if (tuoi <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                tool.hienThiThongBao("Lỗi nhập liệu", "Tuổi không hợp lệ!", false);
                return;
            }

            // ==== 2. Xử lý khách hàng ====
            KhachHangDAO khDAO = new KhachHangDAO();
            String sdtChuan = tool.chuyenSoDienThoai(sdt);
            KhachHang kh = khDAO.timMaKhachHangTheoSDT(sdtChuan);
            String maKH;

            if (kh == null) {
                maKH = tool.taoKhoaChinh("KH");
                boolean themKH = khDAO.themKhachHangMoi(maKH, tenKH, sdtChuan, tuoi);
                if (!themKH) {
                    tool.hienThiThongBao("Lỗi", "Không thể thêm khách hàng mới!", false);
                    return;
                }
            } else {
                maKH = kh.getMaKH();
            }

            // ==== 3. Tạo hóa đơn ====
            String maHD = tool.taoKhoaChinh("HD");
//            String maNV = layMaNhanVienHienTai(); // hoặc "TTNV1"
            String maNV = "TTNV1";
            String diaChiHT = "456 Nguyễn Huệ, TP.HCM";
            String tenHT = "Hiệu Thuốc Tâm Thanh";
            String hotline = "+84-912345689";

            double tienNhan = 0;
            try {
                if (!gui.getTxtTienNhan().getText().trim().isEmpty()) {
                    tienNhan = Double.parseDouble(gui.getTxtTienNhan().getText().trim().replace(",", ""));
                }
            } catch (NumberFormatException e) {
                tool.hienThiThongBao("Lỗi nhập liệu", "Số tiền nhận không hợp lệ!", false);
                return;
            }
            KhachHang khHD = khDAO.timKhachHangTheoMa(maKH);
            NhanVien nv = nvDAO.timNhanVienTheoMa(maNV);
            
            HoaDon hd = new HoaDon(maHD, khHD, nv, hinhThucTT, LocalDate.now(),
                    diaChiHT, tenHT, "", hotline, tienNhan, true);

            if (!hdDAO.themHoaDon(hd)) {
                tool.hienThiThongBao("Lỗi", "Không thể tạo hóa đơn!", false);
                return;
            }

            // ==== 4. Thêm chi tiết hóa đơn ====
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String tenThuoc = tableModel.getValueAt(i, 1).toString();
                Thuoc t = dsThuoc.stream()
                        .filter(x -> x.getTenThuoc().equals(tenThuoc))
                        .findFirst()
                        .orElse(null);

                if (t == null) continue;

                int soLuong = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                double donGia = tool.chuyenTienSangSo(tableModel.getValueAt(i, 4).toString());

                String tenDVT = (String) gui.getCmbDonVi().getSelectedItem();
                String maDVT = dvtDAO.timMaDVTTheoTen(tenDVT); // hoặc lấy từ DAO nếu bạn có danh sách DVT

                hdDAO.themChiTietHoaDon(maHD, t.getMaThuoc(), soLuong, maDVT, donGia);
                thuocDAO.giamSoLuongTon(t.getMaThuoc(), maDVT, soLuong);
            }

            // ==== 5. Thông báo thành công & làm mới giao diện ====
            tool.hienThiThongBao("Thành công", "Xuất hóa đơn thành công!", true);
            tableModel.setRowCount(0);
            lamMoi();

        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi khi xuất hóa đơn!", false);
        }
    }

}