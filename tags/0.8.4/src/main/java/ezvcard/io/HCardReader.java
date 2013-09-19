package ezvcard.io;

import static ezvcard.util.VCardStringUtils.NEWLINE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.types.AddressType;
import ezvcard.types.CategoriesType;
import ezvcard.types.EmailType;
import ezvcard.types.ImppType;
import ezvcard.types.LabelType;
import ezvcard.types.NicknameType;
import ezvcard.types.RawType;
import ezvcard.types.SourceType;
import ezvcard.types.TelephoneType;
import ezvcard.types.TypeList;
import ezvcard.types.UrlType;
import ezvcard.types.VCardType;
import ezvcard.util.HtmlUtils;

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
 * <p>
 * Parses {@link VCard} objects from an HTML page (hCard format).
 * </p>
 * <p>
 * <b>Example:</b>
 * 
 * <pre class="brush:java">
 * URL url = new URL("http://example.com");
 * HCardReader hcardReader = new HCardReader(url);
 * VCard vcard;
 * while ((vcard = hcardReader.readNext()) != null){
 *   ...
 * }
 * </pre>
 * 
 * </p>
 * @author Michael Angstadt
 * @see <a
 * href="http://microformats.org/wiki/hcard">http://microformats.org/wiki/hcard</a>
 */
public class HCardReader {
	private String pageUrl;
	private final List<String> warnings = new ArrayList<String>();
	private final Map<String, Class<? extends VCardType>> extendedTypeClasses = new HashMap<String, Class<? extends VCardType>>();
	private Elements vcardElements;
	private Iterator<Element> it;
	private final List<LabelType> labels = new ArrayList<LabelType>();
	private final List<String> warningsBuffer = new ArrayList<String>();
	private VCard curVCard;
	private Elements embeddedVCards = new Elements();
	private NicknameType nickname;
	private CategoriesType categories;

	/**
	 * Creates a reader that parses hCards from a URL.
	 * @param url the URL of the webpage
	 * @throws IOException if there's a problem loading the webpage
	 */
	public HCardReader(URL url) throws IOException {
		pageUrl = url.toString();
		Document document = Jsoup.parse(url, 30000);
		init(document, url.getRef());
	}

	/**
	 * Creates a reader that parses hCards from an input stream.
	 * @param in the input stream to the HTML page
	 * @throws IOException if there's a problem reading the HTML page
	 */
	public HCardReader(InputStream in) throws IOException {
		this(in, null);
	}

	/**
	 * Creates a reader that parses hCards from an input stream.
	 * @param in the input stream to the HTML page
	 * @param pageUrl the original URL of the HTML page
	 * @throws IOException if there's a problem reading the HTML page
	 */
	public HCardReader(InputStream in, String pageUrl) throws IOException {
		this.pageUrl = pageUrl;
		Document document = (pageUrl == null) ? Jsoup.parse(in, null, "") : Jsoup.parse(in, null, pageUrl);
		String anchor = getAnchor(pageUrl);
		init(document, anchor);
	}

	/**
	 * Creates a reader that parses hCards from a file.
	 * @param file the HTML file
	 * @throws IOException if there's a problem reading the HTML file
	 */
	public HCardReader(File file) throws IOException {
		this(file, null);
	}

	/**
	 * Creates a reader that parses hCards from a file.
	 * @param file the HTML file
	 * @param pageUrl the original URL of the HTML page
	 * @throws IOException if there's a problem reading the HTML file
	 */
	public HCardReader(File file, String pageUrl) throws IOException {
		this.pageUrl = pageUrl;
		Document document = (pageUrl == null) ? Jsoup.parse(file, null, "") : Jsoup.parse(file, null, pageUrl);
		String anchor = getAnchor(pageUrl);
		init(document, anchor);
	}

	/**
	 * Creates a reader that parses hCards from a reader.
	 * @param reader the input stream to the HTML page
	 * @throws IOException if there's a problem reading the HTML page
	 */
	public HCardReader(Reader reader) throws IOException {
		this(reader, null);
	}

	/**
	 * Creates a reader that parses hCards from a reader.
	 * @param reader the input stream to the HTML page
	 * @param pageUrl the original URL of the HTML page
	 * @throws IOException if there's a problem reading the HTML page
	 */
	public HCardReader(Reader reader, String pageUrl) throws IOException {
		this.pageUrl = pageUrl;

		StringBuilder sb = new StringBuilder();
		char buffer[] = new char[4096];
		int read;
		while ((read = reader.read(buffer)) != -1) {
			sb.append(buffer, 0, read);
		}
		String html = sb.toString();

		Document document = (pageUrl == null) ? Jsoup.parse(html) : Jsoup.parse(html, pageUrl);
		String anchor = getAnchor(pageUrl);
		init(document, anchor);
	}

