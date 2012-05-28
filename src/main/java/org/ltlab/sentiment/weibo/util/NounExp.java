package org.ltlab.sentiment.weibo.util;

public class NounExp
{
    private String noun;
    private long posHit;
    private long negHit;
    private NounExpType expectation;
    
    public NounExp(String noun, long posHit, long negHit, 
                   NounExpType expectation) {
        this.noun = noun;
        this.posHit = posHit;
        this.negHit = negHit;
        this.expectation = expectation;
    }
    
    public String getNoun()
    {
        return noun;
    }

    public long getPosHit()
    {
        return posHit;
    }

    public long getNegHit()
    {
        return negHit;
    }

    public NounExpType getExpectation()
    {
        return expectation;
    }
    
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append(noun).append("\t").append(expectation).append("\t")
            .append(posHit).append("\t").append(negHit);
        return sb.toString();
    }
}
