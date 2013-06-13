package ezvcard.parameters;

import ezvcard.types.TelephoneType;

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
 * Represents the TYPE parameter of the {@link TelephoneType} type.
 * <p>
 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
 * </p>
 * @author Michael Angstadt
 */
public class TelephoneTypeParameter extends TypeParameter {
	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final TelephoneTypeParameter BBS = new TelephoneTypeParameter("bbs");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final TelephoneTypeParameter CAR = new TelephoneTypeParameter("car");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter CELL = new TelephoneTypeParameter("cell");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter FAX = new TelephoneTypeParameter("fax");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter HOME = new TelephoneTypeParameter("home");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final TelephoneTypeParameter ISDN = new TelephoneTypeParameter("isdn");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final TelephoneTypeParameter MODEM = new TelephoneTypeParameter("modem");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final TelephoneTypeParameter MSG = new TelephoneTypeParameter("msg");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter PAGER = new TelephoneTypeParameter("pager");

	/**
	 * <b>Supported versions:</b> <code>3.0</code>
	 */
	public static final TelephoneTypeParameter PCS = new TelephoneTypeParameter("pcs");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final TelephoneTypeParameter PREF = new TelephoneTypeParameter("pref");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final TelephoneTypeParameter TEXT = new TelephoneTypeParameter("text");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final TelephoneTypeParameter TEXTPHONE = new TelephoneTypeParameter("textphone");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter VIDEO = new TelephoneTypeParameter("video");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter VOICE = new TelephoneTypeParameter("voice");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final TelephoneTypeParameter WORK = new TelephoneTypeParameter("work");

	/**
	 * Use of this constructor is discouraged and should only be used for
	 * defining non-standard TYPEs. Please use one of the predefined static
	 * objects.
	 * @param value the type value (e.g. "home")
	 */
	public TelephoneTypeParameter(String value) {
		super(value);
	}

	/**
	 * Searches the static objects in this class for one that has a certain type
	 * value.
	 * @param value the type value to search for (e.g. "work")
	 * @return the object or null if not found
	 */
	public static TelephoneTypeParameter valueOf(String value) {
		return findByValue(value, TelephoneTypeParameter.class);
	}
}
