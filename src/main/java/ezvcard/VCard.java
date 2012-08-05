package ezvcard;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import ezvcard.io.VCardReader;
import ezvcard.io.VCardWriter;
import ezvcard.types.AddressType;
import ezvcard.types.AgentType;
import ezvcard.types.AnniversaryType;
import ezvcard.types.BirthdayType;
import ezvcard.types.CalendarRequestUriType;
import ezvcard.types.CalendarUriType;
import ezvcard.types.CategoriesType;
import ezvcard.types.ClassificationType;
import ezvcard.types.ClientPidMapType;
import ezvcard.types.EmailType;
import ezvcard.types.FbUrlType;
import ezvcard.types.FormattedNameType;
import ezvcard.types.GeoType;
import ezvcard.types.ImppType;
import ezvcard.types.KeyType;
import ezvcard.types.KindType;
import ezvcard.types.LabelType;
import ezvcard.types.LanguageType;
import ezvcard.types.LogoType;
import ezvcard.types.MailerType;
import ezvcard.types.MemberType;
import ezvcard.types.NicknameType;
import ezvcard.types.NoteType;
import ezvcard.types.OrganizationType;
import ezvcard.types.PhotoType;
import ezvcard.types.ProdIdType;
import ezvcard.types.ProfileType;
import ezvcard.types.RawType;
import ezvcard.types.RelatedType;
import ezvcard.types.RevisionType;
import ezvcard.types.RoleType;
import ezvcard.types.SortStringType;
import ezvcard.types.SoundType;
import ezvcard.types.SourceDisplayTextType;
import ezvcard.types.SourceType;
import ezvcard.types.StructuredNameType;
import ezvcard.types.TelephoneType;
import ezvcard.types.TimezoneType;
import ezvcard.types.TitleType;
import ezvcard.types.UidType;
import ezvcard.types.UrlType;
import ezvcard.types.VCardType;
import ezvcard.types.XmlType;

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
 * Represents the data in a vCard.
 * @author Michael Angstadt
 */
public class VCard {
	private VCardVersion version = VCardVersion.V3_0;
	private KindType kind;
	private List<MemberType> members = new ArrayList<MemberType>();
	private ProfileType profile;
	private ClassificationType classification;
	private List<SourceType> sources = new ArrayList<SourceType>();
	private SourceDisplayTextType sourceDisplayText;
	private List<FormattedNameType> formattedNames = new ArrayList<FormattedNameType>();
	private List<StructuredNameType> structuredNames = new ArrayList<StructuredNameType>();
	private List<NicknameType> nicknames = new ArrayList<NicknameType>();
	private SortStringType sortString;
	private List<TitleType> titles = new ArrayList<TitleType>();
	private List<RoleType> roles = new ArrayList<RoleType>();
	private List<PhotoType> photos = new ArrayList<PhotoType>();
	private List<LogoType> logos = new ArrayList<LogoType>();
	private List<SoundType> sounds = new ArrayList<SoundType>();
	private BirthdayType birthday; //TODO can be multiple
	private AnniversaryType anniversary; //TODO can be multiple
	private RevisionType rev;
	private ProdIdType prodId;
	private List<AddressType> addresses = new ArrayList<AddressType>();
	private List<LabelType> labels = new ArrayList<LabelType>();
	private List<EmailType> emails = new ArrayList<EmailType>();
	private List<TelephoneType> telephoneNumbers = new ArrayList<TelephoneType>();
	private MailerType mailer;
	private List<UrlType> urls = new ArrayList<UrlType>();
	private List<TimezoneType> timezones = new ArrayList<TimezoneType>();
	private List<GeoType> geos = new ArrayList<GeoType>();
	private List<OrganizationType> organizations = new ArrayList<OrganizationType>();
	private List<CategoriesType> categories = new ArrayList<CategoriesType>();
	private AgentType agent;
	private List<NoteType> notes = new ArrayList<NoteType>();
	private UidType uid;
	private List<KeyType> keys = new ArrayList<KeyType>();
	private List<ImppType> impps = new ArrayList<ImppType>();
	private List<RelatedType> relations = new ArrayList<RelatedType>();
	private List<LanguageType> languages = new ArrayList<LanguageType>();
	private List<CalendarRequestUriType> calendarRequestUris = new ArrayList<CalendarRequestUriType>();
	private List<CalendarUriType> calendarUris = new ArrayList<CalendarUriType>();
	private List<FbUrlType> fbUrls = new ArrayList<FbUrlType>();
	private List<ClientPidMapType> clientPidMaps = new ArrayList<ClientPidMapType>();
	private List<XmlType> xmls = new ArrayList<XmlType>();
	private ListMultimap<String, VCardType> extendedTypes = ArrayListMultimap.create();

