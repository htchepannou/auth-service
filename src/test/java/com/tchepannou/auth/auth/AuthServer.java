package com.tchepannou.auth.auth;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class AuthServer {
    private Server server;

    public AuthServer start (int port, Handler handler) throws Exception {
        server = new Server(port);
        server.setHandler(handler);
        server.start();
        Thread.sleep(100);

        return this;
    }

    public AuthServer stop () throws Exception {
        server.stop();

        return this;
    }

    //-- Inner Class
    public static class OKHandler extends AbstractHandler {
        private String result;

        public OKHandler(String result) {
            this.result = result;
        }

        @Override
        public void handle(String uri, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
            httpServletResponse.addHeader("Content-Type", "application/json");
            httpServletResponse.setStatus(200);
            InputStream in = getClass().getResourceAsStream(result);
            IOUtils.copy(in, httpServletResponse.getOutputStream());
            request.setHandled(true);
        }
    }

    public static class FailHandler extends AbstractHandler {
        private int code;
        public FailHandler(int code){
            this.code = code;
        }
        @Override
        public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
                throws IOException, ServletException {
            httpServletResponse.setStatus(code);
            request.setHandled(true);
        }
    }

}
