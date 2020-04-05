package h2demo.zio

import h2demo.H2Config.Entry
import h2demo.zio.DepsLayer.ZDeps

import zio.{ Has, Task, ZIO, ZLayer }

object ClientLayer {
  type ZClient = Has[ZClient.Service]
  type Resp    = List[Entry]

  object ZClient {

    trait Service {
      def create(): Task[Long]
      def read(): Task[Resp]
    }

    val live = ZLayer.fromService { deps: ZDeps.Service =>
      new Service {

        def create(): Task[Long] = deps.create()
        def read(): Task[Resp]   = deps.read() /* >>= (r => deps.console.get.putStrLn(r.toString)) */

      }

    }

    def create(): ZIO[ZClient, Throwable, Long] = ZIO.accessM(_.get.create)
    def read(): ZIO[ZClient, Throwable, Resp]   = ZIO.accessM(_.get.read)
  }

}
