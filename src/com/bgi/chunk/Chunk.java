package com.bgi.chunk;

import java.io.InputStream;
import java.util.List;
import java.io.IOException;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import com.bgi.util.ChunkUtil;
import com.bgi.util.SpeechUtil;
import com.bgi.util.SystemConfig;
 

public class Chunk {
	public static ChunkerME chunker = null;
	public static POSTaggerME posTagger = null;
	
	static {
		try {
			initModel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void initModel() throws IOException {
		InputStream posModelIn = Chunk.class.getClassLoader().getResourceAsStream(SystemConfig.POS);
        // loading the parts-of-speech model from stream
        POSModel posModel = new POSModel(posModelIn);
        // initializing the parts-of-speech tagger with model
        posTagger = new POSTaggerME(posModel);
		
        // reading the chunker model
        InputStream ins = Chunk.class.getClassLoader().getResourceAsStream(SystemConfig.CHUNKER);
        // loading the chunker model
        ChunkerModel chunkerModel = new ChunkerModel(ins);
        // initializing chunker(maximum entropy) with chunker model
        chunker = new ChunkerME(chunkerModel);
        
        posModelIn.close();
        ins.close();
	}
	
	
	public static List<String>getDataChunking(String text) {
		String[] tokens = SpeechUtil.tokenizer(text);
		String[] tags = posTagger.tag(tokens);
		String[] chunks = chunker.chunk(tokens, tags);
//		for (String e: tags) {
//			System.out.println("tag: " + e);
//		}
		return ChunkUtil.getDataChunk(chunks, tags, tokens);
		
	}
	
	
    public static void main(String[] args){
    	String text = "Find out all research literature on health";
        List<String> ret = getDataChunking(text);
        
        int size = ret.size();
        for (int i = 0; i < size; i++) {
        	System.out.println("data : " + ret.get(i));
        }
    }

}
