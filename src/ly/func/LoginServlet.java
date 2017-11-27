package ly.func;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("LoginServlet")
public class LoginServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response); 
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		
		String  logname = request.getParameter("username");
		String  passwd = request.getParameter("password");
		
		logname = logname.toLowerCase();
		
		System.out.println("user:"+logname);
		new MD5();
		String logpwd = MD5.GetMD5Code(passwd);
  
//加载数据库驱动
        try{
	         try{
	            Class. forName("oracle.jdbc.driver.OracleDriver").newInstance();
	        }catch(IllegalAccessException e){
	        	e.printStackTrace();
	        }catch(InstantiationException e){
	        	e.printStackTrace();
	        }
         } catch(ClassNotFoundException se){
            se.printStackTrace();
            System.out.println("未找到连接数据库驱动" );
         }
		
		
		String dbString = "jdbc:oracle:thin:@localhost:1521/ORCL";
		String dbuser = "muser";
		String dbpwd = "muser";
		ResultSet rSet = null;
		Statement stat = null;
		Connection conn = null;
		HttpSession session = request.getSession() ;
		try {
			conn = DriverManager.getConnection(dbString, dbuser, dbpwd);
			stat = conn.createStatement();
			String sqlString = "select * from loguser where  logname = '"+logname+"' and passwords = '"+logpwd+"'";
			rSet = stat.executeQuery(sqlString);
			if(rSet.next()){
				String usernametr = rSet.getString("username");
				String dept = rSet.getString("department");
				String logintime = rSet.getString("logintime");
				
				session.setAttribute("uname",usernametr );
				session.setAttribute("dept", dept);
				response.sendRedirect("./mainpage.html");
				conn.close();
			}else{
				
				response.sendRedirect("index.html");
				response.setStatus(404);
				conn.close();
				System.out.println("查无此人！");
			}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			response.sendRedirect("index.html");
			e.printStackTrace();
			System.out.println("数据库连接失败！");
		}
		
		
		
		
		
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
