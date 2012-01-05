package com.codepoetics.localtruth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.collect.Range;

public final class ARangeSet {
    public static Matcher<RangeSet<Integer>> withTheRanges(final Range<Integer>...rangeArgs) {
		return new TypeSafeMatcher<RangeSet<Integer>>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("A multirange containing the ranges ");
				description.appendValueList("[", ",", "]", rangeArgs);
			}
			
			@Override
			public boolean matchesSafely(RangeSet<Integer> item) {
				return Matchers.<Range<Integer>>hasItems(rangeArgs).matches(item.ranges());
			}
		};
    }
}