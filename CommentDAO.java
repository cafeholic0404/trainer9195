package comment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



public class CommentDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public CommentDAO(){
		try {
			String dbURL = "jdbc:mysql://localhost:3306/HEALTH?autoReconnect=true&useSSL=false";
			String dbID = "root";
			String dbPassword = "1234";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);	
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int CommentRegister(int reviewCode, String commentTitle, String commentContent, String writer,
			String writeDate, String modifier, String modifyDate, String useYN){
		String SQL = "INSERT INTO comment VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);	
			
			pstmt.setInt(1, reviewCode);
			pstmt.setString(2, commentTitle);
			pstmt.setString(3, commentContent);			
			pstmt.setString(4, writer);
			pstmt.setString(5, getDate());
			pstmt.setString(6, modifier);
			pstmt.setString(7, modifyDate);
			pstmt.setString(8, "Y");
			return pstmt.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; 
	}
	
	public CommentVO getCommentCode(int commentCode) {		
		
		String SQL = "SELECT * FROM comment WHERE commentCode AND commentCode = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, commentCode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				CommentVO commentVO = new CommentVO();
				
				commentVO.setCommentCode(rs.getInt(1));
				commentVO.setReviewCode(rs.getInt(2));
				commentVO.setCommentTitle(rs.getString(3));
				commentVO.setCommentContent(rs.getString(4));				
				commentVO.setWriter(rs.getString(5));
				commentVO.setWriteDate(rs.getString(6));
				commentVO.setModifier(rs.getString(7));
				commentVO.setModifyDate(rs.getString(8));
				commentVO.setUseYN(rs.getString(9));			
				
				return commentVO;
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return null;
	}
}
