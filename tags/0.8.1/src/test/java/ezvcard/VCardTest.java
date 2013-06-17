package ezvcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import ezvcard.io.CompatibilityMode;
import ezvcard.types.HasAltId;
import ezvcard.types.NoteType;
import ezvcard.types.RawType;
import ezvcard.types.RevisionType;
import ezvcard.types.VCardType;

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
public class VCardTest {
	@Test
	public void getAllTypes() {
		VCard vcard = new VCard();

		//type stored in VCardType variable
		RevisionType rev = RevisionType.now();
		vcard.setRevision(rev);

		//type stored in a List
		NoteType note = new NoteType("A note.");
		vcard.addNote(note);

		//extended type with unique name
		RawType xGender = vcard.addExtendedType("X-GENDER", "male");

		//extended types with same name
		RawType xManager1 = vcard.addExtendedType("X-MANAGER", "Michael Scott");
		RawType xManager2 = vcard.addExtendedType("X-MANAGER", "Pointy Haired Boss");

		Collection<VCardType> types = vcard.getAllTypes();
		assertEquals(5, types.size());
		assertTrue(types.contains(rev));
		assertTrue(types.contains(note));
		assertTrue(types.contains(xGender));
		assertTrue(types.contains(xManager1));
		assertTrue(types.contains(xManager2));
	}

	@Test
	public void getAllTypes_none() {
		VCard vcard = new VCard();
		vcard.setVersion(VCardVersion.V2_1); //no type class is returned for VERSION
		assertTrue(vcard.getAllTypes().isEmpty());
	}

	@Test
	public void addExtendedType_raw() {
		VCard vcard = new VCard();
		RawType type = vcard.addExtendedType("NAME", "value");
		assertEquals("NAME", type.getTypeName());
		assertEquals("value", type.getValue());
		assertEquals(Arrays.asList(type), vcard.getExtendedType("NAME"));
	}

	@Test
	public void addExtendedType_type_class() {
		VCard vcard = new VCard();
		VCardTypeImpl type = new VCardTypeImpl("NAME");
		vcard.addExtendedType(type);
		assertEquals(Arrays.asList(type), vcard.getExtendedType(type.getClass()));
	}

	@Test
	public void addAltId() {
		HasAltIdImpl one1 = new HasAltIdImpl("1");
		HasAltIdImpl null1 = new HasAltIdImpl(null);
		Collection<HasAltIdImpl> existing = new ArrayList<HasAltIdImpl>();
		existing.add(one1);
		existing.add(null1);

		HasAltIdImpl two1 = new HasAltIdImpl(null);
		HasAltIdImpl two2 = new HasAltIdImpl(null);
		Collection<HasAltIdImpl> altRepresentations = new ArrayList<HasAltIdImpl>();
		altRepresentations.add(two1);
		altRepresentations.add(two2);

		VCard.addAlt(existing, altRepresentations);

		Collection<HasAltIdImpl> expected = Arrays.asList(one1, null1, two1, two2);
		assertEquals(expected, existing);
		assertEquals("2", two1.altId);
		assertEquals("2", two2.altId);
	}

	@Test
	public void generateAltId() {
		Collection<HasAltId> list = new ArrayList<HasAltId>();
		list.add(new HasAltIdImpl("1"));
		list.add(new HasAltIdImpl("1"));
		list.add(new HasAltIdImpl("2"));
		assertEquals("3", VCard.generateAltId(list));

		list = new ArrayList<HasAltId>();
		list.add(new HasAltIdImpl("1"));
		list.add(new HasAltIdImpl("1"));
		list.add(new HasAltIdImpl("3"));
		assertEquals("2", VCard.generateAltId(list));

		list = new ArrayList<HasAltId>();
		list.add(new HasAltIdImpl("2"));
		list.add(new HasAltIdImpl("2"));
		list.add(new HasAltIdImpl("3"));
		assertEquals("1", VCard.generateAltId(list));

		list = new ArrayList<HasAltId>();
		assertEquals("1", VCard.generateAltId(list));

		list = new ArrayList<HasAltId>();
		list.add(new HasAltIdImpl("one"));
		list.add(new HasAltIdImpl("one"));
		list.add(new HasAltIdImpl("three"));
		assertEquals("1", VCard.generateAltId(list));
	}

	@Test
	public void groupByAltId() {
		HasAltIdImpl one1 = new HasAltIdImpl("1");
		HasAltIdImpl null1 = new HasAltIdImpl(null);
		HasAltIdImpl two1 = new HasAltIdImpl("2");
		HasAltIdImpl one2 = new HasAltIdImpl("1");
		HasAltIdImpl null2 = new HasAltIdImpl(null);
		Collection<HasAltIdImpl> list = Arrays.asList(one1, null1, one2, two1, null2);

		//@formatter:off
		@SuppressWarnings("unchecked")
		List<List<HasAltIdImpl>> expected = Arrays.asList(
			Arrays.asList(one1, one2),
			Arrays.asList(two1),
			Arrays.asList(null1),
			Arrays.asList(null2)
		);
		//@formatter:on
		assertEquals(expected, VCard.groupByAltId(list));
	}

	@Test
	public void groupByAltId_empty() {
		Collection<HasAltIdImpl> list = new ArrayList<HasAltIdImpl>();

		//@formatter:off
		@SuppressWarnings("unchecked")
		List<List<HasAltIdImpl>> expected = Arrays.asList(
		);
		//@formatter:on
		assertEquals(expected, VCard.groupByAltId(list));
	}

	private class HasAltIdImpl implements HasAltId {
		private String altId;

		public HasAltIdImpl(String altId) {
			this.altId = altId;
		}

		//@Overrides
		public String getAltId() {
			return altId;
		}

		//@Overrides
		public void setAltId(String altId) {
			this.altId = altId;
		}
	}

	private class VCardTypeImpl extends VCardType {
		public VCardTypeImpl(String typeName) {
			super(typeName);
		}

		@Override
		protected void doMarshalText(StringBuilder value, VCardVersion version, List<String> warnings, CompatibilityMode compatibilityMode) {
			//empty
		}

		@Override
		protected void doUnmarshalText(String value, VCardVersion version, List<String> warnings, CompatibilityMode compatibilityMode) {
			//empty
		}
	}
}