	/**
	 * Parses a vCard string. Use the {@link VCardReader} class for more control
	 * over how the vCard is parsed.
	 * @param str the vCard
	 * @return the parsed vCard
	 * @throws VCardException if there's a problem parsing the vCard
	 */
	public static VCard parse(String str) throws VCardException {
		try {
			VCardReader vcr = new VCardReader(new StringReader(str));
			return vcr.readNext();
		} catch (IOException e) {
			//never thrown because we're reading from string
			return null;
		}
	}

	/**
	 * Parses a vCard file. Use the {@link VCardReader} class for more control
	 * over how the vCard is parsed.
	 * @param file the vCard
	 * @return the parsed vCard
	 * @throws VCardException if there's a problem parsing the vCard
	 * @throws IOException if there's a problem reading the file
	 */
	public static VCard parse(File file) throws VCardException, IOException {
		VCardReader vcr = null;
		try {
			vcr = new VCardReader(new FileReader(file));
			return vcr.readNext();
		} finally {
			IOUtils.closeQuietly(vcr);
		}
	}

	/**
	 * Writes this vCard to a string. Use the {@link VCardWriter} class for more
	 * control over how the vCard is written.
	 * @throws VCardException if there's a problem writing the vCard
	 */
	public String write() throws VCardException {
		StringWriter sw = new StringWriter();
		try {
			VCardWriter writer = new VCardWriter(sw, (version == null) ? VCardVersion.V3_0 : version);
			writer.write(this);
		} catch (IOException e) {
			//never thrown because we're writing to string
		}
		return sw.toString();
	}

	/**
	 * Writes this vCard to a file. Use the {@link VCardWriter} class for more
	 * control over how the vCard is written.
	 * @param file the file to write to
	 * @throws VCardException if there's a problem writing the vCard
	 * @throws IOException if there's a problem writing to the file
	 */
	public void write(File file) throws VCardException, IOException {
		VCardWriter vcw = null;
		try {
			vcw = new VCardWriter(new FileWriter(file), (version == null) ? VCardVersion.V3_0 : version);
			vcw.write(this);
		} finally {
			IOUtils.closeQuietly(vcw);
		}
	}

	/**
	 * Gets the version attached to this vCard.
	 * @return the vCard version
	 */
	public VCardVersion getVersion() {
		return version;
	}

	/**
	 * Sets the version of this vCard. When marshalling a vCard with the
	 * {@link VCardWriter} class, please use the
	 * {@link VCardWriter#setTargetVersion setTargetVersion} method to define
	 * what version the vCard should be marshalled as. {@link VCardWriter}
	 * <b>does not</b> look at the version that is set on the VCard object.
	 * @param version the vCard version
	 */
	public void setVersion(VCardVersion version) {
		this.version = version;
	}

	/**
	 * Gets the type of entity this vCard represents.
	 * <p>
	 * vCard property name: KIND
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the kind
	 */
	public KindType getKind() {
		return kind;
	}

	/**
	 * Sets the type of entity this vCard represents.
	 * <p>
	 * vCard property name: KIND
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param kind the kind
	 */
	public void setKind(KindType kind) {
		this.kind = kind;
	}

	/**
	 * Gets the members of the group. Only valid if the KIND property is set to
	 * "group".
	 * 
	 * <p>
	 * 
	 * <pre>
	 * VCard vcard = new VCard();
	 * vcard.setKind(KindType.group());
	 * vcard.addMember(...);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * <p>
	 * vCard property name: MEMBER
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the members
	 */
	public List<MemberType> getMembers() {
		return members;
	}

	/**
	 * Adds a member to the group. Only valid if the KIND property is set to
	 * "group".
	 * 
	 * <p>
	 * 
	 * <pre>
	 * VCard vcard = new VCard();
	 * vcard.setKind(KindType.group());
	 * vcard.addMember(...);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * <p>
	 * vCard property name: MEMBER
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param member the member to add
	 */
	public void addMember(MemberType member) {
		this.members.add(member);
	}

	/**
	 * Gets the PROFILE property.
	 * <p>
	 * vCard property name: PROFILE
	 * </p>
	 * <p>
	 * vCard versions: 3.0
	 * </p>
	 * @return the property
	 */
	public ProfileType getProfile() {
		return profile;
	}

