package practicum.lucene.es;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class PracticumAnalyzer extends Analyzer {
	
	public static final Set<String> stop_word_set = new HashSet<String>(Arrays.asList( new String[]{"a","the","and","of","in", "be", "also", "as"}));

	public Collection<PracticumBean> process(String input){
		
		String[] sentences = input.split("\\.");
		Map<String, PracticumBean> map = new HashMap<String, PracticumBean>();
		
		for(int i = 0; i < sentences.length; i++) {

			TokenStream tokenStream = tokenStream(input, new StringReader(sentences[i]));
	        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
	        
            try {

            	while (tokenStream.incrementToken()) {
				    String word = charTermAttr.toString();
				    PracticumBean bean = map.get(word);
				    
				    if(bean == null) {
				    	bean = new PracticumBean(word);
				    	map.put(word, bean);
				    }
				    
				    bean.addOcurrence(i);
				}
            	
			} catch (IOException e) {
	            System.out.println(e.getMessage());
			}
	        
		}
		
		TreeSet<PracticumBean> result = new TreeSet<PracticumBean>();
		result.addAll(map.values());
        
		return result;
	}
	
	public String tokenizeStopStem(String input) {
		 
        TokenStream tokenStream = tokenStream(input, new StringReader(input));
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
        try{
            while (tokenStream.incrementToken()) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(charTermAttr.toString());
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return sb.toString();
	}

	@Override
	public TokenStream tokenStream(String input, Reader arg1) {
        TokenStream tokenStream = new StandardTokenizer(
                Version.LUCENE_36, arg1);
        tokenStream = new LowerCaseFilter(Version.LUCENE_36, tokenStream);
        tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, stop_word_set);
        tokenStream = new PorterStemFilter(tokenStream);
		return tokenStream;
	}
	
	public String proccessMapping(Collection<PracticumBean> results) {
		PracticumOutput output = new PracticumOutput();
		output.setResults(results);
		String jsonString = null;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(output);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
}
