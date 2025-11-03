package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.DonViTinh;

public class DonViTinhDAO {

    private ToolCtrl tool = new ToolCtrl();

    // 🔹 Tìm mã đơn vị tính theo tên
    public String timMaDVTTheoTen(String tenDVT) {
        List<DonViTinh> listDVT = layListDVT();
        for (DonViTinh dvt : listDVT) {
            if (dvt.getTenDVT().equalsIgnoreCase(tenDVT))
                return dvt.getMaDVT();
        }
        return null;
    }

    // 🔹 Lấy danh sách đơn vị đang hoạt động
    public ArrayList<DonViTinh> layListDVT() {
    	ArrayList<DonViTinh> listDVT = new ArrayList<>();

        String sql = """
            SELECT *
            FROM DonViTinh
            WHERE trangThai = 1
            ORDER BY TRY_CAST(REPLACE(maDVT, 'TTDVT', '') AS int)
            """;

        try (Connection con = KetNoiDatabase.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DonViTinh dvt = new DonViTinh(
                        rs.getString("maDVT"),
                        rs.getString("tenDVT"),
                        rs.getBoolean("trangThai"));
                listDVT.add(dvt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listDVT;
    }

    // 🔹 Lấy danh sách đơn vị đã xóa
    public ArrayList<DonViTinh> layDanhSachDaXoa() {
    	ArrayList<DonViTinh> listDaXoa = new ArrayList<>();

        String sql = """
            SELECT *
            FROM DonViTinh
            WHERE trangThai = 0
            ORDER BY TRY_CAST(REPLACE(maDVT, 'TTDVT', '') AS int)
            """;

        try (Connection con = KetNoiDatabase.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DonViTinh dvt = new DonViTinh(
                        rs.getString("maDVT"),
                        rs.getString("tenDVT"),
                        rs.getBoolean("trangThai"));
                listDaXoa.add(dvt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listDaXoa;
    }

    // 🔹 Xóa đơn vị tính (chuyển trạng thái sang 0)
    public boolean xoaDVT(String maDVT) {
        String sql = """
            UPDATE DonViTinh
            SET trangThai = 0
            WHERE maDVT = ?
            """;

        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDVT);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Khôi phục đơn vị tính (chuyển trạng thái sang 1)
    public boolean khoiPhucDVT(String maDVT) {
        String sql = """
            UPDATE DonViTinh
            SET trangThai = 1
            WHERE maDVT = ?
            """;

        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDVT);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Thêm đơn vị tính mới
    public boolean themDVT(DonViTinh dvt) {
        String sql = """
            INSERT INTO DonViTinh (maDVT, tenDVT, trangThai)
            VALUES (?, ?, ?)
            """;

        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dvt.getMaDVT());
            ps.setString(2, dvt.getTenDVT());
            ps.setBoolean(3, dvt.isTrangThai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Tìm đơn vị tính theo tên
    public DonViTinh timTheoTen(String tenDVT) {
        String sql = "SELECT * FROM DonViTinh WHERE tenDVT = ?";

        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenDVT);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new DonViTinh(
                        rs.getString("maDVT"),
                        rs.getString("tenDVT"),
                        rs.getBoolean("trangThai"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