	/**
	 * Sets the PROFILE property.
	 * <p>
	 * vCard property name: PROFILE
	 * </p>
	 * <p>
	 * vCard versions: 3.0
	 * </p>
	 * @param profile the property
	 */
	public void setProfile(ProfileType profile) {
		this.profile = profile;
	}

	/**
	 * Gets the classification of the vCard. It describes the sensitivity of the
	 * information in the vCard.
	 * <p>
	 * vCard property name: CLASS
	 * </p>
	 * <p>
	 * vCard versions: 3.0
	 * </p>
	 * @return the classification
	 */
	public ClassificationType getClassification() {
		return classification;
	}

	/**
	 * Sets the classification of the vCard. It describes the sensitivity of the
	 * information in the vCard.
	 * <p>
	 * vCard property name: CLASS
	 * </p>
	 * <p>
	 * vCard versions: 3.0
	 * </p>
	 * @param classification the classification
	 */
	public void setClassification(ClassificationType classification) {
		this.classification = classification;
	}

	/**
	 * Gets the URIs that can be used to retrieve the most up-to-date version of
	 * the person's vCard.
	 * <p>
	 * vCard property name: SOURCE
	 * </p>
	 * <p>
	 * vCard versions: 3.0, 4.0
	 * </p>
	 * @return the sources
	 */
	public List<SourceType> getSources() {
		return sources;
	}

	/**
	 * Adds a URI that can be used to retrieve the most up-to-date version of
	 * the person's vCard.
	 * <p>
	 * vCard property name: SOURCE
	 * </p>
	 * <p>
	 * vCard versions: 3.0, 4.0
	 * </p>
	 * @param the source
	 */
	public void addSource(SourceType source) {
		this.sources.add(source);
	}

	/**
	 * Gets a textual representation of the vCard source.
	 * <p>
	 * vCard property name: NAME
	 * </p>
	 * <p>
	 * vCard versions: 3.0
	 * </p>
	 * @return a textual representation of the vCard source
	 */
	public SourceDisplayTextType getSourceDisplayText() {
		return sourceDisplayText;
	}

	/**
	 * Sets a textual representation of the vCard source.
	 * <p>
	 * vCard property name: NAME
	 * </p>
	 * <p>
	 * vCard versions: 3.0
	 * </p>
	 * @param sourceDisplayText a textual representation of the vCard source
	 */
	public void setSourceDisplayText(SourceDisplayTextType sourceDisplayText) {
		this.sourceDisplayText = sourceDisplayText;
	}

	/**
	 * Gets the text values that can be used to display the person's name. This
	 * method should only be used if there is expected to be multiple instances
	 * of the property. Otherwise, use the {@link #getFormattedName} method.
	 * <p>
	 * vCard property name: FN
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the display texts of the person's name
	 */
	public List<FormattedNameType> getFormattedNames() {
		return formattedNames;
	}

	/**
	 * Gets the text value used to display the person's name.
	 * <p>
	 * vCard property name: FN
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the display text of the person's name
	 */
	public FormattedNameType getFormattedName() {
		return formattedNames.isEmpty() ? null : formattedNames.get(0);
	}

	/**
	 * Adds a text value used to display the person's name. This method should
	 * only be used for adding multiple property instances to the vCard.
	 * Otherwise, use the {@link #setFormattedName} method.
	 * <p>
	 * vCard property name: FN
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param formattedName the display text for the person's name
	 */
	public void addFormattedName(FormattedNameType formattedName) {
		this.formattedNames.add(formattedName);
	}

	/**
	 * Sets the text value used to display the person's name.
	 * <p>
	 * vCard property name: FN
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the display text for the person's name
	 */
	public void setFormattedName(FormattedNameType formattedName) {
		this.formattedNames.clear();
		addFormattedName(formattedName);
	}

	/**
	 * Gets all N properties. This method should only be used if there is
	 * expected to be multiple instances of the property. Otherwise, use the
	 * {@link #getStructuredName} method.
	 * <p>
	 * vCard property name: N
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the N properties
	 */
	public List<StructuredNameType> getStructuredNames() {
		return structuredNames;
	}

	/**
	 * Gets the individual components of the person's name.
	 * <p>
	 * vCard property name: N
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the components of the person's name
	 */
	public StructuredNameType getStructuredName() {
		return structuredNames.isEmpty() ? null : structuredNames.get(0);
	}

