import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Random;
import java.sql.PreparedStatement;

public class ElizaDB {
	public static void main(String[] args){

		Scanner in = new Scanner(System.in);
		Random rand = new Random();

		Connection con = null;
		ResultSet rs = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:sys as sysdba/oracle@localhost:1521:orcl");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");

			System.out.println("Hi, my name is Eliza!  Tell me about your day.");
			while(true){
				String response = in.nextLine();
				response.toLowerCase();
				if(response.equals("quit"))break;
				String[] words = response.split(" ");
				PreparedStatement pstmt = con.prepareStatement("select secondp from elizaswitch where firstp = ?");
				for(int i = 0;i<words.length;i++){
					pstmt.setString(1, words[i]);
					rs=pstmt.executeQuery();
					if(rs.next()){
						words[i]=rs.getString(1);
					}
				}
				
				int c = rand.nextInt(2);
				
				if(c==0){
					PreparedStatement p = con.prepareStatement("SELECT * FROM (SELECT * FROM elizahedge ORDER BY DBMS_RANDOM.RANDOM)WHERE  rownum=1");
					rs = p.executeQuery();
					if(rs.next())System.out.println(rs.getString(1));
				}
				else if(c==1){
					PreparedStatement p = con.prepareStatement("SELECT * FROM (SELECT * FROM elizaqualifier ORDER BY DBMS_RANDOM.RANDOM)WHERE  rownum=1");
					rs = p.executeQuery();
					if(rs.next()){
						String output = rs.getString(1);
						for(String word:words){
							output+= ' ' + word;
						}
						System.out.println(output);
					}
				}
			}
		}catch (SQLException e) {
			System.out.println("Problem");
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			System.out.println("Problem");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
}
