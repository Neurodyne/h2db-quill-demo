package h2demo

import java.sql.DriverManager

import org.h2.tools.Server

object H2Server {

  def startMemoryServer() = {
    Server.createTcpServer("-tcpAllowOthers").start
    Class.forName("org.h2.Driver")
    DriverManager.getConnection("jdbc:h2:mem:testdb;MODE=PostgreSQL", "sa", "")
    Server.createWebServer().start()
  }

  def startPersistantServer() = {
    Server.createTcpServer("-tcpAllowOthers").start
    Class.forName("org.h2.Driver")
    DriverManager.getConnection("jdbc:h2:tcp://localhost/~/h2test;MODE=PostgreSQL;AUTO_SERVER=TRUE", "sa", "")
    Server.createWebServer().start()

  }
}