	/**
	 * Creates a reader that parses hCards from a string.
	 * @param html the HTML page
	 */
	public HCardReader(String html) {
		this(html, null);
	}

	/**
	 * Creates a reader that parses hCards from a string.
	 * @param html the HTML page
	 * @param pageUrl the original URL of the HTML page
	 */
	public HCardReader(String html, String pageUrl) {
		this.pageUrl = pageUrl;

		Document document = (pageUrl == null) ? Jsoup.parse(html) : Jsoup.parse(html, pageUrl);
		String anchor = getAnchor(pageUrl);
		init(document, anchor);
	}

	/**
	 * Constructor for reading embedded vCards.
	 * @param embeddedVCard the HTML element of the embedded vCard
	 * @param pageUrl the original URL of the HTML page
	 */
	private HCardReader(Element embeddedVCard, String pageUrl) {
		this.pageUrl = pageUrl;
		vcardElements = new Elements(embeddedVCard);
		it = vcardElements.iterator();
	}

	private void init(Document document, String anchor) {
		Element searchIn = null;
		if (anchor != null) {
			searchIn = document.getElementById(anchor);
		}
		if (searchIn == null) {
			searchIn = document;
		}

		vcardElements = searchIn.getElementsByClass("vcard");
		it = vcardElements.iterator();
	}

