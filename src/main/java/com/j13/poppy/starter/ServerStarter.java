package com.j13.poppy.starter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ServerStarter {

    private static Logger LOG = LoggerFactory.getLogger(ServerStarter.class);
    private JettyServer instance = null;
    private static String tmpDir = null;
    private static int port = 8081;


    public ServerStarter() {

    }

    public void run() throws Exception {
        // init server
        JettyServerConfig jettyServerConfig = new JettyServerConfig();
        jettyServerConfig.setPort(port);
        jettyServerConfig.setServerName("");
        jettyServerConfig.setTmpBaseDir(tmpDir);
        instance = new JettyServer(jettyServerConfig);
        instance.init();

        instance.start();

    }


    public static void main(String[] args) throws Exception {
        tmpDir = args[0];
        if (args.length > 1) {
            port = new Integer(args[1]);
        }
        ServerStarter starter = new ServerStarter();
        starter.run();

    }
}
