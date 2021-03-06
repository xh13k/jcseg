package org.lionsoul.jcseg.dic;

import java.io.Serializable;
import java.util.HashMap;
//import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.lionsoul.jcseg.segmenter.Word;

/**
 * Dictionary class
 * 
 * @author	chenxin<chenxin619315@gmail.com>
 */
public class HashMapDictionary extends ADictionary implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**hash table for the words*/
    public final Map<String, IWord>[] dics;
    
    @SuppressWarnings("unchecked")
    public HashMapDictionary( SegmenterConfig config, Boolean sync )
    {
        super(config, sync);
        
        dics = new Map[ILexicon.T_LEN];
        if ( this.sync ) {
            for ( int j = 0; j < ILexicon.T_LEN; j++ ) {
                dics[j] = new ConcurrentHashMap<>(16, 0.80F);
            }
        } else {
            for ( int j = 0; j < ILexicon.T_LEN; j++ ) {
                dics[j] = new HashMap<>(16, 0.80F);
            }
        }
    }
    
    /**
     * @see ADictionary#match(int, String)
     */
    @Override
    public boolean match(int t, String key)
    {
        if ( t >= 0 && t < ILexicon.T_LEN ) {
            return dics[t].containsKey(key);
        }
        return false;
    }
     
    /**
     * @see ADictionary#add(int, IWord) 
    */
    @Override
    public IWord add(int t, IWord word)
    {
        if ( t >= 0 && t < ILexicon.T_LEN ) {
            if ( dics[t].containsKey(word.getValue()) ) {
                return dics[t].get(word.getValue());
            }
            
            dics[t].put(word.getValue(), word);
            return word;
        }
        
        return null;
    }

    /**
     * @see ADictionary#add(int, String, int, int, String[]) 
    */
    @Override
    public IWord add(int t, String key, int fre, int type, String entity[])
    {
        if ( t >= 0 && t < ILexicon.T_LEN ) {
            if ( dics[t].containsKey(key) ) {
                return dics[t].get(key);
            }
            
            IWord word = new Word(key, fre, type, entity);
            dics[t].put(key, word);
            return word;
        }
        
        return null;
    }
    
    /**
     * @see ADictionary#add(int, String, int) 
    */
    @Override
    public IWord add(int t, String key, int type)
    {
        return add(t, key, 0, type, null);
    }

    /**
     * @see ADictionary#add(int, String, int, int) 
    */
    @Override
    public IWord add(int t, String key, int fre, int type)
    {
        return add(t, key, fre, type, null);
    }

    /**
     * @see ADictionary#add(int, String, int, String[]) 
    */
    @Override
    public IWord add(int t, String key, int type, String entity[])
    {
        return add(t, key, 0, type, entity);
    }

    /**
     * @see ADictionary#get(int, String) 
    */
    @Override
    public IWord get(int t, String key)
    {
        if ( t >= 0 && t < ILexicon.T_LEN ) {
            return dics[t].get(key);
        }
        return null;
    }

    /**
     * @see ADictionary#remove(int, String) 
    */
    @Override
    public void remove(int t, String key)
    {
        if ( t >= 0 && t < ILexicon.T_LEN ) {
            dics[t].remove(key);
        }
    }
    
    /**
     * @see ADictionary#size(int) 
    */
    @Override
    public int size(int t)
    {
        if ( t >= 0 && t < ILexicon.T_LEN ) {
            return dics[t].size();
        }
        return 0;
    }
    
}
