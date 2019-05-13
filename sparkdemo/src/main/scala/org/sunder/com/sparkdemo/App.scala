package org.sunder.com.sparkdemo

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import java.sql.DriverManager
import java.util._
import com.mysql.jdbc._
import org.apache.spark.sql.types._


/**
 * @author ${user.name}
 */
object App {
  
   def main(args:Array[String]){
      
     val spark = SparkSession
                 .builder()
                 .master("local")
                 .appName("demo")
                 .getOrCreate()
                 
                 import spark.implicits._
                 
     Class.forName("com.mysql.jdbc.Driver")
     val url = "jdbc:mysql://localhost:3306/employeeRoster?useUnicode=true&useJDBCCompliantTimezoneShift=false&useLegacyDatetimeCode=false&serverTimezone=IST&useSSL=false"
     val dataframe_mysql = spark.read.format("jdbc")
                                .option("url", url)
                                .option("driver", "com.mysql.jdbc.Driver")
                                .option("dbtable", "EMPLOYEEINDEMO")
                                .option("user", "root")
                                .option("password", "admin").load()
     val fet = spark.read.format("jdbc")
                                .option("url", url)
                                .option("driver", "com.mysql.jdbc.Driver")
                                .option("dbtable", "FET")
                                .option("user", "root")
                                .option("password", "admin").load()
                                .select($"ID".as("OUTID"),$"ETIME".as("OUTETIME"))
                                .filter(unix_timestamp($"ETIME","yyyy-MM-dd") >= unix_timestamp(lit("2019-03-17"),"yyyy-MM-dd"))
     
     /* Find first in for the current day */
                                
     val firstInDf1 = dataframe_mysql.join(fet,dataframe_mysql.col("ID") === fet.col("OUTID"),"outer").withColumn("IN_UNIX", lit(unix_timestamp($"IN_TIME","yyyy-MM-dd HH:mm:ss")))
                                    .withColumn("PREV_UNIX",lit(unix_timestamp($"OUTETIME","yyyy-MM-dd HH:mm:ss")))
                                    .filter( $"IN_UNIX" > $"PREV_UNIX" )
                                    .groupBy($"ID").agg(min("IN_UNIX").as("FIRST_IN")).select($"ID".as("PREV_ID"),$"FIRST_IN".as("FIRST_IN_UNIX"))
                                    
                                    
     val firstIn = firstInDf1.join(dataframe_mysql,dataframe_mysql.col("ID") === firstInDf1.col("PREV_ID") && unix_timestamp(dataframe_mysql.col("IN_TIME")) === firstInDf1.col("FIRST_IN_UNIX"),"inner")
                   .withColumn("SCH_END", lit($"FIRST_IN_UNIX" + 12 * 60 * 60))
                   .withColumn("NXT_STRT",lit($"FIRST_IN_UNIX" + 15 * 60 * 60))
                   
     val finalInDF = firstIn.select($"ID",$"IN_TIME",$"LOC",$"REG_CODE",$"COUNTRY_CODE",$"FIRSTT_IN_UNIX",$"SCH_END",$"NXT_STRT")
                     .sort($"ID")
                   
     val outEndDf = firstIn.select($"ID",$"IN_TIME",$"SCH_END",$"NXT_STRT")
                    
                   
     firstIn.show()
     
     /* sameday in same day out 
      * sameday in next day out*/
     
   }

}
