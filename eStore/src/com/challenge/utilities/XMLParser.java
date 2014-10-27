/**
 * @author RAMESH
 *
 */
package com.challenge.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {

	private static XMLParser parser;

	private String ITEM_SEARCH_BASE_URL = "http://www.supermarketapi.com/api.asmx/COMMERCIAL_SearchByProductName?APIKEY=a785cac0f3&ItemName=";

	private XMLParser() {

	}

	public static XMLParser getInstance() {
		if (parser == null)
			parser = new XMLParser();

		return parser;
	}

	public Document getDocumentFromUrlData(String url) {

		Document doc = null;
		try {
			URL urlToRead = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) (urlToRead)
					.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();
			InputStream inputstream = connection.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					inputstream));

			StringBuilder dataRead = new StringBuilder();
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				dataRead.append(inputLine);
			}

			in.close();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(dataRead.toString()));
			doc = db.parse(is);
			doc.getDocumentElement().normalize();

		} catch (MalformedURLException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public ArrayList<ItemDetails> iItemList(String itemToSearch) {
		return getItemListFromUrl(itemToSearch);
	}

	private ArrayList<ItemDetails> getItemListFromUrl(String itemToSearch) {
		ArrayList<ItemDetails> itemList = new ArrayList<ItemDetails>();

		Document doc = getDocumentFromUrlData(ITEM_SEARCH_BASE_URL
				+ itemToSearch);

		if (doc == null)
			return itemList;

		NodeList nList = doc.getElementsByTagName("Product_Commercial");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			NodeList nl = nNode.getChildNodes();

			ItemDetails itemDetails = new ItemDetails();

			for (int i = 0; i < nl.getLength(); i++) {

				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Node eElement = (Node) nl.item(i);
					if (eElement.getNodeName().equalsIgnoreCase("Itemname")) {
						itemDetails.setName(eElement.getTextContent());
					} else if (eElement.getNodeName()
							.equalsIgnoreCase("ItemID"))
						itemDetails.setID(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase(
							"ItemDescription"))
						itemDetails.setDescription(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase(
							"ItemCategory"))
						itemDetails.setCategory(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase(
							"ItemImage"))
						itemDetails.setImageLocation(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase("Pricing")
							&& eElement.getTextContent() != "NOPRICE") {
						try {
							itemDetails.setPricing(Double.parseDouble(eElement
									.getTextContent()));
						} catch (Exception e) {
							itemDetails.setPricing(0.0);
						}
					}

					else if (eElement.getNodeName().equalsIgnoreCase(
							"AisleNumber"))
						itemDetails.setAisleNumber(eElement.getTextContent());
				}
			}
			itemList.add(itemDetails);
		}

		return itemList;
	}
}
