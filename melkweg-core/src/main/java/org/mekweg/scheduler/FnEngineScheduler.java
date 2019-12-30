package org.mekweg.scheduler;

import org.mekweg.handle.Handler;
import org.mekweg.param.ParamWrapper;

import java.util.List;

public class FnEngineScheduler implements Scheduler {

    @Override
    public ParamWrapper run(ParamWrapper paramWrapper, List<Handler> handlerList) {
        if (handlerList == null || handlerList.size() == 0) {
            return paramWrapper;
        }
        for (Handler handler : handlerList) {
            switch (handler.getType()){
                case SAMPLE_HANDLE:
                    paramWrapper = handler.handle(paramWrapper);
                    break;
                case REORDER_HANDLE:
                case CONDITION_HANDLE:
                    handler.setScheduler(this);
                    paramWrapper = handler.handle(paramWrapper);
                    break;
                default:
                    break;
            }
        }
        return paramWrapper;
    }

}
