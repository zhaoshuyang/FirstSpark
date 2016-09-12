package com.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


 
/**
 * 在Win7上，使用JDBC操作Hive
 * @author qindongliang
 * 
 * 大数据技术交流群：376932160
 * **/
public class HiveJDBClient {
  
  /**Hive的驱动字符串*/
  private static String driver="org.apache.hive.jdbc.HiveDriver";
  
  
  
  public static void main(String[] args) throws Exception{
    //加载Hive驱动
      Class.forName(driver);
    //获取hive2的jdbc连接，注意默认的数据库是default
      Connection conn=DriverManager.getConnection("jdbc:hive2://192.168.68.128:10000/hive", "hive", "hive");
      Statement st=conn.createStatement();
      String tableName="mytt";//表名
      ResultSet rs=st.executeQuery("select  avg(count) from "+tableName+" ");//求平均数,会转成MapReduce作业运行
      //ResultSet rs=st.executeQuery("select  * from "+tableName+" ");//查询所有,直接运行
      while(rs.next()){
      	System.out.println(rs.getString(1)+"   ");
      }
      System.out.println("成功!");
      st.close();
      conn.close();
    
  }
  
  
  

}