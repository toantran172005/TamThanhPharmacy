package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;

public class PhieuDoiTraDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();
	NhanVienDAO nvDAO = new NhanVienDAO();
	KhachHangDAO khDAO = new KhachHangDAO();
	ThuocDAO thuocDAO = new ThuocDAO();

	public ArrayList<Object[]> layListPDT() {
		ArrayList<Object[]> listPDT = new ArrayList<>();

		String sql = """
				 SELECT pdt.maPhieuDT, pdt.maHD, hd.maKH, kh.tenKH, kh.sdt, pdt.maNV, nv.tenNV, pdt.ngayDoiTra,
				 pdt.lyDo, hd.diaChiHT, hd.tenHT, hd.hotline
				   FROM PhieuDoiTra pdt
				   JOIN NhanVien nv ON pdt.maNV = nv.maNV
				JOIN HoaDon hd ON pdt.maHD = hd.maHD
				   JOIN KhachHang kh ON hd.maKH = kh.maKH
				   ORDER BY TRY_CAST(SUBSTRING(pdt.maPhieuDT, 6, LEN(pdt.maPhieuDT)) AS INT)
					""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Object[] pdt = { rs.getString("maPhieuDT"), rs.getString("maHD"), rs.getString("maKH"),
						rs.getString("tenKH"), rs.getString("sdt"), rs.getString("maNV"), rs.getString("tenNV"),
						rs.getDate("ngayDoiTra").toLocalDate(), rs.getString("lyDo"), rs.getString("diaChiHT"),
						rs.getString("tenHT"), rs.getString("hotline") };
				listPDT.add(pdt);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listPDT;
	}

	public boolean themPDTBangObject(Object[] pdt, String maNV) {
		String sql = "INSERT INTO PhieuDoiTra (maPhieuDT, maHD, maNV, ngayDoiTra, lyDo) " + "VALUES (?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, pdt[0].toString());
			ps.setString(2, pdt[1].toString());
			ps.setString(3, pdt[2].toString());
			ps.setDate(4, java.sql.Date.valueOf(pdt[3].toString()));
			ps.setString(5, pdt[4].toString());

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean themChiTietPDTBangObject(Object[] ct) {
		String sql = "INSERT INTO CT_PhieuDoiTra (maPhieuDT, maThuoc, soLuong, ghiChu, tienHoan, mucHoan, maDVT) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ct[0].toString());
			ps.setString(2, ct[1].toString());
			ps.setInt(3, Integer.parseInt(ct[2].toString()));
			ps.setString(4, ct[3].toString());
			ps.setDouble(5, Double.parseDouble(ct[4].toString()));
			ps.setDouble(6, Double.parseDouble(ct[5].toString()));
			ps.setString(7, ct[6].toString());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String layMaPDTMoiNhat() {
		String sql = "SELECT TOP 1 pdt.maPhieuDT\r\n" + "FROM PhieuDoiTra pdt\r\n"
				+ "ORDER BY TRY_CAST(SUBSTRING(pdt.maPhieuDT, 6, LEN(pdt.maPhieuDT)) AS INT) DESC;";
		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString("maPhieuDT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Object[]> layDanhSachThuocTheoPhieuDT(String maPhieuDT) {
		ArrayList<Object[]> list = new ArrayList<>();
		String sql = """
				     SELECT ct.maPhieuDT, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT,  ct.mucHoan, ct.tienHoan, ct.ghiChu
					FROM CT_PhieuDoiTra ct
					JOIN Thuoc t ON ct.maThuoc = t.maThuoc
					JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
					LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
					WHERE maPhieuDT = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPhieuDT);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maPhieuDT"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT"), rs.getDouble("mucHoan"),
						rs.getDouble("tienHoan"), rs.getString("ghiChu") };
				list.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public double tinhTongTienHoanTheoPhieuDT(String maPhieuDT) {
		String sql = """
						SELECT ct.maPhieuDT, SUM(ct.tienHoan) AS tongTienHoan
						FROM CT_PhieuDoiTra ct
						WHERE ct.maPhieuDT = ?
						GROUP BY ct.maPhieuDT;
									""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maPhieuDT);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("tongTienHoan");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
