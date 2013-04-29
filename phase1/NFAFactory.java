package phase1;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class NFAFactory
{
	LinkedHashMap<String, String> regexTable;
	LinkedHashMap<String, String> tokenTable;

	// Will take in a table
	public NFAFactory(LinkedHashMap<String, String> regexTable, LinkedHashMap<String, String> tokenTable)
	{
		this.regexTable = regexTable;
		this.tokenTable = tokenTable;
	}

	public LinkedHashSet<NFA> factorize()
	{
		LinkedHashSet<NFA> regexNFAs = new LinkedHashSet<NFA>();
		Set<String> keys1 = regexTable.keySet();
		for(String key : keys1)
		{
			String value = regexTable.get(key);
			NFACreator create = new NFACreator(key, value, regexTable, null);
			regexNFAs.add(create.getNFA());
		}
		
		LinkedHashSet<NFA> nfaSet = new LinkedHashSet<NFA>();
		Set<String> keys2 = tokenTable.keySet();
		for(String key : keys2)
		{
			String value = tokenTable.get(key);
			NFACreator create = new NFACreator(key, value, regexTable, regexNFAs);
			nfaSet.add(create.getNFA());
		}
		return nfaSet;
	}
}