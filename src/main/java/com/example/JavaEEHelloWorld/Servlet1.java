package com.example.JavaEEHelloWorld;

import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.JavaEEHelloWorld.XmlHandler.*;

@WebServlet
public class Servlet1 extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedData = request.getParameter("data");
        PrintWriter out = null;
        String jsonString = "";
        if (requestedData != null && !requestedData.equals("")) {
            String xmlDir = getServletContext().getInitParameter("xml-dir");
            String xmlFilename = getServletContext().getInitParameter("xml-name");
            if (xmlDir == null || xmlDir.equals(""))
                throw new ServletException("Invalid or non-existent xml-dir context-param.");
            if (xmlFilename == null || xmlFilename.equals(""))
                throw new ServletException("Invalid or non-existent xml-name context-param.");

            try {

//            stream = response.getOutputStream();
                out = response.getWriter();
                String xmlPath = getServletContext().getRealPath(xmlDir + "/" + xmlFilename);
                Document doc = getXmlDocument(xmlPath);
                if (requestedData.equals("artists")) {
                    jsonString = new Gson().toJson(getArtists(doc));
                } else if (requestedData.equals("albums")) {
                    String artist = request.getParameter("artist");
                    if (artist != null && !artist.equals("")) {
                        jsonString = new Gson().toJson(getAlbums(doc, artist));
                    }
                } else if (requestedData.equals("songs")) {
                    String artist = request.getParameter("artist");
                    String album = request.getParameter("album");
                    if (artist != null && !artist.equals("") && album != null && album != "") {
                        jsonString = new Gson().toJson(getSongs(doc, artist, album));
                    }
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jsonString);
                out.flush();

            } catch (IOException | ParserConfigurationException | SAXException exception) {

                throw new ServletException(exception.getMessage());

            } finally {
                if (out != null)
                    out.close();
            }
        }


//        ServletOutputStream stream = null;
//        BufferedInputStream buf = null;

    }
}
