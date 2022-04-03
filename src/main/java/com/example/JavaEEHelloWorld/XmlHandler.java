package com.example.JavaEEHelloWorld;

import com.example.JavaEEHelloWorld.models.Album;
import com.example.JavaEEHelloWorld.models.Artist;
import com.example.JavaEEHelloWorld.models.Song;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;

public class XmlHandler {
    public static Document getXmlDocument(String xmlPath) throws IOException, SAXException, ParserConfigurationException {
        File xml = new File(xmlPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xml);
        doc.getDocumentElement().normalize();

        return doc;
    }

    public static List<Artist> getArtists(Document doc) {
        List<Artist> artists = new ArrayList<>();
        NodeList artistNodeList = doc.getElementsByTagName("Artist");
        for (int temp = 0; temp < artistNodeList.getLength(); temp++) {
            Node artistNode = artistNodeList.item(temp);
            String artistName = artistNode.getFirstChild().getNodeValue().trim();
            boolean artistLiked = parseBoolean(artistNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue().trim());
            artists.add(new Artist(artistName, artistLiked));
        }
        return artists;
    }

    public static Node getArtistNode(Document doc, String artist) {
        NodeList artistNodeList = doc.getElementsByTagName("Artist");
        for (int temp = 0; temp < artistNodeList.getLength(); temp++) {
            Node artistNode = artistNodeList.item(temp);
            String artistName = artistNode.getFirstChild().getNodeValue().trim();
            if (artistName.equals(artist)) {
                return artistNode;
            }
        }
        return null;
    }

    public static Node getAlbumNode(Document doc, String artist, String album) {
        Node artistNode = getArtistNode(doc, artist);
        if (artistNode != null) {
            NodeList artistChildNodes = artistNode.getChildNodes();
            for (int i = 0; i < artistChildNodes.getLength(); i++) {
                Node artistChildNode = artistChildNodes.item(i);
                if (artistChildNode.getNodeName().equals("Album")) {
                    String albumName = artistChildNode.getFirstChild().getNodeValue().trim();
                    if (albumName.equals(album)) {
                        return artistChildNode;
                    }
                }
            }
        }
        return null;
    }

    public static Node getSongNode(Document doc, String artist, String album, String song) {
//        Node artistNode = getArtistNode(doc, artist);
        Node albumNode = getAlbumNode(doc, artist, album);
        if (albumNode != null) {
            List<Node> songsNodes = getNodesByName(albumNode, "Songs");
            if (songsNodes.size() == 1) {
                Node songsNode = songsNodes.get(0);
                if (songsNode != null) {
                    List<Node> songNodes = getNodesByName(songsNode, "Song");
                    for (int i = 0; i < songNodes.size(); i++) {
                        Node songNode = songNodes.get(i);
                        if (songNode.getFirstChild().getNodeValue().trim().equals(song)) {
                            return songNode;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static List<Node> getNodesByName(Node rootNode, String name) {
        NodeList artistChildNodes = rootNode.getChildNodes();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < artistChildNodes.getLength(); i++) {
            Node artistChildNode = artistChildNodes.item(i);
            if (artistChildNode.getNodeName().equals(name)) {
                nodes.add(artistChildNode);
            }
        }
        return nodes;
    }

    public static List<Album> getAlbums(Document doc, String artist) {
//        List<String> artists = getArtists(doc);
        List<Album> albums = new ArrayList<>();
        Node artistNode = getArtistNode(doc, artist);
        if (artistNode != null) {
            List<Node> albumNodes = getNodesByName(artistNode, "Album");
            for (Node albumNode : albumNodes) {
                String albumTitle = albumNode.getFirstChild().getNodeValue().trim();
                boolean albumLiked = parseBoolean(albumNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue().trim());
                albums.add(new Album(albumTitle, albumLiked));
            }
        }
        return albums;
    }

    public static void replace(Node root, String textToReplace, String newText) {
        if (root.getNodeType() == root.TEXT_NODE) {
            root.setTextContent(root.getTextContent().replace("Home Owners Agreement", "HMO"));
        }
    }

    public static List<Song> getSongs(Document doc, String artist, String album) {
        List<Song> songs = new ArrayList<>();
        Node albumNode = getAlbumNode(doc, artist, album);
        if (albumNode != null) {
            List<Node> songsNodes = getNodesByName(albumNode, "Songs");
            if (!songsNodes.isEmpty()) {
                List<Node> songNodes = getNodesByName(songsNodes.get(0), "Song");
                for (Node songNode : songNodes) {
                    String SongTitle = songNode.getFirstChild().getNodeValue().trim();
                    boolean songLiked = parseBoolean(songNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue().trim());
                    songs.add(new Song(SongTitle, songLiked));
                }
            }
        }
        return songs;
    }
}