	/**
	 * Adds a property that contains the individual components of the person's
	 * name. This method should ONLY be used if all {@link StructuredNameType}
	 * instances have the same ALTID value. Otherwise, use
	 * {@link #setStructuredName}.
	 * <p>
	 * vCard property name: N
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param structuredName the components of the person's name
	 */
	public void addStructuredName(StructuredNameType structuredName) {
		this.structuredNames.add(structuredName);
	}

	/**
	 * Sets the individual components of the person's name.
	 * <p>
	 * vCard property name: N
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param structuredName the components of the person's name
	 */
	public void setStructuredName(StructuredNameType structuredName) {
		this.structuredNames.clear();
		addStructuredName(structuredName);
	}

	/**
	 * Gets all instances of the NICKNAME property. This method should only be
	 * used if there is expected to be multiple instances of the property.
	 * Otherwise, use the {@link #getNickname} method.
	 * <p>
	 * vCard property name: NICKNAME
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the person's nicknames
	 */
	public List<NicknameType> getNicknames() {
		return nicknames;
	}

	/**
	 * Gets the person's nicknames.
	 * <p>
	 * vCard property name: NICKNAME
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the person's nicknames
	 */
	public NicknameType getNickname() {
		return nicknames.isEmpty() ? null : nicknames.get(0);
	}

	/**
	 * Adds an instance of the NICKNAME property to the vCard. This method
	 * should only be used for adding multiple property instances to the vCard.
	 * Otherwise, use the {@link #setNickname} method.
	 * <p>
	 * vCard property name: NICKNAME
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param nickname the NICKNAME property to add
	 */
	public void addNickname(NicknameType nickname) {
		this.nicknames.add(nickname);
	}

	/**
	 * Sets the person's nicknames.
	 * <p>
	 * vCard property name: NICKNAME
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param nickname the person's nicknames
	 */
	public void setNickname(NicknameType nickname) {
		this.nicknames.clear();
		addNickname(nickname);
	}

	/**
	 * Gets the string that should be used to sort the vCard. For 4.0 vCards,
	 * use the {@link StructuredNameType#getSortAs} and/or
	 * {@link OrganizationType#getSortAs} methods.
	 * <p>
	 * vCard property name: SORT-STRING
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @return the string that's used to sort the vCard
	 */
	public SortStringType getSortString() {
		return sortString;
	}

	/**
	 * Sets the string that should be used to sort the vCard. For 4.0 vCards,
	 * use the {@link StructuredNameType#setSortAs} and/or
	 * {@link OrganizationType#setSortAs} methods.
	 * <p>
	 * vCard property name: SORT-STRING
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @param sortString the string that's used to sort the vCard
	 */
	public void setSortString(SortStringType sortString) {
		this.sortString = sortString;
	}

	/**
	 * Gets the titles associated with the person.
	 * <p>
	 * vCard property name: TITLE
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the titles
	 */
	public List<TitleType> getTitles() {
		return titles;
	}

	/**
	 * Adds a title associated with the person.
	 * <p>
	 * vCard property name: TITLE
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param title the title
	 */
	public void addTitle(TitleType title) {
		this.titles.add(title);
	}

	/**
	 * Gets the roles associated with the person.
	 * <p>
	 * vCard property name: ROLE
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the roles
	 */
	public List<RoleType> getRoles() {
		return roles;
	}

	/**
	 * Adds a role associated with the person.
	 * <p>
	 * vCard property name: ROLE
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param role the role
	 */
	public void addRole(RoleType role) {
		this.roles.add(role);
	}

	/**
	 * Gets the photos attached to the vCard, such as a picture of the person's
	 * face.
	 * <p>
	 * vCard property name: PHOTO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the photos
	 */
	public List<PhotoType> getPhotos() {
		return photos;
	}

	/**
	 * Adds a photo to the vCard, such as a picture of the person's face.
	 * <p>
	 * vCard property name: PHOTO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param photo the photo to add
	 */
	public void addPhoto(PhotoType photo) {
		this.photos.add(photo);
	}

	/**
	 * Gets the logos attached to the vCard, such as the person's company logo.
	 * <p>
	 * vCard property name: LOGO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the logos
	 */
	public List<LogoType> getLogos() {
		return logos;
	}

	/**
	 * Adds a logo to the vCard, such as the person's company logo.
	 * <p>
	 * vCard property name: LOGO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param logo the logo to add
	 */
	public void addLogo(LogoType logo) {
		this.logos.add(logo);
	}

