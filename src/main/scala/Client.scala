package h2demo
import java.{ util => ju }

import scala.jdk.CollectionConverters._

import io.getquill.H2JdbcContext
import io.getquill.SnakeCase

import com.typesafe.config.{ ConfigFactory }

import H2Config._

object H2Client {

  def getConfig(cfg: AppConfig) =
    ConfigFactory.parseMap(
      Map(
        "dataSourceClassName" -> cfg.database.className,
        "dataSource.url"      -> cfg.database.url,
        "dataSource.user"     -> cfg.database.user,
        "dataSource.password" -> cfg.database.pass
      ).asJava
    )

  def work(cfg: AppConfig) = {
    lazy val ctx: H2JdbcContext[SnakeCase] = new H2JdbcContext(
      SnakeCase,
      getConfig(cfg)
    )

    import ctx._

    ctx.executeAction(
      """CREATE TABLE IF NOT EXISTS todo (
        |  checked BOOLEAN,
        |  date VARCHAR
        |);
        |""".stripMargin
    )

    run(query[Entry].insert(_.checked -> true, _.date -> lift(ju.Calendar.getInstance().getTime.toString)))

    println(run(query[Entry].filter(_.checked == true)))
  }
}
