package review;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import comment.CommentVO;

public class ReviewDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public ReviewDAO() {
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
		
	
	public int reviewRegister(String reviewTitle, String reviewContent, String writer,
			String writeDate, String modifier, String modifyDate, String useYN){
		String SQL = "INSERT INTO REVIEW VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);		
			
			pstmt.setString(1, reviewTitle);
			pstmt.setString(2, reviewContent);			
			pstmt.setString(3, writer);
			pstmt.setString(4, getDate());
			pstmt.setString(5, modifier);
			pstmt.setString(6, modifyDate);
			pstmt.setString(7, "Y");
			return pstmt.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; 
	}
		
	
	public ArrayList<ReviewVO> getList(int pageNumber){
		String SQL = "select R.*, count(C.reviewCode) from review R left join comment C on R.reviewCode = C.reviewCode group by R.reviewCode;";

		ArrayList<ReviewVO> list = new ArrayList<ReviewVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ReviewVO reviewVO = new ReviewVO();
				
				reviewVO.setReviewCode(rs.getInt(1));
				reviewVO.setReviewTitle(rs.getString(2));
				reviewVO.setReviewContent(rs.getString(3));				
				reviewVO.setWriter(rs.getString(4));
				reviewVO.setWriteDate(rs.getString(5));
				reviewVO.setModifier(rs.getString(6));
				reviewVO.setModifyDate(rs.getString(7));
				reviewVO.setUseYN(rs.getString(8));
				reviewVO.setCommentCnt(rs.getInt("count(C.reviewCode)"));
				
				list.add(reviewVO);
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	
	public ArrayList<CommentVO> getCntList(int reviewCode){
		String SQL = "SELECT * FROM COMMENT WHERE COMMENTCODE AND reviewCode = ? AND USEYN = 'Y' ORDER BY COMMENTCODE ASC";

		ArrayList<CommentVO> list = new ArrayList<CommentVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);		
			pstmt.setInt(1, reviewCode);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
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
				list.add(commentVO);
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	
	public ReviewVO getReviewCode(int reviewCode) {		
		String SQL = "SELECT * FROM REVIEW WHERE reviewCode = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, reviewCode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
								
				ReviewVO reviewVO = new ReviewVO();
				
				reviewVO.setReviewCode(rs.getInt(1));
				reviewVO.setReviewTitle(rs.getString(2));
				reviewVO.setReviewContent(rs.getString(3));				
				reviewVO.setWriter(rs.getString(4));
				reviewVO.setWriteDate(rs.getString(5));
				reviewVO.setModifier(rs.getString(6));
				reviewVO.setModifyDate(rs.getString(7));
				reviewVO.setUseYN(rs.getString(8));
				return reviewVO;
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return null;
	}
		

	public int Update(int reviewCode, String reviewTitle, String reviewContent, String modifier, String modifyDate) {
		String SQL = "UPDATE REVIEW SET reviewTitle=?, reviewContent = ?, modifier = ?, modifyDate = ? WHERE reviewCode =?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, reviewTitle);
			pstmt.setString(2, reviewContent);
			pstmt.setString(3, modifier);
			pstmt.setString(4, getDate());
			pstmt.setInt(5, reviewCode);
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; 
	}
	
	
	 public int delete(int reviewCode) {
	      String SQL = "UPDATE REVIEW SET USEYN = 'N' WHERE reviewCode = ?";
	      try {
	         PreparedStatement pstmt = conn.prepareStatement(SQL);
	         pstmt.setInt(1, reviewCode);
	         return pstmt.executeUpdate();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return -1;
	   }

}