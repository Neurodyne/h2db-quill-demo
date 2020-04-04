package h2demo

object Demo extends App {

  for {
    cfg <- H2Config.loadConfig()
    _   = println(cfg)

    _ = if (cfg.server.serverType == "memory") H2Server.startMemoryServer(cfg.db)
    else H2Server.startPersistantServer(cfg.db)

    _ = H2Client.work(cfg)
  } yield ()

}
