package ezvcard;

/**
 * Copyright 2011 George El-Haddad. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY GEORGE EL-HADDAD ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GEORGE EL-HADDAD OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of George El-Haddad.
 */

/**
 * Contains all possible vCard versions.
 * @author George El-Haddad
 * @author Michael Angstadt
 */
public enum VCardVersion {
	V2_1("2.1"), V3_0("3.0"), V4_0("4.0");

	private String version;

	/**
	 * @param version the text representation
	 */
	private VCardVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the text representation of this version.
	 * @return the text representation
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets a {@link VCardVersion} instance based on the given text
	 * representation.
	 * @param value the text representation
	 * @return the object or null if not found
	 */
	public static VCardVersion valueOfByStr(String value) {
		for (VCardVersion version : VCardVersion.values()) {
			if (version.getVersion().equals(value)) {
				return version;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return version;
	}
}
