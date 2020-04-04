package h2demo
import java.{ util => ju }

import scala.jdk.CollectionConverters._

import io.getquill.H2JdbcContext
import io.getquill.SnakeCase

import com.typesafe.config.{ ConfigFactory }

import H2Config._

class H2Client(val cfg: AppConfig) {
  lazy val ctx: H2JdbcContext[SnakeCase] = new H2JdbcContext(
    SnakeCase,
    getConfig()
  )
  import ctx._

  private def getConfig() = {
    lazy val memMap = Map(
      "dataSourceClassName" -> cfg.db.className,
      "dataSource.url"      -> cfg.db.memurl,
      "dataSource.user"     -> cfg.db.user,
      "dataSource.password" -> cfg.db.pass
    ).asJava

    lazy val diskMap = Map(
      "dataSourceClassName" -> cfg.db.className,
      "dataSource.url"      -> cfg.db.diskurl,
      "dataSource.user"     -> cfg.db.user,
      "dataSource.password" -> cfg.db.pass
    ).asJava

    if (cfg.server.serverType == "memory") ConfigFactory.parseMap(memMap)
    else ConfigFactory.parseMap(diskMap)
  }

  def create() =
    run(query[Entry].insert(_.checked -> true, _.date -> lift(ju.Calendar.getInstance().getTime.toString)))

  def read() = println(run(query[Entry].filter(_.checked == true)))

  def update() = {
    Thread.sleep(1000)
    run(query[Entry].update(_.checked -> true, _.date -> lift(ju.Calendar.getInstance().getTime.toString)))
    println(run(query[Entry].filter(_.checked == true)))
  }

  def delete() = run(query[Entry].delete)

}
