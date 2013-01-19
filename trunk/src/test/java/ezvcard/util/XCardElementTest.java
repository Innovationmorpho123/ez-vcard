package ezvcard.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import ezvcard.VCardVersion;

/*
 Copyright (c) 2013, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * @author Michael Angstadt
 */
public class XCardElementTest {
	@Test
	public void constructor_string() throws Exception {
		XCardElement xcardElement = new XCardElement("prop");
		Document document = xcardElement.getDocument();
		Element element = xcardElement.getElement();

		assertEquals(element, XmlUtils.getRootElement(document));
		assertEquals("prop", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals(0, element.getChildNodes().getLength());
	}

	@Test
	public void get() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one><two>2</two></prop>");
		assertEquals("2", xcardElement.get("two"));
	}

	@Test
	public void get_xcard_namespace_with_prefix() throws Exception {
		XCardElement xcardElement = build("<v:prop><v:one>1</v:one><v:two>2</v:two></v:prop>", "v");
		assertEquals("2", xcardElement.get("two"));
	}

	@Test
	public void get_empty() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one><two></two></prop>");
		assertEquals("", xcardElement.get("two"));
	}

	@Test
	public void get_none() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one><two>2</two></prop>");
		assertNull(xcardElement.get("three"));
	}

	@Test
	public void get_multiple_names() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one><two>2</two><three>3</three></prop>");
		assertEquals("2", xcardElement.get("two", "three"));
		assertEquals("2", xcardElement.get("three", "two"));
	}

	@Test
	public void get_only_xcard_namespace() throws Exception {
		XCardElement xcardElement = build("<prop><n:four xmlns:n=\"http://example.com\"></n:four></prop>");
		assertNull(xcardElement.get("four"));
	}

	@Test
	public void getAll() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one><two>2</two><two /><three>3</three><two>2-2</two></prop>");
		assertEquals(Arrays.asList("2", "2-2"), xcardElement.getAll("two"));
	}

	@Test
	public void getAll_none() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one><two>2</two></prop>");
		assertTrue(xcardElement.getAll("three").isEmpty());
	}

	@Test
	public void getText() throws Exception {
		XCardElement xcardElement = build("<prop><text>t</text></prop>");
		assertEquals("t", xcardElement.getText());
	}

	@Test
	public void getText_none() throws Exception {
		XCardElement xcardElement = build("<prop><unknown>?</unknown></prop>");
		assertNull(xcardElement.getText());
	}

	@Test
	public void getUri() throws Exception {
		XCardElement xcardElement = build("<prop><uri>u</uri></prop>");
		assertEquals("u", xcardElement.getUri());
	}

	@Test
	public void getUri_none() throws Exception {
		XCardElement xcardElement = build("<prop><unknown>?</unknown></prop>");
		assertNull(xcardElement.getUri());
	}

	@Test
	public void getDateAndOrTime() throws Exception {
		XCardElement xcardElement = build("<prop><date-and-or-time>d</date-and-or-time></prop>");
		assertEquals("d", xcardElement.getDateAndOrTime());
	}

	@Test
	public void getDateAndOrTime_none() throws Exception {
		XCardElement xcardElement = build("<prop><unknown>?</unknown></prop>");
		assertNull(xcardElement.getDateAndOrTime());
	}

	@Test
	public void getTimestamp() throws Exception {
		XCardElement xcardElement = build("<prop><timestamp>t</timestamp></prop>");
		assertEquals("t", xcardElement.getTimestamp());
	}

	@Test
	public void getTimestamp_none() throws Exception {
		XCardElement xcardElement = build("<prop><unknown>?</unknown></prop>");
		assertNull(xcardElement.getTimestamp());
	}

	@Test
	public void append() throws Exception {
		XCardElement xcardElement = build("<prop><one>1</one></prop>");
		Element appendedElement = xcardElement.append("two", "2");
		assertEquals("two", appendedElement.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), appendedElement.getNamespaceURI());
		assertEquals("2", appendedElement.getTextContent());

		Iterator<Element> it = XmlUtils.toElementList(xcardElement.getElement().getChildNodes()).iterator();

		Element element = it.next();
		assertEquals("one", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("1", element.getTextContent());

		element = it.next();
		assertEquals(appendedElement, element);

		assertFalse(it.hasNext());
	}

	@Test
	public void append_multiple() throws Exception {
		XCardElement xcardElement = build("<prop />");
		List<Element> elements = xcardElement.append("number", Arrays.asList("1", "2", "3"));
		Iterator<Element> it = elements.iterator();

		Element element = it.next();
		assertEquals("number", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("1", element.getTextContent());

		element = it.next();
		assertEquals("number", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("2", element.getTextContent());

		element = it.next();
		assertEquals("number", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("3", element.getTextContent());

		assertFalse(it.hasNext());

		assertEquals(XmlUtils.toElementList(xcardElement.getElement().getChildNodes()), elements);
	}

	@Test
	public void appendText() throws Exception {
		XCardElement xcardElement = build("<prop />");
		Element element = xcardElement.appendText("t");
		assertEquals("text", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("t", element.getTextContent());
		assertEquals(XmlUtils.getFirstChildElement(xcardElement.getElement()), element);
	}

	@Test
	public void appendUri() throws Exception {
		XCardElement xcardElement = build("<prop />");
		Element element = xcardElement.appendUri("u");
		assertEquals("uri", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("u", element.getTextContent());
		assertEquals(XmlUtils.getFirstChildElement(xcardElement.getElement()), element);
	}

	@Test
	public void appendDateAndOrTime() throws Exception {
		XCardElement xcardElement = build("<prop />");
		Element element = xcardElement.appendDateAndOrTime("d");
		assertEquals("date-and-or-time", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("d", element.getTextContent());
		assertEquals(XmlUtils.getFirstChildElement(xcardElement.getElement()), element);
	}

	@Test
	public void appendTimestamp() throws Exception {
		XCardElement xcardElement = build("<prop />");
		Element element = xcardElement.appendTimestamp("t");
		assertEquals("timestamp", element.getLocalName());
		assertEquals(VCardVersion.V4_0.getXmlNamespace(), element.getNamespaceURI());
		assertEquals("t", element.getTextContent());
		assertEquals(XmlUtils.getFirstChildElement(xcardElement.getElement()), element);
	}

	private XCardElement build(String innerXml) throws SAXException {
		return build(innerXml, null);
	}

	private XCardElement build(String innerXml, String prefix) throws SAXException {
		//@formatter:off
		String xml =
		"<%sroot xmlns%s=\"" + VCardVersion.V4_0.getXmlNamespace() + "\">" +
			innerXml +
		"</%sroot>";
		//@formatter:on
		if (prefix == null) {
			xml = String.format(xml, "", "", "", "", "");
		} else {
			xml = String.format(xml, prefix + ":", ":" + prefix, prefix + ":");
		}

		Document document = XmlUtils.toDocument(xml);
		Element element = XmlUtils.getFirstChildElement(XmlUtils.getRootElement(document));
		return new XCardElement(element);
	}
}
