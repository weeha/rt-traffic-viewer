package model.traffic;

import com.google.protobuf.ByteString;

/**
 * Created by fnoack on 17.08.2017.
 */
public class FlowAnalysisElemImpl extends AnalysisElemImpl {

    private ByteString rawBytes;

    public FlowAnalysisElemImpl(ProtobufTrafficFlowV5.TrafficFlow flow){
        super();
        rawBytes = flow.getLocation().getOpenlr();
    }

    public boolean matches(ByteString rawBytes){
        return this.rawBytes.equals(rawBytes);
    }
}
