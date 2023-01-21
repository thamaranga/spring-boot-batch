package com.hasithat.springbootbatch.config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;


public class CustomSkipPolicy  implements SkipPolicy {

    /*While processing data if we get a NumberFormatException , NullPointerException
    * then skip that record and continue with other data*/
    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        if(throwable instanceof  NumberFormatException){
            return true;
        }else if(throwable instanceof  NullPointerException){
            return true;
        }
        return false;
    }
}
