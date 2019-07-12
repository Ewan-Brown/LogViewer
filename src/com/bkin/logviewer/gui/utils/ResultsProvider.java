package com.bkin.logviewer.gui.utils;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.common.filters.Filter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: asaunders
 * Date: 2017-Jan-12
 * Time: 10:39 AM
 *
 * The purpose of this class is to filter and search through a result set and provide a copy
 * of that filtered / searched result set. Normally you would use a JTable and either search or filter
 * it using built in methods. However, I've found this incredibly awkward to use.
 *
 * Ideally, you just filter / search and provide the list to the JTable to handle the data normally at that point.
 */
public class ResultsProvider
{
	private static int NEXTID = -1;

	public static int getNextID()
	{
		NEXTID += 1;
		return NEXTID;
	}

	static class FilterWrapper
	{
		FilterWrapper(List<Filter> l){
			this.f = l;
		}
		List<Filter> f;
		boolean isAllowed(Line l){
			return f.stream().allMatch(filter -> filter.isAllowed(l));
		}
	}

	public static List<Line> filterList(List<Line> lines, List<Filter> filters)
	{
		FilterWrapper fw = new FilterWrapper(filters);
		return lines.stream().filter(fw::isAllowed).collect(Collectors.toList());
	}

	// http://stackoverflow.com/questions/86780/how-to-check-if-a-string-contains-another-string-in-a-case-insensitive-manner-in/25379180#25379180
	// supposedly x4 Faster way to figure out if a string contains another string, case insensitive (as opposed to lowercase / contains or regex)
	public static boolean containsIgnoreCase(String src, String what) {
		final int length = what.length();
		if (length == 0)
			return true; // Empty string is contained

		final char firstLo = Character.toLowerCase(what.charAt(0));
		final char firstUp = Character.toUpperCase(what.charAt(0));

		for (int i = src.length() - length; i >= 0; i--) {
			// Quick check before calling the more expensive regionMatches() method:
			final char ch = src.charAt(i);
			if (ch != firstLo && ch != firstUp)
				continue;

			if (src.regionMatches(true, i, what, 0, length))
				return true;
		}

		return false;
	}

	public static int findNextStringIndex(List<Line> lines, int lastIndex, String searchValue)
	{
		int result = -1;

		if (searchValue == null || searchValue.isEmpty())
			return result;

		for (int x = lastIndex + 1; x < lines.size(); x ++)
		{
			if (ResultsProvider.containsIgnoreCase(lines.get(x).getText(), searchValue))
			{
				return x;
			}
		}

		return result;
	}

	public static int findPreviousStringIndex(List<Line> lines, int lastIndex, String searchValue)
	{
		int result = -1;

		if (searchValue == null || searchValue.isEmpty())
			return result;

		for (int x = lastIndex - 1; x >= 0; x --)
		{
			if (ResultsProvider.containsIgnoreCase(lines.get(x).getText(), searchValue))
			{
				return x;
			}
		}

		return result;
	}
}