/**
 * 不同年份上映电影数
 */

import org.apache.spark.sql.{SaveMode, SparkSession}

import java.util.Properties

object AnalysisResult7 {
  def main(args: Array[String]): Unit = {
    //    static {
    //    System.setProperty("HADOOP_HOME", "C:\\Users\\Administrator\\Desktop\\大数据豆瓣电影数据分析与可视化\\数据分析\\SparkMovies\\apache-hadoop-3.1.3-winutils-master")
    //    System.setProperty("hadoop.home.dir", "C:\\Users\\Administrator\\Desktop\\大数据豆瓣电影数据分析与可视化\\数据分析\\SparkMovies\\apache-hadoop-3.1.3-winutils-master")
    //    System.load("C:\\Users\\Administrator\\Desktop\\大数据豆瓣电影数据分析与可视化\\数据分析\\SparkMovies\\apache-hadoop-3.1.3-winutils-master\\bin\\hadoop.dll")
    //    }
    System.setProperty("HADOOP_HOME", "C:\\Users\\Administrator\\Desktop\\大数据豆瓣电影数据分析与可视化\\数据分析\\SparkMovies\\apache-hadoop-3.1.3-winutils-master")
    System.setProperty("hadoop.home.dir", "C:\\Users\\Administrator\\Desktop\\大数据豆瓣电影数据分析与可视化\\数据分析\\SparkMovies\\apache-hadoop-3.1.3-winutils-master")
    System.load("C:\\Users\\Administrator\\Desktop\\大数据豆瓣电影数据分析与可视化\\数据分析\\SparkMovies\\apache-hadoop-3.1.3-winutils-master\\bin\\hadoop.dll")
    // 创建spark对象
    val spark = SparkSession.builder()
      .appName("movies")
      .master("local[*]")
      .getOrCreate()

    // 读取数据进行数据分析
    val data = spark.read.format("csv")
      .option("header", "true")
      .load("hdfs://192.168.79.131:9000/user/moqiling/data.csv")

    // 创建临时表
    data.createTempView("movies")

    // SparkSql数据分析
    val scoreCountData = spark.sql(
      """
        |select `评分` as name, count(*) as value
        |from movies
        |group by `评分` order by value desc
        |""".stripMargin)

    scoreCountData.show()

    val url = "jdbc:mysql://192.168.79.131:3306"
    val database = "db_spark_movie"
    val user = "root"
    val password = "123"

    scoreCountData.write.mode(SaveMode.Append)
      .jdbc(s"$url/$database", "part7", connectionProperties(user, password))
  }
  // 创建链接属性对象
  def connectionProperties(user:String, password:String):java.util.Properties={
    val properties = new Properties()
    properties.setProperty("user",user)
    properties.setProperty("password",password)
    properties
  }
}
