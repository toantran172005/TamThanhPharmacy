package dao;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.ToolCtrl;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatHang;
import connectDB.KetNoiDatabase;

public class PhieuDatHangDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();
	NhanVienDAO nvDAO = new NhanVienDAO();
	KhachHangDAO khDAO = new KhachHangDAO();
	ThuocDAO thuocDAO = new ThuocDAO();

	public ArrayList<PhieuDatHang> layListPhieuDatHang() {
		ArrayList<PhieuDatHang> list = new ArrayList<>();

		String sql = """
				SELECT PDH.maPDH, PDH.maKH, KH.tenKH, PDH.maNV, NV.tenNV,
				       PDH.ngayDat, PDH.ngayHen, PDH.diaChiHT, PDH.tenHT,
				       PDH.ghiChu, PDH.hotline, PDH.trangThai
				FROM PhieuDatHang PDH
				JOIN KhachHang KH ON PDH.maKH = KH.maKH
				JOIN NhanVien NV ON PDH.maNV = NV.maNV
				ORDER BY TRY_CAST(SUBSTRING(PDH.maPDH, 6, LEN(PDH.maPDH)) AS INT)
				""";

		try (Connection conn = KetNoiDatabase.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String maPDH = rs.getString("maPDH");

				// Khách hàng
				KhachHang kh = new KhachHang();
				kh.setMaKH(rs.getString("maKH"));
				kh.setTenKH(rs.getString("tenKH"));

				// Nhân viên
				NhanVien nv = new NhanVien();
				nv.setMaNV(rs.getString("maNV"));
				nv.setTenNV(rs.getString("tenNV"));

				LocalDate ngayDat = rs.getDate("ngayDat").toLocalDate();
				LocalDate ngayHen = rs.getDate("ngayHen").toLocalDate();

				String diaChiHT = rs.getString("diaChiHT");
				String tenHT = rs.getString("tenHT");
				String ghiChu = rs.getString("ghiChu");
				String hotline = rs.getString("hotline");
				String trangThai = rs.getString("trangThai");

				PhieuDatHang pdh = new PhieuDatHang(maPDH, kh, nv, ngayDat, ngayHen, diaChiHT, tenHT, ghiChu, hotline,
						trangThai);
				list.add(pdh);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean themPhieuDatHang(String maPDH, String maKH, String maNV, LocalDate ngayDat, LocalDate ngayHen,
			String ghiChu) {
		String sql = "INSERT INTO PhieuDatHang (maPDH, maKH, maNV, ngayDat, ngayHen, ghiChu, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, 'Chờ hàng')";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ps.setString(2, maKH);
			ps.setString(3, maNV);
			ps.setDate(4, java.sql.Date.valueOf(ngayDat));
			ps.setDate(5, java.sql.Date.valueOf(ngayHen));
			ps.setString(6, ghiChu);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<Object[]> layDanhSachThuocTheoPDH(String maPDH) {
		ArrayList<Object[]> list = new ArrayList<>();
		String sql = """
				    SELECT
				        ct.maPDH,
				        ct.maThuoc,
				        t.tenThuoc,
				        ct.soLuong,
				        ct.maDVT,
				        dvt.tenDVT,
				        ct.donGia
				    FROM CT_PhieuDatHang ct
				    JOIN Thuoc t ON ct.maThuoc = t.maThuoc
				    JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
				    WHERE ct.maPDH = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maPDH"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT"), rs.getDouble("donGia") };
				list.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean themPhieuDatHangBangObject(Object[] pdh, String maNV) {
		String sql = "INSERT INTO PhieuDatHang (maPDH, maKH, maNV, ngayDat, ngayHen, diaChiHT, tenHT, ghiChu, hotline, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, pdh[0].toString());
			ps.setString(2, pdh[1].toString());
			ps.setString(3, pdh[2].toString());
			ps.setDate(4, java.sql.Date.valueOf(pdh[3].toString()));
			ps.setDate(5, java.sql.Date.valueOf(pdh[4].toString()));
			ps.setString(6, pdh[5].toString());
			ps.setString(7, pdh[6].toString());
			ps.setString(8, pdh[7] != null ? pdh[7].toString() : "");
			ps.setString(9, pdh[8].toString());
			ps.setString(10, pdh[9].toString());

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean themChiTietPhieuDatHangBangObject(Object[] ct) {
		String sql = "INSERT INTO CT_PhieuDatHang (maPDH, maThuoc, soLuong, donGia, maDVT) VALUES (?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ct[0].toString());
			ps.setString(2, ct[1].toString());
			ps.setInt(3, Integer.parseInt(ct[2].toString()));
			ps.setDouble(4, Double.parseDouble(ct[3].toString()));
			ps.setString(5, ct[4].toString());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String layMaPDHMoiNhat() {
		String sql = "SELECT TOP 1 pdh.maPDH\r\n" + "FROM PhieuDatHang pdh\r\n"
				+ "ORDER BY TRY_CAST(SUBSTRING(pdh.maPDH, 6, LEN(pdh.maPDH)) AS INT) DESC;";
		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString("maPDH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
		String sql = "UPDATE PhieuDatHang SET trangThai = ? WHERE maPDH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, trangThaiMoi);
			ps.setString(2, maPhieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
