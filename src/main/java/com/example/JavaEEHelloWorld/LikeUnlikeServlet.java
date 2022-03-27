package com.example.JavaEEHelloWorld;

import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.example.JavaEEHelloWorld.XmlHandler.*;

@WebServlet
public class LikeUnlikeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dataToModify = request.getParameter("data");
        if (dataToModify != null) {
            String xmlDir = getServletContext().getInitParameter("xml-dir");
            String xmlFilename = getServletContext().getInitParameter("xml-name");
            String xmlPath = getServletContext().getRealPath(xmlDir + "/" + xmlFilename);
            if (xmlDir == null || xmlDir.equals(""))
                throw new ServletException("Invalid or non-existent xml-dir context-param.");
            if (xmlFilename == null || xmlFilename.equals(""))
                throw new ServletException("Invalid or non-existent xml-name context-param.");
            String artist = request.getParameter("artist");
            String album = request.getParameter("album");
            String action = request.getParameter("action");
            try {
                if (dataToModify.equals("album")) {
                    if (artist != null && album != null &&
                            !artist.equals("") && !album.equals("") &&
                            action != null && !action.equals("")) {
                        changeLikeAlbum(xmlPath, artist, album, action);
                    }
                } else if (dataToModify.equals("artist")) {
                    if (artist != null && !artist.equals("") &&
                            action != null && !action.equals("")) {
                        changeLikeArtist(xmlPath, artist, action);
                    }
                }
            } catch (ParserConfigurationException | SAXException | TransformerException exception) {
                throw new ServletException(exception.getMessage());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
    }

    private void changeLike(String action, Node root) {
        Node likedNode = root.getFirstChild().getNextSibling();
        if (action.equals("like")) {
            likedNode.getFirstChild().setTextContent("true");
        } else if (action.equals("unlike")) {
            likedNode.getFirstChild().setTextContent("false");
        }
    }

    private void changeLikeAlbum(String xmlPath, String artist, String album, String action) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        Document doc = getXmlDocument(xmlPath);
        Node artistNode = getArtistNode(doc, artist);
        if (artistNode != null) {
            List<Node> albumNodes = getNodesByName(artistNode, "Album");
            for (Node albumNode : albumNodes) {
                String albumTitle = albumNode.getFirstChild().getNodeValue().trim();
                if (albumTitle.equals(album)) {
                    changeLike(action, albumNode);
                    saveDocumentChanges(doc, xmlPath);
                }
            }
        }
    }

    private void changeLikeArtist(String xmlPath, String artist, String action) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        Document doc = getXmlDocument(xmlPath);
        Node artistNode = getArtistNode(doc, artist);
        if (artistNode != null) {
            changeLike(action, artistNode);
            saveDocumentChanges(doc, xmlPath);
        }
    }

    private void saveDocumentChanges(Document doc, String xmlPath) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        Result output = new StreamResult(new File(xmlPath));
        transformer.transform(source, output);
    }
}
