package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import database.KetNoiDatabase;
import entity.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KhachHangDAO {

	public ObservableList<KhachHang> layListKhachHang() {

		ObservableList<KhachHang> list = FXCollections.observableArrayList();
		String query = "SELECT \r\n"
				+ "    maKH, \r\n"
				+ "    tenKH, \r\n"
				+ "    sdt, \r\n"
				+ "    tuoi, \r\n"
				+ "    trangThai\r\n"
				+ "FROM KhachHang\r\n"
				+ "ORDER BY \r\n"
				+ "    TRY_CAST(REPLACE(maKH, 'TTKH', '') AS INT);\r\n"
				+ "";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				list.add(new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdt"),
						rs.getInt("tuoi"), rs.getBoolean("trangThai")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public ObservableList<KhachHang> layListKHThongKe() {
		ObservableList<KhachHang> list = FXCollections.observableArrayList();
		String query = "SELECT \r\n"
				+ "    KH.maKH,\r\n"
				+ "    KH.tenKH,\r\n"
				+ "    COUNT(DISTINCT HD.maHD) AS tongDonHang,\r\n"
				+ "    SUM(CT.soLuong * CT.donGia) AS tongTien\r\n"
				+ "FROM KhachHang KH\r\n"
				+ "JOIN HoaDon HD ON KH.maKH = HD.maKH\r\n"
				+ "JOIN CT_HoaDon CT ON HD.maHD = CT.maHD\r\n"
				+ "GROUP BY KH.maKH, KH.tenKH, KH.sdt\r\n"
				+ "";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {
			
			while(rs.next()) {
				list.add(new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getInt("tongDonHang"), rs.getDouble("tongTien")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
