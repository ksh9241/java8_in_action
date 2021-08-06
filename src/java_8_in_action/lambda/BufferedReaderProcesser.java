package java_8_in_action.lambda;

import java.io.BufferedReader;
import java.io.IOException;

@FunctionalInterface
public interface BufferedReaderProcesser {
	String process(BufferedReader b) throws IOException;
}
