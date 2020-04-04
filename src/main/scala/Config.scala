package h2demo

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object H2Config {
  final case class Entry(checked: Boolean, date: String)

  final case class Server(host: String, port: Int)

  final case class Database(
    className: String,
    url: String,
    user: String,
    pass: String
  )

  final case class AppConfig(
    server: Server,
    database: Database
  )

  def loadConfig() = ConfigSource.default.load[AppConfig]

}
