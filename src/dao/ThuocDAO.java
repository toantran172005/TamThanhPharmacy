package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.DonViTinh;
import entity.KeThuoc;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhaCungCap;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;

public class ThuocDAO {

	ToolCtrl tool = new ToolCtrl();

	public String layTenDonViTinhTheoMaThuoc(String maThuoc) {
		String sql = "SELECT dvt.tenDVT FROM Thuoc t " + "JOIN DonViTinh dvt ON t.maDVT = dvt.maDVT "
				+ "WHERE t.maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, maThuoc);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getString("tenDVT");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String layMaThuocTheoTen(String tenThuoc) {
		String sql = "SELECT maThuoc FROM Thuoc WHERE tenThuoc = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, tenThuoc);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("maThuoc");
				} else {
					return null; // không tìm thấy
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean tangSoLuongTon(String maThuoc, String maDVT, int soLuongDoiTra) {
		String sqlLayTiLe = "SELECT tiLe FROM Thuoc_DonViTinh WHERE maThuoc = ? AND maDVT = ?";
		String sqlCapNhat = "UPDATE CT_Kho SET soLuongTon = soLuongTon + ? WHERE maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement psLayTiLe = con.prepareStatement(sqlLayTiLe);
				PreparedStatement psCapNhat = con.prepareStatement(sqlCapNhat)) {

			// Lấy tỉ lệ quy đổi
			psLayTiLe.setString(1, maThuoc);
			psLayTiLe.setString(2, maDVT);

			double tiLe = 1; // Mặc định nếu không có đơn vị quy đổi
			try (ResultSet rs = psLayTiLe.executeQuery()) {
				if (rs.next()) {
					tiLe = rs.getDouble("tiLe");
				}
			}

			// Tính số lượng thực tế cần cộng (theo đơn vị nhỏ nhất)
			double soLuongThucTe = soLuongDoiTra * tiLe;

			// Cập nhật tồn kho
			psCapNhat.setDouble(1, soLuongThucTe);
			psCapNhat.setString(2, maThuoc);

			int rowsAffected = psCapNhat.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Giảm số lượng tồn khi bán hàng
	public boolean giamSoLuongTon(String maThuoc, String maDVT, int soLuongBan) {
		String sqlLayTiLe = "SELECT tiLe FROM Thuoc_DonViTinh WHERE maThuoc = ? AND maDVT = ?";
		String sqlCapNhat = "UPDATE CT_Kho SET soLuongTon = soLuongTon - ? WHERE maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement psLayTiLe = con.prepareStatement(sqlLayTiLe);
				PreparedStatement psCapNhat = con.prepareStatement(sqlCapNhat)) {

			// 1️⃣ Lấy tỉ lệ quy đổi
			psLayTiLe.setString(1, maThuoc);
			psLayTiLe.setString(2, maDVT);

			double tiLe = 1; // Mặc định nếu không có đơn vị quy đổi
			try (ResultSet rs = psLayTiLe.executeQuery()) {
				if (rs.next()) {
					tiLe = rs.getDouble("tiLe");
				}
			}

			// 2️⃣ Tính số lượng thực tế cần trừ (theo đơn vị nhỏ nhất)
			double soLuongThucTe = soLuongBan * tiLe;

			// 3️⃣ Cập nhật tồn kho
			psCapNhat.setDouble(1, soLuongThucTe);
			psCapNhat.setString(2, maThuoc);

			int rowsAffected = psCapNhat.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public double layGiaBanTheoDVT(String maThuoc, String maDVT) {
		String sql = "SELECT giaBan FROM Thuoc_DonViTinh WHERE maThuoc = ? AND maDVT = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, maThuoc);
			stmt.setString(2, maDVT);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getDouble("giaBan");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String layHoacTaoThue(Connection conn, Thuoc t) throws SQLException {
		String sqlFindThue = "SELECT maThue FROM Thue WHERE loaiThue = ? AND tyLeThue = ?";
		String sqlInsertThue = "INSERT INTO Thue (maThue, loaiThue, tyLeThue, moTa) VALUES (?, ?, ?, ?)";

		try (PreparedStatement psFind = conn.prepareStatement(sqlFindThue);
				PreparedStatement psInsert = conn.prepareStatement(sqlInsertThue)) {

			psFind.setString(1, t.getLoaiThue());
			psFind.setDouble(2, t.getThue().getTiLeThue() / 100.0);
			ResultSet rs = psFind.executeQuery();
			if (rs.next()) {
				return rs.getString("maThue");
			}
			rs.close();

			// Nếu chưa có thì tạo mới
			String maThue = tool.taoKhoaChinh("T");
			psInsert.setString(1, maThue);
			psInsert.setString(2, t.getLoaiThue());
			psInsert.setDouble(3, t.getThue().getTiLeThue() / 100.0);
			psInsert.setString(4, "Thuế mới");
			psInsert.executeUpdate();
			return maThue;
		}
	}

	public String layHoacTaoDVT(Connection conn, Thuoc t) throws SQLException {
		String sqlFindDVT = "SELECT maDVT FROM DonViTinh WHERE tenDVT = ?";
		String sqlInsertDVT = "INSERT INTO DonViTinh (maDVT, tenDVT) VALUES (?, ?)";

		try (PreparedStatement psFind = conn.prepareStatement(sqlFindDVT);
				PreparedStatement psInsert = conn.prepareStatement(sqlInsertDVT)) {

			psFind.setString(1, t.getDvt().getTenDVT());
			ResultSet rs = psFind.executeQuery();
			if (rs.next()) {
				return rs.getString("maDVT");
			}
			rs.close();

			String maDVT = tool.taoKhoaChinh("DVT");
			psInsert.setString(1, maDVT);
			psInsert.setString(2, t.getDvt().getTenDVT());
			psInsert.executeUpdate();
			return maDVT;
		}
	}

	public void insertThuoc(Connection conn, String maNCC, ArrayList<Thuoc> listThuoc) {
		String sqlCheckThuoc = "SELECT maThuoc, maKe FROM Thuoc WHERE maThuoc = ?";
		String sqlInsertThuoc = "INSERT INTO Thuoc (maThuoc, maThue, maNCC, maKe, tenThuoc, dangThuoc, giaBan, hanSuDung, trangThai, anh, maKM, maDVT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String sqlInsertKe = "INSERT INTO KeThuoc (maKe, loaiKe, sucChua, moTa, trangThai) VALUES (?, ?, ?, ?, ?)";
		String sqlCheckCTKho = "SELECT soLuongTon FROM CT_Kho WHERE maThuoc = ?";
		String sqlInsertCTKho = "INSERT INTO CT_Kho (maKho, maThuoc, soLuongTon, ghiChu) VALUES (?, ?, ?, ?)";
		String sqlUpdateCTKho = "UPDATE CT_Kho SET soLuongTon = soLuongTon + ? WHERE maThuoc = ?";

		try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckThuoc);
				PreparedStatement psInsert = conn.prepareStatement(sqlInsertThuoc);
				PreparedStatement psInsertKe = conn.prepareStatement(sqlInsertKe);
				PreparedStatement psCheckCTKho = conn.prepareStatement(sqlCheckCTKho);
				PreparedStatement psInsertCTKho = conn.prepareStatement(sqlInsertCTKho);
				PreparedStatement psUpdateCTKho = conn.prepareStatement(sqlUpdateCTKho)) {

			for (Thuoc t : listThuoc) {

				// --- Lấy hoặc tạo mã thuế và đơn vị tính ---
				String maThue = layHoacTaoThue(conn, t);
				String maDVT = layHoacTaoDVT(conn, t);
				t.getThue().setMaThue(maThue);
				t.getDvt().setMaDVT(maDVT);

				// --- Kiểm tra thuốc đã tồn tại ---
				psCheck.setString(1, t.getMaThuoc());
				ResultSet rs = psCheck.executeQuery();

				if (rs.next()) {
					// Thuốc đã có → cập nhật số lượng trong CT_Kho
					psCheckCTKho.setString(1, t.getMaThuoc());
					ResultSet rsKho = psCheckCTKho.executeQuery();
					if (rsKho.next()) {
						// Nếu đã có tồn kho → cộng dồn
						psUpdateCTKho.setInt(1, t.getSoLuong());
						psUpdateCTKho.setString(2, t.getMaThuoc());
						psUpdateCTKho.executeUpdate();
					} else {
						// Nếu chưa có → tạo mới
						String maKho = tool.taoKhoaChinh("KHO");
						psInsertCTKho.setString(1, maKho);
						psInsertCTKho.setString(2, t.getMaThuoc());
						psInsertCTKho.setInt(3, t.getSoLuong());
						psInsertCTKho.setString(4, "Nhập kho tự động");
						psInsertCTKho.executeUpdate();
					}
					rsKho.close();
				} else {
					// Thuốc mới → tạo kệ mới
					String maKe = tool.taoKhoaChinh("KT"); // tạo mã kệ
					String tenKe = "Kệ của thuốc mã: " + t.getMaThuoc();

					// Insert kệ mới
					psInsertKe.setString(1, maKe);
					psInsertKe.setString(2, tenKe);
					psInsertKe.setInt(3, t.getSoLuong()); // đặt số lượng nhập làm sucChua ban đầu
					psInsertKe.setString(4, "Kệ tự tạo khi nhập thuốc mới");
					psInsertKe.setBoolean(5, true);
					psInsertKe.executeUpdate();

					// Insert thuốc mới, gán maKe vừa tạo
					psInsert.setString(1, t.getMaThuoc());
					psInsert.setString(2, maThue);
					psInsert.setString(3, maNCC);
					psInsert.setString(4, maKe);
					psInsert.setString(5, t.getTenThuoc());
					psInsert.setString(6, t.getDangThuoc());
					psInsert.setDouble(7, t.getGiaBan());
					psInsert.setDate(8, tool.localDateSangSqlDate(t.getHanSuDung()));
					psInsert.setBoolean(9, true);
					psInsert.setString(10, "");
					psInsert.setString(11, null);
					psInsert.setString(12, maDVT);
					psInsert.executeUpdate();

					// Insert kho mới
					String maKho = tool.taoKhoaChinh("K");
					psInsertCTKho.setString(1, maKho);
					psInsertCTKho.setString(2, t.getMaThuoc());
					psInsertCTKho.setInt(3, t.getSoLuong());
					psInsertCTKho.setString(4, "Kho tự tạo khi nhập thuốc mới");
					psInsertCTKho.executeUpdate();

					// Hiển thị thông báo thuốc mới
					tool.hienThiThongBao("Thuốc mới", "Phát hiện thuốc mới có mã " + t.getMaThuoc(), true);
				}

				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			tool.hienThiThongBao("Lỗi khi lưu thuốc", "Không thể lưu dữ liệu thuốc!", false);
		}
	}

	public void insertPhieuNhapThuoc(Connection conn, String maPNT, String maNCC, String maNV, LocalDate ngayNhap)
			throws SQLException {
		String sql = """
				    INSERT INTO PhieuNhapThuoc (maPNT, maNCC, maNV, ngayNhap)
				    VALUES (?, ?, ?, ?)
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maPNT);
			ps.setString(2, maNCC);
			ps.setString(3, maNV);
			ps.setDate(4, tool.localDateSangSqlDate(ngayNhap));
			ps.executeUpdate();
		}
	}

	public void insertChiTietPhieuNhap(Connection conn, String maPNT, ArrayList<Thuoc> listThuoc) throws SQLException {
		String sqlCT = """
				    INSERT INTO CT_PhieuNhapThuoc (maPNT, maThuoc, soLo, soLuongNhap, donGiaNhap)
				    VALUES (?, ?, ?, ?, ?)
				""";

		try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
			for (Thuoc t : listThuoc) {
				ps.setString(1, maPNT);
				ps.setString(2, t.getMaThuoc());
				ps.setInt(3, t.getSoLo());
				ps.setInt(4, t.getSoLuong());
				ps.setDouble(5, t.getGiaBan());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	public boolean luuData(String maPNT, String maNCC, String maNV, LocalDate ngayNhap, ArrayList<Thuoc> listThuoc) {
		try (Connection conn = KetNoiDatabase.getConnection()) {
			conn.setAutoCommit(false);

			insertPhieuNhapThuoc(conn, maPNT, maNCC, maNV, ngayNhap);
			insertChiTietPhieuNhap(conn, maPNT, listThuoc);
			insertThuoc(conn, maNCC, listThuoc);

			conn.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<Object[]> layDanhSachThuocChoKM() {
		ArrayList<Object[]> listThuoc = new ArrayList<>();
		String query = "select t.maThuoc, t.tenThuoc, kt.loaiKe, dvt.tenDVT, cthd.donGia\r\n"
				+ "from [dbo].[DonViTinh] dvt JOIN [dbo].[Thuoc] t on dvt.maDVT = t.maDVT JOIN [dbo].[CT_HoaDon] cthd on t.maThuoc = cthd.maThuoc join [dbo].[KeThuoc] kt on t.maKe = kt.maKe\r\n"
				+ "where t.trangThai = 1\r\n" + "order by TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') as int)";

		try (Connection con = KetNoiDatabase.getConnection()) {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(query);

			while (res.next()) {
				Object[] row = new Object[5];

				row[0] = res.getString("maThuoc");
				row[1] = res.getString("tenThuoc");
				row[2] = res.getString("loaiKe");
				row[3] = res.getString("tenDVT");
				row[4] = res.getDouble("donGia");

				listThuoc.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listThuoc;
	}
	
	// ========== LẤY LIST QUỐC GIA SẢN XUẤT ==========
	public ArrayList<QuocGia> layListQuocGiaTheoThuoc(String tenThuoc) {
	    ArrayList<QuocGia> listQG = new ArrayList<>();

	    String sql = """
	        SELECT qg.maQuocGia, qg.tenQuocGia 
	        FROM Thuoc t
	        JOIN QuocGia qg ON t.maQuocGia = qg.maQuocGia
	        WHERE t.tenThuoc = ?
	    """;

	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, tenThuoc);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            String maQG = rs.getString("maQuocGia");
	            String tenQG = rs.getString("tenQuocGia");
	            listQG.add(new QuocGia(maQG, tenQG));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return listQG;
	}
	
	public String timMaQGTheoTen(String tenQG) {
		String sql = "SELECT maQuocGia FROM QuocGia WHERE tenQuocGia = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, tenQG);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("maQuocGia");
				} else {
					return null; // không tìm thấy
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public ArrayList<Thuoc> layListThuocHoanChinh() {
		ArrayList<Thuoc> listThuoc = new ArrayList<>();
		String sql = """
				    SELECT
				        t.maThuoc, t.tenThuoc, t.dangThuoc, t.giaBan, t.hanSuDung, t.trangThai, t.anh,
				        t.maNCC, t.maThue, t.maDVT, t.maKe, ctK.soLuongTon,
				        th.loaiThue, th.tyLeThue, th.moTa,
				        dvt.tenDVT,
				        kt.loaiKe, kt.sucChua, kt.moTa,
				        ncc.tenNCC, ncc.sdt, ncc.diaChi, ncc.email, qg.tenQuocGia
				    FROM Thuoc t
				    LEFT JOIN CT_Kho ctK ON t.maThuoc = ctK.maThuoc
				    LEFT JOIN Thue th ON t.maThue = th.maThue
				    LEFT JOIN DonViTinh dvt ON t.maDVT = dvt.maDVT
				    LEFT JOIN KeThuoc kt ON t.maKe = kt.maKe
				    LEFT JOIN NhaCungCap ncc ON t.maNCC = ncc.maNCC
				    LEFT JOIN QuocGia qg ON t.maQuocGia = qg.maQuocGia
				    WHERE t.trangThai = 1
				    ORDER BY TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') AS INT)
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				// ====== Thông tin con ======
				Thue thue = new Thue(rs.getString("maThue"), rs.getString("loaiThue"), rs.getDouble("tyLeThue"),
						rs.getString("moTa"));

				DonViTinh dvt = new DonViTinh(rs.getString("maDVT"), rs.getString("tenDVT"));

				KeThuoc keThuoc = new KeThuoc(rs.getString("maKe"), rs.getString("loaiKe"), rs.getInt("sucChua"),
						rs.getString("moTa"), true);

				NhaCungCap ncc = new NhaCungCap(rs.getString("maNCC"), rs.getString("tenNCC"), rs.getString("diaChi"),
						rs.getString("sdt"), rs.getString("email"));

				// ====== Thông tin chính ======
				String maThuoc = rs.getString("maThuoc");
				String tenThuoc = rs.getString("tenThuoc");
				String dangThuoc = rs.getString("dangThuoc");
				float giaBan = rs.getFloat("giaBan");
				LocalDate hanSuDung = tool.sqlDateSangLocalDate(rs.getDate("hanSuDung"));
				boolean trangThai = rs.getBoolean("trangThai");
				String anh = rs.getString("anh");
				int soLuongTon = rs.getInt("soLuongTon");
				String noiSanXuat = rs.getString("tenQuocGia");

				// ====== Gộp thành Thuoc ======
				Thuoc thuoc = new Thuoc(maThuoc, thue, keThuoc, dvt, ncc, tenThuoc, dangThuoc, giaBan, hanSuDung,
						trangThai, anh, soLuongTon, noiSanXuat);

				listThuoc.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listThuoc;
	}

	public ArrayList<Thuoc> layListThuoc() {
		ArrayList<Thuoc> listThuoc = new ArrayList<>();

		String sql = "SELECT t.maThuoc, t.maThue, t.maNCC, t.maKe, t.tenThuoc, t.dangThuoc,\r\n"
				+ "				           giaBan, hanSuDung, trangThai, anh, maDVT, ctK.soLuongTon\r\n"
				+ "FROM Thuoc t JOIN [dbo].[CT_Kho] ctK on t.maThuoc = ctK.maThuoc\r\n"
				+ "ORDER BY TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') AS INT)";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				// Khởi tạo các entity con (chỉ cần mã, vì các bảng phụ có thể join sau)
				Thue thue = new Thue(rs.getString("maThue"));
				KeThuoc keThuoc = new KeThuoc(rs.getString("maKe"));
				DonViTinh dvt = new DonViTinh(rs.getString("maDVT"));
				NhaCungCap ncc = new NhaCungCap(rs.getString("maNCC"));
				// Lấy dữ liệu chính của Thuoc
				String maThuoc = rs.getString("maThuoc");
				String tenThuoc = rs.getString("tenThuoc");
				String dangThuoc = rs.getString("dangThuoc");
				float giaBan = rs.getFloat("giaBan");
				LocalDate hanSuDung = rs.getDate("hanSuDung").toLocalDate();
				Boolean trangThai = rs.getBoolean("trangThai");
				String anh = rs.getString("anh");
				int soLuong = rs.getInt("soLuongTon");

				// Tạo đối tượng Thuoc
				Thuoc thuoc = new Thuoc(maThuoc, thue, keThuoc, dvt, ncc, tenThuoc, dangThuoc, giaBan, hanSuDung,
						trangThai, anh, soLuong);

				listThuoc.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listThuoc;
	}

	public Thuoc timThuocTheoMa(String maThuoc) {
		ArrayList<Thuoc> listThuoc = layListThuoc();
		for (Thuoc thuoc : listThuoc) {
			if (thuoc.getMaThuoc().equalsIgnoreCase(maThuoc))
				return thuoc;
		}
		return null;
	}

	public String layMaKMTheoMaThuoc(String maThuoc) {
		String sql = "SELECT maKM FROM Thuoc WHERE maThuoc = ?";
		try (Connection conn = KetNoiDatabase.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maThuoc);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("maKM");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double layDonGiaTheoMaThuoc(String maThuoc) {
		double donGia = 0;
		String sql = "SELECT giaBan FROM Thuoc WHERE maThuoc = ?";
		try (Connection conn = KetNoiDatabase.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maThuoc);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				donGia = rs.getDouble("giaBan");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return donGia;
	}

	// xoá thuốc
	public boolean xoaThuoc(String maTH) {
		String sql = "update [dbo].[Thuoc]\r\n" + "set [trangThai] = 0\r\n" + "where [maThuoc] = ? ";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maTH);
			int res = ps.executeUpdate();
			return res > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// Khôi phục thuốc
	public boolean khoiPhucTH(String maTH) {
		String sql = "update [dbo].[Thuoc]\r\n" + "set [trangThai] = 1\r\n" + "where [maThuoc] = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maTH);
			int res = ps.executeUpdate();
			return res > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// lấy danh sách đã xoá
	public ArrayList<Thuoc> layDanhSachDaXoa() {
		ArrayList<Thuoc> listThuocXoa = new ArrayList<>();

		String sql = """
				    SELECT
				        t.maThuoc, t.tenThuoc, t.dangThuoc, t.giaBan, t.hanSuDung, t.trangThai, t.anh,
				        t.maNCC, t.maThue, t.maDVT, t.maKe, ctK.soLuongTon,
				        th.loaiThue, th.tyLeThue, th.moTa,
				        dvt.tenDVT,
				        kt.loaiKe, kt.sucChua, kt.moTa,
				        ncc.tenNCC, ncc.sdt, ncc.diaChi, ncc.email
				    FROM Thuoc t
				    JOIN CT_Kho ctK ON t.maThuoc = ctK.maThuoc
				    JOIN Thue th ON t.maThue = th.maThue
				    JOIN DonViTinh dvt ON t.maDVT = dvt.maDVT
				    JOIN KeThuoc kt ON t.maKe = kt.maKe
				    JOIN NhaCungCap ncc ON t.maNCC = ncc.maNCC
				    WHERE t.trangThai = 0
				    ORDER BY TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') AS INT)
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				// ====== Thông tin con ======
				Thue thue = new Thue(rs.getString("maThue"), rs.getString("loaiThue"), rs.getDouble("tyLeThue"),
						rs.getString("moTa"));

				DonViTinh dvt = new DonViTinh(rs.getString("maDVT"), rs.getString("tenDVT"));

				KeThuoc keThuoc = new KeThuoc(rs.getString("maKe"), rs.getString("loaiKe"), rs.getInt("sucChua"),
						rs.getString("moTa"), true);

				NhaCungCap ncc = new NhaCungCap(rs.getString("maNCC"), rs.getString("tenNCC"), rs.getString("diaChi"),
						rs.getString("sdt"), rs.getString("email")

				);

				// ====== Thông tin chính ======
				String maThuoc = rs.getString("maThuoc");
				String tenThuoc = rs.getString("tenThuoc");
				String dangThuoc = rs.getString("dangThuoc");
				float giaBan = rs.getFloat("giaBan");
				LocalDate hanSuDung = tool.sqlDateSangLocalDate(rs.getDate("hanSuDung"));
				boolean trangThai = rs.getBoolean("trangThai");
				String anh = rs.getString("anh");
				int soLuongTon = rs.getInt("soLuongTon");

				// ====== Gộp thành Thuoc ======
				Thuoc thuoc = new Thuoc(maThuoc, thue, keThuoc, dvt, ncc, tenThuoc, dangThuoc, giaBan, hanSuDung,
						trangThai, anh, soLuongTon);

				listThuocXoa.add(thuoc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listThuocXoa;
	}

	// cập nhật thuốc
	public boolean capNhatThuoc(Thuoc thuoc) {
		String sql = """
				    UPDATE Thuoc
				    SET tenThuoc = ?, dangThuoc = ?, giaBan = ?, hanSuDung = ?,
				        maDVT = ?,
				        maThue = ?,
				        maKe = ?,
				        anh = ?,
				        trangThai = ?
				    WHERE maThuoc = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, thuoc.getTenThuoc());
			ps.setString(2, thuoc.getDangThuoc());
			ps.setDouble(3, thuoc.getGiaBan());
			ps.setDate(4, tool.localDateSangSqlDate(thuoc.getHanSuDung()));
			ps.setString(5, thuoc.getDvt().getMaDVT());
			ps.setString(6, thuoc.getThue().getMaThue());
			ps.setString(7, thuoc.getKeThuoc().getMaKe());
			ps.setString(8, thuoc.getAnh());
			ps.setBoolean(9, thuoc.getTrangThai());
			ps.setString(10, thuoc.getMaThuoc());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Tìm mã theo giá trị của combobox
	public static String timMaTheoGiaTri(String tenBang, String tenCot, String ma, String giaTri) {
		String sql = "SELECT " + ma + " FROM " + tenBang + " WHERE " + giaTri + " = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, giaTri);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getString(ma);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Thêm thuốc
	public boolean themThuoc(Thuoc thuoc) {
		String sql = "INSERT INTO Thuoc (maThuoc, maThue, maNCC, maKe, tenThuoc, dangThuoc, giaBan, hanSuDung, trangThai, anh, maDVT) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String maThue = thuoc.getThue().getMaThue();
		String maNCC = thuoc.getNcc().getMaNCC();
		String maKe = thuoc.getKeThuoc().getMaKe();
		String maDVT = thuoc.getDvt().getMaDVT();

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, thuoc.getMaThuoc());
			ps.setString(2, maThue);
			ps.setString(3, maNCC);
			ps.setString(4, maKe);
			ps.setString(5, thuoc.getTenThuoc());
			ps.setString(6, thuoc.getDangThuoc());
			ps.setDouble(7, thuoc.getGiaBan());
			ps.setDate(8, tool.localDateSangSqlDate(thuoc.getHanSuDung()));
			ps.setBoolean(9, thuoc.getTrangThai());
			ps.setString(10, thuoc.getAnh());
			ps.setString(11, maDVT);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// lấy danh sách top 10 thuốc bán chạy
	public ArrayList<Thuoc> layListTHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		ArrayList<Thuoc> list = new ArrayList<>();

		String query = "SELECT TOP 10 \r\n" + "    t.maThuoc,\r\n" + "    t.tenThuoc,\r\n"
				+ "    SUM(ct.soLuong) AS soLuongBan,\r\n" + "    SUM(ct.soLuong * ct.donGia) AS doanhThu\r\n"
				+ "FROM CT_HoaDon ct\r\n" + "JOIN HoaDon hd ON ct.maHD = hd.maHD\r\n"
				+ "JOIN Thuoc t ON ct.maThuoc = t.maThuoc\r\n" + "WHERE hd.ngayLap BETWEEN ? AND ?\r\n"
				+ "GROUP BY t.maThuoc, t.tenThuoc\r\n" + "ORDER BY soLuongBan DESC;";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setDate(1, java.sql.Date.valueOf(ngayBD));
			pstmt.setDate(2, java.sql.Date.valueOf(ngayKT));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new Thuoc(rs.getString("maThuoc"), rs.getString("tenThuoc"), rs.getInt("soLuongBan"),
						rs.getInt("doanhThu")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Thuoc layThuocDeDat(Thuoc th) {
		String sql = "select t.maThuoc, t.tenThuoc, dvt.maDVT, dvt.tenDVT\r\n"
				+ "  from [dbo].[Thuoc] t JOIN [dbo].[DonViTinh] dvt on dvt.maDVT = t.maDVT\r\n"
				+ "  where t.maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, th.getMaThuoc());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					DonViTinh dvt = new DonViTinh();
					dvt.setMaDVT(rs.getString("maDVT"));
					dvt.setTenDVT(rs.getString("tenDVT"));

					Thuoc thuoc = new Thuoc();
					thuoc.setMaThuoc(rs.getString("maThuoc"));
					thuoc.setTenThuoc(rs.getString("tenThuoc"));
					thuoc.setDvt(dvt);

					return thuoc;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int laySoLuongTon(String maThuoc) {
		String sql = "SELECT soLuongTon\r\n" + "			From [dbo].[CT_Kho]\r\n"
				+ "					where maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maThuoc);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt("soLuongTon");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


}
