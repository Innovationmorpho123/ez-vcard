package ezvcard.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

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
 * Contains helper methods for dealing with hCards.
 * @author Michael Angstadt
 */
public class HCardUtils {
	/**
	 * Gets the hCard value of an element. The value is determined based on the
	 * following:
	 * <ol>
	 * <li>If the element is "&lt;abbr&gt;" and contains a "title" attribute,
	 * then use the value of the "title" attribute.</li>
	 * <li>Else, if the element contains one or more child elements that have a
	 * CSS class of "value", then append together the text contents of these
	 * elements.</li>
	 * <li>Else, use the text content of the element itself.</li>
	 * </ol>
	 * @param element the HTML element
	 * @return the element's hCard value
	 */
	public static String getElementValue(Element element) {
		//value of "title" attribute should be returned if it's a "<abbr>" tag
		//example: <abbr class="latitude" title="48.816667">N 48� 81.6667</abbr>
		if ("abbr".equals(element.tagName())) {
			String title = element.attr("title");
			if (title.length() > 0) {
				return title;
			}
		}

		StringBuilder value = new StringBuilder();
		Elements valueElements = element.getElementsByClass("value");
		if (valueElements.isEmpty()) {
			//get the text content of all child nodes except "type" elements
			for (Node node : element.childNodes()) {
				if (node instanceof Element) {
					Element e = (Element) node;
					if (!e.classNames().contains("type")) {
						value.append(e.text());
					}
				} else if (node instanceof TextNode) {
					TextNode t = (TextNode) node;
					value.append(t.text());
				}
			}
		} else {
			//append together all children whose CSS class is "value"
			for (Element valueElement : valueElements) {
				//ignore "value" elements that are descendents of other "value" elements
				if (isChildOf(valueElement, valueElements)) {
					continue;
				}

				String text = null;
				if ("abbr".equals(valueElement.tagName())) {
					String title = valueElement.attr("title");
					if (title.length() > 0) {
						text = title;
					}
				}
				if (text == null) {
					text = valueElement.text();
				}
				value.append(text);
			}
		}
		return value.toString();
	}

	/**
	 * Determines whether the given element is a child of one of the given
	 * parent elements.
	 * @param child the child HTML element
	 * @param possibleParents the possible parent HTML elements
	 * @return true if it is a child, false if not
	 */
	public static boolean isChildOf(Element child, Elements possibleParents) {
		for (Element p : child.parents()) {
			if (possibleParents.contains(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Searches for all descendant elements that have a given CSS class name,
	 * and gets their hCard values.
	 * @param element the parent HTML element
	 * @param className the CSS class name
	 * @return the hCard values of all elements that have the given class name
	 */
	public static List<String> getElementValuesByCssClass(Element element, String className) {
		Elements elements = element.getElementsByClass(className);
		List<String> values = new ArrayList<String>(elements.size());
		for (Element e : elements) {
			values.add(getElementValue(e));
		}
		return values;
	}

	/**
	 * Gets the "type" parameter values associated with the given element.
	 * @param element the HTML element
	 * @return the type values in lower-case
	 */
	public static List<String> getTypes(Element element) {
		List<String> types = new ArrayList<String>();
		for (Element child : element.children()) {
			Set<String> classes = child.classNames();
			if (classes.contains("type")) {
				types.add(getElementValue(child).toLowerCase());
			}
		}
		return types;
	}

	private HCardUtils() {
		//hide constructor
	}
}
