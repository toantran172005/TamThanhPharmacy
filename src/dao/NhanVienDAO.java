package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.NhanVien;
import entity.Thue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NhanVienDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();

	public ObservableList<NhanVien> layListNhanVien() {
        ObservableList<NhanVien> list = FXCollections.observableArrayList();

        try (Connection con = KetNoiDatabase.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT * FROM NhanVien nv "
                             + "JOIN Thue t ON nv.maThue = t.maThue "
                             + "ORDER BY TRY_CAST(SUBSTRING(nv.maNV, 3, LEN(nv.maNV)) AS INT)")) {

            while (rs.next()) {
                Thue thue = new Thue(rs.getString("maThue"), rs.getDouble("tyLeThue"));

                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),

                        // ✅ Dùng hàm ToolCtrl để chuyển đổi
                        ToolCtrl.sqlDateSangLocalDate(rs.getDate("ngaySinh")),
                        rs.getBoolean("gioiTinh"),
                        rs.getString("sdt"),
                        ToolCtrl.sqlDateSangLocalDate(rs.getDate("ngayVaoLam")),

                        rs.getDouble("luong"),
                        thue,
                        rs.getBoolean("trangThai"),
                        rs.getString("anh")
                );

                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

	public boolean xoaNhanVien(String maNV) {
		String sql = "DELETE FROM NhanVien WHERE maNV = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNV);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean capNhatNhanVien(NhanVien nv) {
		String sql = "UPDATE NhanVien SET tenNV=?, chucVu=?, gioiTinh=?, luong=?, sdt=?, anh=? WHERE maNV=?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nv.getTenNV());
			ps.setString(2, nv.getChucVu());
	        ps.setBoolean(3, nv.isGioiTinh());
	        ps.setDouble(4, nv.getLuong());
	        ps.setString(5, nv.getSdt());
	        ps.setString(6, nv.getAnh());
	        ps.setString(7, nv.getMaNV());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
