package ezvcard.types.scribes;

import java.util.Date;

import ezvcard.types.DeathdateType;
import ezvcard.util.PartialDate;

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
 */

/**
 * Marshals {@link DeathdateType} properties.
 * @author Michael Angstadt
 */
public class DeathdateScribe extends DateOrTimePropertyScribe<DeathdateType> {
	public DeathdateScribe() {
		super(DeathdateType.class, "DEATHDATE");
	}

	@Override
	protected DeathdateType newInstance(String text) {
		DeathdateType property = new DeathdateType();
		property.setText(text);
		return property;
	}

	@Override
	protected DeathdateType newInstance(Date date, boolean hasTime) {
		DeathdateType property = new DeathdateType();
		property.setDate(date, hasTime);
		return property;
	}

	@Override
	protected DeathdateType newInstance(PartialDate partialDate) {
		DeathdateType property = new DeathdateType();
		property.setPartialDate(partialDate);
		return property;
	}
}
