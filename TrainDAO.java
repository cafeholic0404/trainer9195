package train;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TrainDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	public TrainDAO(){
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
	
	/*
	public int getNext() {
		String SQL = "SELECT trainCode FROM train ORDER BY trainCode DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) -1;	
			}	
			return 1;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; 
	}
	*/
	public int Register(String trainName, String trainType, String trainCnt, String writer, String writeDate, String modifier, 
			String modifyDate, String useYN){
		String SQL = "INSERT INTO TRAIN VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);		
			
			pstmt.setString(1, trainName);
			pstmt.setString(2, trainType);
			pstmt.setString(3, trainCnt);
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
	
	
	public ArrayList<TrainVO> getSearchList(int pageNumber, String word, String dangerBtn, String searchStartDate ,String searchEndDate){
		ArrayList<TrainVO> list = new ArrayList<TrainVO>();		
		
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT * FROM TRAIN WHERE trainCode");
		
		try {
			
			if ( !word.equals("") && dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? ");				
			} else if (word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("")){
				SQL.append(" AND trainType like ? ");			
			} else if ( word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND writeDate >= ? AND writeDate <= ? ");								
			} else if ( !word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? AND trainType like ?");
			} else if ( !word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? AND writeDate >= ?  AND writeDate <= ? ");	
			} else if ( word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND trainType like ? AND writeDate >= ? AND writeDate <= ? ");	
			} else if ( !word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? AND trainType like ? AND writeDate >= ? AND writeDate <= ? ");	
			}
			
			
			SQL.append(" AND useYN = 'Y' ORDER BY trainCode DESC ");
			
			pstmt = conn.prepareStatement(SQL.toString());
			
			if ( !word.equals("") && dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {				
				pstmt.setString(1, "%" + word + "%");
			} else if ( word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {					
				pstmt.setString(1, "%" + dangerBtn + "%");
			} else if ( word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				pstmt.setString(1, searchStartDate);
				pstmt.setString(2, searchEndDate);
			} else if ( !word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {	
				pstmt.setString(1, "%" + word + "%");
				pstmt.setString(2, "%" + dangerBtn + "%");
			} else if ( !word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {	
				pstmt.setString(1, "%" + word + "%");
				pstmt.setString(2, searchStartDate);
				pstmt.setString(3, searchEndDate);	
			} else if ( word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {	
				pstmt.setString(1, "%" + dangerBtn + "%");
				pstmt.setString(2, searchStartDate);
				pstmt.setString(3, searchEndDate);				
			} else if ( !word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				pstmt.setString(1, "%" + word + "%");
				pstmt.setString(2, "%" + dangerBtn + "%");
				pstmt.setString(3, searchStartDate);
				pstmt.setString(4, searchEndDate);
				
			}
			System.out.println("SQL : " + SQL.toString());
			/*
			System.out.println("SQL : " + SQL.toString());
			System.out.println("word : " + word.toString());
			System.out.println("dangerBtn : " + dangerBtn.toString());
			System.out.println("searchStartDate : " + searchStartDate.toString());
			System.out.println("searchEndDate : " + searchEndDate.toString());	
			*/
			rs = pstmt.executeQuery();						
			
			while(rs.next()) {
				TrainVO trainVO = new TrainVO();
				
				trainVO.setTrainCode(rs.getInt(1));
				trainVO.setTrainName(rs.getString(2));
				trainVO.setTrainType(rs.getString(3));
				trainVO.setTrainCnt(rs.getString(4));
				trainVO.setWriter(rs.getString(5));
				trainVO.setWriteDate(rs.getString(6));
				trainVO.setModifier(rs.getString(7));
				trainVO.setModifyDate(rs.getString(8));
				trainVO.setUseYN(rs.getString(9));
				list.add(trainVO);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/*
	public ArrayList<TrainVO> getList(int pageNumber){
		String SQL = "SELECT * FROM TRAIN WHERE useYN = 'Y' ORDER BY trainCode DESC";

		ArrayList<TrainVO> list = new ArrayList<TrainVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			//pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				TrainVO trainVO = new TrainVO();
				
				trainVO.setTrainCode(rs.getInt(1));
				trainVO.setTrainName(rs.getString(2));
				trainVO.setTrainType(rs.getString(3));
				trainVO.setTrainCnt(rs.getString(4));
				trainVO.setWriter(rs.getString(5));
				trainVO.setWriteDate(rs.getString(6));
				trainVO.setModifier(rs.getString(7));
				trainVO.setModifyDate(rs.getString(8));
				trainVO.setUseYN(rs.getString(9));
				list.add(trainVO);
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	*/
	public ArrayList<TrainVO> getSearchListUser(int pageNumber, String userID, String word, String dangerBtn, String searchStartDate ,String searchEndDate){
		ArrayList<TrainVO> list = new ArrayList<TrainVO>();		
		
		StringBuffer SQL = new StringBuffer();
		
		
		SQL.append("SELECT * FROM TRAIN WHERE trainCode AND writer = ? ");
		
		try {
			
			if ( !userID.equals("") && !word.equals("") && dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? ");				
			} else if ( !userID.equals("") && word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("")){
				SQL.append(" AND trainType like ? ");			
			} else if ( !userID.equals("") && word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND writeDate >= ? AND writeDate <= ? ");								
			} else if ( !userID.equals("") && !word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? AND trainType like ?");
			} else if ( !userID.equals("") && !word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? AND writeDate >= ?  AND writeDate <= ? ");	
			} else if ( !userID.equals("") && word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND trainType like ? AND writeDate >= ? AND writeDate <= ? ");	
			} else if ( !userID.equals("") && !word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				SQL.append(" AND trainName like ? AND trainType like ? AND writeDate >= ? AND writeDate <= ? ");	
			}
			
			
			SQL.append(" AND useYN = 'Y' ORDER BY trainCode DESC ");
			
			pstmt = conn.prepareStatement(SQL.toString());
			
			if ( !userID.equals("") && !word.equals("") && dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {				
				pstmt.setString(1, userID);
				pstmt.setString(2, "%" + word + "%");
			} else if ( !userID.equals("") && word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {					
				pstmt.setString(1, userID);
				pstmt.setString(2, "%" + dangerBtn + "%");
			} else if ( !userID.equals("") &&  word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				pstmt.setString(1, userID);
				pstmt.setString(2, searchStartDate);
				pstmt.setString(3, searchEndDate);
			} else if ( !userID.equals("") && !word.equals("") && !dangerBtn.equals("") && searchStartDate.equals("") && searchEndDate.equals("") ) {	
				pstmt.setString(1, userID);
				pstmt.setString(2, "%" + word + "%");
				pstmt.setString(3, "%" + dangerBtn + "%");
			} else if ( !userID.equals("") && !word.equals("") && dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {	
				pstmt.setString(1, userID);
				pstmt.setString(2, "%" + word + "%");
				pstmt.setString(3, searchStartDate);
				pstmt.setString(4, searchEndDate);	
			} else if ( !userID.equals("") && word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {	
				pstmt.setString(1, userID);
				pstmt.setString(2, "%" + dangerBtn + "%");
				pstmt.setString(3, searchStartDate);
				pstmt.setString(4, searchEndDate);				
			} else if ( !userID.equals("") && !word.equals("") && !dangerBtn.equals("") && !searchStartDate.equals("") && !searchEndDate.equals("") ) {
				pstmt.setString(1, userID);
				pstmt.setString(2, "%" + word + "%");
				pstmt.setString(3, "%" + dangerBtn + "%");
				pstmt.setString(4, searchStartDate);
				pstmt.setString(5, searchEndDate);
				
			}
			
			
			System.out.println("SQL : " + SQL.toString());
			System.out.println("userID : " + userID.toString());
			System.out.println("word : " + word.toString());
			System.out.println("dangerBtn : " + dangerBtn.toString());
			System.out.println("searchStratDate : " + searchStartDate.toString());
			System.out.println("searchEndDate : " + searchEndDate.toString());
			
			/*
			System.out.println("SQL : " + SQL.toString());
			System.out.println("word : " + word.toString());
			System.out.println("dangerBtn : " + dangerBtn.toString());
			System.out.println("searchStartDate : " + searchStartDate.toString());
			System.out.println("searchEndDate : " + searchEndDate.toString());	
			*/
			
			rs = pstmt.executeQuery();						
			
			while(rs.next()) {
				TrainVO trainVO = new TrainVO();
				
				trainVO.setTrainCode(rs.getInt(1));
				trainVO.setTrainName(rs.getString(2));
				trainVO.setTrainType(rs.getString(3));
				trainVO.setTrainCnt(rs.getString(4));
				trainVO.setWriter(rs.getString(5));
				trainVO.setWriteDate(rs.getString(6));
				trainVO.setModifier(rs.getString(7));
				trainVO.setModifyDate(rs.getString(8));
				trainVO.setUseYN(rs.getString(9));
				list.add(trainVO);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public ArrayList<TrainVO> getListUser(int pageNumber, String userID){
		String SQL = "SELECT * FROM TRAIN WHERE writer = ? AND USEYN = 'Y' ORDER BY TRAINCODE DESC";

		ArrayList<TrainVO> list = new ArrayList<TrainVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);	
			//pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			pstmt.setString(1, userID);
			
			rs = pstmt.executeQuery();
			
			//System.out.println("userID : " + userID.toString());
			
			while(rs.next()) {
				
				TrainVO trainVO = new TrainVO();
				
				trainVO.setTrainCode(rs.getInt(1));
				trainVO.setTrainName(rs.getString(2));
				trainVO.setTrainType(rs.getString(3));
				trainVO.setTrainCnt(rs.getString(4));
				trainVO.setWriter(rs.getString(5));
				trainVO.setWriteDate(rs.getString(6));
				trainVO.setModifier(rs.getString(7));
				trainVO.setModifyDate(rs.getString(8));
				trainVO.setUseYN(rs.getString(9));
				list.add(trainVO);
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	
	/*
	public ArrayList<TrainVO> getCntList(int pageNumber){

		String SQL = "SELECT ingredientdanger, count(*) from ingredientdangercode group by ingredientdanger order by ingredientdanger";
		
		ArrayList<TrainVO> list = new ArrayList<TrainVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				TrainVO ingredientCodeVO = new TrainVO();
				
				ingredientCodeVO.setIngredientDanger(rs.getString(1));
				ingredientCodeVO.setDangerCnt(rs.getInt("count(*)"));
				list.add(ingredientCodeVO);
			}
			
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	*/
	/*
	public ArrayList<TrainVO> getCntList2(int pageNumber){
		String SQL = "SELECT ingredientuse, count(*) from ingredientdangercode group by ingredientuse order by ingredientuse";
		
		ArrayList<TrainVO> list = new ArrayList<TrainVO>();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				TrainVO ingredientCodeVO = new TrainVO();
				
				ingredientCodeVO.setIngredientUse(rs.getString(1));
				ingredientCodeVO.setUseCnt(rs.getInt("count(*)"));
				list.add(ingredientCodeVO);
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return list;
	}
	*/
	public TrainVO getTrainCode(int trainCode) {		
		
		String SQL = "SELECT * FROM TRAIN WHERE trianCode = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, trainCode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				TrainVO trainVO = new TrainVO();
				
				trainVO.setTrainCode(rs.getInt(1));
				trainVO.setWriter(rs.getString(2));
				trainVO.setWriter(rs.getString(3));
				trainVO.setWriteDate(rs.getString(4));
				trainVO.setModifier(rs.getString(5));
				trainVO.setModifyDate(rs.getString(6));
				trainVO.setUseYN(rs.getString(7));
				return trainVO;
			}
		}catch(Exception e) {
			e.printStackTrace();			
		}		
		return null;
	}
	
	/*
	public int Update(int trainCode, String trainName, String trainCnt,	String modifyDate) {
		String SQL = "UPDATE TRAIN SET trainName=?, trainCnt=?, modifyDate=? WHERE trainCode=?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, trainName);
			pstmt.setString(2, trainCnt);
			pstmt.setString(3, getDate());
			pstmt.setInt(4, trainCode);
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int delete(int trainCode) {
		String SQL = "UPDATE TRAIN SET USEYN = 'N' WHERE trainCode = ?";
		try {
	         PreparedStatement pstmt = conn.prepareStatement(SQL);
	         pstmt.setInt(1, trainCode);
	         return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	*/
}




