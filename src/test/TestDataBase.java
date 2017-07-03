package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import main.DBConnection;

public class TestDataBase {

	private static final String TEST_USER = "testSelectUser";
	private static final String TEST_EMAIL = "testSelectEmail";
	private static final String TEST_WEBPAGE = "testSelectWebPage";
	private static final String TEST_SUMARY = "testSelectSumary";
	private static final String TEST_COMMENTS = "testSelectComment";
	



	//@Test
	public void testConnection(){
		DBConnection connector = new DBConnection("dbtest");
		boolean result = false;
		
		try {
			result = connector.connection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connector.close();
		}
		
		
		Assert.assertEquals(true, result);
		
		
	}
	
	//@Test
	public void testInsert(){
		DBConnection connector = new DBConnection("dbtest");
		
		int id=-1;
		
		try {
			connector.connection();
			id = connector.insert("testUser","testEmail","testWebPage","testSumary","testComment");
						
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connector.close();
		}
		
		Assert.assertEquals(true, id>-1);
		
	}
	
	//@Test
	public void testDelete(){
		DBConnection connector = new DBConnection("dbtest");		
		
		try {
			connector.connection();			
			connector.deleteAll();						
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connector.close();
		}		
				
	}
	
	
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testSelect(){
		DBConnection connector = new DBConnection("dbtest");
		ArrayList<Map> map = new ArrayList<Map>();
		int id=-1;	
		try {
			connector.connection();	
			id = connector.insert(TEST_USER,TEST_EMAIL,TEST_WEBPAGE,TEST_SUMARY,TEST_COMMENTS);
			map = connector.select(id);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connector.close();
		}	
		
		Assert.assertNotNull(map);
		Assert.assertEquals(1,map.size());
		
		HashMap<String, String> hasMap = (HashMap<String, String>) map.get(0);		
		
				
		Assert.assertEquals(TEST_USER, hasMap.get("user"));
		Assert.assertEquals(TEST_EMAIL, hasMap.get("email"));
		Assert.assertEquals(TEST_WEBPAGE, hasMap.get("webpage"));
		Assert.assertEquals(TEST_SUMARY, hasMap.get("summary"));
		Assert.assertEquals(TEST_COMMENTS, hasMap.get("comments"));
	}
	
	
}
