package ezvcard.parameters;

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
 * Represents a VALUE parameter.
 * @author Michael Angstadt
 */
public class ValueParameter extends VCardParameter {
	public static final String NAME = "VALUE";

	/**
	 * <b>Supported versions:</b> <code>2.1 (p.18-9)</code>
	 */
	public static final ValueParameter URL = new ValueParameter("url");

	/**
	 * <b>Supported versions:</b> <code>2.1 (p.8-9)</code>
	 */
	public static final ValueParameter CONTENT_ID = new ValueParameter("content-id");

	/**
	 * <b>Supported versions:</b> <code>3.0</code>
	 */
	public static final ValueParameter BINARY = new ValueParameter("binary");

	/**
	 * <b>Supported versions:</b> <code>3.0, 4.0</code>
	 */
	public static final ValueParameter URI = new ValueParameter("uri");

	/**
	 * <b>Supported versions:</b> <code>3.0, 4.0</code>
	 */
	public static final ValueParameter TEXT = new ValueParameter("text");

	/**
	 * <b>Supported versions:</b> <code>3.0, 4.0</code>
	 */
	public static final ValueParameter DATE = new ValueParameter("date");

	/**
	 * <b>Supported versions:</b> <code>3.0, 4.0</code>
	 */
	public static final ValueParameter TIME = new ValueParameter("time");

	/**
	 * <b>Supported versions:</b> <code>3.0, 4.0</code>
	 */
	public static final ValueParameter DATE_TIME = new ValueParameter("date-time");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter DATE_AND_OR_TIME = new ValueParameter("date-and-or-time");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter TIMESTAMP = new ValueParameter("timestamp");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter BOOLEAN = new ValueParameter("boolean");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter INTEGER = new ValueParameter("integer");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter FLOAT = new ValueParameter("float");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter UTC_OFFSET = new ValueParameter("utc-offset");

	/**
	 * <b>Supported versions:</b> <code>4.0</code>
	 */
	public static final ValueParameter LANGUAGE_TAG = new ValueParameter("language-tag");

	/**
	 * Use of this constructor is discouraged and should only be used for
	 * defining non-standard VALUEs. Please use one of the predefined static
	 * objects.
	 * @param value the type value (e.g. "uri")
	 */
	public ValueParameter(String value) {
		super(NAME, value);
	}

	/**
	 * Searches the static objects in this class for one that has a certain type
	 * value.
	 * @param value the type value to search for (e.g. "text")
	 * @return the object or null if not found
	 */
	public static ValueParameter valueOf(String value) {
		return findByValue(value, ValueParameter.class);
	}
}