	/**
	 * Gets the sounds attached to the vCard, such as a pronunciation of the
	 * person's name.
	 * <p>
	 * vCard property name: SOUND
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the sounds
	 */
	public List<SoundType> getSounds() {
		return sounds;
	}

	/**
	 * Adds a sounds to the vCard, such as a pronunciation of the person's name.
	 * <p>
	 * vCard property name: SOUND
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param sound the sound to add
	 */
	public void addSound(SoundType sound) {
		this.sounds.add(sound);
	}

	/**
	 * Gets the person's birthday.
	 * <p>
	 * vCard property name: BDAY
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the birthday
	 */
	public BirthdayType getBirthday() {
		return birthday;
	}

	/**
	 * Sets the person's birthday.
	 * <p>
	 * vCard property name: BDAY
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param birthday the birthday
	 */
	public void setBirthday(BirthdayType birthday) {
		this.birthday = birthday;
	}

	/**
	 * Gets the person's anniversary.
	 * <p>
	 * vCard property name: ANNIVERSARY
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the anniversary
	 */
	public AnniversaryType getAnniversary() {
		return anniversary;
	}

	/**
	 * Sets the person's anniversary.
	 * <p>
	 * vCard property name: ANNIVERSARY
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param anniversary the anniversary
	 */
	public void setAnniversary(AnniversaryType anniversary) {
		this.anniversary = anniversary;
	}

	/**
	 * Gets the time that the vCard was last modified.
	 * <p>
	 * vCard property name: REV
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the last modified time
	 */
	public RevisionType getRevision() {
		return rev;
	}

	/**
	 * Sets the time that the vCard was last modified.
	 * <p>
	 * vCard property name: REV
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param rev the last modified time
	 */
	public void setRevision(RevisionType rev) {
		this.rev = rev;
	}

	/**
	 * Gets the product ID.
	 * <p>
	 * vCard property name: PRODID
	 * </p>
	 * <p>
	 * vCard versions: 3.0, 4.0
	 * </p>
	 * @return the product ID
	 */
	public ProdIdType getProdId() {
		return prodId;
	}

