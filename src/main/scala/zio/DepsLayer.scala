package h2demo.zio

import java.{ util => ju }

import scala.jdk.CollectionConverters._

import io.getquill.{ H2JdbcContext, SnakeCase }

import com.typesafe.config.{ Config, ConfigFactory }

import h2demo.H2Config._

import zio.{ Has, Task, ZIO, ZLayer }

object DepsLayer {
  type ZDeps = Has[ZDeps.Service]
  type DBCtx = H2JdbcContext[SnakeCase]
  type Resp  = List[Entry]

  object ZDeps {
    trait Service {
      def create(): Task[Long]
      def read(): Task[Resp]
    }

    val live = ZLayer.fromFunction { cfg: AppConfig =>
      new Service {
        lazy val ctx: DBCtx = new H2JdbcContext(
          SnakeCase,
          getConfig()
        )
        import ctx._

        private def getConfig(): Config = {
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
          ZIO.effect(
            run(query[Entry].insert(_.checked -> true, _.date -> lift(ju.Calendar.getInstance().getTime.toString)))
          )

        def read(): Task[Resp] = ZIO.effect(
          run(query[Entry].filter(_.checked == true))
        )
      }
    }
    def create(): ZIO[ZDeps, Throwable, Long] = ZIO.accessM(_.get.create)
    def read(): ZIO[ZDeps, Throwable, Resp]   = ZIO.accessM(_.get.read)
  }
}