	/**
	 * Gets the anchor part of a URL.
	 * @param urlStr the URL
	 * @return the anchor (e.g. "foo" from the URL
	 * "http://example.com/index.php#foo")
	 */
	private String getAnchor(String urlStr) {
		if (urlStr == null) {
			return null;
		}

		try {
			URL url = new URL(urlStr);
			return url.getRef();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Registers an extended type class.
	 * @param clazz the extended type class to register (MUST have a public,
	 * no-arg constructor)
	 * @throws RuntimeException if the class doesn't have a public, no-arg
	 * constructor
	 */
	public void registerExtendedType(Class<? extends VCardType> clazz) {
		extendedTypeClasses.put(getTypeNameFromTypeClass(clazz), clazz);
	}

	/**
	 * Removes an extended type class that was previously registered.
	 * @param clazz the extended type class to remove
	 */
	public void unregisterExtendedType(Class<? extends VCardType> clazz) {
		extendedTypeClasses.remove(getTypeNameFromTypeClass(clazz));
	}

	/**
	 * Gets the warnings from the last vCard that was unmarshalled. This list is
	 * reset every time a new vCard is read.
	 * @return the warnings or empty list if there were no warnings
	 */
	public List<String> getWarnings() {
		return new ArrayList<String>(warnings);
	}

	/**
	 * Reads the next vCard from the data stream.
	 * @return the next vCard or null if there are no more
	 */
	public VCard readNext() {
		Element vcardElement = null;
		while (it.hasNext() && vcardElement == null) {
			vcardElement = it.next();

			//if this element is a child of another "vcard" element, then ignore it because it's an embedded vcard
			if (HtmlUtils.isChildOf(vcardElement, vcardElements)) {
				vcardElement = null;
			}
		}
		if (vcardElement == null) {
			return null;
		}

		warnings.clear();
		warningsBuffer.clear();
		labels.clear();
		nickname = null;
		categories = null;

		curVCard = new VCard();
		curVCard.setVersion(VCardVersion.V3_0);
		if (pageUrl != null) {
			curVCard.addSource(new SourceType(pageUrl));
		}

		//visit all descendant nodes, depth-first
		for (Element child : vcardElement.children()) {
			visit(child);
		}

		//assign labels to their addresses
		for (LabelType label : labels) {
			boolean orphaned = true;
			for (AddressType adr : curVCard.getAddresses()) {
				if (adr.getLabel() == null && adr.getTypes().equals(label.getTypes())) {
					adr.setLabel(label.getValue());
					orphaned = false;
					break;
				}
			}
			if (orphaned) {
				curVCard.addOrphanedLabel(label);
			}
		}

		return curVCard;
	}

	private void visit(Element element) {
		Set<String> classNames = element.classNames();
		for (String className : classNames) {
			if (UrlType.NAME.equalsIgnoreCase(className)) {
				String href = element.attr("href");
				if (href.length() > 0) {
					if (!classNames.contains(EmailType.NAME.toLowerCase()) && href.matches("(?i)mailto:.*")) {
						className = EmailType.NAME;
					} else if (!classNames.contains(TelephoneType.NAME.toLowerCase()) && href.matches("(?i)tel:.*")) {
						className = TelephoneType.NAME;
					} else {
						//try parsing as IMPP
						warningsBuffer.clear();
						ImppType impp = new ImppType();
						try {
							impp.unmarshalHtml(element, warningsBuffer);
							curVCard.addType(impp);
							for (String warning : warningsBuffer) {
								addWarning(warning, impp.getTypeName());
							}
							continue;
						} catch (SkipMeException e) {
							//URL is not an instant messenger URL
						} catch (CannotParseException e) {
							//URL is not an instant messenger URL
						}
					}
				}
			}

			VCardType type = createTypeObject(className);
			if (type == null) {
				//if no type class is found, then it must be an arbitrary CSS class that has nothing to do with vCard
				continue;
			}

			warningsBuffer.clear();
			try {
				type.unmarshalHtml(element, warningsBuffer);

				//LABELs must be treated specially so they can be matched up with their ADRs
				if (type instanceof LabelType) {
					labels.add((LabelType) type);
					continue;
				}

				//add all NICKNAMEs to the same type object
				if (type instanceof NicknameType) {
					NicknameType nn = (NicknameType) type;
					if (nickname == null) {
						nickname = nn;
						curVCard.addType(nickname);
					} else {
						nickname.getValues().addAll(nn.getValues());
					}
					continue;
				}

				//add all CATEGORIES to the same type object
				if (type instanceof CategoriesType) {
					CategoriesType c = (CategoriesType) type;
					if (categories == null) {
						categories = c;
						curVCard.addType(categories);
					} else {
						categories.getValues().addAll(c.getValues());
					}
					continue;
				}
			} catch (SkipMeException e) {
				warningsBuffer.add("Property has requested that it be skipped: " + e.getMessage());
				continue;
			} catch (CannotParseException e) {
				String html = element.outerHtml();
				warningsBuffer.add("Property value could not be parsed.  Property will be saved as an extended type instead." + NEWLINE + "  HTML: " + html + NEWLINE + "  Reason: " + e.getMessage());
				type = new RawType(className, html);
			} catch (EmbeddedVCardException e) {
				if (HtmlUtils.isChildOf(element, embeddedVCards)) {
					//prevents multiple-nested embedded elements from overwriting each other
					continue;
				}

				embeddedVCards.add(element);
				HCardReader embeddedReader = new HCardReader(element, pageUrl);
				try {
					VCard embeddedVCard = embeddedReader.readNext();
					e.injectVCard(embeddedVCard);
				} finally {
					for (String w : embeddedReader.getWarnings()) {
						warningsBuffer.add("Problem unmarshalling nested vCard value: " + w);
					}
				}
			} catch (UnsupportedOperationException e) {
				//type class does not support hCard
				warningsBuffer.add("Property class \"" + type.getClass().getName() + "\" does not support hCard unmarshalling.");
				continue;
			} finally {
				for (String warning : warningsBuffer) {
					addWarning(warning, type.getTypeName());
				}
			}

			curVCard.addType(type);
		}

		for (Element child : element.children()) {
			visit(child);
		}
	}

	/**
	 * Creates the appropriate {@link VCardType} instance, given the type name.
	 * This method does not unmarshal the type, it just creates the type object.
	 * @param typeName the type name (e.g. "fn")
	 * @return the type object or null if the type name was not recognized
	 */
	private VCardType createTypeObject(String typeName) {
		//parse as a registered extended type class (extended type classes should override standard ones)
		Class<? extends VCardType> extendedTypeClass = extendedTypeClasses.get(typeName.toLowerCase());
		if (extendedTypeClass != null) {
			try {
				return extendedTypeClass.newInstance();
			} catch (Exception e) {
				//should never be thrown
				//the type class is checked to see if it has a public, no-arg constructor in the "registerExtendedType" method
				throw new RuntimeException("Extended type class \"" + extendedTypeClass.getName() + "\" must have a public, no-arg constructor.");
			}
		}

		//parse as a standard property
		Class<? extends VCardType> clazz = TypeList.getTypeClassByHCardTypeName(typeName);
		if (clazz != null) {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				//should never be thrown
				//all type classes must have public, no-arg constructors
				throw new RuntimeException(e);
			}
		}

		//parse as a RawType
		if (typeName.toUpperCase().startsWith("X-")) {
			return new RawType(typeName); //use RawType instead of TextType because we don't want to unescape any characters that might be meaningful to this type
		}

		return null;
	}

	/**
	 * Gets the type name from a type class.
	 * @param clazz the type class
	 * @return the type name
	 */
	private String getTypeNameFromTypeClass(Class<? extends VCardType> clazz) {
		try {
			VCardType t = clazz.newInstance();
			return t.getTypeName().toLowerCase();
		} catch (Exception e) {
			//there is no public, no-arg constructor
			throw new RuntimeException(e);
		}
	}

	private void addWarning(String message, String propertyName) {
		warnings.add(propertyName + " property: " + message);
	}
}
