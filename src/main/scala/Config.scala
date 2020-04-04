package h2demo

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object H2Config {
  final case class Entry(checked: Boolean, date: String)

  final case class Server(serverType: String, host: String, port: Int)

  final case class DBConfig(
    className: String,
    memurl: String,
    diskurl: String,
    user: String,
    pass: String
  )

  final case class AppConfig(
    server: Server,
    db: DBConfig
  )

  def loadConfig() = ConfigSource.default.load[AppConfig]

}
