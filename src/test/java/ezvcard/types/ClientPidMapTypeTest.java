package ezvcard.types;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ezvcard.VCardSubTypes;
import ezvcard.VCardVersion;
import ezvcard.io.CompatibilityMode;
import ezvcard.util.XmlUtils;

/*
 Copyright (c) 2012, Michael Angstadt
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
public class ClientPidMapTypeTest {
	@Test
	public void marshal() throws Exception {
		VCardVersion version = VCardVersion.V4_0;
		List<String> warnings = new ArrayList<String>();
		CompatibilityMode compatibilityMode = CompatibilityMode.RFC;

		ClientPidMapType t = new ClientPidMapType(1, "urn:uuid:1234");

		String actual = t.marshalText(version, warnings, compatibilityMode);
		String expected = "1;urn:uuid:1234";
		assertEquals(expected, actual);
	}

	@Test
	public void marshalXml() throws Exception {
		VCardVersion version = VCardVersion.V4_0;
		List<String> warnings = new ArrayList<String>();
		CompatibilityMode compatibilityMode = CompatibilityMode.RFC;

		ClientPidMapType t = new ClientPidMapType(1, "urn:uuid:1234");

		String expectedXml = "<clientpidmap xmlns=\"" + VCardVersion.V4_0.getXmlNamespace() + "\">";
		expectedXml += "<sourceid>1</sourceid>";
		expectedXml += "<uri>urn:uuid:1234</uri>";
		expectedXml += "</clientpidmap>";
		Document expected = XmlUtils.toDocument(expectedXml);

		Document actual = XmlUtils.toDocument("<clientpidmap xmlns=\"" + VCardVersion.V4_0.getXmlNamespace() + "\" />");
		Element element = XmlUtils.getRootElement(actual);

		t.marshalXml(element, version, warnings, compatibilityMode);

		assertXMLEqual(expected, actual);
	}

	@Test
	public void unmarsal() throws Exception {
		VCardVersion version = VCardVersion.V4_0;
		List<String> warnings = new ArrayList<String>();
		CompatibilityMode compatibilityMode = CompatibilityMode.RFC;
		VCardSubTypes subTypes = new VCardSubTypes();

		String input = "1;urn:uuid:1234";

		ClientPidMapType t = new ClientPidMapType();
		t.unmarshalText(subTypes, input, version, warnings, compatibilityMode);

		assertEquals(1, t.getPid().intValue());
		assertEquals("urn:uuid:1234", t.getUri());
	}

	@Test
	public void unmarsalXml() throws Exception {
		VCardVersion version = VCardVersion.V4_0;
		List<String> warnings = new ArrayList<String>();
		CompatibilityMode compatibilityMode = CompatibilityMode.RFC;
		VCardSubTypes subTypes = new VCardSubTypes();

		String inputXml = "<clientpidmap xmlns=\"" + VCardVersion.V4_0.getXmlNamespace() + "\">";
		inputXml += "<sourceid>1</sourceid>";
		inputXml += "<uri>urn:uuid:1234</uri>";
		inputXml += "</clientpidmap>";
		Element input = XmlUtils.getRootElement(XmlUtils.toDocument(inputXml));

		ClientPidMapType t = new ClientPidMapType();
		t.unmarshalXml(subTypes, input, version, warnings, compatibilityMode);

		assertEquals(1, t.getPid().intValue());
		assertEquals("urn:uuid:1234", t.getUri());
	}

	@Test
	public void random() {
		ClientPidMapType clientpidmap = ClientPidMapType.random(2);
		assertEquals(Integer.valueOf(2), clientpidmap.getPid());
		assertTrue(clientpidmap.getUri().matches("urn:uuid:[-\\da-f]+"));
	}
}
