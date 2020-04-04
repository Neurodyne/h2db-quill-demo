package h2demo

import org.flywaydb.core.Flyway

import h2demo.H2Config.AppConfig

object H2Server {

  def start(cfg: AppConfig) = {
    val dataSource = if (cfg.server.serverType == "memory") cfg.db.memurl else cfg.db.diskurl
    Flyway
      .configure()
      .validateMigrationNaming(true)
      .dataSource(dataSource, cfg.db.user, cfg.db.pass)
      .load()
      .migrate()
  }

}
