package DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAO {

	// DAO -> Data Access Object
	// 실제 데이터들이 DB 에 접근할 수 있도록 기능을 정리하는 곳.

	// 1. 드라이버 로딩.
	// 2. DB 연결.
	// 3. sql 문장 전송.
	// 4. 객체 반납.

	Connection conn = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	int result=0;

	// 드라이버 및 DB연결
	public void getCon() {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@project-db-campus.smhrd.com:1523:xe";
			String dbID = "mp_24K_bigdata13_p1_2";
			String dbPW = "smhrd2";

			conn = DriverManager.getConnection(url, dbID, dbPW);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 객체 반납 기능
	public void getClose() {
		try {
			if (rs != null)
				rs.close();
			if (psmt != null)
				psmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int insert(MemberVO vo)
	{
		getCon();
		//SQL 전송
		String sql = "INSERT INTO USER_INFO VALUES(?,?,?,?,?)";
		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			psmt.setString(3, vo.getName());
			psmt.setInt(4, vo.getAge());
			psmt.setInt(5, vo.getScore());
			
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return result;
	}
	public int insertWords(MemberVO vo)
	{
		getCon();
		//SQL 전송
		String sql = "INSERT INTO WORDS (WORD) VALUES(?)";
		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, vo.getWord());
			
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return result;
	}
	public String select(MemberVO vo)
	{
		getCon();
		//SQL 전송
		String sql = "SELECT ID FROM USER_INFO WHERE ID = ? AND PW = ?";
		String result = null;
		
		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			
			rs = psmt.executeQuery();
			if(rs.next())
			{
				System.out.println("넥스트트루");
				result = rs.getString("ID");
//				for (int i = 0; i < result.length(); i++) {
//				    if (Character.isUpperCase(result.charAt(i))) {
//				    	result.toLowerCase();
//				    }
//				}
			}
			else
				{
					result = null;
					System.out.println("널보낸다잉~");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return result;
	}
	public int selectScore(MemberVO vo)
	{
		getCon();
		//SQL 전송
		String sql = "SELECT SCORE FROM USER_INFO WHERE ID = ? AND PW = ?";
		int result = 0;
		
		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			
			rs = psmt.executeQuery();
			if(rs.next())
			{
				System.out.println("넥스트트루");
				result = Integer.valueOf(rs.getString("SCORE")).intValue();
//				for (int i = 0; i < result.length(); i++) {
//				    if (Character.isUpperCase(result.charAt(i))) {
//				    	result.toLowerCase();
//				    }
//				}
			}
			else
				{
					result = 0;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return result;
	}
	public int update(MemberVO vo)
	{
		getCon();
		//SQL 전송
		String sql = "UPDATE USER_INFO SET SCORE = ? WHERE ID = ? AND PW = ?";
		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, vo.getScore());
			psmt.setString(2, vo.getId());
			psmt.setString(3, vo.getPw());
			
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return result;
	}
	public int delete(MemberVO vo)
	{
		getCon();
		//SQL 전송
				String sql = "DELETE FROM USER_INFO WHERE ID = ? AND PW = ?";
				try {
					psmt = conn.prepareStatement(sql);
					
					psmt.setString(1, vo.getId());
					psmt.setString(2, vo.getPw());
					
					result = psmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				getClose();
				return result;
	}
	
	public ArrayList<MemberVO> selectAll()
	{
		getCon();
		String sql = "SELECT * FROM USER_INFO";
		ArrayList<MemberVO> arr = new ArrayList<>();
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			while(rs.next())
			{
				MemberVO vo = new MemberVO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5));
				arr.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return arr;
	}
	public MemberVO SelectAll()
	{
		getCon();
		String sql = "SELECT * FROM USER_INFO";
		MemberVO vo;
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			vo = new MemberVO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5));
			return vo;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return null;
	}
	public ArrayList<MemberVO> selectWordsAll()
	{
		getCon();
		String sql = "SELECT * FROM WORDS";
		ArrayList<MemberVO> arr = new ArrayList<>();
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			while(rs.next())
			{
				MemberVO vo = new MemberVO(rs.getString(1));
				arr.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return arr;
	}
	public ArrayList<MemberVO> getRanking()
	{
		getCon();
		String sql = "SELECT ID,NAME,SCORE FROM USER_INFO ORDER BY SCORE DESC";
		ArrayList<MemberVO> arr = new ArrayList<>();
		

		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			while(rs.next())
			{
				MemberVO vo = new MemberVO(rs.getString(1),null,rs.getString(2),0,rs.getInt(3));
				arr.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getClose();
		return arr;
	}
}

