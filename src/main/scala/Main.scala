package h2demo

object Demo extends App {

  for {
    cfg <- H2Config.loadConfig()
    _   = println(s"Starting ${cfg.server.serverType} server")
    _   = H2Server.start(cfg)

    client = new H2Client(cfg)
    _      = client.create()
    _      = client.read()
    _      = client.update()
    _      = client.delete()
  } yield ()

}
