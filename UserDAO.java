package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class UserDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	public UserDAO() {
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
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT USERPASSWORD FROM USER WHERE USERID = ?";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) 
					return 1;		
				else
					return 0;
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	
	public int join(UserVO userVO) {
		String SQL = "INSERT INTO USER VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userVO.getUserID());
			pstmt.setString(2, userVO.getUserPassword());
			pstmt.setString(3, userVO.getUserName());
			pstmt.setString(4, userVO.getUserYMD());
			pstmt.setString(5, userVO.getUserAge());
			pstmt.setString(6, userVO.getUserGender());
			pstmt.setString(7, userVO.getUserEmail());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public ArrayList<UserVO> getSearchList(int pageNumber, String word){
		ArrayList<UserVO> list = new ArrayList<UserVO>();		
		
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT * FROM USER");
		
		if ( !word.equals("") ) {
			SQL.append(" WHERE userCode ");
		}
		
		try {
			
			if ( !word.equals("") ) {
				SQL.append(" AND userID like ? ");				
			} 
			
			SQL.append("ORDER BY userCode ASC ");
			
			pstmt = conn.prepareStatement(SQL.toString());
			
			if ( !word.equals("") ) {
				pstmt.setString(1, "%" + word + "%");
			} 
				
			/*
			System.out.println("SQL : " + SQL.toString());
			System.out.println("word : " + word.toString());
			*/
			
			rs = pstmt.executeQuery();			
			
			while(rs.next()) {
				UserVO userVO = new UserVO();
				
				userVO.setUserCode(rs.getInt(1));
				userVO.setUserID(rs.getString(2));
				userVO.setUserName(rs.getString(4));
				userVO.setUserYMD(rs.getString(5));
				userVO.setUserAge(rs.getString(6));
				userVO.setUserGender(rs.getString(7));
				userVO.setUserEmail(rs.getString(8));
				list.add(userVO);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<UserVO> getUserList(int pageNumber){
		String SQL = "SELECT * FROM USER WHERE USERCODE ORDER BY USERCODE ASC";

		ArrayList<UserVO> list = new ArrayList<UserVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				UserVO userVO = new UserVO();
				
				userVO.setUserCode(rs.getInt(1));
				userVO.setUserID(rs.getString(2));
				userVO.setUserName(rs.getString(4));
				userVO.setUserYMD(rs.getString(5));
				userVO.setUserAge(rs.getString(6));
				userVO.setUserGender(rs.getString(7));
				userVO.setUserEmail(rs.getString(8));
				list.add(userVO);
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	
}
