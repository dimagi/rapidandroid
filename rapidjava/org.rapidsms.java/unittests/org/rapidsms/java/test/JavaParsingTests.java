/**
 * 
 */
package org.rapidsms.java.test;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import junit.framework.TestCase;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */
public class JavaParsingTests extends TestCase {

	String[] goodMessages = { "bednets nyc 100 30 80",
			"bednets lax 1,500, 750 1000", "nutrition 12345 20 100 0.6 5 y n",
			"nutrition 55446 20kg 100cm 60% 5cm yes no",
			"nutrition 55446 21 kg 100cm 60% 5cm yes no",
			"nutrition 55446 2 kg 100 m 3/4 5cm yes no" };

	String[] problematic = { "bednets nyc 100 30", "bednets lax 1,500",
			"bednets", "bednets 100 200 300", "bednets 100 200 300 400",
			"bednets BOS 100 200 300 12321", "bednets 100 BOS 100 200 120",
			"bednets 100 BOS 100 200 120 51231",
			"bednetsBOS 100 200 120 51231",

			"nutrition asdfsadf 12345 20 100 0.6 5 y n",
			"nutrition 55446 20kg 100cm 60% 5cm yes no",
			"nutrition 55446 20kg 60% 5cm yes no",
			"nutrition 55446 21 100cm 60% 5cm yes no",
			"nutrition 55446 2 kg 100 m 5cm yes no"

	};

	String[] badMessages = { "bednwafasd asd2 12983 klasd12 ajds",
			"nutritiasndfqwer asd2 12983 klasd12 ajds", "aklsjdfl234",
			"bedntestgklajsdljwler", "nutritionaslkdfklwer" };
	
	Vector<String> regexes;
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		regexes.clear();
	}

	private final int BOOL = 0;
	private final int HEIGHT = 1;
	private final int RATIO = 2;
	private final int LENGTH = 3;
	private final int WEIGHT = 4;
	private final int NUMBER = 5;
	private final int WORD = 6;
	
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		
		regexes = new Vector<String>();
		regexes.add("^(t|f|true|false|y|no|yes|n|n0)(\\s|$)");	//bool
		regexes.add("^(\\d+)(\\s*(cm|mm|m|meter|meters))($|\\s)");	//height
		regexes.add("^(\\d+\\:\\d+)|(\\d+\\/\\d+)|(\\d+\\s*%)|(\\d+\\s*pct)");//ratio
		regexes.add("^(\\d+)(\\s*(cm|m))($|\\s)");	//length
		regexes.add("^(\\d+)(\\s*(kg|kilo|kilos))($|\\s)");	//weight
		regexes.add("^(\\d+)($|\\s)");	//number	
		regexes.add("^([A-Za-z]+)($|\\s)");//word
		//System.out.println("Setting up regexes " + regexes.size());
	}

	public void testLenghts() {
				
//		System.out.println("Begin test:  Lengths");
//
//
//		doParse("goodMessages", goodMessages, regexes.get(LENGTH));
//		doParse("problematic", problematic, regexes.get(LENGTH));
//		doParse("badMessages", badMessages, regexes.get(LENGTH));

	}

	public void testNumeric() {
		
		
//		System.out
//				.println("\n\n#########################Begin test:  Numeric########################################");
//
//		doParse("goodMessages", goodMessages, regexes.get(this.NUMBER));
//		doParse("problematic", problematic, regexes.get(NUMBER));
//		doParse("badMessages", badMessages, regexes.get(NUMBER));
	}
	
	public void testSubtractive() {	
		
		
		System.out.println("######################################Begin Subtractive test##################################");

		String[] messages = goodMessages;
		for (int i = 0; i < messages.length; i++) {
			String messageToParse = messages[i];
			String parsedMessage = "";
			System.out.println("\n\n\n\t*** Begin subtractive parse: " + messageToParse);
		

			while (true) {
				String justParsedMessage = subtractiveParse(messageToParse,
						regexes.get(this.WEIGHT));
				if (justParsedMessage.equals(messageToParse)) {
					break;

				} else {
					messageToParse = justParsedMessage + "";
					System.out.println("\tIterate subtractive parse: "
							+ messageToParse);
				}
			}

			System.out.println("\t*** End subtractive parse for message: " + i);
		}
	}

	public String subtractiveParse(String fragmentToParse, String regex) {
		
		Pattern mPattern;
		mPattern = Pattern.compile(regex);
		Matcher matcher = mPattern.matcher(fragmentToParse);
		System.out.println("\tMatching regex: " + regex);
		boolean isMatched = matcher.find();
		int maxSize = -1;
		int maxGroup = -1;
		int minstart = 0;
		int maxend = 0;
		
		if (isMatched) {
//			while (isMatched) {
				for (int q = 0; q < matcher.groupCount(); q++) {

					int currsize = matcher.group(q).length(); // matcher.end(q)
																// -
																// matcher.start(q);

					if (currsize > maxSize) {
						maxGroup = q;
						maxSize = currsize;
					}
				}
//				isMatched = matcher.find();
//			}
			minstart = matcher.start(maxGroup);
			maxend = matcher.end(maxGroup);
			System.out.println(matcher.group(maxGroup));
		} else {
			//System.out.println("\t\tNo match!");
		}
		
		if(minstart < maxend) {
			System.out.println("\t\tFragmenting: " + minstart + "-" + maxend);
			String parsed = fragmentToParse.substring(minstart, maxend);
			
			if(parsed.charAt(parsed.length()-1) == ' ') {
				parsed.trim();				
				System.out.println("trim!");
				maxend = maxend - 1;
			}
			System.out.println("\t\tMatched fragment: ##" + parsed + "##");
			if(minstart == 0) {
				minstart = 1;
			}
			String ret = fragmentToParse.subSequence(0, minstart) + fragmentToParse.substring(maxend);
			
			return ret;
		} else {
			return fragmentToParse;
		}
	}

	private void doParse(String testname, String[] messages,
			String regex) {
		System.out.println("###############  Do Parse: " + testname
				+ " ###################\n\n");

		for (int i = 0; i < messages.length; i++) {
			System.out.println("Matching message: " + messages[i]);

			Pattern mPattern;
			mPattern = Pattern.compile(regex);
			Matcher matcher = mPattern.matcher(messages[i]);
			System.out.println("\tMatching regex: " + regex);
			boolean isMatched = matcher.find();
			if (isMatched) {
				while (isMatched) {

					System.out.println("\t\t**********************");

					for (int q = 0; q < matcher.groupCount(); q++) {
						System.out.println("\t\tRegion:" + matcher.start(q)
								+ "-" + matcher.end(q));
						System.out.println("\t\t\tgroup: " + q + "  ##"
								+ matcher.group(q) + "##");
					}
					System.out.println("\t\t**********************");
					isMatched = matcher.find();
				}
			} else {
				System.out.println("\t\tNo match!");
			}

		}
	}

}