	/**
	 * Sets the product ID.
	 * <p>
	 * vCard property name: PRODID
	 * </p>
	 * <p>
	 * vCard versions: 3.0, 4.0
	 * </p>
	 * @param prodId the product ID
	 */
	public void setProdId(ProdIdType prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets the mailing addresses.
	 * <p>
	 * vCard property name: ADR
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the mailing addresses
	 */
	public List<AddressType> getAddresses() {
		return addresses;
	}

	/**
	 * Adds a mailing address.
	 * <p>
	 * vCard property name: ADR
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param address the mailing address to add
	 */
	public void addAddress(AddressType address) {
		this.addresses.add(address);
	}

	/**
	 * Gets all mailing labels that could not be assigned to an address. Use
	 * {@link AddressType#getLabel} to get a label that has been assigned to an
	 * address.
	 * <p>
	 * vCard property name: LABEL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @return the orphaned labels
	 */
	public List<LabelType> getOrphanedLabels() {
		return labels;
	}

	/**
	 * Adds a mailing label which is not associated with any address. Use of
	 * this method is discouraged. To add a mailing label to an address, use the
	 * {@link AddressType#setLabel()} method.
	 * <p>
	 * vCard property name: LABEL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @param label the orphaned label to add
	 */
	public void addOrphanedLabel(LabelType label) {
		this.labels.add(label);
	}

	/**
	 * Gets the email addresses.
	 * <p>
	 * vCard property name: EMAIL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the email addresses
	 */
	public List<EmailType> getEmails() {
		return emails;
	}

	/**
	 * Adds an email address.
	 * <p>
	 * vCard property name: EMAIL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param email the email address to add
	 */
	public void addEmail(EmailType email) {
		this.emails.add(email);
	}

	/**
	 * Gets the telephone numbers.
	 * <p>
	 * vCard property name: TEL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the telephone numbers
	 */
	public List<TelephoneType> getTelephoneNumbers() {
		return telephoneNumbers;
	}

	/**
	 * Adds a telephone number.
	 * <p>
	 * vCard property name: TEL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param telephoneNumber the telephone number to add
	 */
	public void addTelephoneNumber(TelephoneType telephoneNumber) {
		this.telephoneNumbers.add(telephoneNumber);
	}

	/**
	 * Gets the email client that the person uses.
	 * <p>
	 * vCard property name: MAILER
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @return the email client
	 */
	public MailerType getMailer() {
		return mailer;
	}

	/**
	 * Sets the email client that the person uses.
	 * <p>
	 * vCard property name: MAILER
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @param mailer the email client
	 */
	public void setMailer(MailerType mailer) {
		this.mailer = mailer;
	}

	/**
	 * Gets the URLs. URLs can point to websites such as the person's personal
	 * homepage or business website.
	 * <p>
	 * vCard property name: URL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the URLs
	 */
	public List<UrlType> getUrls() {
		return urls;
	}

	/**
	 * Adds a URL. URLs can point to websites such as the person's personal
	 * homepage or business website.
	 * <p>
	 * vCard property name: URL
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param url the URL to add
	 */
	public void addUrl(UrlType url) {
		this.urls.add(url);
	}

	/**
	 * Gets all instances of the timezone property. This method should only be
	 * used if there are multiple instances of the property. Otherwise, use the
	 * {@link #getTimezone} method.
	 * <p>
	 * vCard property name: TZ
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the timezones
	 */
	public List<TimezoneType> getTimezones() {
		return timezones;
	}

	/**
	 * Gets the timezone the person lives/works in.
	 * <p>
	 * vCard property name: TZ
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the timezone
	 */
	public TimezoneType getTimezone() {
		return timezones.isEmpty() ? null : timezones.get(0);
	}

	/**
	 * Sets the timezone the person lives/works in.
	 * <p>
	 * vCard property name: TZ
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return timezone the timezone
	 */
	public void setTimezone(TimezoneType timezone) {
		this.timezones.clear();
		addTimezone(timezone);
	}

	/**
	 * Adds a timezone to the vCard. This method should only be used for adding
	 * multiple property instances to the vCard. Otherwise, use the
	 * {@link #setTimezone} method.
	 * <p>
	 * vCard property name: TZ
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param timezone the timezone to add
	 */
	public void addTimezone(TimezoneType timezone) {
		this.timezones.add(timezone);
	}

	/**
	 * Gets all geographical position properties. This method should only be
	 * used if there are multiple instances of the property. Otherwise, use the
	 * {@link #getGeo} method.
	 * <p>
	 * vCard property name: GEO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the geographical positions
	 */
	public List<GeoType> getGeos() {
		return geos;
	}

	/**
	 * Gets the geographical position of where the person lives/works.
	 * <p>
	 * vCard property name: GEO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the geographical position
	 */
	public GeoType getGeo() {
		return geos.isEmpty() ? null : geos.get(0);
	}

	/**
	 * Sets the geographical position of where the person lives/works.
	 * <p>
	 * vCard property name: GEO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param geo the geographical position
	 */
	public void setGeo(GeoType geo) {
		geos.clear();
		addGeo(geo);
	}

	/**
	 * Adds a geographical position to the vCard. This method should only be
	 * used for adding multiple property instances to the vCard. Otherwise, use
	 * the {@link #setGeo} method.
	 * <p>
	 * vCard property name: GEO
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param geo the geographical position to add
	 */
	public void addGeo(GeoType geo) {
		this.geos.add(geo);
	}

	/**
	 * Gets the hierarchy of organizations the person belongs to.
	 * <p>
	 * vCard property name: ORG
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the organizations
	 */
	public OrganizationType getOrganization() {
		return organizations.isEmpty() ? null : organizations.get(0);
	}

	/**
	 * Gets all organization properties. This method should only be used if
	 * there are multiple instances of the property. Otherwise, use the
	 * {@link #getOrganization} method.
	 * <p>
	 * vCard property name: ORG
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the organization properties
	 */
	public List<OrganizationType> getOrganizations() {
		return organizations;
	}

	/**
	 * Adds an organization property to the vCard. This method should only be
	 * used for adding multiple property instances to the vCard. Otherwise, use
	 * the {@link #setOrganization} method.
	 * <p>
	 * vCard property name: ORG
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param organization the organization property to add
	 */
	public void addOrganization(OrganizationType organization) {
		this.organizations.add(organization);
	}

	/**
	 * Sets the hierarchy of organizations the person belongs to.
	 * <p>
	 * vCard property name: ORG
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param organization the organizations
	 */
	public void setOrganization(OrganizationType organization) {
		this.organizations.clear();
		addOrganization(organization);
	}

	/**
	 * Gets all category properties. This method should only be used if there
	 * are multiple instances of the property. Otherwise, use the
	 * {@link #getCategories} method.
	 * <p>
	 * vCard property name: CATEGORIES
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the category properties
	 */
	public List<CategoriesType> getCategoriesList() {
		return categories;
	}

	/**
	 * Gets the list of keywords (aka "tags") that can be used to describe the
	 * person.
	 * <p>
	 * vCard property name: CATEGORIES
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the categories
	 */
	public CategoriesType getCategories() {
		return categories.isEmpty() ? null : categories.get(0);
	}

	/**
	 * Adds a category property to the vCard. This method should only be used
	 * for adding multiple property instances to the vCard. Otherwise, use the
	 * {@link #setCategories} method.
	 * <p>
	 * vCard property name: CATEGORIES
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param categories the category property to add
	 */
	public void addCategories(CategoriesType categories) {
		this.categories.add(categories);
	}

	/**
	 * Sets the list of keywords (aka "tags") that can be used to describe the
	 * person.
	 * <p>
	 * vCard property name: CATEGORIES
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param categories the categories
	 */
	public void setCategories(CategoriesType categories) {
		this.categories.clear();
		addCategories(categories);
	}

	/**
	 * Gets the information on the person's representative.
	 * <p>
	 * vCard property name: AGENT
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @return the agent information
	 */
	public AgentType getAgent() {
		return agent;
	}

	/**
	 * Sets the information on the person's representative.
	 * <p>
	 * vCard property name: AGENT
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0
	 * </p>
	 * @param agent the agent information
	 */
	public void setAgent(AgentType agent) {
		this.agent = agent;
	}

	/**
	 * Gets the notes. A note contains free-form, miscellaneous text.
	 * <p>
	 * vCard property name: NOTE
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the notes
	 */
	public List<NoteType> getNotes() {
		return notes;
	}

	/**
	 * Adds a note. A note contains free-form, miscellaneous text.
	 * <p>
	 * vCard property name: NOTE
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param note the note to add
	 */
	public void addNote(NoteType note) {
		this.notes.add(note);
	}

	/**
	 * Gets the unique identifier of the vCard.
	 * <p>
	 * vCard property name: UID
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the unique identifier
	 */
	public UidType getUid() {
		return uid;
	}

	/**
	 * Sets the unique identifier of the vCard.
	 * <p>
	 * vCard property name: UID
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param uid the unique identifier
	 */
	public void setUid(UidType uid) {
		this.uid = uid;
	}

	/**
	 * Gets the public encryption keys.
	 * <p>
	 * vCard property name: KEY
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @return the keys
	 */
	public List<KeyType> getKeys() {
		return keys;
	}

	/**
	 * Adds a public encryption key.
	 * <p>
	 * vCard property name: KEY
	 * </p>
	 * <p>
	 * vCard versions: 2.1, 3.0, 4.0
	 * </p>
	 * @param key the key to add
	 */
	public void addKey(KeyType key) {
		this.keys.add(key);
	}

	/**
	 * Gets the instant messaging handles.
	 * <p>
	 * vCard property name: IMPP
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the instant messaging handles
	 */
	public List<ImppType> getImpps() {
		return impps;
	}

	/**
	 * Adds an instant messaging handle.
	 * <p>
	 * vCard property name: IMPP
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param impp the instant messaging handle to add
	 */
	public void addImpp(ImppType impp) {
		this.impps.add(impp);
	}

	/**
	 * Gets a list of people that the person is related to.
	 * <p>
	 * vCard property name: RELATION
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the person's relations
	 */
	public List<RelatedType> getRelations() {
		return relations;
	}

	/**
	 * Adds someone that the person is related to.
	 * <p>
	 * vCard property name: RELATION
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param related the relation to add
	 */
	public void addRelated(RelatedType related) {
		this.relations.add(related);
	}

	/**
	 * Gets the languages that the person speaks.
	 * <p>
	 * vCard property name: LANG
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the languages
	 */
	public List<LanguageType> getLanguages() {
		return languages;
	}

	/**
	 * Adds a language that the person speaks.
	 * <p>
	 * vCard property name: LANG
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param language the language to add
	 */
	public void addLanguage(LanguageType language) {
		this.languages.add(language);
	}

	/**
	 * Gets the URIs that can be used to schedule a meeting with the person on
	 * his or her calendar.
	 * <p>
	 * vCard property name: CALENDRURI
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the calendar request URIs
	 */
	public List<CalendarRequestUriType> getCalendarRequestUris() {
		return calendarRequestUris;
	}

	/**
	 * Adds a URI that can be used to schedule a meeting with the person on his
	 * or her calendar.
	 * <p>
	 * vCard property name: CALENDRURI
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param calendarRequestUri the calendar request URI to add
	 */
	public void addCalendarRequestUri(CalendarRequestUriType calendarRequestUri) {
		this.calendarRequestUris.add(calendarRequestUri);
	}

	/**
	 * Gets the URIs that point to the person's calendar.
	 * <p>
	 * vCard property name: CALURI
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the calendar URIs
	 */
	public List<CalendarUriType> getCalendarUris() {
		return calendarUris;
	}

	/**
	 * Adds a URI that point to the person's calendar.
	 * <p>
	 * vCard property name: CALURI
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param calendarUri the calendar URI to add
	 */
	public void addCalendarUri(CalendarUriType calendarUri) {
		this.calendarUris.add(calendarUri);
	}

	/**
	 * Gets the URLs that can be used to determine when the person is free
	 * and/or busy.
	 * <p>
	 * vCard property name: FBURL
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the free-busy URLs
	 */
	public List<FbUrlType> getFbUrls() {
		return fbUrls;
	}

	/**
	 * Adds a URL that can be used to determine when the person is free and/or
	 * busy.
	 * <p>
	 * vCard property name: FBURL
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param fbUrl the free-busy URL to add
	 */
	public void addFbUrl(FbUrlType fbUrl) {
		this.fbUrls.add(fbUrl);
	}

	/**
	 * Gets the properties that are used to assign globally-unique identifiers
	 * to individual property instances. CLIENTPIDMAPs are used for merging
	 * together different versions of the same vCard.
	 * <p>
	 * vCard property name: CLIENTPIDMAP
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the client PID maps
	 */
	public List<ClientPidMapType> getClientPidMaps() {
		return clientPidMaps;
	}

	/**
	 * Adds a property that is used to assign a globally-unique identifier to an
	 * individual property instance. CLIENTPIDMAPs are used for merging together
	 * different versions of the same vCard.
	 * <p>
	 * vCard property name: CLIENTPIDMAP
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param clientPidMap the client PID map to add
	 */
	public void addClientPidMap(ClientPidMapType clientPidMap) {
		this.clientPidMaps.add(clientPidMap);
	}

	/**
	 * Gets any XML data that is attached to the vCard. XML properties may be
	 * present if the vCard was encoded in XML and the XML contained
	 * non-standard elements. The XML properties in this case would contain all
	 * of the non-standard XML elements.
	 * <p>
	 * vCard property name: XML
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the XML data
	 */
	public List<XmlType> getXmls() {
		return xmls;
	}

	/**
	 * Adds XML data to the vCard. XML properties may be present if the vCard
	 * was encoded in XML and the XML contained non-standard elements. The XML
	 * properties in this case would contain all of the non-standard XML
	 * elements.
	 * <p>
	 * vCard property name: XML
	 * </p>
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param xml the XML data to add
	 */
	public void addXml(XmlType xml) {
		this.xmls.add(xml);
	}

	/**
	 * Adds an extended type to the vCard.
	 * @param type the extended type to add
	 */
	public void addExtendedType(VCardType type) {
		extendedTypes.put(type.getTypeName(), type);
	}

	/**
	 * Gets all extended types that have the given name.
	 * @param typeName the type name
	 * @return the extended types or empty list if none were found
	 */
	public List<RawType> getExtendedType(String typeName) {
		List<VCardType> types = extendedTypes.get(typeName);
		List<RawType> list = new ArrayList<RawType>(types.size());
		for (VCardType type : types) {
			if (type instanceof RawType) {
				RawType rt = (RawType) type;
				list.add(rt);
			}
		}
		return list;
	}

	/**
	 * Gets all extended types that have been unmarshalled into an extended type
	 * class.
	 * @param clazz the class that the extended type was unmarshalled into
	 * @return the extended types or empty list of none were found
	 */
	@SuppressWarnings("unchecked")
	public <T extends VCardType> List<T> getExtendedType(Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		for (VCardType type : extendedTypes.values()) {
			if (clazz.isInstance(type)) {
				list.add((T) type);
			}
		}
		return list;
	}

	/**
	 * Gets all of the extended types.
	 * @return all of the extended types. The key is the type name and the value
	 * is the list of type objects.
	 */
	public ListMultimap<String, VCardType> getExtendedTypes() {
		return extendedTypes;
	}
}