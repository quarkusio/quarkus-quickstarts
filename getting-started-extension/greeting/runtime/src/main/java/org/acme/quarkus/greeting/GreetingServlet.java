package org.acme.quarkus.greeting;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet
public class GreetingServlet extends HttpServlet { // <1>

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException { // <2>
        resp.getWriter().write("Hello");
    }
}