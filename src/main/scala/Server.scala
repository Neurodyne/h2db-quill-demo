package h2demo

import org.flywaydb.core.Flyway

import h2demo.H2Config.DBConfig

object H2Server {

  def startMemoryServer(cfg: DBConfig) =
    Flyway
      .configure()
      .dataSource(cfg.memurl, cfg.user, cfg.pass)
      .load()
      .migrate()

  def startPersistantServer(cfg: DBConfig) =
    Flyway
      .configure()
      .dataSource(cfg.diskurl, cfg.user, cfg.pass)
      .load()
      .migrate()

}
