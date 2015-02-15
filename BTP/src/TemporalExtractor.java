import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;

public class TemporalExtractor {
	
	static AnnotationPipeline pipeline = new AnnotationPipeline();
    
	static{
		Properties props = new Properties();
    	pipeline.addAnnotator(new TokenizerAnnotator(false));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
        pipeline.addAnnotator(new POSTaggerAnnotator(false));
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
	}
	
	public static List<SUTime.Temporal> extract(String text,String docDate) {
		List<SUTime.Temporal> retVal = new ArrayList<SUTime.Temporal>();
		Annotation annotation = new Annotation(text);
		annotation.set(CoreAnnotations.DocDateAnnotation.class, docDate);
		pipeline.annotate(annotation);
		System.out.println(annotation.get(CoreAnnotations.TextAnnotation.class));
		List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
		for (CoreMap cm : timexAnnsAll) {
//		    List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
		    retVal.add(cm.get(TimeExpression.Annotation.class).getTemporal());
//		    System.out.println(cm + " [from char offset " +
//			tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
//			" to " + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
//			" --> " + cm.get(TimeExpression.Annotation.class).getTemporal());
		}
		return retVal;
   }
}
