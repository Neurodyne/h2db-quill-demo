package h2demo
import java.{ util => ju }

import scala.jdk.CollectionConverters._

import io.getquill.H2JdbcContext
import io.getquill.SnakeCase

import com.typesafe.config.{ ConfigFactory }

import H2Config._

object H2Client {

  private def getConfig(cfg: AppConfig) = {
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

  def work(cfg: AppConfig) = {
    lazy val ctx: H2JdbcContext[SnakeCase] = new H2JdbcContext(
      SnakeCase,
      getConfig(cfg)
    )

    import ctx._

    run(query[Entry].insert(_.checked -> true, _.date -> lift(ju.Calendar.getInstance().getTime.toString)))

    println(run(query[Entry].filter(_.checked == true)))
    println("Done")
  }
}
