package model.location;

/**
 * Created by flori on 04.05.2017.
 */

import openlr.binary.data.AbstractLRP;

/**
 * Created by Florian Noack on 04.05.2017.
 */
public class LastLocationReferencePoint extends LocationReferencePointImpl{

    public LastLocationReferencePoint(AbstractLRP lrp){
        super(lrp);
    }

    public LastLocationReferencePoint(AbstractLRP lrp, AbstractLRP prevLrp){
        super(lrp, prevLrp);
    }
}